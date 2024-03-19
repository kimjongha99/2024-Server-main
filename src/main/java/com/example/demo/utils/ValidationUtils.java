package com.example.demo.utils;

import java.util.List;

public class ValidationUtils {

    public static boolean isContentLengthValid(String content) {
        return content != null && content.length() >= 1 && content.length() <= 100;
    }

    // 이미지 개수 검사 (10개 이하)
    public static boolean isImageCountValid(List<?> images) {
        return images != null && images.size() <= 10;
    }

    public static boolean isCommentLengthValid(String content) {
        return content != null && content.length() >= 1 && content.length() <= 50;

    }
}
