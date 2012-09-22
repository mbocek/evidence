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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.evidence.utility.FieldUtils;
import com.vaadin.ui.FormFieldFactory;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
public final class AnnotationHelper { // NOPMD

	private static final String NESTED_SEPARATOR = ".";
	private static final double TENTH = 0.1;
	private static final double TEN = 10;

	private AnnotationHelper() {
	}
	
	public static boolean isAutomaticForm(final Model model) {
		return model.getClass().isAnnotationPresent(AutomaticForm.class);
	}

	public static Class<? extends FormFieldFactory> getFormFieldFactory(final Model model) {
		final AutomaticForm automaticForm = model.getClass().getAnnotation(AutomaticForm.class);
		final Class<? extends FormFieldFactory> formFieldFactory = automaticForm.formFieldFactory();
		Class<? extends FormFieldFactory> result = null;
		if (FormFieldFactory.class.isAssignableFrom(formFieldFactory)) {
			result = formFieldFactory;
		} 
		return result;
	}
	
	public static void buildData(final Model model, final List<FieldInfo> fieldInfos,
			final Map<String, Double> orderMap, final Map<String, String> captionMap,
			final Map<String, Boolean> requiredMap, final Map<String, Boolean> validatedMap) {
		List<FieldInfo> fields = null;
		try {
			fields = getAllAutomaticFormFields(model.getClass());
		} catch (ClassNotFoundException e) {
			log.warn("Some problems in getting fields!", e);
		}
		
		if (fields != null) {
			for (FieldInfo fieldInfo : fields) {
				buildOrderMapRecursively(orderMap, fieldInfo, 0L, TENTH);
				buildCaptionMapRecursively(captionMap, fieldInfo);
				buildRequiredMapRecursively(requiredMap, fieldInfo);
				buildValidatedMapRecursively(validatedMap, fieldInfo);
				buildFieldInfoRecursively(fieldInfos, fieldInfo);
			}
		}
	}

	private static void buildFieldInfoRecursively(final List<FieldInfo> fieldInfos, final FieldInfo fieldInfo) {
		if (fieldInfo != null) {
			if (fieldInfo.getSubFieldInfo() == null) {
				fieldInfos.add(fieldInfo);
			} else {
				for (FieldInfo subFieldInfo : fieldInfo.getSubFieldInfo()) {
					buildFieldInfoRecursively(fieldInfos, subFieldInfo);
				}
			}
		}
	}

	private static List<FieldInfo> getAllAutomaticFormFields(final Class<? extends Model> modelClass) throws ClassNotFoundException {
		return getAllAutomaticFormFields(modelClass, null, null);
	}

	private static void buildValidatedMapRecursively(final Map<String, Boolean> validatedMap, final FieldInfo fieldInfo) {
		if (fieldInfo.getValidated() == null) {
			if (fieldInfo.getSubFieldInfo() != null) {
				for (FieldInfo subfield : fieldInfo.getSubFieldInfo()) {
					buildRequiredMapRecursively(validatedMap, subfield);
				}
			}			
		} else {
			if (fieldInfo.getSubFieldInfo() == null) {
				validatedMap.put(fieldInfo.getFieldNestedName(), fieldInfo.getValidated());
			} else {
				for (FieldInfo subfield : fieldInfo.getSubFieldInfo()) {
					buildRequiredMapRecursively(validatedMap, subfield);
				}
			}
		}
	}

	private static void buildRequiredMapRecursively(final Map<String, Boolean> requiredMap, final FieldInfo fieldInfo) {
		if (fieldInfo.getRequired() == null) {
			if (fieldInfo.getSubFieldInfo() != null) {
				for (FieldInfo subfield : fieldInfo.getSubFieldInfo()) {
					buildRequiredMapRecursively(requiredMap, subfield);
				}
			}			
		} else {
			if (fieldInfo.getSubFieldInfo() == null) {
				requiredMap.put(fieldInfo.getFieldNestedName(), fieldInfo.getRequired());
			} else {
				for (FieldInfo subfield : fieldInfo.getSubFieldInfo()) {
					buildRequiredMapRecursively(requiredMap, subfield);
				}
			}
		}
	}

