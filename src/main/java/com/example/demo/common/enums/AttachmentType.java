package com.example.demo.common.enums;

public enum AttachmentType {
    IMAGE("Image"),
    VIDEO("Video"),
    DOCUMENT("Document"); // 예시로 문서 타입도 추가해보았습니다.

    private final String description;

    AttachmentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}