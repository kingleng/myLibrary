package com.example.leng.myapplication2.ui.server.videoCache;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.leng.myapplication2.ui.server.videoCache.VideoCacheServer.CONNECTION;
import static com.example.leng.myapplication2.ui.server.videoCache.VideoCacheServer.HOST;
import static com.example.leng.myapplication2.ui.server.videoCache.VideoCacheServer.HOST_PORT;
import static com.example.leng.myapplication2.ui.server.videoCache.VideoCacheServer.IF_RANGE;
import static com.example.leng.myapplication2.ui.server.videoCache.VideoCacheServer.PROXY_HOST;
import static com.example.leng.myapplication2.ui.server.videoCache.VideoCacheServer.REAL_HOST_NAME;

public class HttpRequest {

    String realHost = "";
    int realPort = 80;

    public String method;
    public String url;
    public String protocol;
    public Map<String, String> headers = new HashMap<>();

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public static HttpRequest parse(InputStream local_in) throws IOException {
        HttpRequest httpRequest = new HttpRequest();

        StringBuilder sb = new StringBuilder();
        boolean isFirstLine = true;

        while (true) {
            int charRead = local_in.read();
            if (charRead == -1) {
                break;
            }
            sb.append((char) charRead);
            if (sb.charAt(sb.length() - 1) == '\n') {
                if (sb.length() <= 2) {
                    sb.delete(0, sb.length());
                    break;
                } else {
                    String headLine = sb.toString().replaceAll("\\r\\n", "");
                    if (isFirstLine) {
                        isFirstLine = false;
                        String[] s = headLine.split(" ");
                        if (s.length == 3) {
                            httpRequest.setMethod(s[0]);
                            httpRequest.setUrl(s[1]);
                            httpRequest.setProtocol(s[2]);
                        }
                    } else {
                        String[] split = headLine.split(":");
                        String key = split[0];
                        String value = headLine.substring(headLine.indexOf(key) + key.length() + 1);
                        httpRequest.getHeaders().put(key, value);
                    }
                    sb.delete(0, sb.length());

                }
            }

        }

        String url = httpRequest.url;
        Matcher matcher = Pattern.compile(REAL_HOST_NAME + "=([^&]*)").matcher(url);

        if (!matcher.find() || matcher.groupCount() < 1) {
            return errorRequest();
        } else {
            String realHostName = matcher.group(1);
            int realHostPort = 80;
            if (realHostName.contains(":")) {
                String[] split = realHostName.split(":");
                realHostName = split[0];
                realHostPort = Integer.parseInt(split[1]);
            }

            httpRequest.realHost = realHostName;
            httpRequest.realPort = realHostPort;


            String proxyHost = httpRequest.getHost();
            if (proxyHost != null) {
                proxyHost = proxyHost.trim();
            }

            httpRequest.getHeaders().put(PROXY_HOST, proxyHost);
            httpRequest.getHeaders().put(HOST, realHostName);
            httpRequest.getHeaders().put(HOST_PORT, String.valueOf(realHostPort));
            httpRequest.getHeaders().remove(IF_RANGE);
            httpRequest.getHeaders().put(CONNECTION, "close");

        }

        return httpRequest;
    }

    public String getHost() {
        return headers.get(HOST);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeadText() {
        StringBuilder text = new StringBuilder(method + " " + url + " " + protocol + "\r\n");
        for (Map.Entry<String, String> e : headers.entrySet()) {
            text.append(e.getKey()).append(": ").append(e.getValue()).append("\r\n");
        }
        text.append("\r\n");

        return text.toString();
    }

    private static HttpRequest errorRequest() {
        HttpRequest response = new HttpRequest();
        response.setProtocol("HTTP/1.1");
//            response.setStatusCode(404);
//            response.setStatusString("NoProxyHost");
        HashMap<String, String> headers = new HashMap<>();
        headers.put(CONNECTION, "close");
        return response;
    }
}