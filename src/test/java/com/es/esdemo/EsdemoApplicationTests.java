package com.es.esdemo;

import com.es.esdemo.bean.Book;
import com.es.esdemo.dao.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsdemoApplicationTests {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    /**
     *
     * ElasticsearchCrudRepository方式
     * **/
    @Autowired
    BookRepository bookRepository;

    @Test
    public void contextLoads() {
        bookRepository.deleteAll();
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book book = new Book();
            book.setId("1" + i);
            book.setMessage("hi~" + i);
            book.setName("lishizhen" + i);
            book.setType("人");
            book.setNo(i+(int)(Math.random()*10+10));
            books.add(book);
        }
        bookRepository.saveAll(books);//添加到es
    }

    @Test
    public void findAll() {


        Iterable<Book> all = bookRepository.findAll();
        all.forEach(a -> System.out.println(a.toString()));//查询全部

    }

    @Test
    public void findAllPage() {
        int pageNumber = 1;
        int pageSize = 5;
        Pageable pageable = new PageRequest(pageNumber, pageSize);

        Iterable<Book> all = bookRepository.findAll(pageable);

        all.forEach(a -> System.out.println(a.toString()));//查询全部

    }
    @Test
    public void findById() {
        Book book = bookRepository.findById("1").get();//根据ID获取
        System.out.println(book);
    }



    @Test
    public void del() {

        bookRepository.deleteById("1");  //根据ID删除
    }
}

