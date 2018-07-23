package com.example.cloudstore.authentication.provider;


public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016091400508702";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCctUWNNWANkNSHz0xFybAqKQVPaD614EGN1vNbCGskj01144zKgrTt87U2myGc3k0SSzBLPhqVAGahZgv1Ad9/dBmgBmWgdxkO8iaMhpVu8BVZA4Jt9V4JPIo75tma2D9sAXKBBSY7Z1104gzUzV9tetF/0cG9FctlVUCDm6ZHKNlHgM3RXx8aoRVyY0zsDN65Hqj+sHNPe7fg27xX85ioixlF0lJfgpzj6lDOGb1V90MF2teXBX+J9dBs34ZBCY3rxAuDwi9JdIBMqYQBTX+QzrVXyD1gBcbZO6mrWGUaMKER6Ku52pmErEHIccEMfIBGCu0vC6Jhf4IkXDw1EYmfAgMBAAECggEAc1nXvY7JipLObr2/xislOpOBbics6hIODwtnjN3QiFPXz+xU6Yh3CCT5gh2FC4RzNQEVpn0rcfyejzWfnZToYFgkuK+cxRaF4oOKrqwueT/OZvx3WNthgekHU/qkuR9u5+DU2XXh4549o9j1WvtlwB/E8JKFStstIgUz/Oxo5D1XoJrgD1Xf8IHW8Xn1BeDjZwn0GkFT4/EBkLr0WTQDQvyP1zgTGfJt5d1TRW7heOZaAr1SxnukRytoCRC8VT4dEZeEhN7l1Fh8y5KgVp4GAhDxdQkcAo48CsX6V5ysFx0vH3ql2TPDEOjh6qeETSqMpNm3zAJU45UJ7L+lg70U6QKBgQD0ZFFqCpeZfb1uwfzh2YyprnvWW6C0m0fWpdX1xVaa7bXKhLOQogyDzFJf7m7oAItvENz3iDRbnphwssnSrpqAcsddl9LJnVXVZwyo6UQTgJImjicj6vpSI4YiiK2UFUOQg2rAj5SvhOQ50xfti9cRucyt+rKpjJ+J5gpExovFgwKBgQCkJsNc/lBxk/xeBzfRS7/11CJqhMNpqay7TheQMxSPGey7douyNRDjwMkXO1LarxmRxtvfXnk0VYANU9IeX+KdYCt6fhAwVAWSTNNZx3laUT/Cnj/ZS7m+4aCMx1K4whjxxjf4KtUWTRbpH6oEC2PxZ0JIJyucZ8fEvOMSPTRMtQKBgQC/og4I5YdcTzGEMM6XVhwrTWtDeZO5Ku2YzW7JidTQJUdgJtAawEap7kRFftQuTYcILyPlYUMU+HY2IKuK7WivAdF8fTlG6H1OmcBejpLX7AAuBVeBnZZtY/ehCqV5MXdKURIbLjr7qmu/P9xKY1XgS13IqoT/2AmdeulgZoNg+wKBgEbRemN6TFZT1AL09j3TgmOaO6bc1DmXvC+qxzus7ee29Y0C3c3qWvkHSTo+m8ZA2LUtp9o8NepAINifGv2DvI7M2LUmAfH3rdeqUVxgVJiEiQ97mmwy4VXanZteuu9+vahBLhBg2kGezI2S3ZSVHW8eb1gVnhkCJQtZulU6FoApAoGBANmYxJuo9PcpRBIm9LZdfUfVP+ADebi/vzjCpp0lbrMVUlnWsab1ljz4oLuMp5vXp0cKLi35HzzsUceBSMO+QdrG1WpgkOTkPh+DZdkeCmU6yFMTFQX3jr1n+BVH6eNkwtwp+5hBZGXIwMbxpGEId6HaMB0g+1GkViZX0gxzRmDe";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxUDBiIxn3HJiJOf9KIp6VCd/mdR7dTlFf/dI7NNPEfo81gXhPfwHCMNJfY7E1j98FBtydV75bbMHIgjKr4/1nq95CquC8AIGCdn2zMeV2lNHmx/tMOKLfuxy2q7POrsFh4PYcE0ZVG0pjWcVuytIfIuOv4+ZZe5rxoNof/OPdkj5nD/EFF4qSJTg1Xn0fPGRrjZhMkzx/HZo1WpbMdjP8fT4LgdYBlR26G92Yipb7UXWw9iuccaa/qmSFhK7COXJbf45i/LyDxhpkh/rfan1D0Ji5Ttk4lzYehcE+62/tkLxNjp4O06tqM7bYVYIYGVJN71XzCTkaNpwT+gJ3bLAXQIDAQAB";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/notify_url";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://192.168.100.129:8080/return_url";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";
}
