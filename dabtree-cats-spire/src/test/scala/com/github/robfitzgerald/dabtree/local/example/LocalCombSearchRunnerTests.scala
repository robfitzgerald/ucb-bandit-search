package com.github.robfitzgerald.dabtree.local.example

import com.github.robfitzgerald.dabtree.common.example.DabTreeCombSearchFunctions
import com.github.robfitzgerald.dabtree.DefaultTest
import com.github.robfitzgerald.dabtree.local.Ranking
import com.github.robfitzgerald.dabtree.local.sampler.pedrosorei.Payload

class LocalCombSearchRunnerTests extends DefaultTest {
  "LocalCombSearchRunner" when {
    "called with a 11-item vector problem" ignore {
      "be fun" in new LocalCombSearchRunner {

        def problemSize = 10

        override def numChildren: Int = 5 // children should be 0.00, 0.25, 0.50, 0.75, 1.00
        override def maxValue: Double = math.sqrt(realSolution.size.toDouble)
        override def minValue: Double = realSolution.min

        override def activatedPayloadLimit: Int = 100
        override def totalPayloadCapacity: Int = 200
        override def pStarPromotion: Double = 0.5
        def maxIterations: Int = 25
        def sampleConfidence: Int = 10
        def samplesPerIteration: Int = sampleConfidence * numChildren
        def maxDurationSeconds: Int = 1
        def maxDuration: Long = 1000L * maxDurationSeconds


//         rank by reward and depth. prefer keeping deeper tree nodes active.
//         is possible when the state is represented as a countable collection.
//        final def rankingPolicy: Payload[State, Action, Value] => Double =
//          (payload: Payload[State, Action, Value]) => {
//            payload._1.reward +
//            payload._1.state.size.toDouble / problemSize
//          }
//        override def rankingPolicy = Ranking.GenericRanking
        override def rankingPolicy: Payload[State, Action, Value] => Double = Ranking.CostLowerBoundedRanking

        runSearch(maxIterations, maxDuration, samplesPerIteration) match {
          case None =>
            fail()
          case Some(result) =>
            result.bestState match {
              case None =>
                fail()
              case Some(bestState) =>
//                println("Search Result")
//                println(result)
//                println("Best Solution Found:")
//                println(bestState)
//                println("Real Solution:")
//                println(realSolution)
//                println("Distance:")
//                println(evaluate(bestState))
                DabTreeCombSearchFunctions(realSolution, possibleValues, numChildren, expandLimit).evaluate(bestState) should be < 0.5D // it should get reasonably close...
            }
        }
      }
    }
  }
}
