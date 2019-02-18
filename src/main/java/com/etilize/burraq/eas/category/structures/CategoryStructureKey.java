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

package com.etilize.burraq.eas.category.structures;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

/**
 * This class represents ID for POJO for category structures related tables.
 *
 * @author Umar Zubair
 * @since 1.0
 */
public class CategoryStructureKey {

    private String categoryId;

    private String localeId;

    /**
     * @return the categoryId
     */
    @DynamoDBHashKey
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(final String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the localeId
     */
    @DynamoDBRangeKey
    public String getLocaleId() {
        return localeId;
    }

    /**
     * @param localeId the localeId to set
     */
    public void setLocaleId(final String localeId) {
        this.localeId = localeId;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CategoryStructureKey)) {
            return false;
        }
        final CategoryStructureKey key = (CategoryStructureKey) obj;
        return new EqualsBuilder() //
                .append(getCategoryId(), key.getCategoryId()) //
                .append(getLocaleId(), key.getLocaleId()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getCategoryId()) //
                .append(getLocaleId()) //
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("CategoryId", getCategoryId()) //
                .append("LocaleId", getLocaleId()) //
                .toString();
    }
}
