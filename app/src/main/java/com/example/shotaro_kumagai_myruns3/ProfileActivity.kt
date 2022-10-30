package com.example.shotaro_kumagai_myruns3

import android.app.Dialog
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.io.ByteArrayOutputStream
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var nameView: EditText
    private lateinit var mailView:EditText
    private lateinit var phoneView:EditText
    private  lateinit var femaleView: RadioButton
    private lateinit var maleView:RadioButton
    private lateinit var classView:EditText
    private lateinit var majorView:EditText
    private lateinit var tempImgFileName: String
    private lateinit var tempImgUri: Uri
    private lateinit var cameraResult: ActivityResultLauncher<Intent>
    private lateinit var selectResult: ActivityResultLauncher<Intent>
    private lateinit var viewModel:MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        imageView = findViewById(R.id.photo_image)
        nameView = findViewById(R.id.name_input)
        mailView = findViewById(R.id.mail_input)
        phoneView = findViewById(R.id.phone_input)
        femaleView = findViewById(R.id.female_radio)
        maleView = findViewById(R.id.male_radio)
        classView = findViewById(R.id.class_input)
        majorView = findViewById(R.id.major_input)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        val imgStr: String? = savedInstanceState?.getString(IMG_URI,null)

        if(imgStr !== null){
            loadProfile(false)
            val imgUri: Uri = Uri.parse(imgStr)
            val bitmap = Util.getBitmap(this, imgUri)
            viewModel.userImage.value = bitmap
        }else{
            loadProfile(true)
        }

        Util.checkPermissions(this)

        tempImgFileName= "xd_img.jpg"
        val tempImgFile = File(getExternalFilesDir(null), tempImgFileName)
        tempImgUri = FileProvider.getUriForFile(this, "com.example.shotaro_kumagai_myruns3", tempImgFile)
        cameraResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = Util.getBitmap(this, tempImgUri)
                viewModel.userImage.value = bitmap
            }
            dismissDialog(0)
        }

        selectResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedUri :Uri? = result.data?.data
                if(selectedUri != null){
                    val bitmap = Util.getBitmap(this, selectedUri)
                    viewModel.userImage.value = bitmap
                }
            }
            dismissDialog(0)
        }


        viewModel.userImage.observe(this, Observer { it ->
            val matrix  = Matrix()
            matrix.postRotate(-90F)
            val scaledBitmap = Bitmap.createScaledBitmap(it, 100, 100, true)
            val rotatedBitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )
            imageView.setImageBitmap(rotatedBitmap)
        })
    }


    private  fun loadProfile(isImgLoad: Boolean){
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        nameView.setText(sharedPref.getString(NAME, null))
        mailView.setText(sharedPref.getString(MAIL, null))
        phoneView.setText(sharedPref.getString(PHONE, null))
        if (sharedPref.getInt(CLASS,2147483647) !== 2147483647){
            classView.setText(sharedPref.getInt(CLASS,2147483647).toString())
        }
        majorView.setText(sharedPref.getString(MAJOR, null))
        val genderVal: Int = sharedPref.getInt(GENDER,0)
        if(genderVal == 1){
            maleView.isChecked = true
        }else if(genderVal == 2){
            femaleView.isChecked = true;
        }

        val imgData: String? = sharedPref.getString(IMG_DATA, null)

        if(isImgLoad && imgData !== null){
            val b: ByteArray = Base64.decode(imgData, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
            viewModel.userImage.value = bitmap
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(IMG_URI,tempImgUri.toString())
    }

    override fun onCreateDialog(id: Int): Dialog {
        val builder = AlertDialog.Builder(this)
        val view : View = this.layoutInflater.inflate(R.layout.pick_profile_dialog, null)
        builder.setView(view)
        builder.setTitle("Pick Profile Picture")
        view.findViewById<LinearLayout>(R.id.open_camera).setOnClickListener{
            onTakePhoto()
        }
        view.findViewById<LinearLayout>(R.id.select_photo).setOnClickListener{
            onSelectPhoto()
        }
        return builder.create()
    }


    private fun saveProfile(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit().putString(NAME, nameView.text.toString()).apply()
        sharedPref.edit().putString(MAIL, mailView.text.toString()).apply()
        sharedPref.edit().putString(PHONE, phoneView.text.toString()).apply()
        val classVal:String = classView.text.toString()
        sharedPref.edit().putInt(CLASS, classVal.toInt()).apply()
        sharedPref.edit().putString(MAJOR, majorView.text.toString()).apply()

        if(!maleView.isChecked && !femaleView.isChecked){
            sharedPref.edit().putInt(GENDER,0).apply()
        }else if(maleView.isChecked){
            sharedPref.edit().putInt(GENDER,1).apply()
        }else{
            sharedPref.edit().putInt(GENDER,2).apply()
        }
        sharedPref.edit().putString(MAJOR, majorView.text.toString()).apply()

        val bas: ByteArrayOutputStream = ByteArrayOutputStream()
        viewModel.userImage.value?.compress(Bitmap.CompressFormat.JPEG, 100, bas)
        val b: ByteArray = bas.toByteArray()
        val encodedImg: String = Base64.encodeToString(b, Base64.DEFAULT)
        sharedPref.edit().putString(IMG_DATA, encodedImg).apply()
    }


    fun onSave(view: View){
        saveProfile()
        Toast.makeText(
            this,
            "saved",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun onPickPhoto(view:View){
        showDialog(0)
    }

    private fun onTakePhoto(){
        val intent:Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri)
        cameraResult.launch(intent)
    }

    private fun onSelectPhoto(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }
        selectResult.launch(intent)
    }

    fun onCancel(view: View){
        finish()
    }

    companion object{
        const val NAME = "name"
        const val MAIL = "mail"
        const val PHONE = "phone"
        const val CLASS = "class"
        const val GENDER = "gender"
        const val MAJOR = "major"
        const val IMG_DATA = "img_data"
        const val IMG_URI = "img_uri"
    }
}