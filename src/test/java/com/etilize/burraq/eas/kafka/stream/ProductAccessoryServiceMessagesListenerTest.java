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

package com.etilize.burraq.eas.kafka.stream;

import java.util.*;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.util.Lists;
import org.junit.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import com.etilize.burraq.eas.test.AbstractIntegrationTest;

public class ProductAccessoryServiceMessagesListenerTest extends AbstractIntegrationTest {

    @Autowired
    private KafkaEmbedded kafkaEmbedded;

    @Value("${spring.kafka.consumer.properties.topic.pas}")
    private String productAccessoryServiceTopic;

    @Autowired
    private ConcurrentKafkaListenerContainerFactory<String, String> listener;

    Producer<String, String> producer;

    Consumer<String, String> consumer;

    @Before
    public void init() {
        final Map<String, Object> producerConfigs = new HashMap<>(
                KafkaTestUtils.producerProps(kafkaEmbedded));
        producer = new DefaultKafkaProducerFactory<>(producerConfigs,
                new StringSerializer(), new StringSerializer()).createProducer();
        consumer = listener.getConsumerFactory().createConsumer();
        consumer.subscribe(Lists.newArrayList(productAccessoryServiceTopic));
        consumer.poll(0);
    }

    @After
    public void conclude() {
        consumer.close();
        producer.close();
    }

    @Test
    public void shouldCreateProductMessage() {
        final String createProductWithOutAsseccoriesMessageValue = "{\"key\":\"neo4j\",\"value\":{\"meta\":{\"timestamp\":1547124053731,\"username\":\"neo4j\",\"txId\":328,\"txEventId\":0,\"txEventsCount\":1,\"operation\":\"created\",\"source\":{\"hostname\":\"localhost\"}},\"payload\":{\"id\":\"220\",\"before\":null,\"after\":{\"properties\":{\"productId\":\"1900\"},\"labels\":[\"Product\"]},\"type\":\"node\"},\"schema\":{\"properties\":[],\"constraints\":null}}}";
        producer.send(new ProducerRecord<>(productAccessoryServiceTopic, "sample-key",
                createProductWithOutAsseccoriesMessageValue));
        producer.flush();
        KafkaTestUtils.getRecords(consumer, 3000);
    }
}
