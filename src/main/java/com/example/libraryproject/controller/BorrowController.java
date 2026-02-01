package com.example.libraryproject.controller;

import com.example.libraryproject.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @PostMapping("/borrow/{bookIsbn}")
    @ResponseStatus(HttpStatus.OK)
    public void borrowBook(@PathVariable String bookIsbn, Principal principal) {

        borrowService.borrowBook(bookIsbn, principal.getName());
    }

    @PostMapping("/return/{bookIsbn}")
    @ResponseStatus(HttpStatus.OK)
    public void returnBook(@PathVariable String bookIsbn, Principal principal) {

        borrowService.returnBook(bookIsbn, principal.getName());
    }
}
