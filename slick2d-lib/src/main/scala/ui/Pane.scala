package com.github.fellowship_of_the_bus
package lib
package slick2d
package ui

import org.newdawn.slick.{GameContainer, Graphics, Color}
import org.newdawn.slick.state.{StateBasedGame}

import game.{Game => SlickGame}

abstract class AbstractPane(x: Float, y: Float, width: Float, height: Float, borderColor: Color = Color.black)
          (implicit bg: Color)
    extends AbstractUIElement(x, y, width, height) {

  type Game >: Null <: SlickGame
  protected var game: Game = null

  private var children: List[UIElement] = List()

  def addChildren(child: UIElement, childs: UIElement*): Unit = {
    val newchildren = child::childs.toList
    addChildren(newchildren)
  }

  def addChildren(newchildren: List[UIElement]): Unit = {
    for (c <- newchildren) {
      c.absoluteX += absoluteX
      c.absoluteY += absoluteY
    }
    children = newchildren++children
  }

  def update(gc: GameContainer, sbg: StateBasedGame, delta: Int): Unit = {
    for (child <- children) {
      child.update(gc, sbg, delta)
    }
  }

  def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    val linewidth = g.getLineWidth
    g.setLineWidth(3)

    g.setColor(bg)
    g.fillRect(0, 0, width, height)
    g.setColor(borderColor)
    g.drawRect(0, 0, width, height)

    g.setLineWidth(linewidth)
  }

  override def render(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    g.translate(x, y)
    draw(gc, sbg, g)
    for (child <- children; if (child.isVisible)) {
      child.render(gc, sbg, g)
    }
    g.translate(-x, -y)
  }

  override def init(gc: GameContainer, sbg: StateBasedGame): Unit = {
    super.init(gc, sbg)
    for (child <- children) {
      child.setState(uiState)
      child.init(gc, sbg)
    }
  }

  def resetGame(g: SlickGame): Unit = {
    game = g.asInstanceOf[Game]
    reset()
    for (c <- children) {
      if (c.isInstanceOf[AbstractPane]) {
        c.asInstanceOf[AbstractPane].resetGame(g)
      }
    }
  }

  def reset(): Unit = ()
}

class Pane(x: Float, y: Float, width: Float, height: Float, borderColor: Color = Color.black)
          (implicit bg: Color)
    extends AbstractPane(x, y, width, height) {

  type Game = SlickGame
}
