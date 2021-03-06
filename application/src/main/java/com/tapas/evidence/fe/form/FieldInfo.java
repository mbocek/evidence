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

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString
@AllArgsConstructor
public class FieldInfo {
	
	@Getter
	private String fieldName;
	
	@Getter
	private String fieldNestedName;

	@Getter
	private Long order;
	
	@Getter
	private String caption;
	
	@Getter
	private Boolean required;

	@Getter
	private Boolean validated;

	@Getter
	private Boolean nestedField;

	@Getter
	private List<FieldInfo> subFieldInfo; 
	
	public boolean isNested() {
		return !this.getFieldName().equals(this.getFieldNestedName());
	}
}
