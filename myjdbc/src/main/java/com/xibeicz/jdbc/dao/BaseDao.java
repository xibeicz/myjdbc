package com.xibeicz.jdbc.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xibeicz.jdbc.model.PageParam;
import com.xibeicz.jdbc.model.PageResult;
import com.xibeicz.jdbc.utils.ConnectionUtil;
import com.xibeicz.jdbc.utils.SqlUtils;



/**
 * 通用数据库操作类
 * @author xibeicz
 *
 */
public class BaseDao {

	private static Logger log = LoggerFactory.getLogger(BaseDao.class);
	/**
	 * 通用查询，执行sql并将结果集转成对象返回, 支持分页查询
	 * @param <T>
	 * @param sql
	 * @param cc
	 * @return
	 */
	public static <T> PageResult<T> query(String sql, PageParam page, Class<T> cc){
		String pageSql = SqlUtils.addPageParam(sql, page);
		log.info("执行分页查询sql:[{}]", sql);
		try (Connection conn = ConnectionUtil.getConn();
				PreparedStatement pre = conn.prepareStatement(pageSql);ResultSet res = pre.executeQuery()){
			//进行一次count查询
					int count = getCount(sql);
		            //调用将结果集转换成实体对象方法
		            List<T> list = Populate(res, cc);
		            //count查询
		            PageResult<T> pageResult = new PageResult<T>();
		            pageResult.setRows(list);
		            pageResult.setCount(count);
		            return pageResult;
		} catch (Exception e) {
			log.error(String.format("查询sql[%s]报错", sql), e);
		}
		return null;
	}
	/**
	 * 通用查询，执行sql并将结果集转成对象返回, 不支持分页查询
	 * @param <T>
	 * @param sql
	 * @param cc
	 * @return
	 */
	public static <T> List<T> query(String sql, Class<T> cc){
		log.info("执行查询sql:[{}]", sql);
		try (Connection conn = ConnectionUtil.getConn();
				PreparedStatement pre = conn.prepareStatement(sql);ResultSet res = pre.executeQuery()){
		            //调用将结果集转换成实体对象方法
		            return Populate(res, cc);
		} catch (Exception e) {
			log.error(String.format("查询sql[%s]报错", sql), e);
		}
		return null;
	}
	/**
     * 将结果集转换成实体对象集合
     * @param res 结果集
     * @param c 实体对象映射类
     * @return
    * @throws SQLException
    * @throws IllegalAccessException
    * @throws InstantiationException
     */
    private static <T> List<T> Populate(ResultSet rs,Class<T> cc) throws SQLException, InstantiationException, IllegalAccessException{
        
        //结果集 中列的名称和类型的信息
        ResultSetMetaData rsm = rs.getMetaData();
        int colNumber = rsm.getColumnCount();
        List<T> list = new ArrayList<T>();
        Field[] fields = cc.getDeclaredFields();
        
        //遍历每条记录
        while(rs.next()){
            //实例化对象
        	T t = cc.newInstance();
            //取出每一个字段进行赋值
            for(int i=1;i<=colNumber;i++){
                Object value = rs.getObject(i);
                //匹配实体类中对应的属性
                for(int j = 0;j<fields.length;j++){
                    Field f = fields[j];
                    if(f.getName().toUpperCase().equalsIgnoreCase(rsm.getColumnName(i))){
                        boolean flag = f.isAccessible();
                        f.setAccessible(true);
                        if(f.getGenericType().getTypeName().equals("java.math.BigDecimal")) {
                        	if(value == null) {
                        		f.set(t, value);
                        	}else {
                        		f.set(t, new BigDecimal(value+""));
                        	}
                        }else {
                        	f.set(t, value);
                        }
                        f.setAccessible(flag);
                        break;
                    }
                }
                 
            }
            list.add(t);
        }
        log.info("查询出的数据:[{}]",list);
        return list;
    }
	/**
	 * 批量新增
	 * @param list
	 * @param table
	 * @return
	 */
    public static int bathInsert(List<?> list, String table) {
    	
    	try (Connection conn = ConnectionUtil.getConn();
    			Statement statement = conn.createStatement();){
    		for (Object object : list) {
    			String sql = SqlUtils.beans2db(object, table);
    			log.info("批量sql[{}]", sql);
    			statement.addBatch(sql);
			}
    		statement.executeBatch();
        	return 1;
		} catch (Exception e) {
			log.error(String.format("执行批量新增报错sql报错"), e);
		}
		return -1;
    }
    /**
	 * 批量执行sql
	 * @param list
	 * @param table
	 * @return
	 */
    public static int bathInsert(List<String> sqls) {
    	
    	try (Connection conn = ConnectionUtil.getConn();
    			Statement statement = conn.createStatement();){
    		for (String sql : sqls) {
    			log.info("sql[{}]", sql);
    			statement.addBatch(sql);
			}
    		int[] result = statement.executeBatch();
    		log.info("执行结果:[{}]", result);
        	return 1;
		} catch (Exception e) {
			log.error(String.format("执行批量新增报错sql报错"), e);
		}
		return -1;
    }
    /**
	 * 更新
	 * @param sql
	 * @return
	 */
    public static int update(String sql) {
    	log.info("执行更新sql:[{}]", sql);
    	try (Connection conn = ConnectionUtil.getConn();
    			Statement statement = conn.createStatement();){
		            //调用将结果集转换成实体对象方法
    		//插入测试数据
        	return statement.executeUpdate(sql);
		} catch (Exception e) {
			log.error(String.format("执行更新报错sql[%s]报错", sql), e);
		}
		return -1;
    }
	/**
	 * 执行sql判断是否执行成功
	 * @param sql
	 * @return
	 */
    public static boolean execSql(String sql) {
    	log.info("执行sql:[{}]", sql);
    	try (Connection conn = ConnectionUtil.getConn();
    			Statement statement = conn.createStatement();){
		            //调用将结果集转换成实体对象方法
    		//插入测试数据
        	return statement.execute(sql);
		} catch (Exception e) {
			log.error(String.format("执行sql[%s]报错", sql), e);
		}
		return false;
    }
    /**
     * 获取count
     * @param sql
     * @return
     */
    public static int getCount(String sql) {
    	String countSql = String.format("select count(0) from (%s) as a", sql);
    	log.info("执行sql:[{}]", countSql);
    	ResultSet rs = null;
    	int count = 0;
    	try (Connection conn = ConnectionUtil.getConn();
    			Statement statement = conn.createStatement();){
		            //调用将结果集转换成实体对象方法
    		//插入测试数据
    		rs = statement.executeQuery(countSql);
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			log.error(String.format("执行sql[%s]报错", countSql), e);
		}
		return count;
    }
}
