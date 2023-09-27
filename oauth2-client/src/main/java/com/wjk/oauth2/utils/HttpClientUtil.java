package com.wjk.oauth2.utils;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

    public static String doGet(String url) throws Exception {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;
        try {
            // 1. 创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 2. 创建GET请求方法实例
            HttpGet httpGet = new HttpGet(url);
            // 3. 调用HttpClient实例来执行GET请求方法，得到response
            response = httpClient.execute(httpGet);
            // 4. 读response，判断是否访问成功 2xx表示 成功。
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                // 对得到后的实例可以进行处理，例如读取回复体，读取html
                HttpEntity entity = response.getEntity();
                System.out.println(response);
                System.out.println("=======================");
                return EntityUtils.toString(entity);
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }

            // 6. 释放连接


        }catch (Exception e){
            throw new Exception();
        }finally {
            response.close();
            httpClient.close();
        }

    }

}
