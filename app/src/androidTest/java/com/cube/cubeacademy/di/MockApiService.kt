package com.cube.cubeacademy.di

import com.cube.cubeacademy.lib.api.ApiService
import com.cube.cubeacademy.lib.models.DataWrapper
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee

class MockApiService : ApiService {
	override suspend fun getAllNominations(): DataWrapper<List<Nomination>> {
		return DataWrapper(
			listOf(
				Nomination("1", "1", "name","reason", "process", "2023-10-11", "2023-11-11"),
				Nomination("2", "2", "name","reason", "process", "2023-10-11", "2023-11-11"),
				Nomination("1", "3", "name","reason", "process", "2023-10-11", "2023-11-11"),
				Nomination("2", "4", "name","reason", "process", "2023-10-11", "2023-11-11"),
			)
		)
	}

	override suspend fun getAllNominees(): DataWrapper<List<Nominee>> {
		return DataWrapper(
			listOf(
				Nominee("1", "FirstTest1", "LastTest1"),
				Nominee("2", "FirstTest2", "LastTest2"),
				Nominee("3", "FirstTest3", "LastTest3"),
			)
		)
	}

	override suspend fun createNomination(nomineeId: String, reason: String, process: String): DataWrapper<Nomination> {
		return DataWrapper(
			Nomination(nominationId ="3", nomineeId= nomineeId,nomineeName = "", reason=reason, process = process, dateSubmitted = "2023-10-11", closingDate = "2023-11-12")
		)
	}
}