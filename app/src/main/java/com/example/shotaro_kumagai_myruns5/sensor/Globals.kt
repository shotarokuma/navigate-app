package com.example.shotaro_kumagai_myruns5.sensor

object Globals {
    // Debugging tag
    const val ACCELEROMETER_BUFFER_CAPACITY = 2048
    const val ACCELEROMETER_BLOCK_CAPACITY = 64

    const val SERVICE_TASK_TYPE_COLLECT = 0
    const val SERVICE_TASK_TYPE_CLASSIFY = 1


    const val CLASS_LABEL_KEY = "label"
    const val CLASS_LABEL_STANDING = "Standing"
    const val CLASS_LABEL_WALKING = "Walking"
    const val CLASS_LABEL_RUNNING = "Running"
    const val CLASS_LABEL_OTHER = "Others"

    const val FEAT_FFT_COEF_LABEL = "fft_coef_"
    const val FEAT_MAX_LABEL = "max"
    const val FEAT_SET_NAME = "accelerometer_features"

    const val FEATURE_FILE_NAME = "features.arff"
    const val FEATURE_SET_CAPACITY = 10000
}