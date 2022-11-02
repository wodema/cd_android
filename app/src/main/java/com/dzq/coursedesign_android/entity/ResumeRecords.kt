package com.dzq.coursedesign_android.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime

@Parcelize
data class ResumeRecords(
    var id: Int? = null,

    var companyId: Int? = null,

    var studentId: Int? = null,

    var positionId: Int? = null,

    var studentAvatar: String? = null,

    var studentName: String? = null,

    var studentMajor: String? = null,

    var studentMobile: String? = null,

    var studentEmail: String? = null,

    var studentGraduationDate: LocalDate? = null,

    var resumeUrl: String? = null,

    var companyLogo: String? = null,

    var workCity: String? = null,

    var studentSchool: String? = null,

    var companyName: String? = null,

    var positionName: String? = null,

    var positionSalary: String? = null,

    var educationRequired: String? = null,

    var applyDate: LocalDateTime? = null

) : Parcelable
