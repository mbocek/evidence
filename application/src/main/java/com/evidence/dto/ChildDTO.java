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

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.NotBlank;

import com.evidence.fe.child.ChildDetailFormFieldFactory;
import com.evidence.fe.form.AutomaticForm;
import com.evidence.fe.form.Caption;
import com.evidence.fe.form.Model;
import com.evidence.fe.form.Order;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString
@NoArgsConstructor
@AutomaticForm(formFieldFactory = ChildDetailFormFieldFactory.class)
public class ChildDTO implements Model {

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
	
	@Getter @Setter
	@Order(5) @Caption("mother")
	private Long motherId;

	@Getter @Setter
	@Order(6) @Caption("father")
	private Long fatherId;

	@Getter @Setter
	@Order(7) @Caption("responsiblePerson")
	private Long responsiblePersonId;
	
	public ChildDTO(final Long kindergartenId) {
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
}
