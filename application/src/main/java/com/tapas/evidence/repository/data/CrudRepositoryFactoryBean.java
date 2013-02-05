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

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * Factory bean for prepare CRUD repository and ReadOnly repository.
 * @author Michal Bocek
 * @since 1.0.0
 */
public class CrudRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends
		JpaRepositoryFactoryBean<R, T, I> {
	
	@SuppressWarnings("rawtypes")
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new CrudRepositoryFactory(entityManager);
	}

	private static class CrudRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {

		private EntityManager entityManager;

		public CrudRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		@SuppressWarnings("unchecked")
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			if (ReadOnlyRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
				return new ReadOnlyRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), entityManager);
			} else if (CrudRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
				return new CrudRepositoryImpl<T, I>((Class<T>) metadata.getDomainType(), entityManager);
			} else {
				return super.getTargetRepository(metadata);
			}
		}

		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			if (ReadOnlyRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
				return ReadOnlyRepository.class;
			} else if (CrudRepository.class.isAssignableFrom(metadata.getRepositoryInterface())) {
				return CrudRepository.class;
			} else {
				return super.getRepositoryBaseClass(metadata);
			}
		}
	}
}
