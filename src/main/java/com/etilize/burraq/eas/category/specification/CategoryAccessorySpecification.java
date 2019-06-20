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

import static com.etilize.burraq.eas.category.specification.CategoryAccessorySpecification.*;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.google.common.collect.Maps;

/**
 * This class represents POJO for accessory category structure.
 *
 * @author Kaiser Raja
 * @since 1.0
 */
@DynamoDBTable(tableName = TABLE_NAME)
public class CategoryAccessorySpecification extends CategorySpecification {

    public static final String TABLE_NAME = "category-accessory-specifications";

    @Id
    private CategorySpecificationKey categoryKey;

    private String parentCategoryId;

    private String industryId;

    private String industryName;

    private String categoryName;

    private Map<String, String> attributes = Maps.newHashMap();

    @DateTimeFormat(iso = ISO.DATE_TIME)
    private Date lastUpdateDate;

    public CategoryAccessorySpecification() {

    }

    /**
     * @return the categoryId
     */
    @Override
    @DynamoDBHashKey(attributeName = "categoryId")
    public String getCategoryId() {
        return categoryKey != null ? categoryKey.getCategoryId() : null;
    }

    /**
     * @param categoryId the category id to set
     */
    @Override
    public void setCategoryId(final String categoryId) {
        if (categoryKey == null) {
            categoryKey = new CategorySpecificationKey();
        }
        categoryKey.setCategoryId(categoryId);
    }

    /**
     * @return the localeId
     */
    @Override
    @DynamoDBRangeKey(attributeName = "localeId")
    public String getLocaleId() {
        return categoryKey != null ? categoryKey.getLocaleId() : null;
    }

    /**
     * @param localeId the locale id to set
     */
    @Override
    public void setLocaleId(final String localeId) {
        if (categoryKey == null) {
            categoryKey = new CategorySpecificationKey();
        }
        categoryKey.setLocaleId(localeId);
    }

    /**
     * @return the lastUpdateDate
     */
    @Override
    @DynamoDBTypeConverted(converter = com.etilize.burraq.eas.converter.DateConverter.class)
    public Date getLastUpdateDate() {
        return lastUpdateDate != null ? new Date(lastUpdateDate.getTime()) : null;
    }

    /**
     * @param lastUpdateDate the lastUpdateDate to set
     */
    @Override
    public void setLastUpdateDate(final Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate != null ? new Date(lastUpdateDate.getTime())
                : null;
    }

    /**
     * @return the parentCategoryId
     */
    @Override
    public String getParentCategoryId() {
        return parentCategoryId;
    }

    /**
     * @param parentCategoryId the parentCategoryId to set
     */
    @Override
    public void setParentCategoryId(final String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    /**
     * @return the industryId
     */
    @Override
    public String getIndustryId() {
        return industryId;
    }

    /**
     * @param industryId the industryId to set
     */
    @Override
    public void setIndustryId(final String industryId) {
        this.industryId = industryId;
    }

    /**
     * @return the industryName
     */
    @Override
    public String getIndustryName() {
        return industryName;
    }

    /**
     * @param industryName the industryName to set
     */
    @Override
    public void setIndustryName(final String industryName) {
        this.industryName = industryName;
    }

    /**
     * @return the categoryName
     */
    @Override
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName the categoryName to set
     */
    @Override
    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the attributes
     */
    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    @Override
    public void setAttributes(final Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
