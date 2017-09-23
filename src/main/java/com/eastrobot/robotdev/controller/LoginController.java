/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.robotdev.controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletResponse;

import ch.ethz.ssh2.Connection;
import com.eastrobot.robotdev.entity.ConnectionInfo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("login")
@Controller
public class LoginController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(LoginController.class);
	public static Map<String,ConnectionInfo> map = new ConcurrentHashMap<String, ConnectionInfo>();


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

	@RequestMapping(value = "authenticate")
	public void authenticate(ConnectionInfo connectionInfo, HttpServletResponse response){
		JSONObject jsonObject = new JSONObject();
		Connection connection = new Connection(connectionInfo.getHostname(),connectionInfo.getPort());
		try {
			connection.connect();
			boolean authenticateWithPassword = connection.authenticateWithPassword(connectionInfo.getUsername(), connectionInfo.getPassword());
			if (!authenticateWithPassword) {
				connection.close();
				jsonObject.put("success",false);
				throw new RuntimeException("Authentication failed. Please check hostName, userName and passwd");
			} else {
				long id = System.currentTimeMillis();
				map.put(String.valueOf(id),connectionInfo);

				jsonObject.put("success",true);
				jsonObject.put("sessionId",id);
			}


		} catch (IOException e) {
			e.printStackTrace();
			jsonObject.put("success",false);
		}

		writeJson(response,jsonObject);
	}

	@RequestMapping(value = "ssh")
	public String ssh(String sessionId,Model model){
		model.addAttribute("sessionId",sessionId);
		return "/ssh";
	}

	@RequestMapping(value = "tree")
	public void tree(){

	}
}
