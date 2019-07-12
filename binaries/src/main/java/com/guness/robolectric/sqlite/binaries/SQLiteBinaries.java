package com.guness.robolectric.sqlite.binaries;

public class SQLiteBinaries {
    public enum OS {
        WINDOWS, LINUX, MAC
    }

    public enum ARCH {
        x86, x64
    }

    public static String getLibraryName(OS os, ARCH arch) {
        if (os == OS.MAC) {
            return "libsqlite4java-osx.dylib";
        } else if (arch == ARCH.x86) {
            switch (os) {
                case LINUX:
                    return "libsqlite4java-linux-i386.so";
                case WINDOWS:
                    return "sqlite4java-win32-x86.dll";
            }
        } else {
            switch (os) {
                case LINUX:
                    return "libsqlite4java-linux-amd64.so";
                case WINDOWS:
                    return "sqlite4java-win32-x64.dll";
            }
        }
        throw new UnsupportedOperationException("This OS + Arch combination is not supported");
    }
}
