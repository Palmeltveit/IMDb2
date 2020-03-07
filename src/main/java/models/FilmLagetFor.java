package models;

public enum FilmLagetFor {
    TV(1), FILM(2), KINO(3);


    private int val;
    private FilmLagetFor(int val) { this.val = val; }

    public int ord() { return this.val; }
}
