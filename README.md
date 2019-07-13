# RobolectricSQLite
[![](https://jitpack.io/v/guness/RobolectricSQLite.svg)](https://jitpack.io/#guness/RobolectricSQLite)

Some notes:
- SQLiteLibraryLoader is taken (and heavily modified)from https://github.com/robolectric/robolectric/blob/master/shadows/framework/src/main/java/org/robolectric/shadows/util/SQLiteLibraryLoader.java

## How to use

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Add the dependency

	dependencies {
	        implementation 'com.github.guness:RobolectricSQLite:1.0.2'
	}

Call in code before Robolectric 


    
    @Before
    @CallSuper
    open fun setUp() {
        com.guness.robolectric.sqlite.library.SQLiteLibraryLoader.load()
    }
