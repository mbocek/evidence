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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.vaadin.mvp.uibinder.IUiMessageSource;

import com.evidence.fe.annotation.FieldInfo;
import com.evidence.fe.annotation.MetaModel;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.NestedMethodProperty;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
public class EvidenceForm extends Form {

	private static final String MISSING_MESSAGE = "{{missing message: ";

	private static final long serialVersionUID = 1L;

	private static final String NESTED_SEPARATOR = ".";
	
	public void addFooter(final Component component) {
		this.getFooter().addComponent(component);
	}

	public void setItemDataSource(final Model model, final MetaModel metaModel, final IUiMessageSource messageSource,
			final String messagePrefix, final Locale locale) {
		//Class<? extends Model> clazz = model.getClass();
		final BeanItem<Model> beanItem = new BeanItem<Model>(model);
		for (FieldInfo fieldInfo : metaModel.getFieldInfos()) {
			if (fieldInfo.isNested()) {
				beanItem.addItemProperty(fieldInfo.getFieldNestedName(),
						new NestedMethodProperty(model, fieldInfo.getFieldNestedName())); // NOPMD
			}
		}
		this.setItemDataSource(beanItem);
		setupFormFieldFactory(metaModel, messageSource, locale);
		this.setVisibleItemProperties(metaModel.getOrderedFields());
		for (String fieldName : metaModel.getCaptionedFields()) {
			log.debug("Processing caption for field:{}", fieldName);
			final String fieldCaption = messagePrefix + NESTED_SEPARATOR + metaModel.getFieldCaption(fieldName);
			final Field field = this.getField(fieldName);
			field.setCaption(messageSource.getMessage(fieldCaption, locale));
		}
		for (String fieldName : metaModel.getRequiredFields()) {
			log.debug("Processing required for field:{}", fieldName);
			final Boolean fieldRequired = metaModel.getFieldRequired(fieldName);
			final Field field = this.getField(fieldName);
			log.debug("Field:{} is required:{}", fieldName, fieldRequired);
			field.setRequired(fieldRequired);
			field.setRequiredError(messageSource.getMessage("field.required", locale));
		}
	}

	public boolean validate(final MetaModel metaModel, final Validator validator, final Model model,
			final IUiMessageSource messageSource, final Locale locale) {
		boolean result = false; 
		final BindingResult validationResult = new BeanPropertyBindingResult(model, model.getClass().getName());
		clearFields(metaModel);
		validator.validate(model, validationResult);
		if (validationResult.hasErrors()) {
			for (FieldError error : validationResult.getFieldErrors()) {
				final String field = error.getField();
				final String message = getMessage(error.getCodes(), messageSource, locale);
				final Field fieldComponent = this.getField(field);
				if (fieldComponent instanceof AbstractField) {
					((AbstractField)fieldComponent).setComponentError(new UserError(message)); // NOPMD
				}
			}
			result = false;
		} else {
			result = true;
		}
		return result;		
	}
	
	private String getMessage(final String[] codes, final IUiMessageSource messageSource, final Locale locale) {
		String result = null; 
		for (String code : codes) {
			result = messageSource.getMessage(code, locale);
			if (result != null && !result.startsWith(MISSING_MESSAGE)) {
				break;
			}
		}
		return result;
	}

	private void clearFields(final MetaModel metaModel) {
		final Collection<String> validatedFields = metaModel.getValidatedFields();
		for (String fieldName : validatedFields) {
			final Boolean fieldValidated = metaModel.getFieldvalidated(fieldName);
			if (fieldValidated) {
				final Field fieldComponent = this.getField(fieldName);
				if (fieldComponent instanceof AbstractField) {
					log.debug("Cleanup error for field:{}", fieldName);
					((AbstractField)fieldComponent).setComponentError(null);
				}
			}
		}
	}

	private void setupFormFieldFactory(final MetaModel metaModel, final IUiMessageSource messageSource, final Locale locale) {
		final Class<? extends FormFieldFactory> formFieldFactory = metaModel.getFormFieldFactory();
		if (formFieldFactory != null) {
			try {
				final Constructor<? extends FormFieldFactory> constructor = formFieldFactory
						.getConstructor(new Class[] { IUiMessageSource.class, Locale.class });
				final EvidenceFormFieldFactory instance = (EvidenceFormFieldFactory) constructor
						.newInstance(new Object[] { messageSource, locale });
				this.setFormFieldFactory(instance);
			} catch (InstantiationException e) {
				throw new EvidenceFormException("InstantiationException for " + formFieldFactory.getName(), e);
			} catch (IllegalAccessException e) {
				throw new EvidenceFormException("IllegalAccessException for " + formFieldFactory.getName(), e);
			} catch (SecurityException e) {
				throw new EvidenceFormException("SecurityException for " + formFieldFactory.getName(), e);
			} catch (NoSuchMethodException e) {
				throw new EvidenceFormException("NoSuchMethodException for " + formFieldFactory.getName(), e);
			} catch (IllegalArgumentException e) {
				throw new EvidenceFormException("IllegalArgumentException for " + formFieldFactory.getName(), e);
			} catch (InvocationTargetException e) {
				throw new EvidenceFormException("InvocationTargetException for " + formFieldFactory.getName(), e);
			}
		}
	}
}
