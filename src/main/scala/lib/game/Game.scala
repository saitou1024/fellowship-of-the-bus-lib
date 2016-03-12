package com.github.fellowship_of_the_bus.lib
package game

import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.{StateBasedGame}

trait Game {
  protected var isGameOver: Boolean = false

  def update(gc: GameContainer, game: StateBasedGame, delta: Int): Unit

  def gameOver(): Unit = {
    isGameOver = true
  }
}