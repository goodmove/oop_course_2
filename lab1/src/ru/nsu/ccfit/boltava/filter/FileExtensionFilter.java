package ru.nsu.ccfit.boltava.filter;

import org.jetbrains.annotations.Contract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;

public class FileExtensionFilter implements IFilter {

    private final String mExtension;
    private static Pattern mPattern = Pattern.compile(".*/*.*?(\\..*)");

    @Contract("null -> fail")
    public FileExtensionFilter(String extension) {
        if (extension == null) throw new IllegalArgumentException();

        mExtension = extension.startsWith(".") ? extension : "." + extension;
    }

    @Override
    public boolean check(Path path) {
        Matcher matcher = mPattern.matcher(path.toString());
        return matcher.matches() && matcher.group(1).equals(mExtension);
    }

}
