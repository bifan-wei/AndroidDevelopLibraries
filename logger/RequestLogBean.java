package com.hw.libraries.logger;

public class RequestLogBean {
	
	
	/**
	 * 请求发起时间
	 */
	public String Time;
	/**
	 * 请求地址
	 */
	public String Url;
	/**
	 * 请求参数
	 */
	public String Params;
	/**
	 * 请求类型：1为post or 2为 get
	 */
	public int RequestType ;
	/**
	 * 返回信息
	 */
	public String ResPonse;
	
	/**
	 * 请求状态码
	 */
	public int ResPonseCode = -1;
	
	/**
	 * 请求是否成功
	 */
	public Boolean IsSucess = false;
	
	/**
	 * 错误信息
	 */
	public String ErrorMessage = "";
	/**
	 * 额外信息
	 */
	public String ExtraInfo;
	
	@Override
	public String toString() {
		return "RequestLogBean [Time=" + Time + ", Url=" + Url + ", Params=" + Params + ", RequestType=" + RequestType
				+ ", ResPonseInfo=" + ResPonse + ", ExtraInfo=" + ExtraInfo + "]";
	}
	
	

}
