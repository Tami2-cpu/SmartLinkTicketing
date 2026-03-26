package com.zhilian.core.utils;

import com.zhilian.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户行为日志记录工具
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
public class UserActionLogger {

    private static final Logger logger = LoggerFactory.getLogger(UserActionLogger.class);

    /**
     * 记录用户登录操作
     * 
     * @param username 用户名
     * @param success  是否成功
     * @param ip       IP地址
     */
    public static void logLogin(String username, boolean success, String ip) {
        logger.info("用户登录: username={}, success={}, ip={}", username, success, ip);
    }

    /**
     * 记录用户注册操作
     * 
     * @param username 用户名
     * @param success  是否成功
     * @param ip       IP地址
     */
    public static void logRegister(String username, boolean success, String ip) {
        logger.info("用户注册: username={}, success={}, ip={}", username, success, ip);
    }

    /**
     * 记录用户密码重置操作
     * 
     * @param emailOrMobile 邮箱或手机号
     * @param success       是否成功
     * @param ip            IP地址
     */
    public static void logPasswordReset(String emailOrMobile, boolean success, String ip) {
        logger.info("密码重置: emailOrMobile={}, success={}, ip={}", emailOrMobile, success, ip);
    }

    /**
     * 记录用户头像上传操作
     * 
     * @param username 用户名
     * @param success  是否成功
     * @param ip       IP地址
     */
    public static void logAvatarUpload(String username, boolean success, String ip) {
        logger.info("头像上传: username={}, success={}, ip={}", username, success, ip);
    }

    /**
     * 记录用户信息更新操作
     * 
     * @param username 用户名
     * @param success  是否成功
     * @param ip       IP地址
     */
    public static void logUserUpdate(String username, boolean success, String ip) {
        logger.info("用户信息更新: username={}, success={}, ip={}", username, success, ip);
    }

    /**
     * 记录订单创建操作
     * 
     * @param username 用户名
     * @param orderNo  订单号
     * @param success  是否成功
     * @param ip       IP地址
     */
    public static void logOrderCreate(String username, String orderNo, boolean success, String ip) {
        logger.info("订单创建: username={}, orderNo={}, success={}, ip={}", username, orderNo, success, ip);
    }

    /**
     * 记录订单支付操作
     * 
     * @param username 用户名
     * @param orderNo  订单号
     * @param success  是否成功
     * @param ip       IP地址
     */
    public static void logOrderPay(String username, String orderNo, boolean success, String ip) {
        logger.info("订单支付: username={}, orderNo={}, success={}, ip={}", username, orderNo, success, ip);
    }

    /**
     * 记录订单取消操作
     * 
     * @param username 用户名
     * @param orderNo  订单号
     * @param success  是否成功
     * @param ip       IP地址
     */
    public static void logOrderCancel(String username, String orderNo, boolean success, String ip) {
        logger.info("订单取消: username={}, orderNo={}, success={}, ip={}", username, orderNo, success, ip);
    }

    /**
     * 记录购票人添加操作
     * 
     * @param username  用户名
     * @param buyerName 购票人姓名
     * @param success   是否成功
     * @param ip        IP地址
     */
    public static void logBuyerAdd(String username, String buyerName, boolean success, String ip) {
        logger.info("购票人添加: username={}, buyerName={}, success={}, ip={}", username, buyerName, success, ip);
    }

    /**
     * 记录购票人更新操作
     * 
     * @param username  用户名
     * @param buyerName 购票人姓名
     * @param success   是否成功
     * @param ip        IP地址
     */
    public static void logBuyerUpdate(String username, String buyerName, boolean success, String ip) {
        logger.info("购票人更新: username={}, buyerName={}, success={}, ip={}", username, buyerName, success, ip);
    }

    /**
     * 记录购票人删除操作
     * 
     * @param username 用户名
     * @param buyerId  购票人ID
     * @param success  是否成功
     * @param ip       IP地址
     */
    public static void logBuyerDelete(String username, Long buyerId, boolean success, String ip) {
        logger.info("购票人删除: username={}, buyerId={}, success={}, ip={}", username, buyerId, success, ip);
    }

    /**
     * 记录图片上传操作
     * 
     * @param username 用户名
     * @param fileName 文件名
     * @param success  是否成功
     * @param ip       IP地址
     */
    public static void logImageUpload(String username, String fileName, boolean success, String ip) {
        logger.info("图片上传: username={}, fileName={}, success={}, ip={}", username, fileName, success, ip);
    }

    /**
     * 从上下文获取当前登录用户
     * 
     * @return 用户名
     */
    public static String getCurrentUsername() {
        User user = UserContext.getUser();
        if (user != null) {
            return user.getUsername();
        }
        return "anonymous";
    }
}