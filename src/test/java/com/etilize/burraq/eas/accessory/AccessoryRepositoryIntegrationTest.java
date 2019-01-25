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

package com.etilize.burraq.eas.accessory;

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

import avro.shaded.com.google.common.collect.Sets;

/**
 * This class implements repository test cases for {@link AccessoryRepository}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/accessories/accessories.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class AccessoryRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private AccessoryRepository repository;

    @Test
    public void shouldFindAccessoryById() {
        final Optional<Accessory> accessory = repository.findById("productId123-en_US");
        assertThat(accessory, isPresentAnd(notNullValue()));
        assertThat(accessory.get().getId(), is("productId123-en_US"));
        assertThat(accessory.get().getProductId(), is("productId123"));
        assertThat(accessory.get().getLocaleId(), is("en_US"));
        assertThat(accessory.get().getAccessoryProductIds(), containsInAnyOrder(
                "accessoryProductId12-en_US", "accessoryProductId123-en_US"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/accessories/accessories_after_creating.bson")
    public void shouldCreateNewAccessory() {
        final Accessory accessory = new Accessory();
        accessory.setId("productId1234-en_US");
        accessory.setLocaleId("en_US");
        accessory.setProductId("productId1234");
        accessory.setAccessoryProductIds(Sets.newHashSet("accessoryProductId1234-en_US"));
        accessory.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(accessory);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/accessories/accessories_after_update.bson")
    public void shouldUpdateAccessory() {
        final Accessory accessory = new Accessory();
        accessory.setId("productId123-en_US");
        accessory.setLocaleId("en_US");
        accessory.setProductId("productId123");
        accessory.setAccessoryProductIds(Sets.newHashSet("accessoryProductId12-en_US",
                "accessoryProductId1234-en_US"));
        accessory.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(accessory);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/accessories/accessories_after_delete.bson")
    public void shouldDeleteAccessoryById() {
        repository.deleteById("productId123-en_US");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/accessories/accessories_after_creating.bson")
    public void shouldLinkNewAccessory() {
        final Accessory accessory = new Accessory();
        accessory.setId("productId1234-en_US");
        accessory.setLocaleId("en_US");
        accessory.setProductId("productId1234");
        accessory.setAccessoryProductIds(Sets.newHashSet("accessoryProductId1234-en_US"));
        accessory.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.link(accessory);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/accessories/accessories_after_unlink_one_record.bson")
    public void shouldUnlinkAccessory() {
        final Accessory accessory = new Accessory();
        accessory.setId("productId123-en_US");
        accessory.setLocaleId("en_US");
        accessory.setProductId("productId123");
        accessory.setAccessoryProductIds(Sets.newHashSet("accessoryProductId123-en_US"));
        accessory.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.unlink(accessory);
    }

}
