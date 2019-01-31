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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Decode base64 string messages from PSSS.
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
     * Converts base 64 encoded Json string payload to {@link ProductSpecificationsStatusUpsertMessagePayload}, 
     * <br>following is the sample base 64 encoded json message.</br>
     * <br>Encoded:</br>
     * <p>{\"key\":\"cHJvZHVjdF9zdGF0dXNlczrYp9mE2YXYsdin2LnZig==\",\"fields\":{\"YXJfS1NB\":\"TkVX\",\"aWQ=\":\"2KfZhNmF2LHYp9i52Yo=\",\"X2NsYXNz\":\"Y29tLmV0aWxpemUuYnVycmFxLnBzc3MucHJvZHVjdHN0YXR1cy5Qcm9kdWN0U3RhdHVz\"}}</p>
     * Decoded:
     * <p>{ \"key\":\"product_statuses:product123\", \"fields\":{"ar_KSA":"NEW","id":"product123","_class":"com.etilize.burraq.psss.productstatus.ProductStatus"} }</p>
     *
     * @param message {@link String} Json payload
     * @return {@link ProductSpecificationsStatusUpsertMessagePayload} null when message is not related to "product_statuses"
     */
    public Optional<ProductSpecificationsStatusUpsertMessagePayload> convertJsonToProductSpecificationsStatusUpsertMessagePayload(
            final String message) {
        Optional<ProductSpecificationsStatusUpsertMessagePayload> productSpecificationsStatusUpsertMessagePayload = Optional.empty();
        final JsonObject payloadJson = jsonParser.parse(message) //
                .getAsJsonObject();
        payloadJson.addProperty(KEY, this.decodeBase64String(payloadJson.get(KEY) //
                .getAsString()));
        final String key = payloadJson.get(KEY) //
                .getAsString();
        if (key.startsWith(PRODUCT_STATUSES)) { // Verify it is "product_statuses" message
            payloadJson.add(FIELDS,
                    decodeBase64JsonObject(payloadJson.get(FIELDS) //
                            .getAsJsonObject()) //
                                    .getAsJsonObject());
            productSpecificationsStatusUpsertMessagePayload = Optional.of(
                    new Gson().fromJson(payloadJson.toString(),
                            ProductSpecificationsStatusUpsertMessagePayload.class));
            productSpecificationsStatusUpsertMessagePayload //
                    .get() //
                    .getFields() //
                    .remove(CLASS);
        }
        return productSpecificationsStatusUpsertMessagePayload;
    }

    /**
     * Extracts product id from PSSS "delete product specifications" Json message.
     * <br>following is the sample base 64 encoded json message.</br>
     * <br>Encoded:</br>
     * <p>{\"key\":\"cHJvZHVjdF9zdGF0dXNlcw==\",\"members\":[\"cHJvZHVjdDEyMw==\"]}</p>
     * Decoded:
     * <p>{\"key\":\"product_statuses:product123\",\"members\":[\"product123\"]}</p>
     * @param message {@link String} message payload
     * @return {@link Optional<String>} containing product id.
     */
    public Optional<String> extractProductIdFromDeleteSpecificationsStatusesMessage(
            final String message) {
        final Optional<String> productId;
        final JsonObject payloadJson = jsonParser.parse(message) //
                .getAsJsonObject();
        payloadJson.addProperty(KEY, decodeBase64String(payloadJson.get(KEY) //
                .getAsString()));
        if (payloadJson.get(KEY) //
                .getAsString() //
                .startsWith(PRODUCT_STATUSES)) {// Verify it is "product_statuses" delete "product specifications" message
            payloadJson.add(MEMBERS, decodeBase64JsonObject(payloadJson.get(MEMBERS)) // decoding from Base 64
                    .getAsJsonArray());
            productId = Optional.of(payloadJson.get(MEMBERS) //
                    .getAsJsonArray() //
                    .get(0) //
                    .getAsString());
        } else {
            productId = null;
        }
        return productId;
    }

    private JsonElement decodeBase64JsonObject(final JsonElement payloadJsonElement) {
        final Matcher m = Pattern.compile("[\\w=]+") //
                .matcher(payloadJsonElement.toString());
        final StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, decodeBase64String(m.group()));
        }
        m.appendTail(sb);
        return jsonParser.parse(sb.toString());
    }

    private String decodeBase64String(final String base64EncodedString) {
        return new String(
                Base64.decodeBase64(base64EncodedString.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
    }
}
