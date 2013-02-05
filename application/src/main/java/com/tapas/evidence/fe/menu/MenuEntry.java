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
package com.tapas.evidence.fe.menu;

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.presenter.BasePresenter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class MenuEntry {

	private final String caption;
	private final Class<? extends BasePresenter<?, ? extends EventBus>> presenterType;

	public MenuEntry(final String caption, final Class<? extends BasePresenter<?, ? extends EventBus>> presenterType) {
		this.caption = caption;
		this.presenterType = presenterType;
	}

	public String getCaption() {
		return caption;
	}

	public Class<? extends BasePresenter<?, ? extends EventBus>> getPresenterType() {
		return presenterType;
	}

	@Override
	public String toString() {
		return getCaption();
	}
}
