import java.util.Random;
import java.util.Scanner;

/**
 * MonsterPoker
 */
public class MonsterPoker {

  public class Player {
    String name;
    double hp = 1000;
    int deck[] = new int[5];
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
      this.drawCard();//カードをドロー
      this.displayDeck();//手札を表示
      if(this.name=="Player"){
        this.exchange(scanner);//手札を交換
      }else{
        this.decideExchangeCpu();
        displayExchangeCard();
        if (playerExchangeCards.charAt(0) != '0') {
          this.exchangeCard(playerExchangeCards);
          this.displayDeck();
          this.decideExchangeCpu();
          displayExchangeCard();
          if (playerExchangeCards.charAt(0) != '0') {
            this.exchangeCard(playerExchangeCards);
            this.displayDeck();
          }
        }        
      }
    }

    public void drawCard(){
      for (int i = 0; i < this.deck.length; i++) {
        this.deck[i] = card.nextInt(5);
      }
    }

    public void displayDeck(){
      System.out.print("["+this.name+"]");
      for (int i = 0; i < this.deck.length; i++) {
        System.out.printf("%s ", monsters[this.deck[i]]);
      }
      System.out.println();
    }

    public void exchangeCard(String exchange){
      for (int i = 0; i < exchange.length(); i++) {
        this.deck[Character.getNumericValue(exchange.charAt(i)) - 1] = card.nextInt(5);
      }
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

    public void decideExchangeCpu()throws InterruptedException{
      System.out.println("CPUが交換するカードを考えています・・・・・・");
      Thread.sleep(2000);
      // cpuDeckを走査して，重複するカード以外のカードをランダムに交換する
      // 0,1,0,2,3 といったcpuDeckの場合，2枚目，4枚目，5枚目のカードをそれぞれ交換するかどうか決定し，例えば24といった形で決定する
      // 何番目のカードを交換するかを0,1で持つ配列の初期化
      // 例えばcpuExchangeCards[]が{0,1,1,0,0}の場合は2,3枚目を交換の候補にする
      for (int i = 0; i < cpuExchangeCards.length; i++) {
        cpuExchangeCards[i] = -1;
      }
      for (int i = 0; i < this.deck.length; i++) {
        if (cpuExchangeCards[i] == -1) {
          for (int j = i + 1; j < this.deck.length; j++) {
            if (this.deck[i] == this.deck[j]) {
              cpuExchangeCards[i] = 0;
              cpuExchangeCards[j] = 0;
            }
          }
          if (cpuExchangeCards[i] != 0) {
            cpuExchangeCards[i] = card.nextInt(2);// 交換するかどうかをランダムに最終決定する
            // this.cpuExchangeCards[i] = 1;
          }
        }
      }
    }

    public void judge()throws InterruptedException{
      // 役判定用配列の初期化
      for (int i = 0; i < this.yaku.length; i++) {
        this.yaku[i] = 0;
      }
      // モンスターカードが何が何枚あるかをyaku配列に登録
      for (int i = 0; i < this.deck.length; i++) {
        this.yaku[this.deck[i]]++;
      }
      // 役判定
      // 5が1つある：ファイブ
      // 4が1つある：フォー
      // 3が1つあり，かつ，2が1つある：フルハウス
      // 2が2つある：ツーペア
      // 3が1つある：スリー
      // 2が1つある：ペア
      // 1が5つある：スペシャルファイブ
      // 初期化
      five = false;
      four = false;
      three = false;
      pair = 0; // pair数を保持する
      one = 0;// 1枚だけのカードの枚数
      // 手札ごとのthis.yaku配列の作成
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
  int monsterAP[] = { 10, 20, 30, 25, 30 }; //各モンスターのAP
  int monsterDP[] = { 40, 20, 25, 15, 20 }; //各モンスターのDP
  int cpuExchangeCards[] = new int[5];// それぞれ0,1でどのカードを交換するかを保持する．{0,1,1,0,1}の場合は2,3,5枚目のカードを交換することを表す
  String playerExchangeCards = new String();// 交換するカードを1~5の数字の組み合わせで保持する．上の例の場合，"235"となる．
  // 役判定用フラグ
  // 役判定
  // 5が1つある：ファイブ->five = true
  // 4が1つある：フォー->four = true
  // 3が1つあり，かつ，2が1つある：フルハウス->three = true and pair = 1
  // 2が2つある：ツーペア->pair = 2
  // 3が1つある：スリー->three = true;
  // 2が1つある：ペア->pair = 1
  // 1が5つある：スペシャルファイブ->one=5
  boolean five = false;
  boolean four = false;
  boolean three = false;
  int pair = 0; // pair数を保持する
  int one = 0;// 1枚だけのカードの枚数

  Player player = new Player("Player");
  Player cpu = new Player("CPU");

  public void displayExchangeCard(){
    this.playerExchangeCards = "";
    for (int i = 0; i < cpuExchangeCards.length; i++) {
      if (this.cpuExchangeCards[i] == 1) {
        this.playerExchangeCards = this.playerExchangeCards + (i + 1);
      }
    }
    if (this.playerExchangeCards.length() == 0) {
      this.playerExchangeCards = "0";
    }
    System.out.println(this.playerExchangeCards);
  }

  /**
   * 5枚のモンスターカードをプレイヤー/CPUが順に引く
   *
   * @throws InterruptedException
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

    // バトル
    System.out.println("バトル！！");
    player.attack(cpu);
    cpu.attack(player);

    System.out.println("PlayerのHPは" + player.hp);
    System.out.println("CPUのHPは" + cpu.hp);

  }

}