package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "approvals")
public class Approval {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@ManyToOne
@JoinColumn(name = "expense_id")
private Expense expense;


@ManyToOne
@JoinColumn(name = "approver_id")
private User approver;

private String action;
private String comment;
private LocalDateTime actionAt;

//default constructer
public Approval()
{
	
}

//parameteriesed constructer
public Approval(Long id, Expense expense, User approver, String action, String comment, LocalDateTime actionAt) {
	super();
	this.id = id;
	this.expense = expense;
	this.approver = approver;
	this.action = action;
	this.comment = comment;
	this.actionAt = actionAt;
}

//Getters & Setters
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public Expense getExpense() {
	return expense;
}
public void setExpense(Expense expense) {
	this.expense = expense;
}
public User getApprover() {
	return approver;
}
public void setApprover(User approver) {
	this.approver = approver;
}
public String getAction() {
	return action;
}
public void setAction(String action) {
	this.action = action;
}
public String getComment() {
	return comment;
}
public void setComment(String comment) {
	this.comment = comment;
}
public LocalDateTime getActionAt() {
	return actionAt;
}
public void setActionAt(LocalDateTime actionAt) {
	this.actionAt = actionAt;
}
}
