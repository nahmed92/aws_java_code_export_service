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

package com.etilize.burraq.eas.barcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;

/**
 * It is implementation of {@link BarcodeCustomRepository}
 *
 * @author Umar Zubair
 * @since 1.0
 */
public class BarcodeRepositoryImpl implements BarcodeCustomRepository {

    private static final String ID = "id";

    private static final String TYPE = "type";

    private static final String PRODUCT_ID = "productId";

    private static final String LOCALE_ID = "localeId";

    private static final String LAST_UPDATE_DATE = "lastUpdateDate";

    private static final String CODES = "codes";

    private static final String COLON_PRODUCT_ID = ":productId";

    private static final String COLON_LOCALE_ID = ":localeId";

    private static final String COLON_LAST_UPDATE_DATE = ":lastUpdateDate";

    private static final String COLON_CODES = ":codes";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Table table;

    /**
     * Constructs with dependencies
     *
     * @param amazonDynamoDB amazonDynamoDB
     */
    @Autowired
    public BarcodeRepositoryImpl(final AmazonDynamoDB amazonDynamoDB) {
        Assert.notNull(amazonDynamoDB, "amazonDynamoDB is required.");
        final DynamoDB db = new DynamoDB(amazonDynamoDB);
        table = db.getTable(Barcode.TABLE_NAME);
    }

    private void insert(final Barcode barcode) {
        final ValueMap valueMap = new ValueMap() //
                .withMap(COLON_CODES, barcode.getCodes()) //
                .withString(COLON_PRODUCT_ID, barcode.getProductId()) //
                .withString(COLON_LOCALE_ID, barcode.getLocaleId()) //
                .withLong(COLON_LAST_UPDATE_DATE, barcode.getLastUpdateDate().getTime());
        final String updateExp = String.format("set %s=%s,%s=%s,%s=%s,%s=%s", //
                CODES, COLON_CODES, //
                PRODUCT_ID, COLON_PRODUCT_ID, //
                LOCALE_ID, COLON_LOCALE_ID, //
                LAST_UPDATE_DATE, COLON_LAST_UPDATE_DATE);
        final UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(ID,
                barcode.getId(), //
                TYPE, barcode.getType()) //
                .withUpdateExpression(updateExp) //
                .withValueMap(valueMap) //
                .withConditionExpression("attribute_not_exists(codes)");
        table.updateItem(updateItemSpec);
    }

    private void update(final Barcode barcode) {
        final NameMap nameMap = new NameMap();
        final ValueMap valueMap = new ValueMap() //
                .withLong(COLON_LAST_UPDATE_DATE, barcode.getLastUpdateDate().getTime());
        final StringBuilder updateExp = new StringBuilder("add ");
        barcode.getCodes().keySet().forEach(key -> {
            nameMap.with("#customerId" + key, key);
            valueMap.withStringSet(":code" + key, barcode.getCodes().get(key));
            updateExp.append("codes.#customerId" + key + " :code" + key + ",");
        });
        updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        updateExp.append(String.format(" SET %s=%s", //
                LAST_UPDATE_DATE, COLON_LAST_UPDATE_DATE));
        final UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(ID,
                barcode.getId(), //
                TYPE, barcode.getType()) //
                .withUpdateExpression(updateExp.toString()) //
                .withNameMap(nameMap) //
                .withValueMap(valueMap) //
                .withConditionExpression("attribute_exists(codes)");
        table.updateItem(updateItemSpec);
    }

    @Override
    public void link(final Barcode barcode) {
        try {
            update(barcode);
        } catch (final ConditionalCheckFailedException e) {
            logger.debug(e.getMessage(), e);
            try {
                insert(barcode);
            } catch (final ConditionalCheckFailedException e2) {
                logger.debug(e2.getMessage(), e2);
                update(barcode);
            }
        }
    }

    @Override
    public void unlink(final Barcode barcode) {
        final NameMap nameMap = new NameMap();
        final ValueMap valueMap = new ValueMap() //
                .withLong(COLON_LAST_UPDATE_DATE, barcode.getLastUpdateDate().getTime());
        final StringBuilder updateExp = new StringBuilder("DELETE ");
        barcode.getCodes().keySet().forEach(key -> {
            nameMap.with("#customerId" + key, key);
            valueMap.withStringSet(":code" + key, barcode.getCodes().get(key));
            updateExp.append("codes.#customerId" + key + " :code" + key + ",");
        });
        updateExp.deleteCharAt(updateExp.lastIndexOf(","));
        updateExp.append(String.format(" SET %s=%s", //
                LAST_UPDATE_DATE, COLON_LAST_UPDATE_DATE));
        final UpdateItemSpec updateItemSpec = new UpdateItemSpec() //
                .withPrimaryKey(ID, barcode.getId(), //
                        TYPE, barcode.getType()) //
                .withUpdateExpression(updateExp.toString()) //
                .withNameMap(nameMap) //
                .withValueMap(valueMap) //
                .withConditionExpression("attribute_exists(codes)");
        table.updateItem(updateItemSpec);
    }
}
