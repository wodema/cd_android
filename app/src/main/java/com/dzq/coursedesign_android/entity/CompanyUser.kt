package com.dzq.coursedesign_android.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompanyUser(
    var id: Int? = null,

    var companyId: Int? = null,

    var userName: String? = null,

    var userMobile: String? = null,

    var userEmail: String? = null,

    var idCard: String? = null,

    var companyInfo: CompanyInfo? = null,

    var authStatus: Int? = null
) : Parcelable
