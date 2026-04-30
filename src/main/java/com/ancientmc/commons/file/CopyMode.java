package com.ancientmc.commons.file;

import java.nio.file.Path;

/**
 * Flags that determine how {@link FileUtil#copy(Path, Path, CopyMode, String...)} is executed.
 * These flags let you define what kind of input is being copied, as well as what kind of output that is being targeted.
 *
 * @author moist-mason
 */
public enum CopyMode {

    /**
     * Indicates that the copying action has a file input and a file output.
     * Executes {@link FileUtil#copyFileToFile(Path, Path)} when called.
     */
    FILE_FILE,

    /**
     * Indicates that the copying action has a file input and a directory output.
     * Executes {@link FileUtil#copyFileToDirectory(Path, Path)} when called.
     */
    FILE_DIRECTORY,

    /**
     * Indicates that the copying action has a directory input and a directory output (directory-in-directory).
     * Executes {@link FileUtil#copyDirectoryToDirectory(Path, Path)} when called.
     */
    DIRECTORY_DIRECTORY,

    /**
     * Indicates that the copying action has a directory tree input and a directory output.
     * Helps execute {@link FileUtil#copyDirectoryTree(Path, Path, String...)}.
     */
    TREE_DIRECTORY
}
