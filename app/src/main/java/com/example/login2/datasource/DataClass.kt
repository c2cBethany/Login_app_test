package com.example.login2

class DataClass {
    var dataDesc: String? = null
    var dataUser: String? = null
    var dataImage: String? = null

    constructor(dataDesc: String?, dataUser: String?, dataImage: String?) {
        this.dataDesc = dataDesc
        this.dataUser = dataUser
        this.dataImage = dataImage
    }

    constructor() {}
}