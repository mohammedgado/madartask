package com.gado.madarsofttask.data.local.mapper

import com.gado.madarsofttask.data.local.entity.UserEntity
import com.gado.madarsofttask.data.model.User

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        age = this.age,
        job = this.job,
        title = this.title,
        gender = this.gender
    )
}

fun UserEntity.toModel(): User {
    return User(
        id = this.id,
        name = this.name,
        age = this.age,
        job = this.job,
        title = this.title,
        gender = this.gender
    )
}

fun List<UserEntity>.toModelList(): List<User> {
    return this.map { it.toModel() }
}

fun List<User>.toEntityList(): List<UserEntity> {
    return this.map { it.toEntity() }
}