package com.yjx;

import com.yjx.mapper.ArticleMapper;
import com.yjx.mapper.TagMapper;
import com.yjx.pojo.Article;
import com.yjx.pojo.Hot;
import com.yjx.pojo.Tag;
import com.yjx.service.CrawlerService;
import com.yjx.utils.BloomFilterUtils;
import com.yjx.utils.HttpUtils;
import com.yjx.utils.ValueProperties;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class TruthGuardProjectApplicationTests {
    @Autowired
    private ValueProperties valueProperties;
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    private CrawlerService crawlerService;


}
