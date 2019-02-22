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

import static com.etilize.burraq.eas.utils.Utils.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

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

    /**
     * It save attributes in db.
     *
     * @param request {@link UpdateSpecificationRequest}
     */
    void saveAttributes(UpdateSpecificationRequest request);

    /**
     * It gets attribute value in raw format and save it as it is.
     *
     * @param id id
     * @param attributes map of attribute values by attribute id
     */
    void saveAttributes(String id, Map<String, Object> attributes);

    /**
     * It return Specification for id. findById will throw error if attributes have data.
     * Default implementation of findById can not handle Map<String, Object> type of field attributes.
     *
     * @param id id
     * @return Optional<Specification>
     */
    Optional<Specification> findOne(String id);

    /**
     * It returns {@link UpdateItemSpec} to save raw data attributes in db.
     *
     * @param id id
     * @param attributes map of attribute values by attribute id
     * @return UpdateItemSpec
     */
    default UpdateItemSpec getUpdateItemSpecForSaveAttributesRawData(final String id,
            final Map<String, Object> attributes) {
        final NameMap nameMap = new NameMap();
        nameMap.with(getKeyName(LAST_UPDATE_DATE), LAST_UPDATE_DATE);
        final ValueMap valueMap = new ValueMap() //
                .withLong(getValueName(LAST_UPDATE_DATE), new Date().getTime());
        final StringBuilder updateExp = new StringBuilder(
                getKeyValueUse("SET #%s=:%s,", LAST_UPDATE_DATE));
        attributes.entrySet().forEach(entry -> {
            nameMap.with(getKeyName(entry.getKey()), entry.getKey());
            valueMap.with(getValueName(entry.getKey()), entry.getValue());
            updateExp.append(getKeyValueUse(ATTRIBUTES + ".#%s=:%s,", entry.getKey()));
        });
        updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        final UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(ID, id) //
                .withUpdateExpression(updateExp.toString()) //
                .withNameMap(nameMap) //
                .withValueMap(valueMap);
        return updateItemSpec;
    }

    /**
     * It returns {@link UpdateItemSpec} to save attributes in db.
     *
     * @param request {@link UpdateSpecificationRequest}
     * @return UpdateItemSpec
     */
    default UpdateItemSpec getUpdateItemSpecForSaveAttributes(
            final UpdateSpecificationRequest request) {
        final NameMap nameMap = new NameMap();
        nameMap.with(getKeyName(LAST_UPDATE_DATE), LAST_UPDATE_DATE);
        final ValueMap valueMap = new ValueMap() //
                .withLong(getValueName(LAST_UPDATE_DATE), new Date().getTime());
        final StringBuilder updateExp = new StringBuilder(
                getKeyValueUse("SET #%s=:%s,", LAST_UPDATE_DATE));
        request.getUpdatedAttributes().entrySet().forEach(entry -> {
            nameMap.with(getKeyName(entry.getKey()), entry.getKey());
            valueMap.with(getValueName(entry.getKey()),
                    convertOperationToValue(entry.getValue()));
            updateExp.append(getKeyValueUse(ATTRIBUTES + ".#%s=:%s,", entry.getKey()));
        });
        updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        if (!request.getRemovedFromSetAttributes().isEmpty()) {
            updateExp.append(" DELETE ");
            request.getRemovedFromSetAttributes().entrySet().forEach(entry -> {
                final Object value = convertOperationToValue(entry.getValue());
                if (value instanceof String) {
                    valueMap.withStringSet(getValueName(entry.getKey()), (String) value);
                } else if (value instanceof Number) {
                    valueMap.withNumberSet(getValueName(entry.getKey()), (Number) value);
                } else if (value instanceof Set) {
                    valueMap.with(getValueName(entry.getKey()), (Set) value);
                }
                if (value instanceof String || value instanceof Number
                        || value instanceof Set) {
                    nameMap.with(getKeyName(entry.getKey()), entry.getKey());
                    updateExp.append(
                            getKeyValueUse(ATTRIBUTES + ".#%s :%s,", entry.getKey()));
                }
            });
            updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        }
        if (!request.getAddedToSetAttributes().isEmpty()) {
            updateExp.append(" ADD ");
            request.getAddedToSetAttributes().entrySet().forEach(entry -> {
                final Object value = convertOperationToValue(entry.getValue());
                if (value instanceof String) {
                    valueMap.withStringSet(getValueName(entry.getKey()), (String) value);
                } else if (value instanceof Number) {
                    valueMap.withNumberSet(getValueName(entry.getKey()), (Number) value);
                } else if (value instanceof Set) {
                    valueMap.with(getValueName(entry.getKey()), (Set) value);
                }
                if (value instanceof String || value instanceof Number
                        || value instanceof Set) {
                    nameMap.with(getKeyName(entry.getKey()), entry.getKey());
                    updateExp.append(
                            getKeyValueUse(ATTRIBUTES + ".#%s :%s,", entry.getKey()));
                }
            });
            updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        }
        if (!request.getRemovedAttributeIds().isEmpty()) {
            updateExp.append(" REMOVE ");
            request.getRemovedAttributeIds().forEach(attributeId -> {
                nameMap.with(getKeyName(attributeId), attributeId);
                updateExp.append(String.format("%s.#%s,", ATTRIBUTES, attributeId));
            });
            updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        }
        final UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(ID,
                request.getId()) //
                .withUpdateExpression(updateExp.toString()) //
                .withNameMap(nameMap) //
                .withValueMap(valueMap);
        return updateItemSpec;
    }
}
