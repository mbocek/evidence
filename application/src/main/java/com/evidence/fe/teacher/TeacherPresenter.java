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

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.ViewFactoryException;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.evidence.dto.KindergartenDTO;
import com.evidence.dto.TeacherDTO;
import com.evidence.fe.annotation.MetaModel;
import com.evidence.fe.form.EvidenceForm;
import com.evidence.service.FormMetaModelService;
import com.evidence.service.TeacherService;
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
@Component("teacherPresenter")
@Scope("prototype")
@Presenter(view = TeacherListView.class)
public class TeacherPresenter extends FactoryPresenter<ITeacherListView, TeacherEventBus> {

	private static final Logger log = LoggerFactory.getLogger(TeacherPresenter.class); // NOPMD

	private BeanItemContainer<TeacherDTO> container;

	private Window dialog = null;
	
	private EvidenceForm teacherForm = null;
	
	@Inject
	private TeacherService teacherService;

    @Inject
    private Validator validator;  	
	
	@Inject
	private FormMetaModelService formService;
    
	@Override
	public void bind() {
		final HorizontalLayout buttonBar = this.view.getButtonBar();
		buttonBar.setExpandRatio(this.getView().getExpander(), 1.0f);
		final Table teacherList = this.view.getTeacherList();
		container = new BeanItemContainer<TeacherDTO>(TeacherDTO.class);
		teacherList.setContainerDataSource(container);
		teacherList.setColumnHeader("name", this.getMessage("teacher.list.header.name", this.getLocale()));
		teacherList.setColumnHeader("fullAddress", this.getMessage("teacher.list.header.fullAddress", this.getLocale()));
		teacherList.setVisibleColumns(new String[] { "name", "fullAddress" });
		loadTeacherList();
	}

	private void loadTeacherList() {
		final List<TeacherDTO> teachers = teacherService.getAll();
		this.container.removeAllItems();
		this.container.addAll(teachers);
	}

	public void onAddTeacher() throws ViewFactoryException {
		showCreateEditDialog(new TeacherDTO());
	}

	public void onRemoveTeacher() {
		// check if a user is selected in the table
		final Table teacherList = this.view.getTeacherList();
		final Object selected = teacherList.getValue();
		if (selected != null) {
			this.container.removeItem(selected);
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
		/*final KindergartenDetail view = this.createView(TeacherDetail.class);
		// configure the form with bean item
		this.kindergartenForm = view.getKindergartenForm();
		final KindergartenDTO kindergarten = teacherDTO;
		final MetaModel metaModel = formService.getMetaModel(kindergarten);
		this.kindergartenForm.setItemDataSource(kindergarten, metaModel, this.messageSource, "kindergarten.detail", this.getLocale());

		// create a window using caption from view
		this.dialog = new Window(this.getMessage("kindergarten.detail.caption", this.getLocale()));
		this.dialog.setModal(true);
		this.dialog.addComponent(view);
		this.dialog.getContent().setSizeUndefined();
		this.eventBus.showDialog(this.dialog);*/
	}
}