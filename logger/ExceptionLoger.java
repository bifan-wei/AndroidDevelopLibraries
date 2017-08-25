package com.lancoo.logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.lancoo.hw.appbase.HwConstant;
import com.lancoo.hw.beans.ExceptionBean;
import com.lancoo.hw.utils.FileUtil;
import com.lancoo.hw.utils.TimeUtil;
import com.lancoo.hw.utils.XmlUtil;

/**
 *TODO
 *--------------------
 * author: huangwei
 * 2017年1月16日下午4:48:28
 */
public class ExceptionLoger {

	public static final String savefilepath = HwConstant.SystemFilePathroot + "/526b14";
	public static final String starttag = "exception";
	public static final String tag = "ExceptionLoger";
	public static final int FileMaxSize = 100*1024*1; //限制不超过100K
	private  static final Boolean isLog = true;//是否运行记录

	static {
		if (isLog&&!FileUtil.isFileExist(savefilepath)) {
			FileUtil.CreateFile(savefilepath);
		}
	}

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
	 *@param exceptionBean
	 *--------------------
	 *TODO
	 *--------------------
	 * author: huangwei
	 * 2017年1月16日下午4:08:39
	 */
	public static void log(ExceptionBean exceptionBean) {
		if(isLog){
		if (exceptionBean == null)
			throw new NullPointerException("exceptionBean can not be null");
		checkFileSize();
		String xmldata = XmlUtil.pullXMLCreate(exceptionBean, starttag);
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

	public static void log(String tag ,String Excetionmsg, String extralinfo) {
		ExceptionBean exceptionBean = new ExceptionBean();
		exceptionBean.ExtraInfo = extralinfo;
		exceptionBean.Message = Excetionmsg;
		tag=tag==null?"":tag;
		exceptionBean.Tag = tag;
		exceptionBean.Time = TimeUtil.getCurrentDateString();
		log(exceptionBean);

	}
	
	public static void log(String Excetionmsg, String extralinfo) {
		log(null, Excetionmsg, extralinfo);

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
	public static List<ExceptionBean> getExceptions() {
		if(isLog){
		String exceptionstr = FileUtil.ReadFromFile(savefilepath);
		List<ExceptionBean> exceptions = XmlUtil.getObjectsFromXmlObjects(Decode(exceptionstr), ExceptionBean.class, starttag);		
		return exceptions;
		}else{
			return new ArrayList<ExceptionBean>();
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
		//return MD5Helper.reverseMD5(exceptionstr);
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
		//return MD5Helper.MD5(xmldata);
	}

	
	
}
