package com.Grader.grader_s

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grader_s.R
import com.example.grader_s.adapter_syllabus
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import java.io.IOException
import java.io.OutputStream

class Syllabus : AppCompatActivity(), adapter_syllabus.OnDownloadClickListener {

    private lateinit var r1: RecyclerView
    private lateinit var alertDialog: AlertDialog

    private lateinit var activity: String
    private lateinit var Page_name: TextView
    private var School_name: String = ""
    private var Class: String = ""
    private val REQUEST_WRITE_STORAGE = 112
    private val okHttpClient = OkHttpClient()
    private var currentUrl: String? = null

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences
    private lateinit var toolbar: Toolbar
    private lateinit var a1: adapter_syllabus
    private var subjectlist = ArrayList<dataclass_syllabus>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_syllabus)

        databaseReference = Firebase.database.getReference("")
        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)

        initialize()
        back_button()
        fetch_data()
    }

    private fun fetch_data() {
        databaseReference.child("Schools").child(School_name).child(Class).child("Subjects").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val Sub = childSnapshot.key.toString()
                    for (childsnap2 in childSnapshot.children) {
                        val teacher_name = childsnap2.key.toString()
                        val url = childsnap2.value.toString()
                        subjectlist.add(dataclass_syllabus(R.drawable.pdf, Sub, teacher_name, url))
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error fetching data: ${databaseError.message}")
            }
        })

        r1.layoutManager = LinearLayoutManager(this)
        a1 = adapter_syllabus(subjectlist, this, this)
        r1.adapter = a1
    }

    private fun back_button() {
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
            overridePendingTransition(R.anim.fade_in, 0)
        }
    }

    private fun initialize() {
        toolbar = findViewById(R.id.toolbar)
        r1 = findViewById(R.id.r1_syallabus)
        activity = intent.getStringExtra("Activity").toString()
        Page_name = findViewById(R.id.page_name)
        Page_name.text = activity

        School_name = database_internal.getString("School_name", "1").toString()
        Class = "Class " + database_internal.getString("Class1", "1").toString()

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.progressbar, null)

        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        alertDialog = dialogBuilder.create()
    }

    override fun onDownloadClick(url: String, Subject: String) {
        alertDialog.show()
        currentUrl = url
        showFileNameDialog(Subject)
    }

    private fun showFileNameDialog(Subject: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter File Name")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val fileName = input.text.toString()
            if (fileName.isNotEmpty()) {
                downloadFile(currentUrl!!, fileName)
            } else {
                alertDialog.dismiss()
                Toast.makeText(this, "File name cannot be empty", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel()
        alertDialog.dismiss()}

        builder.show()
    }

    private fun downloadFile(url: String, fileName: String) {
        val request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    alertDialog.dismiss()
                    Log.e(TAG, "Failed to download file", e)
                    Toast.makeText(this@Syllabus, "File download failed", Toast.LENGTH_SHORT).show()
                }
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    runOnUiThread {
                        alertDialog.dismiss()
                        Toast.makeText(this@Syllabus, "File download failed", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, "$fileName.pdf")
                    put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val resolver = contentResolver
                val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                if (uri == null) {
                    runOnUiThread {
                        alertDialog.dismiss()
                        Toast.makeText(this@Syllabus, "Failed to save the file", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                try {
                    val outputStream: OutputStream? = resolver.openOutputStream(uri)
                    if (outputStream != null) {
                        val sink = outputStream.sink().buffer()
                        sink.writeAll(response.body!!.source())
                        sink.close()
                    }
                    runOnUiThread {
                        alertDialog.dismiss()
                        Toast.makeText(this@Syllabus, "File downloaded successfully", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    runOnUiThread {
                        alertDialog.dismiss()
                        Log.e(TAG, "Failed to save the file", e)
                        Toast.makeText(this@Syllabus, "Failed to save the file", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage permission granted", Toast.LENGTH_SHORT).show()
                currentUrl?.let { url ->
                    showFileNameDialog(url)
                }
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
