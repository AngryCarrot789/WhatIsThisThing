package reghzy.witt.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class StringUtils {
    // Fixed decimal place
    public static String d2s(double value, int places) {
        return BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP).toPlainString();
    }
}
