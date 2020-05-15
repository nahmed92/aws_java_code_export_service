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
