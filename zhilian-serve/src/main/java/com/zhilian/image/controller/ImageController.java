package com.zhilian.image.controller;

import com.zhilian.core.exception.Result;
import com.zhilian.image.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
@RequestMapping("/api/image")
@Api(tags = "图片模块")
public class ImageController {

    @Resource
    private ImageService imageService;

    @PostMapping("/upload")
    @ApiOperation("上传图片")
    public Result upload(@RequestParam("file") MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            String fileUrl = imageService.uploadImage(fileBytes, file.getOriginalFilename());
            if (fileUrl != null) {
                return Result.success(fileUrl);
            } else {
                return Result.error("上传失败");
            }
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除图片")
    public Result delete(@RequestBody java.util.Map<String, String> deleteInfo) {
        try {
            String url = deleteInfo.get("url");
            String fileName = url.substring(url.lastIndexOf("=") + 1);
            boolean success = imageService.deleteImage(fileName);
            if (success) {
                return Result.success("删除成功");
            } else {
                return Result.error("文件不存在");
            }
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/view")
    @ApiOperation("查看图片")
    public void view(@RequestParam String fileName, HttpServletResponse response) {
        try {
            String filePath = imageService.getImagePath(fileName);
            File file = new File(filePath);
            if (file.exists()) {
                response.setContentType("image/jpeg");
                try (FileInputStream fis = new FileInputStream(file);
                        OutputStream os = response.getOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                }
            } else {
                response.setStatus(404);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}