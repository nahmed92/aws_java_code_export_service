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

package com.etilize.burraq.eas.kafka.stream;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * Represents Text Translation Update Message that this service publishes.
 *
 * @author Affan Hasan
 * @Since 1.0
 */
public class UpdateTextTranslationEvent extends Message {

    /**
     * Locale
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("Locale Id.")
    private String localeId;

    /**
     * Industry Id
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("Industry Id.")
    private String industryId;

    /**
     * English Text value
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("English Text value.")
    private String value;

    /**
     * Translation
     */
    @JsonProperty(required = true)
    @JsonPropertyDescription("Translation For The English Text.")
    private String translation;

    /**
     * Constructor for creating an instance of {@link UpdateTextTranslationEvent}
     *
     * @param source the source which created this message
     */
    @JsonCreator
    public UpdateTextTranslationEvent(@JsonProperty("source") final String source) {
        super(source);
    }

    /**
     * Returns localeId
     *
     * @return {@link String} localeId
     */
    public String getLocaleId() {
        return localeId;
    }

    /**
     * Sets localeId
     *
     * @param localeId {@link String}
     */
    public void setLocaleId(final String localeId) {
        this.localeId = localeId;
    }

    /**
     * Returns industry id
     *
     * @return {@link String} industry id
     */
    public String getIndustryId() {
        return industryId;
    }

    /**
     * Sets product localeId value
     *
     * @param value {@link String} product localeId
     */
    public void setIndustryId(final String industryId) {
        this.industryId = industryId;
    }

    /**
     * Sets English text value
     *
     * @param value {@link String} text
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * Returns value
     *
     * @return {@link String} value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets Translation
     *
     * @param value {@link String} Translation
     */
    public void setTranslation(final String translation) {
        this.translation = translation;
    }

    /**
     * Returns Translation
     *
     * @return {@link String} Translation
     */
    public String getTranslation() {
        return translation;
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.translation.stream.Message#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UpdateTextTranslationEvent)) {
            return false;
        }
        final UpdateTextTranslationEvent event = (UpdateTextTranslationEvent) obj;
        return new EqualsBuilder() //
                .append(getSource(), event.getSource()) //
                .append(getLocaleId(), event.getLocaleId()) //
                .append(getIndustryId(), event.getIndustryId()) //
                .append(getValue(), event.getValue()) //
                .append(getTranslation(), event.getTranslation()) //
                .append(getPerformedBy(), event.getPerformedBy()) //
                .append(getPerformedDate(), event.getPerformedDate()) //
                .isEquals();
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.translation.stream.Message#hashCode()
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getSource()) //
                .append(getLocaleId()) //
                .append(getIndustryId()) //
                .append(getValue()) //
                .append(getTranslation()) //
                .append(getPerformedBy()) //
                .append(getPerformedDate()) //
                .hashCode();
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.translation.stream.Message#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("LocaleId", getLocaleId()) //
                .append("IndustryId", getIndustryId()) //
                .append("Value", getValue()) //
                .append("Translation", getTranslation()) //
                .append("PerformedBy", getPerformedBy()) //
                .append("PerformedDate", getPerformedDate()) //
                .build();
    }
}
