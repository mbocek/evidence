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

import org.springframework.data.repository.Repository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@NoRepositoryBean
public interface CrudRepository<T, ID extends Serializable> extends Repository<T, ID> {

	/**
	 * Create entity.
	 * @param entity
	 */
	void create(final T entity);
	
	/**
	 * Read entity by id. When entity doesn't exists throw EntityNotFoundException.
	 * @param id entity id
	 * @return entity
	 */
	T read(final ID id);
	 
	/**
	 * Update specified entity.
	 * @param entity
	 * @return
	 */
	T update(final T entity);

	/**
	 * Delete specified entity.
	 * @param entity
	 */
	void delete(final T entity);

	/**
	 * Find entity by id. When entity doesn't exist return null.
	 * @param id entity id
	 * @return entity or null
	 */
	T findById(final ID id);
	
	/**
	 * Find all entities.
	 * @return
	 */
	List<T> findAll();
}
