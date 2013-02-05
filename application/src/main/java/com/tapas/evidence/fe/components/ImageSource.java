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
package com.tapas.evidence.fe.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.tapas.evidence.fe.ApplicationConstants;
import com.vaadin.terminal.StreamResource.StreamSource;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class ImageSource implements StreamSource {

	private static final long serialVersionUID = ApplicationConstants.VERSION;
	
	private byte[] imagebuffer = null;
	 
	 
	 public ImageSource(final ByteArrayOutputStream baos) {
		this.imagebuffer = baos.toByteArray();
	}
	
	 public ImageSource(final byte[] byteArray) {
		this.imagebuffer = byteArray;
	}
	
	/* (non-Javadoc)
	 * @see com.vaadin.terminal.StreamResource.StreamSource#getStream()
	 */
	@Override
	public InputStream getStream() {
		 return new ByteArrayInputStream(this.imagebuffer);
	}
}
