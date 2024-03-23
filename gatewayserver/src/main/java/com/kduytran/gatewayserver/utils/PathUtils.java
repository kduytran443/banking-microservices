package com.kduytran.gatewayserver.utils;

public class PathUtils {
    public static final String CONTEXT_PATH = "bank";

    private PathUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String getPathWithContextPath(final String path) {
        return String.format("/%s/%s", CONTEXT_PATH, path);
    }

    /**
     * Generates a path matcher for a specific service based on the provided service name.
     * This method formats the path matcher to include the context path and the service name, followed by any sub-paths.
     *
     * @param serviceName The name of the service for which the path matcher is generated.
     * @return A formatted path matcher string that includes the context path, service name, and wildcard sub-paths.
     *
     * @Example:
     * "loans" -> "/{contextPath}/loans/**"
     */
    public static final String getPathMatchersOfService(String serviceName) {
        return String.format("/%s/%s/**", PathUtils.CONTEXT_PATH, serviceName);
    }

}
