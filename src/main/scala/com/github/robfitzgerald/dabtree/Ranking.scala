package com.github.robfitzgerald.dabtree

import com.github.robfitzgerald.dabtree.pedrosorei.Payload
import spire.implicits._
import spire.math.Numeric

object Ranking {

  /**
    * a default ranking is not bad or good
    */
  private[Ranking] val DefaultRanking = 0.5D

  /**
    * a generic ranking function which ranks by reward value only
    * @tparam State generic state type
    * @tparam Action generic action type
    * @tparam Value generic value type
    * @return an ordering rank, ascending
    */
  final def GenericRanking[State, Action, Value]: Payload[State, Action, Value] => Double =
    (payload: Payload[State, Action, Value]) => {
      payload._1.reward
    }

  /**
    * each partial solution has a cost, which is between the min and max globally-observed costs. this function
    * creates a ranking based on these values.
    * @tparam State generic state type
    * @tparam Action generic action type
    * @tparam Value generic value type
    * @return an ordering rank, ascending
    */
  final def CostLowerBoundedRanking[State, Action, Value : Numeric]: Payload[State, Action, Value] => Double =
    (payload: Payload[State, Action, Value]) => {
      val (banditParent, globalsOption) = payload

      {
        for {
          costBound <- banditParent.costBound
          globals <- globalsOption
        } yield {
          if (banditParent.mctsStats.observations < 2) DefaultRanking // at visits == 0 or 1, we don't have guarantees on global coefficient values
          else {
            val numer: Value = costBound - globals.state.gBest
            val denom: Value = globals.state.gWorst - globals.state.gBest

            1 - (numer / denom).toDouble
          }
        }
      } match {
        case None => DefaultRanking
        case Some(ranking) => ranking
      }
    }
}
