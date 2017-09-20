/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.robotdev.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("login")
@Controller
public class LoginController {
	
	@RequestMapping()
	public String index(HttpServletResponse response){
		Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userDetails instanceof String){
			//anonymousUser
			return "app/login/login";
		}else{
			try {
				response.sendRedirect("home.do");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
