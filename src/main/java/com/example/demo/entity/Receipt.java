package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "receipts")
public class Receipt {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@ManyToOne
	@JoinColumn(name = "expense_id")
	@JsonBackReference
	private Expense expense;


	private String filePath;


	@Column(columnDefinition = "json")
	private String ocrRawJson;


	private Double ocrConfidence;
	private LocalDateTime uploadedAt;

//	default constuster
public Receipt() {
	
}

//parameteriesed constructer
public Receipt(Long id, Expense expense, String filePath, String ocrRawJson, Double ocrConfidence,
			LocalDateTime uploadedAt) {
		super();
		this.id = id;
		this.expense = expense;
		this.filePath = filePath;
		this.ocrRawJson = ocrRawJson;
		this.ocrConfidence = ocrConfidence;
		this.uploadedAt = uploadedAt;
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
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getOcrRawJson() {
		return ocrRawJson;
	}
	public void setOcrRawJson(String ocrRawJson) {
		this.ocrRawJson = ocrRawJson;
	}
	public Double getOcrConfidence() {
		return ocrConfidence;
	}
	public void setOcrConfidence(Double ocrConfidence) {
		this.ocrConfidence = ocrConfidence;
	}
	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}
	public void setUploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
	}
}
