package org.bedu.gr

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Base64InputStream
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import org.bedu.gr.databinding.ActivityCameraBinding
import org.bedu.gr.room.DataBaseVICO
import org.bedu.gr.room.Servicio
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private  var folio: Int = 0
    private var CAMERA_REQUEST_CODE = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        folio = bundle?.getString("FOLIO")!!.toInt()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Regresar"

        selectPhoto(folio)

        binding.btnOpenCameraVin.setOnClickListener {
            CAMERA_REQUEST_CODE = 1
            if(checkCameraPermission()){
                openCamera(CAMERA_REQUEST_CODE)
            } else{
                requestPermissions(CAMERA_REQUEST_CODE)
            }
        }

        binding.btnOpenCameraPlaca.setOnClickListener {
            CAMERA_REQUEST_CODE = 2
            if(checkCameraPermission()){
                openCamera(CAMERA_REQUEST_CODE)
            } else{
                requestPermissions(CAMERA_REQUEST_CODE)
            }
        }
    }

    private fun openCamera(code: Int){
        /*lifecycleScope.launch {
            startCamera()
        }*/
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(cameraIntent, code)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "La camára no puede ser habilitada.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val images: Bitmap = data?.extras?.get("data") as Bitmap
                    binding.imgPreviewVin.setImageBitmap(images)
                    val base64 = saveMediaToStorage(images)
                    val servicio = Servicio(folio_servicio = folio,photoType = 1, photo=base64 )
                    insertPhoto(servicio)
                }
            }
            2 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val images: Bitmap = data?.extras?.get("data") as Bitmap
                    binding.imgPreviewPlaca.setImageBitmap(images)
                    val base64 = saveMediaToStorage(images)
                    val servicio = Servicio(folio_servicio = folio,photoType = 2, photo=base64 )
                    insertPhoto(servicio)
                }
            }
            else -> {
                Toast.makeText(this, "Código no reconocido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // this method saves the image to gallery
    private fun saveMediaToStorage(bitmap: Bitmap): String {
        var base64: String=""

        // Generating a file name
        // Creamos un nombre único para cada foto
        val format = SimpleDateFormat("dd-MM-yyyyy-HH:mm:ss:SSS", Locale.US)
            .format(System.currentTimeMillis())
       val name = "vico$format"
        val filename = "$name.jpg"

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            this.contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            // These for devices running on android < Q
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            base64 = Base64.getEncoder().encodeToString(byteArray)


            //Toast.makeText(this , "Captured View and saved to Gallery" , Toast.LENGTH_SHORT).show()
        }
        return base64
    }

    private fun insertPhoto(servicio:Servicio) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute({
            DataBaseVICO.getInstance(this).servicioDao().insertServicio(servicio)

            Handler(Looper.getMainLooper()).post{

            }
        })
    }

    private fun selectPhoto(folio:Int) {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        executor.execute({
            val servicios = DataBaseVICO.getInstance(this).servicioDao().getServicioByFolio(folio)
            Handler(Looper.getMainLooper()).post{
            servicios.forEach {
                if (it.photoType == 1){
                    val decodedBytes: ByteArray = Base64.getDecoder().decode(it.photo)
                    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    binding.imgPreviewVin.setImageBitmap(bitmap)
                }
                if (it.photoType == 2){
                    val decodedBytes: ByteArray = Base64.getDecoder().decode(it.photo)
                    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                    binding.imgPreviewPlaca.setImageBitmap(bitmap)
                }
            }
            }
        })
    }

//      private suspend fun startCamera(){
//
//        val cameraProvider = ProcessCameraProvider.getInstance(this).await()
//
//        // Construimos el preview (aquí podemos hacer configuraciones)
//        val preview = Preview.Builder()
//            .build()
//            .apply {
//                setSurfaceProvider(binding.cameraPreview.surfaceProvider)
//            }
//
//        // Seleccionamos la cámara trasera
//        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//        try {
//            // Atamos nuestra cámara al ciclo de vida de nuestro Activity
//            cameraProvider.run {
//                unbindAll()
//                imageCapture = ImageCapture.Builder().build()
//                bindToLifecycle(this@CameraActivity, cameraSelector, preview,  imageCapture)
//            }
//
//        } catch(exception: Exception) {
//            Toast.makeText(this, "No se pudo hacer bind al lifecycle", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    fun takePhoto() {
//        // Creamos un nombre único para cada foto
//        val format = SimpleDateFormat("dd-MM-yyyyy-HH:mm:ss:SSS", Locale.US)
//            .format(System.currentTimeMillis())
//        val name = "vico $format"
//
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image") // La carpeta donde se guarda
//            }
//        }
//
//        // Creamos el builder para la configuración del archivo y los metadatos
//        val outputOptions = ImageCapture.OutputFileOptions
//            .Builder(
//                contentResolver,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//            .build()
//
//        // Seteamos el listener de cuando la captura sea efectuada
//        imageCapture?.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onError(e: ImageCaptureException) {
//                    Toast.makeText(
//                        baseContext,
//                        "Error al capturar imagen",
//                        Toast.LENGTH_SHORT).show()
//                    Log.e("CameraX",e.toString())
//                }
//
//                override fun onImageSaved(
//                    output: ImageCapture.OutputFileResults
//                ) {
//                    Toast.makeText(
//                        baseContext,
//                        "La imagen ${output.savedUri} se guardó correctamente!",
//                        Toast.LENGTH_SHORT).show()
//                    Log.d("CameraX", output.savedUri.toString())
//                }
//            }
//        )
//    }

    //Esto se ejecuta cuando de da la respuesta al dar permisos a la camara
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == CAMERA_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openCamera(CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(this,"Aun no tienes permiso", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkCameraPermission(): Boolean{
        return ActivityCompat
            .checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(code : Int) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            code
        )
    }
}