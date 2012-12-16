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
package com.evidence.fe.form;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import lombok.ToString;

import com.vaadin.ui.FormFieldFactory;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString
public class MetaModel {

	private final Class<? extends FormFieldFactory> formFieldFactory; // NOPMD
	
	private final Collection<FieldInfo> fieldInfos; // NOPMD
	
	private final Map<String, Double> orderMap; // NOPMD
	
	private final Collection<String> orderedFields; // NOPMD
	
	private final Map<String, String> captionMap; // NOPMD

	private final Map<String, Boolean> requiredMap; // NOPMD

	private final Map<String, Boolean> validatedMap; // NOPMD
	
	public MetaModel(final Class<? extends FormFieldFactory> formFieldFactory, final Collection<FieldInfo> fieldInfos,
			final Map<String, Double> orderMap, final Map<String, String> captionMap,
			final Map<String, Boolean> requiredMap, final Map<String, Boolean> validatedMap) {
		this.formFieldFactory = formFieldFactory;
		this.fieldInfos = fieldInfos;
		this.orderMap = orderMap;
		this.captionMap = captionMap;
		this.requiredMap = requiredMap;
		this.validatedMap = validatedMap;
		final ValueComparator vc = new ValueComparator(this.orderMap);
		final TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(vc);
		sortedMap.putAll(orderMap);
		orderedFields = sortedMap.keySet();
	}
	
	public Class<? extends FormFieldFactory> getFormFieldFactory() {
		return this.formFieldFactory;
	}
	
	public String getFieldCaption(final String fieldName) {
		return this.captionMap.get(fieldName);
	}
	
	public Boolean getFieldRequired(final String fieldName) {
		return this.requiredMap.get(fieldName);
	}

	public Boolean getFieldValidated(final String fieldName) {
		return this.validatedMap.get(fieldName);
	}	
	
	public Collection<String> getOrderedFields() {
		return Collections.unmodifiableCollection(orderedFields);
	}
	
	public Collection<String> getCaptionedFields() {
		return Collections.unmodifiableCollection(captionMap.keySet());
	}
	
	public Collection<String> getRequiredFields() {
		return Collections.unmodifiableCollection(requiredMap.keySet());
	}
	
	public Collection<String> getValidatedFields() {
		return Collections.unmodifiableCollection(validatedMap.keySet());
	}
	
	public Collection<FieldInfo> getFieldInfos() {
		return Collections.unmodifiableCollection(fieldInfos);
	}
}
