package com.example.demo.utils;

import com.example.demo.common.enums.ReportStatus;

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

    // 신고 상태 유효성 검사
    public static boolean isReportStatusValid(String status) {
        // 상태가 null인 경우, 검사를 통과시킨다 (모든 상태를 포함하는 조회를 의미)
        if (status == null) return true;

        try {
            // 상태 값이 Enum에 존재하는지 시도
            ReportStatus.valueOf(status.toUpperCase());
            return true; // 존재하는 경우
        } catch (IllegalArgumentException e) {
            return false; // Enum에 없는 값인 경우
        }

}
    }