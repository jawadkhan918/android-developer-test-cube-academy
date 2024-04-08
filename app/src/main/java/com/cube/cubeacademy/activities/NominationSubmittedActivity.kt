package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cube.cubeacademy.R
import com.cube.cubeacademy.databinding.ActivityNominationSubmittedBinding

class NominationSubmittedActivity : AppCompatActivity(), View.OnClickListener {
	private lateinit var binding: ActivityNominationSubmittedBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityNominationSubmittedBinding.inflate(layoutInflater)
		setContentView(binding.root)

		populateUI()

	}

	private fun populateUI() {
		/**
		 * TODO: Add the logic for the two buttons (Don't forget that if you start to add a new nomination, the back button shouldn't come back here)
		 */
		attachClickListener()
	}

	private fun attachClickListener() {
		binding.createNewNomination.setOnClickListener(this)
		binding.backToHome.setOnClickListener(this)

	}

	override fun onClick(v: View?) {
		when (v?.id) {
			R.id.create_new_nomination -> {
				val intent = Intent(this, CreateNominationActivity::class.java)
				launchForResultOfSubmission.launch(intent)
			}
			R.id.back_to_home -> {
				finish()
			}
		}
	}

	private val launchForResultOfSubmission = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) { result ->
		if (result.resultCode == RESULT_OK) {
			finish()

		}
	}
}