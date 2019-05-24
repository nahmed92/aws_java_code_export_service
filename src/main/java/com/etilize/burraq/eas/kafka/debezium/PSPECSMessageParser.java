/*
 * #region
 * export-aggregation-service
 * %%
 * Copyright (C) 2018 Etilize
 * %%
 * NOTICE: All information contained herein is, and remains the property of ETILIZE.
 * The intellectual and technical concepts contained herein are proprietary to
 * ETILIZE and may be covered by U.S. and Foreign Patents, patents in process, and
 * are protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from ETILIZE. Access to the source code contained herein
 * is hereby forbidden to anyone except current ETILIZE employees, managers or
 * contractors who have executed Confidentiality and Non-disclosure agreements
 * explicitly covering such access.
 *
 * The copyright notice above does not evidence any actual or intended publication
 * or disclosure of this source code, which includes information that is confidential
 * and/or proprietary, and is a trade secret, of ETILIZE. ANY REPRODUCTION, MODIFICATION,
 * DISTRIBUTION, PUBLIC PERFORMANCE, OR PUBLIC DISPLAY OF OR THROUGH USE OF THIS
 * SOURCE CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF ETILIZE IS STRICTLY PROHIBITED,
 * AND IN VIOLATION OF APPLICABLE LAWS AND INTERNATIONAL TREATIES. THE RECEIPT
 * OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR
 * IMPLY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO
 * MANUFACTURE, USE, OR SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 * #endregion
 */

package com.etilize.burraq.eas.kafka.debezium;

import static com.etilize.burraq.eas.kafka.debezium.DebeziumMessageProperties.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.avro.generic.GenericData;

