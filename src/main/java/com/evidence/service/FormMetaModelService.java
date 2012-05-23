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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.evidence.fe.annotation.AnnotationHelper;
import com.evidence.fe.annotation.FieldInfo;
import com.evidence.fe.annotation.MetaModel;
import com.evidence.fe.form.Model;
import com.vaadin.ui.FormFieldFactory;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service
@Slf4j
public class FormMetaModelService {
	
	private Map<String, MetaModel> modelMap = new HashMap<String, MetaModel>();
	
	public MetaModel getMetaModel(Model model) {
		log.debug("Getting metamodel for model: {}", model.getClass().getName());
		String modelName = model.getClass().getName();
		if (modelMap.containsKey(modelName)) {
			log.debug("Getting metamodel from cache!");
			return modelMap.get(modelName);
		} else {
			synchronized (modelName) {
				MetaModel metaModel = buildModel(model);
				modelMap.put(modelName, metaModel);
				return metaModel;
			}
		}
	}

	private MetaModel buildModel(Model model) {
		String modelName = model.getClass().getName(); 
		log.debug("Building metamodel for model: {}", modelName);
		MetaModel meta = null;
		if (AnnotationHelper.isAutomaticForm(model)) {
			Class<? extends FormFieldFactory> factory = AnnotationHelper.getFormFieldFactory(model);
			List<FieldInfo> fieldInfos = new ArrayList<FieldInfo>();
			Map<String, Double> orderMap = new HashMap<String, Double>();
			Map<String, String> captionMap = new HashMap<String, String>();
			Map<String, Boolean> requiredMap = new HashMap<String, Boolean>();
			Map<String, Boolean> validatedMap = new HashMap<String, Boolean>();
			AnnotationHelper.buildData(model, fieldInfos, orderMap, captionMap, requiredMap, validatedMap);
			meta = new MetaModel(factory, fieldInfos, orderMap, captionMap, requiredMap, validatedMap);
			log.debug("Adding automatic form to cache with name: {} and meta mode: {}", modelName, meta);
			modelMap.put(modelName, meta);
		}
		return meta;
	}
}
