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
package com.tapas.evidence.repository.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tapas.evidence.credential.EvidenceUserDetails;
import com.tapas.evidence.entity.TenantAware;
import com.tapas.evidence.entity.user.Tenant;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class CrudRepositoryImpl<T, ID extends Serializable> implements CrudRepository<T, ID> {

	private static final Logger log = LoggerFactory.getLogger(CrudRepositoryImpl.class);
	
	private final EntityManager entityManager;

	private final Class<T> entityClass;
	
	public CrudRepositoryImpl(Class<T> entityClass, EntityManager entityManager) {
		this.entityClass = entityClass;
		this.entityManager = entityManager;
	}
	
	@Override
	public void create(T entity) {
		if (log.isTraceEnabled()) {
			log.trace("Creating entity: " + entity.getClass());
			log.trace("  With contents: " + entity); 
		}
		if (entity instanceof TenantAware) {
			Long tenantId = ((EvidenceUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getTenantId();
			Tenant tenant = this.entityManager.find(Tenant.class, tenantId);
			((TenantAware)entity).setTenant(tenant);
		}
		entityManager.persist(entity);
	}

	@Override
	public T read(ID id) {
		if (log.isTraceEnabled()) {
			log.trace("Reading entity for id: " + id);
		}
		final T entry ;
		if (entityClass.isInstance(TenantAware.class)) {
			entry = readTenantAware(id);
		} else {
			entry = this.entityManager.find(this.entityClass, id);
		}	
		if (entry == null) {
			throw new EntityNotFoundException("Entity for class " + this.entityClass + " with id " + id
					+ " can not be found!");
		} 
		return entry;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private T readTenantAware(ID id) {
		final T entry;
		final Long tenantId = ((EvidenceUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getTenantId();
		final Query query = this.entityManager.createQuery("from " + this.entityClass.getName() + " where tenant.id = :tenantId");
		query.setParameter("tenantId", tenantId);
		final List resultList = query.getResultList();
		if (resultList.size() == 1) {
			entry = (T)resultList.get(0);
		} else if (resultList.size() > 1) {
			throw new NonUniqueResultException("Non unique entity for class " + this.entityClass + " with id " + id
				+ "!");
		} else {
			entry = null;
		}
		return entry;
	}

	@Override
	public T update(T entity) {
		if (log.isTraceEnabled()) {
			log.trace("Updating entity: " + entity.getClass());
			log.trace("  With contents: " + entity); 
		}
		return entityManager.merge(entity);
	}

	@Override
	public void delete(T entity) {
		if (log.isTraceEnabled()) {
			log.trace("Deleting entity: " + entity.getClass());
			log.trace("  With contents: " + entity); 
		}
		entityManager.remove(entity);
	}

	@Override
	public T findById(ID id) {
		final T result;
		if (entityClass.isInstance(TenantAware.class)) {
			result = readTenantAware(id);
		} else {
			result = this.entityManager.find(entityClass, id);
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		if (log.isTraceEnabled()) {
			log.trace("Reading all entities");
		}
		final Query query;
		if (entityClass.isInstance(TenantAware.class)) {
			final Long tenantId = ((EvidenceUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getTenantId();
			final String queryString = "from " + this.entityClass.getName() + "where tenant.id = :tenantId";
			query = this.entityManager.createQuery(queryString);
			query.setParameter("tenantId", tenantId);
		} else {
			final String queryString = "from " + this.entityClass.getName();
			query = this.entityManager.createQuery(queryString);
		}
		return query.getResultList();
	}

}
