package com.ancientmc.commons.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.function.Function;

import static com.ancientmc.util.Util.URL;

public class CommonTests {
    private static final Function<String, Path> OUT_PATH = (input) -> Path.of("data/" + input);

    private static final URL MANIFEST = URL.apply("https://piston-meta.mojang.com/mc/game/version_manifest.json");

    @Test
    public void downloadManifest() throws IOException {
        download("https://piston-meta.mojang.com/mc/game/version_manifest.json", "manifest.json");
    }

    public void download(final String url, final String output) throws IOException {
        final FileFunction function = new FileFunction.Download()
                .withUrl(URL.apply(url))
                .withOutput(OUT_PATH.apply(output));
        function.run();
    }
}
