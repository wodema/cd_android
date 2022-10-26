package com.dzq.coursedesign_android.entity

data class CompanyUser(
    val id: Int,

    val companyId: Int,

    val userName: String,

    val userMobile: String,

    val userEmail: String,

    val userAvatar: String,

    val idCard: String,

    val authStatus: Int
)
