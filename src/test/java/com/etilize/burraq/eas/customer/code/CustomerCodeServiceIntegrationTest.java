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

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import com.etilize.burraq.eas.locale.LocaleService;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.common.collect.Lists;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.IgnorePropertyValue;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

/**
 * This class implements repository test cases for {@link CustomerCodeService}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/customer_codes/customer_codes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class CustomerCodeServiceIntegrationTest extends AbstractIntegrationTest {

    @Mock
    private LocaleService localeService;

    private CustomerCodeService service;

    @Autowired
    private CustomerCodeRepository repository;

    @Override
    public void before() {
        service = new CustomerCodeServiceImpl(repository, localeService);
        when(localeService.getLocalesForMarket("US")).thenReturn(
                Lists.newArrayList("en_US", "fr_US"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/customer_codes/customer_codes_after_link.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldLinkNewCustomerCodeWithExitingProduct() {
        service.save("productId123", "US", "code1234", "customerId123");
        verify(localeService).getLocalesForMarket("US");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/customer_codes/customer_codes.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotLinkIfCustomerCodeIsAlreadyLinked() {
        service.save("productId123", "US", "code123", "customerId123");
        verify(localeService).getLocalesForMarket("US");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/customer_codes/customer_codes_after_creating_product.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldCreateNewProductWithCustomerCode() {
        service.save("productId1234", "US", "code1234", "customerId1234");
        verify(localeService).getLocalesForMarket("US");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/customer_codes/customer_codes_after_unlink.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUnlinkCustomerCodeFromExitingProduct() {
        service.delete("productId123", "US", "code12", "customerId123");
        verify(localeService).getLocalesForMarket("US");
    }
}
