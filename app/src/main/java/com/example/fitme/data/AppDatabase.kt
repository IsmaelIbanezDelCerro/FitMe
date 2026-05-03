package com.example.fitme.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitme.data.dao.*
import com.example.fitme.data.entity.*

@Database(
    entities = [
        RegistroPeso::class,
        CheckDiario::class,
        EntrenamientoHistorial::class,
        EjercicioPersonal::class,
        ComidaPersonal::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun registroPesoDao(): RegistroPesoDao
    abstract fun checkDiarioDao(): CheckDiarioDao
    abstract fun entrenamientoDao(): EntrenamientoDao
    abstract fun ejercicioPersonalDao(): EjercicioPersonalDao
    abstract fun comidaPersonalDao(): ComidaPersonalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitme_database"
                )
                    // En desarrollo: si el esquema cambia, se borra y recrea
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
