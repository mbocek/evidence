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
package com.evidence.fe.kindergarden;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.ViewFactoryException;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.evidence.dto.KindergardenDTO;
import com.evidence.fe.annotation.MetaModel;
import com.evidence.fe.form.EvidenceForm;
import com.evidence.service.FormMetaModelService;
import com.evidence.service.KindergardenService;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Component("kindergardenPresenter")
@Scope("prototype")
@Presenter(view = KindergardenListView.class)
public class KindergardenPresenter extends FactoryPresenter<IKindergardenListView, KindergardenEventBus> {

	private static final Logger log = LoggerFactory.getLogger(KindergardenPresenter.class);

	private BeanItemContainer<KindergardenDTO> container;

	private Window dialog = null;
	private EvidenceForm userForm = null;
	
	@Inject
	private KindergardenService kindergardenService;

    @Inject
    private Validator validator;  	
	
	@Inject
	private FormMetaModelService formService;
    
	@Override
	public void bind() {
		Table kindergardenList = this.view.getKindergardenList();
		container = new BeanItemContainer<KindergardenDTO>(KindergardenDTO.class);
		kindergardenList.setContainerDataSource(container);
		kindergardenList.setColumnHeader("name", this.getMessage("kindergarden.list.header.name", this.getView().getLocale()));
		kindergardenList.setColumnHeader("fullAddress", this.getMessage("kindergarden.list.header.fullAddress", this.getView().getLocale()));
		kindergardenList.setVisibleColumns(new String[] { "name", "fullAddress" });
		loadKindergardenList();
	}

	private void loadKindergardenList() {
		List<KindergardenDTO> kindergardens = kindergardenService.getAll();
		this.container.addAll(kindergardens);
	}

	public void onAddKindergarden() throws ViewFactoryException {
		// create view
		KindergardenDetail view = this.createView(KindergardenDetail.class);

		// configure the form with bean item
		this.userForm = view.getKindergardenForm();
		KindergardenDTO kindergarden = new KindergardenDTO();
		kindergarden.setName("name");
		MetaModel metaModel = formService.getMetaModel(kindergarden);
		this.userForm.setItemDataSource(kindergarden, metaModel);

		// create a window using caption from view
		this.dialog = new Window(this.getMessage("kindergarden.detail.caption", this.getView().getLocale()));
		this.dialog.setModal(true);
		this.dialog.addComponent(view);
		this.dialog.setWidth("300px");
		this.eventBus.showDialog(this.dialog);
	}

	public void onRemoveKindergarden() {
		// check if a user is selected in the table
		Table kindergardenList = this.view.getKindergardenList();
		Object selected = kindergardenList.getValue();
		if (selected != null) {
			this.container.removeItem(selected);
		}
	}

	@SuppressWarnings("unchecked")
	public void onSaveUser() {
		// get the user and add it to the container
		BeanItem<KindergardenDTO> item = (BeanItem<KindergardenDTO>) this.userForm.getItemDataSource();
		KindergardenDTO kindergarden = item.getBean();
		
		BindingResult result = new BeanPropertyBindingResult(kindergarden, "kindergarden");
		validator.validate(kindergarden, result);  
		this.container.addBean(kindergarden);
		this.kindergardenService.addKindergarden(kindergarden);
		// close dialog
		this.closeDialog();
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
		this.userForm = null;
	}
	
	public void onEditUser(ItemClickEvent event) {
		log.info("edit");
		if (event.isDoubleClick()) {
			log.info("edit double click");
		}
	}
}
