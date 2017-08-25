package com.hw.libraries.utils;

import java.text.DecimalFormat;

/**
 * 数据格式化工具
 */
public class FormatUtil {

	/**
	 *@param floatv 保留两位小数点，舍入为四舍五入
	 *@return
	 */
	public static float getFloat_KeepTwoDecimalplaces(float floatv) {
		String value = getFloatForma_Keeptwodecimalplaces().format(floatv);
		float a = Float.valueOf(value);
		return a;
	}

	/**
	 *@param doublev 保留两位小数点，舍入为四舍五入
	 *@return
	 */
	public static float getFloat_KeepTwoDecimalplaces(Double doublev) {
		String value = getFloatForma_Keeptwodecimalplaces().format(doublev);
		float a = Float.valueOf(value);
		return a;
	}
	
	/**
	 *@param floatv 保留1位小数点，舍入为四舍五入
	 *@return
	 */
	public static float getFloat_KeepOneDecimalplaces(float floatv) {
		String value = getFloatForma_KeepOnedecimalplaces().format(floatv);
		float a = Float.valueOf(value);
		return a;
	}

	/**
	 *@param doublev 保留1位小数点，舍入为四舍五入
	 *@return
	 */
	public static float getFloat_KeepOneDecimalplaces(Double doublev) {
		String value = getFloatForma_KeepOnedecimalplaces().format(doublev);
		float a = Float.valueOf(value);
		return a;
	}
	
	/**
	 *@param floatv
	 *@return 舍入为四舍五入
	 *--------------------
	 */
	public static int getInt(float floatv) {
		String value = getIntForma().format(floatv);
		int a = Integer.valueOf(value);
		return a;
	}

	
	/**
	 *@param doublev
	 *@return 舍入为四舍五入
	 */
	public static int getInt(Double doublev) {
		String value = getIntForma().format(doublev);
		int a = Integer.valueOf(value);
		return a;
	}
	
	private static DecimalFormat getFloatForma_Keeptwodecimalplaces() {
		return new DecimalFormat("##0.00");
	}
	
	private static DecimalFormat getFloatForma_KeepOnedecimalplaces() {
		return new DecimalFormat("##0.0");
	}

	private static DecimalFormat getIntForma() {
		return new DecimalFormat("##0");

	}
}
