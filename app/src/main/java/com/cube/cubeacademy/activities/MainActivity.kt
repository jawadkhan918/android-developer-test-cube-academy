package com.cube.cubeacademy.activities


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cube.cubeacademy.R
import com.cube.cubeacademy.databinding.ActivityMainBinding
import com.cube.cubeacademy.lib.adapters.NominationsRecyclerViewAdapter
import com.cube.cubeacademy.lib.common.NetworkUtils
import com.cube.cubeacademy.lib.common.EXTRA_SUCCESS_NOMINATION_KEY
import com.cube.cubeacademy.lib.di.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var repository: Repository
    lateinit var nominationsAdapter: NominationsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        populateUI()
    }

    private fun populateUI() {
        /**
         * TODO: Populate the UI with data in this function
         * 		 You need to fetch the list of user's nominations from the api and put the data in the recycler view
         * 		 And also add action to the "Create new nomination" button to go to the CreateNominationActivity
         */

        attachClickListener()
        initializeAdapter()
        loadDataFromDb()

        /*
         * This method will check if the network is currently connected to the device.
         * It provides a basic connection check and does not handle cases where the network is connected but the internet is slow or not working.
         *
         * */
        if (NetworkUtils.isNetworkAvailable(this)) {
            updateDbFromServer()
        }
    }

    private fun attachClickListener() {
        binding.createButton.setOnClickListener(this)
    }

    private fun initializeAdapter() {
        nominationsAdapter = NominationsRecyclerViewAdapter()
        binding.nominationsList.layoutManager = LinearLayoutManager(this)
        binding.nominationsList.adapter = nominationsAdapter
    }

    private fun updateDbFromServer() {
        lifecycleScope.launch {
            repository.updateDbFromServer()
            loadDataFromDb()
        }
    }

    private fun loadDataFromDb() {

        /*
       *I have added the local database for easy retrieval and a better user experience.
       * Additionally, it helps me associate the nominee's name with the nomination Id
       * */


        lifecycleScope.launch {
            var listFromDb = repository.getAllNominationsWithNomineesName()
            handleEmptyList(listFromDb.size)
            nominationsAdapter.submitList(listFromDb)
        }
    }

    private fun handleEmptyList(listSize: Int) {
        val isEmpty = listSize == 0
        binding.emptyContainer.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.nominationsList.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.create_button -> {
                moveToTheCreateNominationActivity()
            }
        }

    }

    private fun moveToTheCreateNominationActivity() {
        val intent = Intent(this, CreateNominationActivity::class.java)
        launchForResultOfSubmission.launch(intent)
    }

    private val launchForResultOfSubmission = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val isSuccessfulSubmission =
                result.data?.getBooleanExtra(EXTRA_SUCCESS_NOMINATION_KEY, false)
            if (isSuccessfulSubmission == true)
                loadDataFromDb()

        }
    }


}