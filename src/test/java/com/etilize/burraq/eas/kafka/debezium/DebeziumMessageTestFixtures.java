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

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.util.ResourceUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Contains factory methods to create Debezium message objects for testing.
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class DebeziumMessageTestFixtures {

    private static final String DATA_SET_PATH = "classpath:datasets";

    /**
     * Returns an instance of {@link GenericData.Record} containing Debezium based Add Industry Message.
     *
     * @return {@link GenericData.Record}
     * @throws IOException
     */
    public static GenericData.Record getAddIndustryMessage() throws IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/add_industry_debezium_message_json_schema.json"),
                "UTF-8");
        final String debeziumMessageJsonObject = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/add_industry_debezium_message_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumMessageJsonObject).getAsJsonObject();
        final GenericData.Record message = new GenericData.Record(schema);
        message.put(OPERATION, OPERATION_CREATE);
        message.put(AFTER, object.get(AFTER).getAsString());
        message.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        message.put(TS_MS, object.get(TS_MS).getAsJsonObject().get(LONG).getAsLong());
        return message;
    }

    /**
     * Returns an instance of {@link GenericData.Record} containing Debezium based Create Product Message.
     *
     * @return {@link GenericData.Record}
     * @throws IOException
     */
    public static GenericData.Record getCreateProductCommandData() throws IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumMessageJsonObject = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/create_product_command_debezium_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumMessageJsonObject).getAsJsonObject();
        final GenericData.Record message = new GenericData.Record(schema);
        message.put(OPERATION, OPERATION_CREATE);
        message.put(AFTER, object.get(AFTER).getAsString());
        message.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        message.put(TS_MS, object.get(TS_MS).getAsJsonObject().get(LONG).getAsLong());
        return message;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getAddProductLocaleCommandValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumAddLocaleCommandJsonObject = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/add_product_locale_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumAddLocaleCommandJsonObject).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH).getAsString());
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsLong());
        return command;
    }

    /**
     * Returns {@link ConsumerRecord<String, String>} containing the mongodb object key.
     *
     * @return {@link ConsumerRecord<String, String>}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static ConsumerRecord<Object, String> getSpecificationUpdateCommandKeyData()
            throws FileNotFoundException, IOException {

        final String debeziumMessageKeyJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_key_json_schema.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageKeyJsonSchema);
        final GenericData.Record command = new GenericData.Record(schema);
        command.put("id", "123");
        return new ConsumerRecord<>(
                "burraq.product-specifications-service.specifications", 0, 0, command,
                null);
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateProductSpecsAddValueAttributeValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_add_text_attribute_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateProductSpecsAddUnitValueAttributeValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_add_unit_attribute_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateProductSpecsAddExceptionAttributeValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_add_exception_attribute_to_set_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateProductSpecsAddRepatableValueAttributeValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_add_repeatable_text_attribute_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateProductSpecsAddRepatable2ndValueAttributeValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_add_repeatable_2nd_text_attribute_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateProductSpecsAddRepatableUnitAttributeValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_add_repeatable_unit_attribute_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateProductSpecsRemoveAttributeData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_remove_attribute_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateProductSpecsAddValueAttributeToSetData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_add_text_attribute_to_set_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateProductSpecsAddUnitAttributeToSetData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_add_unit_attribute_to_set_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getProductSpecsRemoveValueAttributeFromSetData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_remove_value_attribute_from_set_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getProductSpecsRemoveUnitAttributeFromSetData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_remove_unit_attribute_from_set_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link ConsumerRecord<Object, String>} for category service Debezium messages key.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static ConsumerRecord<Object, String> getCreateCategoryCommandKeyData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageKeyJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_key_json_schema.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageKeyJsonSchema);
        final GenericData.Record command = new GenericData.Record(schema);
        command.put("id", "5bbb1a0d184fb72294a870d0");
        return new ConsumerRecord<>("burraq.category-service.categories", 0, 0, command,
                null);
    }

    /**
     * Returns an {@link GenericData.Record} for create category Debezium value object when specification attributes are empty.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getCreateCategoryCommandWithOutAttributesValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(

                        DATA_SET_PATH + "/category_create_value_schema.json"),
                "UTF-8");
        final String debeziumAddLocaleCommandJsonObject = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/category_create_without_attributes_value_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumAddLocaleCommandJsonObject).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_CREATE);
        command.put(AFTER, object.get(AFTER).getAsString());
        command.put(PATCH, null);
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsLong());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} for create category Debezium value object when specification attributes are present.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getCreateCategoryCommandWithAttributesValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(

                        DATA_SET_PATH + "/category_create_value_schema.json"),
                "UTF-8");
        final String debeziumAddLocaleCommandJsonObject = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/category_create_with_attributes_value_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumAddLocaleCommandJsonObject).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_CREATE);
        command.put(AFTER, object.get(AFTER).getAsString());
        command.put(PATCH, null);
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsLong());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} for create category Debezium value object when specification attributes are not present.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateCategoryCommandWithOutAttributesValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(

                        DATA_SET_PATH + "/category_create_value_schema.json"),
                "UTF-8");
        final String debeziumAddLocaleCommandJsonObject = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/category_update_without_attributes_value_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumAddLocaleCommandJsonObject).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH).getAsString());
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsLong());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} for create category Debezium value object when specification attributes are present.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateCategoryCommandWithAttributesValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(

                        DATA_SET_PATH + "/category_create_value_schema.json"),
                "UTF-8");
        final String debeziumAddLocaleCommandJsonObject = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/category_update_with_attributes_value_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumAddLocaleCommandJsonObject).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH).getAsString());
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsLong());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getUpdateProductSpecsAddAttributeWithoutMapping()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/update_spec_add_attribute_without_mapping_command_debezium_value_json_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an {@link GenericData.Record} instance initialized with default values.
     *
     * @return {@link GenericData.Record}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static GenericData.Record getMultipleOperationsProductSpecsValueData()
            throws FileNotFoundException, IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pspecs_debezium_messages_json_schema.json"),
                "UTF-8");
        final String debeziumUpdateSpecsAddAttributeCommand = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/multiple-operations_command_debezium_value.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumUpdateSpecsAddAttributeCommand).getAsJsonObject();
        final GenericData.Record command = new GenericData.Record(schema);
        command.put(OPERATION, OPERATION_UPDATE);
        command.put(AFTER, null);
        command.put(PATCH, object.get(PATCH));
        command.put(SOURCE, object.get(SOURCE).getAsJsonObject());
        command.put(TS_MS, object.get(TS_MS).getAsJsonObject());
        return command;
    }

    /**
     * Returns an instance of {@link GenericData.Record} which represents PMS AddProductLocaleMessage.
     *
     * @return {@link GenericData.Record}
     * @throws IOException
     */
    public static GenericData.Record getPMSAddLocaleMessageValueObject()
            throws IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pms_debezium_messages_schema.json"),
                "UTF-8");
        final String debeziumMessageJsonObject = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pms_add_locale_message_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumMessageJsonObject).getAsJsonObject();
        final GenericData.Record message = new GenericData.Record(schema);
        message.put(OPERATION, object.get(OPERATION) //
                .getAsString());
        message.put(PATCH, object.get(PATCH) //
                .getAsString());
        message.put(AFTER, object.get(AFTER) //
                .getAsJsonNull());
        message.put(SOURCE, object.get(SOURCE) //
                .getAsJsonObject());
        message.put(TS_MS, object.get(TS_MS) //
                .getAsLong());
        return message;
    }

    /**
     * Returns {@link ConsumerRecord<String, String>} containing the mongodb object key.
     *
     * @return {@link ConsumerRecord<String, String>}
     * @throws FileNotFoundException in case of error during file reading.
     * @throws IOException in case of IO errors during file reading.
     */
    public static ConsumerRecord<Object, String> getPMSDebeziumMessagesKeyObject()
            throws FileNotFoundException, IOException {

        final String debeziumMessageKeyJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pms_debezium_messages_key_schema.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageKeyJsonSchema);
        final GenericData.Record command = new GenericData.Record(schema);
        command.put("_id", "{\"_id\" : \"mp5\"}");
        return new ConsumerRecord<>("burraq.product-media-service.media_specifications",
                0, 0, command, null);
    }

    /**
     * Returns an instance of {@link GenericData.Record} which represents PMS ProductMediaEvent with status ASSOCIATED and a URL.
     *
     * @return {@link GenericData.Record}
     * @throws IOException
     */
    public static GenericData.Record getPMSProductMediaEventValueObjectWithURLAndStatusAssociated()
            throws IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pms_debezium_messages_schema.json"),
                "UTF-8");
        final String debeziumMessageJsonObject = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/pms_product_media_event_status_with_status_associated_and_url_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumMessageJsonObject).getAsJsonObject();
        final GenericData.Record message = new GenericData.Record(schema);
        message.put(OPERATION, object.get(OPERATION) //
                .getAsString());
        message.put(PATCH, object.get(PATCH) //
                .getAsString());
        message.put(AFTER, object.get(AFTER) //
                .getAsJsonNull());
        message.put(SOURCE, object.get(SOURCE) //
                .getAsJsonObject());
        message.put(TS_MS, object.get(TS_MS) //
                .getAsLong());
        return message;
    }

    /**
     * Returns an instance of {@link GenericData.Record} which represents PMS ProductMediaEvent with status PENDING and URL as null.
     *
     * @return {@link GenericData.Record}
     * @throws IOException
     */
    public static GenericData.Record getPMSProductMediaEventValueObjectWithoutURLAndStatusPending()
            throws IOException {
        final String debeziumMessageJsonSchema = FileUtils.readFileToString(
                ResourceUtils.getFile(
                        DATA_SET_PATH + "/pms_debezium_messages_schema.json"),
                "UTF-8");
        final String debeziumMessageJsonObject = FileUtils.readFileToString(
                ResourceUtils.getFile(DATA_SET_PATH
                        + "/pms_product_media_event_status_with_status_pending_object.json"),
                "UTF-8");
        final Schema schema = new Schema.Parser().parse(debeziumMessageJsonSchema);
        final JsonObject object = new JsonParser().parse(
                debeziumMessageJsonObject).getAsJsonObject();
        final GenericData.Record message = new GenericData.Record(schema);
        message.put(OPERATION, object.get(OPERATION) //
                .getAsString());
        message.put(PATCH, object.get(PATCH) //
                .getAsString());
        message.put(AFTER, object.get(AFTER) //
                .getAsJsonNull());
        message.put(SOURCE, object.get(SOURCE) //
                .getAsJsonObject());
        message.put(TS_MS, object.get(TS_MS) //
                .getAsLong());
        return message;
    }
}
