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

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.eas.attribute.Attribute;
import com.etilize.burraq.eas.attribute.Scope;
import com.etilize.burraq.eas.attribute.Type;
import com.etilize.burraq.eas.category.specification.CategorySpecificationService;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatus;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatusRepository;
import com.etilize.burraq.eas.specification.value.Value;
import com.etilize.burraq.eas.taxonomy.TaxonomyService;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.etilize.burraq.eas.translation.TranslationService;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.IgnorePropertyValue;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

/**
 * This class implements repository test cases for {@link ProductSpecificationService}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/specifications/specifications.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class ProductSpecificationServiceIntegrationTest extends AbstractIntegrationTest {

    private ProductSpecificationService service;

    @Mock
    private TranslationService translationService;

    @Mock
    private TaxonomyService taxonomyService;

    @Mock
    private CategorySpecificationService categoryStructureService;

    @Mock
    private ProductSpecificationStatusRepository specificationStatusRepository;

    @Autowired
    private ProductBasicSpecificationRepository basicSpecificationRepository;

    @Autowired
    private ProductDetailedSpecificationRepository detailedSpecificationRepository;

    @Autowired
    private ProductAccessorySpecificationRepository accessorySpecificationRepository;

    @Override
    public void before() {
        service = new ProductSpecificationServiceImpl(basicSpecificationRepository,
                detailedSpecificationRepository, translationService, taxonomyService,
                categoryStructureService, specificationStatusRepository, accessorySpecificationRepository);
        when(taxonomyService.findAttributeById("mfgPartNoId")) //
                .thenReturn(getAttribute("Mfg Part No", "industryId123", Type.TEXT, false,
                        false, Scope.INTERNATIONAL));
        when(taxonomyService.findAttributeById("sizeId")) //
                .thenReturn(getAttribute("Size", "industryId123", Type.NUMBER, false,
                        false, Scope.INTERNATIONAL));
        when(taxonomyService.findAttributeById("mfgId")) //
                .thenReturn(getAttribute("Mfg", "industryId123", Type.CODED, true, false,
                        Scope.INTERNATIONAL));
        when(taxonomyService.findAttributeById("processId")) //
                .thenReturn(getAttribute("Process", "industryId123", Type.UNIT, true,
                        false, Scope.INTERNATIONAL));
        when(taxonomyService.findAttributeById("unitRepId")) //
                .thenReturn(getAttribute("Unit Rep", "industryId123", Type.UNIT, true,
                        true, Scope.INTERNATIONAL));
        when(taxonomyService.findAttributeById("stringRepId")) //
                .thenReturn(getAttribute("String Rep", "industryId123", Type.TEXT, true,
                        true, Scope.INTERNATIONAL));
        when(taxonomyService.findAttributeById("numberRepId")) //
                .thenReturn(getAttribute("Number Rep", "industryId123", Type.NUMBER,
                        false, true, Scope.INTERNATIONAL));

        when(taxonomyService.findAttributeById("marketingId")) //
                .thenReturn(getAttribute("Marketing", "industryId123", Type.PARAGRAPH,
                        false, false, Scope.LANGUAGE_SPECIFIC));
        when(taxonomyService.findAttributeById("stringTranslatableId")) //
                .thenReturn(getAttribute("String Trans", "industryId123", Type.TEXT, true,
                        false, Scope.LANGUAGE_SPECIFIC));

        when(taxonomyService.findAttributeById("notexistsId")) //
                .thenReturn(getAttribute("Not exists", "industryId123", Type.NUMBER,
                        false, false, Scope.INTERNATIONAL));

        when(translationService.translateText("industryId123", "enuk_UK", "Sony")) //
                .thenReturn("Sony.");
        when(translationService.translateText("industryId123", "enuk_UK", "abc")) //
                .thenReturn("abc.");
        when(translationService.translateUnit("enuk_UK", "b")) //
                .thenReturn("b.");
        when(translationService.translateUnit("enuk_UK", "g")) //
                .thenReturn("g.");
        when(translationService.translateText("industryId123", "fr_US", "Acer")) //
                .thenReturn("Acer.");
        when(translationService.translateUnit("fr_US", "kg")) //
                .thenReturn("kg.");
        when(translationService.translateUnit("fr_US", "mb")) //
                .thenReturn("mb.");
        when(translationService.translateText("industryId123", "fr_US", "def")) //
                .thenReturn("def.");
        when(translationService.translateText("industryId123", "fr_US", "ghi")) //
                .thenReturn("ghi.");
        when(translationService.translateText("industryId123", "fr_US", "abc")) //
                .thenReturn("abc.");
        final ProductSpecificationStatus specsStatus1 = new ProductSpecificationStatus();
        specsStatus1.setId("product123-en_US");
        specsStatus1.setStatusId("NEW");
        specsStatus1.setLocaleId("en_US");
        specsStatus1.setProductId("product123");
        final ProductSpecificationStatus specsStatus2 = new ProductSpecificationStatus();
        specsStatus2.setId("product123-fr_US");
        specsStatus2.setStatusId("NEW");
        specsStatus2.setLocaleId("fr_US");
        specsStatus2.setProductId("product123");
        final ProductSpecificationStatus specsStatus3 = new ProductSpecificationStatus();
        specsStatus3.setId("product123-en");
        specsStatus3.setStatusId("NEW");
        specsStatus3.setLocaleId("en");
        specsStatus3.setProductId("product123");
        when(specificationStatusRepository.findAllByProductId("product123")) //
                .thenReturn(Lists.newArrayList(specsStatus3, specsStatus1, specsStatus2));

        when(categoryStructureService.findBasicSpecsOfferingAttributes("categoryId123")) //
                .thenReturn(ImmutableMap.of("mfgId", "Mfg", "mfgPartNoId", "Mfg Part No",
                        "sizeId", "Size"));

        when(categoryStructureService.findDetailedSpecsOfferingAttributes(
                "categoryId123")) //
                        .thenReturn(getDetailedOfferingAttributeMap());
    }

    private Map<String, String> getDetailedOfferingAttributeMap() {
        Map<String, String> attributes = Maps.newHashMap();
        attributes.put("mfgId", "Mfg");
        attributes.put("mfgPartNoId", "Mfg Part No");
        attributes.put("sizeId", "Size");
        attributes.put("processId", "Process");
        attributes.put("unitRepId", " Unit Rep");
        attributes.put("stringRepId", "String Rep");
        attributes.put("numberRepId", "Number Rep");
        attributes.put("stringTranslatableId", "String Trans");
        attributes.put("marketingId", "Marketing Info");
        return attributes;
    }

    @Test
    @UsingDataSet(locations = "/datasets/specifications/specifications_for_specs.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/specifications/specifications_for_specs_after_update_attributes_for_locale_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeWhenAttributeValueIsAddedOrReplacedForInternationLocale() {
        final UpdateProductSpecificationRequest request = UpdateProductSpecificationRequestFixture.createWithUpdatedAttributes();
        service.updateSpecifications(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/specifications/specifications_for_specs.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/specifications/specifications_for_specs_after_add_to_set_locale_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeWhenValueIsAddedForRepeatableAttributeForInternationLocale() {
        final UpdateProductSpecificationRequest request = UpdateProductSpecificationRequestFixture.createWithAddedAttributeValue();
        service.updateSpecifications(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/specifications/specifications_for_specs.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/specifications/specifications_for_specs_after_remove_from_set_locale_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeWhenValueIsRemovedFromSetForRepeatableAttributeForInternationLocale() {
        when(translationService.translateText("industryId123", "fr_US", "abc")) //
                .thenReturn("abc.");
        final UpdateProductSpecificationRequest request = UpdateProductSpecificationRequestFixture.createWithRemovedAttributeValue();
        service.updateSpecifications(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/specifications/specifications_for_specs.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/specifications/specifications_for_specs_after_remove_attribute_locale_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeWhenValueIsRemovedForAttributeForInternationLocale() {
        final UpdateProductSpecificationRequest request = UpdateProductSpecificationRequestFixture.createWithRemovedAttributeIds();
        service.updateSpecifications(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/specifications/specifications_for_specs.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/specifications/specifications_for_specs_after_misc_update_locale_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeForMiscActionForInternationLocale() {
        final UpdateProductSpecificationRequest request = UpdateProductSpecificationRequestFixture.createWithMiscUpdates();
        service.updateSpecifications(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/specifications/specifications_for_specs.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/specifications/specifications_for_specs_after_update_locale_en_us.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeForMiscActionForENUSLocale() {
        final UpdateProductSpecificationRequest request = new UpdateProductSpecificationRequest(
                "product123", "en_US");
        request.addUpdatedAttributes("marketingId",
                new Value("Marketing info en_US updated"));
        request.addUpdatedAttributes("stringTranslatableId", new Value("def"));
        service.updateSpecifications(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/specifications/specifications_for_specs.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/specifications/specifications_for_specs_after_update_locale_fr_us.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeForMiscActionForFRUSLocale() {
        final UpdateProductSpecificationRequest request = new UpdateProductSpecificationRequest(
                "product123", "fr_US");
        request.addUpdatedAttributes("marketingId",
                new Value("Marketing info fr_US updated"));
        service.updateSpecifications(request);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/specifications/specifications_after_create.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldCreateNewBasicAndAccessoryAndDetailedSpecification() {
        service.createProduct("product1234", "industryId123", "categoryId123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/specifications/specifications_after_add_locale.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldAddLocaleAndCopySpecificationWhenNonInternationLocaleIsAdded() {
        service.addLocale("product123", "enuk_UK");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/specifications/specifications.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldDoNotingWhenInternationLocaleIsAdded() {
        service.addLocale("product123", "en");
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
