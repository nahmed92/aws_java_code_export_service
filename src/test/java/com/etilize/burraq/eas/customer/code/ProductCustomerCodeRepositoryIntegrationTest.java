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

package com.etilize.burraq.eas.customer.code;

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Date;
import java.util.Optional;

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
 * This class implements repository test cases for {@link ProductCustomerCodeRepository}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/customer_codes/customer_codes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class ProductCustomerCodeRepositoryIntegrationTest
        extends AbstractIntegrationTest {

    @Autowired
    private ProductCustomerCodeRepository repository;

    @Test
    public void shouldFindCustomerCodeById() {
        final ProductCustomerCodeKey id = new ProductCustomerCodeKey();
        id.setCustomerId("customerId123");
        id.setId("productId123-en_US");
        final Optional<ProductCustomerCode> customerCode = repository.findById(id);
        assertThat(customerCode, isPresentAnd(notNullValue()));
        assertThat(customerCode.get().getId(), is("productId123-en_US"));
        assertThat(customerCode.get().getProductId(), is("productId123"));
        assertThat(customerCode.get().getCustomerId(), is("customerId123"));
        assertThat(customerCode.get().getLocaleId(), is("en_US"));
        assertThat(customerCode.get().getCodes(),
                containsInAnyOrder("code12", "code123"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/customer_codes/customer_codes_after_create.bson")
    public void shouldCreateNewCustomerCode() {
        final ProductCustomerCode customerCode = new ProductCustomerCode();
        customerCode.setId("productId1234-en_US");
        customerCode.setCustomerId("customerId1234");
        customerCode.setLocaleId("en_US");
        customerCode.setProductId("productId1234");
        customerCode.setCodes(Sets.newHashSet("code1234"));
        customerCode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(customerCode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/customer_codes/customer_codes_after_update.bson")
    public void shouldUpdateCustomerCode() {
        final ProductCustomerCode customerCode = new ProductCustomerCode();
        customerCode.setId("productId123-en_US");
        customerCode.setCustomerId("customerId123");
        customerCode.setLocaleId("en_US");
        customerCode.setProductId("productId123");
        customerCode.setCodes(Sets.newHashSet("code12", "code1234"));
        customerCode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(customerCode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/customer_codes/customer_codes_after_delete.bson")
    public void shouldDeleteCustomerCodeById() {
        final ProductCustomerCodeKey id = new ProductCustomerCodeKey();
        id.setCustomerId("customerId123");
        id.setId("productId123-en_US");
        repository.deleteById(id);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/customer_codes/customer_codes_after_create.bson")
    public void shouldLinkCustomerCode() {
        final ProductCustomerCode customerCode = new ProductCustomerCode();
        customerCode.setId("productId1234-en_US");
        customerCode.setCustomerId("customerId1234");
        customerCode.setLocaleId("en_US");
        customerCode.setProductId("productId1234");
        customerCode.setCodes(Sets.newHashSet("code1234"));
        customerCode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.link(customerCode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/customer_codes/customer_codes_after_unlink_one_record.bson")
    public void shouldUnlinkCustomerCodeById() {
        final ProductCustomerCode customerCode = new ProductCustomerCode();
        customerCode.setId("productId123-en_US");
        customerCode.setCustomerId("customerId123");
        customerCode.setLocaleId("en_US");
        customerCode.setProductId("productId123");
        customerCode.setCodes(Sets.newHashSet("code123"));
        customerCode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.unlink(customerCode);
    }

}
