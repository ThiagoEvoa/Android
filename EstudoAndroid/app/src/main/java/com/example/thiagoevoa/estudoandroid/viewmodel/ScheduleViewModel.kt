package com.example.thiagoevoa.estudoandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.thiagoevoa.estudoandroid.model.Schedule

class ScheduleViewModel : ViewModel() {
    var schedulesLiveData = MutableLiveData<MutableList<Schedule>>()
    var scheduleLiveData = MutableLiveData<Schedule>()
}