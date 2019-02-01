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

import static com.etilize.burraq.eas.kafka.redis.KafkaConnectRedisMessageProperties.*;
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
    public void shouldGetSpecificationLocalesFromHMSetCommandForCreateSpecificationStatusesFromPSSS() {
        final String base64EncodedMessagePayload = "{\"key\":\"cHJvZHVjdF9zdGF0dXNlczpwcm9kdWN0MTIz\",\"fields\":{\"ZW5fVVM=\":\"TkVX\",\"aWQ=\":\"cHJvZHVjdDEyMw==\",\"X2NsYXNz\":\"Y29tLmV0aWxpemUuYnVycmFxLnBzc3MucHJvZHVjdHN0YXR1cy5Qcm9kdWN0U3RhdHVz\"}}";
        final Optional<KafkaConnectRedisUpsertMessagePayload> payload = kafkaConnectRedisMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                base64EncodedMessagePayload);
        final KafkaConnectRedisUpsertMessagePayload productSpecificationsStatusMessagePayload = payload.get();
        assertThat(productSpecificationsStatusMessagePayload.getKey(),
                is("product_statuses:product123"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .size(), is(2));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get("en_US"), is("NEW"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get(ID), is("product123"));
    }

    @Test
    public void shouldGetSpecificationLocalesFromHMSetCommandForCreateMediaStatusFromPMSS() {
        final String base64EncodedMessagePayloadFromPMSS = "{\"key\":\"cHJvZHVjdF9tZWRpYV9zdGF0dXNlczptMWdhcmFuZA==\""
                + ",\"fields\":{\"ZnJfVVM=\":\"TkVX\",\"ZW5fVVM=\":\"TkVX\","
                + "\"aWQ=\":\"bTFnYXJhbmQ=\"," + "\"X2NsYXNz\""
                + ":\"Y29tLmV0aWxpemUuYnVycmFxLnBtc3MucHJvZHVjdG1lZGlhLnN0YXR1cy5Qcm9kdWN0TWVkaWFTdGF0dXM=\"}}";
        final Optional<KafkaConnectRedisUpsertMessagePayload> payload = kafkaConnectRedisMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                base64EncodedMessagePayloadFromPMSS);
        final KafkaConnectRedisUpsertMessagePayload productSpecificationsStatusMessagePayload = payload.get();
        assertThat(productSpecificationsStatusMessagePayload.getKey(),
                is("product_media_statuses:m1garand"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .size(), is(3));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get("en_US"), is("NEW"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get("fr_US"), is("NEW"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get(ID), is("m1garand"));
    }

    @Test
    public void shouldGetSpecificationLocalesFromHMSetCommandForAddNewSpecificationStatusFromPSSS() {
        final String base64EncodedMessagePayload = "{\"key\":\"cHJvZHVjdF9zdGF0dXNlczpwcm9kdWN0MTIz\","
                + "\"fields\":{\"aWQ=\":\"cHJvZHVjdDEyMw==\","
                + "\"X2NsYXNz\":\"Y29tLmV0aWxpemUuYnVycmFxLnBzc3MucHJvZHVjdHN0YXR1cy5Qcm9kdWN0U3RhdHVz\","
                + "\"ZW5fVUs=\":\"TkVX\", \"ZW5fVVM=\":\"TkVX\"}}";
        final Optional<KafkaConnectRedisUpsertMessagePayload> payload = kafkaConnectRedisMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                base64EncodedMessagePayload);
        final KafkaConnectRedisUpsertMessagePayload productSpecificationsStatusMessagePayload = payload.get();
        assertThat(productSpecificationsStatusMessagePayload.getKey(),
                is("product_statuses:product123"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .size(), is(3));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get("en_UK"), is("NEW"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get("en_US"), is("NEW"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get(ID), is("product123"));
    }

    @Test
    public void shouldGetSpecificationLocalesFromHMSetCommandForAddNewMediaStatusFromPMSS() {
        final String base64EncodedUpsertMessagePayload = "{\"key\":\"cHJvZHVjdF9tZWRpYV9zdGF0dXNlczptMWdhcmFuZA==\","
                + "\"fields\":{\"ZnJfVVM=\":\"TkVX\",\"YXJfS1NB\":\"Q09NUExFVEVE\""
                + ",\"ZW5fVVM=\":\"TkVX\",\"aWQ=\":\"bTFnYXJhbmQ=\""
                + ",\"X2NsYXNz\":\"Y29tLmV0aWxpemUuYnVycmFxLnBtc3MucHJvZHVjdG1lZGlhLnN0YXR1cy5Qcm9kdWN0TWVkaWFTdGF0dXM=\"}}";
        final Optional<KafkaConnectRedisUpsertMessagePayload> payload = kafkaConnectRedisMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                base64EncodedUpsertMessagePayload);
        final KafkaConnectRedisUpsertMessagePayload productSpecificationsStatusMessagePayload = payload.get();
        assertThat(productSpecificationsStatusMessagePayload.getKey(),
                is("product_media_statuses:m1garand"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .size(), is(4));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get("fr_US"), is("NEW"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get("en_US"), is("NEW"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get("ar_KSA"), is("COMPLETED"));
        assertThat(productSpecificationsStatusMessagePayload.getFields() //
                .get(ID), is("m1garand"));
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
        final String base64EncodedMessagePayload = "{\"key\":\"cHJvZHVjdF9zdGF0dXNlcw==\""
                + ",\"members\":[\"cHJvZHVjdDEyMw==\"]}";
        final Optional<String> productId = kafkaConnectRedisMessageDecoder.extractProductIdFromDeleteMessage(
                base64EncodedMessagePayload);
        assertThat(productId.isPresent(), is(true));
        assertThat(productId.get(), is("product123"));
    }

    @Test
    public void shouldGetProductIdToDeleteAllMediaStatusesFromPMSSS() {
        final String base64EncodedDeleteMessagePayload = "{\"key\":\"cHJvZHVjdF9tZWRpYV9zdGF0dXNlcw==\""
                + ",\"members\":[\"bTFnYXJhbmQ=\"]}";
        final Optional<String> productId = kafkaConnectRedisMessageDecoder.extractProductIdFromDeleteMessage(
                base64EncodedDeleteMessagePayload);
        assertThat(productId.isPresent(), is(true));
        assertThat(productId.get(), is("m1garand"));
    }
}
