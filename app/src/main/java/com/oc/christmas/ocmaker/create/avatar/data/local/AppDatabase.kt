package com.oc.christmas.ocmaker.create.avatar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oc.christmas.ocmaker.create.avatar.data.local.dao.UserDao
import com.oc.christmas.ocmaker.create.avatar.data.local.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}