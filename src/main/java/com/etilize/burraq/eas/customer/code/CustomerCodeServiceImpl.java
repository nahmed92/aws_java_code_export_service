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

package com.etilize.burraq.eas.customer.code;

import static com.etilize.burraq.eas.utils.Utils.*;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.etilize.burraq.eas.locale.LocaleService;
import com.google.common.collect.Sets;

/**
 * It implements {@link CustomerCodeService}
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Service
public class CustomerCodeServiceImpl implements CustomerCodeService {

    private static final String CUSTOMER_ID_IS_REQUIRED = "customerId is required";

    private static final String PRODUCT_ID_IS_REQUIRED = "productId is required";

    private static final String MARKET_ID_IS_REQUIRED = "marketId is required";

    private static final String CODE_IS_REQUIRED = "code is required";

    private final CustomerCodeRepository repository;

    private final LocaleService localeService;

    /**
     * Constructs with dependencies
     *
     * @param repository {@link CustomerCodeRepository}
     * @param localeService {@link LocaleService}
     */
    @Autowired
    public CustomerCodeServiceImpl(final CustomerCodeRepository repository,
            final LocaleService localeService) {
        Assert.notNull(repository, "repository should not be null.");
        Assert.notNull(localeService, "localeService should not be null.");
        this.repository = repository;
        this.localeService = localeService;
    }

    @Override
    public void save(final String productId, final String marketId, final String code,
            final String customerId) {
        Assert.hasText(productId, PRODUCT_ID_IS_REQUIRED);
        Assert.hasText(marketId, MARKET_ID_IS_REQUIRED);
        Assert.hasText(code, CODE_IS_REQUIRED);
        Assert.hasText(customerId, CUSTOMER_ID_IS_REQUIRED);

        final List<String> locales = localeService.getLocalesForMarket(marketId);
        locales.forEach(localeId -> {
            final String id = generateId(productId, localeId);
            final CustomerCode customerCode = new CustomerCode();
            customerCode.setId(id);
            customerCode.setLocaleId(localeId);
            customerCode.setProductId(productId);
            customerCode.setCustomerId(customerId);
            customerCode.setCodes(Sets.newHashSet(code));
            customerCode.setLastUpdateDate(new Date());
            repository.link(customerCode);
        });
    }

    @Override
    public void delete(final String productId, final String marketId, final String code,
            final String customerId) {
        Assert.hasText(productId, PRODUCT_ID_IS_REQUIRED);
        Assert.hasText(marketId, MARKET_ID_IS_REQUIRED);
        Assert.hasText(code, CODE_IS_REQUIRED);
        Assert.hasText(customerId, CUSTOMER_ID_IS_REQUIRED);

        final List<String> locales = localeService.getLocalesForMarket(marketId);
        locales.forEach(localeId -> {
            final String id = generateId(productId, localeId);
            final CustomerCode customerCode = new CustomerCode();
            customerCode.setId(id);
            customerCode.setLocaleId(localeId);
            customerCode.setProductId(productId);
            customerCode.setCustomerId(customerId);
            customerCode.setCodes(Sets.newHashSet(code));
            customerCode.setLastUpdateDate(new Date());
            repository.unlink(customerCode);
        });
    }

}
