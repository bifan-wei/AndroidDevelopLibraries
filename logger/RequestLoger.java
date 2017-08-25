package com.hw.libraries.logger;

import com.lancoo.utils.FileUtil;
import com.lancoo.utils.TimeUtil;
import com.lancoo.utils.XmlUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络请求日志，
 */
public class RequestLoger {

	private static final String savefilepath = LoggerSettings.RequestLogerFileSavePath ;
    private static final String starttag = "request";
    private static final String tag = "RequestLoger";
    private static final int FileMaxSize = 100*1024*1;//限制不超过100KB
	private  static final Boolean isLog = LoggerSettings.isPermitRequestLog;//是否运行记录


	/**
	 *@return  返回的是字节数
	 *--------------------
	 *TODO 获取异常日志文件大小
	 *--------------------
	 * author: huangwei
	 * 2017年1月16日下午3:59:21
	 */
	
	public static long getLogerSize() {
		if(isLog){
		File file = new File(savefilepath);
		return file.length();
		}

		return 0;
	}

	/**
	 *@param requestLogBean
	 *--------------------
	 *TODO
	 *--------------------
	 * author: huangwei
	 * 2017年1月16日下午4:08:39
	 */
	public static void log(RequestLogBean requestLogBean) {
		if (requestLogBean == null)
			throw new NullPointerException("requestLogBean can not be null");
		if(isLog){  
		checkFileSize();		
		String xmldata = XmlUtil.pullXMLCreate(requestLogBean, starttag);
		FileUtil.WriteToFile(Ecode(xmldata), savefilepath, true);
		}

	}

	/**
	 *
	 *--------------------
	 *TODO 检查文件大小，过大的话进行清除
	 *--------------------
	 * author: huangwei
	 * 2017年1月22日上午10:00:33
	 */
	private static void checkFileSize() {
		if(getLogerSize()>=FileMaxSize){
			Clear();
		}
	}

	public static void log( String url, String params,int requesttype, String response, String extralinfo) {
		RequestLogBean requestLogBean = new RequestLogBean();
		requestLogBean.Time = TimeUtil.getCurrentDateString();
		requestLogBean.Url = url;
		requestLogBean.Params = params;
		requestLogBean.ExtraInfo = extralinfo;
		requestLogBean.ResPonse = response;
		requestLogBean.RequestType = requesttype;
		log(requestLogBean);

	}

	public static void Clear() {
		if(isLog){
		FileUtil.WriteToFile("", savefilepath, false);
		}
	}

	/**
	 *@return 不会返回null
	 *-------------------- 
	 *TODO 获取异常记录信息数据
	 *--------------------
	 * author: huangwei
	 * 2017年1月16日下午4:50:45
	 */
	public static List<RequestLogBean> getLogs() {
		if(isLog){
		String exceptionstr = FileUtil.ReadFromFile(savefilepath);				  
		List<RequestLogBean> exceptions = XmlUtil.getObjectsFromXmlObjects(Decode(exceptionstr), RequestLogBean.class,
				starttag);
		return exceptions;
		}else{
			
			return new ArrayList<RequestLogBean>();
		}
	}

	/**
	 *@param exceptionstr
	 *@return
	 *--------------------
	 *TODO 解密解码
	 *--------------------
	 * author: huangwei
	 * 2017年1月16日下午4:56:00
	 */
	private static String Decode(String exceptionstr) {

		return exceptionstr;
	}

	/**
	 *@param xmldata
	 *@return
	 *--------------------
	 *TODO 加密加码
	 *--------------------  
	 * author: huangwei
	 * 2017年1月16日下午4:55:24
	 */
	private static String Ecode(String xmldata) {

		return xmldata;
	}

}
