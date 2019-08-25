public class CrossBow extends Weapon{
    private final int price = 150;

    public CrossBow(){
        super();
        super.plusDamageWide = 1;
    }

    public int getPrice(){
        return this.price;
    }


}
