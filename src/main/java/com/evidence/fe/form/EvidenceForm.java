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
package com.evidence.fe.form;

import com.evidence.fe.annotation.MetaModel;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class EvidenceForm extends Form {

	private static final long serialVersionUID = 1L;
	
	public void addFooter(Component component) {
		this.getFooter().addComponent(component);
	}

	public void setItemDataSource(Model model, MetaModel metaModel) {
		Class<? extends Model> clazz = model.getClass();
		BeanItem beanItem = new BeanItem(model);
		this.setItemDataSource(beanItem);
		this.setVisibleItemProperties(metaModel.getOrderedFields());
	}
}
