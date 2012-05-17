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

import com.evidence.fe.annotation.MetaModel;
import com.vaadin.data.util.BeanItem;
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

	private static final long serialVersionUID = 1L;
	
	public void addFooter(Component component) {
		this.getFooter().addComponent(component);
	}

	public void setItemDataSource(Model model, MetaModel metaModel, IUiMessageSource messageSource, Locale locale) {
		//Class<? extends Model> clazz = model.getClass();
		BeanItem<Model> beanItem = new BeanItem<Model>(model);
		this.setItemDataSource(beanItem);
		setupFormFieldFactory(metaModel, messageSource, locale);
		this.setVisibleItemProperties(metaModel.getOrderedFields());
		for (String fieldName : metaModel.getCaptionedFields()) {
			log.debug("Processing caption for field:{}", fieldName);
			String fieldCaption = metaModel.getFieldCaption(fieldName);
			Field field = this.getField(fieldName);
			field.setCaption(messageSource.getMessage(fieldCaption, locale));
		}
		for (String fieldName : metaModel.getRequiredFields()) {
			log.debug("Processing required for field:{}", fieldName);
			Boolean fieldRequired = metaModel.getFieldRequired(fieldName);
			Field field = this.getField(fieldName);
			log.debug("Field:{} is required:{}", fieldName, fieldRequired);
			field.setRequired(fieldRequired);
			field.setRequiredError(messageSource.getMessage("field.required", locale));
		}
	}

	public boolean validate(MetaModel metaModel, Validator validator, Model model) {
		boolean result = false; 
		BindingResult validationResult = new BeanPropertyBindingResult(model, model.getClass().getName());
		clearFields(metaModel);
		validator.validate(model, validationResult);
		if (validationResult.hasErrors()) {
			for (FieldError error : validationResult.getFieldErrors()) {
				String field = error.getField();
				String message = error.getDefaultMessage();
				Field fieldComponent = this.getField(field);
				if (fieldComponent != null && fieldComponent instanceof AbstractField) {
					((AbstractField)fieldComponent).setComponentError(new UserError(message));
				}
			}
			result = false;
		} else {
			result = true;
		}
		return result;
		
	}
	
	private void clearFields(MetaModel metaModel) {
		Collection<String> validatedFields = metaModel.getValidatedFields();
		for (String fieldName : validatedFields) {
			Boolean fieldValidated = metaModel.getFieldvalidated(fieldName);
			if (fieldValidated) {
				Field fieldComponent = this.getField(fieldName);
				if (fieldComponent != null && fieldComponent instanceof AbstractField) {
					log.debug("Cleanup error for field:{}", fieldName);
					((AbstractField)fieldComponent).setComponentError(null);
				}
			}
		}
	}

	private void setupFormFieldFactory(MetaModel metaModel, IUiMessageSource messageSource, Locale locale) {
		Class<? extends FormFieldFactory> formFieldFactory = metaModel.getFormFieldFactory();
		if (formFieldFactory != null) {
			try {
				Constructor<? extends FormFieldFactory> constructor = formFieldFactory.getConstructor(new Class[] { IUiMessageSource.class, Locale.class });
				EvidenceFormFieldFactory instance = (EvidenceFormFieldFactory) constructor.newInstance(new Object[] { messageSource, locale });				
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
