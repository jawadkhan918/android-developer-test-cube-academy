package com.cube.cubeacademy.lib.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable



@Entity(tableName = "Nomination")
@Serializable
data class Nomination(
	@PrimaryKey
	@SerializedName("nomination_id") val nominationId: String,
	@SerializedName("nominee_id") val nomineeId: String,
	@SerializedName("nominee_name") var nomineeName: String?,
	@SerializedName("reason") val reason: String,
	@SerializedName("process") val process: String,
	@SerializedName("date_submitted") val dateSubmitted: String,
	@SerializedName("closing_date") val closingDate: String
) : java.io.Serializable