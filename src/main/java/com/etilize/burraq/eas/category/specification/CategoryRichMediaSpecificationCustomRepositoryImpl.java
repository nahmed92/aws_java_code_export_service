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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;

/**
 * CategoryRichMediaSpecificationCustomRepository implementation
 *
 * @author Nasir Ahmed
 *
 */
public class CategoryRichMediaSpecificationCustomRepositoryImpl
        implements CategoryRichMediaSpecificationCustomRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String TABLE_NAME = "category-rich-media-specifications";

    private final Table table;

    /**
     * Constructor {@link CategoryRichMediaSpecificationCustomRepositoryImpl}
     *
     * @param amazonDynamoDB {@link AmazonDynamoDB}
     */
    public CategoryRichMediaSpecificationCustomRepositoryImpl(
            final AmazonDynamoDB amazonDynamoDB) {
        Assert.notNull(amazonDynamoDB, "amazonDynamoDB is required.");
        final DynamoDB db = new DynamoDB(amazonDynamoDB);
        table = db.getTable(TABLE_NAME);
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.eas.category.specification.CategoryRichMediaSpecificationCustomRepository#updateCategoryAttribute(java.lang.String, java.lang.String, boolean, java.lang.String, java.lang.String)
     */
    @Override
    public void updateCategoryAttribute(final String categoryId, final String localeId,
            final boolean isAttribute, final String fieldName, final String value) {
        try {
            table.updateItem(getUpdateItemSpecForUpadateAttributeData(categoryId,
                    fieldName, isAttribute, value, localeId));
        } catch (final AmazonDynamoDBException e) {
            logger.info("Record not found for categoryId {} and locale {}", categoryId,
                    localeId, e);
        }
    }
}
