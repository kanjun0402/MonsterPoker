import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class MonsterPoker {
  public class Player {
    String name;
    double hp = 1000;
    int deck[] = new int[5];
    int cpuExchangeCards[] = new int[5];
    String playerExchangeCards = new String();
    int yaku[] = new int[5];
    double APmag;
    double DPmag;
    double AP;
    double DP;
    double damage;
    public Player(String name) {
      this.name = name;
    }
    public void drawAndExchange(Scanner scanner)throws InterruptedException{
      System.out.println(this.name+"のDraw！");
      this.drawCard();
      this.displayDeck();
      if(this.name=="Player"){
        this.exchange(scanner);
      }else{
        cpuTurn();
      }
    }
    public void cpuTurn()throws InterruptedException{
      this.decideExchangeCpu();
      this.displayExchangeCard();
      if (playerExchangeCards.charAt(0) != '0') {
        this.exchangeCard(this.playerExchangeCards);
        this.displayDeck();
        this.decideExchangeCpu();
        this.displayExchangeCard();
        if (playerExchangeCards.charAt(0) != '0') {
          this.exchangeCard(this.playerExchangeCards);
          this.displayDeck();
        }
      }        
    }
    public void drawCard(){
      IntStream.range(0, this.deck.length)
               .forEach(i -> this.deck[i] = card.nextInt(5));
    }
    public void displayDeck(){
      System.out.print("["+this.name+"]");
      IntStream.range(0,this.deck.length)
               .mapToObj(i -> monsters[this.deck[i]])
               .forEach(monster -> System.out.printf("%s ", monster));
      System.out.println();
    }
    public void exchangeCard(String exchange){
      exchange.chars()
              .map(Character::getNumericValue)
              .map(i -> i -1)
              .forEach(index -> deck[index] = card.nextInt(5));
    }
    public void exchange(Scanner scanner){
      System.out.println("カードを交換する場合は1から5の数字（左から数えた位置を表す）を続けて入力してください．交換しない場合は0と入力してください");
      String exchange = scanner.nextLine();
      if (exchange.charAt(0) != '0') {
        this.exchangeCard(exchange);
        this.displayDeck();
        System.out.println("もう一度カードを交換する場合は1から5の数字（左から数えた位置を表す）を続けて入力してください．交換しない場合は0と入力してください");
        exchange = scanner.nextLine();
        if (exchange.charAt(0) != '0') {
          this.exchangeCard(exchange);
          this.displayDeck();
        }
      }
    }
    public void displayExchangeCard(){
      this.playerExchangeCards = "";
      this.playerExchangeCards = IntStream.range(0, this.cpuExchangeCards.length)
                                          .filter(i -> this.cpuExchangeCards[i] == 1)
                                          .mapToObj(i -> String.valueOf(i + 1))
                                          .reduce("", String::concat);
      if (this.playerExchangeCards.length() == 0) {
        this.playerExchangeCards = "0";
      }
      System.out.println(this.playerExchangeCards);
    }
    public void decideExchangeCpu()throws InterruptedException{
      System.out.println("CPUが交換するカードを考えています・・・・・・");
      Thread.sleep(2000);
      IntStream.range(0, this.cpuExchangeCards.length).forEach(i -> this.cpuExchangeCards[i] = -1);
      IntStream.range(0, this.deck.length).forEach(i -> {
        if (this.cpuExchangeCards[i] == -1) {
          boolean hasDuplicate = IntStream.range(i + 1, this.deck.length).anyMatch(j -> this.deck[i] == this.deck[j]);
          if (hasDuplicate) {
              this.cpuExchangeCards[i] = 0;
              IntStream.range(i + 1, deck.length)
                       .filter(j -> this.deck[i] == this.deck[j])
                       .forEach(j -> this.cpuExchangeCards[j] = 0);
          }
          if (this.cpuExchangeCards[i] != 0) {
              this.cpuExchangeCards[i] = card.nextInt(2);
          }
        }
      });
    }
    public void judge()throws InterruptedException{
      IntStream.range(0, this.yaku.length)
               .forEach(i -> this.yaku[i] = 0);
      IntStream.of(this.deck)
               .forEach(card -> this.yaku[card]++);
      five = false;
      four = false;
      three = false;
      pair = 0;
      one = 0;
      for (int i = 0; i < this.yaku.length; i++) {
        if (this.yaku[i] == 1) {
          one++;
        } else if (this.yaku[i] == 2) {
          pair++;
        } else if (this.yaku[i] == 3) {
          three = true;
        } else if (this.yaku[i] == 4) {
          four = true;
        } else if (this.yaku[i] == 5) {
          five = true;
        }
      }
      System.out.println(this.name+"の役は・・");
      this.APmag = 1;
      this.DPmag = 1;
      if (one == 5) {
        System.out.println("スペシャルファイブ！AP/DPは両方10倍！");
        this.APmag = 10;
        this.DPmag = 10;
      } else if (five == true) {
        System.out.println("ファイブ！AP/DPは両方5倍！");
        this.APmag = 5;
        this.DPmag = 5;
      } else if (four == true) {
        System.out.println("フォー！AP/DPは両方4倍！");
        this.APmag = 3;
        this.DPmag = 3;
      } else if (three == true && pair == 1) {
        System.out.println("フルハウス！AP/DPは両方3倍");
        this.APmag = 3;
        this.DPmag = 3;
      } else if (three == true) {
        System.out.println("スリーカード！AP/DPはそれぞれ3倍と2倍");
        this.APmag = 3;
        this.DPmag = 2;
      } else if (pair == 2) {
        System.out.println("ツーペア！AP/DPは両方2倍");
        this.APmag = 2;
        this.DPmag = 2;
      } else if (pair == 1) {
        System.out.println("ワンペア！AP/DPは両方1/2倍");
        this.APmag = 0.5;
        this.DPmag = 0.5;
      }
      Thread.sleep(1000);
    }
    public void computeAPDP(){
      for (int i = 0; i < this.yaku.length; i++) {
        if (this.yaku[i] >= 1) {
          this.AP = this.AP + monsterAP[i] * this.yaku[i];
          this.DP = this.DP + monsterDP[i] * this.yaku[i];
        }
      }
      IntStream.range(0, yaku.length)
               .filter(i -> yaku[i] >= 1)
               .forEach(i -> {
                  AP += monsterAP[i] * yaku[i];
                  DP += monsterDP[i] * yaku[i];
               });
      this.AP = this.AP * this.APmag;
      this.DP = this.DP * this.DPmag;
    }
    public void attack(Player enemy)throws InterruptedException{
      System.out.print(this.name+"のDrawした");
      for (int i = 0; i < this.yaku.length; i++) {
        if (this.yaku[i] >= 1) {
          System.out.print(monsters[i] + " ");
          Thread.sleep(500);
        }
      }
      System.out.print("の攻撃！");
      Thread.sleep(1000);
      System.out.println(enemy.name+"のモンスターによるガード！");
      this.damage = (enemy.DP >= this.AP) ? 0 : this.AP - enemy.DP;
      System.out.println((this.damage == 0) ? enemy.name+"はノーダメージ！" : String.format(enemy.name+"は%.0fのダメージを受けた！", this.damage));
      enemy.hp -= this.damage;
    }
  }
  Random card = new Random();
  String monsters[] = { "スライム", "サハギン", "ドラゴン", "デュラハン", "シーサーペント" };// それぞれが0~4のIDのモンスターに相当する
  int monsterAP[] = { 10, 20, 30, 25, 30 };
  int monsterDP[] = { 40, 20, 25, 15, 20 };
  boolean five = false;
  boolean four = false;
  boolean three = false;
  int pair = 0;
  int one = 0;
  Player player = new Player("Player");
  Player cpu = new Player("CPU");

  /**
   *   @throws InterruptedException
   */
  public void drawPhase(Scanner scanner) throws InterruptedException {
    player.drawAndExchange(scanner);
    cpu.drawAndExchange(scanner);
  }
  public void battlePhase() throws InterruptedException {
    player.judge();
    player.computeAPDP();
    cpu.judge();
    cpu.computeAPDP();
    System.out.println("バトル！！");
    player.attack(cpu);
    cpu.attack(player);
    System.out.println("PlayerのHPは" + player.hp);
    System.out.println("CPUのHPは" + cpu.hp);
  }
}