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

package com.etilize.burraq.eas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.etilize.burraq.eas.kafka.debezium.DebeziumMessageParser;
import com.etilize.burraq.eas.kafka.debezium.PSPECSMessageParser;

/**
 * General configurations related to Bean dependencies etc.
 *
 * @author Affan Hasan
 * @since 1.0
 */
@Configuration
public class DebeziumConfig {

    /**
     * Produces an instance of {@link DebeziumMessageParser}
     *
     * @return {@link DebeziumMessageParser}
     */
    @Bean
    public DebeziumMessageParser debeziumMessageParser() {
        return new DebeziumMessageParser();
    }

    /**
     * Produces an instance of {@link PSPECSMessageParser}
     *
     * @return {@link PSPECSMessageParser}
     */
    @Bean
    public PSPECSMessageParser pspecsMessageParser() {
        return new PSPECSMessageParser();
    }
}
