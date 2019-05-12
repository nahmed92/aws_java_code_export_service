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

package com.etilize.burraq.eas.media.specification;

import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

/**
 * Houses details for media attribute value
 *
 * @author Umar Zubair
 * @since 1.0
 */
@DynamoDBDocument
public class MediaAttributeValue {

    private String url;

    private Integer height;

    private Integer width;

    private Set<String> tags;

    /**
     * Constructor for {@link MediaAttributeValue}
     */
    public MediaAttributeValue() {
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(final Integer height) {
        this.height = height;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(final Integer width) {
        this.width = width;
    }

    /**
     * @return the tags
     */
    public Set<String> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(final Set<String> tags) {
        this.tags = tags;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder() //
                .append(getUrl()) //
                .append(getTags()) //
                .append(getHeight()) //
                .append(getWidth()) //
                .hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MediaAttributeValue)) {
            return false;
        }
        final MediaAttributeValue value = (MediaAttributeValue) obj;
        return new EqualsBuilder() //
                .append(getUrl(), value.getUrl()) //
                .append(getTags(), value.getTags()) //
                .append(getHeight(), value.getHeight()) //
                .append(getWidth(), value.getWidth()) //
                .isEquals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this) //
                .append("Url", url) //
                .append("Tags", tags) //
                .append("Height", height) //
                .append("Width", width) //
                .toString();
    }
}
