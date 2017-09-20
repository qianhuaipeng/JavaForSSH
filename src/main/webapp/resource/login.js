 (function($){
			'use strict';
			$.myApp = $.myApp || {};
			$.extend($.myApp, {
				contextPath: '${pageContext.request.contextPath}'
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
								if (_url.indexOf($.myApp.ctx)==-1) _url = $.myApp.ctx + _url;
								alert(_url);
								location.href =  _url;
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