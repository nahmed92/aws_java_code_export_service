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

import static com.etilize.burraq.eas.media.specification.Status.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.mockito.Mock;

import com.etilize.burraq.eas.media.specification.ProductMediaAttributeValue;
import com.etilize.burraq.eas.media.specification.ProductMediaSpecificationService;
import com.etilize.burraq.eas.specification.ProductSpecificationService;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;

/**
 * Contains integration tests for product-media-service message listener.
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class PMSMessagesListenerTest extends AbstractIntegrationTest {

    KafkaConnectDebeziumMessagesReceiver messageReceiver;

    @Mock
    ProductMediaSpecificationService mediaSpecificationService;

    @Mock
    ProductSpecificationService specificationService;

    @Override
    public void before() {
        messageReceiver = new KafkaConnectDebeziumMessagesReceiver(
                new DebeziumMessageParser(), mediaSpecificationService,
                specificationService, new PSPECSMessageParser());
    }

    @Test
    public void shouldProcessAddProductLocaleMessageFromPMS() throws IOException {
        final GenericData.Record addProductLocaleMessage = DebeziumMessageTestFixtures.getPMSAddLocaleMessageValueObject();
        final ConsumerRecord<Object, String> key = DebeziumMessageTestFixtures.getPMSDebeziumMessagesKeyObject();
        doNothing().when(mediaSpecificationService) //
                .addLocale("mp5", "en_UK");
        messageReceiver.processProductMediaServiceMessages(addProductLocaleMessage, key);
        verify(mediaSpecificationService).addLocale("mp5", "en_UK");
    }

    @Test
    public void shouldProcessProductMediaEventFromPMSWhenURLIsProvidedAndStatusAssociated()
            throws IOException {
        final GenericData.Record addProductLocaleMessage = DebeziumMessageTestFixtures.getPMSProductMediaEventValueObjectWithURLAndStatusAssociated();
        final ConsumerRecord<Object, String> key = DebeziumMessageTestFixtures.getPMSDebeziumMessagesKeyObject();
        final ProductMediaAttributeValue value = new ProductMediaAttributeValue();
        value.setUrl("http://www.google.com");
        doNothing().when(mediaSpecificationService) //
                .saveAttribute("mp5", "de_DE", "1", ASSOCIATED, value);
        messageReceiver.processProductMediaServiceMessages(addProductLocaleMessage, key);
        verify(mediaSpecificationService, times(1)).saveAttribute("mp5", "de_DE", "1",
                ASSOCIATED, value);
    }

    @Test
    public void shouldProcessProductMediaEventFromPMSWhenStatusIsPending()
            throws IOException {
        final GenericData.Record addProductLocaleMessage = DebeziumMessageTestFixtures.getPMSProductMediaEventValueObjectWithoutURLAndStatusPending();
        final ConsumerRecord<Object, String> key = DebeziumMessageTestFixtures.getPMSDebeziumMessagesKeyObject();
        doNothing().when(mediaSpecificationService) //
                .saveAttribute("mp5", "en_US", "1", PENDING,
                        new ProductMediaAttributeValue());
        messageReceiver.processProductMediaServiceMessages(addProductLocaleMessage, key);
        verify(mediaSpecificationService, times(1)).saveAttribute("mp5", "en_US", "1",
                PENDING, new ProductMediaAttributeValue());
    }
}
