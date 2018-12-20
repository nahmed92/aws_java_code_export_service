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

/**
 * Contains {@link String} constants used for Debezium based message manipulation in {@link MessageReceiver}.
 *
 * @author Affan Hasan
 * @since 1.0
 */
public interface DebeziumMessageProperties {

    String OPERATION = "op"; // Property "op"

    String KEY = "key"; // Property "key"

    String AFTER = "after"; // Property "after"

    String ID = "_id"; //Property "_id"

    String OID = "$oid"; //Property "$oid"

    String SOURCE = "source"; //Property "source"

    //String STRING = "string"; //Property "string"

    String TS_MS = "ts_ms"; //Property "ts_ms"

    String LONG = "long"; //Property "long"

    String PRODUCT_ID = "productId"; //Property "productId"

    String CATEGORY_ID = "categoryId"; //Property "categoryId"

    String INDUSTRY_ID = "industryId"; //Property "industryId"

    String PATCH = "patch"; //Property "patch"

    String SET = "$set"; //Property "$set"

    String UPDATE_OPERATION_ADD_PRODUCT_LOCALE = "UPDATE_OPERATION_ADD_PRODUCT_LOCALE";

    String UPDATE_OPERATION_UPDATE_PRODUCT_SPECIFICATION = "UPDATE_OPERATION_UPDATE_PRODUCT_SPECIFICATION";

    String CLASS = "_class"; // Property _"_class"

    String VALUE_FIELD = "value"; // Property _"value"

    String UNIT_ATTRIBUTE = "UnitAttribute"; // Property _"UnitAttribute"

    String VALUE_ATTRIBUTE = "ValueAttribute"; // Property _"ValueAttribute"

    String EXCEPTION_ATTRIBUTE = "ExceptionAttribute"; // Property _"ExceptionAttribute"

    String EXCEPTION = "exception"; // Property _"exception"

    String UNIT = "unit"; // Property _"unit"

    String UNSET = "$unset"; // Property _"unit"

    String PUSH = "$push";

    String PULL = "$pull";

    String LOCALE_ID = "localeId";

    String OPERATION_FIELD = "operations";

    String UNIT_FIELD = "unit";

    int LOCALE_ID_INDEX = 0;

    int ATTRIBUTE_ID_INDEX = 1;

    int ELEMENT_ID_INDEX = 2;

    String REMOVE_OPERATION = "REMOVE";

    String UPDATE_OPERATION = "UPDATE";

    String ADD_TO_SET_OPERATION = "ADD_TO_SET";

    String REMOVE_FROM_SET_OPERATION = "REMOVE_FROM_SET";

    String EMPTY_INDEX_OPERATION = "EMPTY_INDEX";

    String ELEMENT = "element";

    String SPECIFICATION_ATTRIBUTES = "specificationAttributes"; // Property _"specificationAttributes"

    String ATTRIBUTE_ID = "attributeId"; // Property _"attributeId"
}
