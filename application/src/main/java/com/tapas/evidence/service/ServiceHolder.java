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
package com.tapas.evidence.service;

import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
public final class ServiceHolder {

	private static ServiceHolder instance;
	
	@Getter
	@Inject 
	private CodeListService codeListService;

	@Getter
	@Inject 
	private KindergartenService kindergartenService;
	
	@Getter
	@Inject 
	private ResponsiblePersonService responsibleService;
	
	private ServiceHolder() {
	}

	public static synchronized ServiceHolder getInstance() { // NOPMD
		if (instance == null) {
			log.debug("Cearing ServiceHolder instance");
			instance = new ServiceHolder();
		} else {
			log.trace("ServiceHolder instance laready exists!");
		}
		return instance;
	}
}
