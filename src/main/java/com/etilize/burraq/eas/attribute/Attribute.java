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

package com.etilize.burraq.eas.attribute;

import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents an attribute
 *
 * @author Sidra Zia
 * @since 1.0
 */
public class Attribute {

    // id is required to set explicitly
    private String id;

    private String name;

    private final String description;

    private final String industryId;

    private final String helpText;

    private final Type type;

    private boolean isRequired;

    private Boolean isRepeatable;

    private Boolean isReadOnly;

    private Scope scope;

    private Boolean isTranslatable;

    private boolean isSystemLevel;

    private Set<Unit> units;

    private Set<String> codedValues;

    private Integer maxLength;

    private String mask;

    /**
     * Constructs an instance of an attribute
     *
     * @param name name
     * @param description description
     * @param industryId industry id
     * @param helpText help text
     * @param type type
     */
    @JsonCreator
    public Attribute(@JsonProperty("name") final String name,
            @JsonProperty("description") final String description,
            @JsonProperty("industryId") final String industryId,
            @JsonProperty("helpText") final String helpText,
            @JsonProperty("type") final Type type) {
        this.name = name;
        this.description = description;
        this.industryId = industryId;
        this.helpText = helpText;
        this.type = type;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the isRequired
     */
    @JsonProperty("isRequired")
    public boolean isRequired() {
        return isRequired;
    }

    /**
     * @param isRequired the isRequired to set
     */
    @JsonProperty("isRequired")
    public void setRequired(final boolean isRequired) {
        this.isRequired = isRequired;
    }

    /**
     * @return the isRepeatable
     */
    @JsonProperty("isRepeatable")
    public Boolean getIsRepeatable() {
        return isRepeatable;
    }

    /**
     * @param isRepeatable the isRepeatable to set
     */
    @JsonProperty("isRepeatable")
    public void setIsRepeatable(final Boolean isRepeatable) {
        this.isRepeatable = isRepeatable;
    }

    /**
     * @return the isReadOnly
     */
    @JsonProperty("isReadOnly")
    public Boolean getIsReadOnly() {
        return isReadOnly;
    }

    /**
     * @param isReadOnly the isReadOnly to set
     */
    @JsonProperty("isReadOnly")
    public void setIsReadOnly(final Boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    /**
     * @return the scope
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * @param scope the scope to set
     */
    public void setScope(final Scope scope) {
        this.scope = scope;
    }

    /**
     * @return the isTranslatable
     */
    @JsonProperty("isTranslatable")
    public Boolean getIsTranslatable() {
        return isTranslatable;
    }

    /**
     * @param isTranslatable the isTranslatable to set
     */
    @JsonProperty("isTranslatable")
    public void setIsTranslatable(final Boolean isTranslatable) {
        this.isTranslatable = isTranslatable;
    }

    /**
     * @return the isSystemLevel
     */
    @JsonProperty("isSystemLevel")
    public boolean isSystemLevel() {
        return isSystemLevel;
    }

    /**
     * @param isSystemLevel the isSystemLevel to set
     */
    @JsonProperty("isSystemLevel")
    public void setSystemLevel(final boolean isSystemLevel) {
        this.isSystemLevel = isSystemLevel;
    }

    /**
     * @return the units
     */
    public Set<Unit> getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(final Set<Unit> units) {
        this.units = units;
    }

    /**
     * @return the codedValues
     */
    public Set<String> getCodedValues() {
        return codedValues;
    }

    /**
     * @param codedValues the codedValues to set
     */
    public void setCodedValues(final Set<String> codedValues) {
        this.codedValues = codedValues;
    }

    /**
     * @return the maxLength
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * @param maxLength the maxLength to set
     */
    public void setMaxLength(final Integer maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * @return the mask
     */
    public String getMask() {
        return mask;
    }

    /**
     * @param mask the mask to set
     */
    public void setMask(final String mask) {
        this.mask = mask;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the industryId
     */
    public String getIndustryId() {
        return industryId;
    }

    /**
     * @return the helpText
     */
    public String getHelpText() {
        return helpText;
    }

    /**
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public final boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof Attribute)) {
            return false;
        }
        final Attribute attribute = (Attribute) object;
        return new EqualsBuilder() //
                .append(getId(), attribute.getId()) //
                .append(getIndustryId(), attribute.getIndustryId()) //
                .append(getName(), attribute.getName()) //
                .append(getDescription(), attribute.getDescription()) //
                .append(getType(), attribute.getType()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getId()) //
                .append(getIndustryId()) //
                .append(getName()) //
                .append(getDescription()) //
                .append(getType()) //
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("Id", getId()) //
                .append("IndustryId", getIndustryId()) //
                .append("Name", getName()) //
                .append("Description", getDescription()) //
                .append("Type", getType()) //
                .append("HelpText", getHelpText()) //
                .append("Scope", getScope()) //
                .append("IsReadOnly", getIsReadOnly()) //
                .append("IsRepeatable", getIsRepeatable()) //
                .append("IsTranslatable", getIsTranslatable()) //
                .append("IsRequired", isRequired) //
                .append("IsSystemLevel", isSystemLevel) //
                .append("Mask", getMask()) //
                .append("MaxLength", getMaxLength()) //
                .append("Units", getUnits()) //
                .append("CodedValues", getCodedValues()) //
                .toString();
    }
}
