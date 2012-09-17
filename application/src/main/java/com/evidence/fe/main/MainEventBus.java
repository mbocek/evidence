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

import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.eventbus.annotation.Event;
import org.vaadin.mvp.presenter.BasePresenter;

import com.evidence.fe.EvidenceApplication;
import com.evidence.fe.menu.MenuPresenter;
import com.evidence.fe.teacher.TeacherPresenter;
import com.vaadin.ui.Field.ValueChangeEvent;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public interface MainEventBus extends EventBus {

	@Event(handlers = { MainPresenter.class })
	void start(EvidenceApplication app);

	@Event(handlers = { MenuPresenter.class })
	void selectMenu(ValueChangeEvent event);

	@Event(handlers = { MainPresenter.class })
	void openModule(Class<? extends BasePresenter<?, ? extends EventBus>> presenterClass);	

	@Event(handlers = { MainPresenter.class })
	void close();
	
	@Event(handlers = { MainPresenter.class, TeacherPresenter.class })
	void selectKindergarten(ValueChangeEvent event);	
}
