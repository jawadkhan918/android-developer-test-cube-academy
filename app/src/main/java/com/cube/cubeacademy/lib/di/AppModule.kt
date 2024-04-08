package com.cube.cubeacademy.lib.di

import android.content.Context
import com.cube.cubeacademy.BuildConfig
import com.cube.cubeacademy.lib.api.ApiService
import com.cube.cubeacademy.lib.api.AuthTokenInterceptor
import com.cube.cubeacademy.lib.room.AppDatabase
import com.cube.cubeacademy.lib.room.dao.NominationDao
import com.cube.cubeacademy.lib.room.dao.NomineeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
	@Singleton
	@Provides
	fun provideApi(): ApiService = OkHttpClient().newBuilder().apply {
		addInterceptor(AuthTokenInterceptor())
	}.build().let {
		Retrofit.Builder()
			.client(it)
			.addConverterFactory(GsonConverterFactory.create())
			.baseUrl(BuildConfig.API_URL).build().create(ApiService::class.java)
	}

	@Singleton
	@Provides
	fun provideRepository(api: ApiService, nominationDao: NominationDao, nomineeDao: NomineeDao): Repository = Repository(api,nominationDao,nomineeDao)


	@Singleton
	@Provides
	fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
		return AppDatabase.getInstance(context)
	}

	@Provides
	fun provideNominationDao(appDatabase: AppDatabase): NominationDao {
		return appDatabase.nominationDao()
	}

	@Provides
	fun provideNomineeDao(appDatabase: AppDatabase): NomineeDao {
		return appDatabase.nomineeDao()
	}
}