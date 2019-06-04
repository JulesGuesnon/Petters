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
import android.widget.*
import java.io.IOException
import android.provider.MediaStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_createprofile.*
import models.CreateProfileInfo
import models.SignUpUser
import models.User

import java.io.ByteArrayOutputStream
import kotlin.collections.HashMap

class CreateProfileFragment: Fragment() {

    private val GALLERY = 1
    private val CAMERA = 2
    private var hasAddedImage = false
    private lateinit var imageBitmap: Bitmap
    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var dateOfBirth: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var typeSpinner: Spinner


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_createprofile, container, false)

        name = view.findViewById(R.id.create_profile_name_field)
        val nameEmpty = view.findViewById<TextView>(R.id.create_profile_noname)
        nameEmpty.visibility = View.INVISIBLE

        description = view.findViewById(R.id.create_profile_description_field)
        val descriptionEmpty = view.findViewById<TextView>(R.id.create_profile_nodescription)
        descriptionEmpty.visibility = View.INVISIBLE

        dateOfBirth = view.findViewById(R.id.create_profile_birth_date_placeholder)
        val dateOfBirthEmpty = view.findViewById<TextView>(R.id.create_profile_nobirth_date)
        dateOfBirthEmpty.visibility = View.INVISIBLE

        val profilePic = view.findViewById<ImageButton>(R.id.create_profile_add_picture)
        val profilePicEmpty = view.findViewById<TextView>(R.id.create_profile_nopic)
        profilePicEmpty.visibility = View.INVISIBLE

        val calendar = view.findViewById<EditText>(R.id.create_profile_birth_date_placeholder)
        calendar.setOnClickListener {
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


        typeSpinner = view.findViewById(R.id.create_profile_type_placeholder)
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
        genderSpinner = view.findViewById(R.id.create_profile_gender_placeholder)
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

        profilePic.setOnClickListener {
            showPictureDialog()
        }


        // Validate trigger and integrity control
        view.findViewById<Button>(R.id.create_profile_validate_button).setOnClickListener() {
            var hasError = false

            if (name.text.isEmpty()) {
                nameEmpty.visibility = View.VISIBLE
                hasError = true
            } else {
                nameEmpty.visibility = View.INVISIBLE
            }

            if (description.text.isEmpty()) {
                descriptionEmpty.visibility = View.VISIBLE
                hasError = true
            } else {
                descriptionEmpty.visibility = View.INVISIBLE

            }

            if (dateOfBirth.text.isEmpty()) {
                dateOfBirthEmpty.visibility = View.VISIBLE
                hasError = true
            } else {
                dateOfBirthEmpty.visibility = View.INVISIBLE
            }

            if (!hasAddedImage) {
                profilePicEmpty.visibility = View.VISIBLE
                hasError = true
            } else {
                profilePicEmpty.visibility = View.INVISIBLE
            }

            if (hasError) return@setOnClickListener

            if (FirebaseAuth.getInstance().currentUser?.uid == null) return@setOnClickListener

            saveImage(imageBitmap)

        }

        return view

    }

    companion object {

        fun newInstance(): CreateProfileFragment {
            return CreateProfileFragment()
        }
    }

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

    private fun choosePhotoFromGallary() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) return

        if (requestCode == GALLERY) {
            val contentURI = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                imageBitmap = bitmap
                Picasso
                    .get()
                    .load(contentURI)
                    .fit()
                    .transform(CircleTransform())
                    .into(create_profile_add_picture)

                hasAddedImage = true
            } catch (e: IOException) {
                e.printStackTrace()
            }


        } else if (requestCode == CAMERA) {
            try {
                val bitmap = data.extras.get("data") as Bitmap
                imageBitmap = bitmap
                create_profile_add_picture.setImageBitmap(bitmap)
                hasAddedImage = true
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveUser(information: CreateProfileInfo) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) return

        val userRef = FirebaseDatabase
            .getInstance()
            .getReference("/users/$uid")

        FirebaseDatabase
            .getInstance()
            .getReference("/users")
            .addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                return
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                return
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                return
            }

            override fun onChildAdded(data: DataSnapshot, p1: String?) {
                val user = data.getValue(SignUpUser::class.java)

                if (user == null || user.uid != FirebaseAuth.getInstance().currentUser?.uid) return

                val trueUser = User(
                    uid= user.uid,
                    name= user.name,
                    petType = information.petType,
                    petGender = information.petGender,
                    petBirth = information.petBirth,
                    petName = information.petName,
                    petDescription = information.petDescription,
                    profilePicture = information.profilePicture,
                    matched = HashMap(),
                    liked = HashMap()
                )

                userRef
                    .setValue(trueUser)
                    .addOnCompleteListener {
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)

                        activity?.finish()
                    }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                return
            }

        })
    }

    private fun saveImage(imageMap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        imageMap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val data = bytes.toByteArray()
        val uuid = UUID.randomUUID().toString()

        val ref = FirebaseStorage
            .getInstance()
            .getReference("/profilePicutres/$uuid")

        ref
            .putBytes(data)
            .addOnFailureListener {
                println(it.message)
            }
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    saveUser(CreateProfileInfo(
                        petName = name.text.toString(),
                        petDescription = description.text.toString(),
                        petBirth = dateOfBirth.text.toString(),
                        petGender = genderSpinner.selectedItem.toString(),
                        petType = typeSpinner.selectedItem.toString(),
                        profilePicture = it.toString()
                    ))
                }
            }
    }

}
