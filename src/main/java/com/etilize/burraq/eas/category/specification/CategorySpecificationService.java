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

import java.util.Map;
import java.util.Set;

/**
 * It contains business logic to maintain category structures.
 *
 * @author Umar Zubair
 * @since 1.0
 */
public interface CategorySpecificationService {

    /**
     * It add/update record to category structures based on offeringId.
     *
     * @param categoryId category id
     * @param offeringId offering id
     * @param attributeIds attribute ids
     */
    void save(String categoryId, String offeringId, Set<String> attributeIds);

    /**
     * It return attribute names by id for basic specs offering.
     *
     * @param categoryId category id
     * @return Map<String, String>
     */
    Map<String, String> findBasicSpecsOfferingAttributes(String categoryId);

    /**
     * It return attribute names by id for detailed specs offering.
     *
     * @param categoryId category id
     * @return Map<String, String>
     */
    Map<String, String> findDetailedSpecsOfferingAttributes(String categoryId);

    /**
     * It return attribute names by id for accessory specs offering.
     *
     * @param categoryId category id
     * @return Map<String, String>
     */
    Map<String, String> findAccessorySpecsOfferingAttributes(String categoryId);

    /**
     * It return boolean rather attribute exists in rich basic offering for category.
     *
     * @param categoryId category id
     * @param attributeId attribute id
     * @return boolean
     */
    boolean hasBasicMediaOfferingAttribute(String categoryId, String attributeId);

    /**
     * It return boolean rather attribute exists in rich media offering for category.
     *
     * @param categoryId category id
     * @param attributeId attribute id
     * @return boolean
     */
    boolean hasRichMediaOfferingAttribute(String categoryId, String attributeId);

    /**
     * Add category specs for newly added locale
     * @param localeId {@link String}
     */
     void addLocale(String localeId);
}
