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
package com.tapas.evidence.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

import com.tapas.evidence.fe.ApplicationConstants;

/**
 * @author Michal Bocek
 * @since 1.0.0 
 */
@MappedSuperclass
public abstract class Person extends TenantAware implements Serializable {

	private static final long serialVersionUID = ApplicationConstants.VERSION;
	
	@Getter	@Setter
	private String name;
	
	@Getter	@Setter
	@Column(name = "SUR_NAME")
	private String surName;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTH_DATE")
	private Date birthDate;

	@Getter	@Setter
	@Column(name = "DELETED", nullable = false)
	private Boolean deleted = Boolean.FALSE;

	@Getter @Setter
	@ManyToOne(cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(name = "KINDERGARTEN_ID")
	private Kindergarten kindergarten;
	
	public Date getBirthDate() {
		return new Date(birthDate.getTime());
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = new Date(birthDate.getTime());
	}
}
