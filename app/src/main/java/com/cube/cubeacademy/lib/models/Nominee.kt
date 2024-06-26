package com.cube.cubeacademy.lib.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Nominee")
data class Nominee(
	@PrimaryKey
	@SerializedName("nominee_id") val nomineeId: String,
	@SerializedName("first_name") val firstName: String,
	@SerializedName("last_name") val lastName: String
)
