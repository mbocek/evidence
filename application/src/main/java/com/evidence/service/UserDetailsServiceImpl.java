/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.evidence.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.evidence.entity.user.Role;
import com.evidence.entity.user.User;
import com.evidence.repository.UserRepository;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Validated
@Service("userDetailsService") 
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

	@Inject
	private UserRepository repository;
	 
	private static class UserDetailBuilder {

		private final User user;
		
		public UserDetailBuilder(User user) {
			this.user = user;
		}

		public UserDetails build() {
			String username = user.getUsername();
			String password = user.getPassword();
			boolean enabled = user.isEnabled();
			boolean accountNonExpired = user.isEnabled();
			boolean credentialsNonExpired = user.isEnabled();
			boolean accountNonLocked = user.isEnabled();

			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (Role role : user.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.name()));
			}

			UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, password,
					enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
			return userDetails;
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		User user = repository.findByUsername(username);
		UserDetails userDetails = null;
		if (user != null) {
			userDetails = new UserDetailBuilder(user).build();
		} else {
			throw new UsernameNotFoundException("User with name: " + username + " not found");
		}
		return userDetails;
	}
}
