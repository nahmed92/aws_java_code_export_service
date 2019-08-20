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

import javax.validation.OverridesAttribute;

import org.springframework.util.Assert;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;

/**
 * This class attribute value of category basic table
 *
 * @author Nasir Ahmed
 *
 */
public class CategoryBasicSpecificationRepositoryImpl
        implements CategoryBasicSpecificationRepositoryCustomRepository {

    public static final String TABLE_NAME = "category-basic-specifications";

    private final Table table;

    /**
     * CategoryBasicSpecificationAttributeUpdateRepositoryImpl Constructor
     * @param amazonDynamoDB {@link AmazonDynamoDB}
     */
    public CategoryBasicSpecificationRepositoryImpl(final AmazonDynamoDB amazonDynamoDB) {
        Assert.notNull(amazonDynamoDB, "amazonDynamoDB is required.");
        final DynamoDB db = new DynamoDB(amazonDynamoDB);
        table = db.getTable(TABLE_NAME);
    }

    /**
     * Update category attribute by provided name and value
     * @param categoryId Attribute CategoryId
     * @param fieldName Attribute Name
     * @param isAttribute is related to offering attribute
     * @param value updated value
     * @param localeId locale id
     */
    @Override
    public void updateCategoryAttribute(final String categoryId, final String fieldName,
            final boolean isAttribute, final String value, final String localeId) {
        table.updateItem(getUpdateItemSpecForUpadateAttributeData(categoryId, fieldName,
                isAttribute, value, localeId));

    }

}
