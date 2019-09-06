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

import java.lang.reflect.Method;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.ApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.util.ReflectionUtils;

/**
 * Adapter class to convert Spring's {@link TestContext}. Since Spring 4.0 change the
 *
 * TestContext class from a class to an interface this method uses reflection.
 *
 * @author Kamal A. Siddiqui
 * @since 1.0
 *
 */
public class ESTestContextAdapter {

    private static final Method GET_TEST_CLASS;

    private static final Method GET_TEST_INSTANCE;

    private static final Method GET_TEST_METHOD;

    private static final Method GET_TEST_EXCEPTION;

    private static final Method GET_APPLICATION_CONTEXT;

    private static final Method GET_ATTRIBUTE;

    private static final Method SET_ATTRIBUTE;

    static {
        try {
            GET_TEST_CLASS = TestContext.class.getMethod("getTestClass");
            GET_TEST_INSTANCE = TestContext.class.getMethod("getTestInstance");
            GET_TEST_METHOD = TestContext.class.getMethod("getTestMethod");
            GET_TEST_EXCEPTION = TestContext.class.getMethod("getTestException");
            GET_APPLICATION_CONTEXT = TestContext.class.getMethod(
                    "getApplicationContext");
            GET_ATTRIBUTE = TestContext.class.getMethod("getAttribute", String.class);
            SET_ATTRIBUTE = TestContext.class.getMethod("setAttribute", String.class,
                    Object.class);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    private final TestContext testContext;

    /**
     * Default constructor
     *
     * @param testContext
     */
    public ESTestContextAdapter(final TestContext testContext) {
        this.testContext = testContext;
    }

    /**
     * @return ElasticsearchOperations object
     */
    public ElasticsearchRestTemplate getESTemplate() {
        return this.testContext.getApplicationContext().getAutowireCapableBeanFactory().getBean(
                ElasticsearchRestTemplate.class);
    }

    /**
     * @return test class
     */
    public Class<?> getTestClass() {
        return (Class<?>) ReflectionUtils.invokeMethod(GET_TEST_CLASS, this.testContext);
    }

    /**
     * @return test method
     */
    public Method getTestMethod() {
        return (Method) ReflectionUtils.invokeMethod(GET_TEST_METHOD, this.testContext);
    }

    /**
     * @return test instance
     */
    public Object getTestInstance() {
        return ReflectionUtils.invokeMethod(GET_TEST_INSTANCE, this.testContext);
    }

    /**
     * @return test excception
     */
    public Throwable getTestException() {
        return (Throwable) ReflectionUtils.invokeMethod(GET_TEST_EXCEPTION,
                this.testContext);
    }

    /**
     * @return application context
     */
    public ApplicationContext getApplicationContext() {
        return (ApplicationContext) ReflectionUtils.invokeMethod(GET_APPLICATION_CONTEXT,
                this.testContext);
    }

    /**
     * @return attribute from test context
     */
    public Object getAttribute(String name) {
        return ReflectionUtils.invokeMethod(GET_ATTRIBUTE, this.testContext, name);
    }

    /**

     * @param name attribute name
     * @param value attribute value
     */
    public void setAttribute(String name, Object value) {
        ReflectionUtils.invokeMethod(SET_ATTRIBUTE, this.testContext, name, value);
    }

}
