package minesweeper;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    /**
     * Spilt the String Str by given Length len,
     * then return a list.
     *
     *
     * @param str The String to be spilt.
     * @param len Length of each substring.
     * @return Substrings as a List
     */
    public static List<String> getListStr(String str, int len) {
        List<String> listStr = new ArrayList<>();
        int strLen = str.length();
        int start = 0;
        int num = len;
        String temp;
        while (true) {
            try {
                if (num >= strLen) {
                    temp = str.substring(start, strLen);
                } else {
                    temp = str.substring(start, num);
                }
            } catch (Exception e) {
                break;
            }
            listStr.add(temp);
            start = num;
            num = num + len;
        }
        return listStr;
    }
}
