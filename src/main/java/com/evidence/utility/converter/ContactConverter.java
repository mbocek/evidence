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
package com.evidence.utility.converter;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import com.evidence.dto.ContactDTO;
import com.evidence.dto.KindergartenDTO;
import com.evidence.entity.Address;
import com.evidence.entity.Contact;
import com.evidence.entity.EmailAddress;
import com.evidence.entity.PhoneNumber;
import com.evidence.utility.DTOConverter;
import com.evidence.utility.PhoneNumberParser;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ContactConverter implements CustomConverter {

	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Object convert(final Object destination, final Object source, final Class destClass, final Class sourceClass) {
		Object result = null;
		if (source instanceof ContactDTO) {
			final Contact contact = convert((ContactDTO) source);
			result = contact;
		} else if (source instanceof KindergartenDTO) {
			throw new MappingException("Only Contact entity can be converted to ContactDTO");
		} else {
			throw new MappingException("Converter ContactConverter used incorrectly. Arguments passed were:"
					+ destination + " and " + source);
		}
		return result;
	}

	private Contact convert(final ContactDTO contactDTO) {
		final Contact contact = new Contact();
		contact.setAddress(DTOConverter.convert(contactDTO.getAddress(), Address.class));
		contact.setEmail(new EmailAddress(contactDTO.getEmail()));
		final PhoneNumberParser landLine = new PhoneNumberParser(contactDTO.getLandLine()).parse();
		contact.setLandLine(new PhoneNumber(landLine.getCountryCode(), landLine.getPhoneNumber()));
		final PhoneNumberParser mobilePhone = new PhoneNumberParser(contactDTO.getLandLine()).parse();
		contact.setMobilePhone(new PhoneNumber(mobilePhone.getCountryCode(), mobilePhone.getPhoneNumber()));
		return contact;
	}
}
