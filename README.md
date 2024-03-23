# 相关技术
- 基础框架：springboot
- ORM框架：mybatis
- 爬虫：HttpClient+Jsoup+Bloom布隆过滤器
- 数据库：MySQL
- 其他技术：详见后端项目的pom.xml

# 项目部署：
- 使用IDEA部署项目，导入maven依赖
- 在application.yml配置数据库的连接信息，服务器端口号，本系统的静态资源（图片）存储的本地路径C:\Users\rongyao\Desktop\images\
```yaml
spring:  
  #数据库的连接信息,主机名localhost:3306,数据库名truth，用户root密码111111
  datasource:  
    driver-class-name: com.mysql.cj.jdbc.Driver  
    url: jdbc:mysql://localhost:3306/truth  
    username: root  
    password: 111111  
    
#设置后端服务器端口号为8081
server:  
  port: 8081
  #设置自定义的全局变量  
value:  
  imagePath: C:\Users\rongyao\Desktop\images\  #本系统存储的图片本地地址，例如在桌面上新建images文件夹，系统会自动把所有的图片放到这个文件夹中。
```

- mysql部署：详见sql文件夹
# 全局配置
在config下的CorsConfig类中，配置了全局跨域请求和请求拦截器（需要验证token才能访问资源）。
```java
@Override  
public void addCorsMappings(CorsRegistry registry) {   // 重写addCorsMappings方法  
    registry.addMapping("/**")  // 添加跨域映射，表示对所有路径都允许跨域访问  
            .allowedOrigins("*")  //放行哪些原始域
            .allowedMethods("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH");  
}
//请求拦截器  
@Override  
public void addInterceptors(InterceptorRegistry registry){  
    registry.addInterceptor(loginCheckInterceptor)  //loginCheckInterceptor位于Intercepter文件夹下。
            .addPathPatterns("/**")  
            .excludePathPatterns("/login")  //拦截除了login外的所有资源  
			.excludePathPatterns("/image/**")  
			.excludePathPatterns("/register");
}
```

# 工具类
- **BloomFilterUtils工具类**，实例化布隆过滤器，将爬取过的网址url放入过滤器中，防止重复爬取url，并将该过滤器的数据持久化到resources文件夹下的bloom。
- **HttpUtils工具类**，设置HttpClient连接池，配置请求信息，用于获取爬取目标url的页面源码，如果是图片url，爬取图片下载到本地。
- **JwtUtils工具类**，用于生成Jwt令牌和解析令牌
- **TextTools工具类**，用于爬虫解析网页源码。
- **ValueProperties**，自定义工具类，用于自定义变量，例如图片保存路径，使用@ConfigurationProperties注解，在application.yml中配置的自定义变量可以通过ValueProperties访问到。

# 定时爬虫任务
- JZcrawlerTask：爬取[【腾讯新闻较真平台】新型冠状病毒肺炎7x24实时辟谣科普 (qq.com)](https://vp.fact.qq.com/home)平台的所有文章内容，共约一千条。
- WeiboTask：实时爬取微博热搜榜，[微博搜索-热搜榜 (weibo.com)](https://s.weibo.com/top/summary?cate=realtimehot)。
- ZHcrawlerTask：实时爬取[中国互联网联合辟谣平台 (piyao.org.cn)](https://www.piyao.org.cn/)今日辟谣榜单的文章。