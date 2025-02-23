package org.example
import kotlin.random.Random


//fun <T> Iterable<T>.containsAny(elements: Iterable<T>): Boolean {
//    val elementsSet = elements.toSet()
//    return this.any { it in elementsSet }
//}

enum class Suit(val w:Int){
    HEART(0),
    DIAMOND(1),
    CLUB(2),
    SPADE(3);

}

enum class Value(val weight:Int){
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13),
    ACE(14);

}


data class Card constructor(val cardSuit: Suit , val value:Value)
{
    companion object{
        private var counter:Int = 0

    }
    private val Id:Int
    override fun toString(): String {
        return "${Id} ${value.name} ${cardSuit.name}"
    }

    init {
        counter+=1
        this.Id = counter
    }

    fun getId():Int{
        return this.Id
    }

}



class Game
{
        private val cardsMix:MutableList<Card> = deck.toMutableList()
        private var playerId:Int = 0
        private var countCard:Int = 36
        private val countCardInHand:Int = 6;
        private var table:MutableList<Card> = mutableListOf()
        private val mainSuit:Suit = Suit.HEART
        private var hands:MutableList<MutableList<Card>> =  MutableList(2) { mutableListOf() }
    companion object{
        private val deck:List<Card>  = generateDeck()
        private fun generateDeck():List<Card> {

            val tmp: List<Pair<Suit,Value>> = Value.entries.flatMap { first -> Suit.entries.map { second -> second to first } }
            val cards :List<Card>  = tmp.map{p->Card(p.first,p.second)}
            return cards
        }


    }

    init {
        mixDeck()
        dealing()
    }

    private fun alter(playerId: Int): Int {
        return if (playerId==0) 1 else 0
    }
    private fun mixDeck() {
        this.cardsMix.shuffle(Random)
    }

    private fun updateDeckCards(countRmvCards:Int){
        this.cardsMix.removeAll { c-> cardsMix.indexOf(c)<countRmvCards}
        this.countCard -= countRmvCards
    }

    private fun dealing(){
        this.hands[0] = cardsMix.take(this.countCardInHand).toMutableList()
        updateDeckCards(this.countCardInHand)
        this.hands[1] = cardsMix.take(this.countCardInHand).toMutableList()
        updateDeckCards(this.countCardInHand)
    }
    private fun additionalCardDraw(n1:Int,  n2:Int){ // n1 - тот , кто атакует
        if (this.countCard > 0){
            var nn1:Int = n1
            var nn2:Int = n2
            if (nn2<0) nn2=0
            if (nn1<0) nn1=0

            if (nn1+nn2>this.countCard)
            {
                nn1 = if (nn1>this.countCard) this.countCard else nn1
                nn2 = if (this.countCard-nn1==0) 0 else if(this.countCard-nn1>=nn2) nn2 else this.countCard-nn1

            }
            this.hands[playerId].addAll(cardsMix.take(nn1))
            println("Атакующий игрок взял  ${nn1} карт")
            updateDeckCards(nn1)
            this.hands[alter(playerId)].addAll(cardsMix.take(nn2))
            println("Обороняющийся игрок взял ${nn2} карт")
            updateDeckCards(nn2)
        }

    }

