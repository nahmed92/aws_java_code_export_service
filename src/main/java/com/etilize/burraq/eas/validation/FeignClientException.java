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

/**
 * This is Feign client exception class which extends RuntimeException
 *
 * @author Sidra Zia
 * @version 1.0
 */
public class FeignClientException extends RuntimeException {

    /**
     * generated serial version id
     */
    private static final long serialVersionUID = 3723405171891236310L;

    private final int status;

    private final String message;

    /**
     * Default constructor to initialize exception class
     *
     * @param message Error message of the exception
     * @param status Error code of the message
     */
    public FeignClientException(final String message, final int status) {
        super(message);
        this.status = status;
        this.message = message;
    }

    /**
     * @return the errorCode
     */
    public final int getStatus() {
        return status;
    }

    /**
     * @return the message
     */
    @Override
    public final String getMessage() {
        return message;
    }

}
