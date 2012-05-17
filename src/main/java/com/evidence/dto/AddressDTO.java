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

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.evidence.fe.annotation.AutomaticForm;
import com.evidence.fe.annotation.Caption;
import com.evidence.fe.annotation.Order;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@AutomaticForm
public class AddressDTO {
	
	@Getter @Setter
	@NotEmpty
	@Order(1) @Caption("address.detail.street")
	private String street = "";
	
	@Getter @Setter
	@NotBlank
	@Length(min=5)
	@Order(2) @Caption("address.detail.city")
	private String city = "";
	
	@Getter @Setter
	@NotEmpty
	@Order(3) @Caption("address.detail.houseNumber")
	private String houseNumber = "";
	
	@Getter @Setter
	@NotEmpty
	@Order(4) @Caption("address.detail.zipCode")
	private String zipCode = "";

	@Getter @Setter
	@NotEmpty
	@Order(5) @Caption("address.detail.stateCode")
	private String stateCode;
}
