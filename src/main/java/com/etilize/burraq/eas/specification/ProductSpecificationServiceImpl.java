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
import com.etilize.burraq.eas.attribute.Scope;
import com.etilize.burraq.eas.attribute.Type;
import com.etilize.burraq.eas.category.specification.CategorySpecificationService;
import com.etilize.burraq.eas.locale.LocaleService;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatus;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatusRepository;
import com.etilize.burraq.eas.taxonomy.TaxonomyService;
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

    private final TaxonomyService taxonomyService;

    private final ProductSpecificationStatusRepository specsStatusRepository;

    private final CategorySpecificationService categoryStructureService;

    private final ProductAccessorySpecificationRepository accessorySpecificationRepository;

    private final ProductMetaDataRepository productMetaDataRepository;

    private final LocaleService localeService;

    /**
     * Constructs with dependencies
     *
     * @param basicSpecificationRepository basicSpecificationRepository
     * @param detailedSpecificationRepository detailedSpecificationRepository
     * @param taxonomyService taxonomyService
     * @param categoryStructureService categoryStructureService
     * @param specsStatusRepository {@link ProductSpecificationStatusRepository}
     * @param productMetaDataRepository {@link ProductMetaDataRepository}
     * @param localeService {@link LocaleService}
     */
    @Autowired
    public ProductSpecificationServiceImpl(
            final ProductBasicSpecificationRepository basicSpecificationRepository,
            final ProductDetailedSpecificationRepository detailedSpecificationRepository,
            final TaxonomyService taxonomyService,
            final CategorySpecificationService categoryStructureService,
            final ProductSpecificationStatusRepository specsStatusRepository,
            final ProductAccessorySpecificationRepository accessorySpecificationRepository,
            final ProductMetaDataRepository productMetaDataRepository,
            final LocaleService localeService) {
        Assert.notNull(basicSpecificationRepository,
                "basicSpecificationRepository should not be null.");
        Assert.notNull(detailedSpecificationRepository,
                "detailedSpecificationRepository should not be null.");
        Assert.notNull(taxonomyService, "taxonomyService should not be null.");
        Assert.notNull(categoryStructureService,
                "categoryStructureService should not be null.");
        Assert.notNull(specsStatusRepository,
                "specsStatusRepository should not be null.");
        Assert.notNull(productMetaDataRepository,
                "productMetaDataRepository should not be null.");
        Assert.notNull(localeService, "localeService should not be null.");
        this.basicSpecificationRepository = basicSpecificationRepository;
        this.detailedSpecificationRepository = detailedSpecificationRepository;
        this.taxonomyService = taxonomyService;
        this.categoryStructureService = categoryStructureService;
        this.specsStatusRepository = specsStatusRepository;
        this.accessorySpecificationRepository = accessorySpecificationRepository;
        this.productMetaDataRepository = productMetaDataRepository;
        this.localeService = localeService;
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
            accessorySpecificationRepository.save(
                    getAccessorySpecification(detialedSpecs));
            //Adding entry on productMetata table
            productMetaDataRepository.save(getProductMetaData(detialedSpecs));
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
                final Map<String, Object> inheretedAttributes = getInheretedAttributes(
                        productId, localeId, detailedSpecificationRepository,
                        detailedSpecsForEN.get().getAttributes());
                detailedSpecificationRepository.saveAttributes(id, inheretedAttributes);
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
                final Map<String, Object> inheretedAttributes = getInheretedAttributes(
                        productId, localeId, basicSpecificationRepository,
                        basicSpecsForEN.get().getAttributes());
                basicSpecificationRepository.saveAttributes(id, inheretedAttributes);
            }

            final Optional<ProductSpecification> accessorySpecsForEN = getAccessorySpecification(
                    productId, LOCALE_EN);
            if (accessorySpecsForEN.isPresent()) {
                final ProductAccessorySpecification accessorySpecs = new ProductAccessorySpecification();
                accessorySpecs.setId(id);
                accessorySpecs.setCategoryId(accessorySpecsForEN.get().getCategoryId());
                accessorySpecs.setIndustryId(accessorySpecsForEN.get().getIndustryId());
                accessorySpecs.setLocaleId(localeId);
                accessorySpecs.setProductId(productId);
                accessorySpecs.setLastUpdateDate(new Date());
                accessorySpecificationRepository.save(accessorySpecs);
                final Map<String, Object> inheretedAttributes = getInheretedAttributes(
                        productId, localeId, accessorySpecificationRepository,
                        accessorySpecsForEN.get().getAttributes());
                accessorySpecificationRepository.saveAttributes(id, inheretedAttributes);
            }

            //Adding entry on productMetata table
            final Optional<ProductMetaData> metadataForEN = getProductMetaData(productId,
                    LOCALE_EN);
            if (metadataForEN.isPresent()) {
                final ProductMetaData productMetaData = new ProductMetaData();
                productMetaData.setId(id);
                productMetaData.setCategoryId(metadataForEN.get().getCategoryId());
                productMetaData.setProductId(productId);
                productMetaData.setIndustryId(metadataForEN.get().getIndustryId());
                productMetaData.setLocaleId(localeId);
                productMetaData.setLastUpdateDate(new Date());
                productMetaDataRepository.save(productMetaData);
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
        final UpdateProductSpecificationRequest accessorySpecsRequest = filterRequestForAccessoryOffering(
                product.getCategoryId(), request);
        if (request.getLocaleId().startsWith(LOCALE_STARTS_WITH_EN)) {
            updateDataAcrossLocales(product.getIndustryId(), basicSpecsRequest,
                    detailedSpecsRequest, accessorySpecsRequest,
                    getAttributesUsedInRequest(request));
        } else {
            basicSpecificationRepository.saveAttributes(basicSpecsRequest);
            detailedSpecificationRepository.saveAttributes(detailedSpecsRequest);
            accessorySpecificationRepository.saveAttributes(accessorySpecsRequest);
        }
    }

    @Override
    public Optional<Product> findProductByProductId(final String productId) {
        return basicSpecificationRepository.findProductById(
                generateId(productId, LOCALE_EN));
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.eas.specification.ProductSpecificationService#updateProductCategory(java.lang.String, java.lang.String)
     */
    @Override
    public void updateProductCategory(final String productId, final String categoryId) {
        final List<ProductSpecificationStatus> specsStatuses = specsStatusRepository.findAllByProductId(
                productId);
        // Get Basic category Attribute
        final Map<String, String> basicOfferingAttribute = categoryStructureService.findBasicSpecsOfferingAttributes(
                categoryId);
        // Get detailed category Attribute
        final Map<String, String> detailedOfferingAttribute = categoryStructureService.findDetailedSpecsOfferingAttributes(
                categoryId);
        // Get Accessory category Attribute
        final Map<String, String> accessoryAttribute = categoryStructureService.findAccessorySpecsOfferingAttributes(
                categoryId);

        specsStatuses.forEach(specsStatus -> {
            final String id = generateId(productId, specsStatus.getLocaleId());
            final Optional<ProductSpecification> productBasicSpecs = basicSpecificationRepository.findOne(
                    id);
            if (productBasicSpecs.isPresent()) {
                final ProductBasicSpecification basicSpecs = (ProductBasicSpecification) productBasicSpecs.get();
                final Map<String, Object> commonAttribute = getCategoriesCommonAttribute(
                        basicSpecs.getAttributes(), basicOfferingAttribute);
                basicSpecs.setAttributes(Maps.newHashMap());
                basicSpecs.setCategoryId(categoryId);
                basicSpecs.setLastUpdateDate(new Date());
                basicSpecificationRepository.save(basicSpecs);
                basicSpecificationRepository.saveAttributes(id, commonAttribute);
            }

            final Optional<ProductSpecification> productDetailedSpecs = detailedSpecificationRepository.findOne(
                    id);
            if (productDetailedSpecs.isPresent()) {
                final ProductDetailedSpecification detailedSpecs = (ProductDetailedSpecification) productDetailedSpecs.get();
                final Map<String, Object> commonAttribute = getCategoriesCommonAttribute(
                        detailedSpecs.getAttributes(), detailedOfferingAttribute);
                detailedSpecs.setAttributes(Maps.newHashMap());
                detailedSpecs.setCategoryId(categoryId);
                detailedSpecs.setLastUpdateDate(new Date());
                detailedSpecificationRepository.save(detailedSpecs);
                detailedSpecificationRepository.saveAttributes(id, commonAttribute);
            }

            final Optional<ProductSpecification> productAcessorySpecs = accessorySpecificationRepository.findOne(
                    id);
            if (productAcessorySpecs.isPresent()) {
                final ProductAccessorySpecification acessorySpecs = (ProductAccessorySpecification) productAcessorySpecs.get();
                final Map<String, Object> commonAttribute = getCategoriesCommonAttribute(
                        acessorySpecs.getAttributes(), accessoryAttribute);
                acessorySpecs.setAttributes(Maps.newHashMap());
                acessorySpecs.setCategoryId(categoryId);
                acessorySpecs.setLastUpdateDate(new Date());
                accessorySpecificationRepository.save(acessorySpecs);
                accessorySpecificationRepository.saveAttributes(id, commonAttribute);
            }
            // update MetaData
            final Optional<ProductMetaData> productMetaData = productMetaDataRepository.findById(
                    id);
            if (productMetaData.isPresent()) {
               final ProductMetaData metaData = productMetaData.get();
                metaData.setCategoryId(categoryId);
                metaData.setLastUpdateDate(new Date());
                productMetaDataRepository.save(metaData);
            }
        });
    }

    private Map<String, Object> getCategoriesCommonAttribute(
            final Map<String, Object> specificationAttribute,
            final Map<String, String> offeringAttribute) {
        Map<String, Object> attributes = Maps.newHashMap();
        specificationAttribute.entrySet().stream().forEach(entry -> {
            if (offeringAttribute.get(entry.getKey()) != null) {
                attributes.put(entry.getKey(), entry.getValue());
            }

        });
        return attributes;
    }

    /**
     * This method return Map of InheretedAttributes
     * @param productId productId
     * @param localeId LocaleId
     * @param repository {@link ProductSpecificationCustomRepository}
     * @param attributes {@link Map<String,Object>}
     * @return {@link Map<String,Object>}
     */
    private Map<String, Object> getInheretedAttributes(final String productId,
            final String localeId, final ProductSpecificationCustomRepository repository,
            final Map<String, Object> internationalSpecs) {

        final Map<String, Object> inheretAttributes = Maps.newHashMap();
        internationalSpecs.entrySet().forEach(entry -> {
            final Attribute attribute = taxonomyService.findAttributeById(entry.getKey());
            // copy data from "en" for non translatable attributes
            if (!attribute.getIsTranslatable()) {
                inheretAttributes.put(entry.getKey(), entry.getValue());
            }
        });
        if (!localeId.startsWith(LOCALE_STARTS_WITH_EN)) {
            // if localeId does not startWith "en" then // Native locale is added
            // copy attributes of type Number from respective english locale // copy from enuk_FR to fr_FR
            final String respectiveEnLocale = localeService.getEnglishLocaleIdForMarket(
                    getMarketFromLocaleId(localeId));
            final String id = generateId(productId, respectiveEnLocale);
            final Optional<ProductSpecification> respectiveEnLocaleData = repository.findOne(
                    id);
            if (respectiveEnLocaleData.isPresent()) {

                respectiveEnLocaleData.get().getAttributes().entrySet().forEach(entry -> {
                    final Attribute attribute = taxonomyService.findAttributeById(
                            entry.getKey());
                    if (Scope.INTERNATIONAL != attribute.getScope()
                            && Type.NUMBER == attribute.getType()) {
                        inheretAttributes.put(entry.getKey(),
                                respectiveEnLocaleData.get().getAttributes().get(
                                        entry.getKey()));
                    }

                });

            }
        }

        return inheretAttributes;
    }

    private void updateDataAcrossLocales(final String industryId,
            final UpdateProductSpecificationRequest basicSpecsRequest,
            final UpdateProductSpecificationRequest detailedSpecsRequest,
            final UpdateProductSpecificationRequest accessorySpecsRequest,
            final Map<String, Attribute> attributesById) {
        final List<ProductSpecificationStatus> specsStatuses = specsStatusRepository.findAllByProductId(
                basicSpecsRequest.getProductId());
        final String originatedLocaleId = basicSpecsRequest.getLocaleId();
        final String originatedMarketId = "_"
                + StringUtils.substringAfterLast(originatedLocaleId, "_");
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
                final UpdateProductSpecificationRequest localizedAccessoryRequest = translateRequest(
                        originatedLocaleId, industryId, specsStatus.getLocaleId(),
                        accessorySpecsRequest, attributesById);
                basicSpecificationRepository.saveAttributes(localizedBasicRequest);
                detailedSpecificationRepository.saveAttributes(localizedDetailedRequest);
                accessorySpecificationRepository.saveAttributes(
                        localizedAccessoryRequest);
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

    private UpdateProductSpecificationRequest filterRequestForAccessoryOffering(
            final String categoryId, final UpdateProductSpecificationRequest request) {
        final Map<String, String> attributes = categoryStructureService.findAccessorySpecsOfferingAttributes(
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

    private boolean isUpdateRequiredForLocale(final Attribute attribute,
            final String originatedLocaleId, final String localeId) {
        return originatedLocaleId.equals(localeId) || // save data in its own locale, en enuk_UK or en_US
                (originatedLocaleId.equals(LOCALE_EN) && !attribute.getIsTranslatable()
                        && attribute.getScope() == Scope.INTERNATIONAL)
                || // copy international, non translatable data to all locales
                (!originatedLocaleId.equals(LOCALE_EN)
                        && originatedLocaleId.startsWith(LOCALE_STARTS_WITH_EN)
                        && !localeId.startsWith(LOCALE_STARTS_WITH_EN)
                        && attribute.getType() == Type.NUMBER
                        && attribute.getScope() != Scope.INTERNATIONAL); // save data in native language for number type for non international attrbibute
    }

    private UpdateProductSpecificationRequest translateRequest(
            final String originatedLocaleId, final String industryId,
            final String localeId, final UpdateProductSpecificationRequest request,
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
            }
        });

        request.getRemovedFromSetAttributes().entrySet().forEach(entry -> {
            final Attribute attribute = attributesById.get(entry.getKey());
            if (isUpdateRequiredForLocale(attribute, originatedLocaleId, localeId)) {
                translatedRequest.addRemovedFromSetAttributes(entry.getKey(),
                        entry.getValue());
            }
        });

        request.getUpdatedAttributes().entrySet().forEach(entry -> {
            final Attribute attribute = attributesById.get(entry.getKey());
            if (isUpdateRequiredForLocale(attribute, originatedLocaleId, localeId)) {
                translatedRequest.addUpdatedAttributes(entry.getKey(), entry.getValue());
            }
        });
        return translatedRequest;
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

    private Optional<ProductSpecification> getDetailedSpecification(
            final String productId, final String localeId) {
        return detailedSpecificationRepository.findOne(generateId(productId, localeId));
    }

    private Optional<ProductSpecification> getBasicSpecification(final String productId,
            final String localeId) {
        return basicSpecificationRepository.findOne(generateId(productId, localeId));
    }

    private Optional<ProductSpecification> getAccessorySpecification(
            final String productId, final String localeId) {
        return accessorySpecificationRepository.findOne(generateId(productId, localeId));
    }

    private ProductBasicSpecification getBasicSpecification(
            final ProductSpecification specs) {
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

    private ProductAccessorySpecification getAccessorySpecification(
            final ProductSpecification specs) {
        final ProductAccessorySpecification accessorySpecs = new ProductAccessorySpecification();
        accessorySpecs.setId(specs.getId());
        accessorySpecs.setAttributes(specs.getAttributes());
        accessorySpecs.setCategoryId(specs.getCategoryId());
        accessorySpecs.setIndustryId(specs.getIndustryId());
        accessorySpecs.setLocaleId(specs.getLocaleId());
        accessorySpecs.setProductId(specs.getProductId());
        accessorySpecs.setLastUpdateDate(specs.getLastUpdateDate());
        return accessorySpecs;
    }

    private ProductMetaData getProductMetaData(final ProductSpecification specs) {
        final ProductMetaData productMetaData = new ProductMetaData();
        productMetaData.setId(specs.getId());
        productMetaData.setCategoryId(specs.getCategoryId());
        productMetaData.setIndustryId(specs.getIndustryId());
        productMetaData.setLocaleId(specs.getLocaleId());
        productMetaData.setProductId(specs.getProductId());
        productMetaData.setLastUpdateDate(specs.getLastUpdateDate());
        return productMetaData;
    }

    private Optional<ProductMetaData> getProductMetaData(final String productId,
            final String localeId) {
        return productMetaDataRepository.findById(generateId(productId, localeId));
    }
}
