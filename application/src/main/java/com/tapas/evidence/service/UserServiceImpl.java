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
package com.tapas.evidence.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.tapas.evidence.credential.EvidenceUser;
import com.tapas.evidence.dto.TenantDTO;
import com.tapas.evidence.dto.UserDTO;
import com.tapas.evidence.entity.user.Role;
import com.tapas.evidence.entity.user.Tenant;
import com.tapas.evidence.entity.user.User;
import com.tapas.evidence.repository.TenantRepository;
import com.tapas.evidence.repository.UserRepository;
import com.tapas.evidence.utility.DTOConverter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Service("userService")
@Validated
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	@Inject
	private UserRepository userRepository;
	 
	@Inject
	private TenantRepository tenantRepository;

	@Inject
	private PasswordEncoder passwordEncoder;
	
	@Inject
	private SaltSource saltSource;
	
	private static class UserDetailBuilder {

		private final User user;
		
		public UserDetailBuilder(User user) {
			this.user = user;
		}

		public UserDetails build() {
			final String username = user.getUsername();
			final String password = user.getPassword();
			final boolean enabled = user.isEnabled();
			final boolean accountNonExpired = user.isEnabled();
			final boolean credentialsNonExpired = user.isEnabled();
			final boolean accountNonLocked = user.isEnabled();
			final Long tenantId = user.getTenant().getId();

			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (Role role : user.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.name()));
			}

			UserDetails userDetails = new EvidenceUser(username, password,
					enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, tenantId);
			return userDetails;
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		UserDetails userDetails = null;
		if (user != null) {
			userDetails = new UserDetailBuilder(user).build();
		} else {
			throw new UsernameNotFoundException("User with name: " + username + " not found");
		}
		return userDetails;
	}
	
	@Transactional
	@Override
	public void create(final UserDTO userDTO) throws TenantAlreadyExists, UserAlreadyExists {
		log.info("Registering user: {}", userDTO);
		User user = DTOConverter.convert(userDTO, User.class);
		
		if (userDTO.getTenantId() != null) {
			Tenant tenant = tenantRepository.read(userDTO.getTenantId());
			user.setTenant(tenant);
		}
		
		if (userRepository.findByUsername(userDTO.getUserName()) != null) {
			throw new UserAlreadyExists();
		}
		
		user.setEmail(user.getUsername());
		Collection<Role> roles = new ArrayList<Role>();
		
		if (userDTO.getTenantName() != null && !userDTO.getTenantName().isEmpty()) {
			user.setEnabled(true);
			roles.add(Role.ROLE_ADMINISTRATOR);
			// check if exist tenant
			if (tenantRepository.findByName(userDTO.getTenantName()).size() > 0) {
				throw new TenantAlreadyExists();
			}
		} else {
			user.setEnabled(false);
		}
		
		roles.add(Role.ROLE_USER);
		user.setRoles(roles);
		UserDetails userDetails = new EvidenceUser(user.getUsername(), "password", new ArrayList<GrantedAuthority>());
		user.setPassword(passwordEncoder.encodePassword(userDTO.getPassword(), saltSource.getSalt(userDetails)));
		userRepository.create(user);
	}

	@Override
	public List<TenantDTO> getTenantList() {
		return DTOConverter.convertList(tenantRepository.findAll(), TenantDTO.class);
	}
}
