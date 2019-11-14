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

package com.etilize.burraq.eas.translation;

import static com.etilize.burraq.eas.ExportAggregationConstants.*;
import static com.etilize.burraq.eas.utils.Utils.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * This class implements {@link TranslationService}
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Service
public class TranslationServiceImpl implements TranslationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TranslationServiceClient serviceClient;

    /**
     * Constructor to instantiate TranslationService
     *
     * @param serviceClient Feign service client {@link TranslationServiceClient}
     *
     */
    @Autowired
    public TranslationServiceImpl(final TranslationServiceClient serviceClient) {
        Assert.notNull(serviceClient, "serviceClient should not be null.");
        this.serviceClient = serviceClient;
    }

    @Override
    public String translateText(final String industryId, final String localeId,
            final String value) {
        String translation = "";
        if (StringUtils.isNotBlank(value)) {
            final String targetLanguageId = getLanguageFromLocaleId(localeId);
            if (targetLanguageId.equals(LANGUAGE_EN)) {
                translation = value;
            } else {
                translation = serviceClient.translateText(
                        new TextTranslationRequest(industryId, LANGUAGE_EN,
                                targetLanguageId, value)).getTranslation();
            }
        }
        logger.info("Translate Text - value: {} translate: {}", value, translation);
        return translation;
    }

    @Override
    public String translateUnit(final String localeId, final String value) {
        String translation = "";
        if (StringUtils.isNotBlank(value)) {
            final String targetLanguageId = getLanguageFromLocaleId(localeId);
            if (targetLanguageId.equals(LANGUAGE_EN)) {
                translation = value;
            } else {
                translation = serviceClient.translateUnit(new UnitTranslationRequest(
                        LANGUAGE_EN, targetLanguageId, value)).getTranslation();
            }
        }
        logger.info("Translate Unit - value: {} translate: {}", value, translation);
        return translation;
    }
}
