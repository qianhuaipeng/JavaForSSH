
package com.eastrobot.robotdev.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 */
public class SystemKeys {
	
	public final static String UTF8 = "UTF-8";
	
	/**
	 * 获取图片对象，统一放在DATAS下
	 * @param imgName
	 * @return
	 */
	public static File getImageAddr(String imgName){
		return getFile(File.separator + "DATAS/" + imgName);
	}

	/**
	 * 获取服务器磁盘文件
	 * @param filePath /resource/fileupload/87649382.jpg
	 * @return
	 */
	public static File getFile(String filePath){
		File file = new File(getDir() + filePath);
		return file;
	}
	
	/**
	 * 获取项目磁盘路径
	 * @return
	 */
	public static String getDir(){
		if (System.getProperty("webAppRoot")==null) return "";
		return System.getProperty("webAppRoot").replaceAll("%20", " ");
	}
	
	/**
	 * 给指定的 JsonArray 对象针对 field 字段排降序
	 * @param arr	JSONArray
	 * @param field	字段，如 hot
	 * @return
	 */
	public static void sort(JSONArray arr, final String field){
		sort(arr, field, false);
	}
	
	/**
	 * 给指定的 JsonArray 对象针对 field 字段排序
	 * @param arr	JSONArray
	 * @param field	字段，如 hot
	 * @param isAsc true：升序，false：降序
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void sort(JSONArray arr, final String field, boolean isAsc){
		Collections.sort(arr, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject json1, JSONObject json2) {
				Object obj1 = 0;
				Object obj2 = 0;
				
				if (json1.has(field)) obj1 = json1.get(field);
				if (json2.has(field)) obj2 = json2.get(field);
				
				if (obj1 instanceof Integer && obj2 instanceof Integer) {
					return ((Integer) obj1).intValue() - ((Integer) obj2).intValue();
				} else {
					return obj1.toString().compareTo(obj2.toString());
				}
			}
		});
		if (!isAsc) {
			Collections.reverse(arr);
		}
	}
	/**
	 * 创建文件夹
	 * @param dirName
	 */
	public static void mkdir(String dirName) {
		File dir = SystemKeys.getFile(dirName);
		if (!dir.exists()){
			try {
				FileUtils.forceMkdir(dir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
