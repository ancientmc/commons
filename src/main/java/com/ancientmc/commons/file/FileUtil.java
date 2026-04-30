package com.ancientmc.commons.file;

import com.ancientmc.commons.Util;
import com.ancientmc.commons.annotation.Internal;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * File-based functions, primarily using the {@link java.nio.file } Path API.
 * Includes functions related to downloading, copying, and extracting.
 *
 * @author moist-mason
 */
public final class FileUtil {

    /**
     * Downloads a file from the provided URL.
     *
     * @param input The input URL.
     * @param output The output file path.
     * @throws IOException exception.
     */
    public static void download(final URL input, final Path output) throws IOException {
        createParentDirectory(output);
        Files.copy(input.openStream(), output);
    }

    /**
     * Creates the provided directory (and any parent directory) if it doesn't exist.
     *
     * @param directory The directory.
     * @throws IOException exception.
     */
    public static void createDirectory(final Path directory) throws IOException {
        if (!exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    /**
     * Creates the parent directory of the path if it doesn't exist.
     *
     * @param path The path.
     * @throws IOException exception.
     */
    public static void createParentDirectory(final Path path) throws IOException {
        createDirectory(path.getParent());
    }

    /**
     * Root method for file copying. Configurable by specifying the {@link CopyMode}.
     *
     * @param input The input file.
     * @param output The output file or directory.
     * @param mode The {@link CopyMode} that determines the type of copy operation.
     * @param inclusions A list of globs indicating paths included for copying (only used for directory tree copying).
     * Leave blank to copy all inputs.
     * @throws IOException exception.
     */
    public static void copy(final Path input, final Path output, final CopyMode mode, final String... inclusions) throws IOException {
        switch (mode) {
            case FILE_FILE -> copyFileToFile(input, output);
            case FILE_DIRECTORY -> copyFileToDirectory(input, output);
            case DIRECTORY_DIRECTORY -> copyDirectoryToDirectory(input, output);
            case TREE_DIRECTORY -> copyDirectoryTree(input, output, inclusions);
        }
    }

    /**
     * Copies a file to another file path.
     * This method is public for documentation purposes.
     * It is usable, but it's recommended to use {@link FileUtil#copy} instead.
     *
     * @param input The input file.
     * @param output The output file.
     * @throws IOException exception.
     */
    @Internal
    public static void copyFileToFile(final Path input, final Path output) throws IOException {
        createParentDirectory(output);
        Files.copy(input, output);
    }

    /**
     * Copies a file into a directory.
     * This method is public for documentation purposes.
     * It is usable, but it's recommended to use {@link FileUtil#copy} instead.
     *
     * @param input The input file.
     * @param output The output directory.
     * @throws IOException exception.
     */
    @Internal
    public static void copyFileToDirectory(final Path input, final Path output) throws IOException {
        createDirectory(output);
        final Path outputPath = output.resolve(input.getFileName());
        Files.copy(input, outputPath);
    }

    /**
     * Copies one directory into another.
     * This method is public for documentation purposes.
     * It's usable, but it's recommended to use {@link FileUtil#copy} instead.
     *
     * @param input The input directory.
     * @param output The output directory.
     * @throws IOException exception.
     */
    @Internal
    public static void copyDirectoryToDirectory(final Path input, final Path output) throws IOException {
        createDirectory(output);
        Path copiedDir = output.resolve(getName(input));
        copyDirectoryTree(input, copiedDir);
    }

    /**
     * Copies the contents of a directory into another directory.
     * This method is public for documentation purposes.
     * It's usable, but it's recommended to use {@link FileUtil#copy} instead.
     *
     * @param input The input directory.
     * @param output The output directory.
     * @param inclusions A list of globs indicating paths included for copying. Leave blank to copy all inputs.
     * @throws IOException exception.
     */
    @Internal
    public static void copyDirectoryTree(final Path input, final Path output, final String... inclusions) throws IOException {
        createDirectory(output);
        final DirectoryTree tree = DirectoryTree.walk(input, inclusions);

        for (Path path : tree) {
            Path relative = input.relativize(path);
            Path copy = output.resolve(relative);
            Files.copy(path, copy);
        }
    }

    /**
     * Gets the file name of a provided path.
     *
     * @param path The path.
     * @return The file name.
     */
    public static String getName(final Path path) {
        return path.getFileName().toString();
    }

    /**
     * Gets the string value of the absolute path.
     *
     * @param path The path.
     * @return The absolute path.
     */
    public static String getAbsolutePath(final Path path) {
        return path.toAbsolutePath().toString();
    }

    /**
     * Checks if a file path exists.
     *
     * @param path The file path.
     * @return {@code true} if the path exists.
     */
    public static boolean exists(final Path path) {
        return Files.exists(path);
    }

    /**
     * Checks if a path is any kind of file (regular or directory).
     *
     * @param path The file path.
     * @return {@code true} if the path is any kind of file.
     */
    public static boolean isFile(final Path path) {
        return isRegularFile(path) || isDirectory(path);
    }

    /**
     * Checks if a path is a regular file.
     *
     * @param path The file path.
     * @return {@code true} if the path is a directory.
     */
    public static boolean isRegularFile(final Path path) {
        return Files.isRegularFile(path);
    }

    /**
     * Checks if a path is a directory.
     *
     * @param path The file path.
     * @return {@code true} if the path is a directory.
     */
    public static boolean isDirectory(final Path path) {
        return Files.isDirectory(path);
    }

    /**
     * Checks if the directory does not exist or if it's empty.
     *
     * @param directory The directory.
     * @return {@code true} if the directory is empty, or it does not exist. An exception is thrown if the path is not a directory.
     * @throws IOException exception.
     */
    public static boolean directoryMissingOrEmpty(final Path directory) throws IOException {
        return !exists(directory) || isDirectoryEmpty(directory);
    }

    /**
     * Checks if the directory is empty.
     *
     * @param directory The directory.
     * @return {@code true} if the directory is empty. An exception is thrown if the path is not a directory.
     * @throws IOException exception.
     */
    public static boolean isDirectoryEmpty(final Path directory) throws IOException {
        if (!isDirectory(directory)) {
            throw new IllegalArgumentException("Path " + directory + " is not a directory.");
        }

        final DirectoryTree tree = DirectoryTree.walk(directory);
        return tree.isEmpty();
    }

    /**
     * Checks if the path name has a match in the included array. Used for path tree filtering.
     *
     * @param path The path.
     * @param inclusions A list of globs indicating included paths.
     * @return {@code true} if the path name is included, and {@code false} if not.
     * If the inclusions are null, no filtering is needed, so the method returns {@code true}.
     */
    public static boolean isIncluded(final Path path, final String... inclusions) {
        if (inclusions.length == 0) return true;

        List<PathMatcher> matchers = Arrays.stream(inclusions)
                .map(s -> "glob:" + s)
                .map(FileSystems.getDefault()::getPathMatcher)
                .toList();

        return Util.anyMatch(matchers, m -> m.matches(path));
    }

    /**
     * Gets the file extension.
     *
     * @param path The file path.
     * @return The extension.
     */
    public static String getExtension(final Path path) {
        final String name = getName(path);
        return name.substring(name.lastIndexOf('.') + 1);
    }

    /**
     * Compresses a ZIP archive (or a JAR file) from a directory tree.
     *
     * @param tree The directory tree.
     * @param output The output archive.
     * @throws IOException exception.
     */
    public static void compressZip(final DirectoryTree tree, final Path output) throws IOException {
        try (ZipArchiveOutputStream zip = new ZipArchiveOutputStream(output)) {
            for (Path path : tree) {
                final Path relative = tree.relativize(path);

                // Ensure directories are defined as such in the entry name. Otherwise, empty files with the directory names get created.
                String entry = relative + (isDirectory(path) ? "/" : "");
                zip.putArchiveEntry(new ZipArchiveEntry(entry));
                zip.closeArchiveEntry();
            }
        }
    }

    /**
     * Extracts a ZIP archive (or a JAR file).
     *
     * @param archive The ZIP archive. Can include ZIPs or JARs.
     * @param output The output directory.
     * @param inclusions A list of globs indicating included paths.
     * @throws IOException exception.
     */
    public static void extractZip(final Path archive, final Path output, final String... inclusions) throws IOException {
        try (final InputStream in = Files.newInputStream(archive)) {
            final ZipArchiveInputStream zin = new ZipArchiveInputStream(in);
            ZipArchiveEntry entry;

            while ((entry = zin.getNextEntry()) != null) {
                final Path entryPath = output.resolve(entry.getName());
                if (!isIncluded(entryPath, inclusions)) continue;

                if (entry.isDirectory()) {
                    createDirectory(entryPath);
                } else {
                    createParentDirectory(entryPath);

                    try (OutputStream out = Files.newOutputStream(entryPath)) {
                        IOUtils.copy(zin, out);
                    }
                }
            }
        }
    }

    /**
     * Representation of a directory tree.
     *
     * <p> This is <b>not</b> meant to be directly instantiated with the constructor. Doing so ends up creating an empty tree,
     * Instead, use {@link DirectoryTree#walk(Path, String...)}. This function walks through the file tree and determines
     * the subpaths. </p>
     *
     * @param root The root directory.
     * @param paths The list of paths nested in the tree.
     */
    public record DirectoryTree(Path root, List<Path> paths) implements Iterable<Path> {

        /**
         * Adds an entry to the sub path list.
         *
         * @param root The root directory. Used to compare with the currently visited path entry; if the visited path
         * -is- the root directory, it does not get added.
         * @param entry The currently visited path.
         * @param paths The list to add the visited path to.
         * @return a {@link FileVisitResult} flag to tell the file visitor to continue walking the tree.
         */
        private static FileVisitResult add(final Path root, final Path entry, final List<Path> paths) {
            if (!entry.equals(root)) { // don't add the root directory into the list.
                paths.add(entry);
            }

            return FileVisitResult.CONTINUE;
        }

        /**
         * Walks the tree of the root directory and creates an instance of {@link DirectoryTree}.
         *
         * @param root The root directory.
         * @param inclusions A list of globs indicating included paths.
         * @return The directory tree.
         * @throws IOException exception.
         */
        public static DirectoryTree walk(final Path root, final String... inclusions) throws IOException {
            final List<Path> paths = new ArrayList<>();

            Files.walkFileTree(root, new SimpleFileVisitor<>() {

                @NonNull
                @Override
                public FileVisitResult preVisitDirectory(@NonNull Path dir, @NonNull BasicFileAttributes attrs) {
                    return isIncluded(dir, inclusions) ? add(root, dir, paths) : FileVisitResult.SKIP_SUBTREE;
                }

                @NonNull
                @Override
                public FileVisitResult visitFile(@NonNull Path file, @NonNull BasicFileAttributes attrs) {
                    return isIncluded(file, inclusions) ? add(root, file, paths) : FileVisitResult.CONTINUE;
                }
            });

            return new DirectoryTree(root, paths);
        }

        @NonNull
        @Override
        public Iterator<Path> iterator() {
            return paths.iterator();
        }

        /**
         * Relativizes the given path within the root of the directory tree.
         *
         * @param path The path.
         * @return The relativized path.
         */
        public Path relativize(final Path path) {
            return root.relativize(path);
        }

        /**
         * Checks if the directory tree is empty, meaning the directory contains no paths.
         *
         * @return {@code true} if the directory tree is empty.
         */
        public boolean isEmpty() {
            return paths.isEmpty();
        }
    }
}
