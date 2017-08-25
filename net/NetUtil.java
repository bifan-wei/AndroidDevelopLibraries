package com.hw.libraries.net;

import com.lancoo.logger.ExceptionLoger;
import com.lancoo.logger.RequestLogBean;
import com.lancoo.logger.RequestLoger;
import com.lancoo.utils.TimeUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * TODO
 * --------------------
 * author: huangwei
 * 2017年8月25日上午10:36:43
 */
public class NetUtil {

    public static final int REQUEST_TYPE_POST = 1;
    public static final int REQUEST_TYPE_GET = 2;

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/json; charset=utf-8");// 设置MediaType
    private static final OkHttpClient client;
    private static final long cacheSize = 1024 * 1024 * 20;// 缓存文件最大限制大小20M
    // private static String cachedirectory = HwConstant.SystemFilePathroot +
    // "/caches"; // 设置缓存文件路径

    static {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(8, TimeUnit.SECONDS); // 设置连接超时时间
        builder.writeTimeout(8, TimeUnit.SECONDS);// 设置写入超时时间
        builder.readTimeout(8, TimeUnit.SECONDS);// 设置读取数据超时时间
        builder.retryOnConnectionFailure(true);// 设置进行连接失败重试
        // builder.cache(cache);// 不设置缓存
        client = builder.build();

    }

    private static RequestLogBean newLogBean(String url, String params, int requesttype) {
        RequestLogBean bean = new RequestLogBean();
        bean.Url = url;
        bean.Params = params;
        bean.Time = TimeUtil.getCurrentDateString();
        bean.RequestType = requesttype;
        return bean;
    }

    /**
     * @param url                    请求地址
     * @param responseListener       返回的ResquestResponse不会是null
     * @param cache_maxAge_inseconds 缓存最大生存时间，单位为秒
     * @return 当前call
     * --------------------
     * TODO get请求，并缓存请求数据，返回的是缓存数据，注意，如果超出了maxAge，缓存会被清除，回调onfail
     * --------------------
     */
    public static Call DoGetAndCache(String url, int cache_maxAge_inseconds,
                                     final NetWorkResponseListener responseListener) {

        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().maxAge(cache_maxAge_inseconds, TimeUnit.SECONDS).build())
                .url(url).build();

