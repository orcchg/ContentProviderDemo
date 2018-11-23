package com.orcchg.contentproviderdemo.hostapp

data class Model(val id: Int, val name: String, val description: String, val temperature: Double, val season: String)
data class ListModels(val models: List<Model>)
