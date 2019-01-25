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

package com.etilize.burraq.eas.barcode;

import static com.etilize.burraq.eas.barcode.Barcode.*;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

/**
 * This class represents POJO for barcodes.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@DynamoDBTable(tableName = TABLE_NAME)
public class Barcode {

    public static final String TABLE_NAME = "barcodes";

    @Id
    private BarcodeId barcodeId;

    private String productId;

    private String localeId;

    private Map<String, Set<String>> codes;

    @DateTimeFormat(iso = ISO.DATE_TIME)
    private Date lastUpdateDate;

    public Barcode() {

    }

    /**
     * @return the id
     */
    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return barcodeId != null ? barcodeId.getId() : null;
    }

    /**
     * @param id the id to set
     */
    public void setId(final String id) {
        if (barcodeId == null) {
            barcodeId = new BarcodeId();
        }
        barcodeId.setId(id);
    }

    /**
     * @return the type
     */
    @DynamoDBRangeKey(attributeName = "type")
    public String getType() {
        return barcodeId != null ? barcodeId.getType() : null;
    }

    /**
     * @param type type to set
     */
    public void setType(final String type) {
        if (barcodeId == null) {
            barcodeId = new BarcodeId();
        }
        barcodeId.setType(type);
    }

    /**
     * @return the codes
     */
    public Map<String, Set<String>> getCodes() {
        return codes;
    }

    /**
     * @param codes the codes to set
     */
    public void setCodes(final Map<String, Set<String>> codes) {
        this.codes = codes;
    }

    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(final String productId) {
        this.productId = productId;
    }

    /**
     * @return the localeId
     */
    public String getLocaleId() {
        return localeId;
    }

    /**
     * @param localeId the localeId to set
     */
    public void setLocaleId(final String localeId) {
        this.localeId = localeId;
    }

    /**
     * @return the lastUpdateDate
     */
    @DynamoDBTypeConverted(converter = com.etilize.burraq.eas.converter.DateConverter.class)
    public Date getLastUpdateDate() {
        return lastUpdateDate != null ? new Date(lastUpdateDate.getTime()) : null;
    }

    /**
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
        if (!(obj instanceof Barcode)) {
            return false;
        }
        final Barcode customerCode = (Barcode) obj;
        return new EqualsBuilder() //
                .append(getId(), customerCode.getId()) //
                .append(getType(), customerCode.getType()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getId()) //
                .append(getType()) //
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("Id", getId()) //
                .append("Type", getType()) //
                .append("ProductId", productId) //
                .append("LocaleId", localeId) //
                .append("Codes", codes) //
                .append("LastUpdateDate", lastUpdateDate) //
                .toString();
    }
}
