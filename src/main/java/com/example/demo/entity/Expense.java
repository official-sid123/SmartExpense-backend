package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "expenses")
@JsonIgnoreProperties({"approver", "items", "receipts", "payment", "approvals"})
public class Expense {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@ManyToOne
@JoinColumn(name = "user_id")
@JsonIgnoreProperties({"expenses"})
private User user;


private String title;
private Double totalAmount;
private String currency;
private String category;
private Long projectId;
private String status;
private LocalDateTime dateOfExpense;
private LocalDateTime submittedAt;
private LocalDateTime approvedAt;
private String notes;
private String gstNumber;     // e.g. 27ABCDE1234F1Z5
private Double gstAmount;


@ManyToOne
@JoinColumn(name = "approver_id")
private User approver;


@OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
@JsonIgnore
private List<ExpenseItem> items;


@OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
@JsonManagedReference
private List<Receipt> receipts;

@OneToOne(mappedBy = "expense", cascade = CascadeType.ALL)
@JsonIgnore
private Payment payment;


@OneToMany(mappedBy = "expense", cascade = CascadeType.ALL)
@JsonIgnore
private List<Approval> approvals;


//default constructer
public Expense() {
	
}

//parameteriesed constructer
public Expense(Long id, User user, String title, Double totalAmount, String currency, String category, Long projectId,
		String status, LocalDateTime dateOfExpense, LocalDateTime submittedAt, LocalDateTime approvedAt, String notes,
		User approver, List<ExpenseItem> items, List<Receipt> receipts, Payment payment, List<Approval> approvals ,String gstNumber,Double gstAmount) {
	super();
	this.id = id;
	this.user = user;
	this.title = title;
	this.totalAmount = totalAmount;
	this.currency = currency;
	this.category = category;
	this.projectId = projectId;
	this.status = status;
	this.dateOfExpense = dateOfExpense;
	this.submittedAt = submittedAt;
	this.approvedAt = approvedAt;
	this.notes = notes;
	this.approver = approver;
	this.items = items;
	this.receipts = receipts;
	this.payment = payment;
	this.approvals = approvals;
	this.gstNumber =gstNumber;
	this.gstAmount = gstAmount;
}

//getter setter
public String getGstNumber() {
    return gstNumber;
}

public void setGstNumber(String gstNumber) {
    this.gstNumber = gstNumber;
}

public Double getGstAmount() {
    return gstAmount;
}

public void setGstAmount(Double gstAmount) {
    this.gstAmount = gstAmount;
}

public Long getId() {
	return id;
}


public void setId(Long id) {
	this.id = id;
}


public User getUser() {
	return user;
}


public void setUser(User user) {
	this.user = user;
}


public String getTitle() {
	return title;
}


public void setTitle(String title) {
	this.title = title;
}


public Double getTotalAmount() {
	return totalAmount;
}


public void setTotalAmount(Double totalAmount) {
	this.totalAmount = totalAmount;
}


public String getCurrency() {
	return currency;
}


public void setCurrency(String currency) {
	this.currency = currency;
}


public String getCategory() {
	return category;
}


public void setCategory(String category) {
	this.category = category;
}


public Long getProjectId() {
	return projectId;
}


public void setProjectId(Long projectId) {
	this.projectId = projectId;
}


public String getStatus() {
	return status;
}


public void setStatus(String status) {
	this.status = status;
}


public LocalDateTime getDateOfExpense() {
	return dateOfExpense;
}


public void setDateOfExpense(LocalDateTime dateOfExpense) {
	this.dateOfExpense = dateOfExpense;
}


public LocalDateTime getSubmittedAt() {
	return submittedAt;
}


public void setSubmittedAt(LocalDateTime submittedAt) {
	this.submittedAt = submittedAt;
}


public LocalDateTime getApprovedAt() {
	return approvedAt;
}


public void setApprovedAt(LocalDateTime approvedAt) {
	this.approvedAt = approvedAt;
}


public String getNotes() {
	return notes;
}


public void setNotes(String notes) {
	this.notes = notes;
}


public User getApprover() {
	return approver;
}


public void setApprover(User approver) {
	this.approver = approver;
}


public List<ExpenseItem> getItems() {
	return items;
}


public void setItems(List<ExpenseItem> items) {
	this.items = items;
}


public List<Receipt> getReceipts() {
	return receipts;
}


public void setReceipts(List<Receipt> receipts) {
	this.receipts = receipts;
}


public Payment getPayment() {
	return payment;
}


public void setPayment(Payment payment) {
	this.payment = payment;
}


public List<Approval> getApprovals() {
	return approvals;
}


public void setApprovals(List<Approval> approvals) {
	this.approvals = approvals;
}

}