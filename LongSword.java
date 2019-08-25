public class LongSword extends Weapon{
    private final int price = 125;

    public LongSword(){
        super();
        super.plusDamageWide = 0;
        super.plusDamageClose = 1.5;
    }

    public int getPrice(){
        return this.price;
    }
}
