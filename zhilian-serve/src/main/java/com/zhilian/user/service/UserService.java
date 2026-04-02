package com.zhilian.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhilian.user.dto.UserLoginDTO;
import com.zhilian.user.dto.UserRegisterDTO;
import com.zhilian.user.entity.User;
import com.zhilian.user.vo.UserVO;

public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userDTO 用户注册信息
     * @return 注册成功的用户信息
     */
    UserVO register(UserRegisterDTO userDTO);

    /**
     * 用户登录
     *
     * @param loginDTO 用户登录信息
     * @return 登录成功的用户信息
     */
    UserVO login(UserLoginDTO loginDTO);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 根据手机号查询用户信息
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    User getByMobile(String mobile);

    /**
     * 根据邮箱查询用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    User getByEmail(String email);

    /**
     * 发送重置密码验证码
     *
     * @param emailOrMobile 邮箱或手机号
     */
    void sendResetCode(String emailOrMobile);

    /**
     * 重置密码
     *
     * @param emailOrMobile 邮箱或手机号
     * @param code          验证码
     * @param newPassword   新密码
     */
    void resetPassword(String emailOrMobile, String code, String newPassword);

    /**
     * 修改用户信息
     *
     * @param userVO 用户信息
     * @return 修改后的用户信息
     */
    UserVO update(UserVO userVO);

    /**
     * 更新密码
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updatePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 更新邮箱
     *
     * @param userId 用户ID
     * @param email  新邮箱
     */
    void updateEmail(Long userId, String email);

    /**
     * 更新手机号
     *
     * @param userId 用户ID
     * @param mobile 新手机号
     */
    void updateMobile(Long userId, String mobile);

    /**
     * 提交实名认证
     *
     * @param userId   用户ID
     * @param realName 真实姓名
     * @param idCard   身份证号码
     */
    void submitAuthentication(Long userId, String realName, String idCard);

    /**
     * 获取认证状态
     *
     * @param userId 用户ID
     * @return 认证状态
     */
    Integer getAuthStatus(Long userId);
}