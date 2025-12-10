package com.example.demo.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Table(name = "users")
@JsonIgnoreProperties({"expenses", "approvals"}) // prevent infinite loop
public class User {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;


private String name;
private String email;
private String role;
private String password;

@JsonIgnore
@ManyToOne
@JoinColumn(name = "reporting_manager_id")
private User reportingManager;


public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}


private String bankAccountNumber;
private String bankIfsc;
private String bankHolderName;
private String bankName;
private String bankBranch;
private String upiId;


private Long orgId;
private LocalDateTime createdAt;


@OneToMany(mappedBy = "user")
@JsonIgnore  // <--- most important
private List<Expense> expenses;



@OneToMany(mappedBy = "approver")
@JsonIgnore  // <--- also needed
private List<Approval> approvals;

@Column(name = "profile_image")
private String profileImage;

//default Constructer 
public User() {
	
}


public String getBankAccountNumber() {
	return bankAccountNumber;
}

public void setBankAccountNumber(String bankAccountNumber) {
	this.bankAccountNumber = bankAccountNumber;
}

public String getBankIfsc() {
	return bankIfsc;
}

public void setBankIfsc(String bankIfsc) {
	this.bankIfsc = bankIfsc;
}

public String getBankHolderName() {
	return bankHolderName;
}

public void setBankHolderName(String bankHolderName) {
	this.bankHolderName = bankHolderName;
}

public String getBankName() {
	return bankName;
}

public void setBankName(String bankName) {
	this.bankName = bankName;
}

public String getBankBranch() {
	return bankBranch;
}

public void setBankBranch(String bankBranch) {
	this.bankBranch = bankBranch;
}

public String getUpiId() {
	return upiId;
}

public void setUpiId(String upiId) {
	this.upiId = upiId;
}

public User(Long id, String name, String email, String role, String password, User reportingManager,
		String bankAccountNumber, String bankIfsc, String bankHolderName, String bankName, String bankBranch,
		String upiId, Long orgId, LocalDateTime createdAt, List<Expense> expenses, List<Approval> approvals,String profileImage) {
	super();
	this.id = id;
	this.name = name;
	this.email = email;
	this.role = role;
	this.password = password;
	this.reportingManager = reportingManager;
	this.bankAccountNumber = bankAccountNumber;
	this.bankIfsc = bankIfsc;
	this.bankHolderName = bankHolderName;
	this.bankName = bankName;
	this.bankBranch = bankBranch;
	this.upiId = upiId;
	this.orgId = orgId;
	this.createdAt = createdAt;
	this.expenses = expenses;
	this.approvals = approvals;
	this.profileImage=profileImage;
}

public String getProfileImage() {
	return profileImage;
}

public void setProfileImage(String profileImage) {
	this.profileImage = profileImage;
}

//Getters & Setters
public User getReportingManager() {
    return reportingManager;
}

public void setReportingManager(User reportingManager) {
    this.reportingManager = reportingManager;
}

public Long getId() {
	return id;
}


public void setId(Long id) {
	this.id = id;
}


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}


public String getEmail() {
	return email;
}


public void setEmail(String email) {
	this.email = email;
}


public String getRole() {
	return role;
}


public void setRole(String role) {
	this.role = role;
}

public Long getOrgId() {
	return orgId;
}


public void setOrgId(Long orgId) {
	this.orgId = orgId;
}


public LocalDateTime getCreatedAt() {
	return createdAt;
}


public void setCreatedAt(LocalDateTime createdAt) {
	this.createdAt = createdAt;
}


public List<Expense> getExpenses() {
	return expenses;
}


public void setExpenses(List<Expense> expenses) {
	this.expenses = expenses;
}


public List<Approval> getApprovals() {
	return approvals;
}


public void setApprovals(List<Approval> approvals) {
	this.approvals = approvals;
}



}
