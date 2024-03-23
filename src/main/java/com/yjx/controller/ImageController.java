package com.yjx.controller;

import com.yjx.pojo.Result;
import com.yjx.service.ArticleService;
import com.yjx.utils.ValueProperties;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ValueProperties valueProperties;
    @Autowired
    private ArticleService articleService;

    //响应文章图片，通过img标签的src属性显示后端图片
    @GetMapping  //图片路径为：http://localhost:8081/image?imgName=example.jpg
    public void getImage(@RequestParam("imgName") String imgName,
                         HttpServletResponse response) throws IOException {
        response.setContentType("image/png,image/jpg"); // 设置响应的MIME类型，告诉浏览器返回的是图片类型
        ServletOutputStream out = response.getOutputStream(); // 获取响应输出流
        Path imagePath = Paths.get(valueProperties.getImagePath(), imgName);
        log.info("响应图片:" + imagePath);
        // 使用 Files 类的 readAllBytes 方法读取图片文件的内容并写入输出流,Files会自动关闭输入流
        byte[] imageBytes = Files.readAllBytes(imagePath);
        out.write(imageBytes); // 将图片数据写入响应输出流，返回给客户端
        out.close(); // 关闭输出流，
    }
    //对上面这个代码进行修改，有利于我对输入输出流的理解，不用管
//    @GetMapping  //图片路径为：http://localhost:8081/image?imgName=example.jpg
//    public void getImage(@RequestParam("imgName") String imgName,
//                         HttpServletResponse response) throws IOException {
//        response.setContentType("image/png,image/jpg"); // 设置响应的MIME类型，告诉浏览器返回的是图片类型
//        ServletOutputStream out = response.getOutputStream(); // 获取响应输出流
//        File file = new File(valueProperties.getImagePath() + imgName); // 根据路径构建文件对象
//        log.info("响应图片:" + file.getPath());
//        InputStream in = new FileInputStream(file); // 创建文件输入流，用于读取图片数据
//        int available = in.available(); // 获取输入流可读取的字节数
//        byte[] bytes = new byte[available]; // 创建一个字节数组，用于存储图片数据
//        in.read(bytes); // 读取图片数据到字节数组中
//        out.write(bytes); // 将图片数据写入响应输出流，返回给客户端
//        in.close(); // 手动关闭输入流
//        out.close(); // 关闭输出流
//    }

    //响应题目的封面图
    @GetMapping("/questionCover") //图片路径为：http://localhost:8081/image/questionCover?imgName=example.jpg
    public void getQuestionCover(@RequestParam("imgName") String imgName,
                                 HttpServletResponse response) throws IOException {
        response.setContentType("image/png,image/jpg"); // 设置响应的MIME类型，告诉浏览器返回的是图片类型
        ServletOutputStream out = response.getOutputStream(); // 获取响应输出流
//        File file = new File(valueProperties.getImagePath() + "/questionCover/" + imgName); // 根据路径构建文件对象
        Path imagePath = Paths.get(valueProperties.getImagePath(),"questionCover", imgName);
        log.info("响应图片:" +imagePath);
        byte[] imageBytes = Files.readAllBytes(imagePath);
        out.write(imageBytes); // 将图片数据写入响应输出流，返回给客户端
        out.close(); // 关闭输出流
    }
    //上传文章图片，并返回图片的url
    @PostMapping()
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try {
            String picName = UUID.randomUUID().toString() + ".jpg";
            Path filePath = Paths.get(valueProperties.getImagePath(), picName);
            log.info("原始图片名字："+file.getOriginalFilename()+"，保存图片路径：{}"+filePath);
            Files.write(filePath, file.getBytes());
            String imageUrl = "/image?imgName="+picName;
            log.info("返回imageUrl："+imageUrl);
            return ResponseEntity.ok().body("{\"location\": \"" + imageUrl + "\"}");  //返回一个js格式的数据，这是因为tinymce只接受这样的数据
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("上传失败");
        }
    }
    //题目封面图片上传
    @PostMapping("/questionCover")
    public ResponseEntity<String> uploadQuestionCover(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        try {
            String picName = UUID.randomUUID().toString() + ".jpg";
            Path filePath = Paths.get(valueProperties.getImagePath(),"questionCover", picName);
            log.info("原始图片名字："+file.getOriginalFilename()+"，保存图片路径：{}"+filePath);
            Files.write(filePath, file.getBytes());
            String imageUrl = "/image/questionCover?imgName="+picName;
            log.info("返回imageUrl："+imageUrl);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("上传失败");
        }
    }

    //question的cover取消上传，需要删除存入的图片
    @DeleteMapping("/questionCover")
    public Result cancelCover(@RequestParam String questionCover) {
        Path filePath=Paths.get(valueProperties.getImagePath(),"questionCover",questionCover.split("imgName=")[1]);
        if (Files.exists(filePath)) {
            try {
                Files.delete(filePath);
                log.info("文件删除成功:"+filePath);
            } catch (IOException e) {
                log.info("文件删除失败：" + e.getMessage());
                e.printStackTrace();
            }
        } else {
            log.info("文件不存在");
        }
        return Result.success();
    }


}
