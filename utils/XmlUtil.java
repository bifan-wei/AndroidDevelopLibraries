package com.lancoo.utils;

import android.util.Xml;

import com.lancoo.logger.ExceptionLoger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *TODO xml格式字符串与实体转化类
 *--------------------
 * 2017年8月25日
 */
public class XmlUtil {

	/**
	 * @param xmldata 要生成xml的数据 xmldata不能为null      
	 * @param starttag 每个结构体的开始与结束tag        
	 * @return 如果xmldata的size为0，返回"",否则返回xml格式字符串 
	 * -------------------- 
	 * TODO pull方式创建xml字符串结构体       
	 *--------------------
	 *2017年1月16日上午9:52:54     
	 */
	public static String pullXMLCreate(List<Object> xmldata, String starttag) {

		if (xmldata == null)
			throw new NullPointerException("xmldata can not be null");

		int size = xmldata.size();
		if (size == 0) {
			return "";
		}

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();//用这个可以设置编码

		try {

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlSerializer xmlSerializer = factory.newSerializer();
			xmlSerializer.setOutput(outputStream,"utf-8"); // 保存创建的xml

			for (int i = 0; i < size; i++) {
				Object obj = xmldata.get(i);
				pullXMLCreatefromObject(xmlSerializer, obj, starttag);
			}

		} catch (XmlPullParserException e) { // XmlPullParserFactory.newInstance
			e.printStackTrace();
			ExceptionLoger.log("xmlscreate", "XmlPullParserException");
		} catch (IllegalArgumentException e) { // xmlSerializer.setOutput
			e.printStackTrace();
			ExceptionLoger.log("xmlscreate", "IllegalArgumentException");
		} catch (IllegalStateException e) { // xmlSerializer.setOutput
			e.printStackTrace();
			ExceptionLoger.log("xmlscreate", "IllegalStateException");
		} catch (IOException e) { // xmlSerializer.setOutput
			e.printStackTrace();
			ExceptionLoger.log("xmlscreate", "IOException");
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionLoger.log("xmlscreate", "Exception");
		}
		
		try {
			return outputStream.toString("utf-8");
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
         return "";

	}

	/**
	 *@param xmldata 实体数据
	 *@param starttag 开始标签
	 *@return 不会返回null
	 *--------------------
	 *TODO pull方式创建xml字符串结构体
	 *--------------------
	 * 2017年1月16日下午4:22:22
	 */
	public static String pullXMLCreate(Object xmldata, String starttag) {

		if (xmldata == null)
			throw new NullPointerException("xmldata can not be null");

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();//用这个可以设置编码

		try {

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlSerializer xmlSerializer = factory.newSerializer();
			xmlSerializer.setOutput(outputStream, "utf-8"); // 保存创建的xml
			pullXMLCreatefromObject(xmlSerializer, xmldata, starttag);

		} catch (XmlPullParserException e) { // XmlPullParserFactory.newInstance
			e.printStackTrace();
			ExceptionLoger.log("xmlcreate", "XmlPullParserException");
		} catch (IllegalArgumentException e) { // xmlSerializer.setOutput
			e.printStackTrace();
			ExceptionLoger.log("xmlcreate", "IllegalArgumentException");
		} catch (IllegalStateException e) { // xmlSerializer.setOutput
			e.printStackTrace();
			ExceptionLoger.log("xmlcreate", "IllegalStateException");
		} catch (IOException e) { // xmlSerializer.setOutput
			e.printStackTrace();
			ExceptionLoger.log("xmlcreate", "IOException");
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionLoger.log("xmlcreate", "Exception");
		}
		

		try {
			return outputStream.toString("utf-8");
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
         return "";
	}

	private static void pullXMLCreatefromObject(XmlSerializer xmlSerializer, Object xmldata, String starttag)
			throws IllegalArgumentException, IllegalStateException, IOException {
		Object obj = xmldata;

		List<Field> filds = getPublicFields(obj.getClass());

		xmlSerializer.startTag("", starttag); // 创建单个结构体开始节点

		for (Field f : filds) {
			String tagname = f.getName();
			Object tagvalue = "";
			try {
				// 获得当前属性的类型和值
				// 类型的话如果是基本类型，会自动装箱
				tagvalue = f.get(obj);
				// 判断各种类型，调用各种类型的put方法将数据存储进去
				if (tagvalue instanceof String) {
					tagvalue = (String) tagvalue;

				} else if (tagvalue instanceof Integer) {
					tagvalue = (Integer) tagvalue;
				} else if (tagvalue instanceof Float) {
					tagvalue = (Float) tagvalue;
				} else if (tagvalue instanceof Long) {
					tagvalue = (Long) tagvalue;
				} else if (tagvalue instanceof Boolean) {
					tagvalue = (Boolean) tagvalue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (tagvalue == null) {
				tagvalue = "";
			}

			xmlSerializer.startTag("", tagname);
			xmlSerializer.text(tagvalue + "");
			xmlSerializer.endTag("", tagname);

		}

		xmlSerializer.endTag("", starttag);// 创建单个结构体结束节点
		xmlSerializer.endDocument();
	}

	private static <T> List<Field> getPublicFields(Class<?> clazz) {

		// 用来存储clazz中用public修饰的属性的list
		List<Field> list = new ArrayList<Field>();
		// 获得clazz中所有用public修饰的属性
		Field[] fields = clazz.getFields();
		// 将fields加入到list中
		for (int i = 0; i < fields.length; i++) {
			list.add(fields[i]);
		}
		return list;

	}

	/**
	 * @param xmlStrxml字符串
	 * @param objecttype 对应的数据结构体
	 * @return objecttype传入错误或导致IllegalAccessException，
	 *         objecttype与xmlStr的属性不对应会返回new出来的objecttype
	 *         objecttype与xmlStr的属性运行忽略掉大小写 
	 *         
	 -------------------- TODO 从单个xml格式数据中获取返回bean
	 *         2017年1月16日上午10:25:19
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public static <T> T getObjectFromXmlObject(String xmlStr, Class<T> objecttype)
			throws XmlPullParserException, IOException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(new StringReader(xmlStr));
		int event = parser.getEventType();

		T t = null;
		try {
			t = objecttype.newInstance();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		}

		if (t == null)
			return t;

		List<Field> publicFields = getPublicFields(objecttype);
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_DOCUMENT:

				break;
			case XmlPullParser.START_TAG:

				String name = parser.getName();
				event = parser.next();
				String value = parser.getText();

				for (Field field : publicFields) {
					// 获得属性名
					String n = field.getName();
					// 如果属性名与键相同
					if (n.equalsIgnoreCase(name)) {

						try {
							field.set(t, value);
						} catch (IllegalAccessException e) {

							e.printStackTrace();
						} catch (IllegalArgumentException e) {

							e.printStackTrace();
						}
						break;
					}
				}

				break;
			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}
		return t;

	}

	/**
	 * @param xmlStr 多个xml结构体字符串         
	 * @param objecttype单个结构体类型
	 * @param starttag 单个xml结构体的开始与结束节点名称
	 * @return 不会返回null，解析出错可能返回无数据
	 *  -------------------- 
	 *  TODO 多个结构体的xml数据解析，注意的是，objecttype不支持复杂的类型，只支持基本类型格式与字符串
	 *  -------------------- 
	 *  author: huangwei 
	 *  2017年1月16日上午10:39:02
	 */
	public static <T> List<T> getObjectsFromXmlObjects(String xmlStr, Class<T> objecttype, String starttag) {

		XmlPullParser parser = Xml.newPullParser();
		List<T> datas = new ArrayList<T>();

		List<Field> publicFields = getPublicFields(objecttype);
		T t = null;

		String key = "";

		String value = "";
		try {
			parser.setInput(new StringReader(xmlStr));

			int event = 0;

			while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:// 解析开始开始时触发

					break;
				case XmlPullParser.START_TAG:// 标签开始时触发

					key = parser.getName();

					if (key.equals(starttag)) {// 判断到是开始tag

						try {// 重新创建一个实例
							t = objecttype.newInstance();
						} catch (InstantiationException e) {

							e.printStackTrace();
						} catch (IllegalAccessException e) {

							e.printStackTrace();
						}

						break;

					} else {

						if (t != null) {// 不出错才处理
							value = parser.nextText();
							value = value == null ? "" : value;// 如果是null的话，变为空字符

							for (Field field : publicFields) {
								// 获得属性名
								String n = field.getName();
								// 如果属性名与键相同
								if (n.equalsIgnoreCase(key)) {

									Object tagvalue = null;
									try {

										// 获得当前属性的类型和值
										// 类型的话如果是基本类型，会自动装箱
										tagvalue = field.get(t);
										// 判断各种类型，调用各种类型的put方法将数据存储进去

										if (tagvalue instanceof String || tagvalue == null) {// 字符串的话可能出现tagvalue==null
											tagvalue = value;
										} else if (tagvalue instanceof Integer) {
											tagvalue = Integer.valueOf(value);
										} else if (tagvalue instanceof Float) {
											tagvalue = Float.valueOf(value);
										} else if (tagvalue instanceof Long) {
											tagvalue = Long.valueOf(value);
										} else if (tagvalue instanceof Boolean) {
											tagvalue = Boolean.valueOf(value);
										} else if (tagvalue instanceof Double) {
											tagvalue = Double.valueOf(value);
										}

										field.set(t, tagvalue);

									} catch (Exception e) {
										e.printStackTrace();

									}

								}

							}

						}
					}

					break;
				case XmlPullParser.END_TAG:// 解析结束时触发
					key = parser.getName();
					if (key != null && key.equals(starttag)) {
						if (t != null) {
							datas.add(t);
						}
					}

					break;
				}

			}
		} catch (XmlPullParserException e1) {

			e1.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return datas;

	}

}
