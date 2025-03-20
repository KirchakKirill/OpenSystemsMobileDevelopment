//package org.example
//
//import kotlin.random.Random
//
//fun <T> Iterable<T>.containsAny(elements: Iterable<T>): Boolean {
//    return this.any { it in elements }
//}
//
//enum class Suit { HEART, DIAMOND, CLUB, SPADE }
//enum class Value(val weight: Int) {
//    SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);
//
//    fun compareCard(other: Value): Int = weight - other.weight
//}
//
//data class Card(val cardSuit: Suit, val value: Value) {
//    companion object {
//        private var Id = 0
//    }
//
//    init { Id++ }
//    override fun toString() = "the card played was - $Id ${value.name} ${cardSuit.name}"
//    fun getId() = Id
//}
//
//class Game {
//    private val deck: List<Card> = generateDeck()
//    private var countCard = 36
//    private val countCardInHand = 5
//    private var playerId = 1
//    private val table = mutableListOf<Card>()
//    private val mainSuit = Suit.HEART
//    private var cardsMix = deck.toMutableList()
//    private var hands: MutableList<MutableList<Card>> = MutableList(2) { mutableListOf() }
//
//    init {
//        mixDeck()
//        dealing()
//    }
//
//    private fun generateDeck(): List<Card> =
//        Value.values().flatMap { value -> Suit.values().map { suit -> Card(suit, value) } }
//
//    private fun mixDeck() = cardsMix.shuffle(Random)
//    private fun updateDeckCards(countRmvCards: Int) {
//        cardsMix = cardsMix.drop(countRmvCards).toMutableList()
//        countCard -= countRmvCards
//    }
//
//    private fun dealing() {
//        hands[0] = cardsMix.filterIndexed { index, _ -> index % 2 == 0 }.take(countCardInHand).toMutableList()
//        hands[1] = cardsMix.filterIndexed { index, _ -> index % 2 != 0 }.take(countCardInHand).toMutableList()
//        updateDeckCards(countCardInHand * 2)
//    }
//
//    private fun additionalCardDraw(n1: Int, n2: Int) {
//        val cardsForPlayer1 = minOf(n1, countCard)
//        val cardsForPlayer2 = minOf(n2, countCard - cardsForPlayer1)
//        hands[0] = cardsMix.take(cardsForPlayer1).toMutableList()
//        hands[1] = cardsMix.take(cardsForPlayer2).toMutableList()
//        updateDeckCards(cardsForPlayer1 + cardsForPlayer2)
//    }
//
//    private fun takeCard() {
//        val (cntCardsPlayer1, cntCardsPlayer2) = hands.map { it.size }
//        if (cntCardsPlayer1 == 0 && cntCardsPlayer2 == 0) dealing()
//        else if (cntCardsPlayer1 != 0 && cntCardsPlayer2 != 0)
//            additionalCardDraw(countCardInHand - cntCardsPlayer1, countCardInHand - cntCardsPlayer2)
//    }
//
//    private fun HandPrintln(playerId: Int) {
//        hands[playerId - 1].forEach { println("масть ${it.cardSuit} достоинство ${it.value}") }
//    }
//
//    fun play() {
//        takeCard()
//        println("Чтобы закончить атаку напишите stop")
//        println("Ходит игрок под номером №1")
//        println("Игра началась!!!")
//        HandPrintln(playerId)
//        println("Выберите карту, которой хотите походить, используя id")
//
//        while (hands[0].isNotEmpty() && hands[1].isNotEmpty() && countCard > 0) {
//            val input = readLine()?.toIntOrNull()
//            if (input != null) {
//                if (playerId == 1) handlePlayer1Move(input)
//                else handlePlayer2Move(input)
//            } else {
//                println("Ошибка: введено не число!")
//            }
//        }
//    }
//
//    private fun handlePlayer1Move(input: Int) {
//        val lstWeight = hands[0].map { it.value.weight }
//        val lstTable = table.map { it.value.weight }
//        if (lstWeight.containsAny(lstTable)) move(playerId, input) else takeCard()
//    }
//
//    private fun handlePlayer2Move(input: Int) {
//        val emt = hands[1].filter { it.value.compareCard(table.last().value) < 0 }.isEmpty()
//        if (table.last().cardSuit != mainSuit) {
//            if (!emt) move(playerId, input) else hands[1].addAll(table)
//        } else {
//            val emt2 = hands[1].filter { it.value.compareCard(table.last().value) < 0 && it.cardSuit == mainSuit }.isEmpty()
//            if (emt2) move(playerId, input) else hands[1].addAll(table)
//        }
//    }
//
//    private fun move(playerId: Int, number: Int) {
//        val card = hands[playerId - 1].first { it.getId() == number }
//        println("Игрок $playerId сделал ход:\n$card")
//        table.add(card)
//        hands[playerId - 1].remove(card)
//        this.playerId = if (playerId == 1) 2 else 1
//    }
//}
//
//fun main() {
//    val game = Game()
//    game.play()
//}
