package com.eastrobot.robotdev.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils{

    /**
     * 默认日期格式
     */
    public final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public final static String CHINESE_PATTERN = "yyyy年M月d日 HH时mm分";

    public final static String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 缓存的SimpleDateFormat对象Map,pattern为键
     */
    // 格式化日期SimpleDateFormat
    private static Map<String, SimpleDateFormat> patternMap = new HashMap<String, SimpleDateFormat>();

    // 初始化formatMap
    static{
        patternMap.put("yyyy-MM-dd HH:mm:ss", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        patternMap.put("yyyy-MM-dd HH:mm", new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        patternMap.put("yyyy-MM-dd HH", new SimpleDateFormat("yyyy-MM-dd HH"));
        patternMap.put("yyyy-MM-dd", new SimpleDateFormat("yyyy-MM-dd"));
        patternMap.put("yyyy年M月d日 HH时mm分", new SimpleDateFormat("yyyy年M月d日 HH时mm分"));
    }

    /**
     * 从缓存中获取SimpleDateFormat对象,否则新创建对象返回 (注意获取的SimpleDateFormat对象不应该对其再次进行模式设置)
     * 
     * @param pattern
     * @return
     */
    public static SimpleDateFormat getCacheSimpleDateFormat( String pattern ){
        SimpleDateFormat sdf = patternMap.get(pattern);
        if( sdf == null ){
            sdf = new SimpleDateFormat(pattern);
            patternMap.put(pattern, sdf);
        }
        return sdf;
    }

    /**    
     * getNewSimpleDateFormat(创建SimpleDateFormat对象返回)   
     * @param pattern
     * @param lenient
     * @return SimpleDateFormat
     * @modify: last edited 2015下午4:13:17    
    */
    public static SimpleDateFormat getNewSimpleDateFormat( String pattern, boolean lenient ){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setLenient(lenient);
        return sdf;
    }

    /**    
     * date2Str(按pattern格式化Date,返回字符串表现形式)    
     * @param d 日期
     * @param pattern 格式字符串
     * @return String
     * last edited 2015上午11:13:14    
    */
    public static String date2Str( Date d, String pattern ){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(d);
    }

    /**    
     * date2Str(按默认CHINESE_PATTERN格式化字符串)    
     * @param d 日期
     * @return String
     * last edited 2015上午11:13:48    
    */
    public static String date2Str( Date d ){
        return date2Str(d, CHINESE_PATTERN);
    }

    /**    
     * date2StrUseCacheSdf(使用缓存的SimpleDateFormat格式化日期)    
     * @param d 日期
     * @param pattern 格式字符串
     * @return String
     * last edited 2015上午11:14:15    
    */
    public static String date2StrUseCacheSdf( Date d, String pattern ){
        SimpleDateFormat sdf = getCacheSimpleDateFormat(pattern);
        return sdf.format(d);
    }

    /**    
     * str2Date(对字符串source进行解析(格式pattern),返回Date对象)    
     * @param source  日期字符串
     * @param pattern 日期格式
     * @return Date
     * last edited 2015上午11:14:40    
    */
    public static Date str2Date( String source, String pattern ){
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = null;
        try{
            // 严格格式化
            // df.setLenient( false );
            date = df.parse(source);
        }catch( ParseException e ){
            e.printStackTrace();
        }
        return date;
    }

    /**    
     * str2Date(对字符串source进行解析(默认格式yyyy-MM-dd HH:mm:ss),返回Date对象)    
     * @param source 日期字符串
     * @return Date 解析异常,返回null
     * last edited 2015上午11:15:19    
    */
    public static Date str2Date( String source ){
        return str2Date(source, DEFAULT_PATTERN);
    }

    /**    
     * daysLater(得到指定日期偏移n天的Date)    
     * @param d 日期参数（可为负数）
     * @param n 偏移天数,Double类型
     * @return Date 返回date或null
     * last edited 2015上午11:15:53    
    */
    public static Date daysLater( Date d, double n ){
        Date date = null;
        if( d != null ){
            date = new Date(d.getTime() + (new Double(86400000 * n).longValue()));
        }
        return date;
    }

    /**    
     * millisLater(计算日期偏移offset毫秒后的Date)    
     * @param d 传入的日期
     * @param offset 偏移的毫秒数
     * @return Date
     * last edited 2015上午11:07:38    
    */
    public static Date millisLater( Date d, long offset ){
        Date date = null;
        if( d != null ){
            date = new Date(d.getTime() + offset);
        }
        return date;
    }

    /**    
     * str2Date(对字符串source进行解析(格式pattern),返回Date对象)    
     * @param source  日期字符串
     * @param pattern 日期格式
     * @param lenient 是否严格格式化
     * @return 转换失败,返回null;
     * Date
     * last edited 2015上午11:08:19    
    */
    public static Date str2Date( String source, String pattern, boolean lenient ){
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = null;
        try{
            df.setLenient(lenient);// 是否严格格式化
            date = df.parse(source);
        }catch( ParseException e ){
            e.printStackTrace();
        }
        return date;
    }

    /**    
     * getDateOfDayStart(返回当天凌晨零点的日期对象)    
     * TODO(这里描述这个方法适用条件 – 可选)    
     * TODO(这里描述这个方法的执行流程 – 可选)    
     * TODO(这里描述这个方法的使用方法 – 可选)    
     * TODO(这里描述这个方法的注意事项 – 可选)    
     * @return Date
     * last edited 2015上午11:09:06    
    */
    public static Date getDateOfDayStart(){
        return str2Date(date2Str(new Date(), YYYY_MM_DD), YYYY_MM_DD);
    }

    /**    
     * withinMonths(传入日期是否在当前日期为结束时间的n个月以内)    
     * @param date 传入日期
     * @param n    N个月
     * @return  boolean
     * last edited 2015上午11:09:27    
    */
    public static boolean withinMonths( Date date, int n ){
        Calendar cal = Calendar.getInstance();
        long dateTime = date.getTime();
        long nTime = cal.getTimeInMillis();
        if( nTime >= dateTime ){
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - n);
            nTime = cal.getTimeInMillis();
            if( dateTime >= nTime )
                return true;
        }
        return false;
    }

    /**    
     * getFirstDayOfMonth(返回当前月的第一天)    
     * @param date
     * @return Date
     * last edited 2015上午11:20:30    
    */
    public static Date getFirstDayOfMonth( Date date ){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**    
     * getFirstDayOfNextMonth(返回下一个月的第一天日期对象)    
     * @param date 
     * @return Date
     * last edited 2015上午11:19:06    
    */
    public static Date getFirstDayOfNextMonth( Date date ){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        return cal.getTime();
    }

    /**    
     * dayApart(开始时间与结束时间相差多少天)    
     * @param start 开始时间
     * @param end 结束时间
     * @return double 当开始日期或者结束日期为空时，则返回Double.MIN_VALUE
     * last edited 2015上午11:18:16    
    */
    public static double dayApart( Date start, Date end ){
        if( start == null || end == null )
            return Double.MIN_VALUE;
        return ((start.getTime() - end.getTime()) / 86400000);
    }

    /**    
     * minus(两个日期相减，返回相差毫秒数) 
     * TODO( end - start = double ) 
     * @param minuend 被减数
     * @param meiosis 减数
     * @return double
     * last edited 2015上午11:17:17    
    */
    /**    
     * minus(这里用一句话描述这个方法的作用)    
     * @param minuend
     * @param meiosis
     * @return double
     * @modify: last edited 2015上午11:28:19    
    */
    public static double minus( Date minuend, Date meiosis ){
        return (minuend.getTime() - meiosis.getTime());
    }

    /**    
     * isDate(判断是否为日期字符串)    
     * @param source 日期字符串
     * @param format 日期格式
     * @return boolean
     * last edited 2015上午11:16:38    
    */
    public static boolean isDate( String source, String format ){
        boolean isDate = false;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try{
            sdf.setLenient(false);
            sdf.parse(source);
            isDate = true;
        }catch( ParseException e ){
            isDate = false;
        }
        return isDate;
    }

    /**
     * 
     * @param date：日期
     * @return:返回当前日期的月份
     */
    public static int getMonth(Date date){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	return calendar.get(Calendar.MONTH);
    }
    
    /**
     * 
     * @param date：日期
     * @return:返回当前日期的月份
     */
    public static String getFormartMonth(Date date){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int month = calendar.get(Calendar.MONTH);
    	String str = String.valueOf(month);
    	if (month < 10) {
    		str = String.valueOf( "0" + month);
		}
    	return str;
    }
    
    /**
     * 判断输入的日期是否和当前日期是同一个月份
     * @param date：输入的日期
     * @return：true 是，false 否
     */
    public static boolean isCurrentMonth(Date date){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	Calendar current = Calendar.getInstance();
    	if (calendar.get(Calendar.MONTH) == current.get(Calendar.MONTH)) {
			return true;
		}
    	
    	return false;
    }
}
