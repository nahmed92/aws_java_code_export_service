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

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.etilize.burraq.eas.annotation.ESTestExecutionListener;
import com.etilize.burraq.eas.annotation.LoadElasticBulkData;
import com.etilize.burraq.eas.attribute.Attribute;
import com.etilize.burraq.eas.attribute.Scope;
import com.etilize.burraq.eas.attribute.Type;
import com.etilize.burraq.eas.locale.LocaleService;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.common.collect.Lists;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.IgnorePropertyValue;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class,
    ESTestExecutionListener.class }, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public class CategoryAttributeUpdateServiceImplTest extends AbstractIntegrationTest {

    @Mock
    private LocaleService localeService;

    @Autowired
    private ElasticsearchRestTemplate operations;

    @Autowired
    private CategoryAccessorySpecificationRepository categoryAccessorySpecificationRepository;

    @Autowired
    private CategoryBasicSpecificationRepository categoryBasicSpecificationRepository;

    @Autowired
    private CategoryDetailedSpecificationRepository categoryDetailedSpecificationRepository;

    @Autowired
    private CategoryBasicMediaSpecificationRepository categoryBasicMediaSpecificationRepository;

    @Autowired
    private CategoryRichMediaSpecificationRepository categoryRichMediaSpecificationRepository;

    private CategoryAttributeUpdateService categoryAttributeUpdateService;

    @Override
    public void before() {
        categoryAttributeUpdateService = new CategoryAttributeUpdateServiceImpl(
                categoryAccessorySpecificationRepository,
                categoryBasicSpecificationRepository,
                categoryDetailedSpecificationRepository,
                categoryBasicMediaSpecificationRepository,
                categoryRichMediaSpecificationRepository, operations, localeService);
        when(localeService.findLocaleIdsForLanguage("ENCA")).thenReturn(
                Lists.newArrayList("en_US", "en_CA"));
        when(localeService.findLocaleIdsForLanguage("ENUS")).thenReturn(
                Lists.newArrayList("en_US", "en_CA"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_specification_attribute_update/category_accessory_attribute_after_translation_update.json")
    @IgnorePropertyValue(properties = { "lastUpdateDate", "_id" })
    @UsingDataSet(locations = "/datasets/accessory_category_structures/accessory_category_structures_for_translation.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @LoadElasticBulkData(jsonDataFile = "datasets/category_elasticsearch_index.json", jsonMappingFile = "datasets/category_elasticsearch_mapping.json")
    public void ShouldUpdateAttributeTranslation() throws IOException {
        categoryAttributeUpdateService.updateAttributeTranslation("industryId123",
                "Mfg Part No", "Updated Part Number", "ENCA");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_specification_attribute_update/accessory_categoryName_after_translation_update.json")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    @UsingDataSet(locations = "/datasets/accessory_category_structures/accessory_category_structures_for_translation.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @LoadElasticBulkData(jsonDataFile = "datasets/category_elasticsearch_index.json", jsonMappingFile = "datasets/category_elasticsearch_mapping.json")
    public void ShouldUpdatePropertyTranslation() throws IOException {
        categoryAttributeUpdateService.updateAttributeTranslation("industryId123",
                "NoteBook", "Notebook Updated name", "ENUS");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_specification_attribute_update/accessory_basic_detail_category_structures_for_translation_updated.json")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    @UsingDataSet(locations = "/datasets/accessory_category_structures/accessory_basic_detail_category_structures_for_translation.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @LoadElasticBulkData(jsonDataFile = "datasets/category_elasticsearch_index.json", jsonMappingFile = "datasets/category_elasticsearch_mapping.json")
    public void ShouldUpdateCategoryNameTranslation() throws IOException {
        categoryAttributeUpdateService.updateAttributeTranslation("industryId123",
                "NoteBook", "Notebook Updated name", "ENUS");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/accessory_category_structures/accessory_category_structures_for_translation.json")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    @UsingDataSet(locations = "/datasets/accessory_category_structures/accessory_category_structures_for_translation.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @LoadElasticBulkData(jsonDataFile = "datasets/category_elasticsearch_index.json", jsonMappingFile = "datasets/category_elasticsearch_mapping.json")
    public void ShouldNotTranslationWhenIndustryIIdNotMatch() throws IOException {
        categoryAttributeUpdateService.updateAttributeTranslation("industryId128",
                "NoteBook", "Notebook Updated name", "ENUS");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_specification_attribute_update/basic_specs_attribute_after_translation_update.json")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    @UsingDataSet(locations = "/datasets/category_specification_attribute_update/basic_specs_category_structures_for_translation.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @LoadElasticBulkData(jsonDataFile = "datasets/category_elasticsearch_index.json", jsonMappingFile = "datasets/category_elasticsearch_mapping.json")
    public void ShouldUpdateBasicAttributeTranslation() throws IOException {
        categoryAttributeUpdateService.updateAttributeTranslation("industryId123",
                "Mfg Part Number", "Updated Part Number", "ENCA");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_specification_attribute_update/basic_specs_categoryName_after_translation_update.json")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    @UsingDataSet(locations = "/datasets/category_specification_attribute_update/basic_specs_category_structures_for_translation.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @LoadElasticBulkData(jsonDataFile = "datasets/category_elasticsearch_index.json", jsonMappingFile = "datasets/category_elasticsearch_mapping.json")
    public void ShouldUpdateBasicSpecsPropertyTranslation() throws IOException {
        categoryAttributeUpdateService.updateAttributeTranslation("industryId123",
                "Baisc NoteBook", "LED TV", "ENUS");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_specification_attribute_update/accessory_basic_category_structures_updated_after_translation.json")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    @UsingDataSet(locations = "/datasets/accessory_category_structures/accessory_basic_detail_category_structures_for_translation.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @LoadElasticBulkData(jsonDataFile = "datasets/category_elasticsearch_index.json", jsonMappingFile = "datasets/category_elasticsearch_mapping.json")
    public void ShouldUpdateAccessoryWithBasicAndDetailedAttributeTranslation()
            throws IOException {
        categoryAttributeUpdateService.updateAttributeTranslation("industryId123",
                "shapeId", "shapeId Update", "ENCA");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_specification_attribute_update/category_with_rich_media_attribute_after_tanslation.json")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    @UsingDataSet(locations = "/datasets/category_specification_attribute_update/category_with_rich_media_attribute_for_tanslation.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @LoadElasticBulkData(jsonDataFile = "datasets/category_elasticsearch_index.json", jsonMappingFile = "datasets/category_elasticsearch_mapping.json")
    public void ShouldUpdateBasicAndRichAttributeTranslationWhenValueIsSameIdIsDifferernt()
            throws IOException {
        categoryAttributeUpdateService.updateAttributeTranslation("industryId123",
                "MediaShapeId", "shapeId Update", "ENCA");
    }

    private Attribute getAttribute(final String name, final String industryId,
            final Type type, final boolean isTranslatable, final boolean isRepeatable,
            final Scope scope) {
        final Attribute attr = new Attribute(name, "description", industryId, "helpText",
                type);
        attr.setIsTranslatable(isTranslatable);
        attr.setIsRepeatable(isRepeatable);
        attr.setScope(scope);
        return attr;
    }

}
