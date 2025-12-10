package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "expense_items")
public class ExpenseItem {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@ManyToOne
@JoinColumn(name = "expense_id")
private Expense expense;
private String description;
private Double amount;
private Double taxAmount;
private String hsnSac;
private Integer qty;

//default constructer
public ExpenseItem() {
	
}

//parameteriesed Constructer
public ExpenseItem(Long id, Expense expense, String description, Double amount, Double taxAmount, String hsnSac,
		Integer qty) {
	super();
	this.id = id;
	this.expense = expense;
	this.description = description;
	this.amount = amount;
	this.taxAmount = taxAmount;
	this.hsnSac = hsnSac;
	this.qty = qty;
}

//getter setter
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
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public Double getAmount() {
	return amount;
}
public void setAmount(Double amount) {
	this.amount = amount;
}
public Double getTaxAmount() {
	return taxAmount;
}
public void setTaxAmount(Double taxAmount) {
	this.taxAmount = taxAmount;
}
public String getHsnSac() {
	return hsnSac;
}
public void setHsnSac(String hsnSac) {
	this.hsnSac = hsnSac;
}
public Integer getQty() {
	return qty;
}
public void setQty(Integer qty) {
	this.qty = qty;
}
}