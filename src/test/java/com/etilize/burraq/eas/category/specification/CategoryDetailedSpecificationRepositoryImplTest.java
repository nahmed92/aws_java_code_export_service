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

package com.etilize.burraq.eas.category.specification;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.etilize.burraq.eas.test.AbstractIntegrationTest;
import com.lordofthejars.nosqlunit.annotation.CustomComparisonStrategy;
import com.lordofthejars.nosqlunit.annotation.IgnorePropertyValue;
import com.lordofthejars.nosqlunit.annotation.ShouldMatchDataSet;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.dynamodb.DynamoFlexibleComparisonStrategy;

@UsingDataSet(locations = "/datasets/detailed_category_structures/detailed_category_structures.bson", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
@CustomComparisonStrategy(comparisonStrategy = DynamoFlexibleComparisonStrategy.class)

public class CategoryDetailedSpecificationRepositoryImplTest
        extends AbstractIntegrationTest {

    @Autowired
    private CategoryDetailedSpecificationRepository repository;

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_specification_attribute_update/category_detailed_attribute_after_offerring_attribute_update.json")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateDetailedCategoryAttribute() throws Exception {
        repository.updateCategoryAttribute("categoryId123", "en_CA", true, "mfgPartNoId",
                "Updated Part Number");
    }

    @Test
    @ShouldMatchDataSet(location = "/datasets/category_specification_attribute_update/category_detail_specs_name_attribute_after_update.json")
    @IgnorePropertyValue(properties = { "lastUpdateDate" })
    public void shouldUpdateDetailedCategoryNameAttribute() throws Exception {
        repository.updateCategoryAttribute("categoryId123", "en_US", false,
                "categoryName", "Notebook Updated name");
    }

}
