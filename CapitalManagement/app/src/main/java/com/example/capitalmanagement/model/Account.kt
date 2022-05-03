package com.example.capitalmanagement.model

data class Account(val title: String, val type: String, var amount: Int) {
    constructor() : this("", "", 0)
}