package com.erencol.sermon.viewmodelpkg

import androidx.databinding.BaseObservable
import com.erencol.sermon.model.SpecialDay

class SpecialDaysCellViewModel(specialDay: SpecialDay): BaseObservable(){

    var specialDayModel: SpecialDay = specialDay

    fun getDayName():String{
        return specialDayModel.name
    }

    fun getDayMiladi():String {
        return specialDayModel.miladi.day.toString() + "." + specialDayModel.miladi.month + " " + specialDayModel.miladi.weekday
    }

    fun getDayHicri():String {
        return specialDayModel.hicri.day.toString() + "." + specialDayModel.hicri.month
    }
}