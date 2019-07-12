package com.guness.robolectric.sqlite

import com.guness.robolectric.sqlite.library.SQLiteLibraryLoader
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun loadSQLiteLibrary() {
        SQLiteLibraryLoader.load()
    }
}
