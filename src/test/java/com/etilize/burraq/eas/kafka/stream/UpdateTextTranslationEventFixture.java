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

package com.etilize.burraq.eas.kafka.stream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.etilize.burraq.eas.kafka.stream.UpdateTextTranslationEvent;
import com.fluentinterface.ReflectionBuilder;

/**
 * Test fixture for {@link UpdateTextTranslationEvent}
 *
 * @author Affan Hasan
 * @since 1.0
 */
public class UpdateTextTranslationEventFixture {

    /**
     * Returns a builder for {@link UpdateTextTranslationEvent}
     *
     * @return {@link UpdateTextTranslationEventBuilder} instance.
     */
    static UpdateTextTranslationEventBuilder aMessage() {
        return ReflectionBuilder.implementationFor(
                UpdateTextTranslationEventBuilder.class).create();
    }

    /**
     * Returns an {@link UpdateTextTranslationEvent} instance initialized with default values.
     *
     * @return {@link UpdateTextTranslationEvent} instance.
     * @throws ParseException
     */
    public static UpdateTextTranslationEvent createWithDefaults() {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(
                    "2017-08-11T08:10:47.321-0000");
            return aMessage() //
                    .of("translation-service") //
                    .withIndustryId("industry123") //
                    .withLocaleId("de_DE") //
                    .withValue("Green") //
                    .withTranslation("Gruen") //
                    .withPerformedBy("burraq@etilizepak.com") //
                    .withPerformedDate(date) //
                    .build();
        } catch (final ParseException e) {
            return null;
        }
    }
}
