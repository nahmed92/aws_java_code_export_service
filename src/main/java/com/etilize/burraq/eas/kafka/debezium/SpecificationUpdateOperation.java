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

package com.etilize.burraq.eas.kafka.debezium;

import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang3.builder.*;

/**
 * Represents Update Product Specification Operation.
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class SpecificationUpdateOperation {

    private String docId;

    private String operationType;

    private Queue<Map<String, Object>> queryParams;

    /**
     * Return doc id
     *
     * @return {@link String}  docId index id
     */
    public String getDocId() {
        return docId;
    }

    /**
     * Set doc id
     *
     * @param docId {@link String} index id
     */
    public void setDocId(final String docId) {
        this.docId = docId;
    }

    /**
     * Get operation type
     *
     * @return {@link String}  index operation Type
     */
    public String getOperationType() {
        return operationType;
    }

    /**
     * Set operation type
     *
     * @param operationType {@link String}
     */
    public void setOperationType(final String operationType) {
        this.operationType = operationType;
    }

    /**
     * Get query parameters
     *
     * @return {@link Map}  index query params map
     */
    public Queue<Map<String, Object>> getQueryParams() {
        return queryParams;
    }

    /**
     * Set query parameters
     *
     * @param {@link Map} index query params map
     */
    public void setQueryParams(final Queue<Map<String, Object>> queryParams) {
        this.queryParams = queryParams;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getDocId()) //
                .append(getOperationType()) //
                .append(getQueryParams()) //
                .hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SpecificationUpdateOperation)) {
            return false;
        }
        final SpecificationUpdateOperation operation = (SpecificationUpdateOperation) obj;
        return new EqualsBuilder() //
                .append(getDocId(), operation.getDocId()) //
                .append(getQueryParams(), operation.getQueryParams()) //
                .append(getOperationType(), operation.getOperationType()) //
                .isEquals();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("DocId", getDocId()) //
                .append("QueryParams ", getQueryParams()) //
                .append("OperationType", getOperationType()) //
                .build();
    }
}
