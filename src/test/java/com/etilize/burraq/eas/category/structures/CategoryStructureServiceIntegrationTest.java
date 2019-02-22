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

package com.etilize.burraq.eas.category.structures;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.eas.attribute.Attribute;
import com.etilize.burraq.eas.attribute.Type;
import com.etilize.burraq.eas.category.Category;
import com.etilize.burraq.eas.category.Status;
import com.etilize.burraq.eas.industry.Industry;
import com.etilize.burraq.eas.locale.LocaleService;
import com.etilize.burraq.eas.media.attribute.MediaAttribute;
import com.etilize.burraq.eas.taxonomy.TaxonomyService;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.etilize.burraq.eas.translation.TranslationService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.IgnorePropertyValue;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

/**
 * This class implements repository test cases for {@link CategoryStructureService}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/category_structures/category_structures.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class CategoryStructureServiceIntegrationTest extends AbstractIntegrationTest {

    @Mock
    private TranslationService translationService;

    @Mock
    private TaxonomyService taxonomyService;

    @Mock
    private LocaleService localeService;

    @Autowired
    private BasicCategoryStructureRepository basicCategoryStructureRepository;

    @Autowired
    private DetailedCategoryStructureRepository detailedCategoryStructureRepository;

    @Autowired
    private BasicMediaCategoryStructureRepository basicMediaCategoryStructureRepository;

    @Autowired
    private RichMediaCategoryStructureRepository richMediaCategoryStructureRepository;

    private CategoryStructureService service;

    @Override
    public void before() {
        service = new CategoryStructureServiceImpl(basicCategoryStructureRepository,
                detailedCategoryStructureRepository,
                basicMediaCategoryStructureRepository,
                richMediaCategoryStructureRepository, translationService, taxonomyService,
                localeService);
        when(localeService.findAllLocaleIds()) //
                .thenReturn(Lists.newArrayList("en_US", "enuk_UK", "en"));
        when(taxonomyService.findIndustryById("industryId123")) //
                .thenReturn(new Industry("industryId123", "IT", ""));
        when(taxonomyService.findCategoryById("categoryId123")) //
                .thenReturn(new Category("Notebook", "industryId123", Status.ACTIVE, "",
                        "parentCategoryId123", null, null, null));
        when(taxonomyService.findAttributeById("mfgPartNoId")) //
                .thenReturn(new Attribute("Mfg Part No", "description", "industryId123",
                        "helpText", Type.TEXT));
        when(taxonomyService.findAttributeById("colorId")) //
                .thenReturn(new Attribute("Color", "description", "industryId123",
                        "helpText", Type.TEXT));
        when(taxonomyService.findMediaAttributeById("maxId")) //
                .thenReturn(new MediaAttribute("Max", "description", null,
                        "industryId123", "helpText",
                        com.etilize.burraq.eas.media.attribute.Type.IMAGE));
        when(translationService.translateText("industryId123", "enuk_UK", "Color")) //
                .thenReturn("Colour");
        when(translationService.translateText("industryId123", "enuk_UK", "Mfg Part No")) //
                .thenReturn("Mfg Part No");
        when(translationService.translateText("industryId123", "enuk_UK", "Max")) //
                .thenReturn("Max.");
        when(translationService.translateText("industryId123", "enuk_UK", "Notebook")) //
                .thenReturn("Notebook.");
        when(translationService.translateText("industryId123", "enuk_UK", "IT")) //
                .thenReturn("I.T.");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_structures/category_structures_after_update_basic_offering.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateBasicCategoryStructure() {
        service.save("categoryId123", "BASIC_SPECS",
                Sets.newHashSet("colorId", "mfgPartNoId"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_structures/category_structures_after_update_detailed_offering.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateDetailedCategoryStructure() {
        service.save("categoryId123", "DETAILED_SPECS",
                Sets.newHashSet("colorId", "mfgPartNoId"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_structures/category_structures_after_update_basic_media_offering.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateBasicMediaCategoryStructure() {
        service.save("categoryId123", "BASIC_MEDIA", Sets.newHashSet("maxId"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_structures/category_structures_after_update_rich_media_offering.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateRichMediaCategoryStructure() {
        service.save("categoryId123", "RICH_MEDIA", Sets.newHashSet("maxId"));
    }
}
