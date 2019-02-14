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

package com.etilize.burraq.eas.utils;

/**
 * It contains utility methods.
 * @author Umar Zubair
 *
 */
public final class Utils {

    private static final String PRODUCTID_LOCALE = "%s-%s";

    /**
     * It is required for sonar.
     */
    private Utils() {
    }

    /**
     * It returns productId-localeId whoch is used as id
     * @param productId product id
     * @param localeId locale id
     * @return productId-localeId
     */
    public static String generateId(final String productId, final String localeId) {
        return String.format(PRODUCTID_LOCALE, productId, localeId);
    }

    /**
     * Returns "#"+key
     *
     * @param key field name
     * @return "#"+key
     */
    public static String getKeyName(final String key) {
        return "#" + key;
    }

    /**
     * Returns ":"+key
     *
     * @param key field name
     * @return ":"+key
     */
    public static String getValueName(final String key) {
        return ":" + key;
    }

    /**
     * Use String.format considering query referring key twice. It is used for cases like query is "#%s :%s" or "#%s=:%s"
     *
     * @param query string to format
     * @param key field name
     * @return "#"+key
     */
    public static String getKeyValueUse(final String query, final String key) {
        return String.format(query, key, key);
    }
}
