public class Bow extends Weapon{
    private final int price = 50;

    public Bow(){
        super();
        super.plusDamageWide = 0.5;
    }

    public int getPrice(){
        return this.price;
    }


}
