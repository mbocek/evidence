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

import com.tapas.evidence.dto.ChildDTO;
import com.tapas.evidence.entity.Child;
import com.tapas.evidence.entity.Kindergarten;
import com.tapas.evidence.entity.ResponsiblePerson;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ChildConverter implements CustomConverter {

	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destClass, final Class<?> sourceClass) {
		Object result = null;
		if (source instanceof ChildDTO) {
			final Child Child = convert((ChildDTO) source, (Child) destination);
			result = Child;
		} else if (source instanceof Child) {
			final ChildDTO Child = convert((Child) source);
			result = Child;
		} else if (source != null) {
			throw new MappingException("Converter ChildConverter used incorrectly. Arguments passed were:"
					+ destination + " and " + source);
		}
		return result;
	}

	private ChildDTO convert(final Child source) {
		final ChildDTO childDTO = new ChildDTO();
		if (source.getMother() != null) {
			childDTO.setMotherId(source.getMother().getId());
		}
		if (source.getFather() != null) {
			childDTO.setFatherId(source.getFather().getId());
		}
		if (source.getResponsiblePerson() != null) {
			childDTO.setResponsiblePersonId(source.getResponsiblePerson().getId());
		}
		childDTO.setBirthDate(source.getBirthDate());
		childDTO.setKindergartenId(source.getKindergarten().getId());
		childDTO.setName(source.getName());
		childDTO.setSurName(source.getSurName());
		childDTO.setId(source.getId());
		childDTO.setPhoto(source.getPhoto());
		return childDTO;
	}

	private Child convert(final ChildDTO sourceDTO, final Child destination) {
		final Child child = destination == null ? new Child() : destination;
		if (sourceDTO.getMotherId() != null) {
			child.setMother(new ResponsiblePerson(sourceDTO.getMotherId()));
		}
		if (sourceDTO.getFatherId() != null) {
			child.setFather(new ResponsiblePerson(sourceDTO.getFatherId()));
		}
		if (sourceDTO.getResponsiblePersonId() != null) {
			child.setResponsiblePerson(new ResponsiblePerson(sourceDTO.getResponsiblePersonId()));
		}
		child.setBirthDate(sourceDTO.getBirthDate());
		child.setKindergarten(new Kindergarten(sourceDTO.getKindergartenId()));
		child.setName(sourceDTO.getName());
		child.setSurName(sourceDTO.getSurName());
		child.setPhoto(sourceDTO.getPhoto());
		return child;
	}
}
