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

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.eas.category.specification.CategorySpecificationService;
import com.etilize.burraq.eas.specification.Product;
import com.etilize.burraq.eas.specification.ProductSpecificationService;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatus;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatusRepository;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.IgnorePropertyValue;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

/**
 * This class implements repository test cases for {@link ProductMediaSpecificationService}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/media_specifications/media_specifications.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class ProductMediaSpecificationServiceIntegrationTest
        extends AbstractIntegrationTest {

    @Mock
    private CategorySpecificationService categoryStructureService;

    @Mock
    private ProductSpecificationStatusRepository specsStatusRepository;

    @Mock
    private ProductSpecificationService specificationService;

    @Autowired
    private ProductBasicMediaSpecificationRepository basicSpecificationRepository;

    @Autowired
    private ProductRichMediaSpecificationRepository detailedSpecificationRepository;

    private ProductMediaSpecificationService service;

    @Override
    public void before() {
        service = new ProductMediaSpecificationServiceImpl(categoryStructureService,
                specificationService, basicSpecificationRepository,
                detailedSpecificationRepository, specsStatusRepository);
        final ProductSpecificationStatus specsStatus1 = new ProductSpecificationStatus();
        specsStatus1.setId("product123-en_US");
        specsStatus1.setStatusId("NEW");
        specsStatus1.setLocaleId("en_US");
        specsStatus1.setProductId("product123");
        final ProductSpecificationStatus specsStatus2 = new ProductSpecificationStatus();
        specsStatus2.setId("product123-en");
        specsStatus2.setStatusId("NEW");
        specsStatus2.setLocaleId("en");
        specsStatus2.setProductId("product123");
        when(specsStatusRepository.findAllByProductId("product123")) //
                .thenReturn(Lists.newArrayList(specsStatus1, specsStatus2));
        final Product product = new Product();
        product.setProductId("product123");
        product.setCategoryId("categoryId123");
        product.setIndustryId("industryId123");
        when(specificationService.findProductByProductId("product123")) //
                .thenReturn(Optional.of(product));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_add_locale.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldAddLocaleForMediaSpecification() {
        service.addLocale("product123", "en_UK");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_update_attribute_for_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeForAllLocaleWhenUpdatedForEN() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "thumbnailId")) //
                        .thenReturn(true);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "thumbnailId")) //
                        .thenReturn(true);
        final ProductMediaAttributeValue value = new ProductMediaAttributeValue();
        value.setUrl("http://abcde");
        service.saveAttribute("product123", "en", "thumbnailId", Status.ASSOCIATED,
                value);
        verify(specsStatusRepository).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_update_attribute_for_non_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeWhenUpdatedForNonEN() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "thumbnailId")) //
                        .thenReturn(true);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "thumbnailId")) //
                        .thenReturn(true);
        final ProductMediaAttributeValue value = new ProductMediaAttributeValue();
        value.setUrl("http://abcde");
        service.saveAttribute("product123", "en_US", "thumbnailId", Status.ASSOCIATED,
                value);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_update_attribute_for_en_only_for_detailed.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeForAllLocaleWhenUpdatedForENOnlyInDetailedOffer() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "largeId")) //
                        .thenReturn(false);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "largeId")) //
                        .thenReturn(true);
        final ProductMediaAttributeValue value = new ProductMediaAttributeValue();
        value.setUrl("http://abcdef");
        service.saveAttribute("product123", "en", "largeId", Status.ASSOCIATED, value);
        verify(specsStatusRepository).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_update_attribute_for_non_en_only_for_detailed.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeWhenUpdatedForNonENOnlyInDetailedOffer() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "largeId")) //
                        .thenReturn(false);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "largeId")) //
                        .thenReturn(true);
        final ProductMediaAttributeValue value = new ProductMediaAttributeValue();
        value.setUrl("http://abcdef");
        service.saveAttribute("product123", "en_US", "largeId", Status.ASSOCIATED, value);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotUpdateAttributeForAllLocaleWhenUpdatedForENAndAttributeIsNotInAnyOffering() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "notExistId")) //
                        .thenReturn(false);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "notExistId")) //
                        .thenReturn(false);
        final ProductMediaAttributeValue value = new ProductMediaAttributeValue();
        value.setUrl("http://abcde");
        service.saveAttribute("product123", "en", "notExistId", Status.ASSOCIATED, value);
        verify(specsStatusRepository).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotUpdateAttributeWhenUpdatedForNonENOnlyAndAttributeIsNotInAnyOffering() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "notExistId")) //
                        .thenReturn(false);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "notExistId")) //
                        .thenReturn(false);
        final ProductMediaAttributeValue value = new ProductMediaAttributeValue();
        value.setUrl("http://abcde");
        service.saveAttribute("product123", "en_US", "notExistId", Status.ASSOCIATED,
                value);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotUpdateAttributeForAllLocaleWhenValueIsEmpty() {
        final ProductMediaAttributeValue value = new ProductMediaAttributeValue();
        value.setUrl("");
        service.saveAttribute("product123", "en", "thumbnailId", Status.ASSOCIATED,
                value);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_delete_attribute_for_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldDeleteAttributeForAllLocaleWhenUpdatedForENWithStatusDELETED() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(true);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(true);
        service.saveAttribute("product123", "en", "maxId", Status.DELETED, null);
        verify(specsStatusRepository).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_delete_attribute_for_non_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldDeleteAttributeWhenUpdatedForNonENWithStatusDELETED() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(true);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(true);
        service.saveAttribute("product123", "en_US", "maxId", Status.DELETED, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @UsingDataSet(locations = "/datasets/media_specifications/media_specifications_before_delete.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_delete_attribute_for_en_from_detailed_specs.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldDeleteAttributeWhenUpdatedForENWithStatusDELETEDForDetailedOffering() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(false);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(true);
        service.saveAttribute("product123", "en", "maxId", Status.DELETED, null);
    }

    @Test
    @UsingDataSet(locations = "/datasets/media_specifications/media_specifications_before_delete.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_delete_attribute_for_non_en_from_detailed_specs.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldDeleteAttributeWhenUpdatedForNonENWithStatusDELETEDForDetailedOffering() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(false);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(true);
        service.saveAttribute("product123", "en_US", "maxId", Status.DELETED, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotDeleteAttributeWhenUpdatedForNonENWithStatusDELETEDAndAttributeNotInAnyOffering() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(false);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(false);
        service.saveAttribute("product123", "en_US", "maxId", Status.DELETED, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_delete_attribute_for_non_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldDeleteAttributeWhenUpdatedForNonENWithStatusEXCEPTION() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(true);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(true);
        service.saveAttribute("product123", "en_US", "maxId", Status.EXCEPTION, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_delete_attribute_for_non_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    @Ignore // Removing on pending case is not valid, current impl it remove tags
    public void shouldDeleteAttributeWhenUpdatedForNonENWithStatusPENDING() {
        when(categoryStructureService.hasBasicMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(true);
        when(categoryStructureService.hasRichMediaOfferingAttribute("categoryId123",
                "maxId")) //
                        .thenReturn(true);
        service.saveAttribute("product123", "en_US", "maxId", Status.PENDING, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotDeleteAttributeWhenUpdatedForNonENWithStatusDOWNLOADING() {
        service.saveAttribute("product123", "en_US", "maxId", Status.DOWNLOADING, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotDeleteAttributeWhenUpdatedForNonENWithStatusASSOCIATED() {
        service.saveAttribute("product123", "en_US", "maxId", Status.ASSOCIATED, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @UsingDataSet(locations = "/datasets/media_specifications/media_specification_category_update.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specification_category_after_update.json")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateMediaProductCategory() {
        when(categoryStructureService.findBasicMediaSpecsOfferingAttributes(
                "categoryId456")) //
                        .thenReturn(
                                ImmutableMap.of("maxId", "maxvalue", "largeId", "test"));
        when(categoryStructureService.findRichMediaSpecsOfferingAttributes(
                "categoryId456")) //
                        .thenReturn(
                                ImmutableMap.of("maxId", "maxvalue", "largeId", "test"));
        service.updateProductCategory("product123", "categoryId456");
    }
}
