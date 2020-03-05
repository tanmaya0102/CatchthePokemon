package com.tanmaya.catchthepokemon

import android.location.Location

class Pokemon{
    var name:String?=null
    var des:String?=null
    var image:Int?=null
    var power:Double?=null
    var isCatch:Boolean?=false
    var loc:Location?=null
    constructor(name: String, des: String, image: Int, power: Double, lat: Double, log: Double)
    {
        this.name = name
        this.des = des
        this.image = image
        this.power = power
        this.loc=Location(name)
        this.loc!!.latitude=lat
        this.loc!!.longitude=log
        this.isCatch = false
    }
}