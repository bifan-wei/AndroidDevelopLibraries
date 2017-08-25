package com.hw.libraries.logger;

import android.os.Environment;

import com.lancoo.utils.FileUtil;

/**
 * Created by HP on 2017/8/25.
 */

public class LoggerSettings {
    public static String Dir = Environment.getExternalStorageDirectory()+"/hwlibriary";
    public static String RequestLogerFileSavePath = Dir +"/5338";
    public static String ExceptionLogerFileSavePath = Dir +"/5339";

    public  static final Boolean isPermitRequestLog = true;//是否运行记录
    public  static final Boolean isPermitExceptionLog = true;//是否运行记录

    static {
        if(!FileUtil.isFileExist(Dir)){
            FileUtil.mkdirs(Dir);
        }
        if(isPermitRequestLog&&!FileUtil.isFileExist(RequestLogerFileSavePath)){
            FileUtil.CreateFile(RequestLogerFileSavePath);
        }
        if(isPermitExceptionLog&&!FileUtil.isFileExist(ExceptionLogerFileSavePath)){
            FileUtil.CreateFile(ExceptionLogerFileSavePath);
        }
    }
}
