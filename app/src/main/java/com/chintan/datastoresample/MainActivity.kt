package com.chintan.datastoresample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.chintan.datastoresample.databinding.ActivityMainBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val scope = MainScope() // Coroutines Scope for executing suspend function

    private lateinit var binding: ActivityMainBinding

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "userPref") // declaring and initializing the datastore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.writeBtn.setOnClickListener {

            if (binding.nameTextField.editText?.text.toString()
                    .isNotEmpty() && binding.nameTextField.editText?.text.toString().isNotEmpty()
            ) {

                scope.launch {
                    saveUserDetails(
                        UserData(
                            binding.nameTextField.editText?.text.toString(),
                            binding.ageTextField.editText?.text.toString().toInt()
                        )
                    )
                }

            }
        }

        binding.readBtn.setOnClickListener {
            scope.launch {
                readUserData().collect { userData ->
                    binding.resultTv.text =
                        "Hello! My name is ${userData.name}\nI am ${userData.age} years old."
                }
            }
        }

    }


    private fun readUserData(): Flow<UserData> = dataStore.data
        .map { currentPreferences ->
            // Lets fetch the data from our DataStore by using the same key which we used earlier for storing the data.
            val name = currentPreferences[NAME_KEY] ?: ""
            val age = currentPreferences[AGE_KEY] ?: 0
            UserData(name, age)
        }


    private suspend fun saveUserDetails(userData: UserData) {
        dataStore.edit { userDetails ->
            // Lets save out data to DataStore!
            userDetails[NAME_KEY] = userData.name //Key = Value
            userDetails[AGE_KEY] = userData.age
        }
    }
}