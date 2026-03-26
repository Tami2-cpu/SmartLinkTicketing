package com.zhilian.core.exception;

/**
 * 业务异常类
 * 
 * @author 智联票务技术团队
 * @date 2026-03-24
 */
public class BusinessException extends RuntimeException {
    private int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    // 静态方法，方便使用
    public static BusinessException badRequest(String message) {
        return new BusinessException(400, message);
    }

    public static BusinessException unauthorized(String message) {
        return new BusinessException(401, message);
    }

    public static BusinessException forbidden(String message) {
        return new BusinessException(403, message);
    }

    public static BusinessException notFound(String message) {
        return new BusinessException(404, message);
    }

    public static BusinessException internalServerError(String message) {
        return new BusinessException(500, message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        BusinessException that = (BusinessException) o;

        return code == that.code;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + code;
        return result;
    }
}