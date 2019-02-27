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
 * This class hold parsed value of Kafka Message
 *
 * @author Nasir Ahmed
 *
 * @Since 1.0
 *
 */

public class AccessoryKafkaMesssagePojo {

    private final String productId;

    private final String marketId;

    private final String accessoryId;

    private final String operationType;

    private final String recordType;

    /**
     * create {@link AccessoryKafkaMesssagePojo} instance
     * 
     * @param productId accessory productId
     * @param marketId accessory market
     * @param accessoryId accessory accessoryId
     * @param operationType [add delete link unlink]
     * @param recordType [node relationship]
     */
    public AccessoryKafkaMesssagePojo(final String productId, final String marketId,
            final String accessoryId, final String operationType,
            final String recordType) {
        this.productId = productId;
        this.marketId = marketId;
        this.accessoryId = accessoryId;
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
     * @return the marketId
     */
    public String getMarketId() {
        return marketId;
    }

    /**
     * @return the accessoryId
     */
    public String getAccessoryId() {
        return accessoryId;
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
        if (!(obj instanceof AccessoryKafkaMesssagePojo)) {
            return false;
        }
        final AccessoryKafkaMesssagePojo accessoryKafkaMesssagePojo = (AccessoryKafkaMesssagePojo) obj;
        return new EqualsBuilder() //
                .append(getProductId(), accessoryKafkaMesssagePojo.getProductId()) //
                .append(getMarketId(), accessoryKafkaMesssagePojo.getMarketId()) //
                .append(getAccessoryId(), accessoryKafkaMesssagePojo.getAccessoryId()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getProductId()) //
                .append(getMarketId()) //
                .append(getAccessoryId()) //
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("ProductId", getProductId()) //
                .append("MarketId", getMarketId()) //
                .append("AccessoryId", getAccessoryId()) //
                .toString();
    }

}
