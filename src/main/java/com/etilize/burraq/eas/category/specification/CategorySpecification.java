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

package com.etilize.burraq.eas.category.specification;

import java.util.Date;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This class represents common abstract method used for category structures.
 * Due to @Id, @DynamoDBHashKey and @DynamoDBRangeKey, have to defined defined abstract setter and getter to enforce common structure.
 *
 * @author Umar Zubair
 * @since 1.0
 */
public abstract class CategorySpecification {

    /**
     * @return the categoryId
     */
    public abstract String getCategoryId();

    /**
     * @param categoryId the category id to set
     */
    public abstract void setCategoryId(String categoryId);

    /**
     * @return the localeId
     */
    public abstract String getLocaleId();

    /**
     * @param localeId the locale id to set
     */
    public abstract void setLocaleId(String localeId);

    /**
     * @return the parentCategoryId
     */
    public abstract String getParentCategoryId();

    /**
     * @param parentCategoryId the parentCategoryId to set
     */
    public abstract void setParentCategoryId(String parentCategoryId);

    /**
     * @return the industryId
     */
    public abstract String getIndustryId();

    /**
     * @param industryId the industryId to set
     */
    public abstract void setIndustryId(String industryId);

    /**
     * @return the industryName
     */
    public abstract String getIndustryName();

    /**
     * @param industryName the industryName to set
     */
    public abstract void setIndustryName(String industryName);

    /**
     * @return the categoryName
     */
    public abstract String getCategoryName();

    /**
     * @param categoryName the categoryName to set
     */
    public abstract void setCategoryName(String categoryName);

    /**
     * @return the attributes
     */
    public abstract Map<String, String> getAttributes();

    /**
     * @param attributes the attributes to set
     */
    public abstract void setAttributes(Map<String, String> attributes);

    /**
     * @return the lastUpdateDate
     */
    public abstract Date getLastUpdateDate();

    /**
     * @param lastUpdateDate the lastUpdateDate to set
     */
    public abstract void setLastUpdateDate(Date lastUpdateDate);

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CategorySpecification)) {
            return false;
        }
        final CategorySpecification category = (CategorySpecification) obj;
        return new EqualsBuilder() //
                .append(getCategoryId(), category.getCategoryId()) //
                .append(getLocaleId(), category.getLocaleId()) //
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
                .append("IndustryId", getIndustryId()) //
                .append("ParentCategoryId", getParentCategoryId()) //
                .append("IndustryName", getIndustryName()) //
                .append("CategoryName", getCategoryName()) //
                .append("Attributes", getAttributes()) //
                .append("LastUpdateDate", getLastUpdateDate()) //
                .toString();
    }
}
