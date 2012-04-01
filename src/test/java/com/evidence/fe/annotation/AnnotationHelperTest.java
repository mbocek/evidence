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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.evidence.fe.form.EvidenceFormFieldFactory;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class AnnotationHelperTest {

	private TestModel testModel = new TestModel();

	/**
	 * Test method for {@link com.evidence.fe.annotation.AnnotationHelper#isAutomaticForm(com.evidence.fe.form.Model)}.
	 */
	@Test
	public void testIsAutomaticForm() {
		assertTrue("TestModel object is automatic form", AnnotationHelper.isAutomaticForm(testModel));
		assertFalse("Raw Object isn't automatic form", AnnotationHelper.isAutomaticForm(new RawModel()));
	}

	/**
	 * Test method for {@link com.evidence.fe.annotation.AnnotationHelper#getFormFieldFactory(com.evidence.fe.form.Model)}.
	 */
	@Test
	public void testGetFormFieldFactory() {
		assertEquals("TestModel FormFieldFactory not exactly expeted class", EvidenceFormFieldFactory.class, AnnotationHelper.getFormFieldFactory(testModel));
	}

	/**
	 * Test method for {@link com.evidence.fe.annotation.AnnotationHelper#buildOrderedMap(com.evidence.fe.form.Model, java.util.Map)}.
	 */
	@Test
	public void testBuildOrderedMap() {
		Map<String, Double> orderedMap = new HashMap<String, Double>();
		AnnotationHelper.buildOrderMap(testModel, orderedMap);
		assertEquals("TestModel doesn't contains two ordered fields!", 4, orderedMap.size());
		assertFalse("TestModel field name order number is 1!", 1 != orderedMap.get("name"));
	}
}
