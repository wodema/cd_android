package com.dzq.coursedesign_android.entity

import java.time.LocalDate

data class Student(

    var id:Int? = null,

    var studentName: String? = null,

    var studentMobile: String? = null,

    var studentAvatar: String? = null,

    var studentEmail: String? = null,

    var school: String? = null,

    var major: String? = null,

    var graduationDate: LocalDate? = null,

    var resumeUrl: String? = null,

    var resumeId: Int? = null

)
