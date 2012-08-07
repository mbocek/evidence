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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evidence.dao.KindergartenDAO;
import com.evidence.dto.KindergartenDTO;
import com.evidence.entity.Kindergarten;
import com.evidence.utility.DTOConverter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class KindergartenService {

	@Autowired
	private KindergartenDAO kindergartenDao;

	public List<KindergartenDTO> getAll() {
		final List<Kindergarten> kindergartens = kindergartenDao.findAll();
		return DTOConverter.convertList(kindergartens, KindergartenDTO.class);
	}

	@Transactional
	public void createOrUpdateKindergarten(final KindergartenDTO kindergartenDTO) {
		Kindergarten kindergarten;
		if (kindergartenDTO.getId() == null) {
			kindergarten = DTOConverter.convert(kindergartenDTO, Kindergarten.class); 
			kindergartenDao.create(kindergarten);
		} else {
			kindergarten = kindergartenDao.read(kindergartenDTO.getId());
			DTOConverter.convert(kindergartenDTO, kindergarten); 
			kindergartenDao.update(kindergarten);
		}
	}
	
	/**
	 * Get kindergarten by id. 
	 * Id is required and must be valid. When entity doesn't exists in database EntityNotFoundException is thrown.
	 * @param id
	 * @return
	 */
	@Valid
	public KindergartenDTO getById(final Long id) {
		final Kindergarten kindergarten = kindergartenDao.read(id);
		return DTOConverter.convert(kindergarten, KindergartenDTO.class);
	}
}