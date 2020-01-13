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

package com.etilize.burraq.eas.media.specification;

import static com.etilize.burraq.eas.utils.Utils.*;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.google.common.collect.Maps;

/**
 * It has common methods which are used for customization made in
 * {@link ProductRichMediaSpecificationRepositoryImpl} && {@link ProductBasicMediaSpecificationRepositoryImpl}
 *
 * @author Umar Zubair
 * @since 1.0
 */
public interface ProductMediaSpecificationCustomRepository {

    String ID = "id";

    String LAST_UPDATE_DATE = "lastUpdateDate";

    String COLON_LAST_UPDATE_DATE = ":" + LAST_UPDATE_DATE;

    String ATTRIBUTES = "attributes";

    /**
     * Adds attribute's value in attributes
     *
     * @param id record id
     * @param attributeId attribute's id
     * @param value {@link ProductMediaAttributeValue}
     */
    void updateAttribute(String id, String attributeId, ProductMediaAttributeValue value);

    /**
     * Removed attribute's value in attributes
     *
     * @param id record id
     * @param attributeId attribute's id
     */
    void removeAttribute(String id, String attributeId);

    /**
     * It returns {@link UpdateItemSpec} to add attribute value
     *
     * @param id record id
     * @param attributeId attribute id
     * @param value {@link ProductMediaAttributeValue}
     * @return {@link UpdateItemSpec}
     */
    default UpdateItemSpec updateAttributeItem(final String id, final String attributeId,
            final ProductMediaAttributeValue value) {
        final NameMap nameMap = new NameMap();
        final ValueMap valueMap = new ValueMap() //
                .withLong(COLON_LAST_UPDATE_DATE, new Date().getTime());
        final StringBuilder updateExp = new StringBuilder();
        nameMap.with("#attrId", attributeId);
        updateExp.append(
                String.format("SET %s=%s", LAST_UPDATE_DATE, COLON_LAST_UPDATE_DATE));
        final Map<String, Object> map = Maps.newHashMap();
        if (value.getTags() != null && !value.getTags().isEmpty()) {
            map.put("tags", value.getTags());
        }
        if (StringUtils.isNotBlank(value.getUrl())) {
            map.put("url", value.getUrl());
        }
        if (value.getHeight() != null) {
            map.put("height", value.getHeight());
        }
        if (value.getWidth() != null) {
            map.put("width", value.getWidth());
        }
        valueMap.withMap(":value", map);
        updateExp.append(String.format(", %s.#attrId=:value ", ATTRIBUTES));

        final UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(ID, id) //
                .withUpdateExpression(updateExp.toString()) //
                .withNameMap(nameMap) //
                .withValueMap(valueMap);
        return updateItemSpec;
    }

    /**
     * It returns {@link UpdateItemSpec} to delete an attribute
     *
     * @param id record id
     * @param attributeId attribute id
     * @return {@link UpdateItemSpec}
     */
    default UpdateItemSpec removeAttributeItem(final String id,
            final String attributeId) {
        final NameMap nameMap = new NameMap();
        final ValueMap valueMap = new ValueMap() //
                .withLong(COLON_LAST_UPDATE_DATE, new Date().getTime());
        final StringBuilder updateExp = new StringBuilder();
        nameMap.with("#attrId", attributeId);
        updateExp.append(String.format("REMOVE %s.#attrId ", ATTRIBUTES));
        updateExp.append(String.format("SET %s=%s", //
                LAST_UPDATE_DATE, COLON_LAST_UPDATE_DATE));
        final UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(ID, id) //
                .withUpdateExpression(updateExp.toString()) //
                .withNameMap(nameMap) //
                .withValueMap(valueMap);
        return updateItemSpec;
    }
}
