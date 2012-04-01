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

import java.io.File;
import java.sql.Connection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.impl.SessionImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/transaction.xml")
public class DbUnitDaoTest {

	@PersistenceContext(name = "entityManagerFactory")
	private EntityManager entityManager;

	@Before
	public void init() throws Exception {
		// insert data into database
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet());
	}

	@After
	public void after() throws Exception {
		// delete data from database
		SessionImpl session = (SessionImpl) entityManager.getDelegate();
		Connection con = session.connection();
		con.createStatement().execute("SET REFERENTIAL_INTEGRITY FALSE;");
		DatabaseOperation.DELETE_ALL.execute(getConnection(), getDataSet());
		con.createStatement().execute("SET REFERENTIAL_INTEGRITY TRUE;");
	}

	private IDatabaseConnection getConnection() throws Exception {
		// get connection
		SessionImpl session = (SessionImpl) entityManager.getDelegate();
		Connection con = session.connection();
		//DatabaseMetaData databaseMetaData = con.getMetaData();
		IDatabaseConnection connection = new DatabaseConnection(con);
		DatabaseConfig config = connection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
		return connection;
	}

	private IDataSet getDataSet() throws Exception {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);
		IDataSet dataSet = builder.build(new File("src/test/resources/dataset.xml"));
		return dataSet;
	}
	
	protected EntityManager getEntityManager() {
		return this.entityManager;
	}
}
