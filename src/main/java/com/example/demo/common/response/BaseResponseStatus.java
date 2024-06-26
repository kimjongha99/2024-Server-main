package com.example.demo.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),


    /**
     * 400 : Request, Response 오류
     */
    POST_ARTICLES_INVALID_CONTENT(false, HttpStatus.BAD_REQUEST.value(), "내용이 1자 이상 100자 이내여야 합니다."),
    INVALID_USER_ACCESS(false, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 유저 접근입니다."),
    POST_ARTICLES_EXCEEDS_IMAGE_LIMIT(false, HttpStatus.BAD_REQUEST.value(), "이미지 개수가 초과했습니다."),
    USERS_EMPTY_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "이메일을 입력해주세요."),
    POST_USERS_INVALID_PASSWORD(false, HttpStatus.BAD_REQUEST.value(), "비밀번호 길이를 확인해주세요."),
    POST_USERS_INVALID_NAME(false, HttpStatus.BAD_REQUEST.value(), "이름 유효성을 확인해주세요."),
    POST_USERS_INVALID_BIRTHDATE(false, HttpStatus.BAD_REQUEST.value(), "생일 날짜 유효성을 확인해주세요."),

    TEST_EMPTY_COMMENT(false, HttpStatus.BAD_REQUEST.value(), "코멘트를 입력해주세요."),
    PRIVACY_POLICY_AGREEMENT_REQUIRED(false, HttpStatus.BAD_REQUEST.value(), "개인정보 처리방침 동의가 필요합니다."),
    LOCATION_BASED_SERVICES_AGREEMENT_REQUIRED(false, HttpStatus.BAD_REQUEST.value(), "위치기반 서비스에 동의가 필요합니다."),
    DATA_POLICY_AGREEMENT_REQUIRED(false, HttpStatus.BAD_REQUEST.value(), "데이터 정책에 동의가 필요합니다."),
    POST_USERS_INVALID_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,HttpStatus.BAD_REQUEST.value(),"중복된 이메일입니다."),
    POST_TEST_EXISTS_MEMO(false,HttpStatus.BAD_REQUEST.value(),"중복된 메모입니다."),

    RESPONSE_ERROR(false, HttpStatus.NOT_FOUND.value(), "값을 불러오는데 실패하였습니다."),

    DUPLICATED_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "중복된 이메일입니다."),
    INVALID_MEMO(false,HttpStatus.NOT_FOUND.value(), "존재하지 않는 메모입니다."),
    FAILED_TO_LOGIN(false,HttpStatus.NOT_FOUND.value(),"없는 아이디거나 비밀번호가 틀렸습니다."),
    EMPTY_JWT(false, HttpStatus.UNAUTHORIZED.value(), "JWT를 입력해주세요."),
    INVALID_JWT(false, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,HttpStatus.FORBIDDEN.value(),"권한이 없는 유저의 접근입니다."),
    NOT_FIND_USER(false,HttpStatus.NOT_FOUND.value(),"일치하는 유저가 없습니다."),
    COMMENT_NOT_FOUND(false,HttpStatus.NOT_FOUND.value(),"코멘트를 찾을수 없습니다."),

    CREATE_COMMENT_INVALID_CONTENT(false,HttpStatus.NOT_FOUND.value(),"댓글의 길이가 너무 길거나 짧습니다."),
    INVALID_OAUTH_TYPE(false, HttpStatus.BAD_REQUEST.value(), "알 수 없는 소셜 로그인 형식입니다."),
    // 접근 금지 관련 수정
    FORBIDDEN_ACCESS(false, HttpStatus.FORBIDDEN.value(), "접근이 금지되었습니다."),

    REQUEST_ERROR(false, HttpStatus.BAD_REQUEST.value(), "잘못된 상태 상태 값" ),

    SUBSCRIPTION_NOT_FOUND(false, HttpStatus.BAD_REQUEST.value(), "잘못된 구독 상태 "),

    /**
     * 500 :  Database, Server 오류
     */
    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버와의 연결에 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 복호화에 실패하였습니다."),


    MODIFY_FAIL_USERNAME(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"유저네임 수정 실패"),
    DELETE_FAIL_USERNAME(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"유저 삭제 실패"),
    MODIFY_FAIL_MEMO(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"메모 수정 실패"),

    UNEXPECTED_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "예상치 못한 에러가 발생했습니다."),
    ARTICLE_NOT_FOUND_OR_INACTIVE(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "기사를 찾을 수 없거나 비활성화되었습니다.."),
    ARTICLE_NOT_FOUND(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "기사를 찾을 수 없습니다"),
    USER_NOT_FOUND(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "유저를 찾을 수 없습니다"),

    ALREADY_EXIST_PAYMENT(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제정보를 찾을수없습니다." ),



    INVALID_PAYMENT(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제정보를 찾을수없습니다." ),
    NOT_FOUND_USER(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제정보를 찾을수없습니다." ),
    NOT_FOUND_PAYMENT(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제정보를 찾을수없습니다." ),
    FAILED_TO_SUBSCRIBE(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제정보를 찾을수없습니다." ),
    NOT_FOUND_SUBSCRIBE(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제정보를 찾을수없습니다." ),
    FAILED_TO_CANCEL_SUBSCRIBE(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제정보를 찾을수없습니다." ),

    FAILED_TO_CANCEL_PAYMENT(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제정보를 찾을수없습니다." ),
    FAILED_TO_PAYMENT(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제정보를 찾을수없습니다 "),
    PAYMENT_VERIFICATION_FAILED(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제정보를 찾을수없습니다 " ),

    ALREADY_EXIST_SUBSCRIPTION(false, HttpStatus.INTERNAL_SERVER_ERROR.value() , "이미 구독이 존재합니다.");



    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
