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

import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Houses details for a Category
 *
 * @author Sidra Zia
 * @version 1.0
 */
public class Category {

    private final String name;

    private final String industryId;

    private final Status status;

    private final String description;

    private final String parentCategoryId;

    private final Set<SpecificationAttribute> specificationAttributes;

    private final Set<MediaAttribute> mediaAttributes;

    private final SortedSet<String> identifiers;

    /**
     * Constructor used to initialize category
     *
     * @param name name
     * @param industryId industry id
     * @param status status
     * @param description description
     * @param parentCategoryId parent category id
     * @param specificationAttributes specification attributes
     * @param mediaAttributes media attributes
     * @param identifiers attribute ids for category identifiers
     */
    @JsonCreator
    public Category(@JsonProperty("name") final String name,
            @JsonProperty("industryId") final String industryId,
            @JsonProperty("status") final Status status,
            @JsonProperty("description") final String description,
            @JsonProperty("parentCategoryId") final String parentCategoryId,
            @JsonProperty("specificationAttributes") final Set<SpecificationAttribute> specificationAttributes,
            @JsonProperty("mediaAttributes") final Set<MediaAttribute> mediaAttributes,
            @JsonProperty("identifiers") final SortedSet<String> identifiers) {
        this.name = name;
        this.industryId = industryId;
        this.status = status;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.specificationAttributes = specificationAttributes;
        this.mediaAttributes = mediaAttributes;
        this.identifiers = identifiers;
    }

    /**
     * Constructor used to initialize category
     *
     * @param name category name
     */
    public Category(final String name) {
        this(name, null, null, null, null, null, null, null);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the industryId
     */
    public String getIndustryId() {
        return industryId;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the parentCategoryId
     */
    public String getParentCategoryId() {
        return parentCategoryId;
    }

    /**
     * @return the specificationAttributes
     */
    public Set<SpecificationAttribute> getSpecificationAttributes() {
        return specificationAttributes;
    }

    /**
     * @return the mediaAttributes
     */
    public Set<MediaAttribute> getMediaAttributes() {
        return mediaAttributes;
    }

    /**
     * @return the identifiers
     */
    public SortedSet<String> getIdentifiers() {
        return identifiers;
    }

    @Override
    public final boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof Category)) {
            return false;
        }

        final Category category = (Category) object;

        return new EqualsBuilder() //
                .append(getName(), category.getName()) //
                .append(getIndustryId(), category.getIndustryId()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getName()) //
                .append(getIndustryId()) //
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("Name", name) //
                .append("IndustryId", industryId) //
                .append("Description", description) //
                .append("ParentCategoryId", parentCategoryId) //
                .append("Status", status) //
                .append("SpecificationAttributes", specificationAttributes) //
                .append("MediaAttributes", mediaAttributes) //
                .append("Identifiers", identifiers) //
                .toString();
    }

}
