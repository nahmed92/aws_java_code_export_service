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

package com.etilize.burraq.eas.taxonomy;

import com.etilize.burraq.eas.attribute.Attribute;
import com.etilize.burraq.eas.category.Category;
import com.etilize.burraq.eas.industry.Industry;
import com.etilize.burraq.eas.media.attribute.MediaAttribute;

/**
 * It contains business logic to communicate and deal with taxonomy related service clients.
 *
 * @author Umar Zubair
 * @since 1.0
 */
public interface TaxonomyService {

    /**
     * It is used to find industry by id.
     *
     * @param industryId industry id
     * @return Industry
     */
    Industry findIndustryById(String industryId);

    /**
     * It is used to find category by id.
     *
     * @param categoryId category id
     * @return Category
     */
    Category findCategoryById(String categoryId);

    /**
     * It is used to find attribute by id.
     *
     * @param attributeId attribute id
     * @return Attribute
     */
    Attribute findAttributeById(String attributeId);

    /**
     * It is used to find media attribute by id.
     *
     * @param attributeId attribute id
     * @return MediaAttribute
     */
    MediaAttribute findMediaAttributeById(String attributeId);
}
