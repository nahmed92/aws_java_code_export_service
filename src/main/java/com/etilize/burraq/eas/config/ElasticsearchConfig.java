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

import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;

/**
 * This is Elasticsearch {@link Configuration}
 *
 * @author Nasir Ahmed
 *
 */
@Configuration
public class ElasticsearchConfig {

    @Value("${spring.data.elasticsearch.node}")
    private String clusterNode;

    @Value("${spring.data.elasticsearch.protocol}")
    private String protocol;

    @Value("${spring.data.elasticsearch.service-name}")
    private String serviceName;

    @Value("${spring.data.elasticsearch.region}")
    private String region;

    private static final AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

    /**
     * This method create the elasticsearch template
     * @return ElasticsearchOperations as elasticsearch template
     */
    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate() {

        final RestHighLevelClient client = client();

        final ElasticsearchRestTemplate opration = new ElasticsearchRestTemplate(client);

        return opration;
    }

    /**
     * This method create the elasticsearch client
     *
     * @return {@link Client} as elasticsearch client
     */
    @Bean
    public RestHighLevelClient client() {
        if (protocol.equals("https") && !region.equals("none")) {
            final AWS4Signer signer = new AWS4Signer();
            signer.setServiceName(serviceName);
            signer.setRegionName(region);
            final HttpRequestInterceptor interceptor = new AWSRequestSigningApacheInterceptor(
                    serviceName, signer, credentialsProvider);
            return new RestHighLevelClient(RestClient.builder(
                    HttpHost.create(clusterNode)).setHttpClientConfigCallback(
                            hacb -> hacb.addInterceptorLast(interceptor)));
        } else {
            return new RestHighLevelClient(
                    RestClient.builder(HttpHost.create(clusterNode)));
        }
    }

}
