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

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.common.collect.Maps;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.IgnorePropertyValue;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

/**
 * This class implements repository test cases for {@link ProductBasicSpecificationRepository}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/basic_specifications/basic_specifications.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class ProductBasicSpecificationRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ProductBasicSpecificationRepository repository;

    @Test
    @UsingDataSet(locations = "/datasets/basic_specifications/basic_specifications_with_attributes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/basic_specifications/basic_specifications_with_attributes_after_update_attributes.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeWhenAttributeValueIsAddedOrReplaced() {
        final UpdateProductSpecificationRequest request = UpdateProductSpecificationRequestFixture.createWithUpdatedAttributes();
        repository.saveAttributes(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/basic_specifications/basic_specifications_with_attributes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/basic_specifications/basic_specifications_with_attributes_after_add_in_set.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeWhenAttributeValueIsAddedInSet() {
        final UpdateProductSpecificationRequest request = UpdateProductSpecificationRequestFixture.createWithAddedAttributeValue();
        repository.saveAttributes(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/basic_specifications/basic_specifications_with_attributes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/basic_specifications/basic_specifications_with_attributes_after_remove_in_set.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeWhenAttributeValueIsRemoveInSet() {
        final UpdateProductSpecificationRequest request = UpdateProductSpecificationRequestFixture.createWithRemovedAttributeValue();
        repository.saveAttributes(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/basic_specifications/basic_specifications_with_attributes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/basic_specifications/basic_specifications_with_attributes_after_remove_attribute.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeWhenAttributeValueIsRemoved() {
        final UpdateProductSpecificationRequest request = UpdateProductSpecificationRequestFixture.createWithRemovedAttributeIds();
        repository.saveAttributes(request);
    }

    @Test
    @UsingDataSet(locations = "/datasets/basic_specifications/basic_specifications_with_attributes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @ShouldMatchDataSet(location = "/datasets/basic_specifications/basic_specifications_with_attributes_after_update_misc.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeForMiscAttributeValueUpdates() {
        final UpdateProductSpecificationRequest request = UpdateProductSpecificationRequestFixture.createWithMiscUpdates();
        repository.saveAttributes(request);
    }

    @Test
    public void shouldFindAllBasicSpecifications() {
        final List<ProductBasicSpecification> specifications = Lists.newArrayList(
                repository.findAll());
        assertThat(specifications, is(notNullValue()));
        assertThat(specifications, hasSize(2));
        assertThat(specifications.get(0).getId(),
                isIn(Arrays.asList("product123-en", "product123-en_US")));
        assertThat(specifications.get(0).getProductId(),
                isIn(Arrays.asList("product123")));
        assertThat(specifications.get(0).getLocaleId(),
                isIn(Arrays.asList("en", "en_US")));
        assertThat(specifications.get(0).getCategoryId(),
                isIn(Arrays.asList("categoryId123")));
        assertThat(specifications.get(0).getIndustryId(),
                isIn(Arrays.asList("industryId123")));
        assertThat(specifications.get(1).getId(),
                isIn(Arrays.asList("product123-en", "product123-en_US")));
        assertThat(specifications.get(1).getProductId(),
                isIn(Arrays.asList("product123")));
        assertThat(specifications.get(1).getLocaleId(),
                isIn(Arrays.asList("en", "en_US")));
        assertThat(specifications.get(1).getCategoryId(),
                isIn(Arrays.asList("categoryId123")));
        assertThat(specifications.get(1).getIndustryId(),
                isIn(Arrays.asList("industryId123")));
    }

    @Test
    @UsingDataSet(locations = "/datasets/basic_specifications/basic_specifications_with_attributes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldFindBasicSpecificationById() {
        final Optional<ProductSpecification> specification = repository.findOne("product123-en");
        assertThat(specification, isPresentAnd(notNullValue()));
        assertThat(specification.get().getId(), is("product123-en"));
        assertThat(specification.get().getProductId(), is("product123"));
        assertThat(specification.get().getLocaleId(), is("en"));
        assertThat(specification.get().getCategoryId(), is("categoryId123"));
        assertThat(specification.get().getIndustryId(), is("industryId123"));
        assertThat(specification.get().getAttributes(), hasEntry("mfgPartNoId", "XYZ"));
        assertThat(specification.get().getAttributes().get("processId"), notNullValue());
        final Map<String, Map<String, Object>> value = (Map<String, Map<String, Object>>) specification.get().getAttributes().get(
                "processId");
        assertThat(value.get("EXTRACTED"), notNullValue());
        assertThat(value.get("EXTRACTED"), hasEntry("unit", "g"));
    }

    @Test
    @UsingDataSet(locations = "/datasets/basic_specifications/basic_specifications_with_attributes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldFindProductById() {
        final Optional<Product> specification = repository.findProductById(
                "product123-en");
        assertThat(specification, isPresentAnd(notNullValue()));
        assertThat(specification.get().getProductId(), is("product123"));
        assertThat(specification.get().getCategoryId(), is("categoryId123"));
        assertThat(specification.get().getIndustryId(), is("industryId123"));
    }

    @Test
    public void shouldReturnOptionalEmptyByIdDoesNotExist() {
        final Optional<ProductSpecification> specification = repository.findOne(
                "product123-en1");
        assertThat(specification.isPresent(), is(false));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/basic_specifications/basic_specifications_after_create.bson")
    public void shouldCreateNewBasicSpecification() {
        final ProductBasicSpecification specs = new ProductBasicSpecification();
        specs.setId("product1234-en");
        specs.setAttributes(Maps.newHashMap());
        specs.setCategoryId("categoryId123");
        specs.setIndustryId("industryId123");
        specs.setLocaleId("en");
        specs.setProductId("product1234");
        specs.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(specs);

    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/basic_specifications/basic_specifications_after_update.bson")
    public void shouldUpdateBasicSpecification() {
        final ProductBasicSpecification specs = repository.findById("product123-en_US").get();
        specs.setCategoryId("categoryId1234");
        specs.setIndustryId("industryId1234");
        repository.save(specs);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/basic_specifications/basic_specifications_after_delete.bson")
    public void shouldDeleteBasicSpecificationById() {
        repository.deleteById("product123-en_US");
    }
}
