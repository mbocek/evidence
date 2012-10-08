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
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.evidence.dao.TeacherDAO;
import com.evidence.dto.TeacherDTO;
import com.evidence.entity.Teacher;
import com.evidence.utility.DTOConverter;

/**
 * Service for access to teacher data.
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service
@Validated
@Transactional(readOnly = true)
public class TeacherService {

	@Autowired
	private TeacherDAO teacherDao;

	/**
	 * Get all non deleted teachers.
	 * @return
	 */
	public List<TeacherDTO> getAll() {
		final List<Teacher> teachers = teacherDao.findAll(false);
		return DTOConverter.convertList(teachers, TeacherDTO.class);
	}

	/**
	 * Find teachers by kindergarten. 
	 * @param id
	 * @return
	 */
	public List<TeacherDTO> findByKindergartenId(@NotNull final Long id) {
		final List<Teacher> teachers = teacherDao.findByKindergartenId(id);
		return DTOConverter.convertList(teachers, TeacherDTO.class);
	}

	/**
	 * Create or update teacher.
	 * @param teacherDTO
	 */
	@Transactional
	public void createOrUpdateTeacher(@NotNull final TeacherDTO teacherDTO) {
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
	public TeacherDTO getById(@NotNull final Long id) {
		final Teacher teacher = teacherDao.read(id);
		return DTOConverter.convert(teacher, TeacherDTO.class);
	}

	/**
	 * Delete teacher object from database.
	 * @param id
	 */
	@Transactional
	public void delete(@NotNull final Long id) {
		Teacher teacher = teacherDao.findById(id);
		teacher.setDeleted(Boolean.TRUE);
		teacherDao.update(teacher);
	}
}
