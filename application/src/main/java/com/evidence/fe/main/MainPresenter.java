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
package com.evidence.fe.main;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;
import org.vaadin.mvp.presenter.FactoryPresenter;
import org.vaadin.mvp.presenter.IPresenter;
import org.vaadin.mvp.presenter.IPresenterFactory;
import org.vaadin.mvp.presenter.annotation.Presenter;

import com.evidence.dto.KindergartenDTO;
import com.evidence.fe.ApplicationConstants;
import com.evidence.fe.EvidenceApplication;
import com.evidence.fe.menu.MenuPresenter;
import com.evidence.service.KindergartenService;
import com.vaadin.ui.Field.ValueChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Scope("prototype")
@Component("mainPresenter")
@Presenter(view = MainView.class)
public class MainPresenter extends FactoryPresenter<IMainView, MainEventBus> {

	private static final Logger log = LoggerFactory.getLogger(MainPresenter.class);

	private EvidenceApplication application;

	private MenuPresenter menuPresenter;

	private IPresenter<?, ? extends EventBus> contentPresenter;
	
	@Inject
	private KindergartenService kindergartenService;

	@Override
	public void bind() {
		final VerticalLayout mainLayout = this.view.getMainLayout();
		final HorizontalSplitPanel layoutPanel = this.view.getSplitLayout();
		mainLayout.setExpandRatio(layoutPanel, 1.0f);
		layoutPanel.setSplitPosition(25, HorizontalSplitPanel.UNITS_PERCENTAGE);

		componentLocation();
	}
	
	
	public void onStart(final EvidenceApplication app) {
		// keep a reference to the application instance
		this.application = app;

		// set the applications main windows (the view)
		this.application.setMainWindow((Window) this.view);

		populateKindergartenSelect();
		
		// load the menu presenter
		this.menuPresenter = (MenuPresenter) this.application.getPresenterFactory().createPresenter("menuPresenter");
		this.view.setMenu(this.menuPresenter.getView());
	}

	private void componentLocation() {
		final HorizontalLayout buttonBar = this.view.getButtonBar();
		buttonBar.setExpandRatio(this.getView().getExpander(), 0.5f);
		buttonBar.setExpandRatio(this.getView().getTitle(), 0.5f);
		buttonBar.setMargin(true);
	}

	private void populateKindergartenSelect() {
		Select kindergartenSelect = this.getView().getKindergarten();
		kindergartenSelect.addItem(ApplicationConstants.SELECT_ALL);
		kindergartenSelect.setItemCaption(ApplicationConstants.SELECT_ALL,
				this.getMessage("kindergarten.select.all", this.getLocale()));
		kindergartenSelect.select(ApplicationConstants.SELECT_ALL);

		for (KindergartenDTO kindergarten : kindergartenService.getAll()) {
			kindergartenSelect.addItem(kindergarten.getId());
			kindergartenSelect.setItemCaption(kindergarten.getId(), kindergarten.getName());
		}
	}

	public void onOpenModule(final Class<? extends BasePresenter<?, ? extends EventBus>> presenter) {
		// opening module invoked from menu 
		log.debug("Openning module for presenter {}", presenter.getCanonicalName());
		final IPresenterFactory pf = this.application.getPresenterFactory();
		this.contentPresenter = pf.createPresenter(presenter.getAnnotation(Component.class).value());
		this.view.setContent((com.vaadin.ui.Component) this.contentPresenter.getView());
	}

	public void onShowDialog(final Window dialog) {
		this.application.getMainWindow().addWindow(dialog);
	}

	public void onClose() {
		log.info("Closing application!");
		this.application.close();
	}
	
	public void onSelectKindergarten(ValueChangeEvent event) {
		this.application.setKindergartenId(Long.valueOf(event.getProperty().getValue().toString()));
	}
}