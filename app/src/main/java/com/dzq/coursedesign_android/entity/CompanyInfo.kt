package com.dzq.coursedesign_android.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompanyInfo(
    var id: Int? = null,
    var companyName: String? = null,
    var companyAddress: String? = null,
    var companyMobile: String? = null,
    var companyEmail: String? = null,
    var companyWebsite: String? = null,
    var companyLogo: String? = null,
    var companyIntroduction: String? = null,
) : Parcelable
