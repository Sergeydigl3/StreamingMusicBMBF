package ru.dingo3.streamingmusicbmbf.helpers;

import javax.swing.*;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;

public class CachedImageIconDb {

    private final String path;

    public CachedImageIconDb(String path) {
        this.path = path;
    }

    public ImageIcon getByUrl(String url) {
        Path file = Paths.get(path, "cache", url.hashCode() + ".jpg");
//        System.out.println("Проверяем файл " + file);
        if (Files.exists(file)) {
            return new ImageIcon(file.toString());
        } else {
            if (!Files.exists(file.getParent())) {
                try {
                    Files.createDirectories(file.getParent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, file);
                System.out.println("Скачали файл " + file);
                return new ImageIcon(file.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ImageIcon getByUrlResized(String url, int width, int height) {
        ImageIcon imageIcon = getByUrl(url);
        if (imageIcon != null) {
            return new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
        }
        return null;
    }
}
