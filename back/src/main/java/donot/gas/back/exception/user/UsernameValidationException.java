package donot.gas.back.exception.user;

public class UsernameValidationException extends RuntimeException{
    public UsernameValidationException() {
        super("Username이 공백입니다.");
    }
}
