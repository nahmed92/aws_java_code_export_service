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

package com.etilize.burraq.eas.locale;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Implements {@link LocaleService}
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Service
public class LocaleServiceImpl implements LocaleService {

    private final LocaleServiceClient localeServiceClient;

    /**
     * Constructor with required dependencies.
     *
     * @param localeServiceClient locale service client
     */
    @Autowired
    public LocaleServiceImpl(final LocaleServiceClient localeServiceClient) {
        Assert.notNull(localeServiceClient, "localeServiceClient can not be null.");
        this.localeServiceClient = localeServiceClient;
    }

    @Override
    public List<String> findLocaleIdsForMarket(final String market) {
        final Collection<Resource<Locale>> locales = localeServiceClient.findBy(market,
                null, 0, 20, null).getContent();
        final List<String> localeStrs = locales.stream().map(
                locale -> StringUtils.substringAfterLast(
                        locale.getLink(Link.REL_SELF).getHref(), "/")).collect(
                                Collectors.toList());
        return localeStrs;
    }

    @Override
    public List<String> findAllLocaleIds() {
        final Collection<Resource<Locale>> locales = localeServiceClient.findBy(null,
                null, 0, 1000, null).getContent();
        final List<String> localeStrs = locales.stream().map(
                locale -> StringUtils.substringAfterLast(
                        locale.getLink(Link.REL_SELF).getHref(), "/")).collect(
                                Collectors.toList());
        return localeStrs;
    }

}
