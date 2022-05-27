package app.homsai.engine.common.domain.utils;

public class MimeTypeHelper {
    private static final String[] supportedMimeTypes =
            new String[] {"image", "video", "pdf", "text", "audio"};

    public static boolean isSupportedMimeTypes(String mimeType) {
        if (mimeType == null || mimeType.isEmpty())
            return false;

        for (String supportedMimeType : supportedMimeTypes) {
            if (mimeType.contains(supportedMimeType))
                return true;
        }

        return false;
    }
}
