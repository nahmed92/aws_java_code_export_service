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

package com.etilize.burraq.eas.kafka.redis;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents create/update 'product_statuses' Json message from PSSS.
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class PSSSUpsertMessagePayload {

    private final String key;

    private final Map<String, String> fields;

    /**
     * Constructor to initialize object properties.
     *
     * @param key {@link String} key
     * @param fields {@link Map<String, String>} fields
     */
    public PSSSUpsertMessagePayload(final String key, final Map<String, String> fields) {
        this.key = key;
        this.fields = fields;
    }

    /**
     * Return key
     *
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * Return {@link Map} fields
     *
     * @return fields
     */
    public Map<String, String> getFields() {
        return fields;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PSSSUpsertMessagePayload)) {
            return false;
        }
        final PSSSUpsertMessagePayload productSpecificationsStatusRequest = (PSSSUpsertMessagePayload) obj;
        return new EqualsBuilder() //
                .append(getKey(), productSpecificationsStatusRequest.getKey()) //
                .append(getFields(), productSpecificationsStatusRequest.getFields()) //
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getKey()) //
                .append(getFields()) //
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("Key", getKey()) //
                .append("Fields", getFields()) //
                .toString();
    }
}
