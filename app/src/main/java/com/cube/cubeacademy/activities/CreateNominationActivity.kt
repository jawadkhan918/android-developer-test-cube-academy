package com.cube.cubeacademy.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.cube.cubeacademy.R
import com.cube.cubeacademy.databinding.ActivityCreateNominationBinding
import com.cube.cubeacademy.lib.adapters.CubeNameSpinnerAdapter
import com.cube.cubeacademy.lib.common.FAIR
import com.cube.cubeacademy.lib.common.NOT_SURE
import com.cube.cubeacademy.lib.common.NetworkUtils
import com.cube.cubeacademy.lib.common.EXTRA_SUCCESS_NOMINATION_KEY
import com.cube.cubeacademy.lib.common.UNFAIR
import com.cube.cubeacademy.lib.common.VERY_FAIR
import com.cube.cubeacademy.lib.common.VERY_UNFAIR
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nominee
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateNominationActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener,
    View.OnClickListener {
    private lateinit var binding: ActivityCreateNominationBinding

    @Inject
    lateinit var repository: Repository
    private var selectedProcess: String = ""
    private var selectedNominee: Nominee? = null
    private var selectedReason: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateNominationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        populateUI()
    }

    private fun populateUI() {
        /**
         * TODO: Populate the form after having added the views to the xml file (Look for TODO comments in the xml file)
         * 		 Add the logic for the views and at the end, add the logic to create the new nomination using the api
         * 		 The nominees drop down list items should come from the api (By fetching the nominee list)
         */


        /*
        *I have added the local database for easy retrieval and a better user experience.
        * Additionally, it helps me associate the nominee's name with the nomination Id
        * */
        loadDataFromDb()
        addListeners()

    }

    private fun loadDataFromDb() {
        lifecycleScope.launch {
            var listFromDb = repository.getAllNomineeFromDb()
            setUpCubeNameSpinnerAdapter(listFromDb)
        }
    }

    private fun setUpCubeNameSpinnerAdapter(nominees: List<Nominee>) {

        /*
       *
       * To handle the hint value for the Spinner, I had to create a custom adapter for the spinner dropdown.
       * However, the hint value is not being shown or selected in this custom adapter.
       * */

        val hintValue = getString(R.string.select_option)
        val list = mutableListOf<Nominee>().apply {
            add(Nominee("", "", "")) // Add empty item as hint
            addAll(nominees) // Add all nominees
        }
        binding.cubeNames.adapter = CubeNameSpinnerAdapter(
            this, hintValue, list
        ) // Custom Spinner Adapter to handle the hint/default value
        binding.mainScrollView.fullScroll(ScrollView.FOCUS_UP)
        checkSubmitButtonActivation()
    }


    private fun addListeners() {
        binding.submitButton.setOnClickListener(this)
        binding.backButton.setOnClickListener(this)
        binding.cubeReactions.setOnCheckedChangeListener(this)
        binding.cubeNames.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedNominee = parent?.getItemAtPosition(position) as Nominee
                checkSubmitButtonActivation()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.reasoning.addTextChangedListener { text ->
            selectedReason = text.toString()
            checkSubmitButtonActivation()
        }
    }

    private fun checkSubmitButtonActivation() {
        val allPermission = !selectedReason.trim()
            .isNullOrEmpty() && !selectedProcess.isNullOrEmpty() && selectedNominee != null && !selectedNominee!!.nomineeId.isNullOrEmpty()
        binding.submitButton.isEnabled = allPermission
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        selectedProcess = when (checkedId) {
            R.id.very_unfair -> VERY_UNFAIR
            R.id.unfair -> UNFAIR
            R.id.not_sure -> NOT_SURE
            R.id.fair -> FAIR
            R.id.very_fair -> VERY_FAIR
            else -> ""
        }
        checkSubmitButtonActivation()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submit_button -> {
                createNomination()
            }

            R.id.back_button -> {
                finish()
            }
        }
    }


    private fun createNomination() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            showProgress(true)
            lifecycleScope.launch {
                selectedNominee?.nomineeId?.let { nomineeId ->
                    val nomination =
                        repository.createNomination(nomineeId, selectedReason, selectedProcess)
                    nomination?.let { submittedSuccessfully() }
                }
            }
        } else {
            showMessage(getString(R.string.internet_not_available))
        }
    }

    private fun submittedSuccessfully() {
        showProgress(false)
        val intent = Intent()
        intent.putExtra(EXTRA_SUCCESS_NOMINATION_KEY, true)
        setResult(RESULT_OK, intent)
        finish()
        startActivity(
            Intent(
                this@CreateNominationActivity, NominationSubmittedActivity::class.java
            )
        )
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.submitButton.text = if (show) "" else getString(R.string.submit_nomination)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}