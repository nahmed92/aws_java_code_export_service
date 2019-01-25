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

package com.etilize.burraq.eas.barcode;

import static com.etilize.burraq.eas.utils.Utils.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.etilize.burraq.eas.specification.status.SpecificationStatus;
import com.etilize.burraq.eas.specification.status.SpecificationStatusRepository;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * It implements {@link BarcodeService}
 *
 * @author Umar Zubair
 * @since 1.0
 */
@Service
public class BarcodeServiceImpl implements BarcodeService {

    private static final String CUSTOMER_ID_IS_REQUIRED = "customerId is required";

    private static final String PRODUCT_ID_IS_REQUIRED = "productId is required";

    private static final String TYPE_IS_REQUIRED = "type is required";

    private static final String CODE_IS_REQUIRED = "code is required";

    private final BarcodeRepository barcodeRepository;

    private final SpecificationStatusRepository specsStatusRepository;

    /**
     * Constructs with dependencies
     *
     * @param barcodeRepository {@link BarcodeRepository}
     * @param specsStatusRepository {@link SpecificationStatusRepository}
     */
    @Autowired
    public BarcodeServiceImpl(final BarcodeRepository barcodeRepository,
            final SpecificationStatusRepository specsStatusRepository) {
        Assert.notNull(barcodeRepository, "barcodeRepository should not be null.");
        Assert.notNull(specsStatusRepository,
                "specsStatusRepository should not be null.");
        this.barcodeRepository = barcodeRepository;
        this.specsStatusRepository = specsStatusRepository;
    }

    @Override
    public void save(final String productId, final String type, final String code,
            final String customerId) {
        Assert.hasText(productId, PRODUCT_ID_IS_REQUIRED);
        Assert.hasText(type, TYPE_IS_REQUIRED);
        Assert.hasText(code, CODE_IS_REQUIRED);
        Assert.hasText(customerId, CUSTOMER_ID_IS_REQUIRED);
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put(customerId, Sets.newHashSet(code));
        final List<SpecificationStatus> specsStatuses = specsStatusRepository.findAllByProductId(
                productId);
        specsStatuses.forEach(specsStatus -> {
            final String id = generateId(productId, specsStatus.getLocaleId());
            final Barcode barcode = new Barcode();
            barcode.setId(id);
            barcode.setLocaleId(specsStatus.getLocaleId());
            barcode.setProductId(productId);
            barcode.setType(type);
            barcode.setCodes(codes);
            barcode.setLastUpdateDate(new Date());
            barcodeRepository.link(barcode);
        });
    }

    @Override
    public void delete(final String productId, final String type, final String code,
            final String customerId) {
        Assert.hasText(productId, PRODUCT_ID_IS_REQUIRED);
        Assert.hasText(type, TYPE_IS_REQUIRED);
        Assert.hasText(code, CODE_IS_REQUIRED);
        Assert.hasText(customerId, CUSTOMER_ID_IS_REQUIRED);
        final Map<String, Set<String>> codes = Maps.newHashMap();
        codes.put(customerId, Sets.newHashSet(code));
        final List<SpecificationStatus> specsStatuses = specsStatusRepository.findAllByProductId(
                productId);
        specsStatuses.forEach(specsStatus -> {
            final String id = generateId(productId, specsStatus.getLocaleId());
            final Barcode barcode = new Barcode();
            barcode.setId(id);
            barcode.setLocaleId(specsStatus.getLocaleId());
            barcode.setProductId(productId);
            barcode.setType(type);
            barcode.setCodes(codes);
            barcode.setLastUpdateDate(new Date());
            barcodeRepository.unlink(barcode);
        });
    }
}
