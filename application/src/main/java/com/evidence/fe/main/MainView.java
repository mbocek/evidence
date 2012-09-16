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

import lombok.Getter;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class MainView extends Window implements IMainView, IUiBindable {
	
	private static final long serialVersionUID = 2836668802193079451L;

	@UiField
	@Getter
	private VerticalLayout mainLayout;

	@UiField
	@Getter
	private HorizontalSplitPanel splitLayout;

	@UiField
	@Getter
	private HorizontalLayout buttonBar;

	@UiField
	@Getter
	private Label title;

	@UiField
	@Getter
	private Label expander;

	@UiField
	@Getter
	private Select kindergarten;

	@Override
	public void setMenu(final Component menu) {
		splitLayout.setFirstComponent(menu);
	}

	@Override
	public void setContent(final Component content) {
		splitLayout.setSecondComponent(content);
	}
}
