package com.example.leng.myapplication2.ui.server.videoCache;

import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static com.example.leng.myapplication2.ui.server.videoCache.VideoCacheServer.CONNECTION;
import static com.example.leng.myapplication2.ui.server.videoCache.VideoCacheServer.HOST;

public class HttpResponse {

    private int statusCode;
    private String statusString;
    public String protocol;
    public Map<String, String> headers = new HashMap<>();

    InputStream inputStream;
    Socket socket;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public static HttpResponse parse(InputStream local_in) {
        HttpResponse response = new HttpResponse();


        StringBuilder sb = new StringBuilder();
        boolean isFirstLine = true;
        try {
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
                            if (s.length >= 2) {
                                response.setProtocol(s[0]);
                                response.setStatusCode(Integer.parseInt(s[1]));
                                String statusString = headLine.substring(headLine.indexOf(s[1]) + s[1].length() + 1);
                                response.setStatusString(statusString);
                            }
                        } else {
                            String[] split = headLine.split(":");
                            String key = split[0].trim();
                            String value = headLine.substring(headLine.indexOf(key) + key.length() + 1);
                            response.getHeaders().put(key, value);

                        }
                        sb.delete(0, sb.length());

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String getHost() {
        return headers.get(HOST);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeadText() {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append(" ").append(statusCode).append(" ").append(statusString).append("\r\n");
        for (Map.Entry<String, String> e : headers.entrySet()) {
            sb.append(e.getKey()).append(": ").append(e.getValue()).append("\r\n");
        }
        sb.append("\r\n");
        return sb.toString();
    }

    private static HttpResponse errorRequest() {
        HttpResponse response = new HttpResponse();
        response.setProtocol("HTTP/1.1");
            response.setStatusCode(404);
            response.setStatusString("NoProxyHost");
        HashMap<String, String> headers = new HashMap<>();
        headers.put(CONNECTION, "close");
        return response;
    }

}