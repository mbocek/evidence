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
package com.evidence.fe.annotation;

import java.util.Comparator;
import java.util.Map;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ValueComparator implements Comparator<String> {

	private Map<String, Double> base;

	public ValueComparator(Map<String, Double> base) {
		this.base = base;
	}

	@Override
	public int compare(String key1, String key2) {
		if (base.get(key1) < base.get(key2)) {
			return -1;
		} else if (base.get(key1).equals(base.get(key2))) {
			return 0;
		} else {
			return 1;
		}
	}
}
