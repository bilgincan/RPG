public class ShortSword extends Weapon{
    private final int price = 50;

    public ShortSword(){
        super();
        super.plusDamageClose = 0.5;
    }

    public int getPrice(){
        return this.price;
    }


}
