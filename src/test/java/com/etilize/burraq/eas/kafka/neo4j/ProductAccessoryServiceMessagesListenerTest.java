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

public class ProductAccessoryServiceMessagesListenerTest extends AbstractIntegrationTest {

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
        final String createProductWithOutAsseccoriesMessageValue = "{\"payload\":{\"id\":\"1620576\",\"start\":{\"id\":\"2318393\",\"labels\":[\"Product\"]},\"end\":{\"id\":\"2316815\",\"labels\":[\"Product\"]},\"before\":null,\"after\":{\"properties\":{\"markets\":[\"US\"],\"productId\":\"e205cc8b-e7d3-4fb5-b10d-e0a6715d76ac\",\"accessoryId\":\"5e24567a-074a-48a4-9989-7506eff91f97\"}},\"label\":\"HAS_ACCESSORY\",\"type\":\"relationship\"},\"meta\":{\"timestamp\":1554118111953,\"username\":\"neo4j\",\"txId\":975090,\"txEventId\":0,\"txEventsCount\":1,\"operation\":\"created\",\"source\":{\"hostname\":\"vmnode0018\"}},\"schema\":{\"properties\":[],\"constraints\":null}}";
        final Message<String> message = new GenericMessage<String>(
                createProductWithOutAsseccoriesMessageValue, headers);
        doNothing() //
                .when(accessoryService).save("e205cc8b-e7d3-4fb5-b10d-e0a6715d76ac", "US",
                        "5e24567a-074a-48a4-9989-7506eff91f97");
        mr.processProductAsseccoryServiceMessages(message);
        verify(accessoryService, times(1)).save("e205cc8b-e7d3-4fb5-b10d-e0a6715d76ac",
                "US", "5e24567a-074a-48a4-9989-7506eff91f97");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowJsonParseExceptionWhenJsonIsNotValid() {
        final Map<String, Object> headers = Maps.newLinkedHashMap();
        final String payload = "\"e400�0test_user@etilizepak.com�����ZHa2703865-1f83-4cea-b037-39c83ce83761token 1\"";
        final Message<String> message = new GenericMessage<String>(payload, headers);
        mr.processProductAsseccoryServiceMessages(message);
    }
}
