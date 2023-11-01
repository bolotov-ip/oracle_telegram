package com.bolotov.oraclebot.telegram.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class StringCompression {

    public static String compress(String str) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        gzipOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
        gzipOutputStream.close();
        String result = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        return result;
    }

    public static String decompress(String compressText) throws IOException {
        byte[] compressedData = compressText.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        gzipInputStream.close();
        String result = outputStream.toString();
        return result;
    }
}