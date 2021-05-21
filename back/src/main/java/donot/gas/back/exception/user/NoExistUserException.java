package donot.gas.back.exception.user;

public class NoExistUserException extends RuntimeException{
    public NoExistUserException() {
        super("존재하지 않는 유저입니다.");
    }
}