	private static void buildCaptionMapRecursively(final Map<String, String> captionMap, final FieldInfo fieldInfo) {
		if (fieldInfo.getCaption() == null) {
			if (fieldInfo.getSubFieldInfo() != null) {
				for (FieldInfo subfield : fieldInfo.getSubFieldInfo()) {
					buildCaptionMapRecursively(captionMap, subfield);
				}
			}			
		} else {
			if (fieldInfo.getSubFieldInfo() == null) {
				captionMap.put(fieldInfo.getFieldNestedName(), fieldInfo.getCaption());
			} else {
				for (FieldInfo subfield : fieldInfo.getSubFieldInfo()) {
					buildCaptionMapRecursively(captionMap, subfield);
				}
			}
		}
	}

	private static void buildOrderMapRecursively(final Map<String, Double> orderMap, final FieldInfo fieldInfo,
			final Long base, final double multiplier) {
		if (fieldInfo.getOrder() != null) {
			if (fieldInfo.getSubFieldInfo() == null) {
				if (base == 0) {
					orderMap.put(fieldInfo.getFieldNestedName(), Double.valueOf(fieldInfo.getOrder()));
				} else {
					orderMap.put(fieldInfo.getFieldNestedName(), base + Double.valueOf(fieldInfo.getOrder()) * multiplier);
				}
			} else {
				for (FieldInfo subfield : fieldInfo.getSubFieldInfo()) {
					buildOrderMapRecursively(orderMap, subfield, fieldInfo.getOrder(), multiplier / TEN);
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<FieldInfo> getAllAutomaticFormFields(final Class modelClass, final FieldInfo parent, //NOPMD
			final String parentFullName) throws ClassNotFoundException { 
		List<FieldInfo> allClassFieldInfos = parent == null ? null : parent.getSubFieldInfo();
		if (modelClass.isAnnotationPresent(AutomaticForm.class)) {
			final Field[] allClassFields = FieldUtils.getAllFields(modelClass, null);
			if (allClassFields.length > 0) {
				allClassFieldInfos = allClassFieldInfos == null ? new ArrayList<FieldInfo>() : allClassFieldInfos;
				for (Field field : allClassFields) {
					final Order order = field.getAnnotation(Order.class);
					final Caption caption = field.getAnnotation(Caption.class);
					if (order != null || caption != null) {
						final Class fieldClass = field.getType();
						FieldInfo fieldInfo = null;
						Long orderNumber = null;
						String captionString = null;
						Boolean requiredBoolean = Boolean.FALSE;
						Boolean validatedBoolean = Boolean.FALSE;
						final String fieldName = field.getName();
						final String fieldNestedName = (parentFullName == null) ? fieldName : parentFullName + NESTED_SEPARATOR + fieldName;
						final Boolean nestedField = (parentFullName == null) ? Boolean.FALSE : Boolean.TRUE;
						
						if (order != null) {
							orderNumber = order.value();
						}						
						
						if (caption != null) {
							if (caption.value() != null && !caption.value().isEmpty()) { //NOPMD
								captionString = caption.value();
							} else {
								captionString = fieldNestedName;
							}
						}
						
						if (field.isAnnotationPresent(NotNull.class) || field.isAnnotationPresent(NotEmpty.class)
								|| field.isAnnotationPresent(NotBlank.class)) {
							requiredBoolean = Boolean.TRUE;
						}
						
						for (Annotation annotation : field.getAnnotations()) {
							if (annotation.getClass().getCanonicalName() != null
									&& annotation.getClass().getCanonicalName().contains("valid")) {
								validatedBoolean = Boolean.TRUE;
							}
						}						
						
						if (fieldClass.isAnnotationPresent(AutomaticForm.class)) {
							final List<FieldInfo> formFields = getAllAutomaticFormFields(fieldClass, fieldInfo, fieldNestedName);
							fieldInfo = new FieldInfo(fieldName, fieldNestedName, orderNumber, captionString, // NOPMD
									requiredBoolean, validatedBoolean, nestedField, formFields); 
						} else {
							fieldInfo = new FieldInfo(fieldName, fieldNestedName, orderNumber, captionString, // NOPMD
									requiredBoolean, validatedBoolean, nestedField, null); 
						}
						allClassFieldInfos.add(fieldInfo);						
					}
				}
			}
		}
		return allClassFieldInfos;
	}
}
