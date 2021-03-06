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
package com.tapas.evidence.fe.form;

import java.util.Locale;

import org.vaadin.mvp.uibinder.IUiMessageSource;

import com.vaadin.ui.DefaultFieldFactory;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class EvidenceFormFieldFactory extends DefaultFieldFactory {

	private static final long serialVersionUID = 1L;

	private transient IUiMessageSource messageSource;
	
	private final Locale locale;

	public EvidenceFormFieldFactory(final IUiMessageSource messageSource, final Locale locale) {
		super();
		this.messageSource = messageSource;
		this.locale = locale;
	}
	
	protected Locale getLocale() {
		return this.locale;
	}
	
	protected IUiMessageSource getMessageSource() {
		return this.messageSource;
	}

	protected String getMessage(final String key) {
		return this.messageSource.getMessage(key, this.getLocale());
	}
}
