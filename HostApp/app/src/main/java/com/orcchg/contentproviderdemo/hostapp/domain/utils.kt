package com.orcchg.contentproviderdemo.hostapp.domain

import android.content.res.AssetManager
import com.google.gson.Gson
import java.nio.charset.Charset

fun readAssetFile(am: AssetManager, filename: String): String {
    val input = am.open(filename)
    val buffer = ByteArray(input.available())
    input.read(buffer)
    input.close()
    return String(buffer, Charset.forName("UTF-8"))
}

fun readModels(am: AssetManager): List<Model> {
    val json = readAssetFile(am, "models.json")
    return Gson().fromJson(json, ListModels::class.java).models
}
