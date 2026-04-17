package com.scs.util;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FileUploadUtil {

    public static final long MAX_BYTES = 5L * 1024L * 1024L;

    private static final Set<String> ALLOWED_EXT = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
    ));

    private FileUploadUtil() {}

    public static String save(Part part, String uploadDir) throws IOException {
        if (part == null || part.getSize() == 0) return null;
        if (part.getSize() > MAX_BYTES) {
            throw new IOException("File exceeds maximum size of 5 MB");
        }

        String original = extractFileName(part);
        if (original == null || original.trim().isEmpty()) return null;
        original = sanitize(original);

        String ext = extension(original);
        if (!ALLOWED_EXT.contains(ext.toLowerCase())) {
            throw new IOException("File type not allowed: " + ext);
        }

        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Could not create upload directory: " + uploadDir);
        }

        String stored = System.currentTimeMillis() + "_" + original;
        Path target = Paths.get(uploadDir, stored);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return stored;
    }

    private static String extractFileName(Part part) {
        String cd = part.getHeader("content-disposition");
        if (cd == null) return null;
        for (String token : cd.split(";")) {
            token = token.trim();
            if (token.startsWith("filename")) {
                int eq = token.indexOf('=');
                if (eq < 0) return null;
                String v = token.substring(eq + 1).trim();
                if (v.startsWith("\"") && v.endsWith("\"")) {
                    v = v.substring(1, v.length() - 1);
                }
                int slash = Math.max(v.lastIndexOf('/'), v.lastIndexOf('\\'));
                if (slash >= 0) v = v.substring(slash + 1);
                return v;
            }
        }
        return null;
    }

    private static String sanitize(String name) {
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private static String extension(String name) {
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) return "";
        return name.substring(dot + 1);
    }
}
