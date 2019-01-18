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

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.eas.accessory.AccessoryRepository;
import com.etilize.burraq.eas.accessory.AccessoryService;
import com.etilize.burraq.eas.accessory.AccessoryServiceImpl;
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
 * This class implements repository test cases for {@link AccessoryService}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/accessories/accessories.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class AccessoryServiceIntegrationTest extends AbstractIntegrationTest {

    @Mock
    private LocaleService localeService;

    private AccessoryService service;

    @Autowired
    private AccessoryRepository repository;

    @Override
    public void before() {
        service = new AccessoryServiceImpl(repository, localeService);
        when(localeService.getLocalesForMarket("US")).thenReturn(
                Lists.newArrayList("en_US", "fr_US"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/accessories/accessories_after_link.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldLinkNewAccessoryWithExitingProduct() {
        service.save("productId123", "US", "accessoryProductId1234");
        verify(localeService).getLocalesForMarket("US");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/accessories/accessories_after_linking_existing_accessory.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotLinkIfAccessoryIsAlreadyLinked() {
        service.save("productId123", "US", "accessoryProductId123");
        verify(localeService).getLocalesForMarket("US");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/accessories/accessories_after_creating_product.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldCreateNewProductWithAccessory() {
        service.save("productId1234", "US", "accessoryProductId1234");
        verify(localeService).getLocalesForMarket("US");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/accessories/accessories_after_unlink.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUnlinkAccessoryFromExitingProduct() {
        service.delete("productId123", "US", "accessoryProductId12");
        verify(localeService).getLocalesForMarket("US");
    }
}
