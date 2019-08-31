public interface Item{
    public int getPrice();

    //this method returnes an array, in which you can read, which abilities rised up
    //the array has always the length 9, because we have totally 9 abilities. The order is:
    //      0               1               2         3         4           5           6              7          8
    // close attack     wide attack     defence     speed     dodge     persuasive   investigation   sneak      heal
    public double[] getPlusEffect();
    public String toString();
}
