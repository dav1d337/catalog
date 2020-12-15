//package com.dav1337d.catalog.ui.account
//
//import android.accounts.AccountManager
//import android.accounts.AccountManagerCallback
//import android.accounts.AccountManagerFuture
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.android.volley.AuthFailureError
//import com.android.volley.Response
//import com.android.volley.toolbox.StringRequest
//import com.dav1337d.catalog.R
//import com.dav1337d.catalog.ui.App
//import com.dav1337d.catalog.util.Singletons
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.tasks.Task
//import com.google.android.material.snackbar.Snackbar
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import kotlinx.android.synthetic.main.google_login_fragment.view.*
//import java.io.FileInputStream
//import java.net.HttpURLConnection
//import java.net.URL
//import java.util.*
//
//
//class GoogleLoginFragment: Fragment() {
//
//    private lateinit var mGoogleSignInClient: GoogleSignInClient
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val rootView = inflater.inflate(R.layout.google_login_fragment, container, false)
//
//        rootView.sign_in_button.setOnClickListener {
//            val signInIntent = mGoogleSignInClient.signInIntent
//            startActivityForResult(signInIntent, 666)
//        }
//
//        return rootView
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        // Check for existing Google Sign In account, if the user is already signed in
//        // the GoogleSignInAccount will be non-null.
//        val account = auth.currentUser
//        Log.i("hallo account", account?.email.toString())
//     //   updateUI(account)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Configure sign-in to request the user's ID, email address, and basic
//        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
//        auth = FirebaseAuth.getInstance()
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == 666) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//        }
//    }
//
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account = completedTask.getResult(ApiException::class.java)
//            Log.i("Login hallo", "test1 " + account?.email)
//
//            val am: AccountManager = AccountManager.get(requireContext())
//            val options = Bundle()
//
//            am.getAuthToken(
//                account!!.account,                     // Account retrieved using getAccountsByType()
//                "https://www.googleapis.com/auth/books",            // Auth scope
//                options,                        // Authenticator-specific options
//                requireActivity(),                           // Your activity
//                OnTokenAcquired(),              // Callback called when a token is successfully acquired
//                Handler {
//                    Log.i("hallo error", it.toString())
//                    true
//                }              // Callback called if an error occurs
//            )
//
//            firebaseAuthWithGoogle(account!!.idToken!!)
//           // updateUI(account)
//        } catch (e: ApiException) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.i("Login hallo", "signInResult:failed code=" + e.statusCode)
//         //   updateUI(null)
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(idToken: String) {
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.i("hallo", "signInWithCredential:success")
//                    val user = auth.currentUser
//                    Log.i("hallo", "test2 " + user?.email)
//                 //   updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.i("hallo", "signInWithCredential:failure", task.exception)
//                    // ...
//                    Snackbar.make(requireView(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
//                //    updateUI(null)
//                }
//
//                // ...
//            }
//
//        auth.getAccessToken(true).addOnCompleteListener {task ->
//            if (task.isSuccessful) {
//                Log.i("hallo success", task.result?.token)
//
//                val token = task.result?.token
//
//            } else {
//                Log.i("hallo fail", task.exception.toString())
//            }
//        }
//
//    }
//
//}
//
//
//private class OnTokenAcquired : AccountManagerCallback<Bundle> {
//
//    override fun run(result: AccountManagerFuture<Bundle>) {
//        // Get the result of the operation from the AccountManagerFuture.
//        val bundle: Bundle = result.result
//
//        // The token is a named value in the bundle. The name of the value
//        // is stored in the constant AccountManager.KEY_AUTHTOKEN.
//        val token: String = bundle.getString(AccountManager.KEY_AUTHTOKEN)!!
//        Log.i("hallo token", token)
//
//
//
//        val url2 = URL("https://www.googleapis.com/books/v1/mylibrary/bookshelves/0/addVolume?volumeId=r7rbDwAAQBAJ&key=AIzaSyAQUsp3YZkeorxwt_WRzcTYl1p9xHKTBD0")
//        val conn = url2.openConnection() as HttpURLConnection
//        conn.apply {
//            requestMethod = "POST"
//            addRequestProperty("Content-Type", "application/json")
//            addRequestProperty("client_id", "337662399009-b6i0kv8pus6d4jp11p4i71a0fnereipp.apps.googleusercontent.com")
//            addRequestProperty("client_secret", "yntpA1y0xUkcmrmxR3HEqIR4")
//            setRequestProperty("Authorization", "OAuth $token")
//        }
//
//
//    }
//}
