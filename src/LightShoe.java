public class LightShoe extends Shoe{
    private final int price = 30;

    public LightShoe(){
        super();
        super.plusSpeed = 1;
        super.plusDodge = 1;
    }

    public int getPrice(){
        return this.price;
    }

}
