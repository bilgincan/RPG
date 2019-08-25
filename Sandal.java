public class Sandal extends Shoe{
    private final int price = 15;

    public Sandal(){
        super();
        super.plusSpeed = 0.5;
        super.plusDodge = 0.5;
    }

    public int getPrice(){
        return this.price;
    }

}
