package com.ancientmc.commons.test;

import com.ancientmc.util.FileUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public abstract class FileFunction {
    protected Path input;
    protected Path output;
    protected String[] inclusions;

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
            FileUtil.copyFile(input, output);
        }
    }

    public static class DirectoryCopy extends FileFunction {

        @Override
        public void run() throws IOException {
            FileUtil.copyDirectory(input, output, true);
        }
    }

    public static class Extract extends FileFunction {

        @Override
        public void run() throws IOException {
            FileUtil.extractZip(input, output, inclusions);
        }
    }
}
