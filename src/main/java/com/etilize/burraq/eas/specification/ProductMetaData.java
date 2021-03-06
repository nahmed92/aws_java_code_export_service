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

package com.etilize.burraq.eas.specification;

import static com.etilize.burraq.eas.specification.ProductMetaData.*;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.etilize.burraq.eas.base.AbstractDynamoEntity;

/**
 * Class holds ProductMetaData information
 *
 * @author Nasir Ahmed
 *
 */
@DynamoDBTable(tableName = TABLE_NAME)
public class ProductMetaData extends AbstractDynamoEntity {

    public static final String TABLE_NAME = "product-metadata";

    private String productId;

    private String localeId;

    private String categoryId;

    private String industryId;

    @DateTimeFormat(iso = ISO.DATE_TIME)
    private Date lastUpdateDate;

    /**
     * Set ProductId
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Set ProductId
     *
     * @param productId the productId to set
     */
    public void setProductId(final String productId) {
        this.productId = productId;
    }

    /**
     * Get Locale Id
     *
     * @return the localeId
     */
    public String getLocaleId() {
        return localeId;
    }

    /**
     * Set Locale Id
     *
     * @param localeId the localeId to set
     */
    public void setLocaleId(final String localeId) {
        this.localeId = localeId;
    }

    /**
     * Get CategoryId
     *
     * @return the categoryId
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * Set CategoryId
     *
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(final String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Get IndustryId
     *
     * @return the industryId
     */
    public String getIndustryId() {
        return industryId;
    }

    /**
     * Set IndustryId
     *
     * @param industryId the industryId to set
     */
    public void setIndustryId(final String industryId) {
        this.industryId = industryId;
    }

    /**
     * Set Last Updated Date
     *
     * @return the lastUpdateDate
     */
    @DynamoDBTypeConverted(converter = com.etilize.burraq.eas.converter.DateConverter.class)
    public Date getLastUpdateDate() {
        return lastUpdateDate != null ? new Date(lastUpdateDate.getTime()) : null;
    }

    /**
     * Set LastUpdated Date
     *
     * @param lastUpdateDate the lastUpdateDate to set
     */
    public void setLastUpdateDate(final Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate != null ? new Date(lastUpdateDate.getTime())
                : null;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ProductMetaData)) {
            return false;
        }
        final ProductMetaData specs = (ProductMetaData) obj;
        return new EqualsBuilder() //
                .append(getProductId(), specs.getProductId()) //
                .append(getLocaleId(), specs.getLocaleId()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getProductId()) //
                .append(getLocaleId()) //
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("ProductId", productId) //
                .append("LocaleId", localeId) //
                .append("CategoryId", categoryId) //
                .append("IndustryId", industryId) //
                .append("LastUpdateDate", lastUpdateDate) //
                .toString();
    }
}
