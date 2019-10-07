package com.najih.news.model;

public class Favorite {
    static boolean liked = false;
    static String title = "( empty )";

    public Favorite() {
    }

    public static boolean isLiked() {
        return liked;
    }

    public static void setLiked(boolean liked) {
        Favorite.liked = liked;
    }
    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        Favorite.title = title;
    }
}
