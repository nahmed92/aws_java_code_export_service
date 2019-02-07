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

package com.etilize.burraq.eas.kafka.redis;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.etilize.burraq.eas.media.status.MediaStatusService;
import com.etilize.burraq.eas.specification.status.SpecificationStatusService;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.common.collect.Maps;

/**
 * Contains integration tests for product-specification-status-service message listener.
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class PSSSMessagesListenerTest extends AbstractIntegrationTest {

    KafkaConnectRedisMessagesReceiver messageReceiver;

    @Mock
    SpecificationStatusService specificationStatusService;

    @Mock
    MediaStatusService mediaStatusService;

    @Override
    public void before() {
        messageReceiver = new KafkaConnectRedisMessagesReceiver(
                specificationStatusService, mediaStatusService);
    }

    @Test
    public void shouldCreateSpecificationStatusWhenNewProductCreatedInPSSS() {
        final String base64EncodedMessagePayload = "{\"key\":\"cHJvZHVjdF9zdGF0dXNlczpwc3NzNjU=\",\"field\":\"ZGVfREU=\",\"value\":\"Q09NUExFVEVE\"}";
        final Map<String, Object> headers = Maps.newLinkedHashMap();
        headers.put("kafka_receivedMessageKey",
                "com.moilioncircle.redis.replicator.cmd.impl.HSetCommand");
        final Message<String> message = new GenericMessage<String>(
                base64EncodedMessagePayload, headers);
        doNothing().when(specificationStatusService) //
                .save("psss65", "de_DE", "COMPLETED");
        messageReceiver.processProductSpecificationsStatusMessage(message);
        verify(specificationStatusService, times(1)).save(eq("psss65"), eq("de_DE"),
                eq("COMPLETED"));
    }

    @Test
    public void shouldDeleteAllSpecificationsWhenProductDeletedInPSSS() {
        final String base64EncodedMessagePayload = "{\"key\":\"cHJvZHVjdF9zdGF0dXNlczpwc3NzNzc=\",\"fields\":[\"ZGVfREU=\",\"c3BfU1A=\"]}";
        final Map<String, Object> headers = Maps.newLinkedHashMap();
        headers.put("kafka_receivedMessageKey",
                "com.moilioncircle.redis.replicator.cmd.impl.HDelCommand");
        final Message<String> message = new GenericMessage<String>(
                base64EncodedMessagePayload, headers);
        doNothing().when(specificationStatusService) //
                .deleteAllByProductId("psss777");
        messageReceiver.processProductSpecificationsStatusMessage(message);
        verify(specificationStatusService, times(1)).deleteAllByProductId(eq("psss77"));
    }

    /**
     * Represents scenario when a new Specification Locale is added or updated in a product.
     */
    @Test
    public void shouldCreateWhenNewLocaleIsAddedToAProductInPSSS() {
        final String base64EncodedMessagePayload = "{\"key\":\"cHJvZHVjdF9zdGF0dXNlczpwc3NzNzc=\",\"field\":\"ZW5fQ0E=\",\"value\":\"Q09NUExFVEVE\"}";
        final Map<String, Object> headers = Maps.newLinkedHashMap();
        headers.put("kafka_receivedMessageKey",
                "com.moilioncircle.redis.replicator.cmd.impl.HSetCommand");
        final Message<String> message = new GenericMessage<String>(
                base64EncodedMessagePayload, headers);
        doNothing().when(specificationStatusService) //
                .save("psss77", "en_CA", "COMPLETED");
        messageReceiver.processProductSpecificationsStatusMessage(message);
        verify(specificationStatusService, times(1)).save(eq("psss77"), eq("en_CA"),
                eq("COMPLETED"));
    }

    /**
     * Represents scenario when a message is not related to add/update/delete "product_specifications"
     */
    @Test
    public void shouldNotSaveWhenMessageIsNotProductStatuses() {
        final String base64EncodedMessagePayloadForStatuses = "{\"key\":\"c3RhdHVzZXM6Tk9UX0ZPVU5E\",\"fields\":{\"X2NsYXNz\":\"Y29tLmV0aWxpemUuYnVycmFxLnBzc3Muc3RhdHVzLlN0YXR1cw==\",\"aWQ=\":\"Tk9UX0ZPVU5E\",\"bmFtZQ==\":\"Tm90IEZvdW5k\",\"ZGVzY3JpcHRpb24=\":\"UHJvZHVjdCBpcyBub3QgZm91bmQ=\",\"Z3JvdXA=\":\"SVNTVUU=\"}}";
        final Map<String, Object> headers = Maps.newLinkedHashMap();
        headers.put("kafka_receivedMessageKey",
                "com.moilioncircle.redis.replicator.cmd.impl.HMSetCommand");
        final Message<String> message = new GenericMessage<String>(
                base64EncodedMessagePayloadForStatuses, headers);

        messageReceiver.processProductSpecificationsStatusMessage(message);
        verify(specificationStatusService, never()).save(anyString(), anyString(),
                anyString());
    }
}
