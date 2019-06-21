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

package com.etilize.burraq.eas.specification.status;

import static com.etilize.burraq.eas.utils.Utils.*;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * It implements {@link ProductSpecificationStatusService}
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Service
public class ProductSpecificationStatusServiceImpl
        implements ProductSpecificationStatusService {

    private static final String PRODICTID_IS_REQUIRED = "productId is required";

    private static final String STATUSID_IS_REQUIRED = "statusId is required";

    private static final String LOCALEID_IS_REQUIRED = "localeId is required";

    private final ProductSpecificationStatusRepository repository;

    /**
     * Constructs with dependencies
     *
     * @param repository {@link ProductSpecificationStatusRepository}
     */
    @Autowired
    public ProductSpecificationStatusServiceImpl(
            final ProductSpecificationStatusRepository repository) {
        Assert.notNull(repository, "repository should not be null.");
        this.repository = repository;
    }

    @Override
    public void save(final String productId, final String localeId,
            final String statusId) {
        Assert.hasText(productId, PRODICTID_IS_REQUIRED);
        Assert.hasText(localeId, LOCALEID_IS_REQUIRED);
        Assert.hasText(statusId, STATUSID_IS_REQUIRED);

        final String id = generateId(productId, localeId);
        final ProductSpecificationStatus status = new ProductSpecificationStatus();
        status.setId(id);
        status.setLocaleId(localeId);
        status.setStatusId(statusId);
        status.setProductId(productId);
        status.setLastUpdateDate(new Date());
        repository.save(status);
    }

    /* (non-Javadoc)
     * @see com.etilize.burraq.eas.specification.status.SpecificationStatusService#deleteAllByProductId(java.lang.String)
     */
    @Override
    public void deleteAllByProductId(final String productId) {
        Assert.notNull(productId, "productId is required.");
        repository.deleteAllByProductId(productId);
    }
}
