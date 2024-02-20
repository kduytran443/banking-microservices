package com.kduytran.gatewayserver.utils;

public class PathUtils {
    public static final String CONTEXT_PATH = "bank";

    public static final String getPathWithContextPath(final String path) {
        return String.format("/%s/%s", CONTEXT_PATH, path);
    }

}
