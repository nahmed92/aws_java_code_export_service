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

package com.etilize.burraq.eas.category;

import org.apache.commons.lang.builder.EqualsBuilder;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Houses reference to an specification attributes associated to a category
 *
 * @author Umar Zubair
 * @since 1.0
 */
public class SpecificationAttribute {

    private final String attributeId;

    private final Source source;

    private final Integer order;

    /**
     * Constructor for attribute associated with category
     *
     * @param attributeId stores attributeId
     * @param source stores source of attribute
     * @param order stores order of attribute
     */
    @JsonCreator
    public SpecificationAttribute(@JsonProperty("attributeId") final String attributeId,
            @JsonProperty("source") final Source source,
            @JsonProperty("order") final Integer order) {
        this.attributeId = attributeId;
        this.source = source;
        this.order = order;
    }

    /**
     * Returns attributeId
     *
     * @return attributeId
     */
    public String getAttributeId() {
        return this.attributeId;
    }

    /**
     * Returns attribute source
     *
     * @return source
     */
    public Source getSource() {
        return this.source;
    }

    /**
     * Returns attribute order
     *
     * @return order
     */
    public Integer getOrder() {
        return this.order;
    }

    @Override
    public final boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof SpecificationAttribute)) {
            return false;
        }

        final SpecificationAttribute specsAttribute = (SpecificationAttribute) object;

        return new EqualsBuilder() //
                .append(attributeId, specsAttribute.getAttributeId()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(attributeId) //
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("AttributeId", attributeId) //
                .append("Source", source) //
                .append("Order", order) //
                .toString();
    }

}
