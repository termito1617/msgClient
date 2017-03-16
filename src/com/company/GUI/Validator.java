package com.company.GUI;

import java.util.regex.Pattern;

/**
 * Created by vlad on 25.02.2017.
 */
public class Validator {
    static boolean checkLength(String str, int min, int max) {
        return !(str.length() < min || str.length() > max);
    }
    static boolean checkString(String str, String reg) {
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str).matches();
    }
}
