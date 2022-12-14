package com.example.demo0301_flowers

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class Classifier(assetManager: AssetManager, modelPath: String,labelPath:String) {
    private var interpreter: Interpreter
    private var labelList:List<String>
    private var imageSizeY:Int
    private var imageSizeX:Int
    private var pixelSize :Int

    data class Recognition(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0F
    ) {
        override fun toString(): String {
            return "Title = $title, Confidence = $confidence"
        }
    }

    init {
        val tfliteOptions = Interpreter.Options()
        tfliteOptions.setNumThreads(5)
        tfliteOptions.setUseNNAPI(true)
        interpreter = Interpreter(loadModelFile(assetManager, modelPath),tfliteOptions)
        labelList = loadLabelList(assetManager,labelPath)
        Log.i(this::class.simpleName,"\n=====模型加载成功=====")

        // 获取输入信息
        val imageTensorIndex = 0
        val imageShape:IntArray =
            interpreter.getInputTensor(imageTensorIndex).shape() //{1,224,224,3}
        val inputDataType = interpreter.getInputTensor(imageTensorIndex).dataType()
        imageSizeY = imageShape[1]
        imageSizeX = imageShape[2]
        pixelSize = imageShape[3]

        Log.i(this::class.simpleName,"\n=====图片的宽" + imageSizeX.toString())
        Log.i(this::class.simpleName,"\n=====图片的高" + imageSizeY.toString())
        Log.i(this::class.simpleName,"\n=====图片的色彩通道" + pixelSize.toString())
        Log.i(this::class.simpleName,"\n=====输入的数据类型" + inputDataType.toString())

        // 获取输出信息
        val probabilityTensorIndex = 0
        val probabilityShape =
            interpreter.getOutputTensor(probabilityTensorIndex).shape() //{1,classes}
        var outputDataType = interpreter.getOutputTensor(probabilityTensorIndex).dataType()
        Log.i(this::class.simpleName,"\n=====分类的数量" + probabilityShape[1])
        Log.i(this::class.simpleName,"\n=====输出的数据类型" + outputDataType.toString())
    }

    fun recognizeImage(bitmap: Bitmap):Recognition{
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, imageSizeX ,imageSizeY, false)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        Log.i(this::class.simpleName,"\n=====图片转换成功=====")

        val result = Array(1){FloatArray(labelList.size)}
        interpreter.run(byteBuffer,result)

        return getResult(result)

    }


    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap):ByteBuffer{
        val imgData = ByteBuffer.allocateDirect(imageSizeX * imageSizeY * pixelSize*4)
        imgData.order(ByteOrder.nativeOrder())
        imgData.rewind()

        val intValues = IntArray(imageSizeX *imageSizeY)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        for (i in 0 until imageSizeX) {
            for (j in 0 until imageSizeY) {
                val value = intValues[pixel++]

                imgData.putFloat((value.shr(16) and 0xFF)/255.0f)
                imgData.putFloat((value.shr(8) and 0xFF)/255.0f)
                imgData.putFloat((value and 0xFF)/255.0f)
            }
        }
        return imgData
    }

    private fun getResult(labelProbArray:Array<FloatArray>):Recognition{
        Log.i(this::class.simpleName, "List Size:(%d, %d, %d)".format(labelProbArray.size, labelProbArray[0].size, labelList.size))

        var max= 0F
        var recognition=Recognition()
        for (i in labelList.indices) {
            val confidence = labelProbArray[0][i]
            if (confidence > max) {
                recognition = Recognition("" + i,
                    if (labelList.size > i) labelList[i] else "Unknown",
                    confidence
                )
                max = confidence
            }
        }

        Log.i(this::class.simpleName, recognition.toString())

        return recognition
    }
}