package com.chintan.datastoresample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import com.chintan.datastoresample.databinding.ActivityMainBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val scope = MainScope()

    private lateinit var binding: ActivityMainBinding

    private lateinit var dataStore: DataStore<Preferences>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // with Preferences DataStore
        dataStore = createDataStore(
            name = "settings"
        )

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
            // Unlike Proto DataStore, there's no type safety here.
            val name = currentPreferences[NAME_KEY] ?: ""
            val age = currentPreferences[AGE_KEY] ?: 0
            UserData(name, age)
        }


    private suspend fun saveUserDetails(userData: UserData) {
        dataStore.edit { userDetails ->
            // We can safely increment our counter without losing data due to races!
            userDetails[NAME_KEY] = userData.name
            userDetails[AGE_KEY] = userData.age
        }
    }
}