        Call call = client.newCall(request);
        startrequest(newLogBean(url, "", REQUEST_TYPE_GET), call, responseListener, REQUEST_TYPE_GET);
        return call;
    }

    /**
     * get请求 ,只获取返回网络请求数据，不进行缓存
     *
     * @param url
     * @param responseListener 返回的ResquestResponse不会是null
     */
    public static Call DoGetOnlyNet(String url, final NetWorkResponseListener responseListener) {
        return DoGetAndCache(url, 0, responseListener);
    }

    /**
     * get请求, 没有超过过时时间StaleTime的话，返回缓存数据，否则重新去获取网络数据，StaleTime限制了默认数据fresh时间
     *
     * @param url
     * @param staletime        缓存过时时间，秒为单位
     * @param responseListener 数据回调接口 返回的ResquestResponse不会是null
     */
    public static Call DoGetInStaleTime(String url, int staletime, final NetWorkResponseListener responseListener) {
        Request request = new Request.Builder()
                .cacheControl(new CacheControl.Builder().maxStale(staletime, TimeUnit.SECONDS).build()).url(url)
                .build();

        Call call = client.newCall(request);
        startrequest(newLogBean(url, "", REQUEST_TYPE_GET), call, responseListener, REQUEST_TYPE_GET);
        return call;
    }

    /**
     * get请求, 只使用缓存，注意，如果是超出了staletime或者超出了maxAge的话会返回504，否则就返回缓存数据
     * @param url
     * @param responseListener 数据回调接口 返回的ResquestResponse不会是null
     */
    public static Call DoGetOnlyCache(String url, final NetWorkResponseListener responseListener) {
        Request request = new Request.Builder().cacheControl(new CacheControl.Builder().onlyIfCached().build()).url(url)
                .build();

        Call call = client.newCall(request);
        startrequest(newLogBean(url, "", REQUEST_TYPE_GET), call, responseListener, REQUEST_TYPE_GET);
        return call;
    }

    /**
     * post表单的
     *
     * @param url
     * @param form             不能为null
     * @param responseListener 返回的ResquestResponse不会是null
     */
    public static Call PostForm(String url, Map<String, String> form, final NetWorkResponseListener responseListener) {

        Set<String> keys = form.keySet();
        Builder formbulider = new FormBody.Builder();
        for (String key : keys) {
            formbulider.add(key, form.get(key));
        }
        FormBody formBody = formbulider.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = client.newCall(request);
        startrequest(newLogBean(url, form.toString(), REQUEST_TYPE_POST), call, responseListener, REQUEST_TYPE_POST);
        return call;

    }

    /**
     * post字符串
     *
     * @param url              请求地址
     * @param bodydata         post字符串数据
     * @param responseListener 返回的ResquestResponse不可以是null
     */
    public static Call PostString(String url, String bodydata, final NetWorkResponseListener responseListener) {
        Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, bodydata))
                .addHeader("context", "mobile").addHeader("version", "1").build();
        Call call = client.newCall(request);

        startrequest(newLogBean(url, bodydata, REQUEST_TYPE_POST), call, responseListener, REQUEST_TYPE_POST);
        return call;
    }

    private static void startrequest(final RequestLogBean logBean, final Call call0,
                                     final NetWorkResponseListener responseListener, int requesttype) {
        final ResquestResponse response1 = new ResquestResponse();
        try {
            call0.enqueue(new Callback() {
                @Override
                public void onFailure(Call arg0, IOException arg1) {
                    if (arg0.isCanceled()) {
                        response1.errormessage = "请求已取消";//
                        response1.isCanceled = true;
                    } else {
                        response1.errormessage = "请求失败了,onFailure";
                    }
                    if (arg1 != null) {
                        Loger.e("IOException", arg1.toString() + "");
                        Throwable t = arg1.getCause();
                        if (t != null) {
                            if (t instanceof SocketTimeoutException) {
                                response1.IsTimeout = true;
                            } else if (t instanceof UnknownHostException) {
                                response1.UnKownHost = true;
                            }

                        }
                    }
                    logBean.ExtraInfo = response1.errormessage;
                    logBean.ResPonse = "";
                    RequestLoger.log(logBean);
                    Loger.e("startrequest", logBean.toString() + "");
                    responseListener.onResponse(response1);

                }

                @Override
                public void onResponse(Call arg0, Response response) throws IOException {

                    response1.Responsecode = response.code();
                    logBean.ResPonseCode = response1.Responsecode;

                    if (!arg0.isCanceled()) {

                        if (response.isSuccessful()) {
                            response1.isSucessful = true;
                            response1.Data = response.body().string();
                            logBean.IsSucess = true;
                            logBean.ResPonse = response1.Data;

                        } else {

                            response1.errormessage = "response失败了，连接状态码为：" + response1.Responsecode + ",msg:"
                                    + StatusCodeMsgTranslater.getMsg(response1.Responsecode);
                            logBean.ErrorMessage = response1.errormessage;

                        }
                    } else {
                        response1.errormessage = "请求取消了";
                    }

                    RequestLoger.log(logBean);
                    responseListener.onResponse(response1);
                }

            });

        } catch (Exception e) {

            e.printStackTrace();
            response1.errormessage = "请求未知异常";
            logBean.ErrorMessage = response1.errormessage;
            RequestLoger.log(logBean);
            responseListener.onResponse(response1);

        }
    }

    /**
     * @param url
     * @return 如果超时返回-3,url格式异常返回-2
     * --------------------
     * TODO 获取文件ContentLength，注意，这个不是同步的
     * --------------------
     */
    public static int getFileContentLength(final String url) {
        URLConnection url1 = null;
        try {
            url1 = new URL(url).openConnection();
            url1.setConnectTimeout(8 * 1000);
            url1.setReadTimeout(1 * 1000);
            int length = url1.getContentLength();
            return length;

        } catch (MalformedURLException e) {
            ExceptionLoger.log("url格式错误", "MalformedURLException in DownLoadFileinAsy1");
            e.printStackTrace();
            return -2;

        } catch (IOException e) {
            ExceptionLoger.log("超时连接", "发生在访问：" + url);
            e.printStackTrace();
            return -3;
        }

    }

    /**
     * @param url
     * @return --------------------
     * TODO 异步获取文件大小
     * --------------------
     */
    public static void getFileContentLengthinAsy(final String url, final INetListener INetListener) {
        new Thread(new Runnable() {
            public void run() {

                int length = getFileContentLength(url);
                if (length == -3) {
                    INetListener.onFaile("连接超时了");
                } else if (length == -2) {
                    INetListener.onFaile("url格式不正确");
                } else {
                    INetListener.onSucess(length);
                }

            }
        }).start();

    }


    public interface INetListener {//网络请求监听

        /**
         * @param errormessage 失败信息
         */
        void onFaile(String errormessage);

        /**
         * --------------------
         * TODO 下载完成后才会回调，这是下载进度监听已经停止
         * --------------------
         */
        void onSucess(Object object);
    }

}
