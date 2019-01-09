package com.es.esdemo.dao;

import com.es.esdemo.bean.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface BookRepository extends ElasticsearchCrudRepository<Book, String> {
}
