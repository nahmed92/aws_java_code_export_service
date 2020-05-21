/*
 * #region
 * export-aggregation-service
 * %%
 * Copyright (C) 2018 - 2020 Etilize
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

package com.etilize.burraq.eas.specification;

import org.apache.commons.lang3.builder.EqualsBuilder;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents locale wise specifications for a product
 *
 * @author Umar Zubair
 * @since 1.0
 */
public class Specification {

    private final String categoryId;

    private final String industryId;

    /**
     * Constructor to instantiate {@link Specification}
     *
     * @param categoryId {@link String} product category id
     * @param industryId {@link String} product industry id
     */
    @JsonCreator
    public Specification(@JsonProperty("categoryId") final String categoryId,
            @JsonProperty("industryId") final String industryId) {
        this.categoryId = categoryId;
        this.industryId = industryId;
    }

    /**
     * Product industry id
     *
     * @return {@link String} product industry id
     */
    public String getIndustryId() {
        return industryId;
    }

    /**
     * Returns product category id
     *
     * @return {@link String} category id
     */
    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Specification)) {
            return false;
        }
        final Specification specification = (Specification) obj;
        return new EqualsBuilder() //
                .append(getCategoryId(), specification.getCategoryId()) //
                .append(getIndustryId(), specification.getIndustryId()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getCategoryId()) //
                .append(getIndustryId()) //
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("CategoryId", getCategoryId()) //
                .append("IndustryId", getIndustryId()) //
                .toString();
    }
}
