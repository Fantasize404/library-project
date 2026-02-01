package com.example.libraryproject.repository;

import com.example.libraryproject.entity.Book;
import com.example.libraryproject.entity.BorrowRecord;
import com.example.libraryproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    Optional<BorrowRecord> findByUserAndBookAndReturnDateIsNull(User user, Book book);
}
