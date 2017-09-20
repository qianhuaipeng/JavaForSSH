<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'login.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="{pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script type="text/javascript">
        (function($){
        	'user strict';
        	
        	$.myApp = $.myApp || {};
        	$.extend($.myApp,{
        		ctx: '/robot-dev'
        	});
        })(jQuery);
		
		$(function(){
					$('#btnOk').click(function(){
						var username = $('#j_username').val();
						var password = $('#j_password').val();
						if ($.trim(username)==''){
							alert('请输入账号');
							return false;
						}
						if ($.trim(password)==''){
							alert('请输入密码');
							return false;
						}
						var param = {
							j_username: $('#j_username').val(),
							j_password: $('#j_password').val()
						}
						
						
						$.post('spring_security_login', param, function(data){
							if (data.type==1){
								var _url = data.targetUrl;
								//if (_url.indexOf($.kbase.ctx)==-1) _url = $.kbase.ctx + _url;
								alert($.myApp.ctx + _url);
								location.href = $.myApp.ctx + _url;
							}else if (data.result==0){
								$.tips({
									content: '账号或密码错误，请重新输入',
									follow: '#j_password'
								});
								loadEnd();
								return false;
							}else if (data.result==-1){
								$.tips({
									content: '账号或密码错误，请重新输入',
									follow: '#j_password'
								});
								return false;
							}
					}, 'json');
			});
		});
	</script>
  </head>
  
  <body>
  	<div class="box_bg bradius pt20 pl20 pb20">
		<p class="bgfff w350 mb20 input pst">
			<i class="dspib vta icon-user"></i> 
			<input type="text" id="j_username" class="w350" placeholder="用户名" maxlength="12">
		</p>
		<p class="bgfff w350 mb20 input pst">
			<i class="dspib vta icon-password"></i> 
			<input type="password" id="j_password" class="w350" placeholder="密码">
		</p>
		<a id="btnOk" href="javascript:void(0);" class="font18 btn-lg btnblue w350">立即登录</a>
	</div>
  </body>
</html>
