package com.movie.web.controller;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.movie.core.constant.CoreConstant;
import com.movie.core.utils.DriveUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

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


/* // đang lỗi chưa sửa
    @GetMapping(value = "/repository/video/{id}")
    public ResponseEntity<InputStreamResource> getFileInDrive(@PathVariable("id") String id) throws IOException {
        googleDrive = DriveUtils.getDriveService();
        InputStream inputStream = googleDrive.files().get(id)
                .executeMediaAsInputStream();
        File file = googleDrive.files().get(id).execute();
        googleDrive.files().get(id).executeMediaAsInputStream();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept-Ranges", "bytes");
        headers.set("content-Type", "video/mp4");
        headers.set("content-Ranges", "bytes 50-1025/17839845");
        headers.set("content-Length", String.valueOf(googleDrive.files().get(id).size()));
        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }
*/

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
