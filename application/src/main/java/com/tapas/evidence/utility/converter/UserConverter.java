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
package com.tapas.evidence.utility.converter;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import com.tapas.evidence.dto.UserDTO;
import com.tapas.evidence.entity.user.Role;
import com.tapas.evidence.entity.user.Tenant;
import com.tapas.evidence.entity.user.User;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class UserConverter implements CustomConverter {

	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destClass, final Class<?> sourceClass) {
		Object result = null;
		if (source instanceof UserDTO) {
			final User teacher = convert((UserDTO) source, (User) destination);
			result = teacher;
		} else if (source instanceof User) {
			final UserDTO teacher = convert((User) source);
			result = teacher;
		} else if (source != null) {
			throw new MappingException("Converter UserConverter used incorrectly. Arguments passed were:"
					+ destination + " and " + source);
		}
		return result;
	}

	private UserDTO convert(final User source) {
		final UserDTO userDTO = new UserDTO();
		userDTO.setEnabled(source.isEnabled());
		userDTO.setName(source.getName());
		userDTO.setSurName(source.getSurName());
		userDTO.setPassword(source.getPassword());
		if (source.getTenant() != null) {
			userDTO.setTenantId(source.getTenant().getId());
			userDTO.setTenantName(source.getTenant().getName());
		}
		userDTO.setUserName(source.getUsername());
		for (Role role : source.getRoles()) {
			userDTO.addRole(role);
		}
		return userDTO;
	}

	private User convert(final UserDTO sourceDTO, final User destination) {
		final User user = destination == null ? new User() : destination;
		user.setEnabled(sourceDTO.getEnabled());
		user.setName(sourceDTO.getName());
		user.setPassword(sourceDTO.getPassword());
		user.setSurName(sourceDTO.getSurName());
		user.setUsername(sourceDTO.getUserName());
		if (sourceDTO.getTenantName() != null) {
			user.setTenant(new Tenant(sourceDTO.getTenantId(), sourceDTO.getTenantName()));
		}		
		user.setRoles(sourceDTO.getRoles());
		return user;
	}
}
