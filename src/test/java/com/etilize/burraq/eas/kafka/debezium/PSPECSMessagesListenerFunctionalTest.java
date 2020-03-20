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

package com.etilize.burraq.eas.kafka.debezium;

import static com.etilize.burraq.eas.kafka.debezium.DebeziumMessageProperties.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;

import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.mockito.Mock;

import com.etilize.burraq.eas.category.specification.CategorySpecificationService;
import com.etilize.burraq.eas.media.specification.ProductMediaSpecificationService;
import com.etilize.burraq.eas.specification.ProductSpecificationService;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.common.collect.Maps;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

/**
 * Contains functional tests for product-specifications-service message listener.
 *
 * @author Affan Hasan
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/detailed_specifications/detailed_specifications.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class PSPECSMessagesListenerFunctionalTest extends AbstractIntegrationTest {

    private KafkaConnectDebeziumMessagesReceiver messageReceiver;

    @Mock
    private ProductMediaSpecificationService mediaSpecificationsService;

    @Mock
    private ProductSpecificationService specificationService;

    @Mock
    private CategorySpecificationService categorySpecificationService;

    @Override
    public void before() {
        messageReceiver = new KafkaConnectDebeziumMessagesReceiver(
                new DebeziumMessageParser(), mediaSpecificationsService,
                specificationService, new PSPECSMessageParser(),
                categorySpecificationService);
    }

    @Test
    public void shouldProcessAssociateCategoryCommand() throws IOException {
        final GenericData.Record associateCategoryCommand = DebeziumMessageTestFixtures.getPSPECSAssociateCategoryCommandValueObject();
        final ConsumerRecord<Object, String> key = DebeziumMessageTestFixtures.getPSPECSDebeziumMessagesKeyObjectForProductId(
                "ppsh");
        doNothing().when(specificationService) //
                .createProduct("ppsh", "ind1", "cat1");
        messageReceiver.processProductSpecificationUpdates(associateCategoryCommand, key);
        verify(specificationService, times(1)).createProduct("ppsh", "ind1", "cat1");
    }

    @Test
    public void shouldProcessAddProductLocaleMessage() throws IOException {
        final GenericData.Record addProductLocaleMessage = DebeziumMessageTestFixtures.getAddProductLocaleMessage();
        final ConsumerRecord<Object, String> key = DebeziumMessageTestFixtures.getPSPECSDebeziumMessagesKeyObjectForProductId(
                "ppsh");
        doNothing().when(specificationService) //
                .addLocale("ppsh", "en_US");
        messageReceiver.processProductSpecificationUpdates(addProductLocaleMessage, key);
        verify(specificationService, times(1)).addLocale("ppsh", "en_US");
    }

    @Test
    public void shouldUpdateProductCategoryForAllLocales() throws IOException {
        final String industryId = "industry123";
        final String productId = "123";
        final String updateCategoryId = "update category id";
        //Given
        final GenericData.Record message = DebeziumMessageTestFixtures.getUpdateProductCategoryIdMessageData();
        final ConsumerRecord<Object, String> key = DebeziumMessageTestFixtures.getSpecificationUpdateCommandKeyData();
        //When
        final Map<String, Object> metaData = Maps.newLinkedHashMap();
        metaData.put(INDUSTRY_ID, industryId);
        metaData.put(CATEGORY_ID, "originalCategoryId");
        messageReceiver.processProductSpecificationUpdates(message, key);
        verify(specificationService, times(1)).updateProductCategory(productId,
                updateCategoryId);
    }
}
