package com.yjx.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

@Slf4j
@Component  //用于爬虫的工具
public class HttpUtils {
    private PoolingHttpClientConnectionManager cm;

    public HttpUtils() {
        this.cm = new PoolingHttpClientConnectionManager();
        this.cm.setMaxTotal(100);   //设置最大连接数
        this.cm.setDefaultMaxPerRoute(10);  //设置主机最大连接数
    }

    /**
     * 根据请求地址得到页面数据
     * params:url
     * return：字符串
     */
    public String doGetHtml(String url) {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(this.cm)
                .build();//获取httpClient对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 Edg/121.0.0.0");
        httpGet.setHeader("Cookie","SUB=_2AkMTqfgxf8NxqwJRmfERzW7na4p_yA_EieKl9QnqJRMxHRl-yT9vqmUCtRB6OCnW384q72dq-_UflzICcZL3ONbXcRrI; SINAGLOBAL=9134679444418.674.1704819442402; _s_tentry=www.google.com; Apache=424301350178.0579.1709615467588; ULV=1709615467608:3:2:2:424301350178.0579.1709615467588:1709431834286; UOR=,,www.google.com.hk");
        log.info("正在使用HttpUtils.doGetHtml()方法，请求网址：" + httpGet.getURI() + "\t方法：" + httpGet.getMethod());
        //给请求设置请求信息
        httpGet.setConfig(this.getConfig());
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);  //发起请求，获取响应
            if (response.getStatusLine().getStatusCode() == 200) {   //解析响应结果
                if (response.getEntity() != null) {
                    String content = EntityUtils.toString(response.getEntity(), "utf-8");
                    return content;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("返回无数据");
        return "无数据";  //没有数据就返回空串
    }

    /**
     * 下载图片
     * params:url
     * return:图片名称
     */
    public String doGetImage(String url, String path) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();//获取httpClient对象
        HttpGet httpGet = new HttpGet(url);
        log.info("正在使用HttpUtils.doGetImage()方法，图片网址：{}", url);
        //给请求设置请求信息
        httpGet.setConfig(this.getConfig());
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);  //发起请求，获取响应
            if (response.getStatusLine().getStatusCode() == 200) {   //解析响应结果
                if (response.getEntity() != null) {
//                    String extName = url.substring(url.lastIndexOf('.'));  //从图片链接获取图片后缀
                    String picName = UUID.randomUUID().toString() + ".jpg";   //随机命名图片
                    OutputStream outputStream = new FileOutputStream(new File(path + picName));  //这是图片的保存路径，这个FileOutputStream继承了OutputStream
                    log.info("图片保存到本地：" + path + picName);
                    response.getEntity().writeTo(outputStream); //保存图片
                    return picName;//返回图片名称
                }
            }
        } catch (Exception e) {
            log.info("保存图片出错啦   ");
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("返回无图片");
        return "无图片";  //没有数据就返回空串
    }

    private RequestConfig getConfig() {
        //配置请求信息
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(1000) //设置连接的最长时间，单位是毫秒
                .setConnectionRequestTimeout(500) //设置获取连接的最长时间，单位是毫秒
                .setSocketTimeout(10000)  //设置数据传输的最长时间，单位是毫秒
                .build();//设置连接最长时间为1s，1000ms
        return config;
    }
}
