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

	private Class<? extends FormFieldFactory> formFieldFactory;
	
	private Collection<FieldInfo> fieldInfos;
	
	private Map<String, Double> orderMap;
	
	private Collection<String> orderedFields;
	
	private Map<String, String> captionMap;

	private Map<String, Boolean> requiredMap;

	private Map<String, Boolean> validatedMap;
	
	public MetaModel(Class<? extends FormFieldFactory> formFieldFactory, Collection<FieldInfo> fieldInfos, Map<String, Double> orderMap, Map<String, String> captionMap, Map<String, Boolean> requiredMap, Map<String, Boolean> validatedMap) {
		this.formFieldFactory = formFieldFactory;
		this.fieldInfos = fieldInfos;
		this.orderMap = orderMap;
		this.captionMap = captionMap;
		this.requiredMap = requiredMap;
		this.validatedMap = validatedMap;
		ValueComparator vc = new ValueComparator(this.orderMap);
		TreeMap<String, Double> sortedMap = new TreeMap<String, Double>(vc);
		sortedMap.putAll(orderMap);
		orderedFields = sortedMap.keySet();
	}
	
	public Class<? extends FormFieldFactory> getFormFieldFactory() {
		return this.formFieldFactory;
	}
	
	public String getFieldCaption(String fieldName) {
		return this.captionMap.get(fieldName);
	}
	
	public Boolean getFieldRequired(String fieldName) {
		return this.requiredMap.get(fieldName);
	}

	public Boolean getFieldvalidated(String fieldName) {
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
