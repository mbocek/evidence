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
package com.evidence.fe.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.evidence.fe.form.AnnotationHelper;
import com.evidence.fe.form.EvidenceFormFieldFactory;
import com.evidence.fe.form.FieldInfo;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class AnnotationHelperTest {

	private final TestModel testModel = new TestModel();

	/**
	 * Test method for {@link com.evidence.fe.form.AnnotationHelper#isAutomaticForm(com.evidence.fe.form.Model)}.
	 */
	@Test
	public void testIsAutomaticForm() {
		assertTrue("TestModel object is automatic form", AnnotationHelper.isAutomaticForm(testModel));
		assertFalse("Raw Object isn't automatic form", AnnotationHelper.isAutomaticForm(new RawModel()));
	}

	/**
	 * Test method for {@link com.evidence.fe.form.AnnotationHelper#getFormFieldFactory(com.evidence.fe.form.Model)}.
	 */
	@Test
	public void testGetFormFieldFactory() {
		assertEquals("TestModel FormFieldFactory not exactly expeted class", EvidenceFormFieldFactory.class,
				AnnotationHelper.getFormFieldFactory(testModel));
	}

	/**
	 * Test method for {@link com.evidence.fe.form.AnnotationHelper#buildOrderedMap(com.evidence.fe.form.Model, java.util.Map)}.
	 */
	@Test
	public void testBuildOrderedMap() {
		final List<FieldInfo> fieldInfos = new ArrayList<FieldInfo>();
		final Map<String, Double> orderedMap = new HashMap<String, Double>();
		final Map<String, String> captionMap = new HashMap<String, String>();
		final Map<String, Boolean> requiredMap = new HashMap<String, Boolean>();
		final Map<String, Boolean> validatedMap = new HashMap<String, Boolean>();
		AnnotationHelper.buildData(testModel, fieldInfos, orderedMap, captionMap, requiredMap, validatedMap);
		assertEquals("TestModel contains 4 fields!", 4, fieldInfos.size());
		assertEquals("TestModel doesn't contains four ordered fields!", 4, orderedMap.size());
		assertFalse("TestModel field name order number is 1!", 1 != orderedMap.get("name"));
		assertEquals("TestModel doesn't contains four caption fields!", 4, captionMap.size());
		assertEquals("TestModel doesn't contains one required field!", 4, requiredMap.size());
		assertTrue("TestModel name attribude isn't required field!", requiredMap.get("name"));
		assertTrue("TestModel name attribude isn't validated field!", requiredMap.get("name"));
	}
}
