package com.example.thiagoevoa.estudoandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.thiagoevoa.estudoandroid.model.Schedule

class ScheduleViewModel : ViewModel() {
    var schedulesLiveData = MutableLiveData<MutableList<Schedule>>()
    var scheduleLiveData = MutableLiveData<Schedule>()

//    fun getSchedules(){
//        ListAsyncTask(URL_SCHEDULE).execute()
//        schedulesLiveData.value = getScheduleFromJSON(ListAsyncTask.result)
//    }
//
//    fun getSchedules(query: String?) {
//        schedulesLiveData.value = getScheduleFromJSON(ListAsyncTask(URL_SCHEDULE).execute(query).get())
//    }
//
//    fun getSchedule(): Schedule? {
//        return scheduleLiveData.value
//    }
//
//    fun getSchedule(position: Int): Schedule? {
//        scheduleLiveData.value = schedulesLiveData.value?.get(position)
//        return scheduleLiveData.value
//    }
//
//    fun setSchedule(schedule: Schedule) {
//        scheduleLiveData.value = schedule
//    }
}

