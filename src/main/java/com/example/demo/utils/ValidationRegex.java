package com.example.demo.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    // 비밀번호 길이 검사 (예시: 최소 8자)
    public static boolean isPasswordValid(String password) {
        return password != null && password.length() >= 8;
    }
    // 이름 길이 검사 (예시: 최소 1자)
    public static boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    // 생일 날짜 유효성 검사 (예시: YYYY-MM-DD)
    public static boolean isBirthDateValid(String birthDate) {
        if (birthDate == null) {
            return false;
        }

        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(birthDate);

        if (!matcher.find()) {
            return false; // 정규 표현식에 맞지 않는 경우
        }

        // 형식에 맞는 경우, 실제로 유효한 날짜인지 추가 검사
        try {
            LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false; // 유효하지 않은 날짜 (예: 2023-02-29)
        }
    }

}

