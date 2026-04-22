package com.ancientmc.util;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;
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
     * Creates the parent directory of the path if it doesn't exist.
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
                new IOException(path + " is not a file."));
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
        if (!isDirectory(directory)) {
            throw new IllegalArgumentException("Path " + directory + " is not a directory.");
        }

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
     * Checks if the path name is in the exclusion array. Used for path tree filtering.
     *
     * @param name The path.
     * @param exclusions A list of regexes indicating excluded paths.
     * @return {@code True} if the path name is excluded, and {@code false} if not.
     * If the exclusions are null, the method returns {@code false}.
     */
    public static boolean isExcluded(final String name, final String... exclusions) {
        if (exclusions == null) return false;
        return Util.anyMatch(exclusions, name::matches);
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
                new IOException(path + " is not a file.")
        );
    }

    /**
     * Creates a {@link ZipArchiveEntry} tree containing the contents of the provided archive file.
     *
     * @param zin The archive.
     * @param exclusions A list of regexes indicating excluded paths.
     * @return The list of archive entries.
     * @throws IOException exception.
     */
    private static List<ZipArchiveEntry> zipTree(final ZipArchiveInputStream zin, final String... exclusions) throws IOException {
        final List<ZipArchiveEntry> entries = Util.list(zin.iterator().asIterator());
        return Util.filteredList(entries, entry -> !isExcluded(entry.getName(), exclusions));
    }

    /**
     * Compresses a ZIP archive (or a JAR file) from a directory tree.
     *
     * @param tree The directory tree.
     * @param output The output archive.
     * @throws IOException exception.
     */
    private static void compressZip(final DirectoryTree tree, final Path output) throws IOException {
        try (ZipArchiveOutputStream zip = new ZipArchiveOutputStream(output)) {
            for (Path path : tree) {
                final Path relative = tree.relativize(path);
                zip.putArchiveEntry(new ZipArchiveEntry(relative.toString()));
                zip.closeArchiveEntry();
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
    public static void extractZip(final Path archive, final Path output, final String... exclusions) throws IOException {
        try (final InputStream in = Files.newInputStream(archive)) {
            final ZipArchiveInputStream zin = new ZipArchiveInputStream(in);
            final List<ZipArchiveEntry> entries = zipTree(zin, exclusions);

            for (ZipArchiveEntry entry : entries) {
                if (!zin.canReadEntryData(entry)) continue;
                final Path entryPath = output.resolve(entry.getName());
                createParentDirectory(entryPath);

                if (entry.isDirectory()) {
                    createDirectory(entryPath);
                }

                try (OutputStream out = Files.newOutputStream(entryPath)) {
                    IOUtils.copy(zin, out);
                }
            }
        }
    }

    /**
     * Representation of a directory tree.
     *
     * <p> This is <b>not</b> meant to be directly instantiated with the constructor.
     * Instead, use {@link DirectoryTree#walk(Path, String...)}. This function walks through the file tree and determines
     * the subpaths. </p>
     *
     * @param paths The list of paths nested in the tree.
     */
    public record DirectoryTree(Path root, List<Path> paths) implements Iterable<Path> {

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
                    return isExcluded(getName(dir), exclusions) ? FileVisitResult.SKIP_SUBTREE : add(root, dir, paths);
                }

                @NonNull
                @Override
                public FileVisitResult visitFile(@NonNull Path file, @NonNull BasicFileAttributes attrs) {
                    return isExcluded(getName(file), exclusions) ? FileVisitResult.TERMINATE : add(root, file, paths);
                }
            });

            return new DirectoryTree(root, paths);
        }

        @NonNull
        @Override
        public Iterator<Path> iterator() {
            return paths.iterator();
        }

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
