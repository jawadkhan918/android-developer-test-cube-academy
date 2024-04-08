package com.cube.cubeacademy.lib.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import kotlinx.coroutines.flow.Flow
@Dao
interface NomineeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMultipleNominee(nominations: List<Nominee>)

    @Query("SELECT * FROM Nominee")
    fun getAllNominee(): List<Nominee>

    @Query("SELECT * FROM Nominee WHERE `nomineeId`=:nomineeId")
    fun getNomineeById(nomineeId: String): Nominee
}