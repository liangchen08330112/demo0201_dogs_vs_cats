package com.example.demo0301_flowers

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.Flower_Forest.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.concurrent.Executors

//这是设置请求权限的code码
private const val REQUEST_CODE_PERMISSIONS = 1
//这是要获取的拍照权限
private val REQUIRED_PERMISSIONS = arrayOf(
                                           Manifest.permission.CAMERA,
                                           Manifest.permission.READ_EXTERNAL_STORAGE)
// 读取相册的请求码
private const val IMAGE_REQUEST_CODE = 2

class MainActivity : AppCompatActivity() {
    private var imageCapture: ImageCapture? = null
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 判断是否有权限
        if (allPermissionsGranted()) {
            //Toast.makeText(this, "已获取权限！", Toast.LENGTH_SHORT).show()
            view_finder.post{startCamera()}
        } else {
            //请求权限
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        btn_take.setOnClickListener {
            //创建文件
            val file = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
            imageCapture?.takePicture(file, executor,
                object : ImageCapture.OnImageSavedListener {
                    override fun onError(
                        imageCaptureError: ImageCapture.ImageCaptureError,
                        message: String,
                        exc: Throwable?
                    ) {
                        //保存失败
                        val msg = "保存失败: $message"
                        view_finder.post {
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onImageSaved(file: File) {
                        //保存成功
                        val msg = "保存成功: ${file.absolutePath}"
                        view_finder.post {
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                            //这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！
                            val intent = Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED)
                            val uri = Uri.fromFile(file)
                            intent.data = uri
                            sendBroadcast(intent)
                        }

                        // 跳转到显示窗口
                        val intent = Intent(this@MainActivity, PreviewActivity::class.java)
                        var imgUri = Uri.fromFile(File(file.absolutePath))
                        intent.putExtra("path",imgUri.toString())
                        startActivity(intent)
                    }
                })
        }

        //从相册选择
        btn_photo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }
    }

    /**
     * 处理权限请求的结果 对话框中有被批准了？否则请提示
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                //Toast.makeText(this, "权限获取成功！！", Toast.LENGTH_SHORT).show()
                view_finder.post{startCamera()}
            } else {
                Toast.makeText(this, "没有权限！！", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    /**
     * 带返回值返回
     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            IMAGE_REQUEST_CODE -> {
                val imgUri: Uri? = data?.data //获取系统返回的照片的Uri
                val intent = Intent(this@MainActivity, PreviewActivity::class.java)
                intent.putExtra("path", imgUri.toString())
                startActivity(intent)
            }
        }
    }

    /**
     * 检查权限是否被授权
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 初始化拍照
     */
    private fun startCamera() {
        //为取景器用例创建配置对象
        val previewConfig = PreviewConfig.Builder().apply {
            //设置取景器前后摄像头
            setLensFacing(CameraX.LensFacing.BACK)
        }.build()

        // 创建预览
        val preview = Preview(previewConfig)

        // 输出到视图控件上
        preview.setOnPreviewOutputUpdateListener {
            val parent = view_finder.parent as ViewGroup
            parent.removeView(view_finder)
            parent.addView(view_finder, 0)

            //view_finder.surfaceTexture = it.surfaceTexture//前置摄像头
        }

        // 为图像捕获用例创建配置对象
        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .apply {
                //设置图像捕获模式
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                //设置前后摄像头
                setLensFacing(CameraX.LensFacing.BACK)
            }.build()

        // 构建图像捕获用例
        imageCapture = ImageCapture(imageCaptureConfig)

        // 将用例绑定到生命周期
        CameraX.bindToLifecycle(this, preview,imageCapture)
    }


}
