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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.etilize.burraq.eas.attribute.Attribute;
import com.etilize.burraq.eas.attribute.AttributeServiceClient;
import com.etilize.burraq.eas.category.Category;
import com.etilize.burraq.eas.category.CategoryServiceClient;
import com.etilize.burraq.eas.industry.Industry;
import com.etilize.burraq.eas.industry.IndustryServiceClient;
import com.etilize.burraq.eas.media.attribute.MediaAttribute;
import com.etilize.burraq.eas.media.attribute.MediaAttributeServiceClient;

/**
 * It implements {@link TaxonomyService}
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Service
public class TaxonomyServiceImpl implements TaxonomyService {

    private static final String INDUSTRY_ID_IS_REQUIRED = "industryId is required";

    private static final String CATGEORY_ID_IS_REQUIRED = "categoryId is required";

    private static final String ATTRIBUTE_ID_IS_REQUIRED = "attributeId is required";

    private final IndustryServiceClient industryServiceClient;

    private final CategoryServiceClient categoryServiceClient;

    private final AttributeServiceClient attributeServiceClient;

    private final MediaAttributeServiceClient mediaAttributeServiceClient;

    /**
     * Constructs with dependencies
     *
     * @param industryServiceClient industryServiceClient
     * @param categoryServiceClient categoryServiceClient
     * @param attributeServiceClient attributeServiceClient
     * @param mediaAttributeServiceClient mediaAttributeServiceClient
     */
    @Autowired
    public TaxonomyServiceImpl(final IndustryServiceClient industryServiceClient,
            final CategoryServiceClient categoryServiceClient,
            final AttributeServiceClient attributeServiceClient,
            final MediaAttributeServiceClient mediaAttributeServiceClient) {
        Assert.notNull(industryServiceClient,
                "industryServiceClient should not be null.");
        Assert.notNull(categoryServiceClient,
                "categoryServiceClient should not be null.");
        Assert.notNull(attributeServiceClient,
                "attributeServiceClient should not be null.");
        Assert.notNull(mediaAttributeServiceClient,
                "mediaAttributeServiceClient should not be null.");
        this.industryServiceClient = industryServiceClient;
        this.categoryServiceClient = categoryServiceClient;
        this.attributeServiceClient = attributeServiceClient;
        this.mediaAttributeServiceClient = mediaAttributeServiceClient;
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.eas.taxonomy.TaxonomyService#findIndustryById(java.lang.String)
     */
    @Override
    public Industry findIndustryById(String industryId) {
        Assert.hasText(industryId, INDUSTRY_ID_IS_REQUIRED);
        return industryServiceClient.findById(industryId).getContent();
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.eas.taxonomy.TaxonomyService#findCategoryById(java.lang.String)
     */
    @Override
    public Category findCategoryById(String categoryId) {
        Assert.hasText(categoryId, CATGEORY_ID_IS_REQUIRED);
        return categoryServiceClient.findById(categoryId).getContent();
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.eas.taxonomy.TaxonomyService#findAttributeById(java.lang.String)
     */
    @Override
    public Attribute findAttributeById(String attributeId) {
        Assert.hasText(attributeId, ATTRIBUTE_ID_IS_REQUIRED);
        return attributeServiceClient.findById(attributeId).getContent();
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.eas.taxonomy.TaxonomyService#findMediaAttributeById(java.lang.String)
     */
    @Override
    public MediaAttribute findMediaAttributeById(String attributeId) {
        Assert.hasText(attributeId, ATTRIBUTE_ID_IS_REQUIRED);
        return mediaAttributeServiceClient.findById(attributeId).getContent();
    }
}
