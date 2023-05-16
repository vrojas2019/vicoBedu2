package org.bedu.gr.UI


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.bedu.gr.MainActivity
import org.bedu.gr.R


class SplashActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        imageView = findViewById(R.id.imageViewSplash)

        Handler().postDelayed({

            /*val transitionDrawable = TransitionDrawable(
                arrayOf(resources.getDrawable(R.drawable.splash), resources.getDrawable(
                    R.drawable.logosplash
                ))
            )
            transitionDrawable.isCrossFadeEnabled = true
            imageView.setImageDrawable(transitionDrawable)
            transitionDrawable.startTransition(500)*/

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }
}