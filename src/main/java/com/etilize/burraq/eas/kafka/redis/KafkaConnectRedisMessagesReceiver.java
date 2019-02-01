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
import java.util.Optional;

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

import com.etilize.burraq.eas.specification.status.SpecificationStatusService;

/**
 * Houses listeners for all incoming Apache Kafka Redis messages
 *
 * @author Affan Hasan
 * @since 1.0
 */
@Service
public class KafkaConnectRedisMessagesReceiver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SpecificationStatusService specificationStatusService;

    /**
     * Constructor to instantiate object instance.
     *
     * @param specificationStatusService
     *            {@link SpecificationStatusService}
     */
    @Autowired
    public KafkaConnectRedisMessagesReceiver(
            final SpecificationStatusService specificationStatusService) {
        this.specificationStatusService = specificationStatusService;
    }

    /**
     * Process product specification status updates from PSSS.
     *
     * @param message
     *            {@link Message<String>}
     * @throws IOException
     *             {@link IOException}
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.psss}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getStringMessagesListenerContainerFactory")
    public void processProductSpecificationsStatusMessage(final Message<String> message) {
        logger.info("Received product specifications status message: [{}]", message);
        final KafkaConnectRedisMessagesDecoder psssMessageDecoder = new KafkaConnectRedisMessagesDecoder();
        final String kafkaReceivedMessageKey = message.getHeaders() //
                .containsKey(KAFKA_RECEIVED_MESSAGE_KEY) ? message.getHeaders() //
                        .get(KAFKA_RECEIVED_MESSAGE_KEY) //
                        .toString() : null;
        if (kafkaReceivedMessageKey.endsWith(HASH_MAP_SET_COMMAND)) {// Case for Add Or Update
            final Optional<PSSSUpsertMessagePayload> requestPayload;
            requestPayload = psssMessageDecoder.convertJsonToProductSpecificationsStatusUpsertMessagePayload(
                    message.getPayload());
            if (requestPayload.isPresent()) {
                requestPayload.get() //
                        .getFields() //
                        .entrySet() //
                        .forEach(item -> {
                            if (!item.getKey() //
                                    .equals(ID)) {
                                specificationStatusService.save(requestPayload.get() //
                                        .getFields() //
                                        .get(ID), item.getKey(), item.getValue());
                            }
                        });
            }
        } else if (kafkaReceivedMessageKey.endsWith(SET_REMOVE_COMMAND)) {// Case for delete all specifications statuses
            final String id = psssMessageDecoder.extractProductIdFromDeleteSpecificationsStatusesMessage(
                    message.getPayload()) //
                    .get();
            specificationStatusService.deleteAllByProductId(id);
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
    public void processProductMediaSpecificationsStatusServiceMessages(
            final GenericData.Record record,
            @Header(KafkaHeaders.MESSAGE_KEY) final ConsumerRecord<Object, String> key) {
        logger.info("Received Media Message: [{}].", record);
    }
}
