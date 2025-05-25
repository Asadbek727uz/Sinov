package com.example.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.auth.databinding.ActivityMain2Binding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class MainActivity2 : AppCompatActivity() {
  private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
         auth = FirebaseAuth.getInstance()
        Picasso.get() .load(auth.currentUser?.photoUrl).into(binding.imageProgi)
        binding.tvName.text = auth.currentUser?.displayName
    }
}