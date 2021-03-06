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
package com.tapas.evidence.dto;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.NotBlank;

import com.tapas.evidence.fe.form.AutomaticForm;
import com.tapas.evidence.fe.form.Caption;
import com.tapas.evidence.fe.form.Model;
import com.tapas.evidence.fe.form.Order;
import com.tapas.evidence.fe.responsible.ResponsiblePersonDetailFormFieldFactory;

/**
 * @author Michal Bocek
 * @since 1.0.0 
 */
@ToString(exclude = "photo")
@AutomaticForm(formFieldFactory = ResponsiblePersonDetailFormFieldFactory.class)
public class ResponsiblePersonDTO implements Model { 

	@Getter @Setter
	private Long id;
	
	@NotNull
	@Getter @Setter
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
	private Date birthDate = new Date();
	
	@Order(5)
	@Valid
	@Getter @Setter
	private ContactDTO contact = new ContactDTO();
	
	@NotNull
	@Order(6)
	@Caption
	@Getter @Setter
	private String type;
	
	@Order(7)
	@Getter @Setter
	private byte[] photo;
	
	public ResponsiblePersonDTO(final Long kindergartenId) {
		this.kindergartenId = kindergartenId;
	}
	
	public Date getBirthDate() {
		return new Date(birthDate.getTime());
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = new Date(birthDate.getTime());
	}

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
