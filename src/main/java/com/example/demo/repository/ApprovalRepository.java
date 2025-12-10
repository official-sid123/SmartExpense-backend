package com.example.demo.repository;

import com.example.demo.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    List<Approval> findByApproverId(Long approverId);
    List<Approval> findByExpenseId(Long expenseId);
}