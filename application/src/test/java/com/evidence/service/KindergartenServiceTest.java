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

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import org.junit.Test;

import com.evidence.data.DbUnitTest;
import com.evidence.dto.AddressDTO;
import com.evidence.dto.ContactDTO;
import com.evidence.dto.KindergartenDTO;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class KindergartenServiceTest extends DbUnitTest {

	@Inject
	private KindergartenService kindergartenService;
	
	/**
	 * Test method for {@link com.evidence.service.KindergartenService#getAll()}.
	 */
	@Test
	public void testGetAll() {
		List<KindergartenDTO> kindergartens = kindergartenService.getAll();
		assertTrue("List of kindergatens should contains some data!", kindergartens.size() > 0);
	}

	/**
	 * Test method for {@link com.evidence.service.KindergartenService#createOrUpdateKindergarten(com.evidence.dto.KindergartenDTO)}.
	 */
	@Test
	public void testCreateOrUpdateKindergarten() {
		List<KindergartenDTO> allBefore = kindergartenService.getAll();
		KindergartenDTO kindergartenDTO = new KindergartenDTO();
		kindergartenDTO.setName("Test");
		kindergartenDTO.setContact(new ContactDTO());
		kindergartenDTO.getContact().setEmail("test@test.com");
		kindergartenDTO.getContact().setLandLine("+43123456789");
		kindergartenDTO.getContact().setMobileNumber("+43123456789");
		kindergartenDTO.getContact().setAddress(new AddressDTO());
		kindergartenDTO.getContact().getAddress().setCity("London");
		kindergartenDTO.getContact().getAddress().setHouseNumber("123");
		kindergartenDTO.getContact().getAddress().setStateCode("CZ");
		kindergartenDTO.getContact().getAddress().setStreet("Baker street");
		kindergartenDTO.getContact().getAddress().setZipCode("12345");
		kindergartenService.createOrUpdateKindergarten(kindergartenDTO);
		List<KindergartenDTO> allAfter = kindergartenService.getAll();
		assertTrue("New kindergarten wasn't created", allAfter.size() == allBefore.size() + 1);
	}

	/**
	 * Test method for {@link com.evidence.service.KindergartenService#getById(java.lang.Long)}.
	 */
	@Test
	public void testGetById() {
		try {
			kindergartenService.getById(1L);
		} catch (EntityNotFoundException e) {
			fail("Kindergarten with id should exists!");
		}
	}

	/**
	 * Test method for {@link com.evidence.service.KindergartenService#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		List<KindergartenDTO> allBefore = kindergartenService.getAll();
		kindergartenService.delete(1L);
		List<KindergartenDTO> allAfter = kindergartenService.getAll();
		assertTrue("Kindergarten wasn't deleted", allAfter.size() == allBefore.size() - 1);
	}
}
