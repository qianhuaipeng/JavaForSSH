/**
 * 前端用到的公共属性或方法，以及对$的一些扩展
 * @required 
 * 	jquery.js
 * 	jquery.cookie.js
 * 	layer.js
 * @param $
 * @author 
 */
(function($){
	'use strict';
	
	$.app = $.app || {};
	$.extend($.app, {
        ctx: $('meta[name="ctx"]').attr('content'),
		/* 是否支持H5浏览器，如果不支持默认为是IE6～8 */
		supportH5: $.support.leadingWhitespace,
		/**
		 * layer弹窗
		 * 使用方法：
		 * $.app.open({
                    content: $('.dialog'),
                    width: 712,
                    height: 518
                })
		 */
		open: function(opts){
			//TODO 高度设置可以再完善些 
			opts = $.extend({
				type: 1,
				title: false,
				shadeClose: true,
				closeBtn : 0,
				width: (document.body.clientWidth-60),
				height: (document.body.clientHeight-60)
			}, opts);
			if (opts.area){
				//delete opts.width
				//delete opts.height
			}else{
				if (String(opts.width).indexOf('%')==-1){
					opts.width = opts.width + 'px';
				}
				if (String(opts.height).indexOf('%')==-1){
					opts.height = opts.height + 'px';
				}
				opts = $.extend(opts, {
					area: [opts.width, opts.height], //宽高
				});
			}
			delete opts.width
			delete opts.height
			//页面层
			window.__kbs_layer_index = layer.open(opts);
			return window.__kbs_layer_index;
		}
	});
	
	
	
	$.extend($, {
		/**
		 * 低版本浏览器判断
		 */
		isLowerBrowser: function(){
			var agent = navigator.userAgent.toLowerCase();
			if (agent.indexOf('msie 6')!=-1 || agent.indexOf('msie 7')!=-1 || agent.indexOf('msie 8')!=-1){
				return true;
			}
			return false;
		},
		/* 获取页面数据 */
		loadPage: function(url){
			var ind = layer.load();
			var res = $.ajax({url:url, async:false }).responseText;
			layer.close(ind);
			return res;
		}
	});

	$.fn.extend({
		/**
		 * 分页
		 * 使用方法：$('#pagePanel').pager({page: 1}, itemMergeHandler);
		 */
		pager: function(opts, callback){
			var rows = opts.rows?opts.rows:opts.pageSize;
				rows = rows?rows:10;/*每页显示多少条*/
			opts = $.extend({
				page: 1,
				rows: rows,
				useTotal: false
			}, opts);
			var _this = this;
			if (opts.page==1){
				$(_this).css({'text-align': 'center'});
			}
			if (opts.url){
				$(_this).attr('_url', opts.url);
				delete opts['url'];
			}
			var url = $(_this).attr('_url');
			layer.loading();
			$.getJSON(url, $.param(opts,true), function(res){
				layer.loaded();
				//执行回调
				if (typeof(callback)=='function'){
					callback(res);
				}
				/*自动计算页数*/
				if(!res.pages && res.total && rows){/*未明确提供总页数*/
					res.pages = Math.ceil(res.total/rows);
					/*
					//Math.ceil() 向上取整
					//Math.floor() 向下取整
					//Math.round() 四舍五入
					*/
				}
				//显示分页
				laypage({
					cont: _this, //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
					pages: res.pages, //通过后台拿到的总页数
					curr: opts.page || 1, //当前页
					first: 1, //将首页显示为数字1,。若不显示，设置false即可
					last: res.pages, //将尾页显示为总页数。若不显示，设置false即可
					prev: '<', //若不显示，设置false即可
					next: '>',//若不显示，设置false即可
					jump: function(obj, first){ //触发分页后的回调
						if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
							opts = $.extend(true, opts, {page: obj.curr})
							$(_this).pager(opts, callback);
						}
					}
				});

				if (opts.useTotal){
					$(_this).prepend('<div style="position:absolute; margin-left:10px;margin-top:3px;">共 ' + res.total  + ' 条记录</div>');
				}
			});
		},
		/**
		 * 滚动条
		 * 使用方法：$('#treeDemo').scrollbar();
		 * 依赖于 mCustomScroolbar 组件
		 */
		scrollbar: function(){
			var _this = this;
			if (!$.isLowerBrowser()){
				$(_this).mCustomScrollbar({theme: 'minimal-dark'});
			}
		}
	})
})(jQuery);

/** 
 * JQuery扩展方法，用户对JQuery EasyUI的DataGrid控件进行操作。 
 */  
$.fn.extend({  
    /** 
     * 修改DataGrid对象的默认大小，以适应页面宽度。 
     *  
     * @param heightMargin 
     *            高度对页内边距的距离。 
     * @param widthMargin 
     *            宽度对页内边距的距离。 
     * @param minHeight 
     *            最小高度。 
     * @param minWidth 
     *            最小宽度。 
     *  
     */  
    resizeDataGrid : function(heightMargin, widthMargin, minHeight, minWidth) {  
        var height = $(document.body).height() - heightMargin;  
        var width = $(document.body).width() - widthMargin;  
        height = height < minHeight ? minHeight : height;  
        width = width < minWidth ? minWidth : width;  
        //console.info("width: " + width + "  height:" + height);
        $(this).datagrid('resize', {  
            height : height,  
            width : width  
        });  
    }  
});  

var MaskUtil = (function(){  
    
    var $mask,$maskMsg;  
       
    var defMsg = '正在处理，请稍待。。。';  
       
    function init(){  
        if(!$mask){  
            $mask = $("<div class=\"datagrid-mask mymask\"></div>").appendTo("body");  
        }  
        if(!$maskMsg){  
            $maskMsg = $("<div class=\"datagrid-mask-msg mymask\">"+defMsg+"</div>")  
                .appendTo("body").css({'font-size':'12px'});  
        }  
           
        $mask.css({width:"100%",height:$(document).height()});  
           
        var scrollTop = $(document.body).scrollTop();  
           
        $maskMsg.css({  
            left:( $(document.body).outerWidth(true) - 190 ) / 2  
            ,top:( ($(window).height() - 45) / 2 ) + scrollTop  
        });   
                   
    }  
       
    return {  
        mask:function(msg){  
            init();  
            $mask.show();  
            $maskMsg.html(msg||defMsg).show();  
        }  
        ,unmask:function(){  
            $mask.hide();  
            $maskMsg.hide();  
        }  
    }  
       
}());