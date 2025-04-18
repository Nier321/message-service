package com.jobconnect.message.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;

public class FileUtil {

    private static final String FILE_Path = "D:\\Projects\\jobconnect\\message-service\\chatmessage";
    private static final String TXT = ".txt";

    // 创建文本文件
    public static void createFile(String fileName) throws IOException {
        Path path = Paths.get(FILE_Path + "\\" + fileName+TXT);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    // 写入中文内容
    public static void writeToFile(String fileName, String content) throws IOException {
        Path path = Paths.get(FILE_Path + "\\" + fileName+TXT);
        Files.write(path, (content + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
         Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
    }

    // 根据文件名删除文件
    public static void deleteFile(String fileName) throws IOException {
        Path path = Paths.get(FILE_Path + "\\" + fileName+TXT);
        Files.deleteIfExists(path);
    }
}
