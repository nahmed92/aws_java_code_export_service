/*
 * #region
 * export-aggregation-service
 * %%
 * Copyright (C) 2018 Etilize
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

package com.etilize.burraq.eas.kafka.stream;

import static com.etilize.burraq.eas.kafka.debezium.DebeziumMessageKeys.OPERATION_UPDATE;
import static com.etilize.burraq.eas.kafka.debezium.DebeziumMessageProperties.*;

import java.io.IOException;
import java.util.Optional;
import java.util.Queue;

import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.etilize.avro.spring.AvroJsonSchemaRegistryClientMessageConverter;
import com.etilize.burraq.eas.kafka.debezium.DebeziumMessageParser;
import com.etilize.burraq.eas.kafka.debezium.SpecificationUpdateOperation;

/**
 * It receives messages from Apache Kafka.
 *
 * @author Affan Hasan
 * @since 1.0
 */
@Service
public class MessageReceiver {

    @Autowired
    private AvroJsonSchemaRegistryClientMessageConverter avroJsonSchemaRegistryClientMessageConverter;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DebeziumMessageParser debeziumMessageParser;

    /**
     * Constructor to instantiate object instance
     *
     * @param debeziumMessageParser {@link @DebeziumMessageParser}
     */
    @Autowired
    public MessageReceiver(final DebeziumMessageParser debeziumMessageParser) {
        this.debeziumMessageParser = debeziumMessageParser;
    }

    /**
     * Process {@link UpdateTextTranslationEvent}
     *
     * @param message {@link Message<byte[]>} message of type byte array
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.tas}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getUpdateTextTranslationEventListenerContainerFactory")
    public void processUpdateTextTranslationEvent(final Message<byte[]> message) {
        final String contentType = message.getHeaders() //
                .get("contentType") //
                .toString(); //
        if (contentType.contains("updatetexttranslationevent")) {
            final UpdateTextTranslationEvent updateTextTranslationEvent = (UpdateTextTranslationEvent) avroJsonSchemaRegistryClientMessageConverter.fromMessage(
                    message, UpdateTextTranslationEvent.class);
            logger.info("Received UpdateTextTranslationEvent [{}]",
                    updateTextTranslationEvent);
            //TODO: further processing needs to be implemented
        } else if (contentType.contains("updateunittranslationevent")) {
            final UpdateUnitTranslationEvent updateUnitTranslationEvent = (UpdateUnitTranslationEvent) avroJsonSchemaRegistryClientMessageConverter.fromMessage(
                    message, UpdateUnitTranslationEvent.class);
            logger.info("Received UpdateUnitTranslationEvent [{}]",
                    updateUnitTranslationEvent);
            //TODO: further processing needs to be implemented
        }
    }

    /**
     * Process barcode-service originated messages
     *
     * @param message {@link Message<String>} message of type {@link String}
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.bacs}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getStringMessagesListenerContainerFactory")
    public void processBarcodeMessages(final Message<String> message) {
        logger.info("Received message from barcode-service [{}]", message);
    }
    
    /**
     * Process product-code-service originated messages
     *
     * @param message {@link Message<String>} message of type {@link String}
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.pcs}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getStringMessagesListenerContainerFactory")
    public void processProductCodeServiceMessages(final Message<String> message) {
        logger.info("Received message from product-code-service [{}]", message);
    }

    /**
     * Process product specifications updates from product-specifications-service
     *
     * @param record {@link GenericData.Record}
     * @param key {@link String}
     * @throws IOException {@link IOException}
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.pspecs}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getProductSpecificationUpdatesMessageListenerContainerFactory")
    public void processProductSpecificationUpdates(final GenericData.Record record,
            @Header(KafkaHeaders.MESSAGE_KEY) final ConsumerRecord<Object, String> key)
            throws IOException {
        final GenericData.Record keyRecord = ((GenericData.Record) key.key());
        final String productId;
        if (keyRecord.get("id") != null) {
            productId = keyRecord.get("id") //
                    .toString() //
                    .replaceAll("\"", "");
        } else {
            productId = keyRecord.get("_id") //
                    .toString() //
                    .replaceAll("\"", "");
        }
        final Optional<String> operation = debeziumMessageParser.extractOperationType(
                record);
        if (operation.isPresent()) {
            switch (operation.get()) {
                case OPERATION_UPDATE:
                    switch (debeziumMessageParser.extractUpdateOperationType(
                            record).get()) {
                        case UPDATE_OPERATION_ADD_PRODUCT_LOCALE:
                            logger.info(
                                    "Recieved Add Product Specification Locale Message:[{}]",
                                    record);
                            processUpdateSpecificationAttributeCommandForAddLocale(
                                    productId, record);
                            break;
                        case UPDATE_OPERATION_UPDATE_PRODUCT_SPECIFICATION:
                            logger.info(
                                    "Recieved Update Product Specification Message:[{}]",
                                    record);
                            processUpdateSpecificationAttributeCommand(productId, record);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    logger.info("Received following Debezium's Message: [{}].", record);
                    break;
            }
        }
    }

    private void processUpdateSpecificationAttributeCommandForAddLocale(final String key,
            final GenericData.Record record) throws IOException {
        logger.info(
                "Received Debezium's Add Product Locale Command from pspecs for key: [{}] with data: [{}]",
                key, record);
        final Optional<String> productId = Optional.ofNullable(key);
        final Optional<String> localeId = debeziumMessageParser.extractProductLocaleFromAddLocaleCommand(
                record);
        if (productId.isPresent() && localeId.isPresent()) {
            //TODO: Add product specification locale logic will be added here.
        }
    }

    private void processUpdateSpecificationAttributeCommand(final String productId,
            final GenericData.Record record) throws IOException {
        logger.info(
                "Received Debezium's Update Producut Specifications Command from pspecs [{}]",
                record);
        final Queue<SpecificationUpdateOperation> operations = debeziumMessageParser.parseToUpdateOperationMap(
                productId, record);
        operations.forEach(operation -> {
            //TODO: processing will be implemented here !
        });
    }
}
