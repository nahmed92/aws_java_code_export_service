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

package com.etilize.burraq.eas.media.attribute;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents POJO for media attribute.
 *
 * @author Umar Zubair
 * @since 1.0
 */
public class MediaAttribute {

    private String name;

    private final String description;

    private final ContentType[] contentTypes;

    private final String industryId;

    private final Type type;

    private final String helpText;

    private Boolean isRequired = Boolean.FALSE;

    private Boolean isLocaleSpecific = Boolean.FALSE;

    private Boolean isSystemLevel = Boolean.FALSE;

    private Boolean manipulate = Boolean.FALSE;

    /**
     * Constructor with dependencies as arguments
     *
     * @param name name of attribute
     * @param description details
     * @param contentTypes Array of {@link ContentType} of attribute
     * @param industryId industry id
     * @param helpText help text
     * @param type type
     */
    @JsonCreator
    public MediaAttribute(@JsonProperty("name") final String name,
            @JsonProperty("description") final String description,
            @JsonProperty("contentTypes") final ContentType[] contentTypes,
            @JsonProperty("industryId") final String industryId,
            @JsonProperty("helpText") final String helpText,
            @JsonProperty("type") final Type type) {
        this.name = name;
        this.description = description;
        this.contentTypes = contentTypes == null ? null : contentTypes.clone();
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
     * @return the helpText
     */
    public String getHelpText() {
        return helpText;
    }

    /**
     * @return the isRequired
     */
    @JsonProperty("isRequired")
    public Boolean getIsRequired() {
        return isRequired;
    }

    /**
     * @param isRequired the isRequired to set
     */
    @JsonProperty("isRequired")
    public void setIsRequired(final Boolean isRequired) {
        this.isRequired = isRequired;
    }

    /**
     * @return the isLocaleSpecific
     */
    @JsonProperty("isLocaleSpecific")
    public Boolean getIsLocaleSpecific() {
        return isLocaleSpecific;
    }

    /**
     * @param isLocaleSpecific the isLocaleSpecific to set
     */
    @JsonProperty("isLocaleSpecific")
    public void setIsLocaleSpecific(final Boolean isLocaleSpecific) {
        this.isLocaleSpecific = isLocaleSpecific;
    }

    /**
     * @return the isSystemLevel
     */
    @JsonProperty("isSystemLevel")
    public Boolean getIsSystemLevel() {
        return isSystemLevel;
    }

    /**
     * @param isSystemLevel the isSystemLevel to set
     */
    @JsonProperty("isSystemLevel")
    public void setIsSystemLevel(final Boolean isSystemLevel) {
        this.isSystemLevel = isSystemLevel;
    }

    /**
     * @return the manipulate
     */
    public Boolean getManipulate() {
        return manipulate;
    }

    /**
     * @param manipulate the manipulate to set
     */
    public void setManipulate(final Boolean manipulate) {
        this.manipulate = manipulate;
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
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * @return the contentTypes
     */
    public ContentType[] getContentTypes() {
        return contentTypes == null ? null : contentTypes.clone();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getName()) //
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
        if (!(obj instanceof MediaAttribute)) {
            return false;
        }
        final MediaAttribute mediaAttribute = (MediaAttribute) obj;
        return new EqualsBuilder() //
                .append(getName(), mediaAttribute.getName()) //
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
                .append("IndustryId", industryId) //
                .append("Name", name) //
                .append("Description", description) //
                .append("Type", type) //
                .append("ContentTypes", contentTypes) //
                .append("HelpText", helpText) //
                .append("IsRequired", isRequired) //
                .append("IsLocaleSpecific", isLocaleSpecific) //
                .append("IsSystemLevel", isSystemLevel) //
                .append("Manipulate", manipulate) //
                .toString();
    }

}
