// aes-util.js
// 通用AES加解密工具，适用于所有页面
// 使用CryptoJS库（需引入cdn）

// 注意：密钥应与后端保持一致，实际生产环境请安全管理密钥
const AES_KEY = "1234567890abcdef"; // 示例密钥，需与后端一致

// 加密字符串
function aesEncrypt(plainText) {
    return CryptoJS.AES.encrypt(plainText, CryptoJS.enc.Utf8.parse(AES_KEY), {
                        mode: CryptoJS.mode.ECB,
                        padding: CryptoJS.pad.Pkcs7
                    }).toString();
                }

// 解密字符串
function aesDecrypt(cipherText) {
    const decrypted = CryptoJS.AES.decrypt(cipherText, CryptoJS.enc.Utf8.parse(AES_KEY), {
                        mode: CryptoJS.mode.ECB,
                        padding: CryptoJS.pad.Pkcs7
    });
    return decrypted.toString(CryptoJS.enc.Utf8);
}

// 用于加密密钥展示、消息加密传输、消息解密展示等
// 其他页面可直接引入本js并调用aesEncrypt/aesDecrypt
