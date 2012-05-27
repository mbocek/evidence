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
package com.evidence.fe.children;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.ViewFactoryException;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.evidence.dto.ChildrenDTO;
import com.evidence.service.UserService;
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
@Component("childrenPresenter")
@Scope("prototype")
@Presenter(view = ChildrenListView.class)
public class ChildrenPresenter extends FactoryPresenter<IChildrenListView, ChildrenEventBus> {

	private static final Logger log = LoggerFactory.getLogger(ChildrenPresenter.class); // NOPMD

	private BeanItemContainer<ChildrenDTO> container;
	private Window dialog = null;
	private Form userForm = null;
	
	@Autowired
	private UserService userService;

	@Override
	public void bind() {
		final Table userList = this.view.getUserList();
		container = new BeanItemContainer<ChildrenDTO>(ChildrenDTO.class);
		userList.setContainerDataSource(container);
		userList.setVisibleColumns(new String[] { "name", "surName" });
		loadUserList();
	}

	private void loadUserList() {
		final List<ChildrenDTO> children = userService.getAll();
		this.container.addAll(children);
	}

	/*
	 * first version public void onCreateUser() { // create an user User u = new User(); u.setUserName("newuser");
	 * u.setFirstName("First name"); u.setLastName("Last name"); this.container.addBean(u); }
	 */

	/* second version, using popup */
	public void onCreateUser() throws ViewFactoryException {
		// create view
		final ChildrenView view = this.createView(ChildrenView.class);

		// configure the form with bean item
		this.userForm = view.getChildrenForm();
		final ChildrenDTO children = new ChildrenDTO();
		children.setName("name");
		children.setSurName("last name");
		final BeanItem<ChildrenDTO> beanItem = new BeanItem<ChildrenDTO>(children);
		this.userForm.setItemDataSource(beanItem);

		// create a window using caption from view
		this.dialog = new Window(view.getCaption());
		view.setCaption(null);
		this.dialog.setModal(true);
		this.dialog.addComponent(view);
		this.dialog.setWidth("300px");
		this.eventBus.showDialog(this.dialog);
	}

	public void onRemoveUser() {
		// check if a user is selected in the table
		final Table userList = this.view.getUserList();
		final Object selected = userList.getValue();
		if (selected != null) {
			this.container.removeItem(selected);
		}
	}

	@SuppressWarnings("unchecked")
	public void onSaveUser() {
		// get the user and add it to the container
		final BeanItem<ChildrenDTO> item = (BeanItem<ChildrenDTO>) this.userForm.getItemDataSource();
		final ChildrenDTO child = item.getBean();
		this.container.addBean(child);
		this.userService.addChild(child);
		// close dialog
		this.closeDialog();
	}

	public void onCancelEditUser() {
		// close dialog only
		this.closeDialog();
	}

	private void closeDialog() {
		// dismiss the dialog
		final Window applicationWindow = (Window) this.dialog.getParent();
		applicationWindow.removeWindow(this.dialog);
		//this.dialog = null;
		//this.userForm = null;
	}
	
	public void onEditUser(final ItemClickEvent event) {
		log.info("edit");
		if (event.isDoubleClick()) {
			log.info("edit double click");
		}
	}
}
