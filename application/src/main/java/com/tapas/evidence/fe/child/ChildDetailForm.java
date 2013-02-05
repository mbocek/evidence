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
package com.tapas.evidence.fe.child;

import lombok.extern.slf4j.Slf4j;

import com.tapas.evidence.fe.ApplicationConstants;
import com.tapas.evidence.fe.components.EvidenceUpload;
import com.tapas.evidence.fe.form.EvidenceForm;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
public class ChildDetailForm extends EvidenceForm {

	private static final long serialVersionUID = ApplicationConstants.VERSION;
	
	private final GridLayout layout;
	
	private final VerticalLayout vLayout;
	
	public ChildDetailForm() {
		layout = new GridLayout(2, 4);
		vLayout = new VerticalLayout();
		vLayout.setSpacing(true);
        //layout.setMargin(false, false, false, false);
        layout.setSpacing(true);
        layout.addComponent(vLayout, 1, 0);
        
		setLayout(layout);
	}
	
	@Override
	protected void attachField(Object propertyId, Field field) {
		log.debug("Adding component: {}", propertyId);
		if (propertyId.equals("name")) {
			 vLayout.addComponent(field);
		} else if (propertyId.equals("surName")) {
			vLayout.addComponent(field);
		} else if (propertyId.equals("birthDate")) {
			vLayout.addComponent(field);
		} else if (propertyId.equals("motherId")) {
			layout.addComponent(field, 0, 1, 1, 1);
		} else if (propertyId.equals("fatherId")) {
			layout.addComponent(field, 0, 2, 1, 2);
		} else if (propertyId.equals("responsiblePersonId")) {
			layout.addComponent(field, 0, 3, 1, 3);
		}
	}
	
	public void addUpload(EvidenceUpload upload) {
		 layout.addComponent(upload, 0, 0);
	}
}
