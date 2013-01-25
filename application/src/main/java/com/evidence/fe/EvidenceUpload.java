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
package com.evidence.fe;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.vaadin.mvp.uibinder.IUiMessageSource;

import com.evidence.utility.UIUtils;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class EvidenceUpload extends CustomComponent implements Upload.SucceededListener, Upload.FailedListener,
		Upload.Receiver {

	private static final long serialVersionUID = ApplicationConstants.VERSION;

	private final Panel root; // Root element for contained components.
	private final Panel imagePanel; // Panel that contains the uploaded image.
	private ByteArrayOutputStream baos;
	private final IUiMessageSource messagesource;
	private final Locale locale;
	
	public EvidenceUpload(IUiMessageSource messageSource, Locale locale) {
		this.messagesource = messageSource;
		this.locale = locale;
		
		root = new Panel(getMessage("upload.component.caption"));
		setCompositionRoot(root);
		UIUtils.clearBorder(root);
		((AbstractLayout)root.getContent()).setMargin(false);

		// Create the Upload component.
		final Upload upload = new Upload(getMessage("upload.upload.caption"), this);

		// Use a custom button caption instead of plain "Upload".
		upload.setButtonCaption(getMessage("upload.button.caption"));

		// Listen for events regarding the success of upload.
		upload.addListener((Upload.SucceededListener) this);
		upload.addListener((Upload.FailedListener) this);

		// Create a panel for displaying the uploaded image.
		imagePanel = new Panel(getMessage("upload.image.caption"));
		((AbstractLayout)imagePanel.getContent()).setMargin(false);
		imagePanel.addComponent(new Label(getMessage("upload.image.notloaded")));
		imagePanel.setWidth("200px");
		imagePanel.setHeight("150px");
		root.addComponent(imagePanel);

		root.addComponent(upload);
    }	
	
	/* (non-Javadoc)
	 * @see com.vaadin.ui.Upload.Succee	dedListener#uploadSucceeded(com.vaadin.ui.Upload.SucceededEvent)
	 */
	@Override
	public void uploadSucceeded(SucceededEvent event) {
		// Log the upload on screen.
		root.addComponent(new Label(getMessage("upload.upload.succeeded")));
		// Display the uploaded file in the image panel.
		showImage(baos.toByteArray());
	}

	public void showImage(byte[] byteArray) {
		final Resource imageResource = new StreamResource(new ImageSource(byteArray), "photo.jpg", getApplication());
		imagePanel.removeAllComponents();
		Embedded image = new Embedded("", imageResource);
		image.setWidth("200px");
		imagePanel.addComponent(image);
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Upload.FailedListener#uploadFailed(com.vaadin.ui.Upload.FailedEvent)
	 */
	@Override
	public void uploadFailed(FailedEvent event) {
		// Log the failure on screen.
		root.addComponent(new Label(getMessage("upload.upload.failed")));
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Upload.Receiver#receiveUpload(java.lang.String, java.lang.String)
	 */
	@Override
	public OutputStream receiveUpload(String filename, String mimeType) {
		if (baos != null) {
			baos.reset();
		} else {
			baos = new ByteArrayOutputStream(100000);
		}
		return baos;
	}
	
	private String getMessage(String key) {
		return this.messagesource.getMessage(key, this.locale);
	}
	
	public byte[] getImage() {
		return baos.toByteArray();
	}
}
