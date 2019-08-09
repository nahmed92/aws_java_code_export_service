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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import com.etilize.burraq.eas.annotation.ESTestContextAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

/**
 * Internal delegate class used to run tests with support for Elasticsearch ,
 * {@link LoadElasticBulkData}  annotations.
 *
 * @author Kamal A. Siddiqui
 * @since 1.0
 *
 */
public class ESUnitRunner {

    private static final Log logger = LogFactory.getLog(ESUnitRunner.class);

    // Default index type
    private static final String DEFAULT_INDEX_TYPE = "default";

    private static final String DEFAULT_INDEX_CASE_INSENSITIVE_SETTING = "{\"testcase_normalizer\": {" //
            + "\"type\": \"custom\"," //
            + "\"char_filter\": []," //
            + "\"filter\": [\"lowercase\", \"asciifolding\"]}}";

    private static final String INDEX_NORMALIZER_SETTING = "analysis.normalizer";

    /**
     * Called before a test class is executed to perform elasticsearch data setup.
     * @param testContext The test context
     * @throws Exception
     */
    public void beforeTestClass(final ESTestContextAdapter testContext) throws Exception {

        final LoadElasticBulkData classNameAnnotation = AnnotationUtils.findAnnotation(
                testContext.getTestClass(), LoadElasticBulkData.class);
        if (classNameAnnotation != null) {
            final JSONObject jsonObject = parseToJSON(testContext.getTestClass(),
                    classNameAnnotation.jsonDataFile());
            final JSONObject jsonMappingObject = parseToJSON(testContext.getTestClass(),
                    classNameAnnotation.jsonMappingFile());
            loadESData(testContext.getESTemplate(), jsonObject, jsonMappingObject);
        }
    }

    /**
     * Called before a test method is executed to perform elasticsearch data setup.
     * @param testContext The test context
     * @throws Exception
     */
    public void beforeTestMethod(final ESTestContextAdapter testContext)
            throws Exception {

        final LoadElasticBulkData methodNameAnnotation = AnnotationUtils.findAnnotation(
                testContext.getTestMethod(), LoadElasticBulkData.class);
        if (methodNameAnnotation != null) {
            final JSONObject jsonObject = parseToJSON(testContext.getTestClass(),
                    methodNameAnnotation.jsonDataFile());
            final JSONObject jsonMappingObject = parseToJSON(testContext.getTestClass(),
                    methodNameAnnotation.jsonMappingFile());
            loadESData(testContext.getESTemplate(), jsonObject, jsonMappingObject);
        }
    }

    /**
     * Load Json file into elasticsearch
     * @throws IOException
     *
     * */
    private void loadESData(final ElasticsearchRestTemplate esTemplate,
            final JSONObject jsonObject, final JSONObject jsonMappingObject)
            throws IOException {

        if (new JSONObject(jsonObject).get("data") != null) {
            loadMultiIndexJson(esTemplate, jsonObject.toJSONString(), jsonMappingObject);
        } else {
            loadIndexJson(esTemplate, jsonObject.toJSONString(), jsonMappingObject);
        }
    }

    /**
     * Load Json source for single index data
     * @throws IOException
     *
     * */
    private void loadIndexJson(final ElasticsearchRestTemplate esTemplate,
            final String jsonObject, final JSONObject jsonMappingObject)
            throws IOException {
        logger.info("loading json data : " + jsonObject);
        final String index = JsonPath.read(jsonObject, "index");
        final String alias = JsonPath.read(jsonObject, "alias");
        createIndex(esTemplate, index, alias, jsonMappingObject);

        final JSONArray sources = (JSONArray) JsonPath.read(jsonObject, "sources");
        final ListIterator<Object> sourcesIterator = sources.listIterator();
        while (sourcesIterator.hasNext()) {
            final LinkedHashMap sourceMap = (LinkedHashMap) sourcesIterator.next();

            final String docId = JsonPath.read(sourceMap, "id").toString();
            final String _source = new JSONObject(
                    JsonPath.read(sourceMap, "_source")).toJSONString();
            index(esTemplate, index, docId, _source);
        }
    }

