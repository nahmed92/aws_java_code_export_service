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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.etilize.burraq.eas.attribute.Attribute;
import com.etilize.burraq.eas.attribute.Type;
import com.etilize.burraq.eas.category.specification.CategorySpecificationService;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatus;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatusRepository;
import com.etilize.burraq.eas.specification.value.UnitAttribute;
import com.etilize.burraq.eas.specification.value.UnitValue;
import com.etilize.burraq.eas.specification.value.Value;
import com.etilize.burraq.eas.taxonomy.TaxonomyService;
import com.etilize.burraq.eas.translation.TranslationService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * It implements {@link ProductSpecificationService}
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Service
public class ProductSpecificationServiceImpl implements ProductSpecificationService {

    private final ProductBasicSpecificationRepository basicSpecificationRepository;

    private final ProductDetailedSpecificationRepository detailedSpecificationRepository;

    private final TranslationService translationService;

    private final TaxonomyService taxonomyService;

    private final ProductSpecificationStatusRepository specsStatusRepository;

    private final CategorySpecificationService categoryStructureService;

    /**
     * Constructs with dependencies
     *
     * @param basicSpecificationRepository basicSpecificationRepository
     * @param detailedSpecificationRepository detailedSpecificationRepository
     * @param translationService translationService
     * @param taxonomyService taxonomyService
     * @param categoryStructureService categoryStructureService
     * @param specsStatusRepository {@link ProductSpecificationStatusRepository}
     */
    @Autowired
    public ProductSpecificationServiceImpl(
            final ProductBasicSpecificationRepository basicSpecificationRepository,
            final ProductDetailedSpecificationRepository detailedSpecificationRepository,
            final TranslationService translationService,
            final TaxonomyService taxonomyService,
            final CategorySpecificationService categoryStructureService,
            final ProductSpecificationStatusRepository specsStatusRepository) {
        Assert.notNull(basicSpecificationRepository,
                "basicSpecificationRepository should not be null.");
        Assert.notNull(detailedSpecificationRepository,
                "detailedSpecificationRepository should not be null.");
        Assert.notNull(translationService, "translationService should not be null.");
        Assert.notNull(taxonomyService, "taxonomyService should not be null.");
        Assert.notNull(categoryStructureService,
                "categoryStructureService should not be null.");
        Assert.notNull(specsStatusRepository,
                "specsStatusRepository should not be null.");
        this.basicSpecificationRepository = basicSpecificationRepository;
        this.detailedSpecificationRepository = detailedSpecificationRepository;
        this.translationService = translationService;
        this.taxonomyService = taxonomyService;
        this.categoryStructureService = categoryStructureService;
        this.specsStatusRepository = specsStatusRepository;
    }

