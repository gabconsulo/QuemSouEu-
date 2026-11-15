package com.grupo.quemsoueu.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PartidaEntity::class], version = 1, exportSchema = false)
abstract class RankingDatabase : RoomDatabase() {
    abstract fun partidaDao(): PartidaDao

    companion object {
        @Volatile
        private var instancia: RankingDatabase? = null

        fun obter(context: Context): RankingDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    RankingDatabase::class.java,
                    "ranking.db",
                ).build().also { instancia = it }
            }
    }
}
