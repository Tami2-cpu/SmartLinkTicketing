import api from './api'

export const captchaService = {
  generate() {
    // 生成随机验证码
    const captchaCode = Math.floor(1000 + Math.random() * 9000).toString();
    // 模拟后端响应，使用包含验证码字符的base64图片
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          code: 200,
          message: '生成成功',
          data: {
            captchaId: Math.random().toString(36).substr(2, 9),
            imageBase64: `data:image/svg+xml;base64,${btoa(`<svg width="120" height="40" xmlns="http://www.w3.org/2000/svg">
              <rect width="100%" height="100%" fill="#f5f5f5"/>
              <text x="20" y="28" font-family="Arial" font-size="24" font-weight="bold" fill="#333">${captchaCode}</text>
            </svg>`)}`,
            code: captchaCode
          }
        })
      }, 100)
    })
  },
  verify(data) {
    // 模拟后端响应，验证验证码是否为4位数字
    return new Promise((resolve) => {
      setTimeout(() => {
        if (data.captcha && data.captcha.length === 4 && /^\d+$/.test(data.captcha)) {
          resolve({ code: 200, message: '验证成功' })
        } else {
          resolve({ code: 400, message: '验证码错误' })
        }
      }, 100)
    })
  }
}