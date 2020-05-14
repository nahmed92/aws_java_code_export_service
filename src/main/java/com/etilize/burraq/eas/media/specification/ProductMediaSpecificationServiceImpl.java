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
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.etilize.burraq.eas.category.specification.CategorySpecificationService;
import com.etilize.burraq.eas.media.status.ProductMediaStatus;
import com.etilize.burraq.eas.media.status.ProductMediaStatusRepository;
import com.etilize.burraq.eas.specification.Product;
import com.etilize.burraq.eas.specification.ProductSpecificationService;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatus;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatusRepository;
import com.google.common.collect.Maps;

/**
 * It implements {@link ProductMediaSpecificationService}
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Service
public class ProductMediaSpecificationServiceImpl
        implements ProductMediaSpecificationService {

    private static final String LOCALE_ID_IS_REQUIRED = "localeId is required";

    private static final String PRODUCT_ID_IS_REQUIRED = "productId is required";

    private static final String STATUS_IS_REQUIRED = "status is required";

    private static final String ATTRIBUTE_ID_IS_REQUIRED = "attributeId is required";

    private final ProductBasicMediaSpecificationRepository basicSpecificationRepository;

    private final ProductRichMediaSpecificationRepository detailedSpecificationRepository;

    private final ProductMediaStatusRepository mediaStatusRepository;

    private final CategorySpecificationService categoryStructureService;

    private final ProductSpecificationService specificationService;

    /**
     * Constructs with dependencies
     *
     * @param categoryStructureService categoryStructureService
     * @param specificationService specificationService
     * @param basicSpecificationRepository basicSpecificationRepository
     * @param detailedSpecificationRepository detailedSpecificationRepository
     * @param mediaStatusRepository mediaStatusRepository
     */
    @Autowired
    public ProductMediaSpecificationServiceImpl(
            final CategorySpecificationService categoryStructureService,
            final ProductSpecificationService specificationService,
            final ProductBasicMediaSpecificationRepository basicSpecificationRepository,
            final ProductRichMediaSpecificationRepository detailedSpecificationRepository,
            final ProductMediaStatusRepository mediaStatusRepository) {
        Assert.notNull(categoryStructureService,
                "categoryStructureService should not be null.");
        Assert.notNull(specificationService, "specificationService should not be null.");
        Assert.notNull(basicSpecificationRepository,
                "basicSpecificationRepository should not be null.");
        Assert.notNull(detailedSpecificationRepository,
                "detailedSpecificationRepository should not be null.");
        Assert.notNull(mediaStatusRepository,
                "mediaStatusRepository should not be null.");
        this.categoryStructureService = categoryStructureService;
        this.specificationService = specificationService;
        this.basicSpecificationRepository = basicSpecificationRepository;
        this.detailedSpecificationRepository = detailedSpecificationRepository;
        this.mediaStatusRepository = mediaStatusRepository;
    }

    @Override
    public void addLocale(final String productId, final String localeId) {
        Assert.hasText(productId, PRODUCT_ID_IS_REQUIRED);
        Assert.hasText(localeId, LOCALE_ID_IS_REQUIRED);

        final String id = generateId(productId, localeId);
        if (!detailedSpecificationRepository.existsById(id)) {
            final ProductRichMediaSpecification detialedSpecs = new ProductRichMediaSpecification();
            detialedSpecs.setId(id);
            detialedSpecs.setAttributes(
                    getDetailedSpecAtributesOfEN(productId, localeId));
            detialedSpecs.setLocaleId(localeId);
            detialedSpecs.setProductId(productId);
            detialedSpecs.setLastUpdateDate(new Date());
            detailedSpecificationRepository.save(detialedSpecs);
        }

        if (!basicSpecificationRepository.existsById(id)) {
            final ProductBasicMediaSpecification basicSpecs = new ProductBasicMediaSpecification();
            basicSpecs.setId(id);
            basicSpecs.setAttributes(getBasicSpecAtributesOfEN(productId, localeId));
            basicSpecs.setLocaleId(localeId);
            basicSpecs.setProductId(productId);
            basicSpecs.setLastUpdateDate(new Date());
            basicSpecificationRepository.save(basicSpecs);
        }
    }

    @Override
    public void saveAttribute(final String productId, final String localeId,
            final String attributeId, final Status status,
            final ProductMediaAttributeValue value) {
        Assert.hasText(productId, PRODUCT_ID_IS_REQUIRED);
        Assert.hasText(localeId, LOCALE_ID_IS_REQUIRED);
        Assert.hasText(attributeId, ATTRIBUTE_ID_IS_REQUIRED);
        Assert.notNull(status, STATUS_IS_REQUIRED);

        if (!basicSpecificationRepository.existsById(generateId(productId, localeId))) {
            // it is required because some times PMS attribute messages comes before add locale in data migration.
            addLocale(productId, localeId);
        }
        switch (status) {
            case ASSOCIATED:
                if (value != null && StringUtils.isNotBlank(value.getUrl())) {
                    updateAttribute(productId, localeId, attributeId, value);
                }
                break;
            case EXCEPTION:
            case DELETED:
                removeAttribute(productId, localeId, attributeId);
                break;
            default:
                break;
        }
    }

    @Override
    public void saveTag(final String productId, final String localeId,
            final String attributeId, final ProductMediaAttributeValue value) {
        Assert.hasText(productId, PRODUCT_ID_IS_REQUIRED);
        Assert.hasText(localeId, LOCALE_ID_IS_REQUIRED);
        Assert.hasText(attributeId, ATTRIBUTE_ID_IS_REQUIRED);
        updateAttribute(productId, localeId, attributeId, value);
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.eas.specification.ProductSpecificationService#updateProductCategory(java.lang.String, java.lang.String)
     */
    @Override
    public void updateProductCategory(final String productId, final String categoryId) {
        final List<ProductMediaStatus> specsStatuses = mediaStatusRepository.findAllByProductId(
                productId);
        // Get Basic Media category Attribute
        final Set<String> basicMediaOfferingAttribute = categoryStructureService.findBasicMediaSpecsOfferingAttributes(
                categoryId).keySet();
        // Get Rich category Attribute
        final Set<String> richMediaOfferingAttribute = categoryStructureService.findRichMediaSpecsOfferingAttributes(
                categoryId).keySet();
        specsStatuses.forEach(specsStatus -> {
            final String id = generateId(productId, specsStatus.getLocaleId());
            final Optional<ProductBasicMediaSpecification> productBasicMediaSpecs = basicSpecificationRepository.findById(
                    id);
            if (productBasicMediaSpecs.isPresent()) {
                final ProductBasicMediaSpecification basicMediaSpecs = productBasicMediaSpecs.get();
                final Map<String, ProductMediaAttributeValue> commonAttribute = getCategoriesCommonAttribute(
                        basicMediaSpecs.getAttributes(), basicMediaOfferingAttribute);
                basicMediaSpecs.setAttributes(commonAttribute);
                basicMediaSpecs.setLastUpdateDate(new Date());
                basicSpecificationRepository.save(basicMediaSpecs);
            }
            final Optional<ProductRichMediaSpecification> richBasicMediaSpecs = detailedSpecificationRepository.findById(
                    id);
            if (richBasicMediaSpecs.isPresent()) {
                final ProductRichMediaSpecification richMediaSpecs = richBasicMediaSpecs.get();
                final Map<String, ProductMediaAttributeValue> commonAttribute = getCategoriesCommonAttribute(
                        richMediaSpecs.getAttributes(), richMediaOfferingAttribute);
                richMediaSpecs.setAttributes(commonAttribute);
                richMediaSpecs.setLastUpdateDate(new Date());
                detailedSpecificationRepository.save(richMediaSpecs);
            }
        });
    }

    private Map<String, ProductMediaAttributeValue> getCategoriesCommonAttribute(
            final Map<String, ProductMediaAttributeValue> mediaAttributeByAttributeId,
            final Set<String> offeringAttribute) {
        final Map<String, ProductMediaAttributeValue> attributes = Maps.newHashMap();
        mediaAttributeByAttributeId.entrySet().stream().forEach(entry -> {
            if (offeringAttribute.contains(entry.getKey())) {
                attributes.put(entry.getKey(), entry.getValue());
            }

        });
        return attributes;
    }

    private void updateAttribute(final String productId, final String localeId,
            final String attributeId, final ProductMediaAttributeValue value) {
        final Product product = specificationService.findProductByProductId(
                productId).get();
        final boolean isOfferedInBasic = categoryStructureService.hasBasicMediaOfferingAttribute(
                product.getCategoryId(), attributeId);
        final boolean isOfferedInRich = categoryStructureService.hasRichMediaOfferingAttribute(
                product.getCategoryId(), attributeId);
        if (LOCALE_EN.equalsIgnoreCase(localeId)) {
            final List<ProductMediaStatus> specsStatuses = mediaStatusRepository.findAllByProductId(
                    productId);
            specsStatuses.forEach(specsStatus -> {
                final String id = generateId(productId, specsStatus.getLocaleId());
                if (isOfferedInRich) {
                    final Optional<ProductRichMediaSpecification> productRichMediaSpecification = detailedSpecificationRepository.findById(
                            id);
                    // tags and attribute fields receive in two different message
                    // when tag message received attribute is empty, it create map with StringSet value
                    // when Associated message received fetch attribute map to get tags and merge in associated attribute
                    if (productRichMediaSpecification.isPresent()) {
                        if (!productRichMediaSpecification.get().getAttributes().isEmpty()
                                && productRichMediaSpecification.get().getAttributes().get(
                                        attributeId) != null) {
                            populateProductMediaAttributeValue(
                                    productRichMediaSpecification.get(), attributeId,
                                    value);
                        }
                    }
                    detailedSpecificationRepository.updateAttribute(id, attributeId,
                            value);
                }
                if (isOfferedInBasic) {
                    final Optional<ProductBasicMediaSpecification> productBasicMediaSpecification = basicSpecificationRepository.findById(
                            id);
                    // tags and attribute fields receive in two different message
                    // when tag message received attribute is empty, it create map with StringSet value in empty
                    // when Associated message received fetch attribute map to get tags and merge in associated attribute
                    if (productBasicMediaSpecification.isPresent()) {
                        if (!productBasicMediaSpecification.get().getAttributes().isEmpty()
                                && productBasicMediaSpecification.get().getAttributes().get(
                                        attributeId) != null) {
                            populateProductMediaAttributeValue(
                                    productBasicMediaSpecification.get(), attributeId,
                                    value);
                        }
                    }
                    basicSpecificationRepository.updateAttribute(id, attributeId, value);
                }
            });
        } else {
            final String id = generateId(productId, localeId);
            if (isOfferedInRich) {
                final Optional<ProductRichMediaSpecification> productRichMediaSpecification = detailedSpecificationRepository.findById(
                        id);
                // tags and attribute fields receive in two different message
                // when tag message received attribute is empty, it create map with StringSet value
                // when Associated message received fetch attribute map to get tags and merge in associated attribute
                if (productRichMediaSpecification.isPresent()) {
                    if (!productRichMediaSpecification.get().getAttributes().isEmpty()
                            && productRichMediaSpecification.get().getAttributes().get(
                                    attributeId) != null) {
                        populateProductMediaAttributeValue(
                                productRichMediaSpecification.get(), attributeId, value);
                    }
                }
                detailedSpecificationRepository.updateAttribute(id, attributeId, value);

            }
            if (isOfferedInBasic) {
                final Optional<ProductBasicMediaSpecification> productBasicMediaSpecification = basicSpecificationRepository.findById(
                        id);
                // tags and attribute fields receive in two different message
                // when tag message received attribute is empty, it create map with StringSet value in empty
                // when Associated message received fetch attribute map to get tags and merge in associated attribute
                if (productBasicMediaSpecification.isPresent()) {
                    if (!productBasicMediaSpecification.get().getAttributes().isEmpty()
                            && productBasicMediaSpecification.get().getAttributes().get(
                                    attributeId) != null) {
                        populateProductMediaAttributeValue(
                                productBasicMediaSpecification.get(), attributeId, value);
                    }
                }
                basicSpecificationRepository.updateAttribute(id, attributeId, value);
            }
        }
    }

    private void populateProductMediaAttributeValue(
            final ProductMediaSpecification productMediaSpecification,
            final String attributeId, final ProductMediaAttributeValue value) {
        if (value.getTags() == null) {
            value.setTags(
                    productMediaSpecification.getAttributes().get(attributeId).getTags());
            // handle scenario of tags change
        } else if (!value.getTags().containsAll(
                productMediaSpecification.getAttributes().get(attributeId).getTags())) {
            value.setHeight(productMediaSpecification.getAttributes().get(
                    attributeId).getHeight());
            value.setWidth(productMediaSpecification.getAttributes().get(
                    attributeId).getWidth());
            value.setUrl(
                    productMediaSpecification.getAttributes().get(attributeId).getUrl());
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
            final List<ProductMediaStatus> specsStatuses = mediaStatusRepository.findAllByProductId(
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

    private Map<String, ProductMediaAttributeValue> getDetailedSpecAtributesOfEN(
            final String productId, final String localeId) {
        Optional<ProductRichMediaSpecification> detailedSpecsOfEN = Optional.empty();
        if (!LOCALE_EN.equalsIgnoreCase(localeId)) {
            detailedSpecsOfEN = getDetailedSpecification(productId, LOCALE_EN);
        }
        return detailedSpecsOfEN.isPresent() ? detailedSpecsOfEN.get().getAttributes()
                : Maps.newHashMap();
    }

    private Map<String, ProductMediaAttributeValue> getBasicSpecAtributesOfEN(
            final String productId, final String localeId) {
        Optional<ProductBasicMediaSpecification> basicSpecsOfEN = Optional.empty();
        if (!LOCALE_EN.equalsIgnoreCase(localeId)) {
            basicSpecsOfEN = getBasicSpecification(productId, LOCALE_EN);
        }
        return basicSpecsOfEN.isPresent() ? basicSpecsOfEN.get().getAttributes()
                : Maps.newHashMap();
    }

    private Optional<ProductRichMediaSpecification> getDetailedSpecification(
            final String productId, final String localeId) {
        return detailedSpecificationRepository.findById(generateId(productId, localeId));
    }

    private Optional<ProductBasicMediaSpecification> getBasicSpecification(
            final String productId, final String localeId) {
        return basicSpecificationRepository.findById(generateId(productId, localeId));
    }
}
