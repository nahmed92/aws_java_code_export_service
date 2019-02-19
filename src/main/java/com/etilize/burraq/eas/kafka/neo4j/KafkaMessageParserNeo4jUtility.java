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

import com.etilize.burraq.eas.barcode.BarcodeKafkaMesssagePojo;
import com.google.gson.JsonObject;

/**
 * Class that parse Kafka Message for Neo4j
 *
 * @author Nasir Ahmed
 * @since 1.0
 */
public class KafkaMessageParserNeo4jUtility {

    /**
     * Process barcode-service originated messages
     *
     * @param jsonRecord {@link JsonObject}
     *
     * @return {@link BarcodeKafkaMesssagePojoTest}
     */
    public static BarcodeKafkaMesssagePojo parseBarcodeKafkaMessage(
            final JsonObject jsonRecord) {
        // Parsing BACS Neo4j message
        //Get Operation that hold Operation information created or deleted
        final String operationType = jsonRecord.getAsJsonObject("value").getAsJsonObject(
                "meta").get("operation").getAsString();
        // PAYLOAD holds actual values like productId, type, code, customerId in before and after property and identify it's node data or relationship data
        final JsonObject PAYLOAD = jsonRecord.getAsJsonObject("value").getAsJsonObject(
                "payload");
        // BEFORE_OR_AFTER_PROPERTY holds actual value of productId, type, code, customerId
        final JsonObject BEFORE_OR_AFTER_PROPERTY = operationType.equals("deleted")
                ? PAYLOAD.getAsJsonObject("before") : PAYLOAD.getAsJsonObject("after");
        final JsonObject jsonObect = BEFORE_OR_AFTER_PROPERTY.getAsJsonObject(
                "properties");
        return new BarcodeKafkaMesssagePojo(jsonObect.get("productId").getAsString(), //
                jsonObect.get("type").getAsString(), //
                jsonObect.get("code").getAsString(), //
                jsonObect.get("customerId").getAsString(), //
                operationType, //
                PAYLOAD.get("type").getAsString());
    }
}
