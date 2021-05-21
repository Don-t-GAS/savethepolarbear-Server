package donot.gas.back.dto;

public class PointDto {
    private static final int ONE = 10;
    private static final int TWO = 8;
    private static final int THREE = 6;
    private static final int FOUR = 2;
    private static final int FIVE = 1;

    public static int getPoint(Integer grade) {
        switch(grade) {
            case 1: return ONE;
            case 2: return TWO;
            case 3: return THREE;
            case 4: return FOUR;
            case 5: return FIVE;
            default: return 0;
        }
    }
}
