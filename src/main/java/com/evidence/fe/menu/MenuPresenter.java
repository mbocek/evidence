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
package com.evidence.fe.menu;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.evidence.fe.children.ChildrenPresenter;
import com.evidence.fe.main.MainEventBus;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.Tree;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Component("menuPresenter")
@Scope("prototype")
@Presenter(view = MenuView.class)
public class MenuPresenter extends BasePresenter<IMenuView, MainEventBus> {

	@Override
	public void bind() {
		MenuEntry userAdminEntry = new MenuEntry(this.getMessage("menu.userChildren", this.getView().getLocale()),
				ChildrenPresenter.class);
		Tree tree = this.view.getTree();
		tree.addItem(userAdminEntry);
		tree.setChildrenAllowed(userAdminEntry, false);
	}

	public void onSelectMenu(ValueChangeEvent event) {
		// get the selected menu entry and initiate another event
		MenuEntry menuEntry = (MenuEntry) this.view.getTree().getValue();
		this.eventBus.openModule(menuEntry.getPresenterType());
	}
}
