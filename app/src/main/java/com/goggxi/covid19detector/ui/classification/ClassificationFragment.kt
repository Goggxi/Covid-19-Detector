package com.goggxi.covid19detector.ui.classification

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.goggxi.covid19detector.R
import com.goggxi.covid19detector.databinding.FragmentClassificationBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class ClassificationFragment : Fragment() {
    private lateinit var binding: FragmentClassificationBinding
    private lateinit var mBitmap: Bitmap
    private lateinit var classificationAlgorithm: ClassificationAlgorithm

    private val inputSize = 224
    private val pickCameraRequestCode = 1
    private val pickGalleryRequestCode = 2
    private val mModelPath = "Models-Covid19-CNN-v3.tflite"
    private val mLabelPath = "labels-v3.txt"
    private val root = FirebaseDatabase.getInstance().getReference("ImageClassification")
    private val reference = FirebaseStorage.getInstance().getReference("ImageClassification")

    private var mUploadTask: StorageTask<*>? = null
    private var lastProcessingTimeMs: Long = 0
    private var uriImage: Uri? = null



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_classification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentClassificationBinding.inflate(layoutInflater)
        activity?.setContentView(binding.root)

        binding.expandedImage.load(R.drawable.bg_klasifikasi){
            crossfade(true)
            crossfade(500)
            transformations(RoundedCornersTransformation(0F, 0F, 80F, 80F))
        }

        binding.toolbar.inflateMenu(R.menu.upload_menu)

        classificationAlgorithm()
        bottomNavigation()
        initAction()
    }

    @SuppressLint("MissingSuperCall", "SetTextI18n")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val menuUpload = binding.toolbar.menu.findItem(R.id.upload_menu)

