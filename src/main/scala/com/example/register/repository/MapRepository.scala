package com.example.register.repository

import com.example.register.actor.RegisterActor
import com.example.register.actor.RegisterActor.SearchResponse
import com.example.register.traits.RegisterRepository

class MapRepository extends RegisterRepository {

  var map: Map[String, List[Double]] = Map[String, List[Double]]()

  override def save(name: String, value: Double): Unit = {
    if (map contains (name)) {
      val register: Option[List[Double]] = map get (name)
      val list: List[Double] = register.get
      val updatedList = list :+ value
      val newMap = map + (name -> updatedList)
      map = newMap
    }else{
      val newMap = map + (name -> List(value))
      map = newMap
    }
//    println("Saved " + value + " to " + name)
  }

  override def search(name: String): Option[RegisterActor.SearchResponse] = {
    if (map contains (name)) {
      val register: Option[List[Double]] = map get (name)
      val list: List[Double] = register.get
      val sr = SearchResponse(list.min, list.max, list, list.length)
//      print(sr)
      Option(sr)
    }else{
      val sr = SearchResponse(0,0,Nil,0)
//      print(sr)
      Option(sr)
    }
  }
}
