/**
 * 日期插件
 * 使用方法：
 * DateUtils.format(new Date()) --> 2017-06-08
 * DateUtils.add(new Date(), 3, 1) --> 2017-09-08
 * DateUtils.add(new Date(), 7) --> 2017-06-15
 * @auther 
 * @date 2017-06-08 11:55
 */
;var DateUtils = {
	YEAR: 0, MONTH: 1, DAY: 2,
	defaultDatePattern: 'yyyy-MM-dd',
	defaultDatetimePattern: 'yyyy-MM-dd HH:mm:ss',
	/* 格式化 */
	format: function(date, fmt){
		//copy from http://www.cnblogs.com/zhangpengshou/archive/2012/07/19/2599053.html
		var o = {
	        "M+": date.getMonth() + 1, //月份 
	        "d+": date.getDate(), //日 
	        "H+": date.getHours(), //小时 
	        "h+": date.getHours(), //小时 
	        "m+": date.getMinutes(), //分 
	        "s+": date.getSeconds(), //秒 
	        "q+": Math.floor((date.getMonth() + 3) / 3), //季度 
	        "S": date.getMilliseconds() //毫秒 
	    };
		fmt = fmt==undefined?this.defaultDatePattern:fmt;
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o){
	    	if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    }
	    return fmt;
	},
	/* 增加或减少年月日 */
	add: function(date, value, type){
		var _this = this;
		type = type==null?2:type;
		switch(type){
		case _this.YEAR:
			date.setYear(date.getFullYear() + value);
			break;
		case _this.MONTH:
			date.setMonth(date.getMonth() + value);
			break;
		default:
			date.setDate(date.getDate() + value);
		}
		return date;
	},
	compareTime: function(){
		if (startDate.length > 0 && endDate.length > 0) {   
		    var startDateTemp = startDate.split(" ");   
		    var endDateTemp = endDate.split(" ");   
		    var arrStartDate = startDateTemp[0].split("-");   
		    var arrEndDate = endDateTemp[0].split("-");   
		    var arrStartTime = startDateTemp[1].split(":");   
		    var arrEndTime = endDateTemp[1].split(":");   
		    var allStartDate = new Date(arrStartDate[0], arrStartDate[1], arrStartDate[2], arrStartTime[0], arrStartTime[1], arrStartTime[2]);   
		    var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2], arrEndTime[0], arrEndTime[1], arrEndTime[2]);   
		    //console.info(allEndDate.getMonth() - allStartDate.getMonth());
			if (allStartDate.getTime() >= allEndDate.getTime()) {   
		        alert("开始时间不能大于结束时间");   
		        return false;   
			} else if(allStartDate.getFullYear() != allEndDate.getFullYear()){
				alert("时间跨度不能跨年分");   
		        return false;
			} else if(allEndDate.getMonth() - allStartDate.getMonth() > 1){
				alert("时间只能选择相邻月份");   
		        return false;
			}
				return true;   
		} else if(startDate.length == 0 && endDate.length > 0){   
			alert("请选择开始时间！");   
			return false;   
	    }   

	}
};