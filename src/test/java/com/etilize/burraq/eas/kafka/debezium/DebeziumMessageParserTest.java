/*
 * #region
 * export-aggregation-service
 * %%
 * Copyright (C) 2018 Etilize
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

import static com.etilize.burraq.eas.kafka.debezium.DebeziumMessageKeys.*;
import static com.etilize.burraq.eas.kafka.debezium.DebeziumMessageProperties.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

/**
 * Contains functional test(s) for {@link DebeziumMessageParser}.
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class DebeziumMessageParserTest {

    private DebeziumMessageParser parser;

    @Before
    public void init() {
        parser = new DebeziumMessageParser();
    }

    @Test
    public void shouldExtractProductLocaleFromAddLocaleCommand() throws IOException {
        final Optional<String> localeOptional = parser.extractProductLocaleFromAddLocaleCommand(
                DebeziumMessageTestFixtures.getAddProductLocaleCommandValueData());
        assertThat(localeOptional.isPresent(), is(true));
        assertThat(localeOptional.get(), is("de_DE"));
    }

    @Test
    public void shouldExtractOperationTypeUpdateForAddProductLocaleCommand()
            throws FileNotFoundException, IOException {
        final Optional<String> operationOptional = parser.extractOperationType(
                DebeziumMessageTestFixtures.getAddProductLocaleCommandValueData());
        assertThat(operationOptional.isPresent(), is(true));
        assertThat(operationOptional.get(), is(OPERATION_UPDATE));
    }

    @Test
    public void shouldExtractUpdateOperationTypeAsAddProductLocale()
            throws FileNotFoundException, IOException {
        final Optional<String> operationTypeOptional = parser.extractUpdateOperationType(
                DebeziumMessageTestFixtures.getAddProductLocaleCommandValueData());
        assertThat(operationTypeOptional.isPresent(), is(true));
        assertThat(operationTypeOptional.get(), is(UPDATE_OPERATION_ADD_PRODUCT_LOCALE));
    }

    @Test
    public void shouldExtractUpdateOperationTypeAsUpdateProductSpecification()
            throws FileNotFoundException, IOException {
        final Optional<String> operationOptional = parser.extractUpdateOperationType(
                DebeziumMessageTestFixtures.getUpdateProductSpecsAddValueAttributeValueData());
        assertThat(operationOptional.isPresent(), is(true));
        assertThat(operationOptional.get(),
                is(UPDATE_OPERATION_UPDATE_PRODUCT_SPECIFICATION));
    }

    @Test
    public void shouldExtractValueAttributeFromUpdateProductSpecsCommand()
            throws FileNotFoundException, IOException {
        final Queue<SpecificationUpdateOperation> operations = parser.parseToUpdateOperationMap(
                "a2ffsbc23",
                DebeziumMessageTestFixtures.getUpdateProductSpecsAddValueAttributeValueData());
        //THEN
        final SpecificationUpdateOperation specUpdateOperation = operations.poll();
        final Map<String, Object> params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        assertThat(specUpdateOperation.getDocId(), is("a2ffsbc23-de_DE"));
        assertThat(specUpdateOperation.getOperationType(), is(UPDATE_OPERATION));
        assertThat(params.get(KEY).toString(), is("9"));
        assertThat(params.get(VALUE_FIELD).toString(), is("Red"));
    }

    @Test
    public void shouldExtractUnitAttributeFromUpdateProductSpecsCommand()
            throws FileNotFoundException, IOException {
        final Queue<SpecificationUpdateOperation> operations = parser.parseToUpdateOperationMap(
                "d2f2fa",
                DebeziumMessageTestFixtures.getUpdateProductSpecsAddUnitValueAttributeValueData());
        //THEN
        final SpecificationUpdateOperation specUpdateOperation = operations.poll();
        final Map<String, Object> params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        assertThat(specUpdateOperation.getDocId(), is("d2f2fa-de_DE"));
        assertThat(specUpdateOperation.getOperationType(), is(UPDATE_OPERATION));
        assertThat(params.get(KEY).toString(), is("2"));
        //unit as a value for indexing
        assertThat(params.get(VALUE_FIELD).toString(), is("kg"));
    }

    @Test
    public void shouldExtractExceptionAttributeFromUpdateProductSpecsCommand()
            throws FileNotFoundException, IOException {
        final Queue<SpecificationUpdateOperation> operations = parser.parseToUpdateOperationMap(
                "df1-456",
                DebeziumMessageTestFixtures.getUpdateProductSpecsAddExceptionAttributeValueData());
        //THEN
        final SpecificationUpdateOperation specUpdateOperation = operations.poll();
        final Map<String, Object> params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        assertThat(specUpdateOperation.getDocId(), is("df1-456-de_DE"));
        //Exception will execute the remove index operation
        assertThat(params.get("operations"), is(REMOVE_OPERATION));
        assertThat(params.get(KEY).toString(), is("66"));
    }

    @Test
    public void shouldExtractRepeatableValueAttributeFromUpdateProductSpecsCommand()
            throws FileNotFoundException, IOException {
        final Queue<SpecificationUpdateOperation> operations = parser.parseToUpdateOperationMap(
                "abc-456",
                DebeziumMessageTestFixtures.getUpdateProductSpecsAddRepatableValueAttributeValueData());
        //THEN
        final SpecificationUpdateOperation specUpdateOperation = operations.poll();
        final Map<String, Object> params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        assertThat(specUpdateOperation.getDocId(), is("abc-456-de_DE"));
        assertThat(specUpdateOperation.getOperationType(), is(UPDATE_OPERATION));
        assertThat(params.get(KEY).toString(), is("3"));
        assertThat(params.get(VALUE_FIELD).toString(), is("[green, red]"));

    }

    @Test
    public void shouldExtractRepeatableUnitAttributeFromUpdateProductSpecsCommand()
            throws FileNotFoundException, IOException {
        final Queue<SpecificationUpdateOperation> operations = parser.parseToUpdateOperationMap(
                "aw2erc-456",
                DebeziumMessageTestFixtures.getUpdateProductSpecsAddRepatableUnitAttributeValueData());
        //THEN
        final SpecificationUpdateOperation specUpdateOperation = operations.poll();
        final Map<String, Object> params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        assertThat(specUpdateOperation.getDocId(), is("aw2erc-456-en_US"));
        assertThat(specUpdateOperation.getOperationType(), is(UPDATE_OPERATION));
        assertThat(params.get(KEY).toString(), is("5"));
        //unit as a value for indexing
        assertThat(params.get(VALUE_FIELD).toString(), is("[kg, kg]"));
    }

    @Test
    public void shouldReturnOperationTypeAsRemoveAttribute()
            throws FileNotFoundException, IOException {
        final Queue<SpecificationUpdateOperation> operations = parser.parseToUpdateOperationMap(
                "ffabc456",
                DebeziumMessageTestFixtures.getUpdateProductSpecsRemoveAttributeData());
        //THEN
        final SpecificationUpdateOperation specUpdateOperation = operations.poll();
        final Map<String, Object> params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        assertThat(specUpdateOperation.getDocId(), is("ffabc456-de_DE"));
        // Remove index operation
        assertThat(specUpdateOperation.getOperationType(), is(REMOVE_OPERATION));
        assertThat(params.get(KEY).toString(), is("4"));
    }

    @Test
    public void shouldReturnOperationTypeAsAddValueAttributeToSet()
            throws FileNotFoundException, IOException {
        final Queue<SpecificationUpdateOperation> operations = parser.parseToUpdateOperationMap(
                "cqqc331q3f",
                DebeziumMessageTestFixtures.getUpdateProductSpecsAddValueAttributeToSetData());
        //THEN
        final SpecificationUpdateOperation specUpdateOperation = operations.poll();
        final Map<String, Object> params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        assertThat(specUpdateOperation.getDocId(), is("cqqc331q3f-en_US"));
        // Remove index operation
        assertThat(specUpdateOperation.getOperationType(), is(UPDATE_OPERATION));
        assertThat(params.get(KEY).toString(), is("3"));
        assertThat(params.get(VALUE_FIELD).toString(), is("orange"));
    }

    @Test
    public void shouldExtractUnitAttributeUponAddingToSet()
            throws FileNotFoundException, IOException {
        final Queue<SpecificationUpdateOperation> operations = parser.parseToUpdateOperationMap(
                "sfbc-456",
                DebeziumMessageTestFixtures.getUpdateProductSpecsAddUnitAttributeToSetData());
        //THEN
        final SpecificationUpdateOperation specUpdateOperation = operations.poll();
        final Map<String, Object> params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        assertThat(specUpdateOperation.getDocId(), is("sfbc-456-en_US"));
        // Remove index operation
        assertThat(specUpdateOperation.getOperationType(), is(UPDATE_OPERATION));
        assertThat(params.get(KEY).toString(), is("3"));
        assertThat(params.get(VALUE_FIELD).toString(), is("kg"));
    }

    @Test
    public void shouldReturnOperationTypeAsUpdateValueAttributeUponRemovingValueFromSet()
            throws FileNotFoundException, IOException {
        final Queue<SpecificationUpdateOperation> operations = parser.parseToUpdateOperationMap(
                "adv-456",
                DebeziumMessageTestFixtures.getProductSpecsRemoveValueAttributeFromSetData());
        //THEN
        final SpecificationUpdateOperation specUpdateOperation = operations.poll();
        final Map<String, Object> params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        assertThat(specUpdateOperation.getDocId(), is("adv-456-de_DE"));
        // Remove index operation
        assertThat(specUpdateOperation.getOperationType(), is(UPDATE_OPERATION));
        assertThat(params.get(KEY).toString(), is("3"));
        assertThat(params.get(VALUE_FIELD).toString(), is("[green]"));
    }

    @Test
    public void shouldReturnOperationTypeAsUpdateUnitAttributeUponRemovingUnitFromSet()
            throws FileNotFoundException, IOException {
        final Queue<SpecificationUpdateOperation> operations = parser.parseToUpdateOperationMap(
                "azf-456",
                DebeziumMessageTestFixtures.getProductSpecsRemoveUnitAttributeFromSetData());
        //THEN
        final SpecificationUpdateOperation specUpdateOperation = operations.poll();
        final Map<String, Object> params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        assertThat(specUpdateOperation.getDocId(), is("azf-456-en_US"));
        // Remove index operation
        assertThat(specUpdateOperation.getOperationType(), is(UPDATE_OPERATION));
        assertThat(params.get(KEY).toString(), is("1"));
        assertThat(params.get(VALUE_FIELD).toString(), is("[kg, kg]"));

    }

    @Test
    public void shouldExtractMultipleOperationsFromUpdateProductSpecsCommand()
            throws FileNotFoundException, IOException {
        final Queue<SpecificationUpdateOperation> operations = (Queue<SpecificationUpdateOperation>) parser.parseToUpdateOperationMap(
                "multiple-operations-456",
                DebeziumMessageTestFixtures.getMultipleOperationsProductSpecsValueData());

        // First set operation for UPDATE
        SpecificationUpdateOperation specUpdateOperation = operations.poll();
        // First operation's first record for remove
        Map<String, Object> params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        //THEN
        assertThat(specUpdateOperation.getDocId(), is("multiple-operations-456-en_US"));
        assertThat(specUpdateOperation.getOperationType(), is(UPDATE_OPERATION));
        assertThat(params.get(KEY).toString(), is("6"));
        //unit as a value for indexing
        assertThat(params.get("operations"), is(REMOVE_OPERATION));

        // Second push operation for ADD_TO_SET_
        specUpdateOperation = operations.poll();
        // Second operation's first record for  ADD_TO_SET_
        params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        //THEN
        assertThat(specUpdateOperation.getDocId(), is("multiple-operations-456-en_US"));
        assertThat(specUpdateOperation.getOperationType(), is(ADD_TO_SET_OPERATION));
        assertThat(params.get(KEY).toString(), is("20"));
        //unit as a value for indexing
        assertThat(params.get("operations"), is(ADD_TO_SET_OPERATION));

        // Third unset operation for REMOVE_
        specUpdateOperation = operations.poll();
        // Third operation's first record for  REMOVE_
        params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        //THEN
        assertThat(specUpdateOperation.getDocId(), is("multiple-operations-456-en_US"));
        assertThat(specUpdateOperation.getOperationType(), is(REMOVE_OPERATION));
        assertThat(params.get(KEY).toString(), is("11"));

        // Forth pull operation for REMOVE_FROM_SET
        specUpdateOperation = operations.poll();
        // Forth operation's first record for  REMOVE_
        params = specUpdateOperation.getQueryParams().poll();
        assertThat(params.isEmpty(), is(false));
        //THEN
        assertThat(specUpdateOperation.getDocId(), is("multiple-operations-456-en_US"));
        assertThat(specUpdateOperation.getOperationType(), is(REMOVE_FROM_SET_OPERATION));
        assertThat(params.get(KEY).toString(), is("20"));
        //unit as a value for indexing
        assertThat(params.get("operations"), is(REMOVE_FROM_SET_OPERATION));
    }
}
