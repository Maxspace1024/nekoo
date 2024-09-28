package com.brian.nekoo.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FileUtil {
    public final static String PERIOD = ".";
    public final static String EMPTY_STR = "";

    public static String generateUuidFileName(String filename) {
        String fileExtension = filename.substring(filename.lastIndexOf(PERIOD));
        return UUID.randomUUID() + fileExtension;
    }

    public static String extractFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(PERIOD)).replace(PERIOD, EMPTY_STR);
    }
}
