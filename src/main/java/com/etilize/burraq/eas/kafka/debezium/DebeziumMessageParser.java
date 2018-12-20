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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import org.apache.avro.generic.GenericData;

import com.etilize.burraq.eas.kafka.debezium.SpecificationUpdateOperation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
     * Extracts {@link Optional<String>} update operation type i.e. either "Add Product Locale" or "Update Product Specifications".
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
                    record.get(PATCH).toString()).getAsJsonObject();
            final String[] keySet = patchJo.get(SET) //
                    .getAsJsonObject() //
                    .keySet() //
                    .toArray(new String[0]);
            localeId = Optional.of(keySet[0]);
        }
        return localeId;
    }

    /**
     * Convert update json to operation Map
     * @param productId Product Id
     * @param updateJson json for update operation
     * @return Map<String, Object> return update index operation map
     * @throws IOException json parsing throws IO exception
     */
    @SuppressWarnings("unchecked")
    private static Queue<SpecificationUpdateOperation> getUpdateOperationMap(
            final String productId, final String updateJson) throws IOException {
        final Queue<SpecificationUpdateOperation> operatonList = new LinkedList<>();
        final ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map;
        // convert JSON string to Map
        map = mapper.readValue(updateJson, new TypeReference<Map<String, Object>>() {
        });
        map.remove("$v");
        Map<String, Object> attributeMap = null;

        final Set<Entry<String, Object>> operations = map.entrySet();
        for (final Entry<String, Object> operation : operations) {
            final SpecificationUpdateOperation specificationUpdateOperation = new SpecificationUpdateOperation();
            //Geting mongo DB operation
            if (operation.getKey().equals(SET)) {
                attributeMap = (Map<String, Object>) operation.getValue();
                specificationUpdateOperation.setOperationType(UPDATE_OPERATION);
            } else if (operation.getKey().equals(UNSET)) {
                // unset only coming in REMOVE case
                attributeMap = (Map<String, Object>) operation.getValue();
                specificationUpdateOperation.setOperationType(REMOVE_OPERATION);
            } else if (operation.getKey().equals(PUSH)) {
                // unset only coming in REMOVE case
                attributeMap = (Map<String, Object>) operation.getValue();
                specificationUpdateOperation.setOperationType(ADD_TO_SET_OPERATION);
            } else if (operation.getKey().equals(PULL)) {
                // unset only coming in REMOVE case
                attributeMap = (Map<String, Object>) operation.getValue();
                specificationUpdateOperation.setOperationType(REMOVE_FROM_SET_OPERATION);
            }
            final Queue<Map<String, Object>> paramsList = getAllOperationsAndParamsMap(
                    productId, specificationUpdateOperation, attributeMap);
            specificationUpdateOperation.setQueryParams(paramsList);
            operatonList.add(specificationUpdateOperation);
        }
        return operatonList;
    }

    private static Queue<Map<String, Object>> getAllOperationsAndParamsMap(
            final String productId,
            final SpecificationUpdateOperation specificationUpdateOperation,
            final Map<String, Object> attributeMap) {
        final Queue<Map<String, Object>> paramsList = new LinkedList<>();
        String[] valuesArray;
        Map<String, Object> values;
        final Set<Entry<String, Object>> entrySet = attributeMap.entrySet();
        for (final Entry<String, Object> entry : entrySet) {
            values = new HashMap<>();
            valuesArray = entry.getKey().split("\\.");
            final String localeId = valuesArray[LOCALE_ID_INDEX];
            specificationUpdateOperation.setDocId(productId + "-" + localeId);
            if (valuesArray.length > ATTRIBUTE_ID_INDEX) {
                final String attributeId = valuesArray[ATTRIBUTE_ID_INDEX];
                values.put(KEY, attributeId);
            }
            values.put(VALUE_FIELD, entry.getValue());
            if (valuesArray.length > ELEMENT_ID_INDEX) {
                values.put(ELEMENT, valuesArray[ELEMENT_ID_INDEX]);
            }
            if (!specificationUpdateOperation.getOperationType().equals(
                    REMOVE_OPERATION)) {
                setValueInQueryParameter(values, false);
                values.remove(ELEMENT);
            }
            if (specificationUpdateOperation.getOperationType() == null) {
                specificationUpdateOperation.setOperationType(EMPTY_INDEX_OPERATION);
            }
            if (specificationUpdateOperation.getOperationType().equals(
                    REMOVE_FROM_SET_OPERATION)) {
                values.put(OPERATION_FIELD, REMOVE_FROM_SET_OPERATION);
            } else if (specificationUpdateOperation.getOperationType().equals(
                    ADD_TO_SET_OPERATION)) {
                values.put(OPERATION_FIELD, ADD_TO_SET_OPERATION);
            }
            paramsList.add(values);
        }

        return paramsList;
    }

    /**
     * Set unit as an attribute value in Query parameter
     *
     * @param map
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void setValueInQueryParameter(final Map<String, Object> map,
            boolean isSet) {
        if (map.get(VALUE_FIELD) instanceof LinkedHashMap) {
            setValueFromMap(map, isSet);
        } else //repeatable values/units
        if (map.get(VALUE_FIELD) instanceof ArrayList) {
            setValueFromList(map);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void setValueFromMap(final Map<String, Object> map, boolean isSet) {
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
     * Extract update operations.
     *
     * @param productId Product Id
     * @param record Update Product Specification Command.
     *
     * @return {@link Optional<SpecificationUpdateOperation>}
     * @throws IOException Parsing IO Exception
     */
    public Queue<SpecificationUpdateOperation> parseToUpdateOperationMap(
            final String productId, final GenericData.Record record) throws IOException {
        final JsonObject jsonRecord = new JsonParser().parse(
                record.toString()).getAsJsonObject();
        return getUpdateOperationMap(productId,
                new JsonParser().parse(jsonRecord.get(PATCH).getAsString()).toString());
    }
}
