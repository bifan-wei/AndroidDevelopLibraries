package com.lancoo.net;


/**
 * 请求状态吗信息转化
 */
public class StatusCodeMsgTranslater {

	public static String getMsg(int statucode) {
		switch (statucode) {
		case -1:
			return "default coed,unkonwerror";		
		case 202:
			return "Accepted ";

		case 502:
			return "Bad Gateway ";
		case 400:
			return " Bad Request ";
		case 409:
			return " Conflict ";
		case 100:
			return "Continue ";
		case 201:
			return " Created ";
		case 417:
			return "Expectation Failed  ";
		case 424:
			return "Failed Dependency ";
		case 403:
			return " Forbidden  ";
		case 504:
			return "Gateway Timeout ";
		case 410:
			return "Gone ";
		case 505:
			return "HTTP Version Not Supported  ";

		case 507:
			return "Insufficient Storage ";
		case 500:
			return "Server Error ";
		case 411:
			return "Length Required ";
		case 423:
			return " Locked ";

		case 405:
			return "Method Not Allowed";
		case 301:
			return "Moved Permanently ";
		case 302:
			return "Moved Temporarily";
		case 207:
			return "Multi-Status ";
		case 300:
			return "Mutliple Choices ";
		case 204:
			return "No Content";
		case 203:
			return "Non Authoritative Information ";
		case 406:
			return "Not Acceptable ";
		case 404:
			return "Not Found ";
		case 501:
			return "Not Implemented ";
		case 304:
			return "Not Modified ";
		case 200:
			return "OK  ";
		case 206:
			return "Partial Content";
		case 402:
			return "Payment Required ";
		case 412:
			return "Precondition Failed ";
		case 102:
			return "Processing ";
		case 407:
			return "Proxy Authentication Required ";
		case 408:
			return "Request Timeout ";
		case 413:
			return "Request Entity Too Large";
		case 414:
			return "Request-URI Too Long ";
		case 416:
			return "Requested Range Not Satisfiable ";
		case 205:
			return "Reset Content ";
		case 303:
			return "See Other ";
		case 503:
			return "Service Unavailable  ";
		case 101:
			return "Switching Protocols";
		case 307:
			return "Temporary Redirect ";
		case 401:
			return "Unauthorized ";
		case 422:
			return "Unprocessable Entity";
		case 415:
			return "Unsupported Media Type";
		case 305:
			return "Use Proxy ";

		default:
			return "unknow statucoed";

		}
	}
}
