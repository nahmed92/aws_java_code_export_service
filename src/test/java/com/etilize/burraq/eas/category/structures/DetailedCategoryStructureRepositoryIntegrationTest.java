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

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.common.collect.ImmutableMap;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

/**
 * This class implements repository test cases for {@link DetailedCategoryStructureRepository}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/detailed_category_structures/detailed_category_structures.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class DetailedCategoryStructureRepositoryIntegrationTest
        extends AbstractIntegrationTest {

    @Autowired
    private DetailedCategoryStructureRepository repository;

    @Test
    public void shouldFindDetailedCategoryStructuresByMultipleIds() {
        final CategoryStructureKey key1 = new CategoryStructureKey();
        key1.setCategoryId("categoryId123");
        key1.setLocaleId("en_CA");
        final CategoryStructureKey key2 = new CategoryStructureKey();
        key2.setCategoryId("categoryId123");
        key2.setLocaleId("en_US");
        final Iterator<DetailedCategoryStructure> categories = repository.findAllById(
                Lists.newArrayList(key1, key2)).iterator();
        assertThat(categories.hasNext(), is(true));
        DetailedCategoryStructure category = categories.next();
        assertThat(category.getCategoryId(), is("categoryId123"));
        assertThat(category.getLocaleId(), isOneOf("en_CA", "en_US"));
        assertThat(category.getParentCategoryId(), is("parentCategoryId123"));
        assertThat(category.getIndustryId(), is("industryId123"));
        assertThat(category.getIndustryName(), is("IT"));
        assertThat(category.getCategoryName(), is("Notebook"));
        assertThat(category.getAttributes(), hasEntry("mfgPartNoId", "Mfg Part No"));
        assertThat(category.getAttributes(), hasEntry("sizeId", "Size"));
        assertThat(categories.hasNext(), is(true));
        category = categories.next();
        assertThat(category.getCategoryId(), is("categoryId123"));
        assertThat(category.getLocaleId(), isOneOf("en_CA", "en_US"));
        assertThat(category.getParentCategoryId(), is("parentCategoryId123"));
        assertThat(category.getIndustryId(), is("industryId123"));
        assertThat(category.getIndustryName(), is("IT"));
        assertThat(category.getCategoryName(), is("Notebook"));
        assertThat(category.getAttributes(), hasEntry("mfgPartNoId", "Mfg Part No"));
        assertThat(category.getAttributes(), hasEntry("sizeId", "Size"));
        assertThat(categories.hasNext(), is(false));
    }

    @Test
    public void shouldFindDetailedCategoryStructuresById() {
        final CategoryStructureKey key = new CategoryStructureKey();
        key.setCategoryId("categoryId123");
        key.setLocaleId("en_CA");
        final Optional<DetailedCategoryStructure> category = repository.findById(key);
        assertThat(category.isPresent(), is(true));
        assertThat(category.get().getCategoryId(), is("categoryId123"));
        assertThat(category.get().getLocaleId(), is("en_CA"));
        assertThat(category.get().getParentCategoryId(), is("parentCategoryId123"));
        assertThat(category.get().getIndustryId(), is("industryId123"));
        assertThat(category.get().getIndustryName(), is("IT"));
        assertThat(category.get().getCategoryName(), is("Notebook"));
        assertThat(category.get().getAttributes(),
                hasEntry("mfgPartNoId", "Mfg Part No"));
        assertThat(category.get().getAttributes(), hasEntry("sizeId", "Size"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/detailed_category_structures/detailed_category_structures_after_create.bson")
    public void shouldCreateNewDetailedCategoryStructure() {
        final DetailedCategoryStructure category = new DetailedCategoryStructure();
        category.setAttributes(ImmutableMap.of("mfgPartNoId", "Mfg Part No"));
        category.setCategoryId("categoryId123");
        category.setLocaleId("enuk_UK");
        category.setIndustryId("industryId123");
        category.setParentCategoryId("parentCategoryId123");
        category.setCategoryName("Notebook");
        category.setIndustryName("IT");
        category.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(category);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/detailed_category_structures/detailed_category_structures_after_update.bson")
    public void shouldUpdateDetailedCategoryStructure() {
        final DetailedCategoryStructure category = new DetailedCategoryStructure();
        category.setAttributes(ImmutableMap.of("mfgPartNoId", "Mfg Part No updated"));
        category.setCategoryId("categoryId123");
        category.setLocaleId("en_CA");
        category.setIndustryId("industryId123");
        category.setParentCategoryId("parentCategoryId123");
        category.setCategoryName("Notebook updated");
        category.setIndustryName("IT updated");
        category.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(category);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/detailed_category_structures/detailed_category_structures_after_delete.bson")
    public void shouldDeleteDetailedCategoryStructureById() {
        final CategoryStructureKey key = new CategoryStructureKey();
        key.setCategoryId("categoryId123");
        key.setLocaleId("en_CA");
        repository.deleteById(key);
    }
}
