package com.example.workshop

import org.json.JSONObject

class Workshop(jsonObject: JSONObject) {
    var name: String = ""
    var imageUrl: String = ""
    var description: String = ""
    var text:String = ""
    var videoUrl: String = ""

    init {
        name = jsonObject.getString("name")
        imageUrl = jsonObject.getString("image")
        description = jsonObject.getString("description")
        text = jsonObject.getString("text")
        videoUrl = jsonObject.getString("video")
    }
}