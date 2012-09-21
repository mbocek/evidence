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

import java.util.List;

import javax.persistence.Query;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import com.evidence.entity.Kindergarten;


/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Repository("kinderGartenDAO")
public class KindergartenDAO extends JpaDAO<Kindergarten, Long> {
	
	/**
	 * Find all kindergartens. Query is based on delete flag.
	 * @param deleted when true return also deleted data.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Kindergarten> findAll(final boolean deleted) {
		if (log.isTraceEnabled()) {
			log.trace("Reading all kindergartens with deleted flag:{}", deleted);
		}
		
		final Query query = this.getEntityManager().createNamedQuery(Kindergarten.QUERY_NAME_FIND_ALL_BY_DELETED_FLAG).
				setParameter("deleted", deleted);
		return query.getResultList();
	}
}