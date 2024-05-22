package com.example.asg02.utils;

import java.text.DecimalFormat;

public class StringUtils {
    public static String createEmbedLinkFromYoutube(String youtubeLink) {
        String id = "https://www.youtube.com/embed/" + youtubeLink.substring(youtubeLink.indexOf("watch?v=") + 8);
        return "<iframe width=\"100%\" height=\"100%\" src=\"" + id + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
    }

    public static String generateDetailsOfCensor(String signature) {
        switch (signature) {
            case "P":
                return "Phim dành cho mọi lứa tuổi";
            case "K":
                return "Phim dành cho khán giả dưới 13 tuổi với điều kiện xem cùng cha mẹ hoặc người giám hộ";
            case "T13":
                return "Phim dành cho khán giả từ 13 tuổi trở lên";
            case "T16":
                return "Phim dành cho khán giả từ 16 tuổi trở lên";
            case "T18":
                return "Phim dành cho khán giả từ 18 tuổi trở lên";
            case "C":
                return "Phim không được phép phổ biến";
            default:
                return "";
        }
    }

    public static String formatMoney(double money) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedMoney = formatter.format(money);
        return formattedMoney;
    }
}
