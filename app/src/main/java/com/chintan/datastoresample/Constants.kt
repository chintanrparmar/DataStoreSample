package com.chintan.datastoresample

import androidx.datastore.preferences.preferencesKey

//Lets define some keys for our preference
val NAME_KEY = preferencesKey<String>("name")
val AGE_KEY = preferencesKey<Int>("age")