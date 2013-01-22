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

import com.evidence.dto.ChildDTO;
import com.evidence.entity.Child;
import com.evidence.entity.Teacher;
import com.evidence.repository.ChildRepository;
import com.evidence.utility.DTOConverter;

/**
 * Service for access to child data.
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service
@Validated
@Transactional(readOnly = true)
public class ChildService {

	@Inject
	private ChildRepository repository;

	/**
	 * Get all non deleted children.
	 * @return
	 */
	public List<ChildDTO> getAll() {
		final List<Child> children = repository.findAll();
		return DTOConverter.convertList(children, ChildDTO.class);
	}

	/**
	 * Find teachers by children. 
	 * @param id
	 * @return
	 */
	public List<ChildDTO> findByKindergartenId(@NotNull final Long id) {
		final List<Teacher> teachers = repository.findByKindergartenId(id);
		return DTOConverter.convertList(teachers, ChildDTO.class);
	}

	/**
	 * Create or update child entity.
	 * @param childDTO
	 */
	@Transactional
	public void createOrUpdateChild(@NotNull final ChildDTO childDTO) {
		Child child;
		if (childDTO.getId() == null) {
			child = DTOConverter.convert(childDTO, Child.class); 
			repository.create(child);
		} else {
			child = repository.read(childDTO.getId());
			DTOConverter.convert(childDTO, child); 
			repository.update(child);
		}
	}
	
	/**
	 * Get child by id. 
	 * Id is required and must be valid. When entity doesn't exists in database EntityNotFoundException is thrown.
	 * @param id
	 * @return
	 */
	@Valid
	public ChildDTO getById(@NotNull final Long id) {
		final Child child = repository.read(id);
		return DTOConverter.convert(child, ChildDTO.class);
	}

	/**
	 * Delete child object from database.
	 * @param id
	 */
	@Transactional
	public void delete(@NotNull final Long id) {
		Child child = repository.findById(id);
		child.setDeleted(Boolean.TRUE);
		repository.update(child);
	}
}
