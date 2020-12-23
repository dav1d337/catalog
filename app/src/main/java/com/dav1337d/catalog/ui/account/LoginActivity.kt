package com.dav1337d.catalog.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dav1337d.catalog.MainActivity
import com.dav1337d.catalog.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        editTextTextEmailAddress.setText(auth.currentUser?.email?: "")
        Log.i(TAG, "currentUser:" + currentUser?.email.toString())
        // updateUI(currentUser)
    }

   fun createAccount(view: View) {
        Log.d(TAG, "createAccount:$email")
        if (!isFormValid()) {
            return
        }

  //      showProgressBar()

        val email = editTextTextEmailAddress.text.toString()
        val password = editTextTextPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Successfully created Account. Hi ${auth.currentUser?.email}!",
                        Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    navigateToHome()
                 //   updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message.toString()}",
                        Toast.LENGTH_SHORT).show()
                  //  updateUI(null)
                }

              //  hideProgressBar()
            }
    }

    private fun isFormValid(): Boolean {
        if (editTextTextEmailAddress.text.isNullOrEmpty() || editTextTextPassword.text.isNullOrEmpty()) {
            Toast.makeText(baseContext, "Email and password are necessary",
                Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun signIn(view: View) {
        Log.d(TAG, "signIn:$email")
        if (!isFormValid()) {
            return
        }

//        showProgressBar()

        val email = editTextTextEmailAddress.text.toString()
        val password = editTextTextPassword.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Successfully logged in. Hi ${auth.currentUser?.email}!",
                        Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    navigateToHome()
             //       updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message.toString()}",
                        Toast.LENGTH_SHORT).show()
              //      updateUI(null)
                }
          //      hideProgressBar()
            }
    }

    fun continueWithoutAccount(view: View) {
        navigateToHome()
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        val TAG = "LoginFragment"
    }
}