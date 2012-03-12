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
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michal Bocek
 * @since 1.0.0
 * @param <T> class for persistence support
 * @param <Id> primary key of entity
 */
public abstract class JpaDAO<T, Id extends Serializable> {
	
	private static final Logger log = LoggerFactory.getLogger(JpaDAO.class);
	protected Class<T> entityClass;

	@PersistenceContext(name = "entityManagerFactory")
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public JpaDAO() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
	}

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

	public T findById(Id id) {
		return this.entityManager.find(entityClass, id);
	}

	public T read(Id id) {
		if (log.isTraceEnabled()) {
			log.trace("Reading entity for id: " + id);
		}
		T entry = this.entityManager.find(this.entityClass, id);
		if (entry == null) {
			throw new EntityNotFoundException("Entity for class " + this.entityClass + " with id " + id
					+ " can not be found!");
		}
		return entry;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		if (log.isTraceEnabled()) {
			log.trace("Reading all entities");
		}
		Query query = this.entityManager.createQuery("from " + this.entityClass.getName());
		return query.getResultList();
	}
}
