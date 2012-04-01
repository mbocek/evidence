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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import com.evidence.fe.form.EvidenceFormFieldFactory;
import com.evidence.fe.form.Model;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
public class AnnotationHelper {

	public static boolean isAutomaticForm(Model model) {
		return model.getClass().isAnnotationPresent(AutomaticForm.class);
	}

	public static Class<? extends EvidenceFormFieldFactory> getFormFieldFactory(Model model) {
		AutomaticForm automaticForm = model.getClass().getAnnotation(AutomaticForm.class);
		Class<? extends EvidenceFormFieldFactory> formFieldFactory = automaticForm.formFieldFactory();
		if (EvidenceFormFieldFactory.class.isAssignableFrom(formFieldFactory)) {
			return formFieldFactory;
		} else {
			return null;
		}
	}
	
	public static void buildOrderMap(Model model, Map<String, Double> orderMap) {
		List<FieldInfo> fields = null;
		try {
			fields = getAllAutomaticFormFields(model.getClass(), null);
		} catch (ClassNotFoundException e) {
			log.warn("Some problems in getting fields!", e);
		}
	
		for (FieldInfo fieldInfo : fields) {
			buildOrderMapRecursively(orderMap, fieldInfo, 0L, 0.1);
		}
	}

	private static void buildOrderMapRecursively(Map<String, Double> orderMap, FieldInfo fieldInfo, Long base,  double multiplier) {
		if (fieldInfo.getOrder() != null) {
			if (fieldInfo.getSubFieldInfo() == null) {
				if (base == 0) {
					orderMap.put(fieldInfo.getField().getName(), Double.valueOf(fieldInfo.getOrder()));
				} else {
					orderMap.put(fieldInfo.getField().getName(), base + Double.valueOf(fieldInfo.getOrder()) * multiplier);
				}
			} else {
				for (FieldInfo subfield : fieldInfo.getSubFieldInfo()) {
					buildOrderMapRecursively(orderMap, subfield, fieldInfo.getOrder(), multiplier / 10);
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<FieldInfo> getAllAutomaticFormFields(Class objectClass, FieldInfo parent) throws ClassNotFoundException {
		List<FieldInfo> allClassFieldInfos = parent != null ? parent.getSubFieldInfo() : null;
		if (objectClass.isAnnotationPresent(AutomaticForm.class)) {
			Field[] allClassFields = FieldUtils.getAllFields(objectClass, null);
			if (allClassFields.length > 0) {
				allClassFieldInfos = allClassFieldInfos == null ? new ArrayList<FieldInfo>() : allClassFieldInfos;
				for (Field field : allClassFields) {
					Order order = field.getAnnotation(Order.class);
					Caption caption = field.getAnnotation(Caption.class);
					if (order != null || caption != null) {
						Class fieldClass = field.getType();
						Long orderNumber = null;
						String captionString = null;
						
						if (order != null) {
							orderNumber = order.value();
						}						
						
						if (caption != null) {
							captionString = caption.value();
						}						
						
						FieldInfo fieldInfo = null;
						if (fieldClass.isAnnotationPresent(AutomaticForm.class)) {
							List<FieldInfo> formFields = getAllAutomaticFormFields(fieldClass, fieldInfo);
							fieldInfo = new FieldInfo(field, orderNumber, captionString, formFields);
						} else {
							fieldInfo = new FieldInfo(field, orderNumber, captionString, null);
						}
						allClassFieldInfos.add(fieldInfo);						
					}
				}
			}
		}
		return allClassFieldInfos;
	}

	public static void buildCaptionMap(Model model, Map<String, String> captionMap) {
		Field[] fields = null;
		try {
			fields = FieldUtils.getAllFields(model.getClass(), null);
		} catch (ClassNotFoundException e) {
			log.warn("Some problems in getting fields!", e);
		}
		
		for (Field field : fields) {
			Caption caption = field.getAnnotation(Caption.class);
			if (caption != null) {
				captionMap.put(field.getName(), caption.value());
			}
		}
	}
}
