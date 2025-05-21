package com.example.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.auth.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


private const val TAG ="MainActivity"
class MainActivity : AppCompatActivity() {
 private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var googleSignInClient: GoogleSignInClient

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        auth = FirebaseAuth.getInstance()

     binding.btnSign.setOnClickListener {
         val signInIntent = googleSignInClient.signInIntent
         startActivityForResult(signInIntent,1)
     }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign-In muvaffaqiyatli bo‘ldi, Firebase bilan autentifikatsiya qilamiz
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign-In muvaffaqiyatsiz bo‘ldi
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in muvaffaqiyatli bo‘ldi, foydalanuvchini UIga uzatamiz
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
     //               updateUI(user)
                   startActivity(Intent(this, MainActivity2::class.java))
                    Toast.makeText(this, "${user?.email}", Toast.LENGTH_SHORT).show()
                } else {
                    // Sign in xato bo‘ldi
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
     //               updateUI(null)
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


}