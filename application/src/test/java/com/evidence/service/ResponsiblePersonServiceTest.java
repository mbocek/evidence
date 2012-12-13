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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import org.junit.Test;

import com.evidence.data.DbUnitTest;
import com.evidence.dto.AddressDTO;
import com.evidence.dto.ContactDTO;
import com.evidence.dto.ResponsiblePersonDTO;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ResponsiblePersonServiceTest extends DbUnitTest {

	@Inject
	private ResponsiblePersonService responsiblePersonService;

	/**
	 * Test method for {@link com.evidence.service.ResponsiblePersonService#getAll()}.
	 */
	@Test
	public void testGetAll() {
		List<ResponsiblePersonDTO> responsiblePersons = responsiblePersonService.getAll();
		assertTrue("List of responsible persons should contains some data!", responsiblePersons.size() > 0);
	}

	/**
	 * Test method for {@link com.evidence.service.ResponsiblePersonService#findByKindergartenId(java.lang.Long)}.
	 */
	@Test
	public void testFindByKindergartenId() {
		List<ResponsiblePersonDTO> responsiblePersons = responsiblePersonService.findByKindergartenId(1L);
		assertTrue("List of responsible persons should contains some data!", responsiblePersons.size() > 0);
	}

	/**
	 * Test method for {@link com.evidence.service.ResponsiblePersonService#createOrUpdateResponsiblePerson(com.evidence.dto.ResponsiblePersonDTO)}.
	 */
	@Test
	public void testCreateOrUpdateResponsiblePersons() {
		List<ResponsiblePersonDTO> allBefore = responsiblePersonService.getAll();
		ResponsiblePersonDTO responsiblePersonDTO = new ResponsiblePersonDTO(1L);
		responsiblePersonDTO.setType("MOTHER");
		responsiblePersonDTO.setName("John");
		responsiblePersonDTO.setSurName("Doe");
		responsiblePersonDTO.setBirthDate(new Date());
		responsiblePersonDTO.setContact(new ContactDTO());
		responsiblePersonDTO.getContact().setEmail("test@test.com");
		responsiblePersonDTO.getContact().setLandLine("+43123456789");
		responsiblePersonDTO.getContact().setMobileNumber("+43123456789");
		responsiblePersonDTO.getContact().setAddress(new AddressDTO());
		responsiblePersonDTO.getContact().getAddress().setCity("London");
		responsiblePersonDTO.getContact().getAddress().setHouseNumber("123");
		responsiblePersonDTO.getContact().getAddress().setStateCode("CZ");
		responsiblePersonDTO.getContact().getAddress().setStreet("Baker street");
		responsiblePersonDTO.getContact().getAddress().setZipCode("12345");
		responsiblePersonService.createOrUpdateResponsiblePerson(responsiblePersonDTO);
		List<ResponsiblePersonDTO> allAfter = responsiblePersonService.getAll();
		assertTrue("New responisble person wasn't created", allAfter.size() == allBefore.size() + 1);
	}

	/**
	 * Test method for {@link com.evidence.service.ResponsiblePersonService#getById(java.lang.Long)}.
	 */
	@Test
	public void testGetById() {
		try {
			responsiblePersonService.getById(1L);
		} catch (EntityNotFoundException e) {
			fail("Responsible person with id should exists!");
		}
	}

	/**
	 * Test method for {@link com.evidence.service.ResponsiblePersonService#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		List<ResponsiblePersonDTO> allBefore = responsiblePersonService.getAll();
		responsiblePersonService.delete(1L);
		List<ResponsiblePersonDTO> allAfter = responsiblePersonService.getAll();
		assertTrue("Responsible person wasn't deleted", allAfter.size() == allBefore.size() - 1);
	}

}
