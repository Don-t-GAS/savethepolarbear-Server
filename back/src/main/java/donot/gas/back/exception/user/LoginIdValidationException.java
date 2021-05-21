package donot.gas.back.exception.user;

public class LoginIdValidationException extends RuntimeException {
    public LoginIdValidationException() {
        super("아이디 생성규칙과 불일치합니다.");
    }
}
