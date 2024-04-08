package com.cube.cubeacademy.lib.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cube.cubeacademy.lib.models.Nomination
import kotlinx.coroutines.flow.Flow
@Dao
interface NominationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNomination(nomination: Nomination):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultipleNominations(nominations: List<Nomination>)

    @Query("SELECT * FROM Nomination")
    fun getAllNomination(): List<Nomination>
}