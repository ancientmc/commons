package com.ancientmc.commons.test;

import com.ancientmc.commons.file.CopyMode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.function.Function;

import static com.ancientmc.commons.Util.URL;

public class CommonTests {
    private static final Path DATA_PATH = Path.of("data");
    private static final Function<String, Path> PATH = DATA_PATH::resolve;

    private static final URL MINECRAFT_JAR_URL = URL.apply("https://piston-data.mojang.com/v1/objects/4e618f09a0c649dde3fdf829df443ce0b8831e65/client.jar");

    @Test
    public void downloadJar() throws IOException {
        download(MINECRAFT_JAR_URL, "26.1.2.jar");
    }

    @Test
    public void extractJar() throws IOException {
        extract("26.1.2.jar", "src/");
    }

    @Test
    public void copyJarFile() throws IOException {
        copyFile("26.1.2.jar", "jar/", CopyMode.FILE_DIRECTORY);
    }

    @Test
    public void copyAllClasses() throws IOException {
        copyDirectory("src/", "classes/", CopyMode.TREE_DIRECTORY);
    }

    @Test
    public void copyComMojangClassesToSomeOtherDirectoryOrSomething() throws IOException {
        copyDirectory("src/com/", "mojangclasses/", CopyMode.DIRECTORY_DIRECTORY);
    }

    @Test
    public void buildSlimJar() throws IOException {
        compress("src/", "slim.jar");
    }

    public void download(final URL url, final String output) throws IOException {
        final FileFunction function = new FileFunction.Download()
                .withUrl(url)
                .withOutput(PATH.apply(output));
        function.run();
    }

    public void copyFile(final String input, final String output, final CopyMode mode) throws IOException {
        final FileFunction function = new FileFunction.FileCopy()
                .withInput(PATH.apply(input))
                .withOutput(PATH.apply(output))
                .withCopyMode(mode);
        function.run();
    }

    public void copyDirectory(final String input, final String output, final CopyMode mode, final String... inclusions) throws IOException {
        final FileFunction function = new FileFunction.DirectoryCopy()
                .withInput(PATH.apply(input))
                .withOutput(PATH.apply(output))
                .withCopyMode(mode)
                .withInclusions(inclusions);
        function.run();
    }

    public void extract(final String input, final String output) throws IOException {
        final FileFunction function = new FileFunction.Extract()
                .withInput(PATH.apply(input))
                .withOutput(PATH.apply(output))
                .withInclusions( // for this test, filter out everything but the actual Minecraft classes.
                        "**/com/**", "**/net/**");
        function.run();
    }

    public void compress(final String input, final String output) throws IOException {
        final FileFunction function = new FileFunction.Compress()
                .withInput(PATH.apply(input))
                .withOutput(PATH.apply(output));
        function.run();
    }
}
