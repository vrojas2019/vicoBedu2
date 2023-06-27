package org.bedu.gr.UI


import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.bedu.gr.MainActivity
import org.bedu.gr.R
import org.bedu.gr.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        animateLogo()

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }

    fun animateLogo(){
        ObjectAnimator.ofFloat(binding.imgLogo,"translationX",200f).apply {
            duration = 3_000
            interpolator = CycleInterpolator(1f)
            start()
        }
        ValueAnimator.ofFloat(0f,720f).run {
            addUpdateListener {
                val value = it.animatedValue as Float
                binding.imgLogo.rotationY = value
            }
            interpolator = AccelerateInterpolator()
            duration = 2_000
            start()
        }



    }
}