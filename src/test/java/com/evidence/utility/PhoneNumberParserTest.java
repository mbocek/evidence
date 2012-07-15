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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class PhoneNumberParserTest {

	private static final String SHORT_NUMBER = "123456789";

	private PhoneNumberParser regular = new PhoneNumberParser("+420123456789");
	
	private PhoneNumberParser oldFasion = new PhoneNumberParser("00420123456789");
	
	private PhoneNumberParser shortNumber = new PhoneNumberParser(SHORT_NUMBER);

	@Test
	public void testRegularPhoneNumber() {
		final PhoneNumberParser regularNumber = regular.parse();
		assertEquals("Country code isn't equal to expected", regularNumber.getCountryCode(), "+420"); 
		assertEquals("Phone number isn't equal to expected", regularNumber.getPhoneNumber(), SHORT_NUMBER); 
	}

	@Test
	public void testOldFasionPhoneNumber() {
		final PhoneNumberParser oldFasionNumber = oldFasion.parse();
		assertEquals("Country code isn't equal to expected", oldFasionNumber.getCountryCode(), "+420"); 
		assertEquals("Phone number isn't equal to expected", oldFasionNumber.getPhoneNumber(), SHORT_NUMBER); 
	}

	@Test
	public void testShortPhoneNumber() {
		final PhoneNumberParser shortNumberPhoneNumber = shortNumber.parse();
		assertEquals("Country code isn't equal to expected", shortNumberPhoneNumber.getCountryCode(), "+420"); 
		assertEquals("Phone number isn't equal to expected", shortNumberPhoneNumber.getPhoneNumber(), SHORT_NUMBER); 
	}
}
