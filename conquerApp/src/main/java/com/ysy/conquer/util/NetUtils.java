package com.ysy.conquer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;


/**
 * User: ysy
 * Date: 2015/11/11
 * Time: 14:20
 */
public class NetUtils {
    /**
     * 当前是否有网
     */
    public static boolean isOpenNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }

    /**
     * 检查是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    /**
     * 检查是否是WIFI
     */
    public static boolean isWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
        }
        return false;
    }

    /**
     * 检查是否是移动网络
     */
    public static boolean isMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }


    /**
     * GET请求方式访问api接口
     *
     * @param urlString
     * @param params
     * @return
     */
    public static String getRequest(String urlString, Map<String, String> params) {
        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15 * 3000);
            connection.setReadTimeout(15 * 3000);
            if (params != null && params.size() != 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    connection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            InputStream is = null;
            int status = connection.getResponseCode();
            if (status == 200) {
                String encoding = connection.getContentEncoding();
                if (encoding != null && "gzip".equalsIgnoreCase(encoding))
                    is = new GZIPInputStream(connection.getInputStream());
                else if (encoding != null
                        && "deflate".equalsIgnoreCase(encoding))
                    is = new InflaterInputStream(connection.getInputStream());
                else {
                    is = connection.getInputStream();
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buff = new byte[2048];
                int length;
                while ((length = is.read(buff)) != -1) {
                    out.write(buff, 0, length);
                }
                is.close();
                out.flush();
                out.close();
                String result = new String(out.toByteArray());
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static long expires(String second) {
        Long l = Long.valueOf(second);
        return l * 1000L + System.currentTimeMillis();
    }


}
