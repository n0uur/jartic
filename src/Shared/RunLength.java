package Shared;

public class RunLength {
    public static String encode(String str) {
        int n = str.length();
        String result = "";
        for (int i = 0; i < n; i++) {
            int count = 1;
            while (i < n - 1 &&
                    str.charAt(i) == str.charAt(i + 1)) {
                count++;
                i++;
            }

            result += (count + "") + (str.charAt(i) == '0' ? 'A' : 'B');
        }
        return result;
    }

    public static String decode(String s) {
        int count = 0;
        StringBuilder result = new StringBuilder () ;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                count = count * 10 + c - '0' ;
            }
            else {
                while (count >0){
                    result.append(c == 'A' ? '0' : '1');
                    count--;
                }
            }
        }
        return result.toString();
    }
}
