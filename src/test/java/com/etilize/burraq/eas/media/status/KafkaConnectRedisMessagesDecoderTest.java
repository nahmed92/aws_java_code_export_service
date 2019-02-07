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

package com.etilize.burraq.eas.media.status;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;

import com.etilize.burraq.eas.kafka.redis.KafkaConnectRedisMessagesDecoder;
import com.etilize.burraq.eas.kafka.redis.KafkaConnectRedisUpsertMessagePayload;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;

/**
 * Functional tests for {@link KafkaConnectRedisMessagesDecoderTest}
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class KafkaConnectRedisMessagesDecoderTest extends AbstractIntegrationTest {

    private final KafkaConnectRedisMessagesDecoder kafkaConnectRedisMessageDecoder = new KafkaConnectRedisMessagesDecoder();

    @Test
    public void shouldGetSpecificationLocalesFromHSetCommandForCreateSpecificationStatusesFromPSSS() {
        final String base64EncodedMessagePayload = "{\"key\":\"cHJvZHVjdF9zdGF0dXNlczpwc3NzNjU=\",\"field\":\"ZGVfREU=\",\"value\":\"Q09NUExFVEVE\"}";
        final Optional<KafkaConnectRedisUpsertMessagePayload> payload =

                kafkaConnectRedisMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                        base64EncodedMessagePayload);
        final KafkaConnectRedisUpsertMessagePayload productSpecificationsStatusMessagePayload = payload.get();
        assertThat(productSpecificationsStatusMessagePayload.getKey(),
                is("product_statuses:psss65"));
        assertThat(productSpecificationsStatusMessagePayload.getField(), is("de_DE"));
        assertThat(productSpecificationsStatusMessagePayload.getValue(), is("COMPLETED"));
    }

    @Test
    public void shouldGetSpecificationLocalesFromHSetCommandForCreateMediaStatusFromPMSS() {
        final String base64EncodedMessagePayloadFromPMSS = "{\"key\":\"cHJvZHVjdF9tZWRpYV9zdGF0dXNlczptMWdhcmFuZA==\""
                + ",\"field\":\"ZnJfVVM=\", \"value\" :\"TkVX\"}";
        final Optional<KafkaConnectRedisUpsertMessagePayload> payload = kafkaConnectRedisMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                base64EncodedMessagePayloadFromPMSS);
        final KafkaConnectRedisUpsertMessagePayload productSpecificationsStatusMessagePayload = payload.get();
        assertThat(productSpecificationsStatusMessagePayload.getKey(),
                is("product_media_statuses:m1garand"));
        assertThat(productSpecificationsStatusMessagePayload.getField(), is("fr_US"));
        assertThat(productSpecificationsStatusMessagePayload.getValue(), is("NEW"));
    }

    @Test
    public void shouldGetSpecificationLocalesFromHSetCommandForAddNewSpecificationStatusFromPSSS() {
        final String base64EncodedMessagePayload = "{\"key\":\"cHJvZHVjdF9zdGF0dXNlczpwc3NzNzc=\",\"field\":\"ZW5fQ0E=\",\"value\":\"Q09NUExFVEVE\"}";
        final Optional<KafkaConnectRedisUpsertMessagePayload> payload = kafkaConnectRedisMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                base64EncodedMessagePayload);
        final KafkaConnectRedisUpsertMessagePayload productSpecificationsStatusMessagePayload = payload.get();
        assertThat(productSpecificationsStatusMessagePayload.getKey(),
                is("product_statuses:psss77"));
        assertThat(productSpecificationsStatusMessagePayload.getField(), is("en_CA"));
        assertThat(productSpecificationsStatusMessagePayload.getValue(), is("COMPLETED"));
    }

    @Test
    public void shouldGetSpecificationLocalesFromHSetCommandForAddNewMediaStatusFromPMSS() {
        final String base64EncodedUpsertMessagePayload = "{\"key\":\"cHJvZHVjdF9tZWRpYV9zdGF0dXNlczptMWdhcmFuZA==\","
                + "\"field\":\"ZnJfVVM=\", \"value\" :\"TkVX\" }";
        final Optional<KafkaConnectRedisUpsertMessagePayload> payload = kafkaConnectRedisMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                base64EncodedUpsertMessagePayload);
        final KafkaConnectRedisUpsertMessagePayload productSpecificationsStatusMessagePayload = payload.get();
        assertThat(productSpecificationsStatusMessagePayload.getKey(),
                is("product_media_statuses:m1garand"));
        assertThat(productSpecificationsStatusMessagePayload.getField(), is("fr_US"));
        assertThat(productSpecificationsStatusMessagePayload.getValue(), is("NEW"));
    }

    @Test
    public void shouldReturnEmptyWhenMessageIsNotForSpecificationStatusesFromPSSS() {
        final String base64EncodedMessagePayloadForStatuses = "{\"key\":\"c3RhdHVzZXM6Tk9UX0ZPVU5E\","
                + "\"fields\":{\"X2NsYXNz\":\"Y29tLmV0aWxpemUuYnVycmFxLnBzc3Muc3RhdHVzLlN0YXR1cw==\","
                + "\"aWQ=\":\"Tk9UX0ZPVU5E\",\"bmFtZQ==\":\"Tm90IEZvdW5k\","
                + "\"ZGVzY3JpcHRpb24=\":\"UHJvZHVjdCBpcyBub3QgZm91bmQ=\",\"Z3JvdXA=\":\"SVNTVUU=\"}}";
        final Optional<KafkaConnectRedisUpsertMessagePayload> productSpecificationsStatusMessagePayload = kafkaConnectRedisMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                base64EncodedMessagePayloadForStatuses);
        assertThat(productSpecificationsStatusMessagePayload.isPresent(), is(false));
    }

    @Test
    public void shouldReturnEmptyWhenMessageIsNotForMediaStatusesFromPMSSS() {
        final String base64EncodedMessagePayloadForStatuses = "{\"key\":\"c3RhdHVzZXM6WUVTX05PVF9GT1VORA==\","
                + "\"fields\":{\"X2NsYXNz\":\"Y29tLmV0aWxpemUuYnVycmFxLnBtc3Muc3RhdHVzLlN0YXR1cw==\","
                + "\"aWQ=\":\"WUVTX05PVF9GT1VORA==\",\"bmFtZQ==\":\"WWVzIE5vdCBGb3VuZA==\","
                + "\"ZGVzY3JpcHRpb24=\":\"UkMgaXMgbm90IGZvdW5k\",\"Z3JvdXA=\":\"SVNTVUU=\"}}";
        final Optional<KafkaConnectRedisUpsertMessagePayload> productSpecificationsStatusMessagePayload = kafkaConnectRedisMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                base64EncodedMessagePayloadForStatuses);
        assertThat(productSpecificationsStatusMessagePayload.isPresent(), is(false));
    }

    @Test
    public void shouldGetProductIdToDeleteAllSpecificationsStatusesFromPSSS() {
        final String base64EncodedMessagePayload = "{\"key\":\"cHJvZHVjdF9zdGF0dXNlczpwc3NzNzc=\",\"fields\":[\"ZGVfREU=\",\"c3BfU1A=\"]}";
        final Optional<String> productId = kafkaConnectRedisMessageDecoder.extractProductIdFromDeleteMessage(
                base64EncodedMessagePayload);
        assertThat(productId.isPresent(), is(true));
        assertThat(productId.get(), is("psss77"));
    }

    @Test
    public void shouldGetProductIdToDeleteAllMediaStatusesFromPMSSS() {
        final String base64EncodedDeleteMessagePayload = "{\"key\":\"cHJvZHVjdF9tZWRpYV9zdGF0dXNlczptMWdhcmFuZA==\""
                + ",\"fields\":[\"ZGVfREU=\"]}";
        final Optional<String> productId = kafkaConnectRedisMessageDecoder.extractProductIdFromDeleteMessage(
                base64EncodedDeleteMessagePayload);
        assertThat(productId.isPresent(), is(true));
        assertThat(productId.get(), is("m1garand"));
    }
}
