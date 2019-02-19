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

package com.etilize.burraq.eas.barcode;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This class hold parsed value of Kafka Message
 *
 * @author Nasir Ahmed
 *
 */
public class BarcodeKafkaMesssagePojo {

    private String productId;

    private String type;

    private String code;

    private String customerId;

    private String operationType;

    private String recordType;

    /**
     * Initialize {@link BarcodeKafkaMesssagePojo}
     * @param productId product Id
     * @param type Type of Barcode
     * @param code bar code
     * @param customerId customer Id
     * @param operationType e.g Add, Delete, link, unlink
     * @param recordType e.g node , relationship
     */
    public BarcodeKafkaMesssagePojo(final String productId, final String type,
            final String code, final String customerId, final String operationType,
            final String recordType) {
        this.productId = productId;
        this.type = type;
        this.code = code;
        this.customerId = customerId;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the customerId
     */
    public String getCustomerId() {
        return customerId;
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
        if (!(obj instanceof BarcodeKafkaMesssagePojo)) {
            return false;
        }
        final BarcodeKafkaMesssagePojo barcodeKafkaMesssagePojo = (BarcodeKafkaMesssagePojo) obj;
        return new EqualsBuilder() //
                .append(getProductId(), barcodeKafkaMesssagePojo.getProductId()) //
                .append(getType(), barcodeKafkaMesssagePojo.getType()) //
                .append(getCode(), barcodeKafkaMesssagePojo.getCode()) //
                .append(getCustomerId(), barcodeKafkaMesssagePojo.getCustomerId()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getProductId()) //
                .append(getType()) //
                .append(getCode()) //
                .append(getCustomerId()) //
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("ProductId", getProductId()) //
                .append("Type", getType()) //
                .append("Code", getCode()) //
                .append("CustomerId", getCustomerId()) //
                .toString();
    }

}
