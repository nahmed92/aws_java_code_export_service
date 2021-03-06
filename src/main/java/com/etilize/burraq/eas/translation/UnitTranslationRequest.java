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

package com.etilize.burraq.eas.translation;

import org.apache.commons.lang3.builder.EqualsBuilder;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This class represents request to translate unit
 *
 * @author Umar Zubair
 * @since 1.0
 */
public class UnitTranslationRequest {

    /**
     * Source Language Id
     */
    private final String sourceLanguageId;

    /**
     * Target Language Id
     */
    private final String targetLanguageId;

    private final String value;

    /**
     * Initializes new request
     *
     * @param sourceLanguageId sourceLanguageId
     * @param targetLanguageId targetLanguageId
     * @param value value to translate
     */
    public UnitTranslationRequest(final String sourceLanguageId,
            final String targetLanguageId, final String value) {
        this.sourceLanguageId = sourceLanguageId;
        this.targetLanguageId = targetLanguageId;
        this.value = value;
    }

    /**
     * @return the target language id
     */
    public String getTargetLanguageId() {
        return targetLanguageId;
    }

    /**
     * @return the source language id
     */
    public String getSourceLanguageId() {
        return sourceLanguageId;
    }

    /**
     * @return the value
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
        if (!(obj instanceof UnitTranslationRequest)) {
            return false;
        }
        final UnitTranslationRequest request = (UnitTranslationRequest) obj;
        return new EqualsBuilder() //
                .append(getSourceLanguageId(), request.getSourceLanguageId()) //
                .append(getTargetLanguageId(), request.getTargetLanguageId()) //
                .append(getValue(), request.getValue()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getSourceLanguageId()) //
                .append(getTargetLanguageId()) //
                .append(getValue()) //
                .hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("sourceLanguageId", getSourceLanguageId()) //
                .append("targetLanguageId", getTargetLanguageId()) //
                .append("Value", getValue()) //
                .toString();
    }
}
