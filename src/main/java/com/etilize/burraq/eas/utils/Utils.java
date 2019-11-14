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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.Link;

import com.etilize.burraq.eas.specification.value.SpecificationValue;
import com.etilize.burraq.eas.specification.value.UnitAttribute;
import com.etilize.burraq.eas.specification.value.UnitValue;
import com.etilize.burraq.eas.specification.value.Value;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
     * parse self link and returns id
     *
     * @param selfLink the self link of the resource
     * @return id
     */
    public static String getIdFromSelfLink(final Link selfLink) {
        return StringUtils.substringAfterLast(selfLink.getHref(), "/");
    }

    /**
     * Return market
     *
     * @param localeId locale id
     * @return market
     */
    public static String getMarketFromLocaleId(final String localeId) {
        return StringUtils.substringAfterLast(localeId, "_");
    }

    /**
     * Return language
     *
     * @param localeId locale id
     * @return language
     */
    public static String getLanguageFromLocaleId(final String localeId) {
        return StringUtils.substringBefore(localeId, "_");
    }

    /**
     * It returns productId-localeId whoch is used as id
     *
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

    /**
     * It returns String,Number,Set of String, Set of Number, Map or List of Map
     *
     * @param opValue Object value for attribute
     * @return Object
     */
    public static Object convertOperationToValue(final Object opValue) {
        Object convertedValue = null;
        if (opValue instanceof Object[] || opValue instanceof ArrayList
                || opValue instanceof Set) {
            Object[] multiObjects = null;
            if (opValue instanceof ArrayList) {
                multiObjects = ((ArrayList) opValue).toArray();
            } else if (opValue instanceof Set) {
                multiObjects = ((Set) opValue).toArray();
            } else {
                multiObjects = (Object[]) opValue;
            }
            final List<Object> values = Lists.newArrayList();
            boolean useList = false;
            for (final Object obj : multiObjects) {
                if ((SpecificationValue) obj instanceof UnitValue) {
                    useList = true;
                }
                values.add(getSpecificationValue((SpecificationValue) obj));
            }
            if (useList) {
                // we have to use list for Map - unit
                convertedValue = values;
            } else {
                // we have to use set for string and number
                convertedValue = ImmutableSet.copyOf(values);
            }
        } else {
            convertedValue = getSpecificationValue((SpecificationValue) opValue);
        }
        return convertedValue;
    }

    /**
     * It return String, Number, Map<String, Map<String, Object>> or same passed object
     *
     * @param opValue {@link SpecificationValue}
     * @return Object
     */
    private static Object getSpecificationValue(final SpecificationValue opValue) {
        if (opValue instanceof Value<?>) {
            final Value<?> value = (Value) opValue;
            if (value.getValue() instanceof String
                    || value.getValue() instanceof Number) {
                return value.getValue();
            }
        } else if (opValue instanceof UnitValue) {
            final UnitValue value = (UnitValue) opValue;
            return getUnitAttrMap(value.getValue());
        }
        return opValue;
    }

    /**
     * It converts Map<String, UnitAttribute> into Map<String, Map<String, Object>>
     *
     * @param unitAttributeMap Map<String, UnitAttribute>
     * @return Map<String, Map<String, Object>>
     */
    private static Map<String, Map<String, Object>> getUnitAttrMap(
            final Map<String, UnitAttribute> unitAttributeMap) {
        final Map<String, Map<String, Object>> result = Maps.newHashMap();
        unitAttributeMap.entrySet().stream().forEach(entry -> {
            final UnitAttribute ua = entry.getValue();
            final Map<String, Object> uaMap = Maps.newHashMap();
            uaMap.put("unit", ua.getUnit());
            uaMap.put("value", ua.getValue());
            result.put(entry.getKey(), uaMap);
        });
        return result;
    }

}
