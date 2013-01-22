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
package com.evidence.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.security.core.context.SecurityContextHolder;

import com.evidence.credential.EvidenceUserDetails;
import com.evidence.entity.Kindergarten;

/**
 * Implementation for custom repository.
 * @author Michal Bocek
 * @since 1.0.0
 */
public class KindergartenRepositoryImpl implements KindergartenRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/* (non-Javadoc)
	 * @see com.evidence.repository.KindergartenRepository#findAll(boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Kindergarten> findAll() {
		final Long tenantId = ((EvidenceUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getTenantId();
		Query query = this.entityManager.createNamedQuery(Kindergarten.QUERY_NAME_FIND_ALL_BY_DELETED_FLAG);
		query.setParameter("deleted", Boolean.FALSE);
		query.setParameter("tenantId", tenantId);
        return query.getResultList();
	}
}
