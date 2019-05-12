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

import static com.etilize.burraq.eas.ExportAggregationConstants.*;
import static com.etilize.burraq.eas.utils.Utils.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.etilize.burraq.eas.category.structures.CategoryStructureService;
import com.etilize.burraq.eas.specification.Product;
import com.etilize.burraq.eas.specification.SpecificationService;
import com.etilize.burraq.eas.specification.status.SpecificationStatus;
import com.etilize.burraq.eas.specification.status.SpecificationStatusRepository;
import com.google.common.collect.Maps;

/**
 * It implements {@link MediaSpecificationService}
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Service
public class MediaSpecificationServiceImpl implements MediaSpecificationService {

    private static final String LOCALE_ID_IS_REQUIRED = "localeId is required";

    private static final String PRODUCT_ID_IS_REQUIRED = "productId is required";

    private static final String STATUS_IS_REQUIRED = "status is required";

    private static final String ATTRIBUTE_ID_IS_REQUIRED = "attributeId is required";

    private final BasicMediaSpecificationRepository basicSpecificationRepository;

    private final RichMediaSpecificationRepository detailedSpecificationRepository;

    private final SpecificationStatusRepository specsStatusRepository;

    private final CategoryStructureService categoryStructureService;

    private final SpecificationService specificationService;

    /**
     * Constructs with dependencies
     *
     * @param categoryStructureService categoryStructureService
     * @param specificationService specificationService
     * @param basicSpecificationRepository basicSpecificationRepository
     * @param detailedSpecificationRepository detailedSpecificationRepository
     * @param specsStatusRepository specsStatusRepository
     */
    @Autowired
    public MediaSpecificationServiceImpl(
            final CategoryStructureService categoryStructureService,
            final SpecificationService specificationService,
            final BasicMediaSpecificationRepository basicSpecificationRepository,
            final RichMediaSpecificationRepository detailedSpecificationRepository,
            final SpecificationStatusRepository specsStatusRepository) {
        Assert.notNull(categoryStructureService,
                "categoryStructureService should not be null.");
        Assert.notNull(specificationService, "specificationService should not be null.");
        Assert.notNull(basicSpecificationRepository,
                "basicSpecificationRepository should not be null.");
        Assert.notNull(detailedSpecificationRepository,
                "detailedSpecificationRepository should not be null.");
        Assert.notNull(specsStatusRepository,
                "specsStatusRepository should not be null.");
        this.categoryStructureService = categoryStructureService;
        this.specificationService = specificationService;
        this.basicSpecificationRepository = basicSpecificationRepository;
        this.detailedSpecificationRepository = detailedSpecificationRepository;
        this.specsStatusRepository = specsStatusRepository;
    }

    @Override
    public void addLocale(final String productId, final String localeId) {
        Assert.hasText(productId, PRODUCT_ID_IS_REQUIRED);
        Assert.hasText(localeId, LOCALE_ID_IS_REQUIRED);

        final String id = generateId(productId, localeId);
        final RichMediaSpecification detialedSpecs = new RichMediaSpecification();
        detialedSpecs.setId(id);
        detialedSpecs.setAttributes(getDetailedSpecAtributesOfEN(productId, localeId));
        detialedSpecs.setLocaleId(localeId);
        detialedSpecs.setProductId(productId);
        detialedSpecs.setLastUpdateDate(new Date());
        detailedSpecificationRepository.save(detialedSpecs);

        final BasicMediaSpecification basicSpecs = new BasicMediaSpecification();
        basicSpecs.setId(id);
        basicSpecs.setAttributes(getBasicSpecAtributesOfEN(productId, localeId));
        basicSpecs.setLocaleId(localeId);
        basicSpecs.setProductId(productId);
        basicSpecs.setLastUpdateDate(new Date());
        basicSpecificationRepository.save(basicSpecs);
    }

    @Override
    public void saveAttribute(final String productId, final String localeId,
            final String attributeId, final Status status,
            final MediaAttributeValue value) {
        Assert.hasText(productId, PRODUCT_ID_IS_REQUIRED);
        Assert.hasText(localeId, LOCALE_ID_IS_REQUIRED);
        Assert.hasText(attributeId, ATTRIBUTE_ID_IS_REQUIRED);
        Assert.notNull(status, STATUS_IS_REQUIRED);
        switch (status) {
            case ASSOCIATED:
                if (value!=null && StringUtils.isNotBlank(value.getUrl())) {
                    updateAttribute(productId, localeId, attributeId, value);
                }
                break;
            case PENDING:
            case EXCEPTION:
            case DELETED:
                removeAttribute(productId, localeId, attributeId);
                break;
            default:
                break;
        }
    }

    private void updateAttribute(final String productId, final String localeId,
            final String attributeId, final MediaAttributeValue value) {
        final Product product = specificationService.findProductByProductId(
                productId).get();
        final boolean isOfferedInBasic = categoryStructureService.hasBasicMediaOfferingAttribute(
                product.getCategoryId(), attributeId);
        final boolean isOfferedInRich = categoryStructureService.hasRichMediaOfferingAttribute(
                product.getCategoryId(), attributeId);
        if (LOCALE_EN.equalsIgnoreCase(localeId)) {
            final List<SpecificationStatus> specsStatuses = specsStatusRepository.findAllByProductId(
                    productId);
            specsStatuses.forEach(specsStatus -> {
                final String id = generateId(productId, specsStatus.getLocaleId());
                if (isOfferedInRich) {
                    detailedSpecificationRepository.updateAttribute(id, attributeId,
                            value);
                }
                if (isOfferedInBasic) {
                    basicSpecificationRepository.updateAttribute(id, attributeId, value);
                }
            });
        } else {
            final String id = generateId(productId, localeId);
            if (isOfferedInRich) {
                detailedSpecificationRepository.updateAttribute(id, attributeId, value);
            }
            if (isOfferedInBasic) {
                basicSpecificationRepository.updateAttribute(id, attributeId, value);
            }
        }
    }

    private void removeAttribute(final String productId, final String localeId,
            final String attributeId) {
        final Product product = specificationService.findProductByProductId(
                productId).get();
        final boolean isOfferedInBasic = categoryStructureService.hasBasicMediaOfferingAttribute(
                product.getCategoryId(), attributeId);
        final boolean isOfferedInRich = categoryStructureService.hasRichMediaOfferingAttribute(
                product.getCategoryId(), attributeId);
        if (LOCALE_EN.equalsIgnoreCase(localeId)) {
            final List<SpecificationStatus> specsStatuses = specsStatusRepository.findAllByProductId(
                    productId);
            specsStatuses.forEach(specsStatus -> {
                final String id = generateId(productId, specsStatus.getLocaleId());
                if (isOfferedInRich) {
                    detailedSpecificationRepository.removeAttribute(id, attributeId);
                }
                if (isOfferedInBasic) {
                    basicSpecificationRepository.removeAttribute(id, attributeId);
                }
            });
        } else {
            final String id = generateId(productId, localeId);
            if (isOfferedInRich) {
                detailedSpecificationRepository.removeAttribute(id, attributeId);
            }
            if (isOfferedInBasic) {
                basicSpecificationRepository.removeAttribute(id, attributeId);
            }
        }
    }

    private Map<String, MediaAttributeValue> getDetailedSpecAtributesOfEN(
            final String productId, final String localeId) {
        Optional<RichMediaSpecification> detailedSpecsOfEN = Optional.empty();
        if (!LOCALE_EN.equalsIgnoreCase(localeId)) {
            detailedSpecsOfEN = getDetailedSpecification(productId, LOCALE_EN);
        }
        return detailedSpecsOfEN.isPresent() ? detailedSpecsOfEN.get().getAttributes()
                : Maps.newHashMap();
    }

    private Map<String, MediaAttributeValue> getBasicSpecAtributesOfEN(
            final String productId, final String localeId) {
        Optional<BasicMediaSpecification> basicSpecsOfEN = Optional.empty();
        if (!LOCALE_EN.equalsIgnoreCase(localeId)) {
            basicSpecsOfEN = getBasicSpecification(productId, LOCALE_EN);
        }
        return basicSpecsOfEN.isPresent() ? basicSpecsOfEN.get().getAttributes()
                : Maps.newHashMap();
    }

    private Optional<RichMediaSpecification> getDetailedSpecification(
            final String productId, final String localeId) {
        return detailedSpecificationRepository.findById(generateId(productId, localeId));
    }

    private Optional<BasicMediaSpecification> getBasicSpecification(
            final String productId, final String localeId) {
        return basicSpecificationRepository.findById(generateId(productId, localeId));
    }
}
