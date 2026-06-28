package com.example.picobotella.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.picobotella.data.ChallengeDB
import com.example.picobotella.data.ChallengeRepository
import com.example.picobotella.model.Challenge
import kotlinx.coroutines.launch

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ChallengeRepository(
        ChallengeDB.getDatabase(application).challengeDao()
    )

    val challenges: LiveData<List<Challenge>> = repository.challenges.asLiveData()

    fun saveChallenge(description: String) {
        val cleanDescription = description.trim()
        if (cleanDescription.isBlank()) return

        viewModelScope.launch {
            repository.saveChallenge(Challenge(description = cleanDescription))
        }
    }

    fun updateChallenge(challenge: Challenge, description: String) {
        val cleanDescription = description.trim()
        if (cleanDescription.isBlank()) return

        viewModelScope.launch {
            repository.updateChallenge(challenge.copy(description = cleanDescription))
        }
    }

    fun deleteChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.deleteChallenge(challenge)
        }
    }
}
