package com.lh1167994.gradetracker

class Class(
    var className: String? = null,
    var grades: Grade? = null,
    var uid: String? = null
)
{
    override fun toString(): String
    {
        if (className != null)
        {
            return className!!
        }
        else
        {
            return "undefined"
        }
    }
}
