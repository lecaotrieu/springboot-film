package com.movie.core.config;

import com.movie.core.constant.CoreConstant;
import com.movie.web.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    FilmHandelInterceptor filmHandelInterceptor;

    @Autowired
    WebHandelInterceptor webHandelInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(filmHandelInterceptor).addPathPatterns("/admin/film/**");
//        registry.addInterceptor(webHandelInterceptor).addPathPatterns("/**").excludePathPatterns("/admin/**","/account/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = CoreConstant.FOLDER_UPLOAD;
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();
        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
    }
}
