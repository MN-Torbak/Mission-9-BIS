package com.openclassrooms.firebaseREM

import android.app.Application
import com.openclassrooms.firebaseREM.api.REMRoomDatabase

class REMApplication : Application() {

    val database by lazy { REMRoomDatabase.getDatabase(this) }
}