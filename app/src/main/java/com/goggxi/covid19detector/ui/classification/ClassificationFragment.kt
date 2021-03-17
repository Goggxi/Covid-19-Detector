package com.goggxi.covid19detector.ui.classification

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.RoundedCornersTransformation
import com.goggxi.covid19detector.R
import com.goggxi.covid19detector.databinding.FragmentClassificationBinding
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
    private var lastProcessingTimeMs: Long = 0


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
    }

    @SuppressLint("MissingSuperCall", "SetTextI18n")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val menuUpload = binding.toolbar.menu.findItem(R.id.upload_menu)
        if(requestCode == pickCameraRequestCode) {
            if (data != null) {
//                val uri = data.data
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
                binding.btnKlasifikasi.isEnabled = true
                menuUpload.isVisible = true
            }
        } else if (requestCode == pickGalleryRequestCode) {
            if (data != null) {
                val uri = data.data

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                mBitmap = scaleImage(mBitmap)
                binding.imgClassification.setImageBitmap(mBitmap)

                binding.btnKlasifikasi.isEnabled = true
                menuUpload.isVisible = true
            }
        } else {
            Toast.makeText(context, "Unrecognized request code", Toast.LENGTH_LONG).show()
        }
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//    }

    override fun onStart() {
        super.onStart()
        classificationAlgorithm()
        bottomNavigation()
        initAction()
    }

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
                    Toast.makeText(context , "Upload Image", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        binding.btnAddPhoto.setOnClickListener {
            pickPhoto()
        }

        binding.btnKlasifikasi.setOnClickListener {
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

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.upload_menu , menu)
//    }

}