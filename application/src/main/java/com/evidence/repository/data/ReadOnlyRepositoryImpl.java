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
package com.evidence.repository.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Repository
public class ReadOnlyRepositoryImpl<T, ID extends Serializable> implements ReadOnlyRepository<T, ID> {

	private static final Logger log = LoggerFactory.getLogger(ReadOnlyRepositoryImpl.class);
	
	private EntityManager entityManager;

	private final Class<T> entityClass;
	
	public ReadOnlyRepositoryImpl(Class<T> entityClass, EntityManager entityManager) {
		this.entityClass = entityClass;
		this.entityManager = entityManager;
	}
	
	@Override
	public T read(ID id) {
		if (log.isTraceEnabled()) {
			log.trace("Reading entity for id: " + id);
		}
		final T entry = this.entityManager.find(this.entityClass, id);
		if (entry == null) {
			throw new EntityNotFoundException("Entity for class " + this.entityClass + " with id " + id
					+ " can not be found!");
		}
		return entry;
	}

	@Override
	public T findById(ID id) {
		return this.entityManager.find(entityClass, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		if (log.isTraceEnabled()) {
			log.trace("Reading all entities");
		}
		final Query query = this.entityManager.createQuery("from " + this.entityClass.getName());
		return query.getResultList();
	}

}
