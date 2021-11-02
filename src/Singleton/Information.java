package Singleton;

public class Information {
    public long rate;
    public long bestDistance;

    public static Information information = new Information();

    private Information() {
        rate = 0;
    }

    public void setBestDistance(long bestDistance){
        this.bestDistance = bestDistance;
    }
}
