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

package com.etilize.burraq.eas.barcode;

import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import com.etilize.burraq.eas.locale.LocaleService;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatus;
import com.etilize.burraq.eas.specification.status.ProductSpecificationStatusRepository;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.IgnorePropertyValue;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

/**
 * This class implements repository test cases for {@link ProductBarcodeService}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/barcodes/barcodes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class ProductBarcodeServiceIntegrationTest extends AbstractIntegrationTest {

    @Mock
    private ProductSpecificationStatusRepository specsStatusRepository;

    private ProductBarcodeService service;

    @Autowired
    private ProductBarcodeRepository repository;

    @Override
    public void before() {
        service = new ProductBarcodeServiceImpl(repository, specsStatusRepository);
        final ProductSpecificationStatus specsStatus1 = new ProductSpecificationStatus();
        specsStatus1.setId("productId123-en_US");
        specsStatus1.setStatusId("NEW");
        specsStatus1.setLocaleId("en_US");
        specsStatus1.setProductId("productId123");
        final ProductSpecificationStatus specsStatus2 = new ProductSpecificationStatus();
        specsStatus2.setId("productId123-fr_US");
        specsStatus2.setStatusId("NEW");
        specsStatus2.setLocaleId("fr_US");
        specsStatus2.setProductId("productId123");
        when(specsStatusRepository.findAllByProductId("productId123")) //
                .thenReturn(Lists.newArrayList(specsStatus1, specsStatus2));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_link.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldLinkNewBarcodeWithExitingProduct() {
        service.save("productId123", "EAN", "code1234", "customerId1234");
        verify(specsStatusRepository).findAllByProductId("productId123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotLinkIfBarcodeIsAlreadyLinked() {
        service.save("productId123", "EAN", "code123", "customerId123");
        verify(specsStatusRepository).findAllByProductId("productId123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_creating_product.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldCreateNewProductWithBarcodeForNewType() {
        service.save("productId123", "GTN", "code123", "customerId123");
        verify(specsStatusRepository).findAllByProductId("productId123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_create_by_new_productid.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldCreateNewProductWithBarcodeForNewProduct() {
        final ProductSpecificationStatus specsStatus1 = new ProductSpecificationStatus();
        specsStatus1.setId("productId1234-en_US");
        specsStatus1.setStatusId("NEW");
        specsStatus1.setLocaleId("en_US");
        specsStatus1.setProductId("productId1234");
        when(specsStatusRepository.findAllByProductId("productId1234")) //
                .thenReturn(Lists.newArrayList(specsStatus1));
        service.save("productId1234", "EAN", "code1234", "customerId1234");
        verify(specsStatusRepository).findAllByProductId("productId1234");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_unlink.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUnlinkBarcodeFromExitingProduct() {
        service.delete("productId123", "EAN", "code12", "customerId12");
        verify(specsStatusRepository).findAllByProductId("productId123");
    }
}
