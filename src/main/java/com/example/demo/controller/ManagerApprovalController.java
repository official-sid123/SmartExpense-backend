package com.example.demo.controller;

import com.example.demo.entity.Expense;
import com.example.demo.service.ApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class ManagerApprovalController {

    private final ApprovalService approvalService;

    public ManagerApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    // 1) GET pending (for manager)
    // managerId can be taken from JWT in real app; for simplicity accept managerId param or header
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")

    public ResponseEntity<?> getPendingExpenses(@RequestParam Long managerId) {
        List<Expense> list = approvalService.getPendingForManager(managerId);
        return ResponseEntity.ok(list);
    }

    // 2) Approve/Reject
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> approveExpense(@PathVariable Long id, @RequestBody Map<String, String> body) {
        // expected body: { "approverId": "12", "action": "APPROVED", "comment": "Looks good" }
        Long approverId = Long.valueOf(body.get("approverId"));
        String action = body.get("action");
        String comment = body.getOrDefault("comment", "");

        try {
            Expense updated = approvalService.approveOrReject(id, approverId, action, comment);
            return ResponseEntity.ok(Map.of("message", "success", "expense", updated));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
