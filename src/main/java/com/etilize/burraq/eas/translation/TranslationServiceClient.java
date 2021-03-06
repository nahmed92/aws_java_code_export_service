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

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Feign client for translation-service
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Component
@FeignClient("translation-service")
public interface TranslationServiceClient {

    String TRANSLATION_SERVICE_CACHE = "translation-service-cache";

    /**
     * It will translate text using end point /texts/translate
     * @param request {@link TextTranslationRequest}
     * @return {@link TranslationResponse}
     */
    @PostMapping(consumes = "application/json", path = "/texts/translate")
    @Cacheable(TRANSLATION_SERVICE_CACHE)
    TranslationResponse translateText(TextTranslationRequest request);

    /**
     * It will translate text using end point /units/translate
     * @param request {@link UnitTranslationRequest}
     * @return {@link TranslationResponse}
     */
    @PostMapping(consumes = "application/json", path = "/units/translate")
    @Cacheable(TRANSLATION_SERVICE_CACHE)
    TranslationResponse translateUnit(UnitTranslationRequest request);
}
