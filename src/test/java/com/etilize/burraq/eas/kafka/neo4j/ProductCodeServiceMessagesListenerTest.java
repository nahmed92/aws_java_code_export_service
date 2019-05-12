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

package com.etilize.burraq.eas.kafka.neo4j;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.etilize.burraq.eas.accessory.AccessoryService;
import com.etilize.burraq.eas.barcode.BarcodeService;
import com.etilize.burraq.eas.customer.code.CustomerCodeService;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.common.collect.Maps;

public class ProductCodeServiceMessagesListenerTest extends AbstractIntegrationTest {

    private KafkaConnectNeo4jMessagesReceiver mr;

    @Mock
    private BarcodeService barcodeService;

    @Mock
    private AccessoryService accessoryService;

    @Mock
    private CustomerCodeService customerCodeService;

    @Before
    public void init() {
        mr = new KafkaConnectNeo4jMessagesReceiver(barcodeService, accessoryService,
                customerCodeService);
    }

    @Test
    public void shouldCreateProductMessage() {
        final Map<String, Object> headers = Maps.newLinkedHashMap();
        final String createProductWithCustomerCodesMessageValue = "{\"payload\":{\"id\":\"1620892\",\"start\":{\"id\":\"2318213\",\"labels\":[\"Product\"]},\"end\":{\"id\":\"2318215\",\"labels\":[\"CustomerCode\"]},\"before\":null,\"after\":{\"properties\":{\"market\":\"US\",\"code\":\"test1ss2ss5566544\",\"productId\":\"a2703865-1f83-4cea-b037-39c83ce83761\",\"customerId\":\"Valvolinen\"}},\"label\":\"IDENTIFIED_BY\",\"type\":\"relationship\"},\"meta\":{\"timestamp\":1554111179578,\"username\":\"neo4j\",\"txId\":975021,\"txEventId\":1,\"txEventsCount\":2,\"operation\":\"created\",\"source\":{\"hostname\":\"vmnode0018\"}},\"schema\":{\"properties\":[],\"constraints\":null}}";
        final Message<String> message = new GenericMessage<String>(
                createProductWithCustomerCodesMessageValue, headers);
        doNothing() //
                .when(customerCodeService).save("a2703865-1f83-4cea-b037-39c83ce83761",
                        "US", "test1ss2ss5566544", "Valvolinen");
        mr.processProductCodeServiceMessages(message);
        verify(customerCodeService, times(1)).save("a2703865-1f83-4cea-b037-39c83ce83761",
                "US", "test1ss2ss5566544", "Valvolinen");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowJsonParseExceptionWhenJsonIsNotValid() {
        final Map<String, Object> headers = Maps.newLinkedHashMap();
        final String payload = "\"e400�0test_user@etilizepak.com�����ZHa2703865-1f83-4cea-b037-39c83ce83761token 1\"";
        final Message<String> message = new GenericMessage<String>(payload, headers);
        mr.processProductCodeServiceMessages(message);
    }
}
