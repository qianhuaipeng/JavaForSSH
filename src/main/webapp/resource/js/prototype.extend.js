
String.prototype.startWithIgnoreCase = function (start) {
    try {
        if (this.toLowerCase().indexOf(start.toLowerCase()) == 0) {
            return true;
        } else {
            return false;
        }
    } catch (e) {
        return false;
    }
}
/* 不区分大小写 */
String.prototype.endWithIgnoreCase = function (end) {
    try {
        if (this.toLowerCase().lastIndexOf(end.toLowerCase()) == (this.length - end.length)) {
            return true;
        } else {
            return false;
        }
    } catch (e) {
        return false;
    }
}

/* 不区分大小写 */
String.prototype.indexOfIgnoreCase = function (find) {
    return this.toLowerCase().indexOf(find.toLowerCase());
}
/**
 * 参考jQuery的$.trim()
 * @return
 */
String.prototype.trim = function () {
    rtrim = /\S/.test("\xA0") ? (/^[\s\xA0]+|[\s\xA0]+$/g) : /^\s+|\s+$/g;
    return this.replace(rtrim, "");
}
/* 
 * @param m ab{0}de{1}g 
 * @param i "c","d"
 * @return abcdefg
 */
String.prototype.fill = function () {
    var args = arguments;
    return this.replace(/\{(\d+)\}/g, function (m, i) {
        return args[i];
    });
}
/**
 * 过滤字符串中的html标签
 */
String.prototype.formatHtml = function(){
	var s = this;
	if (this==null) return '';
	s = s.replace(/<\/?[^>]+>/g, '');
    s = s.replace(/\&[a-z]+;/gi, '');
    return s;
}
/**
 * @author 
 * @since 2014-05-22
 * @description 从字符串中截取指定字符串的最右，从右边开始查找(lastIndexOf)
 * 例如："abcdefg".right("bcd") 最终返回 "efg"
 */
String.prototype.right = function (find) {
    if (this.lastIndexOf(find) == -1) return this;
    return this.substring(this.lastIndexOf(find) + find.length, this.length);
}
window._isWeixin = function () {
    var userAgent = window.navigator.userAgent.toLowerCase();
    var isWeixin = userAgent.match(/MicroMessenger/i) == 'micromessenger';
    return isWeixin;
}
/**
 * 日期格式化
 * //TODO 待扩展，根据pattern来返回日期格式
 */
Date.prototype.format = function(pattern){
	var _this = this;
	var year = _this.getFullYear();
	var month = _this.getMonth() + 1;
	month = month<=9?'0' + month:month;
	var date = _this.getDate();
	date = date<=9?'0' + date:date;
	return year + '-' + month + '-' + date;
}
/**
 * @author 
 * @since 2017-04-13
 * @description 获取url的参数
 * 例如：http://127.0.0.1:9090/html/base/notice/list.do?title=1 最终返回 {title:1}
 */
window._paramParse = function (url) {
    var paramString,
        paramStringArr,
        params = {},
        paramsObj = {};
    if (typeof(url) == 'undefined') {
        //当前url
        url = window.location.search;
    }
    paramString = url.substring(url.indexOf('?') + 1, url.length);
    // 分割
    paramStringArr = decodeURI(paramString).split("&");
    try {
        for (var i = 0; i < paramStringArr.length; i++) {
            var item = paramStringArr[i];
            var key = item.substring(0, item.indexOf("="));
            var value = item.substring(item.indexOf("=") + 1, item.length);
            if (key) {
                // 不存在
                if (!params[key]) {
                    params[key] = [];
                    params[key].push(value);
                } else {
                    params[key].push(value);
                }
            }
        }
        for (var i in params) {
            if (params[i].length > 1) {
                paramsObj[i] = [];
                for (var j in params[i]) {
                    paramsObj[i].push(params[i][j]);
                }
            } else if (params[i].length == 1) {
                paramsObj[i] = params[i][0];
            } else {
                paramsObj[i] = null;
            }
        }
    } catch (ex) {
    }
    return paramsObj;
};

/**
 * IE8下提示 console 未定义错误解决
 * @type {*|Console}
 */
window.console = window.console || (function(){
    var c = {}; c.log = c.warn = c.debug = c.info = c.error = c.time = c.dir = c.profile
        = c.clear = c.exception = c.trace = c.assert = function(){};
    return c;
})();