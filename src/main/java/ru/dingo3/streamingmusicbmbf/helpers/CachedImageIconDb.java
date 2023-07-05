package ru.dingo3.streamingmusicbmbf.helpers;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;

public class CachedImageIconDb {

    private final String path;
    public CachedImageIconDb(String path) {
        this.path = path;
    }

    public ImageIcon getByUrl(String url) {
        Path file = Paths.get(path + url.hashCode() + ".png");
        if (Files.exists(file)) {
            return new ImageIcon(file.toString());
        } else {
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
}
