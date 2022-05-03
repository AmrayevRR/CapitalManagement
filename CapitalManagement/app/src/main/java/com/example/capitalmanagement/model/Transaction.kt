package com.example.capitalmanagement.model

data class Transaction(var category: String, var amount: Int, var description: String, var date: String, var account: String) {
    constructor() : this("", 0, "", "", "")
}