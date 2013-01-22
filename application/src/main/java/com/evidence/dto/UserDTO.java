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
package com.evidence.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.evidence.entity.user.Role;
import com.evidence.fe.ApplicationConstants;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString(exclude = "password")
public class UserDTO implements Serializable {

	private static final long serialVersionUID = ApplicationConstants.VERSION;

	@Size(min = 6, max = 255)
	@Getter @Setter
	private String userName;
	
	@Size(min = 8, max = 255)
	@Getter @Setter
	private String password;
	
	@Getter @Setter
	private Boolean enabled = Boolean.FALSE;

	@Size(min = 1, max = 255)
	@Getter @Setter
	private String name;
	
	@Size(min = 1, max = 255)
	@Getter @Setter
	private String surName;
	
	@Getter @Setter
	private Long tenantId;
	
	@Getter @Setter
	private String tenantName;

	@Getter @Setter
	private String captcha;
	
	@Getter @Setter
	private List<TenantDTO> tenantList;
	
	@Getter
	private final Collection<Role> roles = new ArrayList<Role>();
	
	public void addRole(final Role role) {
		roles.add(role);
	}
}
