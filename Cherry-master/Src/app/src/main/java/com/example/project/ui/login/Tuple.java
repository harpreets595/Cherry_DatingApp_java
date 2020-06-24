package com.example.project.ui.login;

/**
 * Store two values together.
 * @param <S>
 * @param <T>
 */
public class Tuple<S, T> {

    public  static <S,T> Tuple<S,T> tuple(S first, T second) {
        return new Tuple<S,T>()
                .setFirst(first)
                .setSecond(second);
    }

    private S first;
    private T second;

    public S getFirst() {
        return first;
    }

    public Tuple<S, T> setFirst(S first) {
        this.first = first;
        return this;
    }

    public T getSecond() {
        return second;
    }

    public Tuple<S, T> setSecond(T second) {
        this.second = second;
        return this;
    }
}
