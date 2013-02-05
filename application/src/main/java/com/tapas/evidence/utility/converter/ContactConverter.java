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
package com.tapas.evidence.utility.converter;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import com.tapas.evidence.dto.AddressDTO;
import com.tapas.evidence.dto.ContactDTO;
import com.tapas.evidence.entity.Address;
import com.tapas.evidence.entity.Contact;
import com.tapas.evidence.entity.EmailAddress;
import com.tapas.evidence.entity.PhoneNumber;
import com.tapas.evidence.utility.DTOConverter;
import com.tapas.evidence.utility.PhoneNumberParser;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ContactConverter implements CustomConverter {

	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destClass, final Class<?> sourceClass) {
		Object result = null;
		if (source instanceof ContactDTO) {
			final Contact contact = convert((ContactDTO) source, (Contact) destination);
			result = contact;
		} else if (source instanceof Contact) {
			final ContactDTO contact = convert((Contact) source);
			result = contact;
		} else if (source != null) {
			throw new MappingException("Converter ContactConverter used incorrectly. Arguments passed were:"
					+ destination + " and " + source);
		}
		return result;
	}

	private ContactDTO convert(final Contact source) {
		final ContactDTO contactDTO = new ContactDTO();
		contactDTO.setAddress(DTOConverter.convert(source.getAddress(), AddressDTO.class));
		contactDTO.setEmail(source.getEmail().getEmail());
		contactDTO.setLandLine(source.getLandLine().getFullNumber());
		contactDTO.setMobileNumber(source.getMobilePhone().getFullNumber());
		return contactDTO;
	}

	private Contact convert(final ContactDTO sourceDTO, final Contact destination) {
		final Contact contact = destination == null ? new Contact() : destination;
		Address address = contact.getAddress();
		if (address == null) {
			address = DTOConverter.convert(sourceDTO.getAddress(), Address.class);
		} else {
			DTOConverter.convert(sourceDTO.getAddress(), address);
		}
		contact.setAddress(address);
		contact.setEmail(new EmailAddress(sourceDTO.getEmail()));
		final PhoneNumberParser landLine = new PhoneNumberParser(sourceDTO.getLandLine()).parse();
		contact.setLandLine(new PhoneNumber(landLine.getCountryCode(), landLine.getPhoneNumber()));
		final PhoneNumberParser mobilePhone = new PhoneNumberParser(sourceDTO.getLandLine()).parse();
		contact.setMobilePhone(new PhoneNumber(mobilePhone.getCountryCode(), mobilePhone.getPhoneNumber()));
		return contact;
	}
}