    private fun takeCard(playerId: Int){
        val cntCardsAttack = this.hands[playerId].size
        val cntCardsDefeat = this.hands[alter(playerId)].size
        if (cntCardsAttack==0 && cntCardsDefeat==0)
            dealing()
        else if (cntCardsAttack!=0 && cntCardsDefeat!=0)
            additionalCardDraw(this.countCardInHand.minus(cntCardsAttack),this.countCardInHand.minus(cntCardsDefeat))

    }
    private fun handPrintln(playerId:Int){
        this.hands[playerId].forEach { c -> println("id=${c.getId()} масть ${c.cardSuit} достоинство ${c.value}") }
    }
    fun play()
    {
        takeCard(this.playerId)
        println("Игра началась!!!")
        var input2:String? = null
        while (this.hands[0].isNotEmpty() && this.hands[1].isNotEmpty() && this.countCard>0) {
            if (this.hands[0].isEmpty()&&this.hands[1].isEmpty()) takeCard(this.playerId)
            if (this.countCard<=0)
            {
                if (this.hands[0].isEmpty()) println("Победил игрок №${0}")
                else println("Победил игрок №${1}")
            }

            println("Атакует игрок под номером № ${this.playerId}")
            println("Выберите карту, которой хотите атаковать, используя id или закончите раунд")
            handPrintln(this.playerId)
            var input = readLine()

            while (input!="STOP"){
                val id = input?.toIntOrNull()
                val cardChoose = this.hands[this.playerId].first { c -> c.getId() == id }
                if (this.table.isNotEmpty())
                {
                    val cardsOfTable = this.table.map { c -> c.value.weight }
                    if (cardsOfTable.contains(cardChoose.value.weight)){
                        moveAttack(this.playerId, cardChoose)
                        println("Удачная атака")
                    }
                    else {
                        println("Вы не можете подкинуть карту этого достоинства, потому что её нет на столе")
                        println("Выберите карту, которой хотите атаковать, используя id или закончите раунд, написав STOP")
                        handPrintln(this.playerId)
                        input = readLine()
                        continue
                    }
                }
                else {
                    moveAttack(this.playerId, cardChoose)
                }

                println("Защищается игрок под номером № ${this.playerId}")
                println("Выберите карту, которой хотите защищаться, используя id или закончите раунд, написав TAKE(тогда Вы возбмете карты в руку)")
                handPrintln(this.playerId)
                input2 = readLine()
                while (input2!="TAKE"){

                    var idd = input2?.toIntOrNull()

                    var rule1:Boolean = false
                    var rule2:Boolean = false
                    var rule3:Boolean = false

                    if (idd==null){ idd = -1}
                    val cardChoose2 = this.hands[this.playerId].first { c -> c.getId() == idd }
                    rule1 = cardChoose.cardSuit.name==cardChoose2.cardSuit.name && cardChoose2.value.weight>cardChoose.value.weight
                    rule2 = cardChoose2.cardSuit.name==this.mainSuit.name && cardChoose.cardSuit.name!=this.mainSuit.name
                    rule3 = cardChoose.cardSuit.name==this.mainSuit.name && cardChoose2.cardSuit.name==this.mainSuit.name && cardChoose2.value.weight>cardChoose.value.weight

                    if ((rule1 || rule2 ||rule3))
                    {
                        moveDefeat(playerId,cardChoose2)
                        println("Удачная защита")
                        break
                    }
                    else
                    {
                        println("Картой, которую Вы выбрали нельзя защититься, выберите другую подходящую или примите все карты (TAKE)")
                        println("Выберите карту, которой хотите защищаться, используя id или закончите раунд, написав TAKE(тогда Вы возбмете карты в руку)")
                        handPrintln(playerId)
                        input2 = readLine()
                        continue
                    }


                }
                if (input2=="TAKE"){

                    this.hands[this.playerId].addAll(this.table)
                    break
                }
                println("Выберите карту, которой хотите атаковать(подкинуть), используя id или закончите раунд, написав STOP")
                println("!!!Раунд не закончится, если не написать STOP или оппонент не заберет карты!!!")
                handPrintln(this.playerId)
                input = readLine()
            }
            this.table.clear()
            takeCard(this.playerId)
            this.playerId  = alter(this.playerId)
            println("В колоде осталось ${this.countCard} карт...")

        }

    }
    private fun moveAttack(playerId: Int,card:Card){
        println("Игрок ${playerId} атаковал :\n${card.toString()} ")
        this.table.add(card)
        this.hands[playerId].remove(card)
        this.playerId = alter(playerId)
    }

    private fun moveDefeat(playerId: Int,card:Card){
        println("Игрок ${playerId} защитился :\n${card.toString()} ")
        this.table.add(card)
        this.hands[playerId].remove(card)
        this.playerId = alter(playerId)
    }



}
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val g:Game = Game()
    g.play()

}