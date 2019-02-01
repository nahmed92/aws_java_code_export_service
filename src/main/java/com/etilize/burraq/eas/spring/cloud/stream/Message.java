/*
 * #region
 * product-media-service
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

package com.etilize.burraq.eas.spring.cloud.stream;

import java.util.Date;
import org.apache.avro.reflect.AvroEncode;
import org.apache.avro.reflect.DateAsLongEncoding;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * Abstract class for Messages. Allows storing and retrieving creation information.
 *
 * @author Imran Saeed
 * @since 1.0
 */
@JsonClassDescription("Abstract class for Messages. Allows storing and retrieving creation information.")
public abstract class Message {

    /**
     * Source from which this message is generated. This usually is the micro-service name
     * responsible for message creation
     */
    @JsonPropertyDescription("Source from which this message is generated. This usually is the micro-service name responsible for message creation.")
    @JsonProperty(required = true)
    private final String source;

    /**
     * The perform date of the activity which resulted in the generation of this message.
     */
    @JsonPropertyDescription("The perform date of the activity which resulted in the generation of this message.")
    @JsonProperty(required = true)
    @AvroEncode(using = DateAsLongEncoding.class)
    private Date performedDate;

    /**
     * The user who performed the activity which resulted in the generation of this
     * message.
     */
    @JsonPropertyDescription("The user who performed the activity which resulted in the generation of this message.")
    @JsonProperty(required = true)
    private String performedBy;

    /**
     * Constructor to create an instance for {@link Message}
     *
     * @param source the source from which this message was generated
     */
    public Message(final String source) {
        this(source, source, new Date());
    }

    /**
     * Constructor to create an instance for {@link Message}
     *
     * @param source the source from which this message was generated
     * @param performedBy user who performed the activity which resulted in the generation
     *        of this message
     * @param performedDate performed date of activity which resulted in the generation of
     *        this message
     */
    public Message(final String source, final String performedBy,
            final Date performedDate) {
        Assert.hasText(source, "source should not be empty");
        Assert.hasText(performedBy, "performedBy should not be empty");
        Assert.notNull(performedDate, "performedDate should not be null");
        this.source = source;
        this.performedBy = performedBy;
        this.performedDate = new Date(performedDate.getTime());
    }

    /**
     * Returns the source which generated this message
     *
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Returns the perform date of this message.
     *
     * @return the performedDate
     */
    public Date getPerformedDate() {
        return performedDate != null ? new Date(performedDate.getTime()) : null;
    }

    /**
     * Sets the performed date of this message.
     *
     * @param creationDate the creation date to set
     */
    public void setPerformedDate(final Date performedDate) {
        this.performedDate = performedDate != null ? new Date(performedDate.getTime())
                : null;
    }

    /**
     * Returns the user id who performed the activity.
     *
     * @return {@link String}
     */
    public String getPerformedBy() {
        return performedBy;
    }

    /**
     * Sets the user who performed the activity.
     *
     * @param performedBy the creating entity to set
     */
    public void setPerformedBy(final String performedBy) {
        this.performedBy = performedBy;
    }

    /** {@inheritDoc} */
    @Override
    public abstract boolean equals(Object obj);

    /** {@inheritDoc} */
    @Override
    public abstract int hashCode();

    /** {@inheritDoc} */
    @Override
    public abstract String toString();
}
