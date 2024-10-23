package com.bikash.service;

import com.bikash.entity.User;

public interface UserService {
	
	
	 User findByEmail(String email);
	    void saveUser(User user);
	    void updatePassword(String email, String password);

}
