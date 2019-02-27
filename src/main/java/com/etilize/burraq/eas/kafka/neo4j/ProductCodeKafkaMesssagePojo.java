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

package com.etilize.burraq.eas.kafka.neo4j;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class hold product code kafka message
 *
 * @author Nasir Ahmed
 *
 * @Since 1.0
 *
 */
public class ProductCodeKafkaMesssagePojo {

    private final String productId;

    private final String market;

    private final String customerId;

    private final String code;

    private final String operationType;

    private final String recordType;

    /**
     * Constructor ProductCodeKafkaMesssagePojo
     *
     * @param productId product Id
     * @param market associated market
     * @param customerId associated customerId
     * @param code associated code
     * @param operationType associated operationType
     * @param recordType associated recordType
     */
    public ProductCodeKafkaMesssagePojo(final String productId, final String market,
            final String customerId, final String code, final String operationType,
            final String recordType) {
        this.productId = productId;
        this.market = market;
        this.customerId = customerId;
        this.code = code;
        this.operationType = operationType;
        this.recordType = recordType;
    }

    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @return the market
     */
    public String getMarket() {
        return market;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the operationType
     */
    public String getOperationType() {
        return operationType;
    }

    /**
     * @return the recordType
     */
    public String getRecordType() {
        return recordType;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProductCodeKafkaMesssagePojo)) {
            return false;
        }
        final ProductCodeKafkaMesssagePojo productCodeKafkaMesssagePojo = (ProductCodeKafkaMesssagePojo) obj;
        return new EqualsBuilder() //
                .append(getProductId(), productCodeKafkaMesssagePojo.getProductId()) //
                .append(getMarket(), productCodeKafkaMesssagePojo.getMarket()) //
                .append(getCode(), productCodeKafkaMesssagePojo.getCode()) //
                .append(getCustomerId(), productCodeKafkaMesssagePojo.getCustomerId()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getProductId()) //
                .append(getMarket()) //
                .append(getCode()) //
                .append(getCustomerId()) //
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("ProductId", getProductId()) //
                .append("Market", getMarket()) //
                .append("Code", getCode()) //
                .append("CustomerId", getCustomerId()) //
                .toString();
    }

}
