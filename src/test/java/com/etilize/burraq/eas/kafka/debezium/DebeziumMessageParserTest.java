/*
 * #region
 * export-aggregation-service
 * %%
 * Copyright (C) 2018 Etilize
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

import static com.etilize.burraq.eas.kafka.debezium.DebeziumMessageKeys.*;
import static com.etilize.burraq.eas.kafka.debezium.DebeziumMessageProperties.*;
import static com.etilize.burraq.eas.media.specification.Status.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Before;
import org.junit.Test;

import com.etilize.burraq.eas.media.specification.ProductMediaAttributeValue;
import com.google.common.collect.Maps;

import static org.hamcrest.MatcherAssert.*;

/**
 * Contains functional test(s) for {@link DebeziumMessageParser}.
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class DebeziumMessageParserTest {

    private DebeziumMessageParser parser;

    @Before
    public void init() {
        parser = new DebeziumMessageParser();
    }

    @Test
    public void shouldExtractProductLocaleFromAddLocaleCommand() throws IOException {
        final Optional<String> localeOptional = parser.extractProductLocaleFromAddLocaleCommand(
                DebeziumMessageTestFixtures.getAddProductLocaleCommandValueData());
        assertThat(localeOptional.isPresent(), is(true));
        assertThat(localeOptional.get(), is("de_DE"));
    }

    @Test
    public void shouldExtractOperationTypeUpdateForAddProductLocaleCommand()
            throws FileNotFoundException, IOException {
        final Optional<String> operationOptional = parser.extractOperationType(
                DebeziumMessageTestFixtures.getAddProductLocaleCommandValueData());
        assertThat(operationOptional.isPresent(), is(true));
        assertThat(operationOptional.get(), is(OPERATION_UPDATE));
    }

    @Test
    public void shouldExtractUpdateOperationTypeAsAddProductLocale()
            throws FileNotFoundException, IOException {
        final Optional<String> operationTypeOptional = parser.extractUpdateOperationType(
                DebeziumMessageTestFixtures.getAddProductLocaleCommandValueData());
        assertThat(operationTypeOptional.isPresent(), is(true));
        assertThat(operationTypeOptional.get(), is(UPDATE_OPERATION_ADD_PRODUCT_LOCALE));
    }

    @Test
    public void shouldExtractUpdateOperationTypeAsUpdateProductSpecification()
            throws FileNotFoundException, IOException {
        final Optional<String> operationOptional = parser.extractUpdateOperationType(
                DebeziumMessageTestFixtures.getUpdateProductSpecsAddValueAttributeValueData());
        assertThat(operationOptional.isPresent(), is(true));
        assertThat(operationOptional.get(),
                is(UPDATE_OPERATION_UPDATE_PRODUCT_SPECIFICATION));
    }

    @Test
    public void shouldExtractProductIdFromPMSMessageKey()
            throws FileNotFoundException, IOException {
        final ConsumerRecord<Object, String> key = DebeziumMessageTestFixtures.getPMSDebeziumMessagesKeyObject();
        final String productId = parser.getProductIdFromDebeziumMessageKey(key);
        assertThat(productId, is("mp5"));
    }

    @Test
    public void shouldReturnTrueForAddProductLocaleMessageFromPMS()
            throws FileNotFoundException, IOException {
        final GenericData.Record addProductLocaleMessageObject = DebeziumMessageTestFixtures.getPMSAddLocaleMessageValueObject();
        final boolean flag = parser.isAddProductLocaleMessageFromPMS(
                addProductLocaleMessageObject);
        assertThat(flag, is(true));
    }

    @Test
    public void shouldReturnFalseForProductMediaEventFromPMS()
            throws FileNotFoundException, IOException {
        final GenericData.Record addProductLocaleMessageObject = DebeziumMessageTestFixtures.getPMSProductMediaEventValueObjectWithoutURLAndStatusPending();
        final boolean flag = parser.isAddProductLocaleMessageFromPMS(
                addProductLocaleMessageObject);
        assertThat(flag, is(false));
    }

    @Test
    public void shouldReturnPMSProductMediaEventRequest()
            throws FileNotFoundException, IOException {
        final GenericData.Record productMediaEventObject = DebeziumMessageTestFixtures.getPMSProductMediaEventValueObjectWithURLAndStatusAssociated();
        final ConsumerRecord<Object, String> key = DebeziumMessageTestFixtures.getPMSDebeziumMessagesKeyObject();
        final PMSProductMediaEventRequest pMSProductMediaEventRequest = parser.getPMSProductMediaEventRequest(
                productMediaEventObject, key);
        assertThat(pMSProductMediaEventRequest.getProductId(), is("mp5"));
        assertThat(pMSProductMediaEventRequest.getLocaleId(), is("de_DE"));
        assertThat(pMSProductMediaEventRequest.getAttributeId(), is("1"));
        assertThat(pMSProductMediaEventRequest.getStatus(), is(ASSOCIATED));
    }

    @Test
    public void shouldReturnPSPECSAssociateCategoryCommand()
            throws FileNotFoundException, IOException {
        final GenericData.Record associateCategoryCommandObject = DebeziumMessageTestFixtures.getPSPECSAssociateCategoryCommandValueObject();
        final ConsumerRecord<Object, String> key = DebeziumMessageTestFixtures.getPSPECSDebeziumMessagesKeyObjectForProductId(
                "ppsh");
        final AssociateCategoryCommand pspecsAssociateCategoryCommandRequest = parser.getAssociateCategoryCommand(
                associateCategoryCommandObject, key);
        assertThat(pspecsAssociateCategoryCommandRequest.getProductId(), is("ppsh"));
        assertThat(pspecsAssociateCategoryCommandRequest.getIndustryId(), is("ind1"));
        assertThat(pspecsAssociateCategoryCommandRequest.getCategoryId(), is("cat1"));
    }

    @Test
    public void shouldExtractCategoryIdFromUpdateMessage() throws IOException {
        final Optional<String> categoryIdOptional = parser.extractCategoryIdFromUpdateAttributeMessage(
                DebeziumMessageTestFixtures.getUpdateProductCategoryIdMessageData());
        assertThat(categoryIdOptional.isPresent(), is(true));
        assertThat(categoryIdOptional.get(), is("update category id"));
    }

    @Test
    public void shouldExtractUpdateOperationTypeAsUpdateProductCategory()
            throws FileNotFoundException, IOException {
        final Optional<String> operationTypeOptional = parser.extractUpdateOperationType(
                DebeziumMessageTestFixtures.getUpdateProductCategoryIdMessageData());
        assertThat(operationTypeOptional.isPresent(), is(true));
        assertThat(operationTypeOptional.get(),
                is(UPDATE_OPERATION_UPDATE_PRODUCT_CATEGORY_ID));
    }

}
