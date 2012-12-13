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
package com.evidence.fe.child;

import com.evidence.fe.form.EvidenceForm;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ChildDetailForm extends EvidenceForm {

	private static final long serialVersionUID = 1L;
	
	private final GridLayout layout;
	
	public ChildDetailForm() {
		layout = new GridLayout(2, 5);

        layout.setMargin(false, false, false, false);
        layout.setSpacing(true);
        
		setLayout(layout);
	}
	
	@Override
	protected void attachField(Object propertyId, Field field) {
		if (propertyId.equals("name")) {
			 layout.addComponent(field, 0, 0);
		} else if (propertyId.equals("surName")) {
			 layout.addComponent(field, 1, 0);
		} else if (propertyId.equals("birthDate")) {
			 layout.addComponent(field, 0, 1, 1, 1);
		} else if (propertyId.equals("motherId")) {
			 layout.addComponent(field, 0, 2, 1, 2);
		} else if (propertyId.equals("fatherId")) {
			 layout.addComponent(field, 0, 3, 1, 3);
		} else if (propertyId.equals("responsiblePersonId")) {
			 layout.addComponent(field, 0, 4, 1, 4);
		}
	}
}
