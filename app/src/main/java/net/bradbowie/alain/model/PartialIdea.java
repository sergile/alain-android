package net.bradbowie.alain.model;

import net.bradbowie.alain.util.SerializationUtils;

/**
 * Created by bradbowie on 4/11/16.
 */
public class PartialIdea {
    private String word;
    private boolean left;
    private boolean right;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public String toString() {
        return SerializationUtils.toJsonSafe(this);
    }

    public static PartialIdea build(String word, boolean left, boolean right) {
        PartialIdea pi = new PartialIdea();
        pi.setWord(word);
        pi.setLeft(left);
        pi.setRight(right);
        return pi;
    }
}
