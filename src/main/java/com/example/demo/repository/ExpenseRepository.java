package com.example.demo.repository;

import com.example.demo.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    List<Expense> findByUserIdAndStatus(Long userId, String status);

    // New: find pending expenses for manager (user.reportingManager.id = ?)
    // Spring JPA doesn't support nested property with join directly in method name easily,
    // so we'll use @Query:
    @org.springframework.data.jpa.repository.Query("SELECT e FROM Expense e WHERE e.user.reportingManager.id = :managerId AND e.status IN :statuses ORDER BY e.submittedAt DESC")
    List<Expense> findPendingForManager(@org.springframework.data.repository.query.Param("managerId") Long managerId, @org.springframework.data.repository.query.Param("statuses") java.util.List<String> statuses);


    List<String> statuses = List.of("PENDING");
}