package com.xibeicz.jdbc.model;

import java.util.List;
/**
 * 响应数据
 * @author Administrator
 *
 * @param <T>
 */
public class PageResult<T> {

	
	private int count;
	private List<T> rows;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
}
