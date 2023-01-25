package filesystem;

import java.util.Map;

public class PathResolver {

    private static final String TEMPLATE_PATH = "src/main/resources/templates%s";
    private static final String STATIC_PATH = "src/main/resources/static%s";
    public static final String NOT_FOUND_HTML = "src/main/resources/templates/notfound.html";
    public static final String INDEX_HTML = "/index.html";
    public static final String DOMAIN = "/";
    public static final String LOGIN_FAILED_HTML = "login_failed.html";

    private static final Map<Extension, String> mappingInfo = Map.of(
            Extension.INDEX, TEMPLATE_PATH.substring(0, TEMPLATE_PATH.length() - 2) + INDEX_HTML,
            Extension.TEMPLATE, TEMPLATE_PATH,
            Extension.STATIC, STATIC_PATH,
            Extension.ELSE, NOT_FOUND_HTML
    );

    private PathResolver() {
    }

    // todo: String -> nio.Path로 변경

    public static String parse(String url) {
        Extension extension = mappingInfo.keySet().stream()
                .filter(keys -> keys.getExtensions()
                        .stream()
                        .anyMatch(url::endsWith))
                .findAny()
                .orElse(Extension.ELSE);
        return String.format(mappingInfo.get(extension), url);
    }
}