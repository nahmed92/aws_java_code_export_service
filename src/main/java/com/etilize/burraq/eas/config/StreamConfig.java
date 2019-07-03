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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.cloud.stream.schema.avro.AvroMessageConverterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.etilize.avro.spring.AvroJsonSchemaRegistryClientMessageConverter;
import com.etilize.avro.spring.ConfluentSchemaRegistryClientExtensionImpl;
import com.etilize.avro.spring.SchemaRegistryClientExtension;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;

/**
 * Configuration class that houses the bean definitions related to spring-cloud-stream
 *
 * @author Faisal Feroz
 * @since 1.0
 */
@Configuration
public class StreamConfig {

    @Autowired
    private AvroMessageConverterProperties avroMessageConverterProperties;

    /**
     * {@link AvroMapper} instance to use for reading/writing avro encoded messages
     *
     * @return {@link AvroMapper} instance
     */
    @Bean
    public AvroMapper avroMapper() {
        return new AvroMapper();
    }

    /**
     * an implementation of {@link MessageConverter} that converts message to/from avro
     * encoding
     *
     * @param mapper {@link AvroMapper} instance
     * @param schemaRegistryClient {@link SchemaRegistryClientExtension} instance
     * @param cacheManager {@link CacheManager} instance
     * @return an implementation of {@link MessageConverter} that converts message to/from
     *         avro encoding
     */
    @Bean
    @StreamMessageConverter
    public AvroJsonSchemaRegistryClientMessageConverter avroMessageConverter(
            final AvroMapper mapper, final SchemaRegistryClientExtension schemaRegistryClient,
            final CacheManager cacheManager) {
        final AvroJsonSchemaRegistryClientMessageConverter avroJsonSchemaRegistryClientMessageConverter = new AvroJsonSchemaRegistryClientMessageConverter(
                mapper, schemaRegistryClient, cacheManager);
        avroJsonSchemaRegistryClientMessageConverter.setDynamicSchemaGenerationEnabled(
                this.avroMessageConverterProperties.isDynamicSchemaGenerationEnabled());
        if (this.avroMessageConverterProperties.getReaderSchema() != null) {
            avroJsonSchemaRegistryClientMessageConverter.setReaderSchema(
                    this.avroMessageConverterProperties.getReaderSchema());
        }
        if (!ObjectUtils.isEmpty(
                this.avroMessageConverterProperties.getSchemaLocations())) {
            avroJsonSchemaRegistryClientMessageConverter.setSchemaLocations(
                    this.avroMessageConverterProperties.getSchemaLocations());
        }
        avroJsonSchemaRegistryClientMessageConverter.setPrefix(
                this.avroMessageConverterProperties.getPrefix());

        return avroJsonSchemaRegistryClientMessageConverter;
    }

    /**
     * Confluent Schema Registry Client.
     *
     * @param restTemplateBuilder {@link RestTemplateBuilder}
     * @param endpoint the end-point where schema registry is running
     * @param username key
     * @param password secret key
     * @return Confluent Schema Registry Client
     */
    @Bean
    public SchemaRegistryClientExtension schemaRegistryClient(
            final RestTemplateBuilder restTemplateBuilder,
            @Value("${spring.kafka.consumer.properties.schema.registry.url}") final String endpoint,
            @Value("${spring.kafka.consumer.properties.username:''}") final String username,
            @Value("${spring.kafka.consumer.properties.password:''}") final String password) {
        final RestTemplate restTemplate = (StringUtils.isEmpty(username)
                ? restTemplateBuilder
                : restTemplateBuilder.basicAuthorization(username, password)).build();
        final ConfluentSchemaRegistryClientExtensionImpl client = new ConfluentSchemaRegistryClientExtensionImpl(restTemplate, endpoint);
        return client;
    }
}
