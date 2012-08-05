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

import com.evidence.dao.TeacherDAO;
import com.evidence.dto.TeacherDTO;
import com.evidence.entity.Teacher;
import com.evidence.utility.DTOConverter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class TeacherService {

	@Autowired
	private TeacherDAO teacherDao;

	public List<TeacherDTO> getAll() {
		final List<Teacher> teachers = teacherDao.findAll();
		return DTOConverter.convertList(teachers, TeacherDTO.class);
	}

	@Transactional
	public void createOrUpdateTeacher(final TeacherDTO teacherDTO) {
		Teacher teacher;
		if (teacherDTO.getId() == null) {
			teacher = DTOConverter.convert(teacherDTO, Teacher.class); 
			teacherDao.create(teacher);
		} else {
			teacher = teacherDao.read(teacherDTO.getId());
			DTOConverter.convert(teacherDTO, teacher); 
			teacherDao.update(teacher);
		}
	}
	
	/**
	 * Get teacher by id. 
	 * Id is required and must be valid. When entity doesn't exists in database EntityNotFoundException is thrown.
	 * @param id
	 * @return
	 */
	@Valid
	public TeacherDTO getById(final Long id) {
		final Teacher teacher = teacherDao.read(id);
		return DTOConverter.convert(teacher, TeacherDTO.class);
	}
}
