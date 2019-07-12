package com.guness.robolectric.sqlite.binaries;

public class SQLiteBinaries {

    private static final String ALMWORKS_VERSION = "1.0.392";
    private static final String SQLITE4JAVA = "sqlite4java";

    public enum OS {
        WINDOWS, LINUX, MAC
    }

    public enum ARCH {
        x86, x64
    }

    public static String getLibraryName(OS os, ARCH arch) {
        if (os == OS.MAC) {
            return "lib" + SQLITE4JAVA + "-osx-" + ALMWORKS_VERSION + ".dylib";
        } else if (arch == ARCH.x86) {
            switch (os) {
                case LINUX:
                    return "lib" + SQLITE4JAVA + "-linux-i386-" + ALMWORKS_VERSION + ".so";
                case WINDOWS:
                    return SQLITE4JAVA + "-win32-x86-" + ALMWORKS_VERSION + ".dll";
            }
        } else {
            switch (os) {
                case LINUX:
                    return "lib" + SQLITE4JAVA + "-linux-amd64-" + ALMWORKS_VERSION + ".so";
                case WINDOWS:
                    return SQLITE4JAVA + "-win32-x64-" + ALMWORKS_VERSION + ".dll";
            }
        }
        throw new UnsupportedOperationException("This OS + Arch combination is not supported");
    }
}
