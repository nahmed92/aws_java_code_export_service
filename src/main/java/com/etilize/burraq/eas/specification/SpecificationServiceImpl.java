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

import static com.etilize.burraq.eas.ExportAggregationConstants.*;
import static com.etilize.burraq.eas.utils.Utils.*;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

/**
 * It implements {@link SpecificationService}
 * @author Umar Zubair
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    private final BasicSpecificationRepository basicSpecificationRepository;

    private final DetailedSpecificationRepository detailedSpecificationRepository;

    /**
     * Constructs with dependencies
     * @param basicSpecificationRepository basicSpecificationRepository
     * @param detailedSpecificationRepository detailedSpecificationRepository
     */
    @Autowired
    public SpecificationServiceImpl(
            final BasicSpecificationRepository basicSpecificationRepository,
            final DetailedSpecificationRepository detailedSpecificationRepository) {
        Assert.notNull(basicSpecificationRepository,
                "basicSpecificationRepository should not be null.");
        Assert.notNull(detailedSpecificationRepository,
                "detailedSpecificationRepository should not be null.");
        this.basicSpecificationRepository = basicSpecificationRepository;
        this.detailedSpecificationRepository = detailedSpecificationRepository;
    }

    @Override
    public void createProduct(final String productId, final String industryId,
            final String categoryId) {
        final String id = generateId(productId, LOCALE_EN);
        final Optional<DetailedSpecification> specs = detailedSpecificationRepository.findById(
                id);
        if (!specs.isPresent()) {
            final DetailedSpecification detialedSpecs = new DetailedSpecification();
            detialedSpecs.setId(id);
            detialedSpecs.setAttributes(Maps.newHashMap());
            detialedSpecs.setCategoryId(categoryId);
            detialedSpecs.setIndustryId(industryId);
            detialedSpecs.setLocaleId(LOCALE_EN);
            detialedSpecs.setProductId(productId);
            detialedSpecs.setLastUpdateDate(new Date());
            detailedSpecificationRepository.save(detialedSpecs);
            basicSpecificationRepository.save(getBasicSpecification(detialedSpecs));
        }
    }

    private BasicSpecification getBasicSpecification(final Specification specs) {
        final BasicSpecification basicSpecs = new BasicSpecification();
        basicSpecs.setId(specs.getId());
        basicSpecs.setAttributes(specs.getAttributes());
        basicSpecs.setCategoryId(specs.getCategoryId());
        basicSpecs.setIndustryId(specs.getIndustryId());
        basicSpecs.setLocaleId(specs.getLocaleId());
        basicSpecs.setProductId(specs.getProductId());
        basicSpecs.setLastUpdateDate(specs.getLastUpdateDate());
        return basicSpecs;
    }
}
