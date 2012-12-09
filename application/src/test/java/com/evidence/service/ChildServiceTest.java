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
import com.evidence.dto.ChildDTO;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ChildServiceTest extends DbUnitTest {

	@Inject
	private ChildService childService;
	
	@Inject
	private ResponsiblePersonService responsiblePersonService;
	
	/**
	 * Test method for {@link com.evidence.service.ChildService#getAll()}.
	 */
	@Test
	public void testGetAll() {
		List<ChildDTO> children = childService.getAll();
		assertTrue("List of children should contains some data!", children.size() > 0);
	}

	/**
	 * Test method for {@link com.evidence.service.ChildService#findByKindergartenId(java.lang.Long)}.
	 */
	@Test
	public void testFindByKindergartenId() {
		List<ChildDTO> children = childService.findByKindergartenId(1L);
		assertTrue("List of children should contains some data!", children.size() > 0);
	}

	/**
	 * Test method for {@link com.evidence.service.ChildService#createOrUpdateTeacher(com.evidence.dto.ChildDTO)}.
	 */
	@Test
	//@Rollback(false)
	public void testCreateOrUpdateChild() {
		List<ChildDTO> allBefore = childService.getAll();
		ChildDTO childDTO = new ChildDTO();
		childDTO.setName("Kate");
		childDTO.setSurName("Daughter");
		childDTO.setKindergartenId(1L);
		childDTO.setBirthDate(new Date());
		childDTO.setMotherId(1L);
		childService.createOrUpdateChild(childDTO);
		List<ChildDTO> allAfter = childService.getAll();
		assertTrue("New children wasn't created", allAfter.size() == allBefore.size() + 1);
	}

	/**
	 * Test method for {@link com.evidence.service.ChildService#getById(java.lang.Long)}.
	 */
	@Test
	public void testGetById() {
		ChildDTO child = childService.getById(1L);
		assertNotNull("Child object shouldn't be null!", child);
	}

	/**
	 * Test method for {@link com.evidence.service.ChildService#delete(java.lang.Long)}.
	 */
	@Test
	public void testDelete() {
		List<ChildDTO> allBefore = childService.getAll();
		childService.delete(1L);
		List<ChildDTO> allAfter = childService.getAll();
		assertTrue("Child wasn't deleted", allAfter.size() == allBefore.size() - 1);
	}

}
