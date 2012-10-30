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

import lombok.Getter;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ResponsiblePersonListView extends VerticalLayout implements IResponsiblePersonListView, IUiBindable {

	private static final long serialVersionUID = 3807396569678708848L;

	@UiField
	@Getter
	private Table responsiblePersonList;

	@UiField
	@Getter
	private HorizontalLayout buttonBar;

	@UiField
	@Getter
	private Label expander;
}
