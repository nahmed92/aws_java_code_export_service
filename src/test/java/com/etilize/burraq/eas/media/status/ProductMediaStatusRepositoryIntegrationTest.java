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

package com.etilize.burraq.eas.media.status;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

/**
 * This class implements repository test cases for {@link ProductMediaStatusRepository}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/media_statuses/media_statuses.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class ProductMediaStatusRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ProductMediaStatusRepository repository;

    @Test
    public void shouldFindAllMediaStatuses() {
        final List<ProductMediaStatus> specifications = Lists.newArrayList(
                repository.findAll());
        assertThat(specifications, is(notNullValue()));
        assertThat(specifications, hasSize(2));
        assertThat(specifications.get(0).getId(),
                isIn(Arrays.asList("product123-en", "product123-en_US")));
        assertThat(specifications.get(0).getProductId(),
                isIn(Arrays.asList("product123")));
        assertThat(specifications.get(0).getLocaleId(),
                isIn(Arrays.asList("en", "en_US")));
        assertThat(specifications.get(0).getStatusId(),
                isIn(Arrays.asList("PUBLISHED", "NEW")));
        assertThat(specifications.get(1).getId(),
                isIn(Arrays.asList("product123-en", "product123-en_US")));
        assertThat(specifications.get(1).getProductId(),
                isIn(Arrays.asList("product123")));
        assertThat(specifications.get(1).getLocaleId(),
                isIn(Arrays.asList("en", "en_US")));
        assertThat(specifications.get(1).getStatusId(),
                isIn(Arrays.asList("PUBLISHED", "NEW")));
    }

    @Test
    public void shouldFindMediaStatusById() {
        final Optional<ProductMediaStatus> specification = repository.findById(
                "product123-en");
        assertThat(specification, isPresentAnd(notNullValue()));
        assertThat(specification.get().getId(), is("product123-en"));
        assertThat(specification.get().getProductId(), is("product123"));
        assertThat(specification.get().getLocaleId(), is("en"));
        assertThat(specification.get().getStatusId(), is("PUBLISHED"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_statuses/media_statuses_after_create.bson")
    public void shouldCreateNewMediaStatus() {
        final ProductMediaStatus specs = new ProductMediaStatus();
        specs.setId("product123-en_UK");
        specs.setStatusId("NEW");
        specs.setLocaleId("en_UK");
        specs.setProductId("product123");
        specs.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(specs);

    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_statuses/media_statuses_after_update.bson")
    public void shouldUpdateMediaStatus() {
        final ProductMediaStatus specs = new ProductMediaStatus();
        specs.setId("product123-en_US");
        specs.setStatusId("PUBLISHED");
        specs.setLocaleId("en_US");
        specs.setProductId("product123");
        specs.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(specs);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_statuses/media_statuses_after_delete.bson")
    public void shouldDeleteMediaStatusById() {
        repository.deleteById("product123-en_US");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_statuses/media_statuses_after_delete_all_by_id.bson")
    public void shouldDeleteAllMediaStatusesByProductId() {
        repository.deleteAllByProductId("product123");
    }
}
