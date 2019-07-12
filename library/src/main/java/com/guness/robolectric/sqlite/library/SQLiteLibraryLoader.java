package com.guness.robolectric.sqlite.library;

import com.almworks.sqlite4java.SQLite;
import com.almworks.sqlite4java.SQLiteException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.guness.robolectric.sqlite.binaries.SQLiteBinaries;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Initializes sqlite native libraries.
 */
public class SQLiteLibraryLoader {
    private static SQLiteLibraryLoader instance;
    private static final String SQLITE4JAVA = "sqlite4java";

    private final LibraryNameMapper libraryNameMapper;
    private boolean loaded;

    public SQLiteLibraryLoader() {
        this(DEFAULT_MAPPER);
    }

    public SQLiteLibraryLoader(LibraryNameMapper mapper) {
        libraryNameMapper = mapper;
    }

    private static final LibraryNameMapper DEFAULT_MAPPER = System::mapLibraryName;

    public static synchronized void load() {
        if (instance == null) {
            instance = new SQLiteLibraryLoader();
        }
        instance.doLoad();
    }

    public void doLoad() {
        if (loaded) {
            return;
        }
        final long startTime = System.currentTimeMillis();
        File tempDir = Files.createTempDir();
        tempDir.deleteOnExit();
        File extractedLibraryPath = new File(tempDir, getLibName());
        try (FileOutputStream outputStream = new FileOutputStream(extractedLibraryPath)) {
            getLibraryByteSource().copyTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Cannot extract SQLite library into " + extractedLibraryPath, e);
        }
        loadFromDirectory(tempDir);
        logWithTime("SQLite natives prepared in", startTime);
    }

    public String getLibClasspathResourceName() {
        return SQLiteBinaries.getLibraryName(getOS(), getArchitecture());
    }

    private ByteSource getLibraryByteSource() {
        return Resources.asByteSource(Resources.getResource("libs/" + getLibClasspathResourceName()));
    }

    private void logWithTime(final String message, final long startTime) {
        log(message + " " + (System.currentTimeMillis() - startTime));
    }

    private void log(final String message) {
        System.out.println(message);
    }

    @VisibleForTesting
    public boolean isLoaded() {
        return loaded;
    }

    private void loadFromDirectory(final File libPath) {
        // configure less verbose logging
        Logger.getLogger("com.almworks.sqlite4java").setLevel(Level.WARNING);

        SQLite.setLibraryPath(libPath.getAbsolutePath());

        try {
            log("SQLite version: library " + SQLite.getLibraryVersion() + " / core " + SQLite.getSQLiteVersion());
        } catch (SQLiteException e) {
            throw new RuntimeException(e);
        }
        loaded = true;
    }

    private String getLibName() {
        return libraryNameMapper.mapLibraryName(SQLITE4JAVA);
    }

    private SQLiteBinaries.OS getOS() {
        String name = System.getProperty("os.name").toLowerCase(Locale.US);
        if (name.contains("win")) {
            return SQLiteBinaries.OS.WINDOWS;
        } else if (name.contains("linux")) {
            return SQLiteBinaries.OS.LINUX;
        } else if (name.contains("mac")) {
            return SQLiteBinaries.OS.MAC;
        } else {
            throw new UnsupportedOperationException("Architecture '" + name + "' is not supported by SQLite library");
        }
    }

    private SQLiteBinaries.ARCH getArchitecture() {
        String arch = System.getProperty("os.arch").toLowerCase(Locale.US).replaceAll("\\W", "");
        if ("i386".equals(arch) || "x86".equals(arch)) {
            return SQLiteBinaries.ARCH.x86;
        } else {
            return SQLiteBinaries.ARCH.x64;
        }
    }

    public interface LibraryNameMapper {
        String mapLibraryName(String name);
    }
}
