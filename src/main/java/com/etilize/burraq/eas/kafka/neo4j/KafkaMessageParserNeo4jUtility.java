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

import com.google.gson.JsonObject;

/**
 * Class that parse Kafka Message for Neo4j
 *
 * @author Nasir Ahmed
 * @since 1.0
 */
public final class KafkaMessageParserNeo4jUtility {

    private static final String BEFORE = "before";

    private static final String AFTER = "after";

    private static final String OPERATION = "operation";

    private static final String PROPERTIES = "properties";

    private static final String PRODUCTID = "productId";

    private static final String META = "meta";

    private static final String DELETED = "deleted";

    private static final String PAYLOAD = "payload";

    /**
     * Add to remove sonar error
     * sonar suggestion to add a private constructor to hide the implicit public one.
     */
    private KafkaMessageParserNeo4jUtility() {

    }

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
        final String operationType = jsonRecord.getAsJsonObject(META) //
                .get(OPERATION).getAsString();
        // payLoad holds actual values like productId, type, code, customerId in before and after property and identify it's node data or relationship data
        final JsonObject payLoad = jsonRecord.getAsJsonObject(PAYLOAD);
        // beforeAndAfterProperty holds actual value of productId, type, code, customerId
        final JsonObject beforeAndAfterProperty = operationType.equals(DELETED)
                ? payLoad.getAsJsonObject(BEFORE) : payLoad.getAsJsonObject(AFTER);
        final JsonObject jsonObect = beforeAndAfterProperty.getAsJsonObject(PROPERTIES);
        return new BarcodeKafkaMesssagePojo(jsonObect.get(PRODUCTID).getAsString(), //
                jsonObect.get("type").getAsString(), //
                jsonObect.get("code").getAsString(), //
                jsonObect.get("customerId").getAsString(), //
                operationType, //
                payLoad.get("type").getAsString());

    }

    /**
     * Process product-accessory-service originated messages
     *
     * @param jsonRecord {@link JsonObject}
     *
     * @return {@link AccessoryKafkaMesssagePojo}
     */
    public static AccessoryKafkaMesssagePojo parseAccessoriesKafkaMessage(
            final JsonObject jsonRecord) {
        // Parsing PAS Neo4j message
        //Get Operation that hold Operation information created or deleted
        final String operationType = jsonRecord.getAsJsonObject(META).get(
                OPERATION).getAsString();
        // payload holds actual values like productId, market, accessoryId, type in before
        // and after property and identify it's node data or relationship data
        final JsonObject payload = jsonRecord.getAsJsonObject(PAYLOAD);
        // beforeAndAfterProperty holds actual value of productId, type, code, customerId
        final JsonObject beforeAndAfterProperty = operationType.equals(DELETED)
                ? payload.getAsJsonObject(BEFORE) : payload.getAsJsonObject(AFTER);

        final JsonObject jsonObect = beforeAndAfterProperty.getAsJsonObject(PROPERTIES);
        // operationType property specify Node or relationship data
        // if Node data recordType is node, relationship relationship record
        return new AccessoryKafkaMesssagePojo(jsonObect.get(PRODUCTID).getAsString(), //
                jsonObect.get("markets").getAsString(), //
                jsonObect.get("accessoryId").getAsString(), //
                operationType, //
                payload.get("type").getAsString());
    }

    /**
     * Process product-code-service originated messages
     *
     * @param jsonRecord {@link JsonObject}
     *
     * @return {@link ProductCodeKafkaMesssagePojo}
     */
    public static ProductCodeKafkaMesssagePojo parseProductCodeKafkaMessage(
            final JsonObject jsonRecord) {
        // Parsing PCS Neo4j message
        //Get Operation that hold Operation information created or deleted
        final String operationType = jsonRecord.getAsJsonObject(META).get(
                OPERATION).getAsString();
        // payLoad holds actual values like productId, market, accessoryId, type in before
        // and after property and identify it's node data or relationship data
        final JsonObject payLoad = jsonRecord.getAsJsonObject(PAYLOAD);
        // beforeAndAfterProperty holds actual value of productId, type, code, customerId
        final JsonObject beforeAndAfterProperty = operationType.equals(DELETED)
                ? payLoad.getAsJsonObject(BEFORE) : payLoad.getAsJsonObject(AFTER);

        final JsonObject jsonObect = beforeAndAfterProperty.getAsJsonObject(PROPERTIES);
        final String recordType = payLoad.get("type").getAsString();
        return KafkaConnectNeo4jMessagesReceiver.RECORD_TYPE.equals(recordType)
                ? new ProductCodeKafkaMesssagePojo(jsonObect.get(PRODUCTID).getAsString(), //
                        jsonObect.get("market").getAsString(), //
                        jsonObect.get("customerId").getAsString(), //
                        jsonObect.get("code").getAsString(), //
                        operationType, //
                        recordType)
                : null;
    }
}
