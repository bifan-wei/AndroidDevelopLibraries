package com.hw.libraries.net;

/**
 * 请求结果
 */
public class ResquestResponse {
	/**
	 * 请求是否成功
	 */
	public Boolean isSucessful = false;

	/**
	 * 是否超时
	 */
	public Boolean IsTimeout = false;

	/**
	 * 是否的未知地址
	 */
	public Boolean UnKownHost = false;

	/**
	 * 请求取消了请求
	 */
	public Boolean isCanceled = false;

	/**
	 * 请求返回状态码
	 */
	public int Responsecode = -1;
	/**
	 * 数据体
	 */
	public String Data = "";
	/**
	 * 错误信息
	 */
	public String errormessage = "";
	
	@Override
	public String toString() {
		return "ResquestResponse [isSucessful=" + isSucessful + ", IsTimeout=" + IsTimeout + ", UnKownHost="
				+ UnKownHost + ", isCanceled=" + isCanceled + ", Responsecode=" + Responsecode + ", Data=" + Data
				+ ", errormessage=" + errormessage + "]";
	}

	

}
