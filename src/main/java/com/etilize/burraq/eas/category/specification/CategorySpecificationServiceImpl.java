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

import static com.etilize.burraq.eas.ExportAggregationConstants.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.etilize.burraq.eas.category.Category;
import com.etilize.burraq.eas.locale.LocaleService;
import com.etilize.burraq.eas.taxonomy.TaxonomyService;
import com.etilize.burraq.eas.translation.TranslationService;
import com.google.common.collect.Maps;

/**
 * It implements {@link CategorySpecificationService}
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Service
public class CategorySpecificationServiceImpl implements CategorySpecificationService {

    private static final String CATGEORY_ID_IS_REQUIRED = "categoryId is required";

    private static final String OFFERING_ID_IS_REQUIRED = "offeringId is required";

    private static final String ATTRIBUTE_IDS_IS_REQUIRED = "attributeIds is required";

    private final CategoryBasicSpecificationRepository basicCategoryStructureRepository;

    private final CategoryDetailedSpecificationRepository detailedCategoryStructureRepository;

    private final CategoryBasicMediaSpecificationRepository basicMediaCategoryStructureRepository;

    private final CategoryRichMediaSpecificationRepository richMediaCategoryStructureRepository;

    private final TranslationService translationService;

    private final TaxonomyService taxonomyService;

    private final LocaleService localeService;

    /**
     * Constructs with dependencies
     *
     * @param basicCategoryStructureRepository basicCategoryStructureRepository
     * @param detailedCategoryStructureRepository detailedCategoryStructureRepository
     * @param basicMediaCategoryStructureRepository basicMediaCategoryStructureRepository
     * @param richMediaCategoryStructureRepository richMediaCategoryStructureRepository
     * @param translationService translationService
     * @param taxonomyService taxonomyService
     * @param localeService localeService
     */
    @Autowired
    public CategorySpecificationServiceImpl(
            final CategoryBasicSpecificationRepository basicCategoryStructureRepository,
            final CategoryDetailedSpecificationRepository detailedCategoryStructureRepository,
            final CategoryBasicMediaSpecificationRepository basicMediaCategoryStructureRepository,
            final CategoryRichMediaSpecificationRepository richMediaCategoryStructureRepository,
            final TranslationService translationService,
            final TaxonomyService taxonomyService, final LocaleService localeService) {
        Assert.notNull(basicCategoryStructureRepository,
                "basicCategoryStructureRepository should not be null.");
        Assert.notNull(detailedCategoryStructureRepository,
                "detailedCategoryStructureRepository should not be null.");
        Assert.notNull(basicMediaCategoryStructureRepository,
                "basicMediaCategoryStructureRepository should not be null.");
        Assert.notNull(richMediaCategoryStructureRepository,
                "richMediaCategoryStructureRepository should not be null.");
        Assert.notNull(translationService, "translationService should not be null.");
        Assert.notNull(taxonomyService, "taxonomyService should not be null.");
        Assert.notNull(localeService, "localeService should not be null.");
        this.basicCategoryStructureRepository = basicCategoryStructureRepository;
        this.detailedCategoryStructureRepository = detailedCategoryStructureRepository;
        this.basicMediaCategoryStructureRepository = basicMediaCategoryStructureRepository;
        this.richMediaCategoryStructureRepository = richMediaCategoryStructureRepository;
        this.translationService = translationService;
        this.taxonomyService = taxonomyService;
        this.localeService = localeService;
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.eas.category.CategoryStructureService#save(java.lang.String, java.lang.String, java.util.Set)
     */
    @Override
    public void save(final String categoryId, final String offeringId,
            final Set<String> attributeIds) {
        Assert.hasText(categoryId, CATGEORY_ID_IS_REQUIRED);
        Assert.hasText(offeringId, OFFERING_ID_IS_REQUIRED);
        Assert.notEmpty(attributeIds, ATTRIBUTE_IDS_IS_REQUIRED);
        final List<String> localeIds = localeService.findAllLocaleIds();
        localeIds.remove(LOCALE_EN_US);
        localeIds.remove(LOCALE_EN);
        final Category category = taxonomyService.findCategoryById(categoryId);
        final CategoryBasicSpecification categoryStructure = new CategoryBasicSpecification();
        categoryStructure.setCategoryId(categoryId);
        categoryStructure.setCategoryName(category.getName());
        categoryStructure.setLocaleId(LOCALE_EN_US);
        categoryStructure.setParentCategoryId(category.getParentCategoryId());
        categoryStructure.setIndustryId(category.getIndustryId());
        categoryStructure.setIndustryName(
                taxonomyService.findIndustryById(category.getIndustryId()).getName());
        categoryStructure.setLastUpdateDate(new Date());
        switch (offeringId) {
            case OFFERING_BASIC_SPECS:
                categoryStructure.setAttributes(findAttributeNames(attributeIds));
                saveBasicCategoryStructure(categoryStructure, localeIds);
                break;
            case OFFERING_DETAILED_SPECS:
                categoryStructure.setAttributes(findAttributeNames(attributeIds));
                saveDetailedCategoryStructure(categoryStructure, localeIds);
                break;
            case OFFERING_BASIC_MEDIA:
                categoryStructure.setAttributes(findMediaAttributeNames(attributeIds));
                saveBasicMediaCategoryStructure(categoryStructure, localeIds);
                break;
            case OFFERING_RICH_MEDIA:
                categoryStructure.setAttributes(findMediaAttributeNames(attributeIds));
                saveRichMediaCategoryStructure(categoryStructure, localeIds);
                break;
            default:
                break;
        }
    }

    @Override
    public Map<String, String> findBasicSpecsOfferingAttributes(final String categoryId) {
        Map<String, String> attributes = Maps.newHashMap();
        final CategorySpecificationKey key = new CategorySpecificationKey();
        key.setCategoryId(categoryId);
        key.setLocaleId(LOCALE_EN_US);
        final Optional<CategoryBasicSpecification> category = basicCategoryStructureRepository.findById(
                key);
        if (category.isPresent()) {
            attributes = category.get().getAttributes();
        }
        return attributes;
    }

    @Override
    public Map<String, String> findDetailedSpecsOfferingAttributes(
            final String categoryId) {
        Map<String, String> attributes = Maps.newHashMap();
        final CategorySpecificationKey key = new CategorySpecificationKey();
        key.setCategoryId(categoryId);
        key.setLocaleId(LOCALE_EN_US);
        final Optional<CategoryDetailedSpecification> category = detailedCategoryStructureRepository.findById(
                key);
        if (category.isPresent()) {
            attributes = category.get().getAttributes();
        }
        return attributes;
    }

    @Override
    public boolean hasBasicMediaOfferingAttribute(final String categoryId,
            final String attributeId) {
        Map<String, String> attributes = Maps.newHashMap();
        final CategorySpecificationKey key = new CategorySpecificationKey();
        key.setCategoryId(categoryId);
        key.setLocaleId(LOCALE_EN_US);
        final Optional<CategoryBasicMediaSpecification> category = basicMediaCategoryStructureRepository.findById(
                key);
        Boolean exists = Boolean.TRUE;
        if (category.isPresent()) {
            attributes = category.get().getAttributes();
            if (!attributes.isEmpty() && !attributes.containsKey(attributeId)) {
                exists = Boolean.FALSE;
            }
        }
        return exists;
    }

    @Override
    public boolean hasRichMediaOfferingAttribute(final String categoryId,
            final String attributeId) {
        Map<String, String> attributes = Maps.newHashMap();
        final CategorySpecificationKey key = new CategorySpecificationKey();
        key.setCategoryId(categoryId);
        key.setLocaleId(LOCALE_EN_US);
        final Optional<CategoryRichMediaSpecification> category = richMediaCategoryStructureRepository.findById(
                key);
        Boolean exists = Boolean.TRUE;
        if (category.isPresent()) {
            attributes = category.get().getAttributes();
            if (!attributes.isEmpty() && !attributes.containsKey(attributeId)) {
                exists = Boolean.FALSE;
            }
        }
        return exists;
    }

    private void saveBasicCategoryStructure(final CategorySpecification categoryStructure,
            final List<String> localeIds) {
        final CategoryBasicSpecification basicCategoryStructure = new CategoryBasicSpecification();
        basicCategoryStructure.setCategoryId(categoryStructure.getCategoryId());
        basicCategoryStructure.setCategoryName(categoryStructure.getCategoryName());
        basicCategoryStructure.setLocaleId(categoryStructure.getLocaleId());
        basicCategoryStructure.setParentCategoryId(
                categoryStructure.getParentCategoryId());
        basicCategoryStructure.setIndustryId(categoryStructure.getIndustryId());
        basicCategoryStructure.setIndustryName(categoryStructure.getIndustryName());
        basicCategoryStructure.setAttributes(categoryStructure.getAttributes());
        basicCategoryStructure.setLastUpdateDate(categoryStructure.getLastUpdateDate());
        basicCategoryStructureRepository.save(basicCategoryStructure);
        localeIds.forEach(localeId -> {
            final CategoryBasicSpecification localizedCategoryStructure = new CategoryBasicSpecification();
            localizedCategoryStructure.setCategoryId(categoryStructure.getCategoryId());
            localizedCategoryStructure.setCategoryName(
                    translationService.translateText(categoryStructure.getIndustryId(),
                            localeId, categoryStructure.getCategoryName()));
            localizedCategoryStructure.setLocaleId(localeId);
            localizedCategoryStructure.setParentCategoryId(
                    categoryStructure.getParentCategoryId());
            localizedCategoryStructure.setIndustryId(categoryStructure.getIndustryId());
            localizedCategoryStructure.setIndustryName(
                    translationService.translateText(categoryStructure.getIndustryId(),
                            localeId, categoryStructure.getIndustryName()));
            localizedCategoryStructure.setAttributes(
                    translateAttributes(categoryStructure.getIndustryId(), localeId,
                            categoryStructure.getAttributes()));
            basicCategoryStructureRepository.save(localizedCategoryStructure);
        });
    }

    private void saveDetailedCategoryStructure(final CategorySpecification categoryStructure,
            final List<String> localeIds) {
        final CategoryDetailedSpecification detailedCategoryStructure = new CategoryDetailedSpecification();
        detailedCategoryStructure.setCategoryId(categoryStructure.getCategoryId());
        detailedCategoryStructure.setCategoryName(categoryStructure.getCategoryName());
        detailedCategoryStructure.setLocaleId(categoryStructure.getLocaleId());
        detailedCategoryStructure.setParentCategoryId(
                categoryStructure.getParentCategoryId());
        detailedCategoryStructure.setIndustryId(categoryStructure.getIndustryId());
        detailedCategoryStructure.setIndustryName(categoryStructure.getIndustryName());
        detailedCategoryStructure.setAttributes(categoryStructure.getAttributes());
        detailedCategoryStructureRepository.save(detailedCategoryStructure);
        localeIds.forEach(localeId -> {
            final CategoryDetailedSpecification localizedCategoryStructure = new CategoryDetailedSpecification();
            localizedCategoryStructure.setCategoryId(categoryStructure.getCategoryId());
            localizedCategoryStructure.setCategoryName(
                    translationService.translateText(categoryStructure.getIndustryId(),
                            localeId, categoryStructure.getCategoryName()));
            localizedCategoryStructure.setLocaleId(localeId);
            localizedCategoryStructure.setParentCategoryId(
                    categoryStructure.getParentCategoryId());
            localizedCategoryStructure.setIndustryId(categoryStructure.getIndustryId());
            localizedCategoryStructure.setIndustryName(
                    translationService.translateText(categoryStructure.getIndustryId(),
                            localeId, categoryStructure.getIndustryName()));
            localizedCategoryStructure.setAttributes(
                    translateAttributes(categoryStructure.getIndustryId(), localeId,
                            categoryStructure.getAttributes()));
            detailedCategoryStructureRepository.save(localizedCategoryStructure);
        });
    }

    private void saveBasicMediaCategoryStructure(
            final CategorySpecification categoryStructure, final List<String> localeIds) {
        final CategoryBasicMediaSpecification basicMediaCategoryStructure = new CategoryBasicMediaSpecification();
        basicMediaCategoryStructure.setCategoryId(categoryStructure.getCategoryId());
        basicMediaCategoryStructure.setCategoryName(categoryStructure.getCategoryName());
        basicMediaCategoryStructure.setLocaleId(categoryStructure.getLocaleId());
        basicMediaCategoryStructure.setParentCategoryId(
                categoryStructure.getParentCategoryId());
        basicMediaCategoryStructure.setIndustryId(categoryStructure.getIndustryId());
        basicMediaCategoryStructure.setIndustryName(categoryStructure.getIndustryName());
        basicMediaCategoryStructure.setAttributes(categoryStructure.getAttributes());
        basicMediaCategoryStructureRepository.save(basicMediaCategoryStructure);
        localeIds.forEach(localeId -> {
            final CategoryBasicMediaSpecification localizedCategoryStructure = new CategoryBasicMediaSpecification();
            localizedCategoryStructure.setCategoryId(categoryStructure.getCategoryId());
            localizedCategoryStructure.setCategoryName(
                    translationService.translateText(categoryStructure.getIndustryId(),
                            localeId, categoryStructure.getCategoryName()));
            localizedCategoryStructure.setLocaleId(localeId);
            localizedCategoryStructure.setParentCategoryId(
                    categoryStructure.getParentCategoryId());
            localizedCategoryStructure.setIndustryId(categoryStructure.getIndustryId());
            localizedCategoryStructure.setIndustryName(
                    translationService.translateText(categoryStructure.getIndustryId(),
                            localeId, categoryStructure.getIndustryName()));
            localizedCategoryStructure.setAttributes(
                    translateAttributes(categoryStructure.getIndustryId(), localeId,
                            categoryStructure.getAttributes()));
            basicMediaCategoryStructureRepository.save(localizedCategoryStructure);
        });
    }

    private void saveRichMediaCategoryStructure(final CategorySpecification categoryStructure,
            final List<String> localeIds) {
        final CategoryRichMediaSpecification richMediaCategoryStructure = new CategoryRichMediaSpecification();
        richMediaCategoryStructure.setCategoryId(categoryStructure.getCategoryId());
        richMediaCategoryStructure.setCategoryName(categoryStructure.getCategoryName());
        richMediaCategoryStructure.setLocaleId(categoryStructure.getLocaleId());
        richMediaCategoryStructure.setParentCategoryId(
                categoryStructure.getParentCategoryId());
        richMediaCategoryStructure.setIndustryId(categoryStructure.getIndustryId());
        richMediaCategoryStructure.setIndustryName(categoryStructure.getIndustryName());
        richMediaCategoryStructure.setAttributes(categoryStructure.getAttributes());
        richMediaCategoryStructureRepository.save(richMediaCategoryStructure);
        localeIds.forEach(localeId -> {
            final CategoryRichMediaSpecification localizedCategoryStructure = new CategoryRichMediaSpecification();
            localizedCategoryStructure.setCategoryId(categoryStructure.getCategoryId());
            localizedCategoryStructure.setCategoryName(
                    translationService.translateText(categoryStructure.getIndustryId(),
                            localeId, categoryStructure.getCategoryName()));
            localizedCategoryStructure.setLocaleId(localeId);
            localizedCategoryStructure.setParentCategoryId(
                    categoryStructure.getParentCategoryId());
            localizedCategoryStructure.setIndustryId(categoryStructure.getIndustryId());
            localizedCategoryStructure.setIndustryName(
                    translationService.translateText(categoryStructure.getIndustryId(),
                            localeId, categoryStructure.getIndustryName()));
            localizedCategoryStructure.setAttributes(
                    translateAttributes(categoryStructure.getIndustryId(), localeId,
                            categoryStructure.getAttributes()));
            richMediaCategoryStructureRepository.save(localizedCategoryStructure);
        });
    }

    private Map<String, String> findAttributeNames(final Set<String> attributeIds) {
        return attributeIds.stream().collect(Collectors.toMap(attrId -> attrId,
                attrId -> taxonomyService.findAttributeById(attrId).getName()));
    }

    private Map<String, String> findMediaAttributeNames(final Set<String> attributeIds) {
        return attributeIds.stream().collect(Collectors.toMap(attrId -> attrId,
                attrId -> taxonomyService.findMediaAttributeById(attrId).getName()));
    }

    private Map<String, String> translateAttributes(final String industryId,
            final String localeId, final Map<String, String> attributes) {
        return attributes.entrySet().stream().collect(
                Collectors.toMap(entry -> entry.getKey(),
                        entry -> translationService.translateText(industryId, localeId,
                                entry.getValue())));

    }
}
