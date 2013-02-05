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

import com.tapas.evidence.dto.ContactDTO;
import com.tapas.evidence.dto.TeacherDTO;
import com.tapas.evidence.entity.Contact;
import com.tapas.evidence.entity.Kindergarten;
import com.tapas.evidence.entity.Teacher;
import com.tapas.evidence.utility.DTOConverter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class TeacherConverter implements CustomConverter {

	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destClass, final Class<?> sourceClass) {
		Object result = null;
		if (source instanceof TeacherDTO) {
			final Teacher teacher = convert((TeacherDTO) source, (Teacher) destination);
			result = teacher;
		} else if (source instanceof Teacher) {
			final TeacherDTO teacher = convert((Teacher) source);
			result = teacher;
		} else if (source != null) {
			throw new MappingException("Converter TeacherConverter used incorrectly. Arguments passed were:"
					+ destination + " and " + source);
		}
		return result;
	}

	private TeacherDTO convert(final Teacher source) {
		final TeacherDTO teacherDTO = new TeacherDTO(source.getKindergarten().getId());
		teacherDTO.setContact(DTOConverter.convert(source.getContact(), ContactDTO.class));
		teacherDTO.setBirthDate(source.getBirthDate());
		teacherDTO.setName(source.getName());
		teacherDTO.setSurName(source.getSurName());
		teacherDTO.setId(source.getId());
		teacherDTO.setPhoto(source.getPhoto());
		return teacherDTO;
	}

	private Teacher convert(final TeacherDTO sourceDTO, final Teacher destination) {
		final Teacher teacher = destination == null ? new Teacher() : destination;
		Contact contact = teacher.getContact();
		if (contact == null) {
			contact = DTOConverter.convert(sourceDTO.getContact(), Contact.class);
		} else {
			DTOConverter.convert(sourceDTO.getContact(), contact);
		}
		teacher.setContact(contact);
		teacher.setBirthDate(sourceDTO.getBirthDate());
		teacher.setKindergarten(new Kindergarten(sourceDTO.getKindergartenId()));
		teacher.setName(sourceDTO.getName());
		teacher.setSurName(sourceDTO.getSurName());
		teacher.setPhoto(sourceDTO.getPhoto());
		return teacher;
	}
}
