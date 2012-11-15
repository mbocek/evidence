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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.evidence.data.DbUnitTest;
import com.evidence.dto.AddressDTO;
import com.evidence.dto.ContactDTO;
import com.evidence.dto.TeacherDTO;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class TeacherServiceTest extends DbUnitTest {

	@Inject
	private TeacherService teacherService;
	
	/**
	 * Test method for {@link com.evidence.service.TeacherService#getAll()}.
	 */
	@Test
	public void testGetAll() {
		List<TeacherDTO> teachers = teacherService.getAll();
		assertTrue("List of teachers should contains some data!", teachers.size() > 0);
	}

	/**
	 * Test method for {@link com.evidence.service.TeacherService#findByKindergartenId(java.lang.Long)}.
	 */
	@Test
	public void testFindByKindergartenId() {
		List<TeacherDTO> teachers = teacherService.findByKindergartenId(1L);
		assertTrue("List of teachers should contains some data!", teachers.size() > 0);
	}

	/**
	 * Test method for {@link com.evidence.service.TeacherService#createOrUpdateTeacher(com.evidence.dto.TeacherDTO)}.
	 */
	@Test
	public void testCreateOrUpdateTeacher() {
		List<TeacherDTO> allBefore = teacherService.getAll();
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setName("John");
		teacherDTO.setSurName("Doe");
		teacherDTO.setKindergartenId(1L);
		teacherDTO.setBirthDate(new Date());
		teacherDTO.setContact(new ContactDTO());
		teacherDTO.getContact().setEmail("test@test.com");
		teacherDTO.getContact().setLandLine("+43123456789");
		teacherDTO.getContact().setMobileNumber("+43123456789");
		teacherDTO.getContact().setAddress(new AddressDTO());
		teacherDTO.getContact().getAddress().setCity("London");
		teacherDTO.getContact().getAddress().setHouseNumber("123");
		teacherDTO.getContact().getAddress().setStateCode("CZ");
		teacherDTO.getContact().getAddress().setStreet("Baker street");
		teacherDTO.getContact().getAddress().setZipCode("12345");
		teacherService.createOrUpdateTeacher(teacherDTO);
		List<TeacherDTO> allAfter = teacherService.getAll();
		assertTrue("New kindergarten wasn't created", allAfter.size() == allBefore.size() + 1);
	}

	/**
	 * Test method for {@link com.evidence.service.TeacherService#getById(java.lang.Long)}.
	 */
	@Test
	public void testGetById() {
		TeacherDTO teacher = teacherService.getById(1L);
		assertNotNull("Teacher object shouldn't be null!", teacher);
	}

	/**
	 * Test method for {@link com.evidence.service.TeacherService#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		List<TeacherDTO> allBefore = teacherService.getAll();
		teacherService.delete(1L);
		List<TeacherDTO> allAfter = teacherService.getAll();
		assertTrue("Teacher wasn't deleted", allAfter.size() == allBefore.size() - 1);
	}

}
