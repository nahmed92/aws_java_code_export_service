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

package com.etilize.burraq.eas.media.specification;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.eas.specification.status.SpecificationStatus;
import com.etilize.burraq.eas.specification.status.SpecificationStatusRepository;
import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.google.common.collect.Lists;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.IgnorePropertyValue;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

/**
 * This class implements repository test cases for {@link MediaSpecificationService}.
 *
 * @author Umar Zubair
 * @since 1.0
 */
@UsingDataSet(locations = "/datasets/media_specifications/media_specifications.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)
public class MediaSpecificationServiceIntegrationTest extends AbstractIntegrationTest {

    @Mock
    private SpecificationStatusRepository specsStatusRepository;

    @Autowired
    private BasicMediaSpecificationRepository basicSpecificationRepository;

    @Autowired
    private RichMediaSpecificationRepository detailedSpecificationRepository;

    private MediaSpecificationService service;

    @Override
    public void before() {
        service = new MediaSpecificationServiceImpl(basicSpecificationRepository,
                detailedSpecificationRepository, specsStatusRepository);
        final SpecificationStatus specsStatus1 = new SpecificationStatus();
        specsStatus1.setId("product123-en_US");
        specsStatus1.setStatusId("NEW");
        specsStatus1.setLocaleId("en_US");
        specsStatus1.setProductId("product123");
        final SpecificationStatus specsStatus2 = new SpecificationStatus();
        specsStatus2.setId("product123-en");
        specsStatus2.setStatusId("NEW");
        specsStatus2.setLocaleId("en");
        specsStatus2.setProductId("product123");
        when(specsStatusRepository.findAllByProductId("product123")) //
                .thenReturn(Lists.newArrayList(specsStatus1, specsStatus2));
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_add_locale.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldAddLocaleForMediaSpecification() {
        service.addLocale("product123", "en_UK");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_update_attribute_for_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeForAllLocaleWhenUpdatedForEN() {
        service.saveAttribute("product123", "en", "thumbnailId", Status.ASSOCIATED,
                "http://abcde");
        verify(specsStatusRepository).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_update_attribute_for_non_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateAttributeWhenUpdatedForNonEN() {
        service.saveAttribute("product123", "en_US", "thumbnailId", Status.ASSOCIATED,
                "http://abcde");
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotUpdateAttributeForAllLocaleWhenValueIsEmpty() {
        service.saveAttribute("product123", "en", "thumbnailId", Status.ASSOCIATED, "");
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_delete_attribute_for_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldDeleteAttributeForAllLocaleWhenUpdatedForENWithStatusDELETED() {
        service.saveAttribute("product123", "en", "maxId", Status.DELETED, null);
        verify(specsStatusRepository).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_delete_attribute_for_non_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldDeleteAttributeWhenUpdatedForNonENWithStatusDELETED() {
        service.saveAttribute("product123", "en_US", "maxId", Status.DELETED, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_delete_attribute_for_non_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldDeleteAttributeWhenUpdatedForNonENWithStatusEXCEPTION() {
        service.saveAttribute("product123", "en_US", "maxId", Status.EXCEPTION, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications_after_delete_attribute_for_non_en.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldDeleteAttributeWhenUpdatedForNonENWithStatusPENDING() {
        service.saveAttribute("product123", "en_US", "maxId", Status.PENDING, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotDeleteAttributeWhenUpdatedForNonENWithStatusDOWNLOADING() {
        service.saveAttribute("product123", "en_US", "maxId", Status.DOWNLOADING, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/media_specifications/media_specifications.bson")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldNotDeleteAttributeWhenUpdatedForNonENWithStatusASSOCIATED() {
        service.saveAttribute("product123", "en_US", "maxId", Status.ASSOCIATED, null);
        verify(specsStatusRepository, never()).findAllByProductId("product123");
    }
}
