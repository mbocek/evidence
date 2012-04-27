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
package com.evidence.fe.kindergarten;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.ViewFactoryException;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.evidence.dto.KindergartenDTO;
import com.evidence.fe.annotation.MetaModel;
import com.evidence.fe.form.EvidenceForm;
import com.evidence.service.FormMetaModelService;
import com.evidence.service.KindergartenService;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Component("kindergartenPresenter")
@Scope("prototype")
@Presenter(view = KindergartenListView.class)
public class KindergartenPresenter extends FactoryPresenter<IKindergartenListView, KindergartenEventBus> {

	private static final Logger log = LoggerFactory.getLogger(KindergartenPresenter.class);

	private BeanItemContainer<KindergartenDTO> container;

	private Window dialog = null;
	
	private EvidenceForm kindergartenForm = null;
	
	@Inject
	private KindergartenService kindergartenService;

    @Inject
    private Validator validator;  	
	
	@Inject
	private FormMetaModelService formService;
    
	@Override
	public void bind() {
		HorizontalLayout buttonBar = this.view.getButtonBar();
		buttonBar.setExpandRatio(this.getView().getExpander(), 1.0f);
		Table kindergartenList = this.view.getKindergartenList();
		container = new BeanItemContainer<KindergartenDTO>(KindergartenDTO.class);
		kindergartenList.setContainerDataSource(container);
		kindergartenList.setColumnHeader("name", this.getMessage("kindergarten.list.header.name", this.getLocale()));
		kindergartenList.setColumnHeader("fullAddress", this.getMessage("kindergarten.list.header.fullAddress", this.getLocale()));
		kindergartenList.setVisibleColumns(new String[] { "name", "fullAddress" });
		loadKindergartenList();
	}

	private void loadKindergartenList() {
		List<KindergartenDTO> kindergartens = kindergartenService.getAll();
		this.container.addAll(kindergartens);
	}

	public void onAddKindergarten() throws ViewFactoryException {
		// create view
		KindergartenDetail view = this.createView(KindergartenDetail.class);
		// configure the form with bean item
		this.kindergartenForm = view.getKindergartenForm();
		KindergartenDTO kindergarten = new KindergartenDTO();
		MetaModel metaModel = formService.getMetaModel(kindergarten);
		this.kindergartenForm.setItemDataSource(kindergarten, metaModel, this.messageSource, this.getLocale());

		// create a window using caption from view
		this.dialog = new Window(this.getMessage("kindergarten.detail.caption", this.getLocale()));
		this.dialog.setModal(true);
		this.dialog.addComponent(view);
		this.dialog.getContent().setSizeUndefined();
		this.eventBus.showDialog(this.dialog);
	}

	public void onRemoveKindergarten() {
		// check if a user is selected in the table
		Table kindergartenList = this.view.getKindergartenList();
		Object selected = kindergartenList.getValue();
		if (selected != null) {
			this.container.removeItem(selected);
		}
	}

	@SuppressWarnings("unchecked")
	public void onSaveUser() {
		// get the user and add it to the container
		BeanItem<KindergartenDTO> item = (BeanItem<KindergartenDTO>) this.kindergartenForm.getItemDataSource();
		KindergartenDTO kindergarten = item.getBean();
		
		BindingResult result = new BeanPropertyBindingResult(kindergarten, "kindergarten");
		validator.validate(kindergarten, result);
		if (result.hasErrors()) {
			for (FieldError error : result.getFieldErrors()) {
				String field = error.getField();
				String message = error.getDefaultMessage();
				Field fieldComponent = this.kindergartenForm.getField(field);
				if (fieldComponent != null && fieldComponent instanceof AbstractField) {
					((AbstractField)fieldComponent).setComponentError(new UserError(message));
				}
			}
		} else {
			//this.container.addBean(kindergarten);
			this.kindergartenService.addKindergarten(kindergarten);
			// close dialog
			this.closeDialog();
		}
	}

	public void onCancelEditUser() {
		// close dialog only
		this.closeDialog();
	}

	private void closeDialog() {
		// dismiss the dialog
		Window applicationWindow = (Window) this.dialog.getParent();
		applicationWindow.removeWindow(this.dialog);
		this.dialog = null;
		this.kindergartenForm = null;
	}
	
	public void onEditUser(ItemClickEvent event) {
		log.info("edit");
		if (event.isDoubleClick()) {
			log.info("edit double click");
		}
	}
}
