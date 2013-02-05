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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

import org.vaadin.mvp.uibinder.IUiMessageSource;

import com.tapas.evidence.fe.ApplicationConstants;
import com.tapas.evidence.utility.UIUtils;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.SucceededEvent;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
public class EvidenceUpload extends CustomComponent implements Upload.SucceededListener, Upload.FailedListener,
		Upload.Receiver, Upload.StartedListener {

	private static final int MAX_FILE_SIZE = 100000;

	private static final long serialVersionUID = ApplicationConstants.VERSION;

	private static final String MIME_TYPE_JPEG = "image/jpeg"; 
	
	private final Panel root; // Root element for contained components.
	private final Panel imagePanel; // Panel that contains the uploaded image.
	private final Upload upload;
	private final Label status;
	private String errorMessage;
	private ByteArrayOutputStream baos;
	private final IUiMessageSource messagesource;
	private final Locale locale;
	
	
	public EvidenceUpload(final IUiMessageSource messageSource, final Locale locale) {
		this.messagesource = messageSource;
		this.locale = locale;
		
		this.setSizeUndefined();
		
		root = new Panel(getMessage("upload.component.caption"));
		setCompositionRoot(root);
		UIUtils.clearBorder(root);
		((AbstractLayout)root.getContent()).setMargin(false);

		// Create the Upload component.
		upload = new Upload(getMessage("upload.upload.caption"), this);
		upload.setImmediate(true);

		// Use a custom button caption instead of plain "Upload".
		upload.setButtonCaption(getMessage("upload.button.caption"));

		// Listen for events regarding the success of upload.
		upload.addListener((Upload.SucceededListener) this);
		upload.addListener((Upload.FailedListener) this);
		upload.addListener((Upload.StartedListener) this);

		// Create a panel for displaying the uploaded image.
		imagePanel = new Panel(getMessage("upload.image.caption"));
		((AbstractLayout)imagePanel.getContent()).setMargin(false);
		status = new Label(getMessage("upload.image.notloaded"));
		imagePanel.setWidth("200px");
		imagePanel.setHeight("150px");
		root.addComponent(imagePanel);
		root.addComponent(status);

		root.addComponent(upload);
    }	
	
	/* (non-Javadoc)
	 * @see com.vaadin.ui.Upload.Succee	dedListener#uploadSucceeded(com.vaadin.ui.Upload.SucceededEvent)
	 */
	@Override
	public void uploadSucceeded(final SucceededEvent event) {
		// Log the upload on screen.
		errorMessage = "";
		status.setValue(getMessage("upload.upload.succeeded"));
		root.addComponent(status);
		// Display the uploaded file in the image panel.
		showImage(baos.toByteArray());
	}

	public void showImage(String path) {
		final Resource imageResource = new ExternalResource(path);
		Embedded image = new Embedded("", imageResource);
		image.setCaption(null);
		image.setWidth("200px");
		imagePanel.setHeight(null);
		imagePanel.removeAllComponents();
		imagePanel.addComponent(image);
		status.setValue(null);
	}

	public void showImage(final byte[] byteArray) {
		final StreamResource imageResource = new StreamResource(new ImageSource(byteArray), "photo.jpg", getApplication());
		imageResource.setCacheTime(0); // disable cache
		Embedded image = new Embedded("", imageResource);
		image.setCaption(null);
		image.setWidth("200px");
		imagePanel.setHeight(null);
		imagePanel.addComponent(image);
		status.setValue(null);
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Upload.FailedListener#uploadFailed(com.vaadin.ui.Upload.FailedEvent)
	 */
	@Override
	public void uploadFailed(final FailedEvent event) {
		// Log the failure on screen.
		status.setValue(errorMessage + " " + getMessage("upload.upload.failed"));
		root.addComponent(status);
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Upload.Receiver#receiveUpload(java.lang.String, java.lang.String)
	 */
	@Override
	public OutputStream receiveUpload(final String filename, final String mimeType) {
		if (baos != null) {
			baos.reset();
		} else {
			baos = new ByteArrayOutputStream(100000);
		}
		return baos;
	}
	
	private String getMessage(final String key) {
		return this.messagesource.getMessage(key, this.locale);
	}
	
	public byte[] getImage() {
		return baos.toByteArray();
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Upload.StartedListener#uploadStarted(com.vaadin.ui.Upload.StartedEvent)
	 */
	@Override
	public void uploadStarted(final StartedEvent event) {
		if (event.getMIMEType().compareTo(MIME_TYPE_JPEG) != 0) {
			log.debug("Wrong mime type. ({})", event.getMIMEType());
			errorMessage = getMessage("upload.upload.notjpeg");
			upload.interruptUpload();
		}
		if (event.getContentLength() > MAX_FILE_SIZE) {
			log.debug("Too big file for upload. ({})", event.getContentLength());
			errorMessage = getMessage("upload.upload.filesizeexceeded");
			upload.interruptUpload();
		}
	}
}
