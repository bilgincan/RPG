import java.util.Arrays;

public class Test{

    public static void main(String[] args) {
        Player testPlayer = new Player("cnblgn","johnWick","Knight");
        Villian boss = new Villian("Boss");
        LongSword sword = new LongSword();

        testPlayer.purchase(sword);
        System.out.println("Player'ın parası: "+testPlayer.getCharacter().getMoney()+"\n");

        while(testPlayer.getCharacter().getHealth() > 0 && boss.getCharacter().getHealth() > 0){
            testPlayer.closeAttack(boss.getCharacter());
            System.out.println("Boss'un canı: "+boss.getCharacter().getHealth());

            if(boss.getCharacter().getHealth() > 0)
                    boss.closeAttack(testPlayer.getCharacter());

            System.out.println("Player'ın canı: "+testPlayer.getCharacter().getHealth()+"\n");
        }


    }
}
