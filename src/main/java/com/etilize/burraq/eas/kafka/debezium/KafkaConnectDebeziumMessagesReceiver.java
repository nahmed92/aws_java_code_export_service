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

package com.etilize.burraq.eas.kafka.debezium;

import static com.etilize.burraq.eas.kafka.debezium.DebeziumMessageKeys.*;
import static com.etilize.burraq.eas.kafka.debezium.DebeziumMessageProperties.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.etilize.burraq.eas.category.specification.CategorySpecificationService;
import com.etilize.burraq.eas.media.specification.ProductMediaAttributeValue;
import com.etilize.burraq.eas.media.specification.ProductMediaSpecificationService;
import com.etilize.burraq.eas.specification.ProductSpecificationService;
import com.etilize.burraq.eas.specification.UpdateProductSpecificationRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Houses listeners for all incoming Apache Kafka Debezium messages
 *
 * @author Affan Hasan
 * @since 1.0
 */
@Service
public class KafkaConnectDebeziumMessagesReceiver {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DebeziumMessageParser debeziumMessageParser;

    private final PSPECSMessageParser pspecsMessageParser;

    private final ProductMediaSpecificationService mediaSpecificationService;

    private final ProductSpecificationService specificationService;

    private final CategorySpecificationService categorySpecificationService;

    /**
     * Constructor to instantiate object instance.
     *
     * @param debeziumMessageParser {@link @DebeziumMessageParser}
     * @param mediaSpecificationService {@link ProductMediaSpecificationService}
     * @param specificationService {@link ProductSpecificationService}
     * @param pspecsMessageParser {@link PSPECSMessageParser}
     * @param categorySpecificationService {@link CategorySpecificationService}
     */
    @Autowired
    public KafkaConnectDebeziumMessagesReceiver(
            final DebeziumMessageParser debeziumMessageParser,
            final ProductMediaSpecificationService mediaSpecificationService,
            final ProductSpecificationService specificationService,
            final PSPECSMessageParser pspecsMessageParser,
            final CategorySpecificationService categorySpecificationService) {
        this.debeziumMessageParser = debeziumMessageParser;
        this.mediaSpecificationService = mediaSpecificationService;
        this.specificationService = specificationService;
        this.pspecsMessageParser = pspecsMessageParser;
        this.categorySpecificationService = categorySpecificationService;
    }

