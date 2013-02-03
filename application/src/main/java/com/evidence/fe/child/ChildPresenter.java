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

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.ViewFactoryException;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.evidence.dto.ChildDTO;
import com.evidence.fe.ApplicationConstants;
import com.evidence.fe.EvidenceApplication;
import com.evidence.fe.components.EvidenceUpload;
import com.evidence.fe.form.MetaModel;
import com.evidence.service.ChildService;
import com.evidence.service.FormMetaModelService;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Component("childPresenter")
@Scope("prototype")
@Presenter(view = ChildListView.class)
public class ChildPresenter extends FactoryPresenter<IChildListView, ChildEventBus> {

	private BeanItemContainer<ChildDTO> container;

	private Window dialog = null;
	
	private ChildDetailForm childForm = null;
	
	@Inject
	private ChildService childService;

    @Inject
    private Validator validator;  	
	
	@Inject
	private FormMetaModelService formService;
    
	private EvidenceUpload upload;
	
	@Override
	public void bind() {
		final HorizontalLayout buttonBar = this.view.getButtonBar();
		buttonBar.setExpandRatio(this.getView().getExpander(), 1.0f);
		final Table childList = this.view.getChildList();
		container = new BeanItemContainer<ChildDTO>(ChildDTO.class);
		childList.setContainerDataSource(container);
		childList.setColumnHeader("fullName", this.getMessage("child.list.header.fullName", this.getLocale()));
		//childList.setColumnHeader("fullAddress", this.getMessage("child.list.header.fullAddress", this.getLocale()));
		childList.setVisibleColumns(new String[] { "fullName"});
		loadChildList();
	}

	private void loadChildList() {
		List<ChildDTO> teachers; 
		if (getKindergartenId().longValue() == ApplicationConstants.SELECT_ALL.longValue()) {
			teachers = childService.getAll();
		} else {
			teachers = childService.findByKindergartenId(getKindergartenId());
		}
		this.container.removeAllItems();
		this.container.addAll(teachers);
	}
	
	private Long getKindergartenId() {
		return ((EvidenceApplication)this.getApplication()).getKindergartenId();
	}

	public void onAddChild() throws ViewFactoryException {
		if (getKindergartenId().longValue() == ApplicationConstants.SELECT_ALL.longValue()) {
			this.showNotification(
							this.getMessage("child.detail.kindergartenNotSelected.caption", this.getLocale()),
							this.getMessage("child.detail.kindergartenNotSelected.description", this.getLocale()),
							Notification.TYPE_ERROR_MESSAGE);
		} else {
			showCreateEditDialog(new ChildDTO(getKindergartenId()));
		}
	}

	public void onRemoveChild() {
		// check if a user is selected in the table
		final Table childList = this.view.getChildList();
		final Object selected = childList.getValue();
		if (selected != null) {
			this.container.removeItem(selected);
			this.childService.delete(((ChildDTO)selected).getId());
		}
	}

	@SuppressWarnings("unchecked")
	public void onSaveChild() {
		// get the user and add it to the container
		final BeanItem<ChildDTO> item = (BeanItem<ChildDTO>)this.childForm.getItemDataSource();
		final ChildDTO child = item.getBean();
		final MetaModel metaModel = formService.getMetaModel(child);
		
		if (childForm.validate(metaModel, validator, child, this.messageSource, this.getLocale())) {
			child.setPhoto(upload.getImage());
			this.childService.createOrUpdateChild(child);
			// close dialog
			this.closeDialog();
			loadChildList();
		}
	}

	public void onEditChild(final ItemClickEvent event) throws ViewFactoryException {
		if (event.isDoubleClick()) {
			final Long id = ((ChildDTO)event.getItemId()).getId();
			final ChildDTO childDTO = this.childService.getById(id);
			showCreateEditDialog(childDTO);
		}
	}
	
	public void onCancelEditChild() {
		// close dialog only
		this.closeDialog();
	}

	private void closeDialog() {
		// dismiss the dialog
		final Window applicationWindow = this.dialog.getParent();
		applicationWindow.removeWindow(this.dialog);
		this.dialog = null;
		this.childForm = null;
	}

	private void showCreateEditDialog(final ChildDTO childDTO) throws ViewFactoryException {
		// create view
		final ChildDetail view = this.createView(ChildDetail.class);
		// configure the form with bean item
		this.childForm = (ChildDetailForm)view.getChildForm();
		this.childForm.getFooter().setMargin(true, false, false, false);
		final MetaModel metaModel = formService.getMetaModel(childDTO);
		this.childForm.setItemDataSource(childDTO, metaModel, this.messageSource, "child.detail", this.getLocale());

		// photo upload setup
		upload = new EvidenceUpload(this.messageSource, this.getLocale());
		upload.setCaption(this.getMessage("child.detail.upload", this.getLocale()));
		this.childForm.addUpload(upload);
		// create a window using caption from view
		this.dialog = new Window(this.getMessage("child.detail.caption", this.getLocale()));
		this.dialog.setModal(true);
		this.dialog.addComponent(view);
		this.dialog.getContent().setSizeUndefined();
		this.eventBus.showDialog(this.dialog);
		if (childDTO.getPhoto() != null) {
			upload.showImage(childDTO.getPhoto());
		} else {
			upload.showImage(ApplicationConstants.NO_PHOTO);
		}
	}
	
	public void onSelectKindergarten(ValueChangeEvent event) {
		loadChildList();
	}
	
	public void onSaveResponsiblePerson() {
		if (this.childForm != null) {
			Long kindergartenId = (Long)this.childForm.getItemProperty("kindergartenId").getValue();
			Select select = (Select) this.childForm.getField("motherId");
			((ChildDetailFormFieldFactory)this.childForm.getFormFieldFactory()).reloadMothers(kindergartenId, select);
			select = (Select) this.childForm.getField("fatherId");
			((ChildDetailFormFieldFactory)this.childForm.getFormFieldFactory()).reloadFathers(kindergartenId, select);
			select = (Select) this.childForm.getField("responsiblePersonId");
			((ChildDetailFormFieldFactory)this.childForm.getFormFieldFactory()).reloadResponsiblePersons(kindergartenId, select);
		}
	}
}
