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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import javax.inject.Inject;

import org.junit.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;

import com.evidence.data.DbUnitTest;
import com.evidence.entity.user.Role;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ContextConfiguration({"classpath:spring/security.xml"})
public class UserDetailsServiceImplTest extends DbUnitTest {

	@Inject
	private UserDetailsService userDeailsService;

	@Inject
	private PasswordEncoder passwordEncoder;
	
	@Inject
	private SaltSource saltSource;
	
	@Inject
	private AuthenticationManager authenticationManager;
	/**
	 * Test method for {@link com.evidence.service.UserDetailsServiceImpl#loadUserByUsername(java.lang.String)}.
	 */
	@Test
	public void testLoadUserByUsername() {
		UserDetails user = userDeailsService.loadUserByUsername("admin@evidence.com");
		assertNotNull(user);
		assertTrue(user.getAuthorities().size() == 2);
		ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
		list.add(new SimpleGrantedAuthority(Role.ROLE_ADMINISTRATOR.name()));
		assertTrue(user.getAuthorities().containsAll(list));
	}
	
	@Test
	public void testPasswordEncoding() {
		ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
		list.add(new SimpleGrantedAuthority(Role.ROLE_ADMINISTRATOR.name()));
		User u = new User("admin@evidence.com", "password", list);
		System.out.println(saltSource.getSalt(u));
	    System.out.println(passwordEncoder.encodePassword("password", saltSource.getSalt(u)));
	    
	    Authentication authentication = new UsernamePasswordAuthenticationToken("admin@evidence.com", "password");
	    try {
	    	authenticationManager.authenticate(authentication);
	    } catch (BadCredentialsException e) {
	    	fail("Problem with authentication: user/password");
	    }
	}
}
