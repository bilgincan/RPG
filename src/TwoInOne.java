public class TwoInOne<V,C>{
  private V first;
  private C second;

  public TwoInOne(V first,C second){
    this.first = first;
    this.second = second;
  }

  public V getFirst(){
    return this.first;
  }

  public C getSecond(){
    return this.second;
  }
}
