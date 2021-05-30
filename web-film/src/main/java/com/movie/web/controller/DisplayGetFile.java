package com.movie.web.controller;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.movie.core.constant.CoreConstant;
import com.movie.core.utils.DriveUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;

import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@RestController
public class DisplayGetFile {
    private Drive googleDrive;

    /* @GetMapping(value = "/repository/image/{id}")
     public @ResponseBody
     byte[] getImage(@PathVariable("id") String id) throws IOException {
         googleDrive = DriveUtils.getDriveService();
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         googleDrive.files().get(id)
                 .executeMediaAndDownloadTo(outputStream);
         byte[] rs = outputStream.toByteArray();
         return rs;
     }*/
    @Autowired
    ServletContext servletContext;

    @RequestMapping(value = "/image-response-entity", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageAsResponseEntity() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        InputStream in = new URL("https://www.googleapis.com/drive/v3/files/19MCF2s21LlPiW2i5V_oUcUFbMNxVE6lc?key=AIzaSyC7Nyaa0As9iYsDutQ7EsaEfkMtRu3lDn8&v=.mp4&alt=media").openStream();
        byte[] media = IOUtils.toByteArray(in);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
        return responseEntity;
    }

    // đang lỗi chưa sửa
    @GetMapping(value = "/repository/video/{id}")
    public ResponseEntity<InputStreamResource> getFileInDrive(@PathVariable("id") String id) throws IOException {

        googleDrive = DriveUtils.getDriveService();
//        InputStream inputStream = googleDrive.files().get(id).executeMedia().getContent();
        InputStream inputStream = new FileInputStream("C:\\Users\\LapTop T&T\\Downloads\\1.mp4");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Ranges", "bytes");
        headers.set("content-Type", "video/mp4");
        headers.set("content-Ranges", "bytes 50-1025/17839845");
        headers.set("content-Length", String.valueOf(googleDrive.files().get(id).size()));
        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }

    private final String imagesBase = "/" + CoreConstant.FOLDER_UPLOAD;

 /*   @RequestMapping(value = "/fileUpload/{folder}/{id}/{fileName}", method = RequestMethod.GET)
    public @ResponseBody
    byte[] getFile(@PathVariable("folder") String folder, @PathVariable("id") String id, @PathVariable("fileName") String fileName) throws IOException {
        InputStream fin = new FileInputStream( imagesBase + "\\" + folder + "\\" + id +"\\"+ fileName);
        byte[] rs = IOUtils.toByteArray(fin);
        fin.close();
        return rs;
    }*/
}
