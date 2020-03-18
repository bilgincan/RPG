public class LumberjackAxe extends Weapon{
    private final int price = 20;

    public LumberjackAxe(){
        super();
        super.plusDamageClose = 0.25;
    }

    public int getPrice(){
        return this.price;
    }


}
