package com.suntrans.lanzhouwh.websocket;

import android.util.Log;


import com.suntrans.lanzhouwh.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by Looney on 2017/4/29.
 */

public final class WebSocketWrapper extends WebSocketListener {
    private final String TAG = this.getClass().getSimpleName();
    private final String url = "ws://wanhua.suntrans.net:8767";
    private okhttp3.WebSocket socket;
    private final OkHttpClient client;
    private final Request request;

    public WebSocketWrapper() {
        client = new OkHttpClient.Builder().readTimeout(3000, TimeUnit.MILLISECONDS).build();
        request = new Request.Builder().url(url).build();
    }

    @Override
    public void onOpen(okhttp3.WebSocket webSocket, Response response) {
        this.socket = webSocket;
        if (onReceiveListener != null)
            onReceiveListener.onOpen();
    }

    @Override
    public void onMessage(okhttp3.WebSocket webSocket, String text) {
        if (onReceiveListener != null)
            onReceiveListener.onMessage(text);

    }

    @Override
    public void onMessage(okhttp3.WebSocket webSocket, ByteString bytes) {
        if (onReceiveListener != null)
            onReceiveListener.onMessage(bytes.toString());
    }

    @Override
    public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
        if (onReceiveListener != null)
            onReceiveListener.onFailure(t);
    }

    /**
     * 初始化WebSocket服务器
     */
    public void connect() {
        socket = null;
        socket = client.newWebSocket(request, this);
    }


    @Override
    public void onClosing(okhttp3.WebSocket webSocket, int code, String reason) {
        socket = null;
        LogUtil.e("onClosing", code + "," + reason);
        super.onClosing(webSocket, code, reason);
    }

    /**
     * @param s
     * @return
     */
    public boolean sendMessage(String s) {
        if (socket != null) {
            LogUtil.i(TAG, "发送指令:" + s);
            return socket.send(s);
        } else {
            return false;
        }
    }

    public void closeWebSocket() {
        if (socket != null)
            socket.close(1000, "主动关闭");
        socket = null;
        onReceiveListener = null;
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
        Log.e("close", "关闭成功");
    }

    /**
     * 获取全局的ChatWebSocket类
     *
     * @return ChatWebSocket
     */

    private onReceiveListener onReceiveListener;

    public void setOnReceiveListener(WebSocketWrapper.onReceiveListener onReceiveListener) {
        this.onReceiveListener = onReceiveListener;
    }

    public interface onReceiveListener {

        void onMessage(String s);

        void onFailure(Throwable t);

        void onOpen();
    }


}
