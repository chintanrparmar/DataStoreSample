package com.chintan.datastoresample

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


//Lets define some keys for our preference
val NAME_KEY = stringPreferencesKey("name")
val AGE_KEY = intPreferencesKey("age")