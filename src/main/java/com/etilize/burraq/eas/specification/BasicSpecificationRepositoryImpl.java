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

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

/**
 * It defines implementation customization for {@link BasicSpecificationRepository}
 *
 * @author Umar Zubair
 * @since 1.0
 */
public class BasicSpecificationRepositoryImpl
        implements BasicSpecificationCustomRepository {

    private static final String CATEGORY_ID = "categoryId";

    private static final String PRODUCT_ID = "productId";

    private static final String LOCALE_ID = "localeId";

    private static final String INDUSTRY_ID = "industryId";

    private final Table table;

    /**
     * Constructs with dependencies
     *
     * @param amazonDynamoDB amazonDynamoDB
     */
    @Autowired
    public BasicSpecificationRepositoryImpl(final AmazonDynamoDB amazonDynamoDB) {
        Assert.notNull(amazonDynamoDB, "amazonDynamoDB is required.");
        final DynamoDB db = new DynamoDB(amazonDynamoDB);
        table = db.getTable(BasicSpecification.TABLE_NAME);
    }

    @Override
    public void saveAttributes(final UpdateSpecificationRequest request) {
        if (request.hasAnyOperation()) {
            table.updateItem(getUpdateItemSpecForSaveAttributes(request));
        }
    }

    @Override
    public void saveAttributes(final String id, final Map<String, Object> attributes) {
        if (!attributes.isEmpty()) {
            table.updateItem(getUpdateItemSpecForSaveAttributesRawData(id, attributes));
        }
    }

    @Override
    public Optional<Specification> findOne(final String id) {
        final Item item = table.getItem(new PrimaryKey(ID, id));
        BasicSpecification specs = null;
        if (item != null) {
            specs = new BasicSpecification();
            specs.setId(id);
            specs.setCategoryId(item.getString(CATEGORY_ID));
            specs.setIndustryId(item.getString(INDUSTRY_ID));
            specs.setLocaleId(item.getString(LOCALE_ID));
            specs.setProductId(item.getString(PRODUCT_ID));
            specs.setAttributes(item.getRawMap(ATTRIBUTES));
        }
        return Optional.ofNullable(specs);
    }

    @Override
    public Optional<Product> findProductById(final String id) {
        Product product = null;
        final Item item = table.getItem(new GetItemSpec().withPrimaryKey(
                new PrimaryKey(ID, id)).withAttributesToGet(CATEGORY_ID, INDUSTRY_ID,
                        PRODUCT_ID));
        if (item != null) {
            product = new Product();
            product.setCategoryId(item.getString(CATEGORY_ID));
            product.setIndustryId(item.getString(INDUSTRY_ID));
            product.setProductId(item.getString(PRODUCT_ID));
        }
        return Optional.ofNullable(product);
    }
}
