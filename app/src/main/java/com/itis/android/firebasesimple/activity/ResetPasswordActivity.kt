package com.itis.android.firebasesimple.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.itis.android.firebasesimple.R
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        auth = FirebaseAuth.getInstance()

        btn_reset_password.setOnClickListener({
            var email = edt_reset_email.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
            }

            auth?.sendPasswordResetEmail(email)?.addOnCompleteListener({
                if (it.isSuccessful) {
                    Toast.makeText(this@ResetPasswordActivity, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this@ResetPasswordActivity, "Fail", Toast.LENGTH_LONG).show()
                }
            })
        })
    }
}
