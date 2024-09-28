package com.brian.nekoo.enumx;

public enum AssetTypeEnum {
    IMAGE,
    VIDEO,
    AUDIO,
    FILE;

    public static AssetTypeEnum fromExtension(String extension) {
        extension = extension.toLowerCase(); // 忽略大小寫
        switch (extension) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
                return IMAGE;
            case "mp4":
            case "avi":
            case "mov":
            case "mkv":
                return VIDEO;
            case "mp3":
            case "wav":
            case "flac":
            case "aac":
                return AUDIO;
            default:
                return FILE;
        }
    }
}
