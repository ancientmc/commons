package com.ancientmc.commons.test;

import com.ancientmc.util.Util;

import java.net.URL;
import java.nio.file.Path;

public class TestPaths {
    public static final URL MANIFEST = Util.URL.apply("https://piston-meta.mojang.com/mc/game/version_manifest.json");
    public static final URL MINECRAFT_JAR_URL = Util.URL.apply("https://piston-data.mojang.com/v1/objects/4e618f09a0c649dde3fdf829df443ce0b8831e65/client.jar");

    public static Path path(final String input) {
        return Path.of(input);
    }
}
