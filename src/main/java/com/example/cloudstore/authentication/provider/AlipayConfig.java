package com.example.cloudstore.authentication.provider;


public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016091400508702";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDBvfJvh4Y39ajDOfQvZ1mtKOQCzktctup7t3Jg0ip5gGZNtVi4XvQ6MLRVyK3aWfzGxvsk0H4azkgzWH78l9L3IquAooGZ/vsRPYWvikapuiBYq8BQjtc0g6d4vAZr0wIM5jCaMo1e+JBk8zbzgcEAB3d8W2IiYLws+ixIxq2Z0nWqHu9eMIaoyY5lLKxKHsScWOEhkBU5anJMyeQbOEZVfvVRSkEMlbXD9hX7IeGGDVdahbdrys8Wj4tszL+1y0bEQDXWrMFi0CmgiVK96rR2HQzg9WBuDabbjCHg4aoeu935+sagJ+A7b4P9sDe1x0sanDoPxnnaS8oKRkY/fXGjAgMBAAECggEAaFlIFG3oCofTp6K1NDrz/+HAPQqjaIJUdOHvRtqradmQQFZmPJdX72eAGZwGl05UaTOO3Ns2sHVeSdlmO+ICDH2t5ZChQTImQ7jhIhTVzmCjP17yK/FjU7N9JfgPdU+nEQ+CKesEXZx/GQAuHqVKMBvVbTNC/cUgVRFBfMkVg3z4S5dCVhYTlf4miYbRu9wcDXYRac8n9SN33he+fBD3u9eneg/Yx5LWznHjJw0p29KGcdXDCXE5opweih6RG7DQGzIDoq/TnNsny6K2+3kUjBs3OnNTiIAtd6gMPyVPYzhHHz9LWn6uJ7Gz/mT24vROMxAsZ4w7BZzOX2BaI4Vu0QKBgQD037oCyEHkICQwKokqkmLapraES3hXUYO9jb3lHTr0w9MbiEuoAEyFhv9RUVPTKwChxuYy6DZEpl9E/GC4raXHlf6+QHsZSQgwXhjQJMMFVwjgRwzKG21cs+6cEJ7qDLAT0bDGdrED2yGzozRlzTctoRsisjqMiTwNEORp0iV87QKBgQDKi3l2wFAgOD8Lo7fVa+rLgIFBd1O1HRhvxyEGM9nPyUnFuge63Q9lwKaDu1bYyAJK7sk2oKWA/yNI5z1P7rSErZSTPibvas0H/a6Hg51R7qXpV/bnYNuSdrcJr1oZfYup9aYafX3vQlKwmeSUruHUDfYBiYzlDMXZdfYIqyNmzwKBgQCjv6xSyfmOBJROhVM+Xa+vSYaJxzINKhxvuIZA3SAcp4rXQXheOvzw2cqIZVspvfrpM+miJziLBbp1llNTjANtCPfV36axzO2l8najUcH8IyxzVe/OgzLJqCUqxc6O8rcYOcP9o0dSLcpGpx+V5/o7EB3mQMr4SZkXn7EutnGNLQKBgAZBWHb6aX8VPZ02aS8GLjIqvdMvxD4Lr/2DwNlYMJniNY0Nub9jpF4YHQxkdMItKyT1SvGM9I+PUZ+mxJX7YUfXtIb3nGw4eCyPARzeS7WEEUurk8yBiUZ0tCJwb+/pmK6fD4ox5fnFqpgZqJ2O6RqLc9WIUfmg81tL8VVDM8yxAoGAV/J3fCskEAQbO4v5GtbiODD9SU39nxXSPxHUqA+CWcXZ3HqyY7wAOk4oEQfbsKzjRFfmALy61ZPqBxUEEshvOzjjrjUA3lQbZedHM3M84pClPqjVdphQliV1jHzuoSsVt9Ka5RRl6wmnat/cZijuNVpIfWus+MJIjVJ/f00bb2g=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxUDBiIxn3HJiJOf9KIp6VCd/mdR7dTlFf/dI7NNPEfo81gXhPfwHCMNJfY7E1j98FBtydV75bbMHIgjKr4/1nq95CquC8AIGCdn2zMeV2lNHmx/tMOKLfuxy2q7POrsFh4PYcE0ZVG0pjWcVuytIfIuOv4+ZZe5rxoNof/OPdkj5nD/EFF4qSJTg1Xn0fPGRrjZhMkzx/HZo1WpbMdjP8fT4LgdYBlR26G92Yipb7UXWw9iuccaa/qmSFhK7COXJbf45i/LyDxhpkh/rfan1D0Ji5Ttk4lzYehcE+62/tkLxNjp4O06tqM7bYVYIYGVJN71XzCTkaNpwT+gJ3bLAXQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/notify_url";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://192.168.100.152:8080/return_url";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";
}
