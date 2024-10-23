package com.bikash.service;

import org.springframework.stereotype.Service;

import com.bikash.entity.User;
import com.bikash.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService{
	
	private UserRepository userRepository;

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email) ;
	}

	@Override
	public void saveUser(User user) {
		userRepository.save(user);
		
	}

	@Override
	public void updatePassword(String email, String password) {
		User user = userRepository.findByEmail(email);
		if(user!=null) {
			user.setPassword(password);
			user.setAccountLocked(false);
			userRepository.save(user);
		}
		
	}

}
