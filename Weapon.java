public abstract class Weapon implements Item{
    protected double plusDamageClose;
    protected double plusDamageWide;

    public double[] getPlusEffect(){
        double[] returned = new double[9];
        returned[0] = plusDamageClose;
        returned[1] = plusDamageWide;
        return returned;
    }
    public abstract int getPrice();

}
