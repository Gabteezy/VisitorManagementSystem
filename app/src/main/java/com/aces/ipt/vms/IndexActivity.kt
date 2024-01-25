package com.aces.ipt.vms

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aces.ipt.vms.databinding.ActivityIndexBinding
import com.aces.ipt.vms.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class IndexActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndexBinding

    private var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var firebaseDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://visitor-management-syste-f5ddc-default-rtdb.firebaseio.com/")
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    private var user: User? = null
    private var userType = "UNKNOWN"
    private var displayName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnstarted.setOnClickListener {
            val intent = Intent(this@IndexActivity, loginUser::class.java)
            startActivity(intent)
        }
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        if(!isNetworkAvailable(this)){
            Log.d("NETWORK", "NO NETWORK AVAILABLE ")
            var verifyDialog: Dialog = Dialog(this)
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setTitle("INTERNET CONNECTIVITY REQUIRED")
            builder.setMessage("PLEASE TURN ON YOUR WIFI OR DATA TO USE THIS APP.")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS));
                verifyDialog.dismiss()
            }
            verifyDialog = builder.create()
            if(verifyDialog.window != null){
                verifyDialog.window!!.setBackgroundDrawableResource(R.color.gray)
            }
            verifyDialog.show()
//            Toast.makeText(this, "NO NETWORK AVAILABLE", Toast.LENGTH_LONG).show()
            return
        }
        if (FirebaseAuth.getInstance().currentUser!=null) {
            binding.loading.visibility = View.VISIBLE
            Log.d("CURRENT_USER", "NOT NULL")
            Log.d("CURRENT_USER", firebaseAuth.currentUser!!.uid)

            Toast.makeText(this, "Logged In as User Staff", Toast.LENGTH_LONG);
            binding.loading.visibility = View.GONE
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()

            val databaseRef = firebaseDatabase.reference.child("User")
                .child(firebaseAuth.currentUser!!.uid)

            databaseRef.get().addOnCompleteListener { dataSnapshot ->
                if (dataSnapshot.isSuccessful) {

                    Log.d("CURRENT_USER", "IS SUCCESSFUL")
                    user = dataSnapshot.result.getValue(User::class.java)
                    if (user!=null) {

                        userType = user!!.type!!


                        val isVerified = user!!.verified




                        if(userType == "User"){
                            Toast.makeText(this, "Logged In as User Staff", Toast.LENGTH_LONG);
                            binding.loading.visibility = View.GONE
                            val intent = Intent(this, Dashboard::class.java)
                            startActivity(intent)
                            finish()
                        }else if(userType == "Staff"){
                            Toast.makeText(this, "Logged In as User Staff", Toast.LENGTH_LONG);
                            binding.loading.visibility = View.GONE
                        }else{
                            Toast.makeText(this, "Logged In as User Admin", Toast.LENGTH_LONG);
                            binding.loading.visibility = View.GONE
                        }

                    }else{
                        Log.d("CURRENT_USER", "USER CLASS NULL")
                    }
                } else {
                    Log.d("CURRENT_USER", "WITH_AUTH_BUT_NOT_REGISTERED")
                    binding.loading.visibility = View.GONE
                    firebaseAuth.signOut()
                }
            }
        } else {
            binding.loading.visibility = View.GONE

        }
    }

}
