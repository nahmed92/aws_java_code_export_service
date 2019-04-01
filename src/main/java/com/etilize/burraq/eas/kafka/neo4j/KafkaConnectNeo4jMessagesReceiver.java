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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.etilize.burraq.eas.accessory.AccessoryService;
import com.etilize.burraq.eas.barcode.BarcodeService;
import com.etilize.burraq.eas.customer.code.CustomerCodeService;
import com.google.gson.JsonParser;

/**
 * Houses listeners for all incoming Apache Kafka Redis messages
 *
 * @author Affan Hasan
 * @since 1.0
 */
@Service
public class KafkaConnectNeo4jMessagesReceiver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String RECORD_TYPE = "relationship";

    private static final String OPERATION_TYPE = "created";

    private final BarcodeService barcodeService;

    private final AccessoryService accessoryService;
    
    private final CustomerCodeService customerCodeService;
    
    /**
     * Constructor to create instance
     * 
     * @param barcodeService {@link BarcodeService}
     * @param accessoryService {@link AccessoryService}
     * @param customerCodeService {@link CustomerCodeService}
     */
    @Autowired
    public KafkaConnectNeo4jMessagesReceiver(final BarcodeService barcodeService,
    		final AccessoryService accessoryService,
    		final CustomerCodeService customerCodeService) {
    	this.barcodeService = barcodeService;
    	this.accessoryService = accessoryService;
        this.customerCodeService = customerCodeService;
	}

    /**
     * Process barcode-service originated messages
     *
     * @param message
     *            {@link Message<String>} message of type {@link String}
     * @throws IllegalStateException when message payload is not a valie json
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.bacs}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getStringMessagesListenerContainerFactory")
    public void processBarcodeMessages(final Message<String> message) throws IllegalStateException {
        logger.info("Received message from barcode-service [{}]", message);

        final BarcodeKafkaMesssagePojo parsedMessage = KafkaMessageParserNeo4jUtility.parseBarcodeKafkaMessage(
                new JsonParser().parse(
                        message.getPayload().toString()).getAsJsonObject());
        if (RECORD_TYPE.equals(parsedMessage.getRecordType())) {
            if (OPERATION_TYPE.equals(parsedMessage.getOperationType())) {
                barcodeService.save(parsedMessage.getProductId(), //
                        parsedMessage.getType(), //
                        parsedMessage.getCode(), //
                        parsedMessage.getCustomerId());
            } else {
                barcodeService.delete(parsedMessage.getProductId(), //
                        parsedMessage.getType(), //
                        parsedMessage.getCode(), //
                        parsedMessage.getCustomerId());
            }
        }
    }

    /**
     * Process product-code-service originated messages
     *
     * @param message
     *            {@link Message<String>} message of type {@link String}
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.pcs}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getStringMessagesListenerContainerFactory")
    public void processProductCodeServiceMessages(final Message<String> message) throws IllegalStateException {
        logger.info("Received message from product-code-service [{}]", message);
        final ProductCodeKafkaMesssagePojo parsedMessage = KafkaMessageParserNeo4jUtility.parseProductCodeKafkaMessage(
                new JsonParser().parse(
                        message.getPayload().toString()).getAsJsonObject());
        if (parsedMessage != null && RECORD_TYPE.equals(parsedMessage.getRecordType())) {
            if (OPERATION_TYPE.equals(parsedMessage.getOperationType())) {
                customerCodeService.save(parsedMessage.getProductId(), //
                        parsedMessage.getMarket(), //
                        parsedMessage.getCode(), parsedMessage.getCustomerId());
            } else {
                customerCodeService.delete(parsedMessage.getProductId(), //
                        parsedMessage.getMarket(), //
                        parsedMessage.getCode(), parsedMessage.getCustomerId());
            }
        }
    }

    /**
     * Process product-accessory-service originated messages
     *
     * @param message
     *            {@link AccessoryKafkaMesssagePojo} message of type {@link String}
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.pas}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getStringMessagesListenerContainerFactory")
    public void processProductAsseccoryServiceMessages(final Message<String> message) throws IllegalStateException {
        logger.info("Received message from product-accessories-service [{}]", message);
        final AccessoryKafkaMesssagePojo parsedMessage = KafkaMessageParserNeo4jUtility.parseAccessoriesKafkaMessage(
                new JsonParser().parse(
                        message.getPayload().toString()).getAsJsonObject());
        if (RECORD_TYPE.equals(parsedMessage.getRecordType())) {
            if (OPERATION_TYPE.equals(parsedMessage.getOperationType())) {
                accessoryService.save(parsedMessage.getProductId(), //
                        parsedMessage.getMarketId(), //
                        parsedMessage.getAccessoryId());
            } else {
                accessoryService.delete(parsedMessage.getProductId(), //
                        parsedMessage.getMarketId(), //
                        parsedMessage.getAccessoryId());
            }
        }
    }
}