//        data?.data?.let {
//            uriImage = it
//        }

        if(requestCode == pickCameraRequestCode) {
            if (data != null) {
//                val uri = data.data
//                uriImage = data.data
                mBitmap = data.extras?.get("data") as Bitmap
//                val bytes = ByteArrayOutputStream()
//                mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
//                try {
//                    mBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }

                mBitmap = scaleImage(mBitmap)
                binding.imgClassification.setImageBitmap(mBitmap)

                val file = File(context?.cacheDir,"CUSTOM NAME") //Get Access to a local file.
                file.delete() // Delete the File, just in Case, that there was still another File
                file.createNewFile()
                val fileOutputStream = file.outputStream()
                val byteArrayOutputStream = ByteArrayOutputStream()
                mBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                fileOutputStream.write(byteArray)
                fileOutputStream.flush()
                fileOutputStream.close()
                byteArrayOutputStream.close()

                uriImage = file.toUri()

//                binding.btnKlasifikasi.isEnabled = true
//                menuUpload.isVisible = true
            }
        } else if (requestCode == pickGalleryRequestCode) {
            if (data != null) {
                uriImage = data.data
//                val uri = data.data

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uriImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                mBitmap = scaleImage(mBitmap)
                binding.imgClassification.setImageBitmap(mBitmap)

//                binding.btnKlasifikasi.isEnabled = true
//                menuUpload.isVisible = true
            }
        } else {
            Toast.makeText(context, "Unrecognized request code", Toast.LENGTH_LONG).show()
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//    }

//    override fun onStart() {
//        super.onStart()
//        classificationAlgorithm()
//        bottomNavigation()
//        initAction()
//    }

    private fun classificationAlgorithm() {
        classificationAlgorithm = ClassificationAlgorithm(activity?.assets!!, mModelPath, mLabelPath, inputSize)
    }

    private fun pickPhoto() {
        val options = arrayOf("Ambil Gambar", "Pilih Foto Dari Galeri", "Batal")
        val builder = AlertDialog.Builder(context)

        builder.setIcon(R.drawable.ic_add_foto)
        builder.setTitle("Pilih Gambar Citra Chest Ray !")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Ambil Gambar" -> {
                    val pickCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(pickCamera, pickCameraRequestCode)
                }
                options[item] == "Pilih Foto Dari Galeri" -> {
                    val pickGallery = Intent(Intent.ACTION_PICK)
                    pickGallery.type = "image/*"
                    startActivityForResult(pickGallery, pickGalleryRequestCode)
                }
                else -> {
                    dialog.dismiss()
                }
            }
        }

        builder.show()
    }

    @SuppressLint("SetTextI18n")
    private fun initAction() {

        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.upload_menu -> {

                    if (mUploadTask != null && mUploadTask!!.isInProgress) {
                        Toast.makeText(context, "Sedang Mengupload...", Toast.LENGTH_SHORT).show()
                    } else {
                        if (binding.txtHasil.text.equals("-")) {
                            Toast.makeText(context , "Belum Ada Gambar Yang Diklasifikasi.", Toast.LENGTH_LONG).show()
                        } else {
                            uploadToFirebase(uriImage!!)
                        }
                    }
                    true
                }
                R.id.show_all_image -> {
                    startActivity(Intent(context, ShowImageActivity::class.java))
                    true
                }
                else -> false
            }
        }

        binding.btnAddPhoto.setOnClickListener {
            pickPhoto()
        }

        binding.btnKlasifikasi.setOnClickListener {

            if (uriImage != null){

                val startTime = SystemClock.uptimeMillis()  //menghitung waktu awal
                val results = classificationAlgorithm.recognizeImage(mBitmap).firstOrNull()

                when {
                    results?.title.equals("COVID-19") -> {
                        binding.txtHasil.setTextColor(ContextCompat.getColor( requireContext() , R.color.red1))
                        binding.txtHasil.text= results?.title
                    }
                    results?.title.equals("VIRAL PNEUMONIA") -> {
                        binding.txtHasil.setTextColor(ContextCompat.getColor( requireContext() , R.color.blue1))
                        binding.txtHasil.text= results?.title
                    }
                    else -> {
                        binding.txtHasil.setTextColor(ContextCompat.getColor( requireContext() , R.color.green1))
                        binding.txtHasil.text= results?.title
                    }
                }

                binding.txtResultPropabilitas.text= results?.percent.toString() + "%"
                lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime//menghitung lamanya proses
                val waktu = lastProcessingTimeMs.toString() //konversi ke string
                binding.txtResultWaktuDeteksi.text = "$waktu ms "

            } else {
                Toast.makeText(context, "Silihkan Pilih Foto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun scaleImage(bitmap: Bitmap?): Bitmap {
        val orignalWidth = bitmap!!.width
        val originalHeight = bitmap.height
        val scaleWidth = inputSize.toFloat() / orignalWidth
        val scaleHeight = inputSize.toFloat() / originalHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, orignalWidth, originalHeight, matrix, true)
    }

    private fun bottomNavigation() {
        // Nav host fragment
        val host: NavHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.container) as NavHostFragment?
            ?: return
        // nav controller
        val navController = host.navController

        // Setup bottom navigation view
        binding.bottomNavigationId.setupWithNavController(navController)
    }

    private fun uploadToFirebase(uri: Uri) {

        val fileRef = reference.child(System.currentTimeMillis().toString() + "." + getFileExtension(uri))

        mUploadTask = fileRef.putFile(uri)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { uri ->

                        val model = ImageUpload(
                                uri.toString(),
                                binding.txtHasil.text.toString().trim(),
                                binding.txtResultPropabilitas.text.toString().trim(),
                                binding.txtResultWaktuDeteksi.text.toString().trim()
                        )

                        val modelId = root.push().key

                        root.child(modelId!!).setValue(model)

                        CoroutineScope(Dispatchers.Main).launch {
                            delay(3000L)
                            binding.progressUpload.visibility = View.GONE
                            binding.textViewProgressUpload.visibility = View.GONE

                            binding.imgClassification.load(R.drawable.ic_photo_classification){
                                crossfade(true)
                                crossfade(500)
                            }

                            binding.txtHasil.setTextColor(ContextCompat.getColor( requireContext() , R.color.textColor1))
                            binding.txtHasil.text = "-"
                            binding.txtResultPropabilitas.text = "-"
                            binding.txtResultWaktuDeteksi.text = "-"

                            uriImage = null
                        }

                        Toast.makeText(context , "Upload Berhasil", Toast.LENGTH_SHORT).show()

//                        binding.imgClassification.setImageResource(R.drawable.ic_launcher_background)
                    }
                }
                .addOnProgressListener {
                    binding.textViewProgressUpload.visibility = View.VISIBLE
                    binding.progressUpload.visibility = View.VISIBLE
                    val progress = 100.0 * it.bytesTransferred / it.totalByteCount
                    binding.progressUpload.progress = progress.toInt()
                    binding.textViewProgressUpload.text = "Loading... " + progress.toInt() + " %"
                }
                .addOnFailureListener {
                    binding.textViewProgressUpload.visibility = View.GONE
                    binding.progressUpload.visibility = View.GONE
                    Toast.makeText(context, "Upload Gagal !!", Toast.LENGTH_SHORT).show()
                }
    }

    private fun getFileExtension(mUri: Uri): String? {
        val cr = activity?.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr?.getType(mUri))
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.upload_menu , menu)
//    }

}