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
package com.evidence.dao;

import static org.junit.Assert.fail;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import com.evidence.entity.Kindergarden;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Transactional
public class KinderGardenDAOTest extends DbUnitDaoTest {

	@Inject
	private KindergardenDAO kinderGardenDAO;
	
	@Test
	public void testCreate() {
		Kindergarden kinderGarden = kinderGardenDAO.read(1L);
		Assert.assertNotNull(kinderGarden.getContact());
		Assert.assertNotNull(kinderGarden.getContact().getAddress());
		Assert.assertNotNull(kinderGarden.getContact().getEmail());
		Assert.assertNotNull(kinderGarden.getContact().getLandLine());
		Assert.assertNotNull(kinderGarden.getContact().getMobilePhone());
	}

	/**
	 * Test method for {@link com.evidence.dao.JpaDAO#delete(java.lang.Object)}.
	 */
	@Test
	public void testDelete() {
		Kindergarden kinderGarden = kinderGardenDAO.read(1L);
		kinderGardenDAO.delete(kinderGarden);
		try {
			kinderGarden = kinderGardenDAO.read(1L);
			fail("Entity shouldn't be exist!"); 
		} catch (EntityNotFoundException e) {
		}
	}

	/**
	 * Test method for {@link com.evidence.dao.JpaDAO#findById(java.io.Serializable)}.
	 */
	@Test
	public void testFindById() {
		Kindergarden kinderGarden = kinderGardenDAO.findById(1L);
		Assert.assertNotNull(kinderGarden);
	}

	/**
	 * Test method for {@link com.evidence.dao.JpaDAO#read(java.io.Serializable)}.
	 */
	@Test
	public void testRead() {
		try {
			Kindergarden kinderGarden = kinderGardenDAO.read(1L);
			Assert.assertNotNull(kinderGarden);
		} catch (EntityNotFoundException e) {
			fail("Entity should be exist!"); 
		}
	}

	/**
	 * Test method for {@link com.evidence.dao.JpaDAO#findAll()}.
	 */
	@Test
	public void testFindAll() {
		List<Kindergarden> kinderGardens = kinderGardenDAO.findAll();
		Assert.assertEquals(1, kinderGardens.size());
	}
}
