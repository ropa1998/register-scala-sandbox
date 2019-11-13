package com.example.register.traits

import com.example.register.actor.RegisterActor
import com.example.register.actor.RegisterActor.SearchResponse

trait RegisterRepository {
  def search(name: String): Option[RegisterActor.SearchResponse]

  def save(name: String, value: Double):Unit
}
