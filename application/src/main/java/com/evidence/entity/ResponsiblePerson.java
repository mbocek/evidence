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
package com.evidence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Entity
@Table(name = "RESPONSIBLE_PERSON")
@ToString
@NamedQueries(value = { 
	@NamedQuery(name = ResponsiblePerson.QUERY_NAME_FIND_ALL_BY_DELETED_FLAG, query = ResponsiblePerson.QUERY_FIND_ALL_BY_DELETED_FLAG), 
	@NamedQuery(name = ResponsiblePerson.QUERY_NAME_FIND_BY_KINDERGARTEN_ID_AND_DELETED_FLAG, query = ResponsiblePerson.QUERY_FIND_BY_KINDERGARTEN_ID_AND_DELETED_FLAG) 
})
public class ResponsiblePerson extends Person {

	private static final long serialVersionUID = 1L;

	public static final String QUERY_NAME_FIND_ALL_BY_DELETED_FLAG = "ResponsiblePerson.finaAllByDeletedFlag";   
	public static final String QUERY_FIND_ALL_BY_DELETED_FLAG = "SELECT r FROM ResponsiblePerson r WHERE r.deleted = :deleted";

	public static final String QUERY_NAME_FIND_BY_KINDERGARTEN_ID_AND_DELETED_FLAG = "ResponsiblePerson.finaAllByKindergartenIdAndDeletedFlag";
	public static final String QUERY_FIND_BY_KINDERGARTEN_ID_AND_DELETED_FLAG = "SELECT r FROM ResponsiblePerson r WHERE r.kindergarten.id = :kindergartenId AND r.deleted = :deleted";
	
	@Getter
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Getter @Setter
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH }, optional = false)
	@JoinColumn(name = "CONTACT_ID")
	private Contact contact;
	
	@Getter @Setter
	@Enumerated(value = EnumType.STRING)
	private ResponsibilityType type;
}
