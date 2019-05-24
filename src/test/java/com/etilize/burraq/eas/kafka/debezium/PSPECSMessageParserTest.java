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

package com.etilize.burraq.eas.kafka.debezium;

import static org.hamcrest.CoreMatchers.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.avro.generic.GenericData;
import org.junit.Test;

import com.etilize.burraq.eas.specification.UpdateProductSpecificationRequest;
import com.etilize.burraq.eas.specification.value.UnitValue;
import com.etilize.burraq.eas.specification.value.Value;

import static org.hamcrest.MatcherAssert.*;

public class PSPECSMessageParserTest {

    final private PSPECSMessageParser parser = new PSPECSMessageParser();

    @Test
    public void shouldExtractTextAttributeFromPSPECSUpdateAttributeOperation()
            throws FileNotFoundException, IOException {
        final GenericData.Record record = DebeziumMessageTestFixtures.getUpdateProductSpecsAddValueAttributeValueData();
        final UpdateProductSpecificationRequest request = parser.getUpdateSpecificationRequest(
                "ppsh", record);
        assertThat(request.getProductId(), is("ppsh"));
        assertThat(request.getLocaleId(), is("en_US"));
        assertThat(request.getUpdatedAttributes() //
                .size(), is(1));
        assertThat(request.getUpdatedAttributes() //
                .get("1"), instanceOf(Value.class));
        assertThat(request.getUpdatedAttributes() //
                .get("1"), is(new Value<>("New Attribute")));
    }

    @Test
    public void shouldExtractUnitAttributeFromPSPECSUpdateAttributeOperation()
            throws FileNotFoundException, IOException {
        final GenericData.Record record = DebeziumMessageTestFixtures.getUpdateProductSpecsAddUnitValueAttributeValueData();
        final UpdateProductSpecificationRequest request = parser.getUpdateSpecificationRequest(
                "ppsh", record);
        assertThat(request.getProductId(), is("ppsh"));
        assertThat(request.getLocaleId(), is("en_US"));
        assertThat(request.getUpdatedAttributes() //
                .size(), is(1));
        assertThat(request.getUpdatedAttributes() //
                .get("30"), instanceOf(UnitValue.class));
        final UnitValue unitValue = (UnitValue) request.getUpdatedAttributes() //
                .get("30");
        assertThat(unitValue.getValue() //
                .get("EXTRACTED") //
                .getUnit(), is("GB"));
        assertThat(unitValue.getValue() //
                .get("EXTRACTED") //
                .getValue() //
                .intValue(), is(3));
        assertThat(unitValue.getValue() //
                .get("BASE") //
                .getUnit(), is("bytes"));
        assertThat(unitValue.getValue() //
                .get("BASE") //
                .getValue() //
                .intValue(), is(30072));
    }

    @Test
    public void shouldExtractExceptionCodeFromPSPECS()
            throws FileNotFoundException, IOException {
        final GenericData.Record record = DebeziumMessageTestFixtures.getUpdateProductSpecsAddExceptionAttributeValueData();
        final UpdateProductSpecificationRequest request = parser.getUpdateSpecificationRequest(
                "ppsh", record);
        assertThat(request.getProductId(), is("ppsh"));
        assertThat(request.getLocaleId(), is("en_US"));
        assertThat(request.getRemovedAttributeIds() //
                .size(), is(1));
        assertThat(request.getRemovedAttributeIds() //
                .iterator() //
                .next(), is("DNA"));
    }

    @Test
    public void shouldExtractRemovedTextAttributeIdFromPSPECSRemoveAttributeOperation()
            throws FileNotFoundException, IOException {
        final GenericData.Record record = DebeziumMessageTestFixtures.getUpdateProductSpecsRemoveTextAttributeData();
        final UpdateProductSpecificationRequest request = parser.getUpdateSpecificationRequest(
                "ppsh", record);
        assertThat(request.getProductId(), is("ppsh"));
        assertThat(request.getLocaleId(), is("en_US"));
        assertThat(request.getRemovedAttributeIds() //
                .size(), is(2));
        final Iterator<String> it = request.getRemovedAttributeIds() //
                .iterator();
        while (it.hasNext()) {
            final String item = it.next();
            assertThat(item, anyOf(is("mfgPartNoId"), is("mfgId")));
        }
    }

    @Test
    public void shouldExtractRemovedUnitAttributeIdFromPSPECSRemoveAttributeOperation()
            throws FileNotFoundException, IOException {
        final GenericData.Record record = DebeziumMessageTestFixtures.getUpdateProductSpecsRemoveAttributeData();
        final UpdateProductSpecificationRequest request = parser.getUpdateSpecificationRequest(
                "ppsh", record);
        assertThat(request.getProductId(), is("ppsh"));
        assertThat(request.getLocaleId(), is("en_US"));
        assertThat(request.getRemovedAttributeIds() //
                .size(), is(1));
        assertThat(request.getRemovedAttributeIds() //
                .iterator() //
                .next(), is("30"));
    }

