package donot.gas.back.exception.user;

public class NoArgException extends RuntimeException{
    public NoArgException() {
        super("필요값이 없습니다.");
    }
}
