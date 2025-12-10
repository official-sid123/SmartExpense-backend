package com.example.demo.dto;

public class UpdateProfileRequest {

    private String name;
    private String email;
    private String bankAccountNumber;
    private String bankIfsc;
    private String bankHolderName;
    private String bankName;
    private String bankBranch;
    private String upiId;   
    private Long reportingManagerId;
    private String profileImage;
    

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

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
	public Long getReportingManagerId() { return reportingManagerId; }
    public void setReportingManagerId(Long reportingManagerId) { this.reportingManagerId = reportingManagerId; }
}
