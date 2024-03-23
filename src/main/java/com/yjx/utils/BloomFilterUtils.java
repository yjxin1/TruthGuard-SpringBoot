package com.yjx.utils;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class BloomFilterUtils {

    private BloomFilter<String> bloomFilter;

    public BloomFilterUtils() throws IOException {
        this.bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),
                20000,
                0.01);
        // 将资源文件加载到布隆过滤器
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("data/bloom")) {
            if (in != null) {
                this.bloomFilter = BloomFilter.readFrom(in, Funnels.stringFunnel(StandardCharsets.UTF_8));
            } else {
                // 处理资源文件不存在的情况
                throw new FileNotFoundException("Resource file 'data/bloom' not found");
            }
        }
    }

    public void urlPut(String url){
        this.bloomFilter.put(url);
    }

    public boolean duplicateCheck(String url){
        return this.bloomFilter.mightContain(url);
    }

    public void persistBloom() throws IOException {
        try (OutputStream out = new FileOutputStream("data/bloom")) {
            this.bloomFilter.writeTo(out);
        }
    }
}
