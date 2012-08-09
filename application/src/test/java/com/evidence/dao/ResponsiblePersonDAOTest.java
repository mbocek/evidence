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

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.evidence.entity.Contact;
import com.evidence.entity.Kindergarten;
import com.evidence.entity.ResponsibilityType;
import com.evidence.entity.ResponsiblePerson;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/transaction.xml")
public class ResponsiblePersonDAOTest extends DbUnitDaoTest {

    @Inject
    private ResponsiblePersonDAO personDAO;
    
    @Inject
    private ResponsibilityTypeDAO responsibilityTypeDAO;
    
    @Inject
    private ContactDAO contactDAO;
    
    @Inject
    private KindergartenDAO kindergartenDAO;
    
	@Test
	public void testCreate() {
		final ResponsibilityType father = responsibilityTypeDAO.read(ResponsibilityType.Type.FATHER.name());
		final Contact contact = contactDAO.read(1L);
		final ResponsiblePerson person = new ResponsiblePerson();
		final Kindergarten kindergarten = kindergartenDAO.read(1L);
		person.setName("name");
		person.setSurName("surName");
		person.setKindergarten(kindergarten);
		person.setType(father);
		person.setContact(contact);
		personDAO.create(person);
		assertEquals(personDAO.findAll().get(0).getName(), "name");
	}

	@Test
	public void testFindAll() {
		assertEquals(0, personDAO.findAll().size());
	}
}
