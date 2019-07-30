package com.example.leng.myapplication2.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.leng.myapplication2.R;
import com.example.leng.myapplication2.base.HttpsTrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kingleng.baselibrary.base.BaseModule;

public class JsoupActivity extends AppCompatActivity {

    public static String BASE_URL = "https://www.qu.la/";

    EditText id_edit;
    Button id_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsoup);

        HttpsTrustManager.trustAll();

        initView();
        initEvent();
    }

    private void initView(){
        id_edit = findViewById(R.id.id_edit);
        id_btn = findViewById(R.id.id_btn);
    }

    private void initEvent(){

        id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new Thread(){
//                    @Override
//                    public void run() {
//                        super.run();
//                        try {
//                            String url = getNovelUrl(id_edit.getText().toString());
//                            List<Map<String, String>> mapList = getNovelIndex(url);
//                            Log.e("asd",mapList.toString());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();

                BaseModule.startActivityByTypeCode(JsoupActivity.this,"130020");

            }
        });
    }

    /**
     * 获取搜索结果
     * @param keywords
     * @return
     * @throws IOException
     */
    public static String getNovelUrl(String keywords) throws IOException {

        String url = "https://sou.xanbhx.com/search?siteid=qula&q=" + URLEncoder.encode(keywords, "utf-8");
        Document document = Jsoup.connect(url).get();
        for (Element a : document.getElementsByTag("a")) {
            if (keywords.equals(a.text().trim()))
                return a.attr("href");
        }
        return null;
    }

    /**
     * 获取目录
     * @param url
     * @return
     * @throws IOException
     */
    public static List<Map<String, String>> getNovelIndex(String url) throws IOException {
        List<Map<String, String>> list = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        for (Element a : document.getElementsByTag("a")) {
            if (a.hasAttr("style") && "".equals(a.attr("style")) && a.attr("href").contains("/book/")) {
                Map<String, String> m = new HashMap<>();
                m.put("url", a.attr("href"));
                m.put("name", a.text());
                list.add(m);
            }
        }
        return list;
    }

    /**
     * 获取章节内容
     * @param url
     * @return
     * @throws IOException
     */
    public static String getText(String url) throws IOException {
        url = BASE_URL + url;
        Document document = Jsoup.connect(url).get();
        Element content = document.getElementById("content");
        return content.html().replace("<script>chaptererror();</script>", "");
    }


}
