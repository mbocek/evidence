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
package com.tapas.evidence.fe.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.multitype.MultiTypeCaptchaService;
import com.tapas.evidence.dto.UserDTO;
import com.tapas.evidence.service.TenantAlreadyExists;
import com.tapas.evidence.service.UserAlreadyExists;
import com.tapas.evidence.service.UserService;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Controller
@RequestMapping(value="/users")
public class UserController {

    @Inject
    private Validator validator;

    @Inject
    private UserService userService;

	@Inject
	private MultiTypeCaptchaService captchaService;
    
    @RequestMapping(value="new", method = RequestMethod.GET)
    public ModelAndView getNewUser() {
    	UserDTO userDTO = new UserDTO();
    	userDTO.setTenantList(userService.getTenantList());
    	ModelAndView modelAndView = new ModelAndView("register", "user", userDTO);
    	return modelAndView;
     }

    @RequestMapping(value = "new", method = RequestMethod.POST)
    public String handleRegister(@RequestParam("passwordConfirmation") String passwordConfirmation,
            @Valid @ModelAttribute("user") UserDTO user, 
            BindingResult results, SessionStatus status, HttpServletRequest request) {
    	log.info("Trying to register user: {}", user);
    	user.setTenantList(userService.getTenantList());
    	String result;
		
    	if (user.getPassword().compareTo(passwordConfirmation) != 0) {
			results.rejectValue("password", "user.password.notsame", "Password and confirmation password are not same");
		}
		
		validateCaptcha(user.getCaptcha(), results, request.getSession().getId(), "screen.register.captcha.error");
		
		if (!results.hasErrors()) {
			try {
				userService.create(user);
				result = "redirect:/app";
			} catch (TenantAlreadyExists e) {
				log.info("Tenant with name: {} already exists!", user.getTenantName());
				results.rejectValue("tenantName", "tenant.alreadyExists", "Tenant already exists");
				result = "register";
			} catch (UserAlreadyExists e) {
				log.info("User with name: {} already exists!", user.getUserName());
				results.rejectValue("userName", "user.alreadyExists");
				result = "register";
			}
		} else {
			result = "register";
		}
		return result;
    }
    
	private void validateCaptcha(String captcha, BindingResult result, String sessionId, String errorCode) {
		// If the captcha field is already rejected
		if (null != result.getFieldError("captcha")) {
			return;
		}
		
		boolean validCaptcha = false;
		try {
			validCaptcha = captchaService.validateResponseForID(sessionId, captcha);
		} catch (CaptchaServiceException e) {
			// should not happen, may be thrown if the id is not valid
			log.warn("Catcha validation problem:", e);
		}
		if (!validCaptcha) {
			result.rejectValue("captcha", errorCode);
		}
	}    
}
