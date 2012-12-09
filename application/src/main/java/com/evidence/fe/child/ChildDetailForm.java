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

import org.vaadin.mvp.uibinder.IUiMessageSource;

import com.evidence.dto.ChildDTO;
import com.evidence.dto.ResponsiblePersonDTO;
import com.evidence.fe.form.EvidenceForm;
import com.evidence.fe.form.MetaModel;
import com.evidence.fe.form.Model;
import com.evidence.service.ServiceHolder;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Select;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ChildDetailForm extends EvidenceForm {

	private static final long serialVersionUID = 1L;
	
	private GridLayout layout;
	
	public ChildDetailForm() {
		layout = new GridLayout(2, 5);

        layout.setMargin(false, false, false, false);
        layout.setSpacing(true);
        
		setLayout(layout);
	}
	
	@Override
	protected void attachField(Object propertyId, Field field) {
		if (propertyId.equals("name")) {
			 layout.addComponent(field, 0, 0);
		} else if (propertyId.equals("surName")) {
			 layout.addComponent(field, 1, 0);
		} else if (propertyId.equals("birthDate")) {
			 layout.addComponent(field, 0, 1, 1, 1);
		} else if (propertyId.equals("motherId")) {
			 layout.addComponent(field, 0, 2, 1, 2);
		} else if (propertyId.equals("fatherId")) {
			 layout.addComponent(field, 0, 3, 1, 3);
		} else if (propertyId.equals("responsiblePersonId")) {
			 layout.addComponent(field, 0, 4, 1, 4);
		}
	}
	
	@Override
	public void setItemDataSource(Model model, MetaModel metaModel, IUiMessageSource messageSource,
			String messagePrefix, Locale locale) {
		super.setItemDataSource(model, metaModel, messageSource, messagePrefix, locale);
		
		Long kindergartenId = ((ChildDTO)model).getKindergartenId();
		fillMothers(kindergartenId);
		fillFathers(kindergartenId);
		fillResponsiblePersons(kindergartenId);
	}

	private void fillMothers(Long kindergartenId) {
		Select mother = (Select)this.getField("motherId");
		final List<ResponsiblePersonDTO> responsiblePersons = ServiceHolder.getInstance().getResponsibleService().findMothersByKindergartenId(kindergartenId);
		for (ResponsiblePersonDTO responsiblePerson : responsiblePersons) {
			mother.addItem(responsiblePerson.getId());
			mother.setItemCaption(responsiblePerson.getId(), responsiblePerson.getFullName());
		}
	}
	
	private void fillFathers(Long kindergartenId) {
		Select father = (Select)this.getField("fatherId");
		final List<ResponsiblePersonDTO> responsiblePersons = ServiceHolder.getInstance().getResponsibleService().findFathersByKindergartenId(kindergartenId);
		for (ResponsiblePersonDTO responsiblePerson : responsiblePersons) {
			father.addItem(responsiblePerson.getId());
			father.setItemCaption(responsiblePerson.getId(), responsiblePerson.getFullName());
		}
	}

	private void fillResponsiblePersons(Long kindergartenId) {
		Select responsiblePersonSelect = (Select)this.getField("responsiblePersonId");
		final List<ResponsiblePersonDTO> responsiblePersons = ServiceHolder.getInstance().getResponsibleService().findResponsiblePersonsByKindergartenId(kindergartenId);
		for (ResponsiblePersonDTO responsiblePerson : responsiblePersons) {
			responsiblePersonSelect.addItem(responsiblePerson.getId());
			responsiblePersonSelect.setItemCaption(responsiblePerson.getId(), responsiblePerson.getFullName());
		}
	}
}
