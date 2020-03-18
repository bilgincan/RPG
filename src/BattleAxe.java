public class BattleAxe extends Weapon{
    private final int price = 100;

    public BattleAxe(){
        super();
        super.plusDamageClose = 1;
    }

    public int getPrice(){
        return this.price;
    }


}
