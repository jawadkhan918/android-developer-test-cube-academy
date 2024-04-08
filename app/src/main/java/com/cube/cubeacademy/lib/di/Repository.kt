package com.cube.cubeacademy.lib.di

import com.cube.cubeacademy.BuildConfig
import com.cube.cubeacademy.lib.api.ApiService
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import com.cube.cubeacademy.lib.room.dao.NominationDao
import com.cube.cubeacademy.lib.room.dao.NomineeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Repository(
    val api: ApiService,
    val nominationDao: NominationDao,
    val nomineeDao: NomineeDao
) {

    suspend fun getAllNominations(): List<Nomination> {
        return api.getAllNominations().data
    }

    suspend fun createNomination(nomineeId: String, reason: String, process: String): Nomination? {
        var nomination = api.createNomination(nomineeId,reason,process).data
        insertNominationInDb(nomination)
        return nomination
    }


    suspend fun insertMultipleNominationsInDb(nominations: List<Nomination>) {
        withContext(Dispatchers.IO) {
            nominationDao.insertMultipleNominations(nominations)
        }
    }

    suspend fun insertNominationInDb(nomination:Nomination) {
        withContext(Dispatchers.IO) {
            nominationDao.insertNomination(nomination)
        }

    }


    /*
	* This function is responsible for retrieving the latest data for both Nomination and Nominees from the server
	*  and loading it into the local database.
	*
	* */
    suspend fun updateDbFromServer() {
        withContext(Dispatchers.IO) {
            var list = getAllNominations()
            var nomineesList = getAllNominees()
            insertMultipleNominationsInDb(list)
            insertMultipleNomineesInDb(nomineesList)
        }

    }
    suspend fun getAllNominees(): List<Nominee> {
        return api.getAllNominees().data
    }

    suspend fun getAllNomineeFromDb(): List<Nominee> {
        return withContext(Dispatchers.IO) {
            nomineeDao.getAllNominee()
        }
    }

    suspend fun insertMultipleNomineesInDb(nominees: List<Nominee>) {
        withContext(Dispatchers.IO) {
            nomineeDao.insertMultipleNominee(nominees)
        }
    }


    /*
	* This function is responsible for querying the local database from the nominee table using the nominee ID retrieved from the nomination table.
	* It retrieves the nominee name and sets it in the Nomination list accordingly,
	* returning the updated Nomination list with nominee names.
	*
	* */
    suspend fun getAllNominationsWithNomineesName(): List<Nomination> {
        return withContext(Dispatchers.IO) {
            val nominations = nominationDao.getAllNomination()
            nominations.forEach { nomination ->
                val nominee = nomineeDao.getNomineeById(nomination.nomineeId)
                nomination.nomineeName = "${nominee.firstName} ${nominee.lastName}"
            }
            nominations
        }
    }


}