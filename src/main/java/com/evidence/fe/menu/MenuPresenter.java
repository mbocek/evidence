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
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.evidence.fe.children.ChildrenPresenter;
import com.evidence.fe.kindergarten.KindergartenPresenter;
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
public class MenuPresenter extends FactoryPresenter<IMenuView, MainEventBus> {

	@Override
	public void bind() {
		Tree tree = this.view.getTree();
		addEntry(tree, this.getMessage("menu.kindergarten", this.getLocale()), KindergartenPresenter.class);
		addEntry(tree, this.getMessage("menu.child", this.getLocale()), ChildrenPresenter.class);
	}
	
	private void addEntry(Tree tree, String caption, Class<? extends BasePresenter<?, ? extends EventBus>> presenterType) {
		MenuEntry entry = new MenuEntry(caption, presenterType);
		tree.addItem(entry);
		tree.setChildrenAllowed(entry, false);
	}

	public void onSelectMenu(ValueChangeEvent event) {
		// get the selected menu entry and initiate another event
		MenuEntry menuEntry = (MenuEntry) this.view.getTree().getValue();
		this.eventBus.openModule(menuEntry.getPresenterType());
	}
}
