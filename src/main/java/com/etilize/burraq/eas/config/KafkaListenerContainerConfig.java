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

package com.etilize.burraq.eas.config;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;

import java.util.HashMap;
import java.util.Map;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.etilize.burraq.stream.translation.UpdateTextTranslationEvent;

/**
 * Contains configuration for Apache Kafka Listener Container(s)
 *
 * @author Affan Hasan
 * @since 1.0
 */
@EnableKafka
@Configuration
public class KafkaListenerContainerConfig {

    @Value("${spring.kafka.consumer.group-id}")
    private String kafkaGroupId;

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBootStrapServer;

    @Value("${spring.kafka.consumer.properties.schema.registry.url}")
    private String schemaRegistryURL;

    @Value("${spring.kafka.consumer.properties.basic.auth.credentials.source}")
    private String basicAuthCredentialsSource;

    @Value("${spring.kafka.consumer.properties.basic.auth.user.info}")
    private String basicAuthUserInfo;

    @Value("${spring.kafka.properties.ssl.endpoint.identification.algorithm}")
    private String sslIdedntificationAlgorithm;

    @Value("${spring.kafka.properties.security.protocol}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Value("${spring.kafka.properties.sasl.jaas.config}")
    private String saslJaasConfig;

    @Value("${spring.kafka.properties.sasl.request.timeout.ms}")
    private String saslRequestTimeout;

    @Value("${spring.kafka.properties.sasl.retry.backoff.ms}")
    private String saslRetryBackOffMs;

    /**
     * Produces {@link ConcurrentKafkaListenerContainerFactory} instance for {@link UpdateTextTranslationEvent}
     *
     * @return {@link ConcurrentKafkaListenerContainerFactory} instance
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<byte[], byte[]> getUpdateTextTranslationEventListenerContainerFactory() {
        final Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaBootStrapServer);
        props.put(GROUP_ID_CONFIG, kafkaGroupId);
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        addSecurityConfigsToKafkaConsumer(props);
        final ConcurrentKafkaListenerContainerFactory<byte[], byte[]> cklcf = new ConcurrentKafkaListenerContainerFactory<>();
        cklcf.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));
        return cklcf;
    }

    /**
     * Produces {@link ConcurrentKafkaListenerContainerFactory} instance for Debezium messages
     *
     * @return {@link ConcurrentKafkaListenerContainerFactory} instance
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<byte[], byte[]> getDebeziumMessagesListenerContainerFactory() {
        final Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaBootStrapServer);
        props.put(GROUP_ID_CONFIG, kafkaGroupId);
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        addSecurityConfigsToKafkaConsumer(props);
        props.put("schema.registry.url", schemaRegistryURL);
        props.put("basic.auth.credentials.source", basicAuthCredentialsSource);
        props.put("basic.auth.user.info", basicAuthUserInfo);
        final ConcurrentKafkaListenerContainerFactory<byte[], byte[]> cklcf = new ConcurrentKafkaListenerContainerFactory<>();
        cklcf.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));
        return cklcf;
    }

    /**
     * Produces {@link ConcurrentKafkaListenerContainerFactory} instance for {@link String} messages
     *
     * @return {@link ConcurrentKafkaListenerContainerFactory} instance
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> getStringMessagesListenerContainerFactory() {
        final Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaBootStrapServer);
        props.put(GROUP_ID_CONFIG, kafkaGroupId);
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        addSecurityConfigsToKafkaConsumer(props);
        final ConcurrentKafkaListenerContainerFactory<String, String> cklcf = new ConcurrentKafkaListenerContainerFactory<>();
        cklcf.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));
        return cklcf;
    }

    private void addSecurityConfigsToKafkaConsumer(final Map<String, Object> props) {
        props.put("ssl.endpoint.identification.algorithm", sslIdedntificationAlgorithm);
        props.put(SECURITY_PROTOCOL_CONFIG, securityProtocol);
        props.put("sasl.mechanism", saslMechanism);
        props.put("sasl.jaas.config", saslJaasConfig);
        props.put("sasl.request.timeout.ms", saslRequestTimeout);
        props.put("sasl.retry.backoff.ms", saslRetryBackOffMs);
    }
}