    @Override
    public void createProduct(final String productId, final String industryId,
            final String categoryId) {
        final String id = generateId(productId, LOCALE_EN);
        final Optional<ProductDetailedSpecification> specs = detailedSpecificationRepository.findById(
                id);
        if (!specs.isPresent()) {
            final ProductDetailedSpecification detialedSpecs = new ProductDetailedSpecification();
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

    @Override
    public void addLocale(final String productId, final String localeId) {
        if (!LOCALE_EN.equalsIgnoreCase(localeId)) {
            final String id = generateId(productId, localeId);
            final Optional<ProductSpecification> detailedSpecsForEN = getDetailedSpecification(
                    productId, LOCALE_EN);
            if (detailedSpecsForEN.isPresent()) {
                final ProductDetailedSpecification detialedSpecs = new ProductDetailedSpecification();
                detialedSpecs.setId(id);
                detialedSpecs.setCategoryId(detailedSpecsForEN.get().getCategoryId());
                detialedSpecs.setIndustryId(detailedSpecsForEN.get().getIndustryId());
                detialedSpecs.setLocaleId(localeId);
                detialedSpecs.setProductId(productId);
                detialedSpecs.setLastUpdateDate(new Date());
                detailedSpecificationRepository.save(detialedSpecs);
                final Map<String, Object> translateAttributes = translateAttributes(
                        detialedSpecs.getIndustryId(), localeId,
                        detailedSpecsForEN.get().getAttributes());
                detailedSpecificationRepository.saveAttributes(id, translateAttributes);
            }

            final Optional<ProductSpecification> basicSpecsForEN = getBasicSpecification(
                    productId, LOCALE_EN);
            if (basicSpecsForEN.isPresent()) {
                final ProductBasicSpecification basicSpecs = new ProductBasicSpecification();
                basicSpecs.setId(id);
                basicSpecs.setCategoryId(basicSpecsForEN.get().getCategoryId());
                basicSpecs.setIndustryId(basicSpecsForEN.get().getIndustryId());
                basicSpecs.setLocaleId(localeId);
                basicSpecs.setProductId(productId);
                basicSpecs.setLastUpdateDate(new Date());
                basicSpecificationRepository.save(basicSpecs);
                final Map<String, Object> translateAttributes = translateAttributes(
                        basicSpecs.getIndustryId(), localeId,
                        basicSpecsForEN.get().getAttributes());
                basicSpecificationRepository.saveAttributes(id, translateAttributes);
            }
        }
    }

    @Override
    public void updateSpecifications(final UpdateProductSpecificationRequest request) {
        final Product product = findProductByProductId(request.getProductId()).get();
        final UpdateProductSpecificationRequest basicSpecsRequest = filterRequestForBasicOffering(
                product.getCategoryId(), request);
        final UpdateProductSpecificationRequest detailedSpecsRequest = filterRequestForDetailedOffering(
                product.getCategoryId(), request);
        if (request.getLocaleId().startsWith(LANGUAGE_EN)) {
            updateDataAcrossLocales(product.getIndustryId(), basicSpecsRequest,
                    detailedSpecsRequest, getAttributesUsedInRequest(request));
        } else {
            basicSpecificationRepository.saveAttributes(basicSpecsRequest);
            detailedSpecificationRepository.saveAttributes(detailedSpecsRequest);
        }
    }

    @Override
    public Optional<Product> findProductByProductId(final String productId) {
        return basicSpecificationRepository.findProductById(
                generateId(productId, LOCALE_EN));
    }

    private void updateDataAcrossLocales(final String industryId,
            final UpdateProductSpecificationRequest basicSpecsRequest,
            final UpdateProductSpecificationRequest detailedSpecsRequest,
            final Map<String, Attribute> attributesById) {
        final List<ProductSpecificationStatus> specsStatuses = specsStatusRepository.findAllByProductId(
                basicSpecsRequest.getProductId());
        final String originatedLocaleId = basicSpecsRequest.getLocaleId();
        final String originatedMarketId = StringUtils.substringAfterLast(
                originatedLocaleId, "_");
        specsStatuses.forEach(specsStatus -> {
            if (originatedLocaleId.equals(LOCALE_EN)
                    || (StringUtils.isNotBlank(originatedMarketId)
                            && specsStatus.getLocaleId().endsWith(originatedMarketId))) {
                final UpdateProductSpecificationRequest localizedBasicRequest = translateRequest(
                        originatedLocaleId, industryId, specsStatus.getLocaleId(),
                        basicSpecsRequest, attributesById);
                final UpdateProductSpecificationRequest localizedDetailedRequest = translateRequest(
                        originatedLocaleId, industryId, specsStatus.getLocaleId(),
                        detailedSpecsRequest, attributesById);
                basicSpecificationRepository.saveAttributes(localizedBasicRequest);
                detailedSpecificationRepository.saveAttributes(localizedDetailedRequest);
            }
        });
    }

    private UpdateProductSpecificationRequest filterRequestForBasicOffering(
            final String categoryId, final UpdateProductSpecificationRequest request) {
        final Map<String, String> attributes = categoryStructureService.findBasicSpecsOfferingAttributes(
                categoryId);
        if (attributes.isEmpty()) {
            return request;
        } else {
            return filterRequest(request, attributes);
        }
    }

    private UpdateProductSpecificationRequest filterRequestForDetailedOffering(
            final String categoryId, final UpdateProductSpecificationRequest request) {
        final Map<String, String> attributes = categoryStructureService.findDetailedSpecsOfferingAttributes(
                categoryId);
        if (attributes.isEmpty()) {
            return request;
        } else {
            return filterRequest(request, attributes);
        }
    }

    private UpdateProductSpecificationRequest filterRequest(
            final UpdateProductSpecificationRequest request,
            final Map<String, String> attributes) {
        final UpdateProductSpecificationRequest filteredRequest = new UpdateProductSpecificationRequest(
                request.getProductId(), request.getLocaleId());
        attributes.keySet().stream().forEach(attrId -> {
            if (request.getRemovedAttributeIds().contains(attrId)) {
                filteredRequest.addRemovedAttributeIds(attrId);
            }
            if (request.getAddedToSetAttributes().containsKey(attrId)) {
                filteredRequest.addAddedToSetAttributes(attrId,
                        request.getAddedToSetAttributes().get(attrId));
            }
            if (request.getRemovedFromSetAttributes().containsKey(attrId)) {
                filteredRequest.addRemovedFromSetAttributes(attrId,
                        request.getRemovedFromSetAttributes().get(attrId));
            }
            if (request.getUpdatedAttributes().containsKey(attrId)) {
                filteredRequest.addUpdatedAttributes(attrId,
                        request.getUpdatedAttributes().get(attrId));
            }
        });
        return filteredRequest;
    }

    private boolean isTranslationRequiredForLocale(final String localeId) {
        return !localeId.equals(LOCALE_EN) && !localeId.equals(LOCALE_EN_US);
    }

    private boolean isUpdateRequiredForLocale(final Attribute attribute,
            final String originatedLocaleId, final String localeId) {
        return attribute.getIsTranslatable() || originatedLocaleId.equals(LOCALE_EN)
                || originatedLocaleId.equals(localeId);
    }

    private UpdateProductSpecificationRequest translateRequest(final String originatedLocaleId,
            final String industryId, final String localeId,
            final UpdateProductSpecificationRequest request,
            final Map<String, Attribute> attributesById) {
        final UpdateProductSpecificationRequest translatedRequest = new UpdateProductSpecificationRequest(
                request.getProductId(), localeId);
        request.getRemovedAttributeIds().forEach(attributeId -> {
            final Attribute attribute = attributesById.get(attributeId);
            if (isUpdateRequiredForLocale(attribute, originatedLocaleId, localeId)) {
                translatedRequest.addRemovedAttributeIds(attributeId);
            }
        });
        request.getAddedToSetAttributes().entrySet().forEach(entry -> {
            final Attribute attribute = attributesById.get(entry.getKey());
            if (isUpdateRequiredForLocale(attribute, originatedLocaleId, localeId)) {
                translatedRequest.addAddedToSetAttributes(entry.getKey(),
                        entry.getValue());
                if (attribute.getIsTranslatable()
                        && isTranslationRequiredForLocale(localeId)) {
                    translatedRequest.addAddedToSetAttributes(entry.getKey(),
                            translate(industryId, localeId, entry.getValue()));
                }
            }
        });

        request.getRemovedFromSetAttributes().entrySet().forEach(entry -> {
            final Attribute attribute = attributesById.get(entry.getKey());
            if (isUpdateRequiredForLocale(attribute, originatedLocaleId, localeId)) {
                translatedRequest.addRemovedFromSetAttributes(entry.getKey(),
                        entry.getValue());
                if (attribute.getIsTranslatable()
                        && isTranslationRequiredForLocale(localeId)) {
                    translatedRequest.addRemovedFromSetAttributes(entry.getKey(),
                            translate(industryId, localeId, entry.getValue()));
                }
            }
        });

        request.getUpdatedAttributes().entrySet().forEach(entry -> {
            final Attribute attribute = attributesById.get(entry.getKey());
            if (isUpdateRequiredForLocale(attribute, originatedLocaleId, localeId)) {
                translatedRequest.addUpdatedAttributes(entry.getKey(), entry.getValue());
                if (attribute.getIsTranslatable()
                        && isTranslationRequiredForLocale(localeId)) {
                    if (Type.UNIT != attribute.getType()) {
                        translatedRequest.addUpdatedAttributes(entry.getKey(),
                                translate(industryId, localeId, entry.getValue()));
                    } else {
                        translatedRequest.addUpdatedAttributes(entry.getKey(),
                                translateUnit(localeId, entry.getValue()));
                    }
                }
            }
        });
        return translatedRequest;
    }

    private Object translate(final String industryId, final String localeId,
            final Object inputValue) {
        final Object value = convertOperationToValue(inputValue);
        if (value instanceof String) {
            return new Value(translationService.translateText(industryId, localeId,
                    (String) value));
        } else if (value instanceof Set) {
            return ((Set<String>) value).stream().filter(v -> v instanceof String).map(
                    v -> new Value(translationService.translateText(industryId, localeId,
                            (String) v))).collect(Collectors.toSet());
        } else if (value instanceof List) {
            return ((List<String>) value).stream().filter(v -> v instanceof String).map(
                    v -> new Value(translationService.translateText(industryId, localeId,
                            (String) v))).collect(Collectors.toSet());
        }
        return inputValue;
    }

    private Object translateUnit(final String localeId, final Object inputValue) {
        if (inputValue instanceof UnitValue) {
            return translateUnitData(localeId, (UnitValue) inputValue);
        } else if (inputValue instanceof Set) {
            return ((Set) inputValue).stream().filter(v -> v instanceof UnitValue).map(
                    v -> translateUnitData(localeId, (UnitValue) v)).collect(
                            Collectors.toSet());
        } else if (inputValue instanceof List) {
            return ((List) inputValue).stream().filter(v -> v instanceof UnitValue).map(
                    v -> translateUnitData(localeId, (UnitValue) v)).collect(
                            Collectors.toSet());
        }
        return inputValue;
    }

    /**
     * It translate units. UnitAttribute and UnitValue are used in it.
     *
     * @param localeId locale id
     * @param unitValue UnitValue
     * @return unitValue
     */
    private UnitValue translateUnitData(final String localeId,
            final UnitValue unitValue) {
        final Map<String, UnitAttribute> translatedValue = Maps.newHashMap();
        unitValue.getValue().entrySet().forEach(entry -> {
            final String translation = translationService.translateUnit(localeId,
                    entry.getValue().getUnit());
            translatedValue.put(entry.getKey(),
                    new UnitAttribute(entry.getValue().getValue(), translation));
        });
        return new UnitValue(translatedValue);
    }

    /**
     * It translate raw data. SpecificationValue is not involved in these attribute value.
     *
     * @param industryId industry id
     * @param localeId locale id
     * @param attributes attributes
     * @return Map<String, Object>
     */
    private Map<String, Object> translateAttributes(final String industryId,
            final String localeId, final Map<String, Object> attributes) {
        final Map<String, Object> translatedAttribute = Maps.newHashMap(attributes);
        if (!localeId.equals(LOCALE_EN) && !localeId.equals(LOCALE_EN_US)) {
            translatedAttribute.entrySet().forEach(entry -> {
                final Attribute attribute = taxonomyService.findAttributeById(
                        entry.getKey());
                if (attribute.getIsTranslatable() && Type.NUMBER != attribute.getType()) {
                    if (Type.UNIT != attribute.getType()) {
                        if (entry.getValue() instanceof String) {
                            entry.setValue(translationService.translateText(industryId,
                                    localeId, (String) entry.getValue()));
                        } else if (entry.getValue() instanceof Set) {
                            final Set<String> translatedValues = ((Set<String>) entry.getValue()).stream().filter(
                                    v -> v instanceof String).map(
                                            v -> translationService.translateText(
                                                    industryId, localeId,
                                                    (String) v)).collect(
                                                            Collectors.toSet());
                            entry.setValue(translatedValues);
                        }
                    } else {
                        if (entry.getValue() instanceof Map) {
                            entry.setValue(translateUnitData(localeId,
                                    (Map<String, Map<String, Object>>) entry.getValue()));
                        } else if (entry.getValue() instanceof List) {
                            final List<Map> values = ((List<Map<String, Map<String, Object>>>) entry.getValue()).stream().map(
                                    val -> translateUnitData(localeId, val)).collect(
                                            Collectors.toList());
                            entry.setValue(values);
                        }
                    }
                }
            });
        }
        return translatedAttribute;
    }

    private Map<String, Attribute> getAttributesUsedInRequest(
            final UpdateProductSpecificationRequest request) {
        final Set<String> attributeIds = Sets.newHashSet(
                request.getAddedToSetAttributes().keySet());
        attributeIds.addAll(request.getRemovedAttributeIds());
        attributeIds.addAll(request.getRemovedFromSetAttributes().keySet());
        attributeIds.addAll(request.getUpdatedAttributes().keySet());
        final Map<String, Attribute> attributesById = attributeIds.stream().map(
                attributeId -> {
                    final Attribute attr = taxonomyService.findAttributeById(attributeId);
                    attr.setId(attributeId);
                    return attr;
                }).collect(Collectors.toMap(Attribute::getId, attr -> attr));
        return attributesById;
    }

    /**
     * It translate raw data for units. UnitAttribute and UnitValue are not involved in it.
     *
     * @param localeId locale id
     * @param value value
     * @return Map<String, Map<String, Object>>
     */
    private Map<String, Map<String, Object>> translateUnitData(final String localeId,
            final Map<String, Map<String, Object>> value) {
        final Map<String, Map<String, Object>> translatedValue = Maps.newHashMap();
        value.entrySet().forEach(entry -> {
            final Map<String, Object> detail = Maps.newHashMap(entry.getValue());
            translatedValue.put(entry.getKey(), detail);
            detail.put("unit", translationService.translateUnit(localeId,
                    (String) detail.get("unit")));
        });
        return translatedValue;
    }

    private Optional<ProductSpecification> getDetailedSpecification(final String productId,
            final String localeId) {
        return detailedSpecificationRepository.findOne(generateId(productId, localeId));
    }

    private Optional<ProductSpecification> getBasicSpecification(final String productId,
            final String localeId) {
        return basicSpecificationRepository.findOne(generateId(productId, localeId));
    }

    private ProductBasicSpecification getBasicSpecification(final ProductSpecification specs) {
        final ProductBasicSpecification basicSpecs = new ProductBasicSpecification();
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
