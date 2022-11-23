package com.example.shotaro_kumagai_myruns5.sensor

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.shotaro_kumagai_myruns5.R
import com.example.shotaro_kumagai_myruns5.start.map.AutomaticActivity
import weka.core.Attribute
import weka.core.DenseInstance
import weka.core.Instance
import weka.core.Instances
import weka.core.converters.ArffSaver
import weka.core.converters.ConverterUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import kotlin.math.sqrt


class SensorService: Service(), SensorEventListener {
    private val mFeatLen = Globals.ACCELEROMETER_BLOCK_CAPACITY + 2
    private lateinit var mFeatureFile: File
    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private var mServiceTaskType = 0
    private lateinit var mDataset: Instances
    private lateinit var mClassAttribute: Attribute
    private lateinit var mAsyncTask: OnSensorChangedTask
    private lateinit var mAccBuffer: ArrayBlockingQueue<Double>

    override fun onCreate() {
        super.onCreate()
        mAccBuffer = ArrayBlockingQueue<Double>(Globals.ACCELEROMETER_BUFFER_CAPACITY)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST)
        mFeatureFile = File(getExternalFilesDir(null), Globals.FEATURE_FILE_NAME)
        Log.d(Globals.TAG, mFeatureFile.absolutePath)
        mServiceTaskType = Globals.SERVICE_TASK_TYPE_COLLECT

        // Create the container for attributes
        val allAttr = ArrayList<Attribute>()

        // Adding FFT coefficient attributes
        val df = DecimalFormat("0000")
        for (i in 0 until Globals.ACCELEROMETER_BLOCK_CAPACITY) {
            allAttr.add(Attribute(Globals.FEAT_FFT_COEF_LABEL + df.format(i.toLong())))
        }
        // Adding the max feature
        allAttr.add(Attribute(Globals.FEAT_MAX_LABEL))

        // Declare a nominal attribute along with its candidate values
        val labelItems = ArrayList<String>(3)
        labelItems.add(Globals.CLASS_LABEL_STANDING)
        labelItems.add(Globals.CLASS_LABEL_WALKING)
        labelItems.add(Globals.CLASS_LABEL_RUNNING)
        labelItems.add(Globals.CLASS_LABEL_OTHER)
        mClassAttribute = Attribute(Globals.CLASS_LABEL_KEY, labelItems)
        allAttr.add(mClassAttribute)

        // Construct the dataset with the attributes specified as allAttr and
        // capacity 10000
        mDataset = Instances(Globals.FEAT_SET_NAME, allAttr, Globals.FEATURE_SET_CAPACITY)