    /**
     * Load Json source for multiple index data
     * @throws IOException
     *
     * */
    private void loadMultiIndexJson(final ElasticsearchRestTemplate esTemplate,
            final String jsonObject, final JSONObject jsonMappingObject)
            throws IOException {
        logger.info("loading json data : " + jsonObject);

        final JSONArray dataList = (JSONArray) JsonPath.read(jsonObject, "data");
        final ListIterator<Object> iterator = dataList.listIterator();
        while (iterator.hasNext()) {
            @SuppressWarnings("rawtypes")
            final LinkedHashMap a = (LinkedHashMap) iterator.next();
            final String index = a.get("index").toString();
            final String alias = a.get("alias").toString();

            createIndex(esTemplate, index, alias, jsonMappingObject);

            final JSONArray sources = (JSONArray) a.get("sources");
            final ListIterator<Object> sourcesIterator = sources.listIterator();
            while (sourcesIterator.hasNext()) {
                @SuppressWarnings("rawtypes")
                final LinkedHashMap source = (LinkedHashMap) sourcesIterator.next();
                final String docId = JsonPath.read(source, "id").toString();
                final String _source = new JSONObject(
                        JsonPath.read(source, "_source")).toJSONString();
                index(esTemplate, index, docId, _source);

            }
        }
    }

    /**
     * Creates a new index
     *
     * @param esTemplate ElasticsearchOperations
     * @param industryId industryId
     * @throws IOException
     */
    private void createIndex(final ElasticsearchRestTemplate esTemplate,
            final String industryId, final String alias,
            final JSONObject jsonMappingObject) throws IOException {

        GetIndexRequest getRequest = new GetIndexRequest();
        getRequest.indices(industryId);

        if (!esTemplate.indexExists(industryId)) {

            Map<String, Object> setting = new HashMap<>();
            setting.put("index.mapping.coerce", false);

            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> fieldMapping = new ObjectMapper().readValue(
                        DEFAULT_INDEX_CASE_INSENSITIVE_SETTING, HashMap.class);
                setting.put(INDEX_NORMALIZER_SETTING, fieldMapping);
            } catch (IOException e) {
                e.printStackTrace();
            }

            final JSONObject mapping = JsonPath.read(jsonMappingObject, "mappings");

            CreateIndexRequest createIndexRequest = new CreateIndexRequest(industryId);
            createIndexRequest.settings(setting);
            createIndexRequest.mapping(DEFAULT_INDEX_TYPE,
                    mapping.get(DEFAULT_INDEX_TYPE).toString(), XContentType.JSON);
            createIndexRequest.alias(new Alias(alias));

            esTemplate.createIndex(industryId, setting);
            AliasQuery query = new AliasQuery();
            query.setAliasName(alias);
            query.setIndexName(industryId);
            esTemplate.addAlias(query);
            esTemplate.putMapping(industryId, DEFAULT_INDEX_TYPE,
                    mapping.get(DEFAULT_INDEX_TYPE).toString());
        }
    }

    /**
     * Performs index operations
     *
     * @param esTemplate ElasticsearchOperations
     * @param indexName indexName index name
     * @param indexId indexId index id
     * @param source source index source
     * @throws IOException
     *
     */
    private void index(final ElasticsearchRestTemplate esTemplate, final String indexName,
            final String indexId, final String source) throws IOException {

        //        final IndexRequest indexRequest = new IndexRequest(indexName,DEFAULT_INDEX_TYPE);
        //        indexRequest.id(indexId);
        //        indexRequest.source(source,XContentType.JSON);
        //        esTemplate.index(indexRequest, RequestOptions.DEFAULT);

        final IndexQuery indexQuery1 = new IndexQuery();
        // Id of an elastic search document would be combinition of productId & localeId - productId123-localeId
        indexQuery1.setId(indexId);
        indexQuery1.setIndexName(indexName);
        indexQuery1.setType(DEFAULT_INDEX_TYPE);
        indexQuery1.setSource(source);
        esTemplate.index(indexQuery1);
        esTemplate.refresh(indexName);
    }

    /**
     * Loads a data set from file location
     */
    private JSONObject parseToJSON(final Class<?> testClass, final String location)
            throws Exception {
        ClassPathResource resource = getResourceLoader(location);

        //JSON from file to Object
        // IndexJson obj = mapper.readValue(resource.getFile(), IndexJson.class);
        if (resource.exists()) {
            final JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(Files.newBufferedReader(
                    Paths.get(resource.getURI()), StandardCharsets.UTF_8));
        }
        return null;
    }

    /**
     * Gets the {@link ClassPathResource} that will be used to load the dataset {@link Resource}s.
     * @param fileLocation
     * @return ClassPathResource
     */
    protected ClassPathResource getResourceLoader(String fileLocation) {
        return new ClassPathResource(fileLocation);
    }
}
