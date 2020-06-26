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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.etilize.burraq.eas.media.status.ProductMediaStatusService;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatusService;

/**
 * Houses listeners for all incoming Apache Kafka Redis messages
 *
 * @author Affan Hasan
 * @since 1.0
 */
@Service
public class KafkaConnectRedisMessagesReceiver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ProductSpecificationStatusService specificationStatusService;

    private ProductMediaStatusService mediaStatusService;

    /**
     * Constructor to instantiate object instance.
     *
     * @param specificationStatusService {@link ProductSpecificationStatusService}
     * @param mediaStatusService {@link ProductMediaStatusService}
     */
    @Autowired
    public KafkaConnectRedisMessagesReceiver(
            final ProductSpecificationStatusService specificationStatusService,
            final ProductMediaStatusService mediaStatusService) {
        this.specificationStatusService = specificationStatusService;
        this.mediaStatusService = mediaStatusService;
    }

    /**
     * Process product specification status updates from PSSS.
     *
     * @param message {@link Message<String>}
     * @throws IOException {@link IOException}
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.psss}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getStringMessagesListenerContainerFactory")
    @Nonnull
    public void processProductSpecificationsStatusMessage(final Message<String> message) {
        logger.info("Received Product Specifications Status Message: [{}]", message);
        final KafkaConnectRedisMessagesDecoder psssMessageDecoder = new KafkaConnectRedisMessagesDecoder();
        /**
         * We are adding NOSONAR here in order to avoid "findbugs:NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"
         */
        final String kafkaReceivedMessageKey = message.getHeaders() //
                .containsKey(KAFKA_RECEIVED_MESSAGE_KEY) ? message.getHeaders() //
                        .get(KAFKA_RECEIVED_MESSAGE_KEY) //
                        .toString() : ""; //NOSONAR
        try {
            if (kafkaReceivedMessageKey.endsWith(HASH_SET_COMMAND)
                    || kafkaReceivedMessageKey.endsWith(HASH_SET_NX_COMMAND)) {// Case for Add Or Update
                final Optional<KafkaConnectRedisUpsertMessagePayload> requestPayload;
                requestPayload = psssMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                        message.getPayload());
                if (requestPayload.isPresent()) {
                    final KafkaConnectRedisUpsertMessagePayload payload = requestPayload.get();
                    final String productId = payload.getKey() //
                            .split(":")[1];
                    specificationStatusService.save(productId, payload.getField(),
                            payload.getValue());
                }
            } else if (kafkaReceivedMessageKey.endsWith(KEY_STRING_VALUE_HASH_COMMAND)) {
                logger.info("Processing KeyStringValueHash command for PSSS");
                final List<Optional<KafkaConnectRedisUpsertMessagePayload>> requestPayloadList;
                requestPayloadList = psssMessageDecoder.convertJsonToKafkaConnectRedisKeyStringValueHashMessagePayload(
                        message.getPayload());
                for (Optional<KafkaConnectRedisUpsertMessagePayload> payloadOptional : requestPayloadList) {
                    if (payloadOptional.isPresent()) {
                        final KafkaConnectRedisUpsertMessagePayload payload = payloadOptional.get();
                        final String productId = payload.getKey() //
                                .split(":")[1];
                        logger.info(
                                "Parsed Product Specifications Status message: productId: {} localeId:{} status: {}",
                                productId, payload.getField(), payload.getValue());
                        specificationStatusService.save(productId, payload.getField(),
                                payload.getValue());
                    }
                }
            } else if (kafkaReceivedMessageKey.endsWith(H_DEL_COMMAND)) {// Case for delete all specifications statuses
                final String id = psssMessageDecoder.extractProductIdFromDeleteMessage(
                        message.getPayload()) //
                        .get();
                specificationStatusService.deleteAllByProductId(id);
            }
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

    }

    /**
     * PMSS messages listener.
     *
     * @param message {@link Message<String>}
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.pmss}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getStringMessagesListenerContainerFactory")
    public void processPMSSMessages(final Message<String> message) {
        logger.info("Received Product Media Status Service message: [{}]", message);
        final KafkaConnectRedisMessagesDecoder psssMessageDecoder = new KafkaConnectRedisMessagesDecoder();
        /**
         * We are adding NOSONAR here in order to avoid "findbugs:NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"
         */
        final String kafkaReceivedMessageKey = message.getHeaders() //
                .containsKey(KAFKA_RECEIVED_MESSAGE_KEY) ? message.getHeaders() //
                        .get(KAFKA_RECEIVED_MESSAGE_KEY) //
                        .toString() : ""; //NOSONAR
        try {
            if (kafkaReceivedMessageKey.endsWith(HASH_SET_COMMAND)
                    || kafkaReceivedMessageKey.endsWith(HASH_SET_NX_COMMAND)) {// Case for Add Or Update
                final Optional<KafkaConnectRedisUpsertMessagePayload> requestPayload;
                requestPayload = psssMessageDecoder.convertJsonToKafkaConnectRedisUpsertMessagePayload(
                        message.getPayload());
                if (requestPayload.isPresent()) {
                    final KafkaConnectRedisUpsertMessagePayload payload = requestPayload.get();
                    final String productId = payload.getKey() //
                            .split(":")[1];
                    mediaStatusService.save(productId, payload.getField(),
                            payload.getValue());
                }
            } else if (kafkaReceivedMessageKey.endsWith(KEY_STRING_VALUE_HASH_COMMAND)) {
                logger.info("Processing KeyStringValueHash command for PMSS");
                final List<Optional<KafkaConnectRedisUpsertMessagePayload>> requestPayloadList;
                requestPayloadList = psssMessageDecoder.convertJsonToKafkaConnectRedisKeyStringValueHashMessagePayload(
                        message.getPayload());
                for (Optional<KafkaConnectRedisUpsertMessagePayload> payloadOptional : requestPayloadList) {
                    if (payloadOptional.isPresent()) {
                        final KafkaConnectRedisUpsertMessagePayload payload = payloadOptional.get();
                        final String productId = payload.getKey() //
                                .split(":")[1];
                        logger.info(
                                "Parsed Product Media Status message: productId: {} localeId:{} status: {}",
                                productId, payload.getField(), payload.getValue());
                        mediaStatusService.save(productId, payload.getField(),
                                payload.getValue());
                    }
                }
            } else if (kafkaReceivedMessageKey.endsWith(H_DEL_COMMAND)) {// Case for delete all media statuses
                final String id = psssMessageDecoder.extractProductIdFromDeleteMessage(
                        message.getPayload()) //
                        .get();
                mediaStatusService.deleteAllByProductId(id);
            }
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }

    }
}
