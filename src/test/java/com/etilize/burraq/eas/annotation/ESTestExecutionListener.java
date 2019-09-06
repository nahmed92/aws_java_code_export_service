/*
 * #region
 * export-aggregation-service
 * %%
 * Copyright (C) 2018 Etilize
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

package com.etilize.burraq.eas.annotation;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * <code>ESTestExecutionListener</code> which provides support for elasticsearch data
 * loading for testing, with support {@link LoadElasticBulkData} annotations.
 * <p>
 * Elasticsearch datasets are loaded from json formatted file see LoadElasticBulkData
 *
 * If you are using auto wiring in test classes then use with
 * {@link DependencyInjectionTestExecutionListener}
 *
 * @author Kamal A. Siddiqui
 */
public class ESTestExecutionListener extends AbstractTestExecutionListener {

    private static ESUnitRunner runner = new ESUnitRunner();

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        runner.beforeTestClass(new ESTestContextAdapter(testContext));
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        runner.beforeTestMethod(new ESTestContextAdapter(testContext));
    }
}
