package util;

import java.util.Objects;

public final class StringUtil {

    public static boolean isEmpty(String string) {
        return Objects.isNull(string) || string.isEmpty();
    }
}
