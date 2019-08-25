public class MediumArmoredShoe extends Shoe{
    private final int price = 55;

    public MediumArmoredShoe(){
        super();
        super.plusSpeed = 0.2;
        super.plusDodge = 0.5;
        super.plusDefence = 0.5;
    }

    public int getPrice(){
        return this.price;
    }

}
