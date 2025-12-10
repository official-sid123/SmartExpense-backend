package com.example.demo.service;

import com.example.demo.entity.Approval;
import com.example.demo.entity.Expense;
import com.example.demo.entity.User;
import com.example.demo.repository.ApprovalRepository;
import com.example.demo.repository.ExpenseRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ApprovalService {

    private final ExpenseRepository expenseRepository;
    private final ApprovalRepository approvalRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;

    public ApprovalService(ExpenseRepository expenseRepository,
                           ApprovalRepository approvalRepository,
                           UserRepository userRepository,
                           EmailService emailService,
                           NotificationRepository notificationRepository) {
        this.expenseRepository = expenseRepository;
        this.approvalRepository = approvalRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.notificationRepository = notificationRepository;
    }

    public List<Expense> getPendingForManager(Long managerId) {
        List<String> statuses = List.of("CREATED", "SUBMITTED", "PENDING");
        return expenseRepository.findPendingForManager(managerId, statuses);
    }

    @Transactional
    public Expense approveOrReject(Long expenseId, Long approverId, String action, String comment) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        // Verify approver is indeed manager for that expense's user OR already set as approver
        User reportingManager = expense.getUser().getReportingManager();
        if (reportingManager == null || !reportingManager.getId().equals(approverId)) {
            throw new RuntimeException("You are not authorized to approve this expense");
        }


        // Create Approval record
        Approval approval = new Approval();
        approval.setExpense(expense);
        approval.setApprover(approver);
        approval.setAction(action); // "APPROVED" or "REJECTED"
        approval.setComment(comment);
        approval.setActionAt(LocalDateTime.now());
        approvalRepository.save(approval);

        // Update expense
        if ("APPROVED".equalsIgnoreCase(action)) {
            expense.setStatus("APPROVED");
            expense.setApprovedAt(LocalDateTime.now());
            expense.setApprover(approver);
        } else if ("REJECTED".equalsIgnoreCase(action)) {
            expense.setStatus("REJECTED");
            expense.setApprover(approver);
        } else {
            throw new RuntimeException("Unknown action");
        }

        Expense saved = expenseRepository.save(expense);

        // Send email to expense owner
        try {
            String subject = "Your expense " + expense.getTitle() + " has been " + action.toLowerCase();
            String body = "Hello " + expense.getUser().getName() + ",\n\nYour expense titled '" + expense.getTitle()
                    + "' has been " + action.toLowerCase() + " by " + approver.getName() + ".\n\nNote: " + comment;
            emailService.sendEmail(expense.getUser().getEmail(), subject, body);
        } catch (Exception ex) {
            // log; don't fail transaction for email issues
            ex.printStackTrace();
        }

        // Save in-app notification
        try {
            com.example.demo.entity.Notification n = new com.example.demo.entity.Notification();
            n.setUserId(expense.getUser().getId());
            n.setTitle("Expense " + action);
            n.setBody("Your expense '" + expense.getTitle() + "' was " + action.toLowerCase() + " by " + approver.getName());
            notificationRepository.save(n);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return saved;
    }
}
