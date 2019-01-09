package com.es.esdemo;

import com.es.esdemo.bean.Book;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCount;
import org.elasticsearch.search.aggregations.metrics.valuecount.ValueCountAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsdemoApplicationTests2 {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;


    @Test
    public void findAll() {
        Client client = elasticsearchTemplate.getClient();
        SearchResponse searchResponse = client.prepareSearch("product")//类似库
                .setTypes("book")                                               //类似表
                .get();
        SearchHits hits = searchResponse.getHits();
        for (SearchHit h : hits.getHits()) {
            System.err.println(h.getSource());
        }
    }

    @Test
    public void findCount() {
        NativeSearchQuery nsq = new NativeSearchQuery(QueryBuilders.termQuery("type", "人"));
        SearchType book = SearchType.DEFAULT;
        nsq.setSearchType(book);
        long count = elasticsearchTemplate.count(nsq, Book.class);
        System.out.println(count);
    }

    @Test
    public void findAllByAge() {
        int limit = 3;
        int page = 1;
        Client client = elasticsearchTemplate.getClient();
        ValueCountAggregationBuilder field = AggregationBuilders.count("valueCount").field("no");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("type", "人");
        SearchRequestBuilder srb = client.prepareSearch("product")
                .setTypes("book")
                .setQuery(termQueryBuilder);
        SearchResponse resp =
                srb.addAggregation(field).execute().actionGet();


        ValueCount no_count = resp.getAggregations().get("valueCount");
        resp = srb                                                          //条件
                .addSort("no", SortOrder.DESC)                                                    //排序
                .setFrom((page - 1) * limit)                                                          //分页
                .setSize(limit)
                .execute().actionGet();
        SearchHits hits = resp.getHits();
        for (SearchHit h : hits.getHits()) {
            System.err.println(h.getSource());
        }
        double v = Double.valueOf(no_count.getValue()) / Double.valueOf(limit);
        double totalPage = Math.ceil(v);
        System.out.println("总记录数：" + no_count.getValue());
        System.out.println(" 当前页：" + page + "/" + totalPage);
        client.close();
    }

    @Test
    public void findLikeMessage() {
        Client client = elasticsearchTemplate.getClient();
        SearchResponse searchResponse = client.prepareSearch("product")
                .setTypes("book")
                //.setPostFilter(QueryBuilders.rangeQuery("id").from(11).to(13))//条件，between  and
                .setQuery(QueryBuilders.matchQuery("message", "hi"))   //模糊查询
                .addSort("no", SortOrder.DESC)
                .get();
        SearchHit[] hits = searchResponse.getHits().getHits();

        System.out.println(hits.length);
        for (SearchHit h : hits) {
            System.err.println(h.getSource());
        }
    }


}

