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
import static org.apache.commons.lang3.StringUtils.remove;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.etilize.burraq.eas.media.specification.ProductMediaAttributeValue;
import com.etilize.burraq.eas.media.specification.Status;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Class for parsing received Debezium messages.
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class DebeziumMessageParser {

    /**
     * Extracts {@link Optional<String>} operation type from Debezium originated message.
     *
     * @param record {@link GenericData.Record}
     * @return {@link Optional<String>} operation type.
     */
    public Optional<String> extractOperationType(final GenericData.Record record) {
        final Optional<String> operationType = Optional.empty();
        if (record.get(OPERATION) != null) {
            return Optional.of(record.get(OPERATION).toString());
        }
        return operationType;
    }

    /**
     * Extracts {@link Optional<String>} update operation type i.e. either "Add Product
     * Locale" or "Update Product Specifications".
     *
     * @param record {@link GenericData.Record} Debezium generated update specs command.
     * @return {@link Optional<String>} update operation type.
     */
    public Optional<String> extractUpdateOperationType(final GenericData.Record record) {
        Optional<String> updateOperationType = Optional.empty();
        if (record.get(PATCH) != null) {
            final JsonObject jsonRecord = new JsonParser().parse(
                    record.toString()).getAsJsonObject();
            final JsonObject patchJo = new JsonParser().parse(
                    jsonRecord.get(PATCH).getAsString()).getAsJsonObject();
            if (null == patchJo.get(SET)) {
                return Optional.of(UPDATE_OPERATION_UPDATE_PRODUCT_SPECIFICATION);
            }
            final JsonObject setJson = new JsonParser().parse(
                    patchJo.get(SET).toString()).getAsJsonObject();
            final Set<Entry<String, JsonElement>> entrySet = setJson.entrySet();
            for (final Entry<String, JsonElement> entry : entrySet) {
                if (!(entry.getValue() instanceof JsonArray)) {
                    final JsonObject localvalueJson = new JsonParser().parse(
                            entry.getValue().toString()).getAsJsonObject();
                    if (localvalueJson.entrySet().size() == 0) {
                        updateOperationType = Optional.of(
                                UPDATE_OPERATION_ADD_PRODUCT_LOCALE);
                        return updateOperationType;
                    }
                }
                updateOperationType = Optional.of(
                        UPDATE_OPERATION_UPDATE_PRODUCT_SPECIFICATION);
            }
        }
        return updateOperationType;
    }

    /**
     * Extracts {@link Optional<String>} product locale from Debezium originated message.
     *
     * @param record {@link GenericData.Record} Debezium generated command.
     * @return {@link Optional<String>} product locale.
     */
    public Optional<String> extractProductLocaleFromAddLocaleCommand(
            final GenericData.Record record) {
        Optional<String> localeId = Optional.empty();
        if (record.get(PATCH) != null) {
            final JsonObject patchJo = new JsonParser().parse(
                    record.get(PATCH).toString()) //
                    .getAsJsonObject();
            if (patchJo.has(SET)) {
                final String[] keySet = patchJo.get(SET) //
                        .getAsJsonObject() //
                        .keySet() //
                        .toArray(new String[0]);
                if (keySet[0].contains(".")) {
                    localeId = Optional.of(keySet[0].split("\\.")[0]);
                } else {
                    localeId = Optional.of(keySet[0]);
                }
            } else if (patchJo.has(UNSET)) {
                final String[] keySet = patchJo.get(UNSET) //
                        .getAsJsonObject() //
                        .keySet() //
                        .toArray(new String[0]);
                if (keySet[0].contains(".")) {
                    localeId = Optional.of(keySet[0].split("\\.")[0]);
                } else {
                    localeId = Optional.of(keySet[0]);
                }
            }
        }
        return localeId;
    }

    /**
     * Set unit as an attribute value in Query parameter
     *
     * @param map
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void setValueInQueryParameter(final Map<String, Object> map,
            final boolean isSet) {
        if (map.get(VALUE_FIELD) instanceof LinkedHashMap) {
            setValueFromMap(map, isSet);
        } else //repeatable values/units
        if (map.get(VALUE_FIELD) instanceof ArrayList) {
            setValueFromList(map);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void setValueFromMap(final Map<String, Object> map,
            final boolean isSet) {
        if (null != ((LinkedHashMap) map.get(VALUE_FIELD)).get(UNIT_FIELD)) {
            map.put(VALUE_FIELD, ((LinkedHashMap) map.get(VALUE_FIELD)).get(UNIT_FIELD));
        } else if (null != ((LinkedHashMap) map.get(VALUE_FIELD)).get("exception")) {
            map.put(OPERATION_FIELD, REMOVE_OPERATION);
        } else {
            map.put(VALUE_FIELD, ((LinkedHashMap) map.get(VALUE_FIELD)).get(VALUE_FIELD));
        }
        if (isSet) {
            if (map.get(ELEMENT) != null) {
                map.put(OPERATION_FIELD, ADD_TO_SET_OPERATION);
            } else if (map.get(OPERATION_FIELD) == null) {
                map.put(OPERATION_FIELD, UPDATE_OPERATION);
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void setValueFromList(final Map<String, Object> map) {
        final ArrayList<Object> unitList = new ArrayList<>();
        for (final Object valueObj : ((ArrayList) map.get(VALUE_FIELD))) {
            if (valueObj instanceof Map) {
                if (null != ((Map) valueObj).get(UNIT_FIELD)) {
                    unitList.add(((Map) valueObj).get(UNIT_FIELD));
                } else {
                    unitList.add(((Map) valueObj).get(VALUE_FIELD));
                }
            }
        }
        if (!unitList.isEmpty()) {
            map.put(VALUE_FIELD, unitList);
            if (map.get(OPERATION_FIELD) == null) {
                map.put(OPERATION_FIELD, UPDATE_OPERATION);
            }
        }
    }

    /**
     * Extracts productId from Debezium messages
     *
     * @param key {@link ConsumerRecord<Object, String>} message key
     * @return {@link String} productId
     */
    public String getProductIdFromDebeziumMessageKey(
            final ConsumerRecord<Object, String> key) {
        final JsonObject idKey = new JsonParser().parse(key.key() //
                .toString()) //
                .getAsJsonObject();
        final String productId;
        if (idKey.get("id") != null) {
            productId = idKey.get("id") //
                    .toString();
        } else {
            productId = idKey.get("_id") //
                    .toString();
        }
        return remove(remove(
                remove(remove(remove(remove(remove(productId, "\\"), "\""), "{"), "}"),
                        " "),
                "_id:"), "id:");
    }

    /**
     * Returns PMS message type i.e. AddProductLocaleMessage Or ProductMediaEvent
     *
     * @param record {@link GenericData.Record} Apache kafka value message
     * @return {@link boolean} value for message type.
     */
    public boolean isAddProductLocaleMessageFromPMS(final GenericData.Record record) {
        final String patch = record.get(PATCH) //
                .toString();
        final JsonObject setJO = new JsonParser().parse(patch) //
                .getAsJsonObject() //
                .get(SET) //
                .getAsJsonObject();
        final int keySize = setJO.keySet() //
                .size();
        return keySize == 1 ? true : // It means AddProductLocaleMessage
                false;// It means ProductMediaEvent
    }

    /**
     * Returns an instance of {@link PMSProductMediaEventRequest}
     *
     * @param record {@link GenericData.Record} Apache kafka value message
     * @param key {@link ConsumerRecord<Object, String>} Apache Kafka message key
     * @return {@link PMSProductMediaEventRequest}
     */
    public PMSProductMediaEventRequest getPMSProductMediaEventRequest(
            final GenericData.Record record, final ConsumerRecord<Object, String> key) {
        Status status = null;
        String localeId = null;
        String attributeId = null;
        final ProductMediaAttributeValue attributeValue = new ProductMediaAttributeValue();
        final String patch = record.get(PATCH) //
                .toString();
        // when tag and image attribute set
        final JsonElement setJOElement = new JsonParser().parse(patch) //
                .getAsJsonObject() //
                .get(SET);
        // when attributes are unset on delete/Exception
        final JsonElement unSetJOElement = new JsonParser().parse(patch) //
                .getAsJsonObject() //
                .get(UNSET);

        final String productId = getProductIdFromDebeziumMessageKey(key);

        if (setJOElement != null) {
            final JsonObject setJO = setJOElement //
                    .getAsJsonObject();
            localeId = setJO.keySet() //
                    .iterator() //
                    .next() //
                    .split("\\.")[0];
            attributeId = setJO.keySet() //
                    .iterator() //
                    .next() //
                    .split("\\.")[1];

            for (final String item : setJO.keySet()) {
                if (item.endsWith("status")) {
                    status = Status.valueOf(setJO.get(item) //
                            .getAsString());
                } else if (item.endsWith("tags")) {
                    attributeValue.setTags((setJO.get(item) //
                            .isJsonNull() ? null : getTags((JsonArray) setJO.get(item))));
                } else if (item.endsWith("url")) {
                    attributeValue.setUrl(setJO.get(item) //
                            .isJsonNull() ? null : setJO.get(item) //
                                    .getAsString());
                } else if (item.endsWith("height")) {
                    attributeValue.setHeight(setJO.get(item) //
                            .isJsonNull() ? null : setJO.get(item) //
                                    .getAsInt());
                } else if (item.endsWith("width")) {
                    attributeValue.setWidth(setJO.get(item) //
                            .isJsonNull() ? null : setJO.get(item) //
                                    .getAsInt());
                }
            }
        } else if (unSetJOElement != null) {
            final JsonObject unSetJO = unSetJOElement//
                    .getAsJsonObject();
            localeId = unSetJO.keySet() //
                    .iterator() //
                    .next() //
                    .split("\\.")[0];
            attributeId = unSetJO.keySet() //
                    .iterator() //
                    .next() //
                    .split("\\.")[1];

            for (final String item : unSetJO.keySet()) {
                if (item.endsWith("status")) {
                    status = Status.valueOf(unSetJO.get(item) //
                            .getAsString());
                } else if (item.endsWith("tags")) {
                    attributeValue.setTags((unSetJO.get(item) //
                            .isJsonNull() ? null
                                    : getTags((JsonArray) unSetJO.get(item))));
                }
            }
            // In case of Delete no status comes in message
            // therefore removing on basis of unset Patch value
            if (status == null) {
                status = Status.DELETED;
            }

        }
        return new PMSProductMediaEventRequest(productId, localeId, attributeId, status,
                attributeValue);
    }

    /**
     * Returns an instance of {@link AssociateCategoryCommand}
     *
     * @param record {@link GenericData.Record}
     * @param key {@link ConsumerRecord<Object, String>}
     * @return {@link AssociateCategoryCommand}
     */
    public AssociateCategoryCommand getAssociateCategoryCommand(
            final GenericData.Record record, final ConsumerRecord<Object, String> key) {
        final String after = record.get(AFTER) //
                .toString();
        final JsonObject associateCategoryJson = new JsonParser().parse(after) //
                .getAsJsonObject();
        return new AssociateCategoryCommand(associateCategoryJson.get(ID) //
                .getAsString(), associateCategoryJson.get(INDUSTRY_ID) //
                        .getAsString(),
                associateCategoryJson.get(CATEGORY_ID) //
                        .getAsString());
    }

    /**
     * convert JSONARRAY into Set<String>
     *
     * @param array {@link JsonArray}
     * @return {@link Set<String>}
     */
    private Set<String> getTags(final JsonArray array) {
        final Set<String> tags = new HashSet<String>();
        array.forEach(obj -> {
            tags.add(obj.getAsString());
        });
        return tags;
    }
}
