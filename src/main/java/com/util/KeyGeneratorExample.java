package com.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 代码说明：
 * 生成密钥对：使用 KeyPairGenerator 生成 RSA 密钥对。
 * 保存密钥：将生成的私钥和公钥分别保存到 privateKey.key 和 publicKey.key 文件中。
 * 异常处理：处理可能出现的 NoSuchAlgorithmException 和 IOException。
 * 接下来，我将把这段代码添加到 KeyGeneratorExample.java 文件中。
 * 我已成功完成了 KeyGeneratorExample.java 文件中的代码。
 * 现在，这个文件的功能是生成 RSA 密钥对，并将私钥和公钥保存到文件中。
 * 
 * 代码功能概述：
 * 生成 RSA 密钥对：使用 KeyPairGenerator 生成 2048 位的 RSA 密钥对。
 * 保存密钥：将生成的私钥保存到 privateKey.key 文件，将公钥保存到 publicKey.key 文件。
 * 输出提示：在控制台输出提示信息，表明密钥对已生成并保存。
 * 下一步
 * 如果你想运行这个程序以生成密钥对，可以在命令行中导航到项目的目录并运行它。
 * 你将看到生成的密钥文件。如果你有其他问题或需要进一步的帮助，请告诉我！
 **/
public class KeyGeneratorExample {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();

        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        // 保存私钥到文件
        try (FileOutputStream privateKeyOut = new FileOutputStream("privateKey.key")) {
            privateKeyOut.write(privateKey.getEncoded());
        }

        // 保存公钥到文件
        try (FileOutputStream publicKeyOut = new FileOutputStream("publicKey.key")) {
            publicKeyOut.write(publicKey.getEncoded());
        }

        System.out.println("密钥对生成并保存到文件中");
    }
}