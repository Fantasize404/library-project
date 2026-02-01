package com.example.libraryproject.service;

import com.example.libraryproject.entity.Book;
import com.example.libraryproject.entity.BorrowRecord;
import com.example.libraryproject.entity.User;
import com.example.libraryproject.repository.BookRepository;
import com.example.libraryproject.repository.BorrowRecordRepository;
import com.example.libraryproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class BorrowService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void borrowBook(String bookIsbn, String username) {

        Book book = bookRepository.findByIsbn(bookIsbn)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到書籍"));

        if (book.isBorrowed()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "書籍已被借出");
        }

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到使用者"));

        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setUser(user);
        record.setBorrowDate(LocalDateTime.now());
        borrowRecordRepository.save(record);

        book.setBorrowed(true);
        bookRepository.save(book);
    }

    @Transactional
    public void returnBook(String bookIsbn, String username) {

        Book book = bookRepository.findByIsbn(bookIsbn)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到書籍"));

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到使用者"));

        BorrowRecord record = borrowRecordRepository.findByUserAndBookAndReturnDateIsNull(user, book)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "你沒有借閱這本書，或已經歸還"));

        record.setReturnDate(LocalDateTime.now());
        borrowRecordRepository.save(record);

        book.setBorrowed(false);
        bookRepository.save(book);
    }
}
