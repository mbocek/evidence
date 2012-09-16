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

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.NotBlank;

import com.evidence.fe.annotation.AutomaticForm;
import com.evidence.fe.annotation.Caption;
import com.evidence.fe.annotation.Order;
import com.evidence.fe.form.Model;
import com.evidence.fe.teacher.TeacherDetailFormFieldFactory;
import com.evidence.utility.builder.FullAddressBuilder;
import com.evidence.utility.builder.FullNameBuilder;

/**
 * @author Michal Bocek
 * @since 1.0.0 
 */
@ToString
@AutomaticForm(formFieldFactory = TeacherDetailFormFieldFactory.class)
public class TeacherDTO implements Model { 

	@Getter @Setter
	private Long id;
	
	@Getter @Setter
	@Order(1) @Caption("kindergarten")
	private Long kindergartenId;
	
	@NotBlank
	@Order(2)
	@Caption
	@Getter	@Setter
	private String name = "";
	
	@NotBlank
	@Order(3)
	@Caption
	@Getter	@Setter
	private String surName = "";
	
	@Order(4)
	@Caption
	@NotNull
	@Getter @Setter
	private Date birthDate;
	
	@Order(5)
	@Valid
	@Getter @Setter
	private ContactDTO contact = new ContactDTO();

	public String getFullName() {
		return new FullNameBuilder(name, surName).getFullName();
	}

	public String getFullAddress() {
		String result = "";
		if (contact != null && contact.getAddress() != null && contact.getAddress().getState() != null) {
			result = new FullAddressBuilder(contact.getAddress()).getFullAddress();
		}
		return result;
	}
}
