package com.example.thiagoevoa.estudoandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.thiagoevoa.estudoandroid.model.Professional

class ProfessionalViewModel : ViewModel() {
    var professionalsLiveData = MutableLiveData<MutableList<Professional>>()
    var professionalLiveData = MutableLiveData<Professional>()
}