        // Set the last column/attribute (standing/walking/running) as the class
        // index for classification
        mDataset.setClassIndex(mDataset.numAttributes() - 1)
        val i = Intent(this, AutomaticActivity::class.java)
        // Read:
        // http://developer.android.com/guide/topics/manifest/activity-element.html#lmode
        // IMPORTANT!. no re-create activity
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        mAsyncTask = OnSensorChangedTask()
        mAsyncTask.execute()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mAsyncTask.cancel(true)
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        mSensorManager.unregisterListener(this)
        Log.i("", "")
        super.onDestroy()
    }

    inner class OnSensorChangedTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg arg0: Void?): Void? {
            val inst: Instance = DenseInstance(mFeatLen)
            inst.setDataset(mDataset)
            var blockSize = 0
            val fft = FFT(Globals.ACCELEROMETER_BLOCK_CAPACITY)
            val accBlock = DoubleArray(Globals.ACCELEROMETER_BLOCK_CAPACITY)
            val im = DoubleArray(Globals.ACCELEROMETER_BLOCK_CAPACITY)
            var max = Double.MIN_VALUE
            while (true) {
                try {
                    accBlock[blockSize++] = mAccBuffer.take().toDouble()
                    if (blockSize == Globals.ACCELEROMETER_BLOCK_CAPACITY) {
                        blockSize = 0
                        max = .0
                        for (v in accBlock) {
                            if (max < v) {
                                max = v
                            }
                        }
                        fft.fft(accBlock, im)
                        for (i in accBlock.indices) {
                            val mag = sqrt(accBlock[i] * accBlock[i] + im[i]
                                    * im[i])
                            inst.setValue(i, mag)
                            im[i] = .0
                        }
                        inst.setValue(Globals.ACCELEROMETER_BLOCK_CAPACITY, max)
                        val key = WekaClassifier.classify(inst.toDoubleArray().toTypedArray())
                        inst.setValue(mClassAttribute, key)
                        mDataset.add(inst)
                        println(mDataset)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun onCancelled() {
            Log.e("123", mDataset.size.toString() + "")
            if (mServiceTaskType == Globals.SERVICE_TASK_TYPE_CLASSIFY) {
                super.onCancelled()
                return
            }
            Log.i("in the loop", "still in the loop cancelled")
            var toastDisp: String
            if (mFeatureFile.exists()) {

                // merge existing and delete the old dataset
                val source: ConverterUtils.DataSource
                try {
                    // Create a datasource from mFeatureFile where
                    // mFeatureFile = new File(getExternalFilesDir(null),
                    // "features.arff");
                    source = ConverterUtils.DataSource(FileInputStream(mFeatureFile))
                    // Read the dataset set out of this datasource
                    val oldDataset = source.dataSet
                    oldDataset.setClassIndex(mDataset.numAttributes() - 1)
                    // Sanity checking if the dataset format matches.
                    if (!oldDataset.equalHeaders(mDataset)) {
                        // Log.d(Globals.TAG,
                        // oldDataset.equalHeadersMsg(mDataset));
                        throw java.lang.Exception(
                            "The two datasets have different headers:\n")
                    }

                    // Move all items over manually
                    for (i in mDataset.indices) {
                        oldDataset.add(mDataset[i])
                    }
                    mDataset = oldDataset
                    // Delete the existing old file.
                    mFeatureFile.delete()
                    Log.i("delete", "delete the file")
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                toastDisp = getString(R.string.ui_sensor_service_toast_success_file_updated)
            } else {
                toastDisp = getString(R.string.ui_sensor_service_toast_success_file_created)
            }
            Log.i("save", "create saver here")
            // create new Arff file
            val saver = ArffSaver()
            // Set the data source of the file content
            saver.instances = mDataset
            Log.e("1234", mDataset.size.toString() + "")
            try {
                // Set the destination of the file.
                // mFeatureFile = new File(getExternalFilesDir(null),
                // "features.arff");
                saver.setFile(mFeatureFile)
                // Write into the file
                saver.writeBatch()
                Log.i("batch", "write batch here")
                Toast.makeText(applicationContext, toastDisp,
                    Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                toastDisp = getString(R.string.ui_sensor_service_toast_error_file_saving_failed)
                e.printStackTrace()
            }
            Log.i("toast", "toast here")
            super.onCancelled()
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val m = Math.sqrt((event.values[0] * event.values[0] + event.values[1] * event.values[1] + (event.values[2]
                    * event.values[2])).toDouble())

            // Inserts the specified element into this queue if it is possible
            // to do so immediately without violating capacity restrictions,
            // returning true upon success and throwing an IllegalStateException
            // if no space is currently available. When using a
            // capacity-restricted queue, it is generally preferable to use
            // offer.
            try {
                mAccBuffer.add(m)
            } catch (e: IllegalStateException) {

                // Exception happens when reach the capacity.
                // Doubling the buffer. ListBlockingQueue has no such issue,
                // But generally has worse performance
                val newBuf = ArrayBlockingQueue<Double>(mAccBuffer.size * 2)
                mAccBuffer.drainTo(newBuf)
                mAccBuffer = newBuf
                mAccBuffer.add(m)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}