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

package com.etilize.burraq.eas.kafka.debezium;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.etilize.burraq.eas.media.specification.Status;

/**
 * Represents PMSProductMediaEventRequest request from PMS
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class PMSProductMediaEventRequest {

    private final String productId;

    private final String localeId;

    private final String attributeId;

    private final Status status;

    private final String value;

    /**
     * Constructor to instantiate object.
     *
     * @param productId {@link String} productId
     * @param localeId {@link String} localeId
     * @param attributeId {@link String} attributeId
     * @param status {@link Status} status
     * @param value {@link String} value
     */
    public PMSProductMediaEventRequest(final String productId, final String localeId,
            final String attributeId, final Status status, final String value) {
        this.productId = productId;
        this.localeId = localeId;
        this.attributeId = attributeId;
        this.status = status;
        this.value = value;
    }

    /**
     * @return {@link String} productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @return {@link String} localeId
     */
    public String getLocaleId() {
        return localeId;
    }

    /**
     * @return {@link String} attributeId
     */
    public String getAttributeId() {
        return attributeId;
    }

    /**
     * @return {@link Status} status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return {@link String} value
     */
    public String getValue() {
        return value;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PMSProductMediaEventRequest)) {
            return false;
        }
        final PMSProductMediaEventRequest pmsPMSProductMediaEventRequest = (PMSProductMediaEventRequest) obj;
        return new EqualsBuilder() //
                .append(getProductId(), pmsPMSProductMediaEventRequest.getProductId()) //
                .append(getLocaleId(), pmsPMSProductMediaEventRequest.getLocaleId()) //
                .append(getValue(), pmsPMSProductMediaEventRequest.getValue()) //
                .append(getStatus(), pmsPMSProductMediaEventRequest.getStatus()) //
                .append(getAttributeId(), pmsPMSProductMediaEventRequest.getAttributeId()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getProductId()) //
                .append(getLocaleId()) //
                .append(getAttributeId()) //
                .append(getStatus()) //
                .append(getValue()) //
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("ProductId", getProductId()) //
                .append("LocaleId", getLocaleId()) //
                .append("AttributeId", getAttributeId()) //
                .append("Status", getStatus()) //
                .append("Value", getValue()) //
                .build();
    }
}
