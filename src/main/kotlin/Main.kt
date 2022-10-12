import kotlin.random.Random

// Enum to hold the options for each Attack
enum class Weapon{ROCK, PAPER, SCISSORS}

// Class to hold a player/enemy Attack choice and facilitate comparison
class Attack(private var option: Weapon){
    private fun getWeapon():Weapon{
        return option
    }

    operator fun compareTo(rhs:Attack) : Int{
        when (option){
            Weapon.ROCK->{
                if(rhs.getWeapon() == Weapon.ROCK) {
                    println("There is a clash of wood on wood, no damage")
                    return 0
                }
                else if (rhs.getWeapon() == Weapon.PAPER){
                    println("Your club is stopped by their shield, giving them time to strike.")
                    return -1
                }
                else if (rhs.getWeapon() == Weapon.SCISSORS){
                    println("There is a clash of wood on steel, as your club forces them back.")
                    return 1
                }
            }
            Weapon.PAPER->{
                if(rhs.getWeapon() == Weapon.ROCK){
                    println("Your shield stops their club, allowing you time to strike.")
                    return 1
                }
                else if (rhs.getWeapon() == Weapon.PAPER){
                    println("You both cower behind shields. No damage.")
                    return 0
                }
                else if (rhs.getWeapon() == Weapon.SCISSORS){
                    println("Your shield does not protect you from their sword.")
                    return -1
                }
            }
            Weapon.SCISSORS-> {
                if (rhs.getWeapon() == Weapon.ROCK){
                    println("There is a clash of wood on steel, their club pushing your sword back.")
                    return -1
                }
                else if (rhs.getWeapon() == Weapon.PAPER){
                    println("There is a clash of wood on steel, as your sword bites in to their shield, forcing them back")
                    return 1
                }
                else if (rhs.getWeapon() == Weapon.SCISSORS){
                    println("There is a clash of steel on steel, as your swords collide. No Damage")
                    return 0
                }
            }
        }
        println("Something went wrong, Attack comparison failed. Defaulting to Player win.")
        return 1
    }
}

// Base class for anything that participates in a fight, players and enemies
open class Entity {
    protected var health = 10
    open fun getMove():Attack{ return Attack(Weapon.SCISSORS)}
    @JvmName("getHealth1")
    fun getHealth() : Int{ return health }
    open fun getHit(){ health -= 1}
    open fun display() { println("If you're seeing this, an error happened. Entity as enemy.")}
}

// Class for player interaction. Includes I/O and player stats.
class Player :Entity(){
    override fun getMove(): Attack {
        while (true){
            val attackInput = readln()
            when(attackInput.lowercase()){
                "1","rock","club"->{return Attack(Weapon.ROCK)}
                "2","paper","shield"->{return Attack(Weapon.PAPER)}
                "3","scissors","sword"->{return Attack(Weapon.SCISSORS)}
                else->{println("Choice not recognized, please choose again")}
            }
        }
    }
}

// Base class for enemies, simplifies interface in fight.
open class Enemy :Entity(){
    override fun display() {
        println("If you're seeing this, an error happened. Enemy as enemy.")
    }
    open fun getName():String{ return "Error- EnemyBase"}
}

// Skeleton class. Favors swords, never uses clubs
class Skeleton : Enemy(){
    override fun display() {
        println("Skeleton Health: $health")
    }

    override fun getMove(): Attack {
        return when(Random.nextInt(0,4)){
            0,1,3-> Attack(Weapon.SCISSORS)
            2-> Attack(Weapon.PAPER)
            else-> Attack(Weapon.SCISSORS)
        }
    }

    override fun getName(): String {
        return "Skeleton"
    }
}

// Zombie Class. Favors Clubs, but occasionally uses shield or sword
class Zombie : Enemy(){
    override fun display() {
        println("Zombie Health: $health")
    }

    override fun getMove(): Attack {
        return when(Random.nextInt(0,4)){
            0,1-> Attack(Weapon.ROCK)
            2-> Attack(Weapon.PAPER)
            3-> Attack(Weapon.SCISSORS)
            else-> Attack(Weapon.SCISSORS)
        }
    }

    override fun getName(): String {
        return "Zombie"
    }
}

// Goblin Class. Favors shields, but occasionally uses swords or clubs
class Goblin : Enemy(){
    override fun display() {
        println("Zombie Health: $health")
    }

    override fun getMove(): Attack {
        return when(Random.nextInt(0,6)){
            0,1,4,5-> Attack(Weapon.PAPER)
            2-> Attack(Weapon.ROCK)
            3-> Attack(Weapon.SCISSORS)
            else-> Attack(Weapon.SCISSORS)
        }
    }

    override fun getName(): String {
        return "Zombie"
    }
}

// Class to handle everything to do with a fight.
class Fight(choice : Int){
    private val player : Player = Player()
    private val enemy : Enemy = generateEnemy(choice)

    fun start(){
        battleLoop()
    }

    private fun battleLoop(){
        while ((player.getHealth() > 0) and (enemy.getHealth() > 0)){
            displayFightStatus()
            val playerAttack = player.getMove()
            val enemyAttack = enemy.getMove()
            val result = playerAttack.compareTo(enemyAttack)
            if (result > 0){
                enemy.getHit()
            } else if (result < 0){
                player.getHit()
            }
        }
        if(player.getHealth() == 0) println("You have been felled in battle by a ${enemy.getName()}!")
        else println("You have triumphed over the ${enemy.getName()}!")
    }

    private fun displayFightStatus(){
        println("Player health is: ${player.getHealth()}")
        enemy.display()
    }
}

fun main(args: Array<String>) {

    var mainMenuInput =  ""

    println("Welcome to ADD GAME NAME HERE!")
    while(mainMenuInput != "exit"){
        mainMenuInput = mainMenu()
        try{
            val choice = mainMenuInput.toInt()
            val fight = Fight(choice)
            fight.start()
        }
        catch(nfe : NumberFormatException){ /* Nothing */ }
    }
}

// Main Menu loop. Continues until valid choice is made.
fun mainMenu():String{
    println("*******************")
    println("Select your enemy:")
    println("1. Skeleton")
    println("2. Zombie")
    println("3. Goblin")
    val choice = readln()
    when(choice){
        "1","2","3"->{}
        "exit"->{
            println("Bye!")
        }
        else->{
            println("That's not a choice!")
        }
    }
    return choice
}

fun generateEnemy(choice : Int) : Enemy{
    return when(choice){
        1->Skeleton()
        2->Zombie()
        3->Goblin()
        else->Enemy()
    }
}