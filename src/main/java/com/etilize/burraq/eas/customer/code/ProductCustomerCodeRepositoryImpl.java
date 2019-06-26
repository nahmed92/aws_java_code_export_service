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

package com.etilize.burraq.eas.customer.code;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.google.common.collect.Lists;

/**
 * It is implementation of {@link ProductCustomerCodeCustomRepository}
 *
 * @author Umar Zubair
 * @since 1.0
 */
public class ProductCustomerCodeRepositoryImpl
        implements ProductCustomerCodeCustomRepository {

    private static final String ID = "id";

    private static final String CUSTOMER_ID = "customerId";

    private static final String PRODUCT_ID = "productId";

    private static final String LOCALE_ID = "localeId";

    private static final String LAST_UPDATE_DATE = "lastUpdateDate";

    private static final String CODES = "codes";

    private final Table table;

    /**
     * Constructs with dependencies
     *
     * @param amazonDynamoDB amazonDynamoDB
     */
    @Autowired
    public ProductCustomerCodeRepositoryImpl(final AmazonDynamoDB amazonDynamoDB) {
        Assert.notNull(amazonDynamoDB, "amazonDynamoDB is required.");
        final DynamoDB db = new DynamoDB(amazonDynamoDB);
        table = db.getTable(ProductCustomerCode.TABLE_NAME);
    }

    @Override
    public void link(final ProductCustomerCode customerCode) {
        final List<AttributeUpdate> attributeUpdates = Lists.newArrayList(
                new AttributeUpdate(PRODUCT_ID).put(customerCode.getProductId()),
                new AttributeUpdate(LOCALE_ID).put(customerCode.getLocaleId()),
                new AttributeUpdate(LAST_UPDATE_DATE).put(
                        customerCode.getLastUpdateDate().getTime()),
                new AttributeUpdate(CODES).addElements(
                        customerCode.getCodes().toArray()));
        final UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(ID,
                customerCode.getId(), CUSTOMER_ID,
                customerCode.getCustomerId()).withAttributeUpdate(attributeUpdates);
        table.updateItem(updateItemSpec);
    }

    @Override
    public void unlink(final ProductCustomerCode customerCode) {
        final List<AttributeUpdate> attributeUpdates = Lists.newArrayList(
                new AttributeUpdate(PRODUCT_ID).put(customerCode.getProductId()),
                new AttributeUpdate(LOCALE_ID).put(customerCode.getLocaleId()),
                new AttributeUpdate(LAST_UPDATE_DATE).put(
                        customerCode.getLastUpdateDate().getTime()),
                new AttributeUpdate(CODES).removeElements(
                        customerCode.getCodes().toArray()));
        final UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(ID,
                customerCode.getId(), CUSTOMER_ID,
                customerCode.getCustomerId()).withAttributeUpdate(attributeUpdates);
        table.updateItem(updateItemSpec);
    }
}
