public class OrdinaryAxe extends Weapon{
    private final int price = 50;

    public OrdinaryAxe(){
        super();
        super.plusDamageClose = 0.5;
    }

    public int getPrice(){
        return this.price;
    }
}
