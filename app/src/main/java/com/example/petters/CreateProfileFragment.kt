package com.example.petters

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.util.*
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.*
import java.io.IOException
import android.widget.Toast
import android.provider.MediaStore
import android.media.MediaScannerConnection
import android.os.Environment
import com.google.android.gms.common.util.IOUtils.toByteArray
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import kotlinx.android.synthetic.main.fragment_createprofile.*
import kotlinx.android.synthetic.main.fragment_createprofile.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class CreateProfileFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_createprofile, container, false)

        val name = view.findViewById<EditText>(R.id.create_profile_name_field)
        val nameEmpty = view.findViewById<TextView>(R.id.create_profile_noname)
        nameEmpty.visibility = View.INVISIBLE

        val description = view.findViewById<EditText>(R.id.create_profile_description_field)
        val descriptionEmpty = view.findViewById<TextView>(R.id.create_profile_nodescription)
        descriptionEmpty.visibility = View.INVISIBLE

        val dateOfBirth = view.findViewById<EditText>(R.id.create_profile_birth_date_placeholder)
        val dateOfBirthEmpty = view.findViewById<TextView>(R.id.create_profile_nobirth_date)
        dateOfBirthEmpty.visibility = View.INVISIBLE

        val profilePic = view.findViewById<ImageButton>(R.id.create_profile_add_picture)
        val profilePicEmpty = view.findViewById<TextView>(R.id.create_profile_nopic)
        profilePicEmpty.visibility = View.INVISIBLE

        val calendar = view.findViewById<EditText>(R.id.create_profile_birth_date_placeholder)
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


        val typeSpinner: Spinner = view.findViewById(R.id.create_profile_type_placeholder)
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
        val genderSpinner: Spinner = view.findViewById(R.id.create_profile_gender_placeholder)
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

        val cameraButton = view.findViewById<ImageButton>(R.id.create_profile_add_picture)
        cameraButton.setOnClickListener(){
            showPictureDialog()
        }


        // Validate trigger and integrity control
        view.findViewById<Button>(R.id.create_profile_validate_button).setOnClickListener() {
            if (name.text.isEmpty()) {
                nameEmpty.visibility = View.VISIBLE
            } else {
                nameEmpty.visibility = View.INVISIBLE
            }
            if (description.text.isEmpty()) {
                descriptionEmpty.visibility = View.VISIBLE
            } else {
                descriptionEmpty.visibility = View.INVISIBLE

            }
            if (dateOfBirth.text.isEmpty()) {
                dateOfBirthEmpty.visibility = View.VISIBLE
            } else {
                dateOfBirthEmpty.visibility = View.INVISIBLE
            }
            //if (profilePic.getTag(1) != "null") {
                //profilePicEmpty.visibility = View.VISIBLE
                //println(profilePic.getTag(1))
            //} else {
                //profilePicEmpty.visibility = View.INVISIBLE
            //}
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
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

       startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
       startActivityForResult(intent, CAMERA)
    }




    companion object {

        fun newInstance(): CreateProfileFragment {
            return CreateProfileFragment()
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
