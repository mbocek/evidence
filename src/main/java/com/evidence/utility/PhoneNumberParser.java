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
package com.evidence.utility;

import lombok.Getter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class PhoneNumberParser {

	private static final String COUNTRY_CODE_PREFIX = "+";
	
	private static final String OLD_COUNTRY_CODE_PREFIX = "00";

	public static final String DAFAULT_COUNTRY_CODE = "+420";

	@Getter
	private String countryCode; 

	@Getter
	private String phoneNumber; 
	
	private final String data; 

	public PhoneNumberParser(final String data) {
		this.data = data;
	}

	public PhoneNumberParser parse() {
		if (this.data.startsWith(COUNTRY_CODE_PREFIX)) {
			this.countryCode = this.data.substring(0, 4);
			this.phoneNumber = this.data.substring(4);
		} else if (this.data.startsWith(OLD_COUNTRY_CODE_PREFIX)) {
			this.countryCode = COUNTRY_CODE_PREFIX + this.data.substring(2, 5);
			this.phoneNumber = this.data.substring(5);
		} else {
			this.countryCode = DAFAULT_COUNTRY_CODE;
			this.phoneNumber = this.data;
		}
		return this;
	}
}
