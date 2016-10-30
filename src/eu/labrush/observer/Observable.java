package eu.labrush.observer;

public interface Observable {
    public void setObserver(Observer obs);
    public void removeObserver();
    public void notifyObserver(String str);
}