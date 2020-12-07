package com.psi.nusaku.Activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.psi.nusaku.Activity.ui.welcome.Intro1Fragment
import com.psi.nusaku.Activity.ui.welcome.Intro2Fragment
import com.psi.nusaku.Activity.ui.welcome.Intro3Fragment
import com.psi.nusaku.Activity.ui.welcome.IntroSliderAdapter
import com.psi.nusaku.R

@Suppress("DEPRECATION")
class IntroSliderActivity : AppCompatActivity() {

    private val fragmentList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // making the status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setContentView(R.layout.activity_intro_slider)

        val adapter = IntroSliderAdapter(this)
//        vpIntroSlider.adapter = adapter

        fragmentList.addAll(listOf(
            Intro1Fragment(), Intro2Fragment(), Intro3Fragment()
        ))
        adapter.setFragmentList(fragmentList)

//        indicatorLayout.setIndicatorCount(adapter.itemCount)
//        indicatorLayout.selectCurrentPosition(0)

  //      registerListeners()
    }

//    private fun registerListeners() {
//     vpIntroSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//
//        })
//
//        btnSignUp.setOnClickListener {
//            val position = vpIntroSlider.currentItem
//
//            if (position < fragmentList.lastIndex) {
//                vpIntroSlider.currentItem = position + 1
//            } else {
//                startActivity(Intent(this, RegisterActivity::class.java))
//                finish()
//            }
//        }
//    }
}
