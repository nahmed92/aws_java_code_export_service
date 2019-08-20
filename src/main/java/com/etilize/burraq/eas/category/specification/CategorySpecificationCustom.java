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

package com.etilize.burraq.eas.category.specification;

import static com.etilize.burraq.eas.utils.Utils.*;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

public interface CategorySpecificationCustom {

    String LAST_UPDATE_DATE = "lastUpdateDate";

    String ATTRIBUTES = "attributes";

    String FIELD_CATEGORY_ID = "categoryId";

    String LOCALE_ID = "localeId";

    /*
     * (non-Javadoc)
     *
     * @see com.etilize.burraq.eas.attributetranslation.AttributeTranslationRepository#
     * getUpdateItemSpecForUpadateAttributeData(com.etilize.burraq.eas.
     * attributetranslation.Category, java.lang.String, java.lang.String)
     */
    default UpdateItemSpec getUpdateItemSpecForUpadateAttributeData(
            final String categoryId, final String fieldName, final boolean isAttribute,
            final String value, final String localeId) {

        final NameMap nameMap = new NameMap();
        nameMap.with(getKeyName(LAST_UPDATE_DATE), LAST_UPDATE_DATE);
        final ValueMap valueMap = new ValueMap() //
                .withLong(getValueName(LAST_UPDATE_DATE), new Date().getTime());
        final StringBuilder updateExp = new StringBuilder(
                getKeyValueUse("SET #%s=:%s,", LAST_UPDATE_DATE));
        nameMap.with(getKeyName(fieldName), fieldName);
        valueMap.with(getValueName(fieldName), value);
        if (isAttribute) {
            updateExp.append(getKeyValueUse(ATTRIBUTES + ".#%s=:%s,", fieldName));
        } else {
            updateExp.append(getKeyValueUse("#%s=:%s,", fieldName));
        }
        updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        return new UpdateItemSpec().withPrimaryKey(FIELD_CATEGORY_ID, categoryId,
                LOCALE_ID, localeId) //
                .withUpdateExpression(updateExp.toString()) //
                .withNameMap(nameMap) //
                .withValueMap(valueMap);
    }

}
