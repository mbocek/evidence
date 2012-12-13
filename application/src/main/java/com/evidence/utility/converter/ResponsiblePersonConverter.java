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
import com.evidence.dto.ResponsiblePersonDTO;
import com.evidence.entity.Contact;
import com.evidence.entity.Kindergarten;
import com.evidence.entity.ResponsibilityType;
import com.evidence.entity.ResponsiblePerson;
import com.evidence.utility.DTOConverter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ResponsiblePersonConverter implements CustomConverter {

	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destClass, final Class<?> sourceClass) {
		Object result = null;
		if (source instanceof ResponsiblePersonDTO) {
			final ResponsiblePerson responsiblePerson = convert((ResponsiblePersonDTO) source, (ResponsiblePerson) destination);
			result = responsiblePerson;
		} else if (source instanceof ResponsiblePerson) {
			final ResponsiblePersonDTO responsiblePerson = convert((ResponsiblePerson) source);
			result = responsiblePerson;
		} else if (source != null) {
			throw new MappingException("Converter ResponsiblePersonConverter used incorrectly. Arguments passed were:"
					+ destination + " and " + source);
		}
		return result;
	}

	private ResponsiblePersonDTO convert(final ResponsiblePerson source) {
		final ResponsiblePersonDTO responsiblePersonDTO = new ResponsiblePersonDTO(source.getKindergarten().getId());
		responsiblePersonDTO.setContact(DTOConverter.convert(source.getContact(), ContactDTO.class));
		responsiblePersonDTO.setBirthDate(source.getBirthDate());
		responsiblePersonDTO.setName(source.getName());
		responsiblePersonDTO.setSurName(source.getSurName());
		responsiblePersonDTO.setType(source.getType().name());
		responsiblePersonDTO.setId(source.getId());
		return responsiblePersonDTO;
	}

	private ResponsiblePerson convert(final ResponsiblePersonDTO sourceDTO, final ResponsiblePerson destination) {
		final ResponsiblePerson responsiblePerson = destination == null ? new ResponsiblePerson() : destination;
		Contact contact = responsiblePerson.getContact();
		if (contact == null) {
			contact = DTOConverter.convert(sourceDTO.getContact(), Contact.class);
		} else {
			DTOConverter.convert(sourceDTO.getContact(), contact);
		}
		responsiblePerson.setContact(contact);
		responsiblePerson.setBirthDate(sourceDTO.getBirthDate());
		responsiblePerson.setKindergarten(new Kindergarten(sourceDTO.getKindergartenId()));
		responsiblePerson.setName(sourceDTO.getName());
		responsiblePerson.setSurName(sourceDTO.getSurName());
		responsiblePerson.setType(ResponsibilityType.valueOf(sourceDTO.getType()));
		return responsiblePerson;
	}
}
