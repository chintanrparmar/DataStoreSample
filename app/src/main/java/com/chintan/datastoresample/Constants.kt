package com.chintan.datastoresample

import androidx.datastore.preferences.preferencesKey

val NAME_KEY = preferencesKey<String>("name")
val AGE_KEY = preferencesKey<Int>("age")