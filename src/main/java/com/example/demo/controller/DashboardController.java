package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
    protected DashboardService dashboardService;
	
	@GetMapping("/expensecount")
    @PreAuthorize("isAuthenticated()")
	public Long ExpenseController() {
		
		Long count =dashboardService.expenseCount();
		return count;
	}
	

}
