package com.gado.madarsofttask.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "age")
    val age: Int,
    
    @ColumnInfo(name = "job")
    val job: String,
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "gender")
    val gender: String
)