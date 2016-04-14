package net.bradbowie.alain.generator;

import net.bradbowie.alain.model.Idea;
import net.bradbowie.alain.model.PartialIdea;
import net.bradbowie.alain.util.CollectionUtils;
import net.bradbowie.alain.util.LOG;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bradbowie on 4/11/16.
 */
public class IdeaGenerator {

    private static final String TAG = LOG.tag(IdeaGenerator.class);

    private Random r = new Random();
    private List<PartialIdea> left;
    private List<PartialIdea> right;
    private List<Integer> colors;

    public void setPartialIdeas(List<PartialIdea> defaults) {
        left = new ArrayList<>();
        right = new ArrayList<>();

        for(PartialIdea pi : defaults) {
            if(pi.isLeft()) left.add(pi);
            if(pi.isRight()) right.add(pi);
        }
    }

    public void setColors(List<Integer> colors) {
        this.colors = colors;
    }

    public Idea next() {
        if(!CollectionUtils.isValid(left) || !CollectionUtils.isValid(right) || !CollectionUtils.isValid(colors)) return null;

        PartialIdea left = getLeft();
        PartialIdea right = getRight();
        while(left == right) {
            right = getRight();
        }

        int leftColor = getColor();
        int rightColor = getColor();
        while(leftColor == rightColor) {
            rightColor = getColor();
        }

        return Idea.build(left.getWord(), leftColor, right.getWord(), rightColor);
    }

    private PartialIdea getLeft() {
        return left.get(r.nextInt(left.size()));
    }

    private PartialIdea getRight() {
        return right.get(r.nextInt(right.size()));
    }

    private int getColor() {
        return colors.get(r.nextInt(colors.size()));
    }
}
