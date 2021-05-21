package donot.gas.back.exception.user;

public class PasswordValidationException extends RuntimeException {
    public PasswordValidationException() {
        super("비밀번호 생성규칙과 불일치합니다.");
    }
}
