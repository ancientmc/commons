package com.ancientmc.util;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.file.PathUtils;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

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
     * Creates the parent of the provided directory (and any super-parent directory) if it doesn't exist.
     *
     * @param path The path.
     * @throws IOException exception.
     */
    public static void createParentDirectory(final Path path) throws IOException {
        createDirectory(path.getParent());
    }

    /**
     * Basic copy method.
     *
     * @param input The input file or directory.
     * @param output The output file or directory.
     * @param exclusions A list of regexes indicating excluded paths from copying.
     * @throws IOException exception.
     */
    public static void copy(final Path input, final Path output, final String... exclusions) throws IOException {
        copy(input, output, false, exclusions);
    }

    /**
     * Basic copy method.
     *
     * @param input The input file or directory.
     * @param output The output file or directory.
     * @param wholeDirectory For directory copying. Set to {@code true} if you're copying an entire directory object into another directory (Path target_dir/src_dir).
     * Set to {@code false} if you're copying the directory *contents* into another directory (Path target_dir/src_contents).
     * @param exclusions A list of regexes indicating excluded paths from copying.
     * @throws IOException exception.
     */
    public static void copy(final Path input, final Path output, final boolean wholeDirectory, final String... exclusions) throws IOException {
        if (isDirectory(input)) {
            copyDirectory(input, output, wholeDirectory, exclusions);
        } else {
            copyFile(input, output);
        }
    }

    /**
     * Copies a single file. Handles whether the target is another file path or a directory.
     *
     * @param input The input file.
     * @param output The output file or directory.
     * @throws IOException exception.
     */
    public static void copyFile(final Path input, final Path output) throws IOException {
        if (isDirectory(output)) {
            createDirectory(output);
            PathUtils.copyFileToDirectory(input, output);
        } else {
            createParentDirectory(output);
            Files.copy(input, output);
        }
    }

    /**
     * Copies a directory. Handles whether you're copying the entire directory into another path or only copying its contents.
     *
     * @param input The input directory.
     * @param output The output directory.
     * @param wholeDirectory For directory copying. Set to {@code true} if you're copying an entire directory object into another directory (Path target_dir/src_dir).
     * Set to {@code false} if you're copying the directory *contents* into another directory (Path target_dir/src_contents).
     * @param exclusions A list of regexes indicating excluded paths from copying.
     * @throws IOException exception.
     */
    public static void copyDirectory(final Path input, final Path output, final boolean wholeDirectory, final String... exclusions) throws IOException {
        if (wholeDirectory) {
            copyWholeDirectory(input, output);
        } else {
            copyDirectoryContents(input, output, exclusions);
        }
    }

    public static void copyWholeDirectory(final Path input, final Path output) throws IOException {
        createDirectory(output);
        Path copiedDir = output.resolve(getName(input));
        copyDirectoryContents(input, copiedDir);
    }

    /**
     * Copies the contents of a directory into another directory.
     *
     * @param input The input directory.
     * @param output The output directory.
     * @param exclusions A list of regexes indicating excluded paths from copying.
     * @throws IOException exception.
     */
    public static void copyDirectoryContents(final Path input, final Path output, final String... exclusions) throws IOException {
        createDirectory(output);
        final DirectoryTree tree = DirectoryTree.walk(input, exclusions);

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
        return Util.get(
                isFile(path),
                path.getFileName().toString(),
                path + " is not a file.");
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
     * Checks if a file name is not in the exclusion list.
     * @param name The file name.
     * @param exclusions A list of regexes indicating excluded paths.
     * @return t
     */
    private static boolean notExcluded(final String name, final String... exclusions) {
        return Arrays.stream(exclusions).noneMatch(name::matches);
    }

    /**
     * Gets the file extension.
     *
     * @param path The file path.
     * @return The extension. Throws if the provided path is not a regular file.
     */
    public static String getExtension(final Path path) {
        final String name = getName(path);
        return Util.get(
                isRegularFile(path),
                name.substring(name.lastIndexOf('.') + 1),
                path + " is not a file."
        );
    }

    /**
     * Creates a {@link ZipArchiveEntry} tree containing the contents of the provided archive file.
     *
     * @param archive The archive.
     * @param exclusions A list of regexes indicating excluded paths.
     * @return The list of archive entries.
     * @throws IOException exception.
     */
    private static List<ZipArchiveEntry> zipTree(final Path archive, final String... exclusions) throws IOException {
        try (InputStream in = Files.newInputStream(archive)) {
            final ZipArchiveInputStream zin = new ZipArchiveInputStream(in);
            final List<ZipArchiveEntry> entries = Util.list(zin.iterator().asIterator());

            if (exclusions != null) {
                return entries.stream().filter(entry -> notExcluded(entry.getName(), exclusions)).toList();
            } else {
                return entries;
            }
        }
    }

    /**
     * Extracts a ZIP archive (or a JAR file).
     *
     * @param archive The ZIP archive. Can include ZIPs or JARs.
     * @param output The output directory.
     * @param exclusions A list of regexes indicating excluded paths.
     * @throws IOException exception.
     */
    public static void extract(final Path archive, final Path output, final String... exclusions) throws IOException {
        final List<ZipArchiveEntry> entries = zipTree(archive, exclusions);

        for (ZipArchiveEntry entry : entries) {
            final Path entryPath = output.resolve(entry.getName());
            createParentDirectory(entryPath);

            try (OutputStream out = Files.newOutputStream(entryPath)) {
                Files.copy(entryPath, out);
            }
        }
    }

    /**
     * Representation of a directory tree.
     *
     * <p> This is <b>not</b> meant to be instantiated with the constructor.
     * Use {@link DirectoryTree#walk(Path, String...)} to call an instance of this object. </p>
     *
     * @param paths The list of paths nested in the tree.
     */
    public record DirectoryTree(List<Path> paths) implements Iterable<Path> {

        /**
         * Checks if the path is excluded.
         *
         * @param path The path.
         * @param exclusions A list of regexes indicating excluded paths.
         * @return {@code True} if the path name is excluded, and {@code false} if not. If the exclusions are null, the method returns {@code false}.
         */
        private static boolean isExcluded(final Path path, final String... exclusions) {
            if (exclusions == null) return false;
            return Util.anyMatch(exclusions, getName(path)::matches);
        }

        /**
         * Adds an entry to the sub path list.
         *
         * @param root The root directory. Used to compare with the currently visited path entry; if the visited path
         * is the root directory, it does not get added.
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
         * @param exclusions A list of regexes indicating excluded paths.
         * @return The directory tree.
         * @throws IOException exception.
         */
        public static DirectoryTree walk(final Path root, final String... exclusions) throws IOException {
            final List<Path> paths = new ArrayList<>();

            Files.walkFileTree(root, new SimpleFileVisitor<>() {

                @NonNull
                @Override
                public FileVisitResult preVisitDirectory(@NonNull Path dir, @NonNull BasicFileAttributes attrs) {
                    return isExcluded(dir, exclusions) ? FileVisitResult.SKIP_SUBTREE : add(root, dir, paths);
                }

                @NonNull
                @Override
                public FileVisitResult visitFile(@NonNull Path file, @NonNull BasicFileAttributes attrs) {
                    return isExcluded(file, exclusions) ? FileVisitResult.TERMINATE : add(root, file, paths);
                }
            });

            return new DirectoryTree(paths);
        }

        @NonNull
        @Override
        public Iterator<Path> iterator() {
            return paths.iterator();
        }
    }
}
