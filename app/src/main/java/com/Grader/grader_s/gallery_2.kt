package com.Grader.grader_s

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.grader_s.R
import com.example.grader_s.adapter_syllabus
import com.google.firebase.database.DatabaseReference
import com.ortiz.touchview.TouchImageView
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import java.io.IOException
import java.io.OutputStream


class gallery_2 : AppCompatActivity() {

    private lateinit var activity: String
    private lateinit var url_text: TextView
    private var School_name: String = ""
    private var url: String = ""
    private val okHttpClient = OkHttpClient()
    private var currentPagePosition = 0
    private lateinit var onBackPressedCallback : OnBackPressedCallback


    private var position: Int =0
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database_internal: SharedPreferences
    private lateinit var toolbar: Toolbar
    private lateinit var v1: ViewPager2
    private lateinit var viewPagerViewModel: ViewPagerViewModel
    private lateinit var a1: adapter_gallery_2
    private var image_list: MutableList<dataclass_gallery> = mutableListOf()
    private lateinit var data_list:  ArrayList<String>

    private lateinit var Syllabus_image: TouchImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery2)

        database_internal = getSharedPreferences("Database_local", MODE_PRIVATE)
        Intialize()
        back_button()
        image_show()


    }

    private fun image_show() {

        a1 = adapter_gallery_2(image_list,v1)
        v1.adapter= a1

        v1.setCurrentItem(position,false)
        url = data_list[position]
        url_text.setText(url)



//        viewPagerViewModel = ViewModelProvider(this).get(ViewPagerViewModel::class.java)

//        viewPagerViewModel.currentPosition.observe(this, Observer { position ->
//            // Update UI with the current position
//            Toast.makeText(this, "Current position: $position", Toast.LENGTH_SHORT).show()
//        })

//        v1.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                // Update the ViewModel with the current position
//                viewPagerViewModel.setCurrentPosition(position)
//
//                Log.i("Position",position.toString())
//            }
//        })

        v1.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPagePosition = position
                url= data_list[currentPagePosition]
                url_text.setText(url)

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.download) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO),
                    adapter_syllabus.REQUEST_WRITE_STORAGE
                )
            } else {

                Log.i("Linkk",url_text.text.toString())
                showFileNameDialog(url)
            }
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun showFileNameDialog(currentUrl: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter File Name")

        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val fileName = input.text.toString()
            if (fileName.isNotEmpty()) {
                downloadFile(currentUrl!!, fileName)
            } else {
                //alertDialog.dismiss()
                Toast.makeText(this, "File name cannot be empty", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel()
            }

        builder.show()
    }

    private fun downloadFile(url: String, fileName: String) {
        val request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    Log.e(ContentValues.TAG, "Failed to download file", e)
                    Toast.makeText(this@gallery_2, "File download failed", Toast.LENGTH_SHORT).show()
                }
            }

            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@gallery_2, "File download failed", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                // Determine the MIME type and extension from the response headers or URL
                val contentType = response.header("Content-Type") ?: "application/octet-stream"
                val fileExtension = when (contentType) {
                    "image/jpeg" -> "jpg"
                    "image/png" -> "png"
                    "image/gif" -> "gif"
                    "video/mp4" -> "mp4"
                    "video/x-matroska" -> "mkv"
                    "video/webm" -> "webm"
                    "video/3gpp" -> "3gp"
                    else -> "bin" // Default to binary file
                }

                val contentValues = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, "$fileName.$fileExtension")
                    put(MediaStore.Downloads.MIME_TYPE, contentType)
                    put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val resolver = contentResolver
                val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                if (uri == null) {
                    runOnUiThread {
                        Toast.makeText(this@gallery_2, "Failed to save the file", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@gallery_2, "File downloaded successfully", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    runOnUiThread {
                        Log.e(ContentValues.TAG, "Failed to save the file", e)
                        Toast.makeText(this@gallery_2, "Failed to save the file", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }


    private fun Intialize() {

        activity = intent.getStringExtra("Activity").toString()
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        v1 = findViewById(R.id.viewPager)
        url_text = findViewById(R.id.url_id)

        position = intent.getIntExtra("Position",1)
        data_list  = intent.getSerializableExtra("image_list") as ArrayList<String>
        for(i in data_list){
            image_list.add(dataclass_gallery(i))
        }

        School_name = database_internal.getString("School_name", "1").toString()


    }

    private fun back_button() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@gallery_2, Gallery::class.java)
                intent.putExtra("Activity",activity)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)// or perform any other action
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        toolbar.setNavigationOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }
    }
}