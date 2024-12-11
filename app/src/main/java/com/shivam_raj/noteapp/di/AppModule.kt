package com.shivam_raj.noteapp.di

import android.content.Context
import androidx.room.Room
import com.shivam_raj.noteapp.database.NoteDao
import com.shivam_raj.noteapp.database.NoteDatabase
import com.shivam_raj.noteapp.database.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NoteDao): NoteRepository {
        return NoteRepository(dao)
    }

    @Provides
    @Singleton
    fun provideNoteDao(@ApplicationContext context: Context): NoteDao {
        return Room.databaseBuilder(
            context = context,
            klass = NoteDatabase::class.java,
            name = "Note_Database"
        ).fallbackToDestructiveMigration().build().noteDao()
    }
}