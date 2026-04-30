package com.ancientmc.commons.test;

import com.ancientmc.commons.file.CopyMode;
import com.ancientmc.commons.file.FileUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public abstract class FileFunction {
    protected Path input;
    protected Path output;
    protected String[] inclusions;
    protected CopyMode mode;

    public abstract void run() throws IOException;

    public FileFunction withInput(final Path input) {
        this.input = input;
        return this;
    }

    public FileFunction withOutput(final Path output) {
        this.output = output;
        return this;
    }

    public FileFunction withInclusions(final String... exclusions) {
        this.inclusions = exclusions;
        return this;
    }

    public FileFunction withCopyMode(final CopyMode mode) {
        this.mode = mode;
        return this;
    }

    public static class Download extends FileFunction {
        private URL url;

        public Download withUrl(final URL url) {
            this.url = url;
            return this;
        }

        @Override
        public void run() throws IOException {
            FileUtil.download(url, output);
        }
    }

    public static class FileCopy extends FileFunction {

        @Override
        public void run() throws IOException {
            FileUtil.copy(input, output, mode);
        }
    }

    public static class DirectoryCopy extends FileFunction {

        @Override
        public void run() throws IOException {
            FileUtil.copy(input, output, mode, inclusions);
        }
    }

    public static class Extract extends FileFunction {

        @Override
        public void run() throws IOException {
            FileUtil.extractZip(input, output, inclusions);
        }
    }

    public static class Compress extends FileFunction {
        @Override
        public void run() throws IOException {
            final FileUtil.DirectoryTree tree = FileUtil.DirectoryTree.walk(input);
            FileUtil.compressZip(tree, output);
        }
    }
}
