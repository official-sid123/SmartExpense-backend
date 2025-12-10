package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "payments")
public class Payment {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


@OneToOne
@JoinColumn(name = "expense_id")
private Expense expense;
private String gateway;
private String gatewayPayoutId;
private Double amount;
private String currency;


@Column(columnDefinition = "json")
private String beneficiaryDetails;

//default Constructer
public Payment()
{
	}

//parametriesed constructer
public Payment(Long id, Expense expense, String gateway, String gatewayPayoutId, Double amount, String currency,
		String beneficiaryDetails, String status, LocalDateTime initiatedAt, LocalDateTime completedAt,
		String errorMessage) {
	super();
	this.id = id;
	this.expense = expense;
	this.gateway = gateway;
	this.gatewayPayoutId = gatewayPayoutId;
	this.amount = amount;
	this.currency = currency;
	this.beneficiaryDetails = beneficiaryDetails;
	this.status = status;
	this.initiatedAt = initiatedAt;
	this.completedAt = completedAt;
	this.errorMessage = errorMessage;
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
public String getGateway() {
	return gateway;
}
public void setGateway(String gateway) {
	this.gateway = gateway;
}
public String getGatewayPayoutId() {
	return gatewayPayoutId;
}
public void setGatewayPayoutId(String gatewayPayoutId) {
	this.gatewayPayoutId = gatewayPayoutId;
}
public Double getAmount() {
	return amount;
}
public void setAmount(Double amount) {
	this.amount = amount;
}
public String getCurrency() {
	return currency;
}
public void setCurrency(String currency) {
	this.currency = currency;
}
public String getBeneficiaryDetails() {
	return beneficiaryDetails;
}
public void setBeneficiaryDetails(String beneficiaryDetails) {
	this.beneficiaryDetails = beneficiaryDetails;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public LocalDateTime getInitiatedAt() {
	return initiatedAt;
}
public void setInitiatedAt(LocalDateTime initiatedAt) {
	this.initiatedAt = initiatedAt;
}
public LocalDateTime getCompletedAt() {
	return completedAt;
}
public void setCompletedAt(LocalDateTime completedAt) {
	this.completedAt = completedAt;
}
public String getErrorMessage() {
	return errorMessage;
}
public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}
private String status;
private LocalDateTime initiatedAt;
private LocalDateTime completedAt;
private String errorMessage;



}