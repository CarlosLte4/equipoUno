package com.example.picobotella.data

import com.example.picobotella.model.Challenge
import kotlinx.coroutines.flow.Flow

class ChallengeRepository(private val challengeDao: ChallengeDao) {

    val challenges: Flow<List<Challenge>> = challengeDao.getListChallenge()

    suspend fun saveChallenge(challenge: Challenge) {
        challengeDao.saveChallenge(challenge)
    }

    suspend fun updateChallenge(challenge: Challenge) {
        challengeDao.updateChallenge(challenge)
    }

    suspend fun deleteChallenge(challenge: Challenge) {
        challengeDao.deleteChallenge(challenge)
    }
}
