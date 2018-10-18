package com.example.thiagoevoa.estudoandroid.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.thiagoevoa.estudoandroid.model.Client

class ClientViewModel : ViewModel() {
    var clientsLiveData = MutableLiveData<MutableList<Client>>()
    var clientLiveData = MutableLiveData<Client>()
}