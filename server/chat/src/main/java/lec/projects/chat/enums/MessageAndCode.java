package lec.projects.chat.enums;

public enum MessageAndCode {
    OK("OK","999"),
    NON_USER("用户不存在","1000"),
    LOGIN_SUCCESS("用户登录成功","1001"),
    WSUNAUTHORIZED("未授权的WebSocket连接","403"),
    SIGNUPFAIL("注册失败","1002"),
    USER_EXIST("用户已经存在","1003"),
    EDIT_FAIL("修改失败","1004"),
    UNAUTHORIZED("未授权","1005"),
    ;

    private String message;
    private String code;
    MessageAndCode(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
