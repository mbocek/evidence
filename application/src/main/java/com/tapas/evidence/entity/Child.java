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
package com.tapas.evidence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.tapas.evidence.fe.ApplicationConstants;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Entity
@ToString
@NoArgsConstructor
@Table(name = "CHILD")
@NamedQueries(value = { 
		@NamedQuery(name = Child.QUERY_NAME_FIND_ALL_BY_DELETED_FLAG, query = Child.QUERY_FIND_ALL_BY_DELETED_FLAG),  
		@NamedQuery(name = Child.QUERY_NAME_FIND_BY_KINDERGARTEN_ID_AND_DELETED_FLAG, query = Child.QUERY_FIND_BY_KINDERGARTEN_ID_AND_DELETED_FLAG) 
	})
public class Child extends Person {

	private static final long serialVersionUID = ApplicationConstants.VERSION;

	public static final String QUERY_NAME_FIND_ALL_BY_DELETED_FLAG = "Child.finaAllByDeletedFlag";   
	public static final String QUERY_FIND_ALL_BY_DELETED_FLAG = "SELECT c FROM Child c WHERE c.deleted = :deleted AND c.tenant.id = :tenantId";

	public static final String QUERY_NAME_FIND_BY_KINDERGARTEN_ID_AND_DELETED_FLAG = "Child.finaByKindergartenIdAndDeltedFlag";
	public static final String QUERY_FIND_BY_KINDERGARTEN_ID_AND_DELETED_FLAG = "SELECT c FROM Child c " +
			"WHERE c.kindergarten.id = :kindergartenId AND c.deleted = :deleted AND c.tenant.id = :tenantId";

	@Getter
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Getter @Setter
	@OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "MOTHER_ID")
	private ResponsiblePerson mother;
	
	@Getter @Setter
	@OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "FATHER_ID")
	private ResponsiblePerson father;
	
	@Getter @Setter
	@OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumn(name = "RESPONSIBLE_PERSON_ID")
	private ResponsiblePerson responsiblePerson;
	
	@Getter @Setter
	@Lob
	@Column(name = "PHOTO", length = 102400)
	private byte[] photo;
}
