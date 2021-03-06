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

import static com.github.npathai.hamcrestopt.OptionalMatchers.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.common.collect.Maps;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

import avro.shaded.com.google.common.collect.Sets;

/**
 * This class implements repository test cases for {@link ProductBarcodeRepository}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/barcodes/barcodes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class ProductBarcodeRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ProductBarcodeRepository repository;

    @Test
    public void shouldFindBarcodeById() {
        final ProductBarcodeKey id = new ProductBarcodeKey();
        id.setType("EAN");
        id.setId("productId123-en_US");
        final Optional<ProductBarcode> barcode = repository.findById(id);
        assertThat(barcode, isPresentAnd(notNullValue()));
        assertThat(barcode.get().getId(), is("productId123-en_US"));
        assertThat(barcode.get().getProductId(), is("productId123"));
        assertThat(barcode.get().getType(), is("EAN"));
        assertThat(barcode.get().getLocaleId(), is("en_US"));
        assertThat(barcode.get().getCodes().get("customerId12"),
                containsInAnyOrder("code12", "code14"));
        assertThat(barcode.get().getCodes().get("customerId123"), contains("code123"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_create_by_new_productid.bson")
    public void shouldCreateNewBarcodeByNewProductId() {
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put("customerId1234", Sets.newHashSet("code1234"));
        final ProductBarcode barcode = new ProductBarcode();
        barcode.setId("productId1234-en_US");
        barcode.setType("EAN");
        barcode.setLocaleId("en_US");
        barcode.setProductId("productId1234");
        barcode.setCodes(codes);
        barcode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(barcode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_create_by_new_type.bson")
    public void shouldCreateNewBarcodeByNewType() {
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put("customerId1234", Sets.newHashSet("code1234"));
        final ProductBarcode barcode = new ProductBarcode();
        barcode.setId("productId123-en_US");
        barcode.setType("GTN");
        barcode.setLocaleId("en_US");
        barcode.setProductId("productId123");
        barcode.setCodes(codes);
        barcode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(barcode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_update.bson")
    public void shouldUpdateBarcode() {
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put("customerId123", Sets.newHashSet("code123"));
        final ProductBarcode barcode = new ProductBarcode();
        barcode.setId("productId123-en_US");
        barcode.setType("EAN");
        barcode.setLocaleId("en_US");
        barcode.setProductId("productId123");
        barcode.setCodes(codes);
        barcode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.save(barcode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_delete.bson")
    public void shouldDeleteBarcodeById() {
        final ProductBarcodeKey id = new ProductBarcodeKey();
        id.setType("EAN");
        id.setId("productId123-en_US");
        repository.deleteById(id);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_create_by_new_productid.bson")
    public void shouldLinkBarcode() {
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put("customerId1234", Sets.newHashSet("code1234"));
        final ProductBarcode barcode = new ProductBarcode();
        barcode.setId("productId1234-en_US");
        barcode.setType("EAN");
        barcode.setLocaleId("en_US");
        barcode.setProductId("productId1234");
        barcode.setCodes(codes);
        barcode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.link(barcode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_link_one_record.bson")
    public void shouldLinkBarcodeWithExistingProduct() {
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put("customerId1234", Sets.newHashSet("code1234"));
        final ProductBarcode barcode = new ProductBarcode();
        barcode.setId("productId123-en_US");
        barcode.setType("EAN");
        barcode.setLocaleId("en_US");
        barcode.setProductId("productId123");
        barcode.setCodes(codes);
        barcode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.link(barcode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_link_one_record_with_existing_product_and_customer.bson")
    public void shouldLinkBarcodeWithExistingProductAndCustomer() {
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put("customerId123", Sets.newHashSet("code1234"));
        final ProductBarcode barcode = new ProductBarcode();
        barcode.setId("productId123-en_US");
        barcode.setType("EAN");
        barcode.setLocaleId("en_US");
        barcode.setProductId("productId123");
        barcode.setCodes(codes);
        barcode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.link(barcode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_link_multiple_codes_for_multiple_customers.bson")
    public void shouldLinkMultipleBarcodesWithMultipleCustomersForExistingProduct() {
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put("customerId123", Sets.newHashSet("code1234", "code12345"));
        codes.put("customerId1234", Sets.newHashSet("code1", "code2"));
        final ProductBarcode barcode = new ProductBarcode();
        barcode.setId("productId123-en_US");
        barcode.setType("EAN");
        barcode.setLocaleId("en_US");
        barcode.setProductId("productId123");
        barcode.setCodes(codes);
        barcode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.link(barcode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_unlink_one_record.bson")
    public void shouldUnlinkBarcodeIfThereIsOnlyOneCodeForCustomer() {
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put("customerId123", Sets.newHashSet("code123"));
        final ProductBarcode customerCode = new ProductBarcode();
        customerCode.setId("productId123-en_US");
        customerCode.setType("EAN");
        customerCode.setLocaleId("en_US");
        customerCode.setProductId("productId123");
        customerCode.setCodes(codes);
        customerCode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.unlink(customerCode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_unlink_one_code_for_one_record.bson")
    public void shouldUnlinkOneBarcodeIfThereAreTwoCodesForCustomer() {
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put("customerId12", Sets.newHashSet("code12"));
        final ProductBarcode customerCode = new ProductBarcode();
        customerCode.setId("productId123-en_US");
        customerCode.setType("EAN");
        customerCode.setLocaleId("en_US");
        customerCode.setProductId("productId123");
        customerCode.setCodes(codes);
        customerCode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.unlink(customerCode);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_unlink_all_codes_for_one_record.bson")
    public void shouldUnlinkAllBarcodeIfThereAreTwoCodesForCustomer() {
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put("customerId12", Sets.newHashSet("code14", "code12"));
        final ProductBarcode customerCode = new ProductBarcode();
        customerCode.setId("productId123-en_US");
        customerCode.setType("EAN");
        customerCode.setLocaleId("en_US");
        customerCode.setProductId("productId123");
        customerCode.setCodes(codes);
        customerCode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.unlink(customerCode);
    }

}
