package com.ancientmc.commons.test;

import java.nio.file.Path;

public class Paths {
    public static final String MANIFEST = "https://piston-meta.mojang.com/mc/game/version_manifest.json";

    public static Path path(final String input) {
        return Path.of(input);
    }
}