    @Test
    public void shouldExtractRepeatableTextAttributeFromPSPECSUpdateAttributeOperation()
            throws FileNotFoundException, IOException {
        final GenericData.Record record = DebeziumMessageTestFixtures.getUpdateProductSpecsAddRepatableValueAttributeValueData();
        final UpdateProductSpecificationRequest request = parser.getUpdateSpecificationRequest(
                "ppsh", record);
        assertThat(request.getProductId(), is("ppsh"));
        assertThat(request.getLocaleId(), is("en_US"));
        assertThat(request.getUpdatedAttributes() //
                .size(), is(1));
        assertThat(request.getUpdatedAttributes() //
                .get("888"), instanceOf(List.class));
        @SuppressWarnings("unchecked")
        final List<Value<String>> valueAttributes = (List<Value<String>>) request.getUpdatedAttributes() //
                .get("888");
        assertThat(valueAttributes.size(), is(1));
        assertThat(valueAttributes.get(0) //
                .getValue(), is("blue"));
    }

    @Test
    public void shouldExtractRepeatableTextAttributeFromPSPECSAddToSetOperation()
            throws FileNotFoundException, IOException {
        final GenericData.Record record = DebeziumMessageTestFixtures.getUpdateProductSpecsAddRepatable2ndValueAttributeValueData();
        final UpdateProductSpecificationRequest request = parser.getUpdateSpecificationRequest(
                "ppsh", record);
        assertThat(request.getProductId(), is("ppsh"));
        assertThat(request.getLocaleId(), is("en_US"));
        assertThat(request.getAddedToSetAttributes() //
                .size(), is(1));
        assertThat(request.getAddedToSetAttributes() //
                .get("888"), instanceOf(Value.class));
        assertThat(request.getAddedToSetAttributes() //
                .get("888"), is(new Value<String>("Green")));
    }

    @Test
    public void shouldExtractRepeatableUnitAttributeFromPSPECS()
            throws FileNotFoundException, IOException {
        final GenericData.Record record = DebeziumMessageTestFixtures.getUpdateProductSpecsAddRepatableUnitAttributeValueData();
        final UpdateProductSpecificationRequest request = parser.getUpdateSpecificationRequest(
                "ppsh", record);
        assertThat(request.getProductId(), is("ppsh"));
        assertThat(request.getLocaleId(), is("en_US"));
        assertThat(request.getUpdatedAttributes() //
                .get("777"), instanceOf(List.class));
        @SuppressWarnings("unchecked")
        final List<UnitValue> unitArray = (List<UnitValue>) request.getUpdatedAttributes() //
                .get("777");
        unitArray.forEach(unit -> {
            assertThat(unit.getValue() //
                    .get("EXTRACTED") //
                    .getValue() //
                    .toString(), anyOf(is("3"), is("1.5")));
        });
        unitArray.forEach(unit -> {
            assertThat(unit.getValue() //
                    .get("BASE") //
                    .getValue() //
                    .toString(), anyOf(is("30072"), is("1500")));
        });
        unitArray.forEach(unit -> {
            assertThat(unit.getValue() //
                    .get("EXTRACTED") //
                    .getUnit(), anyOf(is("GB"), is("kg")));
        });
        unitArray.forEach(unit -> {
            assertThat(unit.getValue() //
                    .get("BASE") //
                    .getUnit(), anyOf(is("bytes"), is("g")));
        });
    }

    @Test
    public void shouldExtractRepeatableValueAttributeFromPSPECSRemoveFromSetOperation()
            throws FileNotFoundException, IOException {
        final GenericData.Record record = DebeziumMessageTestFixtures.getProductSpecsRemoveValueAttributeFromSetData();
        final UpdateProductSpecificationRequest request = parser.getUpdateSpecificationRequest(
                "ppsh", record);
        assertThat(request.getProductId(), is("ppsh"));
        assertThat(request.getLocaleId(), is("en_US"));
        assertThat(request.getUpdatedAttributes() //
                .get("888"), instanceOf(List.class));
        @SuppressWarnings("unchecked")
        final List<Value<String>> valueArray = (List<Value<String>>) request.getUpdatedAttributes() //
                .get("888");
        assertThat(valueArray.size(), is(1));
        assertThat(valueArray.get(0) //
                .getValue(), is("blue"));
    }

    @Test
    public void shouldExtractRepeatableUnitAttributeFromPSPECSRemoveFromSetOperation()
            throws FileNotFoundException, IOException {
        final GenericData.Record record = DebeziumMessageTestFixtures.getProductSpecsRemoveUnitAttributeFromSetData();
        final UpdateProductSpecificationRequest request = parser.getUpdateSpecificationRequest(
                "ppsh", record);
        assertThat(request.getProductId(), is("ppsh"));
        assertThat(request.getLocaleId(), is("en_US"));
        assertThat(request.getUpdatedAttributes() //
                .get("777"), instanceOf(List.class));
        @SuppressWarnings("unchecked")
        final List<UnitValue> unitArray = (List<UnitValue>) request.getUpdatedAttributes() //
                .get("777");
        unitArray.forEach(unit -> {
            assertThat(unit.getValue() //
                    .get("EXTRACTED") //
                    .getValue() //
                    .toString(), is("3"));
        });
        unitArray.forEach(unit -> {
            assertThat(unit.getValue() //
                    .get("BASE") //
                    .getValue() //
                    .toString(), is("30072"));
        });
        unitArray.forEach(unit -> {
            assertThat(unit.getValue() //
                    .get("EXTRACTED") //
                    .getUnit(), is("GB"));
        });
        unitArray.forEach(unit -> {
            assertThat(unit.getValue() //
                    .get("BASE") //
                    .getUnit(), is("bytes"));
        });
    }
}
