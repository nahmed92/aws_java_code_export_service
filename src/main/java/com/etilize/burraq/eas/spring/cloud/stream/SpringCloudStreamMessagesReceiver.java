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

package com.etilize.burraq.eas.spring.cloud.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.etilize.avro.spring.AvroJsonSchemaRegistryClientMessageConverter;

/**
 * Houses message handlers for spring cloud stream based messages.
 *
 * @author Affan Hasan
 * @since 1.0
 */
@Service
public class SpringCloudStreamMessagesReceiver {

    private final AvroJsonSchemaRegistryClientMessageConverter avroJsonSchemaRegistryClientMessageConverter;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructor to instantiate object instance
     *
     * @param avroJsonSchemaRegistryClientMessageConverter {@link AvroJsonSchemaRegistryClientMessageConverter}
     */
    @Autowired
    public SpringCloudStreamMessagesReceiver(
            final AvroJsonSchemaRegistryClientMessageConverter avroJsonSchemaRegistryClientMessageConverter) {
        this.avroJsonSchemaRegistryClientMessageConverter = avroJsonSchemaRegistryClientMessageConverter;
    }

    /**
     * Process {@link UpdateTextTranslationEvent}
     *
     * @param message {@link Message<byte[]>} message of type byte array
     */
    @KafkaListener(topics = "${spring.kafka.consumer.properties.topic.tas}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "getUpdateTextTranslationEventListenerContainerFactory")
    public void processUpdateTextTranslationEvent(final Message<byte[]> message) {
        /**
         * We are adding NOSONAR here in order to avoid "findbugs:NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"
         */
        final String contentType = message.getHeaders() //
                .get("contentType") //
                .toString(); //NOSONAR
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
}
