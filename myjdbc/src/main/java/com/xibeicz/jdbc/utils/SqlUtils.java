package com.xibeicz.jdbc.utils;

import java.lang.reflect.Field;
import java.util.List;

import com.xibeicz.jdbc.model.PageParam;

import main.java.org.apache.commons.beanutils.BeanUtils;

/**
 * sql工具类
 * @author xibeicz
 *
 */
public class SqlUtils {

	/**
	 * JavaBean的list集合拼接成Insert语句
	 * @param objs	bean集合
	 * @param table	表名
	 * @return
	 * 	返回格式：insert  into TABLE (PRAMA,PRAMA,……) values(VALUE,VALUE,VALUE……),(VALUE,VALUE,VALUE……),(VALUE,VALUE,VALUE……)……  ；
	 */
	public static String beans2db(List<?> objs, String table) {
		String sql="";
		StringBuffer prama=new StringBuffer();
		StringBuffer values=new StringBuffer();
		try {
			Field[] fields = objs.get(0).getClass().getDeclaredFields();
			for (Field field : fields) {
				prama.append(","+field.getName());
			}
			for (Object obj : objs) {
				values=values.append(",(").append(getValue2Bean(obj)).append(")");
			}
			
			prama=prama.deleteCharAt(0);
			values=values.deleteCharAt(0);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		sql="insert into "+table+"("+prama+") values " +values;
		return sql;
	}
	/**
	 * JavaBean的list集合拼接成Insert语句
	 * @param objs	bean集合
	 * @param table	表名
	 * @return
	 * 	返回格式：insert  into TABLE (PRAMA,PRAMA,……) values(VALUE,VALUE,VALUE……)；
	 */
	public static String beans2db(Object obj, String table) {
		String sql="";
		StringBuffer prama=new StringBuffer();
		StringBuffer values=new StringBuffer();
		try {
			Class<?> class1 = obj.getClass();
			Field[] fields = class1.getDeclaredFields();
			for (Field field : fields) {
				prama.append(","+field.getName());
			}
			values=values.append("(").append(getValue2Bean(obj)).append(")");
			
			prama=prama.deleteCharAt(0);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		sql="insert into "+table+"("+prama+") values " +values;
		return sql;
	}
    /**
   	 * 参数值拼接
   	 * param obj
   	 * return
   	 * throws Exception
   	 */
   	private static StringBuffer getValue2Bean(Object obj) throws Exception{
   		Field[] fields  =  obj.getClass().getDeclaredFields();
   		StringBuffer value = new StringBuffer();
   		String v= null;
   		for (Field field : fields) {
   			v=BeanUtils.getProperty(obj, field.getName());
   			switch (field.getGenericType().getTypeName()) {
			case "java.lang.String":
				value.append(",'"+v+"'");
				break;
			default:
				value.append(","+v);
				break;
			}
   		}
   		value=value.deleteCharAt(0);
   		return value;
   	}
   	/**
   	 * 添加分页参数
   	 * @param sql
   	 * @param page
   	 * @return
   	 */
   	public static String addPageParam(String sql, PageParam page) {
   		Integer start = page.getStart();
   		Integer limit = page.getLimit();
   		if(start !=null && start >-1 && limit != null && limit >0) {
   			return String.format("%s limit %d,%d", sql, start, limit);
   		}
   		return sql;
   	}
}
