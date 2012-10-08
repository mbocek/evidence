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
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.Test;

import com.evidence.entity.Address;
import com.evidence.entity.Contact;
import com.evidence.entity.EmailAddress;
import com.evidence.entity.Kindergarten;
import com.evidence.entity.PhoneNumber;
import com.evidence.entity.ResponsibilityType;
import com.evidence.entity.ResponsiblePerson;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ResponsiblePersonDAOTest extends DbUnitTest {

    @Inject
    private ResponsiblePersonDAO personDAO;
    
    @Inject
    private ContactDAO contactDAO;
    
    @Inject
    private KindergartenDAO kindergartenDAO;
    
    @Inject
    private StateDAO stateDAO;
    
	@Test
	public void testCreate() {
		final ResponsiblePerson person = new ResponsiblePerson();
		final Kindergarten kindergarten = kindergartenDAO.read(1L);
		person.setName("name");
		person.setSurName("surName");
		person.setKindergarten(kindergarten);
		person.setType(ResponsibilityType.MOTHER);
		person.setContact(new Contact());
		person.getContact().setEmail(new EmailAddress("test@test.com"));
		person.getContact().setLandLine(new PhoneNumber("+43", "123456789"));
		person.getContact().setMobilePhone(new PhoneNumber("+43", "123456789"));
		person.getContact().setAddress(new Address());
		person.getContact().getAddress().setCity("London");
		person.getContact().getAddress().setHouseNumber("123");
		person.getContact().getAddress().setState(stateDAO.findById("CZ"));
		person.getContact().getAddress().setStreet("Baker street");
		person.getContact().getAddress().setZipCode("12345");
		personDAO.create(person);
		ResponsiblePerson testPerson = null;
		for (ResponsiblePerson responsiblePerson : personDAO.findAll()) {
			if (responsiblePerson.getSurName().equals("surName")) {
				testPerson = responsiblePerson;
			}
		}
		assertEquals(testPerson.getName(), "name");
		assertEquals(testPerson.getType(), ResponsibilityType.MOTHER);
	}

	@Test
	public void testFindAll() {
		assertTrue(personDAO.findAll().size() > 0);
	}
}
