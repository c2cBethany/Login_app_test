package com.example.login2.datasource

class DataClassForum {
    var dataDesc: String? = null
    var userID: String? = null

    constructor(dataDesc: String?, userID: String?){
        this.dataDesc = dataDesc
        this.userID = userID
    }
    constructor()
    {}
}