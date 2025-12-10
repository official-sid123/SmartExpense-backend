package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // recipient
    private String title;
    @Column(columnDefinition = "text")
    private String body;
    
    public Notification() {
    	
    }
    
    public Notification(Long id, Long userId, String title, String body, Boolean readFlag, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.body = body;
		this.readFlag = readFlag;
		this.createdAt = createdAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Boolean getReadFlag() {
		return readFlag;
	}
	public void setReadFlag(Boolean readFlag) {
		this.readFlag = readFlag;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	private Boolean readFlag = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters / setters
    // default constructor
}
