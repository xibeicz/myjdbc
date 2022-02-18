package com.xibeicz.jdbc.model;
/**
 * 分页查询
 */
public class PageParam {

	/**
	 * 查询行数
	 */
	private Integer limit;
	/**
	 * 起始行
	 */
	private Integer start;
	
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public PageParam(Integer limit, Integer start) {
		super();
		this.limit = limit;
		this.start = start;
	}
	public PageParam() {
		super();
	}
	
}
