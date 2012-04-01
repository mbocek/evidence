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
package com.evidence.dao;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michal Bocek
 * @since 1.0.0
 * @param <T> class for persistence support
 * @param <Id> primary key of entity
 */
public abstract class JpaDAO<T, Id extends Serializable> extends JpaImmutableDAO<T, Id> {
	
	private static final Logger log = LoggerFactory.getLogger(JpaDAO.class);

	public void create(T entity) {
		if (log.isTraceEnabled()) {
			log.trace("Creating entity: " + entity.getClass());
			log.trace("  With contents: " + String.valueOf(entity));
		}
		this.entityManager.persist(entity);
	}

	public void delete(T entity) {
		if (log.isTraceEnabled()) {
			log.trace("Deleting entity: " + entity.getClass());
			log.trace("  With contents: " + String.valueOf(entity));
		}
		this.entityManager.remove(entity);
	}

	public T update(T entity) {
		if (log.isTraceEnabled()) {
			log.trace("Updating entity: " + entity.getClass());
			log.trace("  With contents: " + String.valueOf(entity));
		}
		return this.entityManager.merge(entity);
	}
}
