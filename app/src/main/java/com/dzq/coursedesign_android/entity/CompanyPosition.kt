package com.dzq.coursedesign_android.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompanyPosition(
    var id: Int? = null,

    var companyId: Int? = null,

    var companyName: String? = null,

    var positionName: String? = null,

    var positionSalary: String? = null,

    var workCity: String? = null,

    var education: String? = null,

    var positionMajor: String? = null,

    var positionNumber: String? = null,

    var positionRequirement: String? = null
) : Parcelable