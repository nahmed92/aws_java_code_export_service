/*
 * #region
 * export-aggregation-service
 * %%
 * Copyright (C) 2018 - 2019 Etilize
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

package com.etilize.burraq.eas.specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.etilize.burraq.eas.specification.value.SpecificationValue;
import com.etilize.burraq.eas.specification.value.UnitAttribute;
import com.etilize.burraq.eas.specification.value.UnitValue;
import com.etilize.burraq.eas.specification.value.Value;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * It defines common services for {@link DetailedSpecificationRepository} & {@link BasicSpecificationRepository}
 *
 * @author Umar Zubair
 * @since 1.0
 */
public interface SpecificationCustomRepository {

    String ID = "id";

    String LAST_UPDATE_DATE = "lastUpdateDate";

    String ATTRIBUTES = "attributes";

    String COLON_LAST_UPDATE_DATE = ":lastUpdateDate";

    String COLON_ATTRIBUTES = ":attributes";

    /**
     * It save attributes in db.
     *
     * @param id product id
     * @param request {@link UpdateSpecificationRequest}
     */
    void saveAttributes(String id, UpdateSpecificationRequest request);

    /**
     * It return attributes data
     *
     * @param id product id
     * @return Map<String, Object>
     */
    Map<String, Object> getAttributes(String id);

    /**
     * It returns {@link UpdateItemSpec} to save attributes in db.
     *
     * @param id product id
     * @param request {@link UpdateSpecificationRequest}
     * @return UpdateItemSpec
     */

    default UpdateItemSpec getUpdateItemSpecForSaveAttributes(final String id,
            final UpdateSpecificationRequest request) {
        final ValueMap valueMap = new ValueMap() //
                .withLong(COLON_LAST_UPDATE_DATE, new Date().getTime());
        final StringBuilder updateExp = new StringBuilder(String.format("SET %s=%s,", //
                LAST_UPDATE_DATE, COLON_LAST_UPDATE_DATE));
        request.getUpdatedAttributes().entrySet().forEach(entry -> {
            valueMap.with(":" + entry.getKey(),
                    convertOperationToValue(entry.getValue()));
            updateExp.append(String.format("%s.%s=:%s,", ATTRIBUTES, entry.getKey(),
                    entry.getKey()));
        });
        updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        if (!request.getRemovedFromSetAttributes().isEmpty()) {
            updateExp.append(" DELETE ");
            request.getRemovedFromSetAttributes().entrySet().forEach(entry -> {
                final Object value = convertOperationToValue(entry.getValue());
                if (value instanceof String) {
                    valueMap.withStringSet(":" + entry.getKey(),
                            (String) convertOperationToValue(entry.getValue()));
                    updateExp.append(String.format("%s.%s :%s,", ATTRIBUTES,
                            entry.getKey(), entry.getKey()));
                } else if (value instanceof Number) {
                    valueMap.withNumberSet(":" + entry.getKey(),
                            (Number) convertOperationToValue(entry.getValue()));
                    updateExp.append(String.format("%s.%s :%s,", ATTRIBUTES,
                            entry.getKey(), entry.getKey()));
                } else if (value instanceof Set) {
                    valueMap.with(":" + entry.getKey(),
                            (Set) convertOperationToValue(entry.getValue()));
                    updateExp.append(String.format("%s.%s :%s,", ATTRIBUTES,
                            entry.getKey(), entry.getKey()));
                }
            });
            updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        }
        if (!request.getAddedToSetAttributes().isEmpty()) {
            updateExp.append(" ADD ");
            request.getAddedToSetAttributes().entrySet().forEach(entry -> {
                final Object value = convertOperationToValue(entry.getValue());
                if (value instanceof String) {
                    valueMap.withStringSet(":" + entry.getKey(),
                            (String) convertOperationToValue(entry.getValue()));
                    updateExp.append(String.format("%s.%s :%s,", ATTRIBUTES,
                            entry.getKey(), entry.getKey()));
                } else if (value instanceof Number) {
                    valueMap.withNumberSet(":" + entry.getKey(),
                            (Number) convertOperationToValue(entry.getValue()));
                    updateExp.append(String.format("%s.%s :%s,", ATTRIBUTES,
                            entry.getKey(), entry.getKey()));
                } else if (value instanceof Set) {
                    valueMap.with(":" + entry.getKey(),
                            (Set) convertOperationToValue(entry.getValue()));
                    updateExp.append(String.format("%s.%s :%s,", ATTRIBUTES,
                            entry.getKey(), entry.getKey()));
                }
            });
            updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        }
        if (!request.getRemovedAttributeIds().isEmpty()) {
            updateExp.append(" REMOVE ");
            request.getRemovedAttributeIds().forEach(attributeId -> {
                updateExp.append(String.format("%s.%s,", ATTRIBUTES, attributeId));
            });
            updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        }
        final UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(ID, id) //
                .withUpdateExpression(updateExp.toString()) //
                .withValueMap(valueMap);
        return updateItemSpec;
    }

    /**
     * It returns String,Number,Set of String, Set of Number, Map or List of Map
     * @param opValue Object value for attribute
     * @return Object
     */
    default Object convertOperationToValue(final Object opValue) {
        Object convertedValue = null;
        if (opValue instanceof Object[] || opValue instanceof ArrayList) {
            Object[] multiObjects = null;
            if (opValue instanceof ArrayList) {
                multiObjects = ((ArrayList) opValue).toArray();
            } else {
                multiObjects = (Object[]) opValue;
            }
            final List<Object> values = Lists.newArrayList();
            boolean useList = false;
            for (final Object obj : multiObjects) {
                if ((SpecificationValue) obj instanceof UnitValue) {
                    useList = true;
                }
                values.add(getSpecificationValue((SpecificationValue) obj));
            }
            if (useList) {
                // we have to use list for Map - unit
                convertedValue = values;
            } else {
                // we have to use set for string and number
                convertedValue = ImmutableSet.copyOf(values);
            }
        } else {
            convertedValue = getSpecificationValue((SpecificationValue) opValue);
        }
        return convertedValue;
    }

    /**
     * It return String, Number, Map<String, Map<String, Object>> or same passed object
     * @param opValue {@link SpecificationValue}
     * @return Object
     */
    default Object getSpecificationValue(final SpecificationValue opValue) {
        if (opValue instanceof Value<?>) {
            final Value<?> value = (Value) opValue;
            if (value.getValue() instanceof String
                    || value.getValue() instanceof Number) {
                return value.getValue();
            }
        } else if (opValue instanceof UnitValue) {
            final UnitValue value = (UnitValue) opValue;
            return getUnitAttrMap(value.getValue());
        }
        return opValue;
    }

    /**
     * It converts Map<String, UnitAttribute> into Map<String, Map<String, Object>>
     *
     * @param unitAttributeMap Map<String, UnitAttribute>
     * @return Map<String, Map<String, Object>>
     */
    default Map<String, Map<String, Object>> getUnitAttrMap(
            final Map<String, UnitAttribute> unitAttributeMap) {
        final Map<String, Map<String, Object>> result = Maps.newHashMap();
        unitAttributeMap.entrySet().stream().forEach(entry -> {
            final UnitAttribute ua = entry.getValue();
            final Map<String, Object> uaMap = Maps.newHashMap();
            uaMap.put("unit", ua.getUnit());
            uaMap.put("value", ua.getValue());
            result.put(entry.getKey(), uaMap);
        });
        return result;
    }
}
