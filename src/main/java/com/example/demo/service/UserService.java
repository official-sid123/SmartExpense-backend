package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UpdateProfileRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	 @Autowired
	    private UserRepository userRepository;
	    
	 public List<User> getAllManagers() {
		    List<User> list = userRepository.findByRoleIgnoreCase("ROLE_MANAGER");
		    return list != null ? list : List.of();   // safe → never returns null
		}

	 
	 public User updateProfile(Long userId, UpdateProfileRequest req) {

		    User user = userRepository.findById(userId)
		            .orElseThrow(() -> new RuntimeException("User not found"));

		    user.setName(req.getName());
		    user.setEmail(req.getEmail());

		    // 🔥 Separate bank details
		    user.setBankAccountNumber(req.getBankAccountNumber());
		    user.setBankIfsc(req.getBankIfsc());
		    user.setBankHolderName(req.getBankHolderName());
		    user.setBankName(req.getBankName());
		    user.setBankBranch(req.getBankBranch());
		    user.setUpiId(req.getUpiId());
		    user.setProfileImage(req.getProfileImage());

		    // Reporting Manager update
		    if (req.getReportingManagerId() != null) {
		        User manager = userRepository.findById(req.getReportingManagerId())
		                .orElseThrow(() -> new RuntimeException("Manager not found"));
		        user.setReportingManager(manager);
		    }

		    return userRepository.save(user);
		}


}
