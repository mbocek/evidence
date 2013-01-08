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
package com.evidence.entity.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Entity
@Table(name = "USER_ENTITY")
@ToString
public class User implements Serializable {
	
	private static final long serialVersionUID = -4659043714451032587L;

	// should be email
	@Id
	@Getter @Setter
	@Column(name = "USER_NAME", nullable = false, length = 255)
	private String username;
	
	@Getter @Setter
	@Column(name = "PASSWORD", nullable = false, length = 64)
	private String password;
	
	@Getter
	@Column(name = "ENABLED", nullable = false)
	private boolean enabled;
	
	@Getter @Setter
	@Column(name = "NAME", nullable = false, length = 255)
	private String name;
	
	@Getter @Setter
	@Column(name = "SUR_NAME", nullable = false, length = 255)
	private String surName;
	
	@Getter @Setter
	@Column(name = "EMAIL", nullable = false, length = 255)
	private String email;

	@ElementCollection(targetClass = Role.class)
	@JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "USER_NAME"))
	@Column(name = "ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private Collection<Role> roles;
	
	public Collection<Role> getRoles() {
		return Collections.unmodifiableCollection(this.roles);
	}
}
