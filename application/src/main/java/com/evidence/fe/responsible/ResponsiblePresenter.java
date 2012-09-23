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
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.evidence.dto.ResponsiblePersonDTO;
import com.evidence.fe.ApplicationConstants;
import com.evidence.fe.EvidenceApplication;
import com.evidence.fe.form.EvidenceForm;
import com.evidence.service.FormMetaModelService;
import com.evidence.service.ResponsiblePersonService;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Component("responsiblePresenter")
@Scope("prototype")
@Presenter(view = ResponsibleListView.class)
public class ResponsiblePresenter extends FactoryPresenter<IResponsibleListView, ResponsibleEventBus> {

	private BeanItemContainer<ResponsiblePersonDTO> container;

	private Window dialog = null;
	
	private EvidenceForm responsiblePersonForm = null;
	
	@Inject
	private ResponsiblePersonService responsiblePersonService;

    @Inject
    private Validator validator;  	
	
	@Inject
	private FormMetaModelService formService;
    
	@Override
	public void bind() {
		final HorizontalLayout buttonBar = this.view.getButtonBar();
		buttonBar.setExpandRatio(this.getView().getExpander(), 1.0f);
		final Table responsiblePersonList = this.view.getResponsibleList();
		container = new BeanItemContainer<ResponsiblePersonDTO>(ResponsiblePersonDTO.class);
		responsiblePersonList.setContainerDataSource(container);
		responsiblePersonList.setColumnHeader("fullName", this.getMessage("repsonsibility.list.header.fullName", this.getLocale()));
		responsiblePersonList.setColumnHeader("fullAddress", this.getMessage("repsonsibility.list.header.fullAddress", this.getLocale()));
		responsiblePersonList.setVisibleColumns(new String[] { "fullName", "fullAddress" });
		loadTeacherList();
	}

	private void loadTeacherList() {
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
/*
	public void onAddTeacher() throws ViewFactoryException {
		showCreateEditDialog(new TeacherDTO());
	}

	public void onRemoveTeacher() {
		// check if a user is selected in the table
		final Table teacherList = this.view.getTeacherList();
		final Object selected = teacherList.getValue();
		if (selected != null) {
			this.container.removeItem(selected);
			this.teacherService.delete(((TeacherDTO)selected).getId());
		}
	}

	@SuppressWarnings("unchecked")
	public void onSaveTeacher() {
		// get the user and add it to the container
		final BeanItem<TeacherDTO> item = (BeanItem<TeacherDTO>)this.teacherForm.getItemDataSource();
		final TeacherDTO teacher = item.getBean();
		final MetaModel metaModel = formService.getMetaModel(teacher);
		
		if (teacherForm.validate(metaModel, validator, teacher, this.messageSource, this.getLocale())) {
			//this.container.addBean(kindergarten);
			this.teacherService.createOrUpdateTeacher(teacher);
			// close dialog
			this.closeDialog();
			loadTeacherList();
		}
	}

	public void onEditTeacher(final ItemClickEvent event) throws ViewFactoryException {
		if (event.isDoubleClick()) {
			final Long id = ((TeacherDTO)event.getItemId()).getId();
			final TeacherDTO teacherDTO = this.teacherService.getById(id);
			showCreateEditDialog(teacherDTO);
		}
	}
	
	public void onCancelEditTeacher() {
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

	private void showCreateEditDialog(final TeacherDTO teacherDTO) throws ViewFactoryException {
		// create view
		final TeacherDetail view = this.createView(TeacherDetail.class);
		// configure the form with bean item
		this.teacherForm = view.getTeacherForm();
		final TeacherDTO teacher = teacherDTO;
		final MetaModel metaModel = formService.getMetaModel(teacher);
		this.teacherForm.setItemDataSource(teacher, metaModel, this.messageSource, "teacher.detail", this.getLocale());

		// create a window using caption from view
		this.dialog = new Window(this.getMessage("teacher.detail.caption", this.getLocale()));
		this.dialog.setModal(true);
		this.dialog.addComponent(view);
		this.dialog.getContent().setSizeUndefined();
		this.eventBus.showDialog(this.dialog);
	}*/
	
	public void onSelectKindergarten(ValueChangeEvent event) {
		loadTeacherList();
	}
	
}
