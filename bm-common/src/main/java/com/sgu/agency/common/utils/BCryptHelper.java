package com.sgu.agency.common.utils;

public class BCryptHelper {
//    private static final String cipherAlphabet = "xnyahpogzqwbtsflrcvmuekjdi9807231456";
//    private static final String plainAlphabet = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String plainAlphabet = "aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ0123456789/";
    private static final String cipherAlphabet = "/9876543210ZzỴỵÝýỸỹỶỷỲỳYyXxWwVvỰựỨứỮữỬửỪừƯưỤụÚúŨũỦủÙùUuTtSsRrQqPpỢợỚớỠỡỞởỜờƠơỘộỐốỖỗỔổỒồÔôỌọÓóÕõỎỏÒòOoNnMmLlKkJjỊịÍíĨĩỈỉÌìIiHhGgFfỆệẾếỄễỂểỀềÊêẸẹÉéẼẽẺẻÈèEeĐđDdCcBbẬậẤấẪẫẨẩẦầÂâẶặẮắẴẵẲẳẰằĂăẠạÁáÃãẢảÀàAa";


//    public static boolean check(String text, String hashed) {
//        return BCrypt.checkpw(text, hashed);
//    }

//    public static String encode(String text) {
//        return BCrypt.hashpw(text, BCrypt.gensalt(9));
//    }

    static String hoanVi(String input, String oldAlphabet, String newAlphabet) {
        String output = "";
        int inputLen = input.length();

        if (oldAlphabet.length() != newAlphabet.length()) {
            return null;
        }

        for (int i = 0; i < inputLen; ++i) {
            int oldCharIndex = oldAlphabet.indexOf(input.charAt(i));
            if (oldCharIndex >= 0) {
                output += newAlphabet.charAt(oldCharIndex);
            } else {
                output += input.charAt(i);
            }
        }
        return output;
    }

    public static String encrypt(String input) {
        return hoanVi(input, plainAlphabet, cipherAlphabet);
    }

    // vi?t hàm gi?i mã
    public static String decrypt(String input) {
        return hoanVi(input, cipherAlphabet, plainAlphabet);
    }

    public static boolean checkData(String text, String hashed) {
        return decrypt(hashed).equals(text);
    }
}
