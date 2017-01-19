package com.suntrans.lanzhouwh.api;

import com.suntrans.lanzhouwh.App;
import com.suntrans.lanzhouwh.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Looney on 2016/12/15.
 */

public class RetrofitHelper {

//    public static final String BASE_URL = "http://www.suntrans.net:8956";
    public static final String BASE_URL = "http://yunapp.egird.com";

    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
    }
    public static Api getLoginApi() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }

    public static Api getApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }


    private static void initOkHttpClient() {
        Interceptor netInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String header = App.getSharedPreferences().getString("access_token","-1");
                header = "Bearer "+header;
                LogUtil.i(header);
                Request original = chain.request();
                Request request= original.newBuilder()
                        .header("Authorization", header)
                        .method(original.method(), original.body())
                        .build();
                Response response = chain.proceed(request);
//                InputStream stream = response.body().byteStream();
//                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//                StringBuilder sb = new StringBuilder();
//                String line="";
//                while ((line = reader.readLine())!=null){
//                    sb.append(line);
//                }
//                System.out.println(sb.toString());
                return response;
            }
        };



        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient==null){
                    mOkHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(netInterceptor)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }


//    private static void initOkHttpClient() {
//
//        Interceptor netInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//
//                Response response=null;
//                if (request.method().equals("POST")){
//                    if (request.body() instanceof FormBody){
//                        FormBody.Builder builder=new FormBody.Builder();
//                        FormBody body = (FormBody) request.body();
//                        for (int i=0;i<body.size();i++){
//                            builder.addEncoded(body.encodedName(i), body.encodedValue(i));
//                        }
//                        body = builder
//                                .addEncoded("appkey", "suntrans2016")
//                                .build();
//                        request = request.newBuilder().post(body).build();
//                    }
//                    response= chain.proceed(request);
//
//                }else if (request.method().equals("GET")){
//
//                    HttpUrl.Builder builder = request.url()
//                            .newBuilder()
//                            .scheme(request.url().scheme())
//                            .host(request.url().host())
//                            .addQueryParameter("appkey","suntrans2016");
//                    request = request.newBuilder()
//                            .url(builder.build())
//                            .build();
//                    if (!UiUtils.isNetworkAvailable()){
//                        request = request.newBuilder()
//                                .cacheControl(CacheControl.FORCE_CACHE)
//                                .build();
//                        System.out.println("暂无网络");
//                    }
//
//                    response= chain.proceed(request);
//
//                    if (UiUtils.isNetworkAvailable()){
//                        int maxAge = 60 * 60*3; // read from cache for 1 minute
//                        response.newBuilder()
//                                .removeHeader("Pragma")
//                                .header("Cache-Control", "public, max-age=" + maxAge)
//                                .build();
//                    }else {
//                        int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
//                        response.newBuilder()
//                                .removeHeader("Pragma")
//                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                                .build();
//                    }
//                }
//
//
//
//
//                return response;
//            }
//        };
//
//
//
//        if (mOkHttpClient == null) {
//            //设置Http缓存
//            synchronized (RetrofitHelper.class) {
//                if (mOkHttpClient==null){
//                    Cache cache = new Cache(new File(App.getApplication().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);
//                    mOkHttpClient = new OkHttpClient.Builder()
//                            .addInterceptor(netInterceptor)
//                            .cache(cache)
//                            .retryOnConnectionFailure(true)
//                            .connectTimeout(15, TimeUnit.SECONDS)
//                            .build();
//                }
//            }
//        }
//    }



}
