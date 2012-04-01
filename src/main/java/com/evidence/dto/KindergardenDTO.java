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

import javax.validation.constraints.NotNull;

import lombok.Delegate;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotEmpty;

import com.evidence.fe.annotation.AutomaticForm;
import com.evidence.fe.annotation.Caption;
import com.evidence.fe.annotation.Order;
import com.evidence.fe.form.Model;
import com.evidence.fe.kindergarden.KinedrgardenDetailFormFieldFactory;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@AutomaticForm(formFieldFactory=KinedrgardenDetailFormFieldFactory.class)
public class KindergardenDTO implements Model {

	@Getter @Setter
	@NotNull
	private Long id;
	
	@Getter @Setter
	@NotEmpty
	@Caption("kindergarden.detail.name")
	@Order(1)
	private String name;

	@Delegate
	@Order(2)
	private AddressDTO address = new AddressDTO();
	
	@Getter @Setter
	@Order(3)
	private String fullAddress; 
}
