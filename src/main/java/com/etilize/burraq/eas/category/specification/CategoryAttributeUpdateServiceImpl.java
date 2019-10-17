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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.etilize.burraq.eas.locale.LocaleService;
import com.google.common.collect.Lists;

/**
 * Service to update Category attribute translation once translation message received
 *
 * @author Nasir Ahmed
 *
 */
@Service
public class CategoryAttributeUpdateServiceImpl
        implements CategoryAttributeUpdateService {

    /**
     * Log variable for all child classes. Uses LoggerFactory.getLogger(getClass()) from
     * slf4j Logging
     */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String FIELD_CATEGORY_ID = "categoryId";

    private static final String FIELD_INDUSTRY_ID = "industryId";

    private static final String ACCESSORY_SPEFICIATION_OFFERING = "accessorySpecificationAttributes";

    private static final String BASIC_SPEFICIATION_OFFERING = "basicSpecificationAttributes";

    private static final String DETAILED_SPEFICIATION_OFFERING = "detailedSpecificationAttributes";

    private static final String BASIC_MEDIA_SPEFICIATION_OFFERING = "basicMediaAttributes";

    private static final String RICH_MEDIA_SPEFICIATION_OFFERING = "richMediaAttributes";

    private static final String ALL_OFFERING = "all_offering";

    private static final String ELASTIC_INDEX = "category-index";

    private final CategoryAccessorySpecificationRepository categoryAccessorySpecificationRepository;

    private final CategoryBasicSpecificationRepository categoryBasicSpecificationRepository;

    private final CategoryDetailedSpecificationRepository categoryDetailedSpecificationRepository;

    private final CategoryBasicMediaSpecificationRepository categoryBasicMediaSpecificationRepository;

    private final CategoryRichMediaSpecificationRepository categoryRichMediaSpecificationRepository;

    private final ElasticsearchRestTemplate operations;

    private final LocaleService localeService;

    /**
     * Constructor {@link CategoryAttributeUpdateServiceImpl}
     *
     * @param categoryAccessorySpecificationRepository
     *        {@link CategoryAccessorySpecificationRepository}
     * @param categoryBasicSpecificationRepository
     *        {@link CategoryBasicSpecificationRepository}
     * @param categoryDetailedSpecificationRepository
     *        {@link CategoryDetailedSpecificationRepositoryv}
     * @param categoryBasicMediaSpecificationRepository
     *        {@link CategoryBasicMediaSpecificationRepository}
     * @param categoryRichMediaSpecificationRepository
     *        {@link CategoryRichMediaSpecificationRepository}
     * @param operations {@link ElasticsearchRestTemplate}
     * @param localeService {@link LocaleService}
     */
    @Autowired
    public CategoryAttributeUpdateServiceImpl(
            final CategoryAccessorySpecificationRepository categoryAccessorySpecificationRepository,
            final CategoryBasicSpecificationRepository categoryBasicSpecificationRepository,
            final CategoryDetailedSpecificationRepository categoryDetailedSpecificationRepository,
            final CategoryBasicMediaSpecificationRepository categoryBasicMediaSpecificationRepository,
            final CategoryRichMediaSpecificationRepository categoryRichMediaSpecificationRepository,
            final ElasticsearchRestTemplate operations,
            final LocaleService localeService) {
        Assert.notNull(categoryAccessorySpecificationRepository,
                "CategoryAccessorySpecificationRepository is required.");
        Assert.notNull(categoryBasicSpecificationRepository,
                "CategoryBasicSpecificationRepository is required.");
        Assert.notNull(categoryDetailedSpecificationRepository,
                "CategoryDetailedSpecificationRepository is required.");
        Assert.notNull(categoryBasicMediaSpecificationRepository,
                "categoryBasicMediaSpecificationRepository is required.");
        Assert.notNull(categoryRichMediaSpecificationRepository,
                "CategoryRichMediaSpecificationRepository is required.");
        Assert.notNull(operations, "ElasticsearchRestTemplate is required.");
        Assert.notNull(localeService, "LocaleService is required.");
        this.categoryAccessorySpecificationRepository = categoryAccessorySpecificationRepository;
        this.categoryBasicSpecificationRepository = categoryBasicSpecificationRepository;
        this.categoryDetailedSpecificationRepository = categoryDetailedSpecificationRepository;
        this.categoryBasicMediaSpecificationRepository = categoryBasicMediaSpecificationRepository;
        this.categoryRichMediaSpecificationRepository = categoryRichMediaSpecificationRepository;
        this.operations = operations;
        this.localeService = localeService;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.etilize.burraq.eas.elasticsearch.ElasticSearchService#
     * updateAttributeTranslation(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public void updateAttributeTranslation(final String industryId,
            final String sourceValue, final String translatedValue,
            final String languageId) {
        Assert.notNull(industryId, "industryId is required.");
        Assert.notNull(sourceValue, "sourceValue is required.");
        Assert.notNull(translatedValue, "translatedValue is required.");
        Assert.notNull(languageId, "languageId is required.");

        findCategoriesByValue(industryId, sourceValue).stream().forEach(category -> {
            // get all Locale by languageId
            final List<String> locales = localeService.findLocaleIdsForLanguage(
                    languageId);

            if (locales.isEmpty()) {
                logger.info("No locales found against languageId {}", languageId);
            }

            // create attribute to update
            if (category.getOffering().equals(ACCESSORY_SPEFICIATION_OFFERING)
                    || category.getOffering().equals(ALL_OFFERING)) {
                locales.stream().forEach(localeId -> {
                    categoryAccessorySpecificationRepository.updateCategoryAttribute(
                            category.getCategoryId(), localeId, category.isAttribute(),
                            category.getFieldName(), translatedValue);
                });
            }
            if (category.getOffering().equals(BASIC_SPEFICIATION_OFFERING)
                    || category.getOffering().equals(ALL_OFFERING)) {
                locales.stream().forEach(localeId -> {
                    categoryBasicSpecificationRepository.updateCategoryAttribute(
                            category.getCategoryId(), localeId, category.isAttribute(),
                            category.getFieldName(), translatedValue);
                });
            }
            if (category.getOffering().equals(DETAILED_SPEFICIATION_OFFERING)
                    || category.getOffering().equals(ALL_OFFERING)) {
                locales.stream().forEach(localeId -> {
                    categoryDetailedSpecificationRepository.updateCategoryAttribute(
                            category.getCategoryId(), localeId, category.isAttribute(),
                            category.getFieldName(), translatedValue);
                });
            }
            if (category.getOffering().equals(BASIC_MEDIA_SPEFICIATION_OFFERING)
                    || category.getOffering().equals(ALL_OFFERING)) {
                locales.stream().forEach(localeId -> {
                    categoryBasicMediaSpecificationRepository.updateCategoryAttribute(
                            category.getCategoryId(), localeId, category.isAttribute(),
                            category.getFieldName(), translatedValue);
                });
            }
            if (category.getOffering().equals(RICH_MEDIA_SPEFICIATION_OFFERING)
                    || category.getOffering().equals(ALL_OFFERING)) {
                locales.stream().forEach(localeId -> {
                    categoryRichMediaSpecificationRepository.updateCategoryAttribute(
                            category.getCategoryId(), localeId, category.isAttribute(),
                            category.getFieldName(), translatedValue);
                });
            }
            logger.info("Translation updated for attribute {}", category.getFieldName());
        });
    }

    /**
     * find categories of given source value in Elastic category Index
     *
     * @param industryId {@link String industry Id}
     * @param sourceValue {@link String source value}
     * @return {@link list of categories}
     * @throws IOException
     */
    private List<CategoryTranslationAttribute> findCategoriesByValue(
            final String industryId, final String sourceValue) {
        final QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(
                sourceValue).useAllFields(true);
        final SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilder);
        sourceBuilder.postFilter(QueryBuilders.matchQuery(FIELD_INDUSTRY_ID, industryId));
        final SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(ELASTIC_INDEX);
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse;
        try {
            searchResponse = operations.getClient().search(searchRequest,
                    RequestOptions.DEFAULT);
        } catch (final IOException e) {
            throw new RuntimeException(
                    "Exception while updating translation [" + e.getMessage() + "]");
        }

        final List<CategoryTranslationAttribute> categoryTranslationAttributes = Lists.newArrayList();
        CategoryTranslationAttribute categoryTranslationAttribute;
        for (final SearchHit hit : searchResponse.getHits().getHits()) {
            final String categoryId = (String) hit.getSourceAsMap().get(FIELD_CATEGORY_ID);
            for (final Map.Entry<String, Object> entry : hit.getSourceAsMap().entrySet()) {
                // check in Attribute Offering map
                if (entry.getValue() instanceof Map) {
                    final Map<String, String> attributeMap = (Map<String, String>) entry.getValue();
                    for (final Map.Entry<String, String> attributeEntry : attributeMap.entrySet()) {
                        if (attributeEntry.getValue().equals(sourceValue)) {
                            categoryTranslationAttribute = new CategoryTranslationAttribute();
                            categoryTranslationAttribute.setFieldName(
                                    attributeEntry.getKey());
                            categoryTranslationAttribute.setAttribute(true);
                            // set Attribute Offering
                            categoryTranslationAttribute.setOffering(entry.getKey());
                            categoryTranslationAttribute.setCategoryId(categoryId);
                            categoryTranslationAttributes.add(
                                    categoryTranslationAttribute);
                        }
                    }
                } else if (entry.getValue().equals(sourceValue)) {
                    categoryTranslationAttribute = new CategoryTranslationAttribute();
                    categoryTranslationAttribute.setOffering(ALL_OFFERING);
                    categoryTranslationAttribute.setFieldName(entry.getKey());
                    categoryTranslationAttribute.setCategoryId(categoryId);
                    categoryTranslationAttributes.add(categoryTranslationAttribute);
                }

            }
        }
        if (categoryTranslationAttributes.isEmpty()) {
            logger.info("No category found against source value {}", sourceValue);
        }
        return categoryTranslationAttributes;
    }
}
