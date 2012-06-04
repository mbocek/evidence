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

import com.evidence.dto.KindergartenDTO;
import com.evidence.entity.Kindergarten;
import com.evidence.utility.DTOConverter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class KindergartenConverter implements CustomConverter {

	@SuppressWarnings("rawtypes")
	public Object convert(final Object destination, final Object source, final Class destClass, final Class sourceClass) {
		Object result = null;
		if (source instanceof KindergartenDTO) {
			final Kindergarten kindergarten = convert((KindergartenDTO) source);
			result = kindergarten;
		} else if (source instanceof KindergartenDTO) {
			throw new MappingException("Only converting Kindergarten entity to KindergartenDTO is supported");
		} else {
			throw new MappingException("Converter KindergartenConverter used incorrectly. Arguments passed were:"
					+ destination + " and " + source);
		}
		return result;
	}

	private Kindergarten convert(final KindergartenDTO kindergartenDTO) {
		final Kindergarten result = DTOConverter.convert(kindergartenDTO, Kindergarten.class);
		//Address address = DTOConverter.convert(kindergartenDTO.getAddress(), Address.class);
		return result;
	}
}
