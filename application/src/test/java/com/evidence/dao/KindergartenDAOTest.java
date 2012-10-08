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

import com.evidence.entity.Kindergarten;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class KindergartenDAOTest extends DbUnitTest {

	@Inject
	private KindergartenDAO kindergartenDAO;
	
	@Test
	public void testCreate() {
		final Kindergarten kindergarten = kindergartenDAO.read(1L);
		Assert.assertNotNull(kindergarten.getContact());
		Assert.assertNotNull(kindergarten.getContact().getAddress());
		Assert.assertNotNull(kindergarten.getContact().getEmail());
		Assert.assertNotNull(kindergarten.getContact().getLandLine());
		Assert.assertNotNull(kindergarten.getContact().getMobilePhone());
	}

	/**
	 * Test method for {@link com.evidence.dao.JpaDAO#delete(java.lang.Object)}.
	 */
	@Test
	public void testDelete() {
		Kindergarten kindergarten = kindergartenDAO.read(1L);
		kindergartenDAO.delete(kindergarten);
		try {
			kindergarten = kindergartenDAO.read(1L);
			fail("Entity shouldn't be exist!"); 
		} catch (EntityNotFoundException e) { // NOPMD
			Assert.assertTrue(true);
		}
	}

	/**
	 * Test method for {@link com.evidence.dao.JpaDAO#findById(java.io.Serializable)}.
	 */
	@Test
	public void testFindById() {
		final Kindergarten kindergarten = kindergartenDAO.findById(1L);
		Assert.assertNotNull(kindergarten);
	}

	/**
	 * Test method for {@link com.evidence.dao.JpaDAO#read(java.io.Serializable)}.
	 */
	@Test
	public void testRead() {
		try {
			final Kindergarten kindergarten = kindergartenDAO.read(1L);
			Assert.assertNotNull(kindergarten);
		} catch (EntityNotFoundException e) {
			fail("Entity should be exist!"); 
		}
	}

	/**
	 * Test method for {@link com.evidence.dao.JpaDAO#findAll()}.
	 */
	@Test
	public void testFindAll() {
		final List<Kindergarten> kindergartens = kindergartenDAO.findAll();
		Assert.assertEquals(1, kindergartens.size());
	}
}
