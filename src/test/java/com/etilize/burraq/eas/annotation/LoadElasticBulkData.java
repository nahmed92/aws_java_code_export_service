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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Load test data into elasticsearch server
 *
 * There are two types of json data files
 * One is use for single index has multiple index documents
 * <code>{
    "alias": "industry10",
    "index": "industry10-v1",
    "sources": [
        {
            "id": "abc0101-en_US",
            "_source": {
                "1": [
                    "red"
                ],
                "2": "DNF",
                "3": [
                    "abc",
                    "def"
                ],
                "industryId": "industry10",
                "productId": "abc0101",
                "categoryId": "categoryId7",
                "localeId": "en_US"
            }
        },
        {
            "id": "xyz0102-en_US",
            "_source": {
                "1": [
                    "yellow"
                ],
                "2": "DNF",
                "3": [
                    "abc",
                    "def"
                ],
                "industryId": "industry10",
                "productId": "xyz0102",
                "categoryId": "categoryId7",
                "localeId": "en_US"
            }
        }
    ]
}</code>
 *
 * Second is use for Multiple indexes with multiple index documents
 *<code>
     {
        "data": [
            {
                "alias": "industry1",
                "index": "industry1-v1",
                "sources": [
                    {
                        "id": "xyz123-en_US",
                        "_source": {
                            "1": [
                                "green"
                            ],
                            "2": "DNF",
                            "3": [
                                "abc",
                                "def"
                            ],
                            "industryId": "industryId002",
                            "productId": "xyz123",
                            "categoryId": "categoryId002",
                            "localeId": "en_US"
                        }
                    },
                    {
                        "id": "xyz456-en_US",
                        "_source": {
                            "1": [
                                "green"
                            ],
                            "2": "DNF",
                            "3": [
                                "abc",
                                "def"
                            ],
                            "industryId": "industryId002",
                            "productId": "xyz456",
                            "categoryId": "categoryId002",
                            "localeId": "en_US"
                        }
                    }
                ]
            },
            {
                "alias": "industry2",
                "index": "industry2-v1",
                "sources": [
                    {
                        "id": "acb456-en_US",
                        "_source": {
                            "1": [
                                "green"
                            ],
                            "2": "DNF",
                            "3": [
                                "abc",
                                "def"
                            ],
                            "industryId": "industryId002",
                            "productId": "acb456",
                            "categoryId": "categoryId002",
                            "localeId": "en_US"
                        }
                    },
                    {
                        "id": "xyz789-en_US",
                        "_source": {
                            "1": [
                                "green"
                            ],
                            "2": "DNF",
                            "3": [
                                "abc",
                                "def"
                            ],
                            "industryId": "industryId002",
                            "productId": "xyz789",
                            "categoryId": "categoryId002",
                            "localeId": "en_US"
                        }
                    }
                ]
            }
        ]
    }
 * </code>
 *
 * @author Kamal A. Siddiqui
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface LoadElasticBulkData {

    String jsonDataFile();

    String jsonMappingFile() default "";

}
