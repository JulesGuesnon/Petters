package com.example.petters

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_createprofile.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ProfileFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val cameraButton = view.findViewById<ImageButton>(R.id.edit_profile_add_picture)
        cameraButton.setOnClickListener(){
            showPictureDialog()
        }

        val calendar = view.findViewById<EditText>(R.id.edit_profile_birth_date_placeholder)
        calendar.setOnClickListener{
            val c = Calendar.getInstance()
            var day = c.get(Calendar.DAY_OF_MONTH)
            var month = c.get(Calendar.MONTH)
            var year = c.get(Calendar.YEAR)
            val dpd = DatePickerDialog(
                activity,
                DatePickerDialog.OnDateSetListener { datePicker, selyear, monthOfYear, dayOfMonth ->
                    day = dayOfMonth
                    month = monthOfYear + 1
                    year = selyear
                    calendar.text = SpannableStringBuilder("$day/$month/$year")
                }, year, month, day
            )
            dpd.show()
        }

        val typeSpinner: Spinner = view.findViewById(R.id.edit_profile_type_placeholder)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            activity,
            R.array.type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            typeSpinner.adapter = adapter
        }
        val genderSpinner: Spinner = view.findViewById(R.id.edit_profile_gender_placeholder)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            activity,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            genderSpinner.adapter = adapter
        }



        return view
    }
    private val GALLERY = 1
    private val CAMERA = 2

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(activity)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems,
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> choosePhotoFromGallary()
                    1 -> takePhotoFromCamera()
                }
            })
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }


    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.getContentResolver(), contentURI)
                    val path = saveImage(bitmap)
                    create_profile_add_picture.setImageBitmap(bitmap)
                    //create_profile_add_picture.setTag(1, "Pic imported")
                    //println(create_profile_add_picture.getTag(1))

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(activity, "Image Import failed :/", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            create_profile_add_picture.setImageBitmap(thumbnail)
            saveImage(thumbnail)
            Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }
    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            Environment.getExternalStorageDirectory().path + "petters/profile/img"
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(
                wallpaperDirectory, Calendar.getInstance()
                    .timeInMillis.toString() + ".jpg"
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                activity,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }
}