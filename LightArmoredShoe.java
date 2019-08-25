public class LightArmoredShoe extends Shoe{
    private final int price = 50;

    public LightArmoredShoe(){
        super();
        super.plusSpeed = 0.5;
        super.plusDodge = 1;
        super.plusDefence = 0.2;
    }

    public int getPrice(){
        return this.price;
    }

}
