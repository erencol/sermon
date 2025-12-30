package com.erencol.sermon.viewmodelpkg

import androidx.databinding.BaseObservable
import com.erencol.sermon.model.SpecialDay

class SpecialDaysCellViewModel(specialDay: SpecialDay): BaseObservable(){

    var specialDayModel: SpecialDay = specialDay

    fun getDayName():String{
        return specialDayModel.name
    }

    fun getDayDate():String {
        return specialDayModel.miladi.day.toString() + "." + specialDayModel.miladi.month + " " + specialDayModel.miladi.weekday
    }
}