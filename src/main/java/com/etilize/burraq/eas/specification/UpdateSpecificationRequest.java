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

import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.etilize.burraq.eas.utils.Utils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * It contains parsed data for specification updates
 *
 * @author Umar Zubair
 * @since 1.0
 */
public class UpdateSpecificationRequest {

    private final String productId;

    private final String localeId;

    private final Set<String> removedAttributeIds = Sets.newHashSet();

    private final Map<String, Object> updatedAttributes = Maps.newHashMap();

    private final Map<String, Object> removedFromSetAttributes = Maps.newHashMap();

    private final Map<String, Object> addedToSetAttributes = Maps.newHashMap();

    /**
     * Constructs the object
     *
     * @param productId product id
     * @param localeId locale id
     */
    public UpdateSpecificationRequest(final String productId, final String localeId) {
        this.productId = productId;
        this.localeId = localeId;
    }

    /**
     * @return productId-localeId
     */
    public String getId() {
        return Utils.generateId(productId, localeId);
    }

    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @return the localeId
     */
    public String getLocaleId() {
        return localeId;
    }

    /**
     * It adds attribute in removedAttributeIds
     *
     * @param attributeId attribute id to remove
     */
    public void addRemovedAttributeIds(final String attributeId) {
        removedAttributeIds.add(attributeId);
    }

    /**
     * It adds attribute and value in updatedAttributes
     *
     * @param attributeId attribute id to update
     * @param value {@link Object}
     */
    public void addUpdatedAttributes(final String attributeId, final Object value) {
        updatedAttributes.put(attributeId, value);
    }

    /**
     * It adds attribute and value in removedFromSetAttributes
     *
     * @param attributeId attribute id to update
     * @param value {@link Object}
     */
    public void addRemovedFromSetAttributes(final String attributeId,
            final Object value) {
        removedFromSetAttributes.put(attributeId, value);
    }

    /**
     * It adds attribute and value in addedToSetAttributes
     *
     * @param attributeId attribute id to update
     * @param value {@link Object}
     */
    public void addAddedToSetAttributes(final String attributeId, final Object value) {
        addedToSetAttributes.put(attributeId, value);
    }

    /**
     * @return the removedAttributeIds
     */
    public Set<String> getRemovedAttributeIds() {
        return removedAttributeIds;
    }

    /**
     * @return the updatedAttributes
     */
    public Map<String, Object> getUpdatedAttributes() {
        return updatedAttributes;
    }

    /**
     * @return the removedFromSetAttributes
     */
    public Map<String, Object> getRemovedFromSetAttributes() {
        return removedFromSetAttributes;
    }

    /**
     * @return the addedToSetAttributes
     */
    public Map<String, Object> getAddedToSetAttributes() {
        return addedToSetAttributes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("ProductId", productId) //
                .append("LocaleId", localeId) //
                .append("AddedToSetAttributes", addedToSetAttributes) //
                .append("UpdatedAttributes", updatedAttributes) //
                .append("RemovedAttributeIds", removedAttributeIds) //
                .append("RemovedFromSetAttributes", removedFromSetAttributes) //
                .toString();
    }

}
