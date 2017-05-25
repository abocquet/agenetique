package eu.labrush.observer;

public interface Observable {
    void setObserver(Observer obs);
    void removeObserver();
    void notifyObserver(String str);
}