import com.etilize.burraq.eas.specification.UpdateProductSpecificationRequest;
import com.etilize.burraq.eas.specification.value.UnitAttribute;
import com.etilize.burraq.eas.specification.value.UnitValue;
import com.etilize.burraq.eas.specification.value.Value;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Class for parsing Debezium messages from PSPECS.
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class PSPECSMessageParser {

    private static final int THREE = 3;

    /**
     * Converts update specifications message from PSPECS into an instance of {@link UpdateProductSpecificationRequest}
     *
     * @param productId
     *            {@link String} productId
     * @param record
     *            {@link GenericData.Record}
     * @return {@link UpdateProductSpecificationRequest}
     */
    public UpdateProductSpecificationRequest getUpdateSpecificationRequest(
            final String productId, final GenericData.Record record) {
        final DebeziumMessageParser debeziumMessageParser = new DebeziumMessageParser();
        final String localeId = debeziumMessageParser.extractProductLocaleFromAddLocaleCommand(
                record) //
                .get();
        UpdateProductSpecificationRequest request = null;
        final JsonObject patch = extractPatchObject(record);

        // If not then it is a remove attribute operation
        if (isUpdateAttributeOperation(patch)) { // It is an update attribute operation
            request = getRequestForUpdateOperation(productId, localeId, patch);

        } else if (isRemoveAttributeOperation(patch)) { // It means it is remove attribute operation
            request = getRequestForRemoveAttributeOperation(productId, localeId, patch);
        }
        return request;
    }

    private UpdateProductSpecificationRequest getRequestForUpdateOperation(
            final String productId, final String localeId, final JsonObject patch) {
        final UpdateProductSpecificationRequest request = new UpdateProductSpecificationRequest(
                productId, localeId);
        final JsonObject setCommand = patch.get(SET) //
                .getAsJsonObject();
        final Iterator<String> keySetIt = setCommand.keySet() //
                .iterator();
        while (keySetIt.hasNext()) {// Iterating key sets to process individual operation
            final String key = keySetIt.next();
            final String attributeId = getAttributeIdFromKey(key);

            if (isUpdateNonRepeatbleOperation(setCommand, key)) { // It means update non-repeatable attribute
                updateRequestForNonRepeatableAttributes(request, setCommand, key,
                        attributeId);
            } else { // It means update repeatable attributes
                updateRequestForRepeatableAttributes(request, setCommand, key,
                        attributeId);
            }
        }
        return request;
    }

    private void updateRequestForNonRepeatableAttributes(
            final UpdateProductSpecificationRequest request, final JsonObject setCommand,
            final String key, final String attributeId) {
        final JsonObject entrySetValue = setCommand.get(key) //
                .getAsJsonObject();
        if (isUpdateUnitAttributeOperation(entrySetValue)) { // unit attribute
            final JsonObject unitAttributeObject = entrySetValue.get(VALUE_FIELD) //
                    .getAsJsonObject();
            request.addUpdatedAttributes(attributeId,
                    convertToUnitValue(unitAttributeObject));
        } else if (isUpdateValueAttributeOperation(entrySetValue)) { // Value attribute
            if (isKeyContainsIndexId(key)) {// It means add to set operation
                request.addAddedToSetAttributes(attributeId,
                        convertToValue(entrySetValue));
            } else {// It means update value attribute
                request.addUpdatedAttributes(attributeId, convertToValue(entrySetValue));
            }
        } else if (isUpdateExceptionCodeOperation(entrySetValue)) { // It means exception code
            request.addRemovedAttributeIds(entrySetValue.get(EXCEPTION) //
                    .getAsString());
        }
    }

    private void updateRequestForRepeatableAttributes(
            final UpdateProductSpecificationRequest request, final JsonObject setCommand,
            final String key, final String attributeId) {
        final JsonArray keyValue = setCommand.get(key) //
                .getAsJsonArray();
        if (keyValue.getAsJsonArray() //
                .getAsJsonArray().size() > 0) {// If array is not empty
            if (isArrayOfValueAttributes(keyValue)) { // is means array of value attributes

                final List<Value<String>> valueAttributes = Lists.newArrayList();
                for (final JsonElement item : keyValue.getAsJsonArray()) {
                    final JsonObject jo = (JsonObject) item;
                    valueAttributes.add(convertToValue(jo));
                }
                request.addUpdatedAttributes(attributeId, valueAttributes);
            } else if (isArrayOfUnitAttributes(keyValue)) { // it means array of unit attributes

                final List<UnitValue> unitAttributes = Lists.newArrayList();
                for (final JsonElement item : keyValue.getAsJsonArray()) {
                    final JsonObject unitAttributeJO = ((JsonObject) item) //
                            .get(VALUE_FIELD) //
                            .getAsJsonObject();
                    unitAttributes.add(convertToUnitValue(unitAttributeJO));
                }
                request.addUpdatedAttributes(attributeId, unitAttributes);
            }
        }
    }

    private UpdateProductSpecificationRequest getRequestForRemoveAttributeOperation(
            final String productId, final String localeId, final JsonObject patch) {
        final UpdateProductSpecificationRequest request = new UpdateProductSpecificationRequest(
                productId, localeId);
        final JsonObject setCommandJO = patch.get(UNSET) //
                .getAsJsonObject();
        final Iterator<String> keySetIt = setCommandJO.keySet() //
                .iterator();
        while (keySetIt.hasNext()) {
            final String attributeId = getAttributeIdFromKey(keySetIt.next());
            request.addRemovedAttributeIds(attributeId);
        }
        return request;
    }

    private JsonObject extractPatchObject(final GenericData.Record record) {
        final JsonObject object = new JsonParser().parse(record.get(PATCH).toString()) //
                .getAsJsonObject();
        object.remove("$v");
        return object;
    }

    private boolean isUpdateAttributeOperation(final JsonObject patch) {
        return patch.keySet() //
                .contains(SET);
    }

    private boolean isRemoveAttributeOperation(final JsonObject patch) {
        return patch.keySet() //
                .contains(UNSET);
    }

    private String getAttributeIdFromKey(final String key) {
        return key.split("\\.")[1];
    }

    private boolean isKeyContainsIndexId(final String key) {
        return key.split("\\.").length == THREE;
    }

    private boolean isUpdateNonRepeatbleOperation(final JsonObject setCommand,
            final String key) {
        return setCommand.get(key) //
                .isJsonObject();
    }

    private boolean isUpdateUnitAttributeOperation(final JsonObject entrySetValue) {
        return (entrySetValue.has(VALUE_FIELD) && entrySetValue.get(VALUE_FIELD) //
                .isJsonObject());
    }

    private boolean isUpdateValueAttributeOperation(final JsonObject entrySetValue) {
        return (entrySetValue.has(VALUE_FIELD) && entrySetValue.get(VALUE_FIELD) //
                .isJsonPrimitive());
    }

    private boolean isUpdateExceptionCodeOperation(final JsonObject entrySetValue) {
        return (!entrySetValue.has(VALUE_FIELD) && entrySetValue.has(EXCEPTION));
    }

    private boolean isArrayOfValueAttributes(final JsonArray keyValue) {
        return (keyValue.getAsJsonArray() //
                .getAsJsonArray()//
                .get(0) //
                .getAsJsonObject() //
                .has(VALUE_FIELD) && keyValue.getAsJsonArray() //
                        .getAsJsonArray()//
                        .get(0) //
                        .getAsJsonObject() //
                        .get(VALUE_FIELD) //
                        .isJsonPrimitive());
    }

    private boolean isArrayOfUnitAttributes(final JsonArray keyValue) {
        return (keyValue.getAsJsonArray() //
                .getAsJsonArray()//
                .get(0) //
                .getAsJsonObject() //
                .has(VALUE_FIELD) && keyValue.getAsJsonArray() //
                        .getAsJsonArray() //
                        .get(0) //
                        .getAsJsonObject().get(VALUE_FIELD) //
                        .isJsonObject());
    }

    private UnitValue convertToUnitValue(final JsonObject unitJson) {
        final Map<String, UnitAttribute> map = Maps.newLinkedHashMap();
        for (String key : unitJson.keySet()) {
            map.put(key, new Gson().fromJson(unitJson.get(key), UnitAttribute.class));
        }
        return new UnitValue(map);
    }

    private Value<String> convertToValue(final JsonObject valueJson) {
        @SuppressWarnings("unchecked")
        final Value<String> value = new Gson().fromJson(valueJson, Value.class);
        return value;
    }
}
