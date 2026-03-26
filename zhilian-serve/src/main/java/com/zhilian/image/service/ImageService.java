package com.zhilian.image.service;

public interface ImageService {
    /**
     * 上传图片
     * @param fileBytes 图片字节数组
     * @param fileName 文件名
     * @return 图片访问URL
     */
    String uploadImage(byte[] fileBytes, String fileName);

    /**
     * 删除图片
     * @param fileName 文件名
     * @return 是否删除成功
     */
    boolean deleteImage(String fileName);

    /**
     * 获取图片存储路径
     * @param fileName 文件名
     * @return 图片存储路径
     */
    String getImagePath(String fileName);
}