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
package com.evidence.fe.responsible;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.ViewFactoryException;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.evidence.dto.ResponsiblePersonDTO;
import com.evidence.fe.ApplicationConstants;
import com.evidence.fe.EvidenceApplication;
import com.evidence.fe.EvidenceUpload;
import com.evidence.fe.form.EvidenceForm;
import com.evidence.fe.form.MetaModel;
import com.evidence.service.FormMetaModelService;
import com.evidence.service.ResponsiblePersonService;
import com.evidence.utility.UIUtils;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Component("responsiblePersonPresenter")
@Scope("prototype")
@Presenter(view = ResponsiblePersonListView.class)
public class ResponsiblePersonPresenter extends FactoryPresenter<IResponsiblePersonListView, ResponsiblePersonEventBus> {

	private BeanItemContainer<ResponsiblePersonDTO> container;

	private Window dialog = null;
	
	private EvidenceForm responsiblePersonForm = null;
	
	@Inject
	private ResponsiblePersonService responsiblePersonService;

    @Inject
    private Validator validator;  	
	
	@Inject
	private FormMetaModelService formService;
	
	private EvidenceUpload upload;
    
	@Override
	public void bind() {
		final HorizontalLayout buttonBar = this.view.getButtonBar();
		buttonBar.setExpandRatio(this.getView().getExpander(), 1.0f);
		final Table responsiblePersonList = this.view.getResponsiblePersonList();
		container = new BeanItemContainer<ResponsiblePersonDTO>(ResponsiblePersonDTO.class);
		responsiblePersonList.setContainerDataSource(container);
		responsiblePersonList.setColumnHeader("fullName", this.getMessage("responsiblePerson.list.header.fullName", this.getLocale()));
		responsiblePersonList.setColumnHeader("fullAddress", this.getMessage("responsiblePerson.list.header.fullAddress", this.getLocale()));
		responsiblePersonList.setVisibleColumns(new String[] { "fullName", "fullAddress" });
		loadResponsiblePersonList();
	}

	private void loadResponsiblePersonList() {
		List<ResponsiblePersonDTO> responsiblePersons; 
		if (getKindergartenId().longValue() == ApplicationConstants.SELECT_ALL.longValue()) {
			responsiblePersons = responsiblePersonService.getAll();
		} else {
			responsiblePersons = responsiblePersonService.findByKindergartenId(getKindergartenId());
		}
		this.container.removeAllItems();
		this.container.addAll(responsiblePersons);
	}
	
	private Long getKindergartenId() {
		return ((EvidenceApplication)this.getApplication()).getKindergartenId();
	}
	
	public void onAddResponsiblePerson() throws ViewFactoryException {
		if (getKindergartenId().longValue() == ApplicationConstants.SELECT_ALL.longValue()) {
			this.showNotification(
							this.getMessage("child.detail.kindergartenNotSelected.caption", this.getLocale()),
							this.getMessage("child.detail.kindergartenNotSelected.description", this.getLocale()),
							Notification.TYPE_ERROR_MESSAGE);
		} else {
			showCreateEditDialog(new ResponsiblePersonDTO(getKindergartenId()));
		}
	}

	public void onRemoveResponsiblePerson() {
		// check if a user is selected in the table
		final Table repsonsiblePersonList = this.view.getResponsiblePersonList();
		final Object selected = repsonsiblePersonList.getValue();
		if (selected != null) {
			this.container.removeItem(selected);
			this.responsiblePersonService.delete(((ResponsiblePersonDTO)selected).getId());
		}
	}

	@SuppressWarnings("unchecked")
	public void onSaveResponsiblePerson() {
		// get the user and add it to the container
		final BeanItem<ResponsiblePersonDTO> item = (BeanItem<ResponsiblePersonDTO>)this.responsiblePersonForm.getItemDataSource();
		final ResponsiblePersonDTO responsiblePerson = item.getBean();
		final MetaModel metaModel = formService.getMetaModel(responsiblePerson);
		
		if (responsiblePersonForm.validate(metaModel, validator, responsiblePerson, this.messageSource, this.getLocale())) {
			responsiblePerson.setPhoto(upload.getImage());
			//this.container.addBean(kindergarten);
			this.responsiblePersonService.createOrUpdateResponsiblePerson(responsiblePerson);
			// close dialog
			this.closeDialog();
			loadResponsiblePersonList();
		}
	}

	public void onEditResponsiblePerson(final ItemClickEvent event) throws ViewFactoryException {
		if (event.isDoubleClick()) {
			final Long id = ((ResponsiblePersonDTO)event.getItemId()).getId();
			final ResponsiblePersonDTO responsiblePersonDTO = this.responsiblePersonService.getById(id);
			showCreateEditDialog(responsiblePersonDTO);
		}
	}
	
	public void onCancelEditResponsiblePerson() {
		// close dialog only
		this.closeDialog();
	}

	private void closeDialog() {
		// dismiss the dialog
		final Window applicationWindow = this.dialog.getParent();
		applicationWindow.removeWindow(this.dialog);
		this.dialog = null;
		//this.kindergartenForm = null;
	}

	private void showCreateEditDialog(final ResponsiblePersonDTO responsiblePersonDTO) throws ViewFactoryException {
		// create view
		final ResponsiblePersonDetail view = this.createView(ResponsiblePersonDetail.class);
		UIUtils.clearBorder(view.getContainer());
		// configure the form with bean item
		this.responsiblePersonForm = view.getResponsiblePersonForm();
		final MetaModel metaModel = formService.getMetaModel(responsiblePersonDTO);
		this.responsiblePersonForm.setItemDataSource(responsiblePersonDTO, metaModel, this.messageSource, 
				"responsiblePerson.detail", this.getLocale());
		// photo upload setup
		upload = new EvidenceUpload(this.messageSource, this.getLocale());
		upload.setCaption(this.getMessage("responsiblePerson.detail.upload", this.getLocale()));
		this.responsiblePersonForm.getLayout().addComponent(upload);
		// create a window using caption from view
		this.dialog = new Window(this.getMessage("responsiblePerson.detail.caption", this.getLocale()));
		this.dialog.setModal(true);
		this.dialog.addComponent(view);
		this.dialog.getContent().setSizeUndefined();
		this.eventBus.showDialog(this.dialog);
		if (responsiblePersonDTO.getPhoto() != null) {
			upload.showImage(responsiblePersonDTO.getPhoto());
		}
	}
	
	public void onSelectKindergarten(ValueChangeEvent event) {
		loadResponsiblePersonList();
	}
}
