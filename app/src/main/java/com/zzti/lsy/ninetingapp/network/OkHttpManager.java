package com.zzti.lsy.ninetingapp.network;


import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.ArrayMap;

import com.google.gson.JsonObject;
import com.zzti.lsy.ninetingapp.utils.SpUtils;
import com.zzti.lsy.ninetingapp.utils.UIUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by lsy on 2016/5/12.
 */
public final class OkHttpManager {
    private static OkHttpClient client;
    private final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final static String EMPTY_JSON = new JsonObject().toString();
    private final static Handler handler = new Handler(Looper.getMainLooper());

    private static Cache cache;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    static {
        //        File director = FileUtils.getCache();
        int maxSize = 10 * 1024 * 1024; // 10 MiB
        // http://facebook.github.io/stetho/
        //        cache = new Cache(director, maxSize);
        client = new OkHttpClient.Builder()
                //.addInterceptor(new LoggingInterceptor())
                //.addNetworkInterceptor(new StethoInterceptor())
                //                .addNetworkInterceptor(new CacheInterceptor2())
                //                .cache(cache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private static <T> void executeCall(final Request request, final OnResponse<T> onResponse) {
        if (onResponse == null) {
            throw new NullPointerException("OnResponse is null!");
        }
        final Call call = client.newCall(request);
        final String reqUrl = request.url()
                .toString();
        ThreadUtils.runOnBackThread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = call.execute();
                    if (response == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onResponse.onFailed(-1, "response null", reqUrl);
                                onResponse.onCompleted();
                            }
                        });
                        return;
                    }
                    if (response.isSuccessful()) {
                        String result = response.body()
                                .string();
                        final T t = onResponse.analyseResult(result);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onResponse.onSuccess(t);
                                onResponse.onCompleted();
                            }
                        });
                    } else {
                        final int code = response.code();
                        final String msg = response.message();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                onResponse.onFailed(code, msg, reqUrl);
                                onResponse.onCompleted();
                            }
                        });
                    }
                } catch (final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onResponse.onFailed(-1, e.getMessage(), reqUrl);
                            //                            onResponse.onError(e, reqUrl);
                            onResponse.onCompleted();
                        }
                    });
                } finally {
                    if (response != null) {
                        response.body()
                                .close();
                    }
                }
            }
        });
    }

    private static void addHeadersToRequest(ArrayMap<String, String> headers,
                                            Request.Builder builder) {
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = headers.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private static <T> void basePost(String url,
                                     ArrayMap<String, String> headers,
                                     JSONObject params,
                                     Object tag,
                                     OnResponse<T> onResponse) {
        final RequestBody reqBody = RequestBody.create(JSON,
                params == null
                        ? EMPTY_JSON
                        : params.toString());
        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null) {
            if (tag instanceof JsonObject) {
                throw new IllegalArgumentException("tag cannot be JsonObject.");
            }
            builder.tag(tag.getClass()
                    .getName());
        }
        addHeadersToRequest(headers, builder);
        Request request = builder.post(reqBody)
                .build();
        executeCall(request, onResponse);
    }

    public static <T> void postFormBody(String url,
                                        HashMap<String, String> params,
                                        Object tag,
                                        OnResponse<T> onResponse) {
        FormBody.Builder formBuild = new FormBody.Builder();

        if (!url.equals(Urls.POST_LOGIN_URL)) {
            formBuild.add("sessionId", SpUtils.getInstance().getString(SpUtils.SESSIONID, ""));
        }
        if (params != null) {
            // params是存放参数的ArrayMap
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            // 遍历参数集合，添加到请求体
            for (Map.Entry<String, String> entry : entrySet) {
                // addFormDataPart方法三个参数的方法，分别对应要提交的key，value
                formBuild.add(entry.getKey(), entry.getValue());
            }
        }

        // 构造Request对象，方法为POST
        Request.Builder reqBuilder = new Request.Builder().url(url)
                .post(formBuild.build());
        // 根据需要添加的header信息
        //        reqBuilder.addHeader("Authorization", "token"/*这里可以添加授权的头信息*/);
        // 设置tag
        if (tag != null) {
            reqBuilder.tag(tag.getClass()
                    .getName());
        }
        Request request = reqBuilder.build();
        executeCall(request, onResponse);

    }

    public static <T> void postForm(String url,
                                    HashMap<String, String> params,
                                    Object tag,
                                    OnResponse<T> onResponse) {

        // 构造Multipart请求体，并设置类型为Form
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        MultipartBody.Builder multiBuilder = new MultipartBody.Builder().setType(MultipartBody.);
        // params是存放参数的ArrayMap
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        // 遍历参数集合，添加到请求体
        for (Map.Entry<String, String> entry : entrySet) {
            File file = new File(entry.getValue());
            if (file.exists()) {
                // addFormDataPart方法三个参数的方法，分别对应要提交的key，value和文件对象
                multiBuilder.addFormDataPart(entry.getKey(),
                        entry.getValue(),
                        RequestBody.create(null, file));
            } else {
                // addFormDataPart方法三个参数的方法，分别对应要提交的key，value
                multiBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        // 构造Request对象，方法为POST
        Request.Builder reqBuilder = new Request.Builder().url(url)
                .post(multiBuilder.build());
        // 根据需要添加的header信息
        //        reqBuilder.addHeader("Authorization", "token"/*这里可以添加授权的头信息*/);
        // 设置tag
        if (tag != null) {
            reqBuilder.tag(tag.getClass()
                    .getName());
        }
        Request request = reqBuilder.build();
        executeCall(request, onResponse);
    }


    public static <T> void uploading(String url, HashMap<String, String> params, Object tag, OnResponse<T> onResponse) {
        // 构造Multipart请求体，并设置类型为Form
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // params是存放参数的ArrayMap
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        // 遍历参数集合，添加到请求体
        for (Map.Entry<String, String> entry : entrySet) {
            File file = new File(entry.getValue());
            String[] split = entry.getValue().split("/");
            if (file.exists()) {
                // addFormDataPart方法三个参数的方法，分别对应要提交的key，value和文件对象
                multiBuilder.addFormDataPart(entry.getKey(),
                        split[split.length - 1],
                        RequestBody.create(MEDIA_TYPE_PNG, file));
            }
        }
        // 构造Request对象，方法为POST
        Request.Builder reqBuilder = new Request.Builder().url(url)
                .post(multiBuilder.build());
        // 根据需要添加的header信息
        //        reqBuilder.addHeader("Authorization", "token"/*这里可以添加授权的头信息*/);
        // 设置tag
        if (tag != null) {
            reqBuilder.tag(tag.getClass()
                    .getName());
        }
        Request request = reqBuilder.build();
        executeCall(request, onResponse);

    }

    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public static <T> void postNoToken(String url,
                                       ArrayMap<String, String> headers,
                                       JSONObject params,
                                       Object tag,
                                       OnResponse<T> onResponse) {
        basePost(url, headers, params, tag, onResponse);
    }

    private static <T> void post(String url,
                                 ArrayMap<String, String> headers,
                                 JSONObject params,
                                 Object tag,
                                 OnResponse<T> onResponse) {
        if (headers == null) {
            headers = new ArrayMap<>();
        }
        //TODO 添加headers
//                headers.put("Authorization", ConstantValues.TOKEN_STRING + GlobalParams.token);
        basePost(url, headers, params, tag, onResponse);
    }

    public static <T> void post(String url, JSONObject params, Object tag, OnResponse<T> onResponse) {
        post(url, null, params, tag, onResponse);
    }

    public static <T> void post(String url, Object tag, OnResponse<T> onResponse) {
        post(url, null, null, tag, onResponse);
    }

    public static <T> void getNoToken(String url,
                                      ArrayMap<String, String> headers,
                                      ArrayMap<String, String> params,
                                      Object tag,
                                      final OnResponse<T> onResponse) {
        String url1 = handleUrl(url, params);
        Request.Builder builder = new Request.Builder().url(url1);
        if (tag != null) {
            if (tag instanceof ArrayMap) {
                throw new IllegalArgumentException("tag cannot be ArrayMap.");
            }
            builder.tag(tag.getClass()
                    .getName());
        }
        addHeadersToRequest(headers, builder);
        Request request = builder.get()
                .build();
        executeCall(request, onResponse);
    }

    private static String handleUrl(String url, ArrayMap<String, String> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        sb.append("?");
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static <T> void get(String url,
                               ArrayMap<String, String> headers,
                               ArrayMap<String, String> params,
                               Object tag,
                               final OnResponse<T> onResponse) {
        if (headers == null) {
            headers = new ArrayMap<>();
        }
        //TODO 添加headers
        //        headers.put("Authorization", ConstantValues.TOKEN_STRING + GlobalParams.token);
        getNoToken(url, headers, params, tag, onResponse);
    }

    public static <T> void get(String url,
                               ArrayMap<String, String> params,
                               Object tag,
                               OnResponse<T> response) {
        get(url, null, params, tag, response);
    }

    public static <T> void get(String url, Object tag, OnResponse<T> response) {
        get(url, null, null, tag, response);
    }

    public static <T> void put(String url, JsonObject params, Object tag, OnResponse<T> onResponse) {
        final RequestBody reqBody = RequestBody.create(JSON,
                params == null
                        ? EMPTY_JSON
                        : params.toString());
        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null) {
            if (tag instanceof JsonObject) {
                throw new IllegalArgumentException("tag cannot be JsonObject.");
            }
            builder.tag(tag.getClass()
                    .getName());
        }
        builder.addHeader("Authorization", "Token");
        Request request = builder.put(reqBody)
                .build();
        executeCall(request, onResponse);
    }

    public static <T> void delete(String url, Object tag, OnResponse<T> onResponse) {
        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null) {
            if (tag instanceof JsonObject) {
                throw new IllegalArgumentException("tag cannot be JsonObject.");
            }
            if (tag instanceof ArrayMap) {
                throw new IllegalArgumentException("tag cannot be ArrayMap.");
            }
            builder.tag(tag.getClass()
                    .getName());
        }
        builder.addHeader("Authorization", "Token");
        Request request = builder.delete()
                .build();
        executeCall(request, onResponse);
    }

    public static <T> void patch(String url,
                                 JsonObject params,
                                 Object tag,
                                 OnResponse<T> onResponse) {
        final RequestBody reqBody = RequestBody.create(JSON,
                params == null
                        ? EMPTY_JSON
                        : params.toString());
        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null) {
            if (tag instanceof JsonObject) {
                throw new IllegalArgumentException("tag cannot be JsonObject.");
            }
            builder.tag(tag.getClass()
                    .getName());
        }
        //TODO 添加headers
        //        builder.header("Authorization", ConstantValues.TOKEN_STRING + GlobalParams.token);
        Request request = builder.patch(reqBody)
                .build();
        executeCall(request, onResponse);
    }

    public static void download(String downloadUrl,
                                Object tag,
                                final String downloadPath,
                                final String fileName,
                                final DownloaderListener listener) {
        OkHttpClient tmpClient = client.newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain)
                            throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(
                                        originalResponse.body(),
                                        listener))
                                .build();
                    }
                })
                .build();

        File path = new File(downloadPath);
        final File file = new File(downloadPath, fileName);
        if (!path.exists()) {
            path.mkdirs();
        }
        Request.Builder builder = new Request.Builder().url(downloadUrl);
        if (tag != null) {
            if (tag instanceof JsonObject) {
                throw new IllegalArgumentException("tag cannot be JsonObject.");
            }
            if (tag instanceof ArrayMap) {
                throw new IllegalArgumentException("tag cannot be ArrayMap.");
            }
            builder.tag(tag.getClass()
                    .getName());
        }
        Request request = builder.build();
        //发送异步请求
        tmpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        listener.onFail(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response)
                            throws IOException {
                        //将返回结果转化为流，并写入文件
                        int len;
                        byte[] buf = new byte[2048];
                        InputStream inputStream = response.body()
                                .byteStream();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        while ((len = inputStream.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, len);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        inputStream.close();
                    }
                });
    }

    public static void cancel(Object tag) {
        if (tag == null) {
            client.dispatcher()
                    .cancelAll();
            return;
        }
        List<Call> calls = client.dispatcher()
                .queuedCalls();
        for (int i = calls.size() - 1; i >= 0; i--) {
            Call call = calls.get(i);
            if (call.request()
                    .tag()
                    .equals(tag.getClass()
                            .getName())) {
                call.cancel();
            }
        }
    }

    public abstract static class OnResponse<T> {
        /**
         * 解析服务器返回的数据,非主线程
         *
         * @param result 服务器返回的字符串
         * @return 所需要被解析成的类型
         */
        public abstract T analyseResult(String result);

        /**
         * 此方法只有服务器成功返回时回调
         *
         * @param t
         */
        public abstract void onSuccess(T t);

        public void onCompleted() {

        }

        /**
         * 网络请求失败的回调，包括网络错误请求结果，网络请求失败等
         *
         * @param code
         * @param msg
         */
        public void onFailed(int code, String msg, String url) {
            switch (code) {
                case -1:
                    UIUtils.showT("网络错误，请稍候重试");
                    break;
                case HttpURLConnection.HTTP_CLIENT_TIMEOUT:
                    UIUtils.showT("连接超时，请稍候重试");
                    break;

//                case HttpURLConnection.HTTP_INTERNAL_ERROR:
//                    UIUtils.showT("服务器忙，请稍候重试");
//                    break;

                case HttpURLConnection.HTTP_BAD_REQUEST:
                    UIUtils.showT("网络错误，请稍候重试");
                    break;

                default:
                    UIUtils.showT(msg);
                    break;
            }
        }
    }

    static class LoggingInterceptor
            implements Interceptor {
        @Override
        public Response intercept(Chain chain)
                throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();

            return response;
        }
    }

    /**
     * 拦截器一(有网和没有网都是先读缓存)
     * 设置max-age为60s之后，这60s之内无论有没有网,都读缓存。
     */
    static class CacheInterceptor
            implements Interceptor {
        @Override
        public Response intercept(Chain chain)
                throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            int maxAge = 60;
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
            return response;
        }
    }

    /**
     * 拦截器二(离线可以缓存，在线就获取最新数据)
     * only-if-cached:(仅为请求标头)请求:告知缓存者,我希望内容来自缓存，我并不关心被缓存响应,是否是新鲜的.
     * public:(仅为响应标头)响应:告知任何途径的缓存者,可以无条件的缓存该响应.
     */
    static class CacheInterceptor2
            implements Interceptor {
        @Override
        public Response intercept(Chain chain)
                throws IOException {
            //拦截reqeust
            Request request = chain.request();
            //判断网络连接状况
            if (!UIUtils.isNetworkConnected()) {
                //无网络时只从缓存中读取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);
            if (UIUtils.isNetworkConnected()) {
                // 有网络时 设置缓存超时时间1分钟
                int maxAge = 60; // read from cache for 1 minute
                response.newBuilder()
                        //清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .removeHeader("Pragma")
                        //设置缓存超时时间
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                response.newBuilder()
                        .removeHeader("Pragma")
                        //设置缓存策略，及超时策略
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }

    private static class ProgressResponseBody
            extends ResponseBody {
        private final ResponseBody responseBody;
        private final DownloaderListener downloaderListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody,
                                    DownloaderListener downloaderListener) {
            this.responseBody = responseBody;
            this.downloaderListener = downloaderListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }


        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;
                long startTime = System.nanoTime();

                @Override
                public long read(Buffer sink, long byteCount)
                        throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytesRead += bytesRead != -1
                            ? bytesRead
                            : 0;
                    float kb = totalBytesRead / 1024;
                    double second = (System.nanoTime() - startTime) * Math.pow(10, -9);
                    downloaderListener.update(totalBytesRead,
                            responseBody.contentLength(),
                            kb / second,
                            bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }


    public interface DownloaderListener {
        /**
         * @param bytesRead     已下载字节数
         * @param contentLength 总字节数
         * @param done          是否下载完成
         */
        void update(long bytesRead, long contentLength, double speed, boolean done);

        void onFail(Exception e);
    }
}
