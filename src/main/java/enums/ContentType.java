package enums;

import java.util.Arrays;

public enum ContentType {
    HTML("text/html;charset=utf-8", ".html"),
    CSS("text/css;charset=utf-8", ".css"),
    JS("text/javascript;charset=utf-8", ".js"),
    PLAIN("plaintext;charset=utf-8", "*"),
    TTF("application/x-font-ttf", ".ttf"),
    OTF("application/x-font-otf", ".otf"),
    WOFF("application/font-woff", ".woff"),
    WOFF2("application/font-woff2", ".woff2");

    private String type;
    private String extensions;

    ContentType(String type, String extensions) {
        this.type = type;
        this.extensions = extensions;
    }

    public String getType() {
        return type;
    }

    public static ContentType find(String url) {
        return Arrays.stream(ContentType.values())
                .filter(c -> url.endsWith(c.extensions))
                .findFirst()
                .orElse(PLAIN);
    }
}