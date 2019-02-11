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

package com.etilize.burraq.eas.specification.value;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that holds unit & value for metric systems for attribute
 *
 * @author Umar Zubair
 * @since 1.0
 */
public class UnitValue implements SpecificationValue {

    private final Map<String, UnitAttribute> value;

    /**
     * Constructor with arguments
     *
     * @param value metric system specific unit values
     */
    @JsonCreator
    public UnitValue(@JsonProperty("value") final Map<String, UnitAttribute> value) {
        Assert.notNull(value, "value must not be null.");
        this.value = value;
    }

    /**
     * return value
     *
     * @return unitAttributes by measuring system
     */
    public Map<String, UnitAttribute> getValue() {
        return value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getValue()) //
                .hashCode();
    }

    /*
     * (non-Javadoc)
     *
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
        if (!(obj instanceof UnitValue)) {
            return false;
        }
        final UnitValue unitValue = (UnitValue) obj;
        return new EqualsBuilder() //
                .append(getValue(), unitValue.getValue()) //
                .isEquals();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("Value", value) //
                .toString();
    }

}
