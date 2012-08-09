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

import com.evidence.dto.AddressDTO;
import com.evidence.dto.StateDTO;
import com.evidence.entity.Address;
import com.evidence.entity.State;
import com.evidence.utility.DTOConverter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class AddressConverter implements CustomConverter {

	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destClass, final Class<?> sourceClass) {
		Object result = null;
		if (source instanceof AddressDTO) {
			final Address address = convert((AddressDTO) source, (Address) destination);
			result = address;
		} else if (source instanceof Address) {
			final AddressDTO address = convert((Address) source);
			result = address;
		} else if (source != null) {
			throw new MappingException("Converter AddressConverter used incorrectly. Arguments passed were:"
					+ destination + " and " + source);
		}
		return result;
	}

	private AddressDTO convert(final Address address) {
		final AddressDTO addressDTO = new AddressDTO();
		addressDTO.setCity(address.getCity());
		addressDTO.setHouseNumber(address.getHouseNumber());
		final StateDTO stateDTO = DTOConverter.convert(address.getState(), StateDTO.class);
		addressDTO.setState(stateDTO);
		final String stateCode = stateDTO == null ? null : stateDTO.getCode();
		addressDTO.setStateCode(stateCode);
		addressDTO.setStreet(address.getStreet());
		addressDTO.setZipCode(address.getZipCode());
		return addressDTO;
	}

	private Address convert(final AddressDTO addressDTO, final Address destination) {
		final Address address = destination == null ? new Address() : destination;
		address.setCity(addressDTO.getCity());
		address.setHouseNumber(addressDTO.getHouseNumber());
		final State state = new State();
		state.setCode(addressDTO.getStateCode());
		address.setState(state);
		address.setStreet(addressDTO.getStreet());
		address.setZipCode(addressDTO.getZipCode());
		return address;
	}
}
