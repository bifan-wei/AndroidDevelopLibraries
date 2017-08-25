package com.hw.libraries.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 */
public class FileUtil {

	/**
	 * @param bytesize 字节大小
	 * @return 字节大小转化为显示字符，小于MB的计算KB,大于1MB的计算MB
	 */
	public static String ByteSizeToKBorMBString(long bytesize) {
		if (bytesize <= 0) {
			return "0B";
		}
		if (bytesize < 1024)
			return bytesize + "B";

		int kb = Math.round(bytesize / 1024);

		if (kb >= 1024) {
			float mb = FormatUtil.getFloat_Keeptwodecimalplaces(((float)kb) / 1024);
			return mb + " MB";
		}

		return kb + " KB";
	}


	/**
	 * @param path
	 * @return 判断文件是否存在
	 */
	public static Boolean isFileExist(String path) {
		File file = new File(path);
		return file.exists();
	}


	/**
	 * @param path 创建文件
	 * @return
	 */
	public static Boolean CreateFile(String path) {
		File file = new File(path);
		try {
			file.createNewFile();
			return true;
		} catch (IOException e) {

			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir 将要删除的文件目录
	 * @return boolean 
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}


	/**
	 * @param path 创建目录
	 */
	public static void mkdirs(String path) {
		File file = new File(path);
		file.mkdirs();
	}

	/**
	 * 调用该方法前确保文件是存在
	 * @param savestr要写入的字符串       
	 * @param filepath 文件路径        
	 * @param inappendmode 是否是后面追加模式        
	 *  ------------------- 
	 *  TODO 将字符串写入指定文件
	 *  --------------------
	 */
	public static void WriteToFile(String savestr, String filepath, Boolean inappendmode) {
		try {
			FileWriter writer = new FileWriter(filepath, inappendmode);
			writer.write(savestr);
			writer.close();
		} catch (IOException e1) {

			e1.printStackTrace();
		}

	}

	/**  读取指定文件字符数据
	 *@param filepath 
	 *@return 文件不存在是返回""
	 *--------------------
	 */
	public static String ReadFromFile(String filepath) {
		try {
			FileReader reader = new FileReader(filepath);
			StringBuffer stringBuffer = new StringBuffer();

			int length = 512;
			char[] cs = new char[length];
			int nums = 0;
			while ((nums = reader.read(cs, 0, length)) != -1) {

				if (nums < length && nums > 0) {
					char[] cs1 = new char[nums];
					System.arraycopy(cs, 0, cs1, 0, nums);
					stringBuffer.append(cs1);
				} else {
					stringBuffer.append(cs);

				}

			}
			reader.close();
			return stringBuffer.toString();

		} catch (IOException e1) {

			e1.printStackTrace();
		}
		return "";
	}

	/**
	 *@param filefrom 要复制的源文件路径
	 *@param savepathto 要复制保存的路径
	 *@return
	 *--------------------
	 *TODO 复制文件到指定文件
	 *--------------------
	 */
	public static Boolean CopyFile(String filefrom, String savepathto) {
		return CopyFile(new File(filefrom), savepathto, null);
	}

	/**
	 * @param filefrom
	 * @param savepathto
	 * @param copyListsner 文件复制监听
	 * *TODO 复制文件到指定文件
	 * @return
	 */
	public static Boolean CopyFile(String filefrom, String savepathto, FileCopyListsner copyListsner) {
		return CopyFile(new File(filefrom), savepathto, copyListsner);
	}

	/**
	 *@param filefrom 要复制的源文件
	 *@param savepathto 要复制保存的路径
	 *@return 返回复制是否成功
	 *--------------------
	 *TODO
	 */
	public static Boolean CopyFile(File filefrom, String savepathto, FileCopyListsner copyListsner) {

		if (filefrom == null || !filefrom.exists()) {
			if (copyListsner != null) {
				copyListsner.onFaile();
			}
			return false;
		}

		File file = new File(savepathto);
		if (file.exists()) {
			file.delete();
		}

		try {
			file.createNewFile();
		} catch (IOException e) {

			e.printStackTrace();
		}

		if (!file.exists()) {
			if (copyListsner != null) {
				copyListsner.onFaile();
			}
			return false;
		}

		try {
			if (copyListsner != null) {
				copyListsner.onStart();
			}
			long sumlength = filefrom.length();
			long copiednum = 0;
			FileInputStream fis = new FileInputStream(filefrom);
			FileOutputStream outputStream = new FileOutputStream(file);
			int byteCount = 512;
			int readnum = 0;
			byte[] buffer = new byte[byteCount];
			try {
				while ((readnum = (fis.read(buffer))) != -1) {
					outputStream.write(buffer, 0, readnum);
					copiednum = copiednum + readnum;
					if (copyListsner != null) {
						copyListsner.onProgress(copiednum, sumlength);
					}
				}

				if (copyListsner != null) {
					copyListsner.onProgress(sumlength, sumlength);
					copyListsner.onEnd();
				}
				fis.close();
				outputStream.close();
				return true;
			} catch (IOException e) {

				e.printStackTrace();
			} finally {

			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		if (copyListsner != null) {
			copyListsner.onFaile();
		}

		return false;
	}

	public interface FileCopyListsner {
		/**
		 * 开始复制
		 */
		public void onStart();

		/**
		 * 复制失败
		 */
		public void onFaile();

		/**
		 * 复制结束
		 */
		public void onEnd();

		/**
		 * @param copiednums 已复制长度
		 * @param sumnums 总长度
		 */
		public void onProgress(long copiednums, long sumnums);
	}
}
