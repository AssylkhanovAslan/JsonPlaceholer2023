package kz.tutorial.jsonplaceholdertypicode.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int,
    val createdAt: String,
    val ViewCount: Int,
) : Parcelable