    /**
    * Process product specifications updates from product-specifications-service
    *
    * @param record {@link GenericData.Record}
    * @param key {@link String}
    * @throws IOException {@link IOException}
    */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.pspecs}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getDebeziumMessagesListenerContainerFactory")
    public void processProductSpecificationUpdates(final GenericData.Record record,
            @Header(KafkaHeaders.MESSAGE_KEY) final ConsumerRecord<Object, String> key) {
        final String productId = debeziumMessageParser.getProductIdFromDebeziumMessageKey(
                key);
        final Optional<String> operation = debeziumMessageParser.extractOperationType(
                record);
        if (operation.isPresent()) {
            switch (operation.get()) {
                case OPERATION_CREATE:
                    processAssociateCategoryCommandFromPSPECS(key, record);
                    break;
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

    /**
     * Process media updates from product-media-service
     *
     * @param record {@link GenericData.Record}
     * @param key {@link ConsumerRecord<Object, String>}
     * @throws IOException {@link IOException}
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.pms}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getDebeziumMessagesListenerContainerFactory")
    public void processProductMediaServiceMessages(final GenericData.Record record,
            @Header(KafkaHeaders.MESSAGE_KEY) final ConsumerRecord<Object, String> key) {
        logger.info("Received Product Media Service Message: [{}].", record);
        if (debeziumMessageParser.isAddProductLocaleMessageFromPMS(record)) {// It means AddProductLocaleMessage
            final PMSAddProductLocaleRequest pmsAddProductLocaleRequest = debeziumMessageParser.getPMSAddProductLocaleRequest(
                    record, key);
            mediaSpecificationService.addLocale(pmsAddProductLocaleRequest.getProductId(),
                    pmsAddProductLocaleRequest.getLocaleId());
        } else {// It means ProductMediaEvent
            final PMSProductMediaEventRequest request = debeziumMessageParser.getPMSProductMediaEventRequest(
                    record, key);
            final ProductMediaAttributeValue value = new ProductMediaAttributeValue();
            // TODO need take care cases, when height, width and tags are also coming.
            value.setUrl(request.getValue());
            mediaSpecificationService.saveAttribute(request.getProductId(),
                    request.getLocaleId(), request.getAttributeId(), request.getStatus(),
                    value);
        }
        logger.info("product media service message processed sucessfully...");
    }

    /**
     * Process product offering updates from product-offering-service
     *
     * @param record {@link GenericData.Record}
     * @param key {@link ConsumerRecord<Object, String>}
     * @throws IOException {@link IOException}
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.pos}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getDebeziumMessagesListenerContainerFactory")
    public void processProductOfferingServiceMessages(final GenericData.Record record,
            @Header(KafkaHeaders.MESSAGE_KEY) final ConsumerRecord<Object, String> key) {
        logger.info("Received Product Offering Service Message: [{}].", record);
        final Optional<String> operation = debeziumMessageParser.extractOperationType(
                record);
        switch (operation.get()) {
            case OPERATION_CREATE:
                processProductOfferingMessage(AFTER, record);
                break;
            case OPERATION_UPDATE:
                processProductOfferingMessage(PATCH, record);
                break;
            default:
                logger.info("Received following Debezium's Message: [{}].", record);
                break;
        }
    }

    /**
     * Process locale service message
     *
     * @param record {@link GenericData.Record}
     * @param key {@link ConsumerRecord<Object, String>}
     * @throws IOException {@link IOException}
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.ls}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getDebeziumMessagesListenerContainerFactory")
    public void addLocaleMessages(final GenericData.Record record,
            @Header(KafkaHeaders.MESSAGE_KEY) final ConsumerRecord<Object, String> key) {
        logger.info("Received Locale Service Message: [{}].", record);
        final Optional<String> operationType = debeziumMessageParser.extractOperationType(
                record);
        if (OPERATION_CREATE.equals(operationType.get())) {
            final String operation = record.get(AFTER) //
                    .toString();
            final JsonObject localeJSON = new JsonParser().parse(operation) //
                    .getAsJsonObject();
            final String localeId = localeJSON.get("_id").getAsString();
            // add locale in category
            categorySpecificationService.addLocale(localeId);
            logger.info("Add {} locale message processed sucessfully...", localeId);
        }
    }

    private void processAssociateCategoryCommandFromPSPECS(
            final ConsumerRecord<Object, String> key, final GenericData.Record record) {
        logger.info(
                "Received Debezium's AssociateCategoryCommand pspecs for key: [{}] with data: [{}]",
                key, record);
        final AssociateCategoryCommand pSPECSAssociateCategoryCommandRequest = debeziumMessageParser.getAssociateCategoryCommand(
                record, key);
        specificationService.createProduct(
                pSPECSAssociateCategoryCommandRequest.getProductId(),
                pSPECSAssociateCategoryCommandRequest.getIndustryId(),
                pSPECSAssociateCategoryCommandRequest.getCategoryId());
    }

    private void processUpdateSpecificationAttributeCommandForAddLocale(final String key,
            final GenericData.Record record) {
        logger.info(
                "Received Debezium's Add Product Locale Command from pspecs for key: [{}] with data: [{}]",
                key, record);
        final Optional<String> productId = Optional.ofNullable(key);
        final Optional<String> localeId = debeziumMessageParser.extractProductLocaleFromAddLocaleCommand(
                record);
        if (productId.isPresent() && localeId.isPresent()) {
            // add locale in product
            specificationService.addLocale(productId.get(), localeId.get());
            logger.info("product add locale message processed sucessfully...");
        }
    }

    private void processUpdateSpecificationAttributeCommand(final String productId,
            final GenericData.Record record) {
        logger.info(
                "Received Debezium's Update Producut Specifications Command from pspecs [{}]",
                record);
        final UpdateProductSpecificationRequest request = pspecsMessageParser.getUpdateSpecificationRequest(
                productId, record);
        specificationService.updateSpecifications(request);
        logger.info("update Specs message processed sucessfully...");
    }

    private void processProductOfferingMessage(final String operationType,
            final GenericData.Record record) {

        final String operation = record.get(operationType) //
                .toString();
        final JsonObject associateCategoryJson = new JsonParser().parse(operation) //
                .getAsJsonObject();
        final Set<String> attributeIds = new HashSet<>();
        final String categoryId = associateCategoryJson.get("categoryId").getAsString();
        final String offeringId = associateCategoryJson.get("offeringId").getAsString();
        final JsonArray attributes = associateCategoryJson.get(
                "attributes").getAsJsonArray();
        if (attributes.size() > 0) {
            attributes.forEach(attribute -> {
                attributeIds.add(attribute.getAsString());
            });
        }
        categorySpecificationService.save(categoryId, offeringId, attributeIds);
        logger.info("product offering message processed sucessfully...");
    }
}
