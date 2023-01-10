package http.common;

import java.util.Arrays;
import java.util.Optional;

public enum MediaType {
    TEXT_PLAIN("text/plain", "text"),
    TEXT_HTML("text/html", "html"),
    TEXT_CSS("text/css", "css"),
    IMAGE_JPEG("image/jpeg", "jpeg"),
    IMAGE_PNG("image/png", "png"),
    AUDIO_MPEG("audio/mpeg", "mpeg"),
    AUDIO_OGG("audio.ogg", "ogg"),
    VIDEO_MP4("video/mp4", "mp4");

    private final String type;
    private final String extension;

    MediaType(String type, String extension) {
        this.type = type;
        this.extension = extension;
    }

    public String getType() {
        return this.type;
    }

    public static Optional<MediaType> fromExtension(String extension) {
        return Arrays.stream(MediaType.values())
                .filter(type -> type.extension.equals(extension))
                .findFirst();
    }

}