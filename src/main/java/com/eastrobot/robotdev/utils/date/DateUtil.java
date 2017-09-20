package com.eastrobot.robotdev.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONObject;

public final class DateUtil {
	/**
	 * 日期格式（yyyy-MM-dd）
	 */
	public static String PATTERN_DATE = "yyyy-MM-dd";
	
	/**
	 * 日期格式（"yyyy-MM-dd HH:mm:ss"）
	 */
	public static String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 获取日期和时间(对象,时、分、秒不为0)
	 * @return
	 */
	public static Date dateTime(){
		return new Date();
	}
	
	/**
	 * 获取日期(字符串)
	 * @param pattern	时间字符串格式
	 * @return
	 */
	public static String get(String pattern){
		return new SimpleDateFormat(pattern).format(dateTime());
	}
	
	//*********************************************************
	
	/**
	 * 获取日期(字符串)
	 * 	例如：2015-01-01
	 * @return
	 */
	public static String getDate() {
		return get(PATTERN_DATE);
	}
	
	/**
	 * 获取日期和时间(字符串)
	 * 	例如：2015-01-01 01:59:59
	 * @return
	 */
	public static String getDateTime() {
		return get(PATTERN_DATETIME);
	}
	
	//*********************************************************
	
	/**
	 * 获取日期(对象,时、分、秒为0)
	 * @return
	 */
	public static Date date(String date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取日期(对象,时、分、秒为0)
	 * @return
	 */
	public static Date date(String date){
		return date(date, PATTERN_DATE);
	}
	
	/**
	 * 获取日期
	 * @return
	 */
	public static Date dateTime(String date){
		return date(date, PATTERN_DATETIME);
	}
	
	/**
	 * 获取日期(对象,时、分、秒为0)
	 * @return
	 */
	public static Date date(){
		return date(getDate());
	}
	
	//*********************************************************
	
	/**
	 * 获取指定日期(字符串)
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formt(Date date, String pattern) {
		return date == null ? "" : new SimpleDateFormat(pattern).format(date);
	}
	
	/**
	 * 获取指定日期(字符串)
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formtDate(Date date) {
		return formt(date, PATTERN_DATE);
	}
	
	/**
	 * 获取指定日期和时间(字符串)
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formtDateTime(Date date) {
		return formt(date, PATTERN_DATETIME);
	}
	
	/**
	 * 获取昨天（字符串）
	 * @return
	 */
	public static String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return new SimpleDateFormat(PATTERN_DATE).format(cal.getTime());
	}
	
	/**
	 * 获取当前月份(字符串)
	 * 	例如：01、02、10、12
	 * @return
	 */
	public static String getMonth(){
		Calendar c = Calendar.getInstance();
		String month = "" + c.get(Calendar.YEAR) ;
		if(c.get(Calendar.MONTH)<9){
			month += 0;
		}
		month += c.get(Calendar.MONTH)+1;
		return month;
	}
	
	/**
	 * 获取当前日、周、月时间段
	 * @param cycleType	1-日,2-周,3-月
	 * @return date_s 起始时间,date_e 结束时间
	 * 	
	 */
	public static JSONObject timeBucket(int cycleType){
		String date_s,date_e;
		JSONObject timeBucket = new JSONObject();
		if(cycleType == 1){
			date_s = DateUtil.formtDateTime(DateUtil.date());
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(DateUtil.date());
			calendar.add(Calendar.DATE, +1);
			calendar.add(Calendar.SECOND, -1);
			date_e = DateUtil.formtDateTime(calendar.getTime());
			
		}else if(cycleType == 2){
			Calendar calendar = Calendar.getInstance();
			//设置每周第一天是星期几(1为星期日,2为星期一,7为星期六)
			calendar.setFirstDayOfWeek(2);
			calendar.setTime(DateUtil.date());
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			date_s = DateUtil.formtDateTime(calendar.getTime());
			
			calendar.setTime(DateUtil.date());
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			calendar.add(Calendar.DATE, +1);
			calendar.add(Calendar.SECOND, -1);
			date_e = DateUtil.formtDateTime(calendar.getTime());
		}else if(cycleType == 3){
			Calendar calendar = Calendar.getInstance();
			//设置每周第一天是星期几(1为星期日,2为星期一,7为星期六)
			calendar.setTime(DateUtil.date());
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			date_s = DateUtil.formtDateTime(calendar.getTime());
			
			calendar.setTime(DateUtil.date());
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			calendar.add(Calendar.DATE, +1);
			calendar.add(Calendar.SECOND, -1);
			date_e = DateUtil.formtDateTime(calendar.getTime());
		}else{
			return null;
		}
		timeBucket.put("date_s", date_s);
		timeBucket.put("date_e", date_e);
		return timeBucket;
	}
	
	/**
	 * 计算2个日期相距field（年、月、天）的值
	 * @param sdate
	 * @param bdate
	 * @param field	Calendar.DATE/Calendar.MONTH/Calendar.YEAR
	 * @return
	 */
	public static int between(Date sdate, Date edate, int field){
		try {
			if(sdate.after(edate))
				throw new Exception("日期sdate必须小于日期bdate!");
			if(field == Calendar.DATE){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(sdf.format(sdate).equals(sdf.format(edate)))return 0;
			}else if (field == Calendar.MONTH){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				if(sdf.format(sdate).equals(sdf.format(edate)))return 0;
			}else if (field == Calendar.YEAR){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				if(sdf.format(sdate).equals(sdf.format(edate)))return 0;
			}else{
				throw new Exception("field只支持Calendar.DATE/Calendar.MONTH/Calendar.YEAR!");
			}
			
			Calendar scalendar = Calendar.getInstance();
			scalendar.setTime(sdate);
			Calendar ecalendar = Calendar.getInstance();
			ecalendar.setTime(edate);
			
			int i = 0;
			while(scalendar.before(ecalendar)){
				i++;
				scalendar.add(field, 1);
			}
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 计算2个日期相距field（年、月、天）的值
	 * @param sdate
	 * @param bdate
	 * @param field	Calendar.DATE\Calendar.MONTH\Calendar.YEAR
	 * @return
	 */
	public static int between(String sdate, String edate, String pattern, int field){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Date sdate_ = sdf.parse(sdate);
			Date edate_ = sdf.parse(edate);
			return between(sdate_, edate_, field);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}
