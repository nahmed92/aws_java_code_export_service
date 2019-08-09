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

package com.etilize.burraq.eas.test;

import static com.lordofthejars.nosqlunit.dynamodb.DynamoDbRule.DynamoDbRuleBuilder.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.github.wonwoo.dynamodb.test.autoconfigure.AutoConfigureDynamo;
import com.lordofthejars.nosqlunit.dynamodb.DynamoDbRule;

import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import org.springframework.kafka.test.context.EmbeddedKafka;

/**
 * Base class for integration tests
 *
 * @author Faisal Feroz
 * @since 1.0
 *
 */

@EmbeddedKafka
@AutoConfigureDynamo
public abstract class AbstractIntegrationTest extends AbstractTest {

    @Autowired
    protected ApplicationContext context;

    // Rendom port from 20k to max
    private static final int ES_PORT = (int) (Math.random() * ((65500 - 20000) + 1))
            + 20000;

    // Rendom port from 20k to max
    private static final int ES_HTTP_PORT = (int) (Math.random() * ((6550 - 2000) + 1))
            + 2000;

    // Fixed cluster name
    private static final String ES_CLUSTER_NAME = "burraq_cluster";

    private static EmbeddedElastic embeddedElastic;

    private static boolean isRunningembeddedElastic = false;

    @Rule
    public DynamoDbRule dynamoDbRule = newDynamoDbRule().defaultSpringDynamoDb();

    @BeforeClass
    public static void setupClass() throws Exception {
        System.setProperty("sqlite4java.library.path", "native-libs");
    }

    /**
     * @ClassRule for loading embedded Elasticsearch
     *
     * @return TestRule TestRule
     */

    @ClassRule
    public static TestRule getEmbeddedElasticsearch() {
        if (!isRunningembeddedElastic) {
            System.setProperty("burraq.embedded.elastic.node", //
                    "localhost:" + ES_HTTP_PORT);
            System.setProperty("burraq.embedded.elastic.node.protocol", //
                    "http");

            try {
                if (embeddedElastic == null) {
                    embeddedElastic = EmbeddedElastic.builder() //
                            .withElasticVersion("6.5.0") //
                            .withSetting(PopularProperties.TRANSPORT_TCP_PORT, ES_PORT) //
                            .withSetting(PopularProperties.HTTP_PORT, ES_HTTP_PORT) //

                            .withSetting(PopularProperties.CLUSTER_NAME, ES_CLUSTER_NAME) //
                            .withEsJavaOpts("-Xms128m -Xmx512m") //
                            .withStartTimeout(2, TimeUnit.MINUTES).build().start();
                }
                isRunningembeddedElastic = true;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return RuleChain.emptyRuleChain();
    }

}
