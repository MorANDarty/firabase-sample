package com.itis.android.firebasesimple.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.itis.android.firebasesimple.R
import kotlinx.android.synthetic.main.activity_phone_sign_in.*
import java.util.concurrent.TimeUnit

class PhoneSignInActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    private lateinit var verificationCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var phoneVerificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_sign_in)
        auth = FirebaseAuth.getInstance()
        btn_submit.setOnClickListener({ sendCode() })
        btn_confirm.setOnClickListener({ verifyCode() })
    }

    fun sendCode() {
        var phoneNumber = et_phone_number.text.toString()
        setUpVerificationCallbacks()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, verificationCallbacks)
    }

    private fun setUpVerificationCallbacks() {
        verificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredentials(credential)
            }

            override fun onVerificationFailed(e: FirebaseException?) {
                Log.d(Log.ERROR.toString(), "Invalid credential: " + e?.getLocalizedMessage());
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                phoneVerificationId = verificationId
                resendToken = token
            }
        }
    }

    fun verifyCode() {
        var code: String = et_code.text.toString()
        var credential = PhoneAuthProvider.getCredential(phoneVerificationId, code)
        signInWithPhoneAuthCredentials(credential)
    }

    private fun signInWithPhoneAuthCredentials(credential: PhoneAuthCredential) {
        auth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        val intent = Intent(this@PhoneSignInActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
    }
}
