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

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
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
		final HorizontalLayout buttonBar = this.view.getButtonBar();
		buttonBar.setExpandRatio(this.getView().getExpander(), 1.0f);
		final Table kindergartenList = this.view.getKindergartenList();
		container = new BeanItemContainer<KindergartenDTO>(KindergartenDTO.class);
		kindergartenList.setContainerDataSource(container);
		kindergartenList.setColumnHeader("name", this.getMessage("kindergarten.list.header.name", this.getLocale()));
		kindergartenList.setColumnHeader("fullAddress", this.getMessage("kindergarten.list.header.fullAddress", this.getLocale()));
		kindergartenList.setVisibleColumns(new String[] { "name", "fullAddress" });
		loadKindergartenList();
	}

	private void loadKindergartenList() {
		final List<KindergartenDTO> kindergartens = kindergartenService.getAll();
		this.container.removeAllItems();
		this.container.addAll(kindergartens);
	}

	public void onAddKindergarten() throws ViewFactoryException {
		showCreateEditDialog(new KindergartenDTO());
	}

	public void onRemoveKindergarten() {
		// check if a user is selected in the table
		final Table kindergartenList = this.view.getKindergartenList();
		final Object selected = kindergartenList.getValue();
		if (selected != null) {
			this.container.removeItem(selected);
		}
	}

	@SuppressWarnings("unchecked")
	public void onSaveKindergarten() {
		// get the user and add it to the container
		final BeanItem<KindergartenDTO> item = (BeanItem<KindergartenDTO>) this.kindergartenForm.getItemDataSource();
		final KindergartenDTO kindergarten = item.getBean();
		final MetaModel metaModel = formService.getMetaModel(kindergarten);
		
		if (kindergartenForm.validate(metaModel, validator, kindergarten, this.messageSource, this.getLocale())) {
			//this.container.addBean(kindergarten);
			this.kindergartenService.createOrUpdateKindergarten(kindergarten);
			// close dialog
			this.closeDialog();
			loadKindergartenList();
		}
	}

	public void onEditKindergarten(final ItemClickEvent event) throws ViewFactoryException {
		if (event.isDoubleClick()) {
			final Long id = ((KindergartenDTO)event.getItemId()).getId();
			final KindergartenDTO kindergartenDTO = this.kindergartenService.getById(id);
			showCreateEditDialog(kindergartenDTO);
		}
	}
	
	public void onCancelEditKindergarten() {
		// close dialog only
		this.closeDialog();
	}

	private void closeDialog() {
		// dismiss the dialog
		final Window applicationWindow = (Window) this.dialog.getParent();
		applicationWindow.removeWindow(this.dialog);
		//this.dialog = null;
		//this.kindergartenForm = null;
	}

	private void showCreateEditDialog(final KindergartenDTO kindergartenDTO) throws ViewFactoryException {
		// create view
		final KindergartenDetail view = this.createView(KindergartenDetail.class);
		// configure the form with bean item
		this.kindergartenForm = view.getKindergartenForm();
		final KindergartenDTO kindergarten = kindergartenDTO;
		final MetaModel metaModel = formService.getMetaModel(kindergarten);
		this.kindergartenForm.setItemDataSource(kindergarten, metaModel, this.messageSource, "kindergarten.detail", this.getLocale());

		// create a window using caption from view
		this.dialog = new Window(this.getMessage("kindergarten.detail.caption", this.getLocale()));
		this.dialog.setModal(true);
		this.dialog.addComponent(view);
		this.dialog.getContent().setSizeUndefined();
		this.eventBus.showDialog(this.dialog);
	}
}
