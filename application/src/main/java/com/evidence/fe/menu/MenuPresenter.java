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

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.evidence.fe.kindergarten.KindergartenPresenter;
import com.evidence.fe.main.MainEventBus;
import com.evidence.fe.teacher.TeacherPresenter;
import com.evidence.service.KindergartenService;
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

	@Inject
	private KindergartenService kindergartenService;
	
	@Override
	public void bind() {
		final Tree tree = this.view.getTree();
		addEntry(tree, null, Boolean.FALSE, this.getMessage("menu.kindergarten", this.getLocale()), KindergartenPresenter.class);
		//List<KindergartenDTO> kindergartens = kindergartenService.getAll();
		//for (KindergartenDTO kindergartenDTO : kindergartens) {
		//	MenuEntry kindergarten = addEntry(tree, kindergartenMenu, Boolean.TRUE, kindergartenDTO.getName(), null);
		addEntry(tree, null, Boolean.FALSE, this.getMessage("menu.teacher", this.getLocale()), TeacherPresenter.class);
		//}
	}
	
	private MenuEntry addEntry(final Tree tree, final MenuEntry parent, final Boolean hasChildren, final String caption,
			final Class<? extends BasePresenter<?, ? extends EventBus>> presenterType) {
		final MenuEntry entry = new MenuEntry(caption, presenterType);
		tree.addItem(entry);
		tree.setParent(entry, parent);
		tree.setChildrenAllowed(entry, hasChildren);
		return entry;
	}

	public void onSelectMenu(final ValueChangeEvent event) {
		// get the selected menu entry and initiate another event
		final MenuEntry menuEntry = (MenuEntry) this.view.getTree().getValue();
		if (menuEntry != null) {
			this.eventBus.openModule(menuEntry.getPresenterType());
		}
	}
}
