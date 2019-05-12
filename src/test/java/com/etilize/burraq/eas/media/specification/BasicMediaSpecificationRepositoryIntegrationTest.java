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
import com.google.common.collect.Sets;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.IgnorePropertyValue;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

/**
 * This class implements repository test cases for {@link BasicMediaSpecificationRepository}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/basic_media_specifications/basic_media_specifications.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class BasicMediaSpecificationRepositoryIntegrationTest
        extends AbstractIntegrationTest {

    @Autowired
    private BasicMediaSpecificationRepository repository;

    @Test
    public void shouldFindAllBasicMediaSpecifications() {
        final List<BasicMediaSpecification> specifications = Lists.newArrayList(
                repository.findAll());
        assertThat(specifications, is(notNullValue()));
        assertThat(specifications, hasSize(2));
        assertThat(specifications.get(0).getId(),
                isIn(Arrays.asList("product123-en", "product123-en_US")));
        assertThat(specifications.get(0).getProductId(),
                isIn(Arrays.asList("product123")));
        assertThat(specifications.get(0).getLocaleId(),
                isIn(Arrays.asList("en", "en_US")));
        assertThat(specifications.get(0).getAttributes(), notNullValue());
        assertThat(specifications.get(0).getAttributes().get("maxId"), notNullValue());
        assertThat(specifications.get(0).getAttributes().get("maxId").getUrl(),
                is("http://abcd"));
        assertThat(specifications.get(0).getAttributes().get("maxId").getWidth(),
                is(160));
        assertThat(specifications.get(0).getAttributes().get("maxId").getHeight(),
                is(190));
        assertThat(specifications.get(0).getAttributes().get("maxId").getTags(),
                contains("Prod Specs"));
        assertThat(specifications.get(1).getId(),
                isIn(Arrays.asList("product123-en", "product123-en_US")));
        assertThat(specifications.get(1).getProductId(),
                isIn(Arrays.asList("product123")));
        assertThat(specifications.get(1).getLocaleId(),
                isIn(Arrays.asList("en", "en_US")));
        assertThat(specifications.get(1).getAttributes(), notNullValue());
        assertThat(specifications.get(1).getAttributes().get("maxId"), notNullValue());
        assertThat(specifications.get(1).getAttributes().get("maxId").getUrl(),
                is("http://abcde"));
    }

    @Test
    public void shouldFindBasicMediaSpecificationById() {
        final Optional<BasicMediaSpecification> specification = repository.findById(
                "product123-en");
        assertThat(specification, isPresentAnd(notNullValue()));
        assertThat(specification.get().getId(), is("product123-en"));
        assertThat(specification.get().getProductId(), is("product123"));
        assertThat(specification.get().getLocaleId(), is("en"));
        assertThat(specification.get().getAttributes(), notNullValue());
        assertThat(specification.get().getAttributes().get("maxId"), notNullValue());
        assertThat(specification.get().getAttributes().get("maxId").getUrl(),
                is("http://abcd"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/basic_media_specifications/basic_media_specifications_after_create.bson")
    public void shouldCreateNewBasicMediaSpecification() {
        final MediaAttributeValue value = new MediaAttributeValue();
        value.setUrl("http://abc");
        value.setHeight(190);
        value.setWidth(190);
        value.setTags(Sets.newHashSet("Prod Specs"));
        final Map<String, MediaAttributeValue> attributes = Maps.newHashMap();
        attributes.put("maxId", value);
        final BasicMediaSpecification specs = new BasicMediaSpecification();
        specs.setId("product1234-en");
        specs.setAttributes(attributes);
        specs.setLocaleId("en");
        specs.setProductId("product1234");
        specs.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(specs);

    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/basic_media_specifications/basic_media_specifications_after_update.bson")
    public void shouldUpdateBasicMediaSpecification() {
        final MediaAttributeValue value = new MediaAttributeValue();
        value.setUrl("http://abc");
        value.setHeight(190);
        value.setWidth(160);
        value.setTags(Sets.newHashSet("Prod Specs"));
        final BasicMediaSpecification specs = repository.findById(
                "product123-en_US").get();
        specs.getAttributes().put("maxId", value);
        repository.save(specs);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/basic_media_specifications/basic_media_specifications_after_delete.bson")
    public void shouldDeleteBasicMediaSpecificationById() {
        repository.deleteById("product123-en_US");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/basic_media_specifications/basic_media_specifications_after_update_attribute_one_record.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeValueForId() {
        final MediaAttributeValue value = new MediaAttributeValue();
        value.setUrl("http://xy.png");
        value.setWidth(160);
        value.setTags(Sets.newHashSet("Prod Specs"));
        repository.updateAttribute("product123-en_US", "maxId", value);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/basic_media_specifications/basic_media_specifications_after_add_attribute_one_record.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldAddAttributeValueForId() {
        final MediaAttributeValue value = new MediaAttributeValue();
        value.setUrl("http://xy.png");
        value.setHeight(190);
        value.setWidth(160);
        value.setTags(Sets.newHashSet("Prod Specs"));
        repository.updateAttribute("product123-en_US", "thumbnailId", value);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/basic_media_specifications/basic_media_specifications_after_delete_attribute_one_record.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldRemoveAttributeValueForId() {
        repository.removeAttribute("product123-en_US", "maxId");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/basic_media_specifications/basic_media_specifications.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldDoNothingOnRemoveAttributeWhichDoesNotExistForId() {
        repository.removeAttribute("product123-en_US", "thumbnailId");
    }
}
