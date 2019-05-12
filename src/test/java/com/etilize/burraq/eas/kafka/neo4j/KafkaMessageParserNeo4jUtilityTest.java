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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class KafkaMessageParserNeo4jUtilityTest extends AbstractIntegrationTest {

    private static final String DATA_SET_PATH = "classpath:datasets";

    @Test
    public void shouldParseCreateMessage() throws IOException {

        final String barcodeKafkaMessage = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH + "/barcode_create_message.json"),
                "UTF-8");
        final BarcodeKafkaMesssagePojo parsedProperies = KafkaMessageParserNeo4jUtility.parseBarcodeKafkaMessage(
                new JsonParser().parse(barcodeKafkaMessage).getAsJsonObject());

        assertThat(parsedProperies.getRecordType(), is("relationship"));
        assertThat(parsedProperies.getOperationType(), is("created"));
        assertThat(parsedProperies.getProductId(), is("78612"));
        assertThat(parsedProperies.getType(), is("EAN"));
        assertThat(parsedProperies.getCode(), is("0885170129832"));
        assertThat(parsedProperies.getCustomerId(), is("Lenovo"));
    }

    @Test
    public void shouldParseDeleteMessage() throws IOException {
        final String barcodeKafkaMessage = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH + "/barcode_delete_message.json"),
                "UTF-8");
        final BarcodeKafkaMesssagePojo parsedProperies = KafkaMessageParserNeo4jUtility.parseBarcodeKafkaMessage(
                new JsonParser().parse(barcodeKafkaMessage).getAsJsonObject());
        assertThat(parsedProperies.getRecordType(), is("relationship"));
        assertThat(parsedProperies.getOperationType(), is("deleted"));
        assertThat(parsedProperies.getProductId(), is("78612"));
        assertThat(parsedProperies.getType(), is("EAN"));
        assertThat(parsedProperies.getCode(), is("0885170129832"));
        assertThat(parsedProperies.getCustomerId(), is("Lenovo"));
    }

    @Test
    public void shouldParseAccessoryCreateMessage() throws IOException {

        final String accessoryKafkaMessage = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH + "/accessory_create_message.json"),
                "UTF-8");
        final AccessoryKafkaMesssagePojo parsedProperies = KafkaMessageParserNeo4jUtility.parseAccessoriesKafkaMessage(
                new JsonParser().parse(accessoryKafkaMessage).getAsJsonObject());
        assertThat(parsedProperies.getRecordType(), is("relationship"));
        assertThat(parsedProperies.getOperationType(), is("created"));
        assertThat(parsedProperies.getProductId(), is("98769"));
        assertThat(parsedProperies.getMarketId(), is("US"));
        assertThat(parsedProperies.getAccessoryId(), is("1000009365"));
    }

    @Test
    public void shouldParseAccessoryDeleteMessage() throws IOException {
        final String accessoryKafkaMessage = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH + "/accessory_delete_message.json"),
                "UTF-8");
        final AccessoryKafkaMesssagePojo parsedProperies = KafkaMessageParserNeo4jUtility.parseAccessoriesKafkaMessage(
                new JsonParser().parse(accessoryKafkaMessage).getAsJsonObject());
        assertThat(parsedProperies.getRecordType(), is("relationship"));
        assertThat(parsedProperies.getOperationType(), is("deleted"));
        assertThat(parsedProperies.getProductId(), is("98769"));
        assertThat(parsedProperies.getMarketId(), is("US"));
        assertThat(parsedProperies.getAccessoryId(), is("1000009365"));
    }

    @Test
    public void shouldGetProductCodeCreatedMessage() throws IOException {

        final String productCodeKafkaMessage = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH + "/productcode_create_message.json"),
                "UTF-8");
        final ProductCodeKafkaMesssagePojo parsedProperies = KafkaMessageParserNeo4jUtility.parseProductCodeKafkaMessage(
                new JsonParser().parse(productCodeKafkaMessage).getAsJsonObject());
        assertThat(parsedProperies.getRecordType(), is("relationship"));
        assertThat(parsedProperies.getOperationType(), is("created"));
        assertThat(parsedProperies.getProductId(), is("55555"));
        assertThat(parsedProperies.getMarket(), is("EU"));
        assertThat(parsedProperies.getCustomerId(), is("IM_UK"));
        assertThat(parsedProperies.getCode(), is("0010343843173"));
    }

    @Test
    public void shouldGetProductCodeDeleteMessage() throws IOException {
        final String productCodeKafkaMessage = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH + "/productcode_delete_message.json"),
                "UTF-8");
        final ProductCodeKafkaMesssagePojo parsedProperies = KafkaMessageParserNeo4jUtility.parseProductCodeKafkaMessage(
                new JsonParser().parse(productCodeKafkaMessage).getAsJsonObject());
        assertThat(parsedProperies.getRecordType(), is("relationship"));
        assertThat(parsedProperies.getOperationType(), is("deleted"));
        assertThat(parsedProperies.getProductId(), is("55555"));
        assertThat(parsedProperies.getMarket(), is("US"));
        assertThat(parsedProperies.getCustomerId(), is("IM_US"));
        assertThat(parsedProperies.getCode(), is("0010343843172"));
    }

}
