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
package com.evidence.fe.teacher;

import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

import org.vaadin.mvp.uibinder.IUiMessageSource;

import com.evidence.dto.KindergartenDTO;
import com.evidence.dto.StateDTO;
import com.evidence.fe.form.EvidenceFormFieldFactory;
import com.evidence.service.ServiceHolder;
import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Select;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
public class TeacherDetailFormFieldFactory extends EvidenceFormFieldFactory {

	private static final long serialVersionUID = 1L;

	public TeacherDetailFormFieldFactory(final IUiMessageSource messageSource, final Locale locale) {
		super(messageSource, locale);
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.FormFieldFactory#createField(com.vaadin.data.Item, java.lang.Object, com.vaadin.ui.Component)
	 */
	@Override
	public Field createField(final Item item, final Object propertyId, final Component uiContext) {
		log.debug("Item: {}; Property id: {}; Componet: {}", new Object[] {item, propertyId, uiContext});
		Field field = null;
		final String pid = (String) propertyId;
		if ("contact.address.stateCode".equals(pid)) {
			final Select select = new Select(pid);
			final List<StateDTO> states = ServiceHolder.getInstance().getCodeListService().getStates();
			log.debug(states.toString());
			for (StateDTO stateDTO : states) {
				select.addItem(stateDTO.getCode());
				select.setItemCaption(stateDTO.getCode(), stateDTO.getName());
			}
			select.setNewItemsAllowed(false);
			select.setNullSelectionAllowed(false);
			field = select;
		} else if ("kindergartenId".equals(pid)) {
			final Select select = new Select(pid);
			final List<KindergartenDTO> kindergartens = ServiceHolder.getInstance().getKindergartenService().getAll();
			log.debug(kindergartens.toString());
			for (KindergartenDTO kindergartenDTO : kindergartens) {
				select.addItem(kindergartenDTO.getId());
				select.setItemCaption(kindergartenDTO.getId(), kindergartenDTO.getName());
			}
			select.setNewItemsAllowed(false);
			select.setNullSelectionAllowed(false);
			field = select;
		} else {
			field = super.createField(item, propertyId, uiContext);
		}
		return field;
	}

}
