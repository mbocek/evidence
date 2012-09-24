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

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;

import com.evidence.fe.main.MainPresenter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Window;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public interface ResponsibleEventBus extends EventBus {

	@Event(handlers = { ResponsiblePresenter.class })
	void addResponsible();

	@Event(handlers = { ResponsiblePresenter.class })
	void removeResponsible();

	@Event(handlers = { MainPresenter.class })
	void showDialog(Window dialog);

	@Event(handlers = { ResponsiblePresenter.class })
	void saveResponsible();

	@Event(handlers = { ResponsiblePresenter.class })
	void cancelEditResponsible();
	
	@Event(handlers = { ResponsiblePresenter.class })
	void editResponsible(ItemClickEvent event);
}
