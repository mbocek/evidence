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
package com.evidence.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.evidence.utility.PhoneNumberParser;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Embeddable
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Column(nullable = false)
	private String countryCode;
	
	@Getter
	@Column(nullable = false)
	private String number;
	
	public String getFullNumber() {
		final StringBuffer result = new StringBuffer(countryCode == null ? PhoneNumberParser.DAFAULT_COUNTRY_CODE : countryCode);
		result.append(number);
		return result.toString();
	}
}
