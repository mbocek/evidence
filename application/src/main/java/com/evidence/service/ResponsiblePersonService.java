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
package com.evidence.service;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.evidence.dto.ResponsiblePersonDTO;
import com.evidence.entity.ResponsiblePerson;
import com.evidence.repository.ResponsiblePersonRepository;
import com.evidence.utility.DTOConverter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service
@Validated
@Transactional(readOnly = true)
public class ResponsiblePersonService {

	@Inject
	private ResponsiblePersonRepository repository;

	/**
	 * Get all non deleted responsible persons.
	 * @return
	 */
	public List<ResponsiblePersonDTO> getAll() {
		final List<ResponsiblePerson> responsiblePersons = repository.findAll(false);
		return DTOConverter.convertList(responsiblePersons, ResponsiblePersonDTO.class);
	}

	/**
	 * Find responsible persons by kindergarten. 
	 * @param id
	 * @return
	 */
	public List<ResponsiblePersonDTO> findByKindergartenId(@NotNull final Long id) {
		final List<ResponsiblePerson> responsiblePersons = repository.findByKindergartenId(id);
		return DTOConverter.convertList(responsiblePersons, ResponsiblePersonDTO.class);
	}

	/**
	 * Create or update responsible person.
	 * @param responsiblePersonDTO
	 */
	@Transactional
	public void createOrUpdateResponsiblePerson(@NotNull final ResponsiblePersonDTO responsiblePersonDTO) {
		ResponsiblePerson responsiblePerson;
		if (responsiblePersonDTO.getId() == null) {
			responsiblePerson = DTOConverter.convert(responsiblePersonDTO, ResponsiblePerson.class); 
			repository.create(responsiblePerson);
		} else {
			responsiblePerson = repository.read(responsiblePersonDTO.getId());
			DTOConverter.convert(responsiblePersonDTO, responsiblePerson); 
			repository.update(responsiblePerson);
		}
	}
	
	/**
	 * Get responsible person by id. 
	 * Id is required and must be valid. When entity doesn't exists in database EntityNotFoundException is thrown.
	 * @param id
	 * @return
	 */
	@Valid
	public ResponsiblePersonDTO getById(@NotNull final Long id) {
		final ResponsiblePerson responsiblePerson = repository.read(id);
		return DTOConverter.convert(responsiblePerson, ResponsiblePersonDTO.class);
	}

	/**
	 * Delete responsible person object from database.
	 * @param id
	 */
	@Transactional
	public void delete(@NotNull final Long id) {
		ResponsiblePerson responsiblePerson = repository.findById(id);
		responsiblePerson.setDeleted(Boolean.TRUE);
		repository.update(responsiblePerson);
	}
}
