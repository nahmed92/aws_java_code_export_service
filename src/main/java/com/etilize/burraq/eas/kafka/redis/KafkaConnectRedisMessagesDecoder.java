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

import static com.etilize.burraq.eas.kafka.redis.KafkaConnectRedisMessageProperties.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Decode base64 encoded string messages from PSSS & PMSS.
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class KafkaConnectRedisMessagesDecoder {

    private final JsonParser jsonParser;

    /**
     * Default constructor.
     */
    public KafkaConnectRedisMessagesDecoder() {
        jsonParser = new JsonParser();
    }

    /**
     * Converts base 64 encoded Json string payload to {@link KafkaConnectRedisUpsertMessagePayload},
     * <br>following is the sample base 64 encoded json message.</br>
     * <br>Encoded:</br>
     * <p>{"key":"cHJvZHVjdF9zdGF0dXNlczpwc3NzNjU=","field":"ZGVfREU=","value":"QWx0"}</p>
     * Decoded:
     * <p>{"key":"product_statuses:psss65","field":"de_DE","value":"Alt"}</p>
     *
     * @param message {@link String} Json payload
     * @return {@link KafkaConnectRedisUpsertMessagePayload} null when message is not related to "product_statuses"
     */
    public Optional<KafkaConnectRedisUpsertMessagePayload> convertJsonToKafkaConnectRedisUpsertMessagePayload(
            final String message) {
        Optional<KafkaConnectRedisUpsertMessagePayload> productSpecificationsStatusUpsertMessagePayload = Optional.empty();
        final JsonObject payloadJson = jsonParser.parse(message) //
                .getAsJsonObject();
        payloadJson.addProperty(KEY, this.decodeBase64String(payloadJson.get(KEY) //
                .getAsString()));
        final String key = payloadJson.get(KEY) //
                .getAsString();
        // Verify it is "product_statuses" (from PSSS) or "product_media_statuses" message from (PMSS)
        if (key.startsWith(PRODUCT_STATUSES) || key.startsWith(PRODUCT_MEDIA_STATUSES)) {
            payloadJson.addProperty(FIELD, this.decodeBase64String(payloadJson.get(FIELD) //
                    .getAsString()));
            payloadJson.addProperty(VALUE, this.decodeBase64String(payloadJson.get(VALUE) //
                    .getAsString()));
            productSpecificationsStatusUpsertMessagePayload = Optional.of(
                    new KafkaConnectRedisUpsertMessagePayload(payloadJson.get(KEY) //
                            .getAsString(), payloadJson.get(FIELD) //
                                    .getAsString(),
                            payloadJson.get(VALUE) //
                                    .getAsString()));
        }
        return productSpecificationsStatusUpsertMessagePayload;
    }

    /**
     * Converts base 64 encoded Json string payload to {@link KafkaConnectRedisUpsertMessagePayload},
     * <br>following is the sample base 64 encoded json message.</br>
     * <br>Encoded:</br>
     * <p>{"key":"cHJvZHVjdF9zdGF0dXNlczpwc3NzNjU=","field":"ZGVfREU=","value":"QWx0"}</p>
     * Decoded:
     * <p>{"key":"product_statuses:psss65","field":"de_DE","value":"Alt"}</p>
     *
     * @param message {@link String} Json payload
     * @return {@link KafkaConnectRedisUpsertMessagePayload} null when message is not related to "product_statuses"
     */
    public List<Optional<KafkaConnectRedisUpsertMessagePayload>> convertJsonToKafkaConnectRedisKeyStringValueHashMessagePayload(
            final String message) {

        List<Optional<KafkaConnectRedisUpsertMessagePayload>> statusMessagePayloadList = new ArrayList<>();

        Optional<KafkaConnectRedisUpsertMessagePayload> productSpecificationsStatusUpsertMessagePayload = Optional.empty();

        final JsonObject payloadJson = jsonParser.parse(message) //
                .getAsJsonObject();
        final String key = this.decodeBase64String(payloadJson.get(KEY) //
                .getAsString());

        // Verify it is "product_statuses" (from PSSS) or "product_media_statuses" message from (PMSS)
        if (key.startsWith(PRODUCT_STATUSES) || key.startsWith(PRODUCT_MEDIA_STATUSES)) {

            final JsonObject valueJson = jsonParser.parse(payloadJson.get(VALUE) //
                    .toString()) //
                    .getAsJsonObject();
            ;
            for (final Entry<String, JsonElement> entry : valueJson.entrySet()) {
                productSpecificationsStatusUpsertMessagePayload = Optional.of(
                        new KafkaConnectRedisUpsertMessagePayload(key,
                                this.decodeBase64String(entry.getKey()),
                                this.decodeBase64String(entry.getValue().getAsString())));
                statusMessagePayloadList.add(
                        productSpecificationsStatusUpsertMessagePayload);
            }

        }
        return statusMessagePayloadList;
    }

    /**
     * Extracts product id from delete message from PSSS & PMSS.
     * <br>following is the sample base 64 encoded json message.</br>
     * <br>Encoded:</br>
     * <p>{"key":"cHJvZHVjdF9zdGF0dXNlczpwc3NzNzc=","fields":["ZGVfREU=","c3BfU1A="]}</p>
     * Decoded:
     * <p>{"key":"cHJvZHVjdF9zdGF0dXNlczpwc3NzNzc=","fields":["de_DE","sp_SP"]}</p>
     *
     * @param message {@link String} message payload
     * @return {@link Optional<String>} containing product id.
     */
    public Optional<String> extractProductIdFromDeleteMessage(final String message) {
        final Optional<String> productId;
        final JsonObject payloadJson = jsonParser.parse(message) //
                .getAsJsonObject();
        payloadJson.addProperty(KEY, decodeBase64String(payloadJson.get(KEY) //
                .getAsString()));
        final String decodedkey = payloadJson.get(KEY) //
                .getAsString();
        // Verify it is "product_statuses" (from PSSS) or "product_media_statuses" (from PMSS)
        if (decodedkey.startsWith(PRODUCT_STATUSES)
                || decodedkey.startsWith(PRODUCT_MEDIA_STATUSES)) {
            productId = Optional.ofNullable(decodedkey.split(":")[1]); // splitting as key will be of form product_statuses:<productId>
        } else {
            productId = null;
        }
        return productId;
    }

    private String decodeBase64String(final String base64EncodedString) {
        return new String(
                Base64.decodeBase64(base64EncodedString.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
    }
}
