package com.example.register.traits

import com.example.register.actor.RegisterActor.SearchResponse

trait Register {

  def search(name: String): SearchResponse

  def save(name: String, value: Double): Unit

}
