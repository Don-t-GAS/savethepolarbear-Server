package donot.gas.back.exception.user;

public class MismatchPasswordException extends RuntimeException {

    public MismatchPasswordException() {
        super("비밀번호가 불일치합니다.");
    }
}

