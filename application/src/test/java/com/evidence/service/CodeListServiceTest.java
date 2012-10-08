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

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.evidence.dao.DbUnitTest;
import com.evidence.dto.StateDTO;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class CodeListServiceTest extends DbUnitTest {

	@Inject
	private CodeListService codeListService;
	
	/**
	 * Test method for {@link com.evidence.service.CodeListService#getStates()}.
	 */
	@Test
	public void testGetStates() {
		List<StateDTO> states = codeListService.getStates();
		assertTrue("List of state should contains some data!", states.size() > 0);
	}
}
