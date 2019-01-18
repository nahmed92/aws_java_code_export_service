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

package com.etilize.burraq.eas.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 * This class implements {@link ErrorDecoder} and decodes Feign errors
 *
 * @author Sidra Zia
 * @version 1.0
 */
public class FeignErrorDecoder implements ErrorDecoder {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * decodes Feign erroneous response
     *
     * @param methodKey the method name of feign client that was invoked for request
     * @param response the response returned
     * @return feignClientException
     */
    @Override
    public FeignClientException decode(final String methodKey, final Response response) {
        final int errorCode = response.status();
        String errorReason = response.reason();
        if (errorReason == null) {
            if (HttpStatus.NOT_FOUND.value() == errorCode) {
                errorReason = "Requested resource is not found";
            } else if (HttpStatus.UNAUTHORIZED.value() == errorCode) {
                errorReason = "You are not authorized to access requested resource";
            } else {
                errorReason = "Could not complete requested operation";
            }
        }
        logger.error("Error :: {}, Error Code :: {}, request :: {}, response :: {}",
                errorReason, errorCode, response.request().toString(),
                response.toString());
        return new FeignClientException(errorReason, errorCode);
    }
}
