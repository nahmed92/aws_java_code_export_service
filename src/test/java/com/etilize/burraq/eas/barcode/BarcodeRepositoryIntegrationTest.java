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
 * This class implements repository test cases for {@link BarcodeRepository}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/barcodes/barcodes.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class BarcodeRepositoryIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private BarcodeRepository repository;

    @Test
    public void shouldFindBarcodeById() {
        final BarcodeId id = new BarcodeId();
        id.setType("EAN");
        id.setId("productId123-en_US");
        final Optional<Barcode> barcode = repository.findById(id);
        assertThat(barcode, isPresentAnd(notNullValue()));
        assertThat(barcode.get().getId(), is("productId123-en_US"));
        assertThat(barcode.get().getProductId(), is("productId123"));
        assertThat(barcode.get().getType(), is("EAN"));
        assertThat(barcode.get().getLocaleId(), is("en_US"));
        assertThat(barcode.get().getCodes().get("customerId12"), is("code12"));
        assertThat(barcode.get().getCodes().get("customerId123"), is("code123"));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_create_by_new_productid.bson")
    public void shouldCreateNewBarcodeByNewProductId() {
        final Map<String, String> codes = Maps.newHashMap();
        codes.put("customerId1234", "code1234");
        final Barcode barcode = new Barcode();
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
        final Map<String, String> codes = Maps.newHashMap();
        codes.put("customerId1234", "code1234");
        final Barcode barcode = new Barcode();
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
        final Map<String, String> codes = Maps.newHashMap();
        codes.put("customerId123", "code123");
        final Barcode barcode = new Barcode();
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
        final BarcodeId id = new BarcodeId();
        id.setType("EAN");
        id.setId("productId123-en_US");
        repository.deleteById(id);
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/barcodes/barcodes_after_create_by_new_productid.bson")
    public void shouldLinkBarcode() {
        final Map<String, String> codes = Maps.newHashMap();
        codes.put("customerId1234", "code1234");
        final Barcode barcode = new Barcode();
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
        final Map<String, String> codes = Maps.newHashMap();
        codes.put("customerId1234", "code1234");
        final Barcode barcode = new Barcode();
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
    public void shouldUnlinkBarcodeById() {
        final Map<String, String> codes = Maps.newHashMap();
        codes.put("customerId123", "code123");
        final Barcode customerCode = new Barcode();
        customerCode.setId("productId123-en_US");
        customerCode.setType("EAN");
        customerCode.setLocaleId("en_US");
        customerCode.setProductId("productId123");
        customerCode.setCodes(codes);
        customerCode.setLastUpdateDate(new Date(Long.valueOf("1546528059097")));
        repository.unlink(customerCode);
    }
}
