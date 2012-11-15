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

import org.springframework.data.jpa.repository.Query;

import com.evidence.entity.Teacher;
import com.evidence.repository.data.CrudRepository;

/**
 * Repository for access on teacher.
 * @author Michal Bocek
 * @since 1.0.0
 */
public interface TeacherRepository extends CrudRepository<Teacher, Long> {
	
	/**
	 * Find all teachers based on deleted flag.
	 * @param deleted
	 * @return
	 */
	@Query("select t from Teacher t where t.deleted = ?1")
	List<Teacher> findAll(final boolean deleted);
	
	/**
	 * Find teachers for specified kindergarten.
	 * @param id
	 * @return
	 */
	List<Teacher> findByKindergartenId(final Long id);
}