package com.example.CrudApp.controller;


import com.example.CrudApp.model.Book;
import com.example.CrudApp.repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class bookController {

    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks(){
        try{
            List <Book> booklist = new ArrayList<>();
            bookRepo.findAll().forEach(booklist::add);

            if(booklist.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return  new ResponseEntity<>(booklist,HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getBookById/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> bookdata = bookRepo.findById(id);

        if (bookdata.isPresent()) {
            return new ResponseEntity<>(bookdata.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        Book bobj =bookRepo.save(book);
        return new ResponseEntity<>(bobj,HttpStatus.OK);

    }


    @PostMapping("/updateBookById/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable Long id, @RequestBody Book newBookdata){

        Optional<Book> oldbookdata = bookRepo.findById(id);
        if(oldbookdata.isPresent()){
            Book updtbook = oldbookdata.get();
            updtbook.setTitle(newBookdata.getTitle());
            updtbook.setAuthor(newBookdata.getAuthor());

            Book updobj = bookRepo.save(updtbook);
            return new ResponseEntity<>(updobj,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<HttpStatus> deleteBookById(@PathVariable Long id){
        bookRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
