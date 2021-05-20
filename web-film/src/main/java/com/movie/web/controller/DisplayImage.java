package com.movie.web.controller;

import com.google.api.services.drive.Drive;
import com.movie.core.constant.CoreConstant;
import com.movie.core.utils.DriveUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
public class DisplayImage {
    private Drive googleDrive;

  /*  @GetMapping(value = "/repository/film/{id}")
    public @ResponseBody
    byte[] getImage(@PathVariable("id") String id) throws IOException {
        googleDrive = DriveUtils.getDriveService();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        googleDrive.files().get(id)
                .executeMediaAndDownloadTo(outputStream);
        byte[] rs = outputStream.toByteArray();
        return rs;
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
