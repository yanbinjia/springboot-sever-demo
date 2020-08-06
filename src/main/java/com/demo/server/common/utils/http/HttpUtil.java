/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-06T18:58:59.480+08:00
 */

package com.demo.server.common.utils.http;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public static final int CONNECT_TIMEOUT = 3 * 1000;// milliseconds
    public static final int READ_TIMEOUT = 3 * 1000;// milliseconds
    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36";

    public static String sendQueryByJsoup(String url, Map<String, String> param) {
        String queryStr = queryStr(url, param);
        try {
            Connection.Response response = Jsoup.connect(queryStr)
                    .userAgent(USER_AGENT)
                    .timeout(READ_TIMEOUT)
                    .ignoreContentType(true)
                    .execute();
            return response.body();
        } catch (IOException e) {
            logger.error("sendQueryByJsoup error. url={}", queryStr, e);
        }
        return "";
    }

    public static String sendGet(String url, Map<String, String> param) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        String queryStr = queryStr(url, param);
        try {
            logger.info("sendGet - {}", queryStr);
            URL realUrl = new URL(queryStr);
            URLConnection conn = realUrl.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", USER_AGENT);
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            logger.info("recv - {}", result);
        } catch (ConnectException e) {
            logger.error("调用HttpUtils.sendGet ConnectException, url={}", queryStr, e);
        } catch (SocketTimeoutException e) {
            logger.error("调用HttpUtils.sendGet SocketTimeoutException, url={}", queryStr, e);
        } catch (IOException e) {
            logger.error("调用HttpUtils.sendGet IOException, url={}", queryStr, e);
        } catch (Exception e) {
            logger.error("调用HttpsUtil.sendGet Exception, url={}", queryStr, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                logger.error("调用in.close Exception, url={}", queryStr, ex);
            }
        }
        return result.toString();
    }

    public static String sendPost(String url, Map<String, String> param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        String queryStr = queryStr(url, param);
        try {
            logger.info("sendPost - {}", url);
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", USER_AGENT);
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(paramStr(param));
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            logger.info("recv - {}", result);
        } catch (ConnectException e) {
            logger.error("调用HttpUtils.sendPost ConnectException, url={}", queryStr, e);
        } catch (SocketTimeoutException e) {
            logger.error("调用HttpUtils.sendPost SocketTimeoutException, url={}", queryStr, e);
        } catch (IOException e) {
            logger.error("调用HttpUtils.sendPost IOException, url={}", queryStr, e);
        } catch (Exception e) {
            logger.error("调用HttpsUtil.sendPost Exception, url={}", queryStr, e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                logger.error("调用in.close Exception, url={}", queryStr, ex);
            }
        }
        return result.toString();
    }

    public static String sendSSLPost(String url, Map<String, String> param) {
        StringBuilder result = new StringBuilder();
        String queryStr = queryStr(url, param);
        try {
            logger.info("sendSSLPost - {}", queryStr);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
            URL console = new URL(queryStr);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", USER_AGENT);
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String ret = "";
            while ((ret = br.readLine()) != null) {
                if (ret != null && !ret.trim().equals("")) {
                    result.append(new String(ret.getBytes("ISO-8859-1"), "utf-8"));
                }
            }
            logger.info("recv - {}", result);
            conn.disconnect();
            br.close();
        } catch (ConnectException e) {
            logger.error("调用HttpUtils.sendSSLPost ConnectException, url={}", queryStr, e);
        } catch (SocketTimeoutException e) {
            logger.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url={}", queryStr, e);
        } catch (IOException e) {
            logger.error("调用HttpUtils.sendSSLPost IOException, url={}", queryStr, e);
        } catch (Exception e) {
            logger.error("调用HttpsUtil.sendSSLPost Exception, url={}", queryStr, e);
        }
        return result.toString();
    }

    public static String queryStr(String url, Map<String, String> param) {
        return url + "?" + paramStr(param);
    }

    public static String paramStr(Map<String, String> param) {
        StringBuffer paramStrSb = new StringBuffer();
        if (param != null) {
            // 处理请求参数,对参数按照 key1=value1&key2=value2 的格式，并按照参数名ASCII字典序排序
            SortedMap<String, String> paramsSortedMap = new TreeMap<>(param);
            if (paramsSortedMap != null) {
                paramsSortedMap.forEach((k, v) -> {
                    if (paramStrSb.length() == 0) {
                        paramStrSb.append(k + "=" + v);
                    } else {
                        paramStrSb.append("&" + k + "=" + v);
                    }
                });
            }
        }
        return paramStrSb.toString();
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static void main(String[] args) throws IOException {
        Map<String, String> param = new HashMap<>();
        param.put("ip", "106.38.36.10");
        param.put("accessKey", "accessKey12313");
        String url = "http://ip.taobao.com/outGetIpInfo";

        System.out.println(HttpUtil.sendGet(url, param));
        System.out.println(HttpUtil.sendPost(url, param));
        System.out.println(HttpUtil.sendSSLPost(url, param));

        System.out.println(sendQueryByJsoup(url, param));
    }
}