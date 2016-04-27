package net.bradbowie.alain.generator;

import junit.framework.Assert;

import net.bradbowie.alain.model.Idea;
import net.bradbowie.alain.model.PartialIdea;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by bradbowie on 4/23/16.
 */
public class IdeaGeneratorTest {

    private IdeaGenerator generator;

    @Before
    public void init() {
        generator = new IdeaGenerator();
    }

    @Test
    public void generateHelloWorld() {
        generator.setColors(Arrays.asList(0, 1));

        PartialIdea left = new PartialIdea();
        left.setLeft(true);
        left.setRight(false);
        left.setWord("Hello");

        PartialIdea right = new PartialIdea();
        right.setLeft(false);
        right.setRight(true);
        right.setWord("World");

        generator.setPartialIdeas(Arrays.asList(left, right));

        Idea idea = generator.next();
        Assert.assertNotNull(idea);
        Assert.assertNotNull(idea.getLeft());
        Assert.assertSame(left.getWord(), idea.getLeft());
        Assert.assertNotNull(idea.getRight());
        Assert.assertSame(right.getWord(), idea.getRight());
        Assert.assertNotSame(idea.getLeft(), idea.getRight());
        Assert.assertNotSame(idea.getLeftColor(), idea.getRightColor());
    }

    @Test
    public void generateUniqueSample() {
        generator.setColors(Arrays.asList(0, 1));

        PartialIdea p1 = new PartialIdea();
        p1.setLeft(true);
        p1.setRight(true);
        p1.setWord("Hello");

        PartialIdea p2 = new PartialIdea();
        p2.setLeft(true);
        p2.setRight(true);
        p2.setWord("World");

        generator.setPartialIdeas(Arrays.asList(p1, p2));

        for(int i = 0; i < 1000; i++) {
            Idea idea = generator.next();
            Assert.assertNotNull(idea);
            Assert.assertNotNull(idea.getLeft());
            Assert.assertNotNull(idea.getRight());
            Assert.assertNotSame(idea.getLeft(), idea.getRight());
            Assert.assertNotSame(idea.getLeftColor(), idea.getRightColor());
        }
    }
}
