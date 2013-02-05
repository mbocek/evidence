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
package com.tapas.evidence.service;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.tapas.evidence.dto.KindergartenDTO;
import com.tapas.evidence.entity.Kindergarten;
import com.tapas.evidence.repository.KindergartenRepository;
import com.tapas.evidence.utility.DTOConverter;

/**
 * Service for access to kindergarten data.
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service
@Validated
@Transactional(readOnly = true)
public class KindergartenService {

	@Inject
	private KindergartenRepository repository;

	/**
	 * Get all non deleted kindergartens.
	 * @return kindergartens
	 */
	public List<KindergartenDTO> getAll() {
		final List<Kindergarten> kindergartens = repository.findAll();
		return DTOConverter.convertList(kindergartens, KindergartenDTO.class);
	}

	/**
	 * Create or update kindergarten.  
	 * @param kindergartenDTO
	 */
	@Transactional
	public void createOrUpdateKindergarten(@NotNull final KindergartenDTO kindergartenDTO) {
		Kindergarten kindergarten;
		if (kindergartenDTO.getId() == null) {
			kindergarten = DTOConverter.convert(kindergartenDTO, Kindergarten.class); 
			repository.create(kindergarten);
		} else {
			kindergarten = repository.read(kindergartenDTO.getId());
			DTOConverter.convert(kindergartenDTO, kindergarten); 
			repository.update(kindergarten);
		}
	}
	
	/**
	 * Get kindergarten by id. 
	 * Id is required and must be valid. When entity doesn't exists in database EntityNotFoundException is thrown.
	 * @param id
	 * @return
	 */
	public KindergartenDTO getById(@NotNull final Long id) {
		final Kindergarten kindergarten = repository.read(id);
		return DTOConverter.convert(kindergarten, KindergartenDTO.class);
	}
	
	/**
	 * Delete kindergarten object from database.
	 * @param id
	 */
	@Transactional
	public void delete(@NotNull final Long id) {
		Kindergarten kindergarten = repository.findById(id);
		kindergarten.setDeleted(Boolean.TRUE);
		repository.update(kindergarten);
	}
}
