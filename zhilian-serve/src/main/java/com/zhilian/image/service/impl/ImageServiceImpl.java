package com.zhilian.image.service.impl;

import com.zhilian.image.service.ImageService;
import com.zhilian.core.constants.ImageConstants;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public String uploadImage(byte[] fileBytes, String fileName) {
        try {
            // 确保上传目录存在
            File uploadDir = new File(ImageConstants.UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一文件名
            String uniqueFileName = UUID.randomUUID().toString() + "." + getFileExtension(fileName);
            String filePath = ImageConstants.UPLOAD_DIR + uniqueFileName;

            // 保存文件
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(fileBytes);
            }

            // 返回文件URL
            return "/api/image/view?fileName=" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteImage(String fileName) {
        try {
            File file = new File(ImageConstants.UPLOAD_DIR + fileName);
            if (file.exists()) {
                return file.delete();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getImagePath(String fileName) {
        return ImageConstants.UPLOAD_DIR + fileName;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}