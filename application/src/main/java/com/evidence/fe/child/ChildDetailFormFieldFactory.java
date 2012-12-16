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
package com.evidence.fe.child;

import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

import org.vaadin.mvp.uibinder.IUiMessageSource;

import com.evidence.dto.ResponsiblePersonDTO;
import com.evidence.fe.form.EvidenceFormFieldFactory;
import com.evidence.service.ServiceHolder;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.Select;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
public class ChildDetailFormFieldFactory extends EvidenceFormFieldFactory {

	private static final long serialVersionUID = 1L;

	public ChildDetailFormFieldFactory(final IUiMessageSource messageSource, final Locale locale) {
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
		if ("birthDate".equals(pid)) {
			field = super.createField(item, propertyId, uiContext);
			field.setWidth("100%");
			((DateField)field).setDateFormat(this.getMessage("date.format"));
		} else if ("motherId".equals(pid)) {
			final Select select = new Select(pid);
			Long kindergartenId = (Long)item.getItemProperty("kindergartenId").getValue();
			reloadMothers(kindergartenId, select);
			select.setNewItemsAllowed(false);
			select.setNullSelectionAllowed(false);
			select.setWidth("100%");
			field = select;
		} else if ("fatherId".equals(pid)) {
			final Select select = new Select(pid);
			Long kindergartenId = (Long)item.getItemProperty("kindergartenId").getValue();
			reloadFathers(kindergartenId, select);
			select.setNewItemsAllowed(false);
			select.setNullSelectionAllowed(false);
			select.setWidth("100%");
			field = select;
		} else if ("responsiblePersonId".equals(pid)) {
			final Select select = new Select(pid);
			Long kindergartenId = (Long)item.getItemProperty("kindergartenId").getValue();
			reloadResponsiblePersons(kindergartenId, select);
			select.setNewItemsAllowed(false);
			select.setNullSelectionAllowed(false);
			select.setWidth("100%");
			field = select;
		} else {
			field = super.createField(item, propertyId, uiContext);
		}
		return field;
	}
	
	protected void reloadMothers(final Long kindergartenId, final Select mother) {
		log.debug("reloading mothers for kindergatend {}", kindergartenId);
		final List<ResponsiblePersonDTO> responsiblePersons = ServiceHolder.getInstance().getResponsibleService().findMothersByKindergartenId(kindergartenId);
		for (ResponsiblePersonDTO responsiblePerson : responsiblePersons) {
			if (!mother.getItemIds().contains(responsiblePerson.getId())) {
				mother.addItem(responsiblePerson.getId());
				mother.setItemCaption(responsiblePerson.getId(), responsiblePerson.getFullName());
			}
		}
	}
	
	protected void reloadFathers(final Long kindergartenId, final Select father) {
		log.debug("reloading father for kindergatend {}", kindergartenId);
		final List<ResponsiblePersonDTO> responsiblePersons = ServiceHolder.getInstance().getResponsibleService().findFathersByKindergartenId(kindergartenId);
		for (ResponsiblePersonDTO responsiblePerson : responsiblePersons) {
			if (!father.getItemIds().contains(responsiblePerson.getId())) {
				father.addItem(responsiblePerson.getId());
				father.setItemCaption(responsiblePerson.getId(), responsiblePerson.getFullName());
			}
		}
	}

	protected void reloadResponsiblePersons(final Long kindergartenId, final Select responsiblePersonSelect) {
		log.debug("reloading responsible person for kindergatend {}", kindergartenId);
		final List<ResponsiblePersonDTO> responsiblePersons = ServiceHolder.getInstance().getResponsibleService().findResponsiblePersonsByKindergartenId(kindergartenId);
		for (ResponsiblePersonDTO responsiblePerson : responsiblePersons) {
			if (!responsiblePersonSelect.getItemIds().contains(responsiblePerson.getId())) {
				responsiblePersonSelect.addItem(responsiblePerson.getId());
				responsiblePersonSelect.setItemCaption(responsiblePerson.getId(), responsiblePerson.getFullName());
			}
		}
	}
	
}
