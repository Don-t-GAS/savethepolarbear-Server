package donot.gas.back.exception.user;

public class ExistLoginIdException extends RuntimeException {
    public ExistLoginIdException() {
        super("이미 존재하는 로그인ID 입니다.");
    }
}
