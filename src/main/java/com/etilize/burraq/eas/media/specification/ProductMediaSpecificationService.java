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

package com.etilize.burraq.eas.media.specification;

/**
 * It contains business logic to maintain rich and basic media specifications.
 *
 * @author Umar Zubair
 * @since 1.0
 */
public interface ProductMediaSpecificationService {

    /**
     * It added record with productId-localeId.
     *
     * @param productId product id
     * @param localeId locale id
     */
    void addLocale(String productId, String localeId);

    /**
     * It add/update/delete an attribute for productId-localeId. If localeId is en, it will replicate change to all other locales for product.
     *
     * @param productId product id
     * @param localeId locale id
     * @param attributeId attribute id
     * @param status status
     * @param value {@link ProductMediaAttributeValue}, it is optional, will have url, height,width and tags only when status is ASSOCIATED or will have tags for any status
     */
    void saveAttribute(String productId, String localeId, String attributeId,
            Status status, ProductMediaAttributeValue value);

    /**
     * It add Tag
     *
     * @param productId product id
     * @param localeId locale id
     * @param attributeId attribute id
     * @param value {@link ProductMediaAttributeValue}, it is optional, will have url, height,width and tags only when status is ASSOCIATED or will have tags for any status
     */
    void saveTag(final String productId, final String localeId, final String attributeId,
            final ProductMediaAttributeValue value);

    /**
     * update product category on category change message
     *
     * @param productId {@link String}
     * @param categoryId {@link String}
     */
    void updateProductCategory(String productId, String categoryId);

}
