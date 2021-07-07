package com.bikeescape.api.util;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String convertListToString(List<String> list) {
        return list.stream().collect(Collectors.joining(". ", "", ". "));
    }

}
