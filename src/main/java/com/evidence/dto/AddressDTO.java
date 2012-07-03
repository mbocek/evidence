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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.NotBlank;

import com.evidence.fe.annotation.AutomaticForm;
import com.evidence.fe.annotation.Caption;
import com.evidence.fe.annotation.Order;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString
@AutomaticForm
public class AddressDTO {
	
	@NotBlank
	@Getter @Setter
	@Order(1) @Caption
	private String street = "";
	
	@NotBlank
	@Getter @Setter
	@Order(2) @Caption
	private String city = "";
	
	@NotBlank
	@Getter @Setter
	@Order(3) @Caption
	private String houseNumber = "";
	
	@NotBlank
	@Getter @Setter
	@Order(4) @Caption
	private String zipCode = "";

	@NotBlank
	@Getter @Setter
	@Order(5) @Caption
	private String stateCode;
	
	@Getter @Setter
	private StateDTO state;
}
