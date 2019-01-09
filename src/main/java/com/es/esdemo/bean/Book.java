package com.es.esdemo.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
@Data
@Document(indexName = "product", type = "book")
public class Book {
    @Id
    String id;
    String name;
    String message;
    String type;
    Integer No;
}
