package com.es.esdemo.utils;

import org.apache.lucene.codecs.blocktree.Stats;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.stats.extended.ExtendedStats;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * ES聚合工具类
 **/
public class AggregationUtil {
    private String index = "index";
    private Client client;

    public AggregationUtil() {
        client = elasticsearchTemplate.getClient();
    }

    public AggregationUtil(String index) {
        this.index = index;
    }

    public AggregationUtil(String index, Client client) {
        this.index = index;
        this.client = client;
    }

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 最大值统计
     *
     * @param field
     * @return
     */
    public double max(String field) {
        AggregationBuilder agg = AggregationBuilders.max("aggMax").field(field);
        SearchResponse response = client.prepareSearch(index).addAggregation(agg).get();
        Max max = response.getAggregations().get("aggMax");
        return max.getValue();
    }

    /**
     * 最小值统计
     *
     * @param field
     * @return
     */
    public double min(String field) {
        AggregationBuilder agg = AggregationBuilders.min("aggMin").field(field);
        SearchResponse response = client.prepareSearch(index).addAggregation(agg).get();
        Min min = response.getAggregations().get("aggMin");
        return min.getValue();
    }

    /**
     * 合计统计
     *
     * @param field
     * @return
     */
    public double sum(String field) {
        AggregationBuilder agg = AggregationBuilders.sum("aggSum").field(field);
        SearchResponse response = client.prepareSearch(index).addAggregation(agg).get();
        Sum sum = response.getAggregations().get("aggSum");
        return sum.getValue();
    }

    /**
     * 平均值统计
     *
     * @param field
     * @return
     */
    public double avg(String field) {
        AggregationBuilder agg = AggregationBuilders.avg("aggAvg").field(field);
        SearchResponse response = client.prepareSearch(index).addAggregation(agg).get();
        Avg avg = response.getAggregations().get("aggAvg");
        return avg.getValue();
    }

    /**
     * 基本统计
     *
     * @param field
     * @return
     */
    public Stats stats(String field) {
        AggregationBuilder agg = AggregationBuilders.stats("aggStats").field(field);
        SearchResponse response = client.prepareSearch(index).addAggregation(agg).execute().actionGet();
        return response.getAggregations().get("aggStats");
    }

    /**
     * 高级统计
     *
     * @param field
     * @return
     */
    public ExtendedStats extendedStats(String field) {
        AggregationBuilder agg = AggregationBuilders.extendedStats("exStats").field(field);
        SearchResponse response = client.prepareSearch(index).addAggregation(agg).execute().actionGet();
        return response.getAggregations().get("exStats");
    }

    /**
     * 基数统计
     *
     * @param field
     * @return
     */
    public double cardinality(String field) {
        AggregationBuilder agg = AggregationBuilders.cardinality("cardinality").field(field);
        SearchResponse response = client.prepareSearch(index).addAggregation(agg).get();
        Cardinality c = response.getAggregations().get("cardinality");
        return c.getValue();
    }

    /**
     * 文档数量统计
     *
     * @param field
     * @return
     */
    public double valueCount(String field) {
        AggregationBuilder agg = AggregationBuilders.count("valueCount").field(field);
        SearchResponse response = client.prepareSearch(index).addAggregation(agg).execute().actionGet();
        ValueCount count = response.getAggregations().get("valueCount");
        return count.getValue();
    }
}
