package com.erencol.sermon.view.specialdays

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erencol.sermon.R
import com.erencol.sermon.databinding.ActivitySpecialDaysBinding
import com.erencol.sermon.model.SpecialDay
import com.erencol.sermon.view.specialdays.SpecialDayAdapter
import com.erencol.sermon.view.specialdays.SpecialDaysViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class SpecialDaysActivity : AppCompatActivity() {
    private val ACTIVITY_CALLBACK = 1
    private var reviewInfo: ReviewInfo? = null
    private lateinit var reviewManager: ReviewManager
    lateinit var specialDaysBinding: ActivitySpecialDaysBinding
    val adapter = SpecialDayAdapter()

    var specialDaysViewModel: SpecialDaysViewModel = SpecialDaysViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        specialDaysBinding = DataBindingUtil.setContentView(this, R.layout.activity_special_days)
        specialDaysBinding.lifecycleOwner = this
        specialDaysBinding.specialDaysViewModel = specialDaysViewModel
        setListReligiousDays(specialDaysBinding.specialdaysrecyclerview)
        setToolBar()
        setupObserver()
        reviewGooglePlayReviewInterface()
    }

    private fun setListReligiousDays(listReligiousDays: RecyclerView) {
        specialDaysBinding.specialdaysrecyclerview.adapter = adapter
        specialDaysBinding.specialdaysrecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listReligiousDays.setAdapter(adapter)
        listReligiousDays.setLayoutManager(LinearLayoutManager(this))
        specialDaysViewModel.getReligiousDays()
    }

    fun setupObserver() {
       specialDaysViewModel.religiousDaysLivedata.observe(this, { religiousDays ->
           adapter.setSpecialDays(religiousDays.religiousDays)
           adapter.setOnItemClickListener { specialDay, position ->
                createBottomSheetDialog(specialDay)

           }
       })
    }

    @SuppressLint("SetTextI18n")
    fun createBottomSheetDialog(specialDay: SpecialDay) {

        val bottomSheetDialog = BottomSheetDialog(this);
        val bottomSheetView: View = LayoutInflater.from(this)
            .inflate(R.layout.special_day_detail_bottom_sheet, null)

        val tvTitle = bottomSheetView.findViewById<TextView>(R.id.tvTitle)
        val tvDescription = bottomSheetView.findViewById<TextView>(R.id.tvDescription)
        val tvDate = bottomSheetView.findViewById<TextView>(R.id.tvDate)
        val btnClose = bottomSheetView.findViewById<Button>(R.id.btnClose)

        tvTitle.text = specialDay.name
        tvDescription.text = specialDay.description
        tvDate.text = specialDay.miladi.day.toString() + "." + specialDay.miladi.month + " " + specialDay.miladi.weekday

        btnClose.setOnClickListener { _: View? -> bottomSheetDialog.dismiss() }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    fun reviewGooglePlayReviewInterface() {
        //Create the ReviewManager instance
        reviewManager = ReviewManagerFactory.create(this)

        //Request a ReviewInfo object ahead of time (Pre-cache)
        val requestFlow = reviewManager.requestReviewFlow()
        requestFlow.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                //Received ReviewInfo object
                reviewInfo = request.result
            } else {
                //Problem in receiving object
                reviewInfo = null
            }
        }

    }

    fun setToolBar (){
        setSupportActionBar(specialDaysBinding.toolbar as Toolbar?)
        supportActionBar?.title = resources.getString(R.string.special_days_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTIVITY_CALLBACK && resultCode == RESULT_OK) {
            Handler().postDelayed({
                reviewInfo?.let {
                    val flow = reviewManager.launchReviewFlow(this@SpecialDaysActivity, it)
                    flow.addOnSuccessListener {
                        //Showing toast is only for testing purpose, this shouldn't be implemented
                        //in production app.
                        Toast.makeText(
                                this@SpecialDaysActivity,
                                "Thanks for the feedback!",
                                Toast.LENGTH_LONG
                        ).show()
                    }
                    flow.addOnFailureListener {
                        //Showing toast is only for testing purpose, this shouldn't be implemented
                        //in production app.
                        Toast.makeText(this@SpecialDaysActivity, "${it.message}", Toast.LENGTH_LONG).show()
                    }
                    flow.addOnCompleteListener {
                        //Showing toast is only for testing purpose, this shouldn't be implemented
                        //in production app.
                        Toast.makeText(this@SpecialDaysActivity, "Completed!", Toast.LENGTH_LONG).show()
                    }
                }
            }, 3000)
        }
        super.onActivityResult(requestCode, resultCode, data)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            super.onBackPressed()
            return true
        } else {
            return  super.onOptionsItemSelected(item)
        }
    }
}