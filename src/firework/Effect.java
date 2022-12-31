package firework;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.List;
import java.util.Random;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author Raven
 */
public class Effect {

    private double x;
    private double y;
    private float animate;
    private EffectOption[] effects;
    private Color color = new Color(245, 40, 117);

    public Effect(double x, double y, int totalEffect, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        generateEffect(totalEffect);
    }

    private void generateEffect(int totalEffect) {
        effects = new EffectOption[totalEffect];
        int anglePer = 360 / totalEffect;
        Random ran = new Random();
        for (int i = 0; i < totalEffect; i++) {
            float angle = ran.nextInt(anglePer) + 1 + (anglePer * i);
            effects[i] = new EffectOption(angle);
        }
    }

    public void render(Graphics2D g2) {
        Composite com = g2.getComposite();
        for (int i = 0; i < effects.length; i++) {
            EffectOption option = effects[i];
            double lx = x + option.getCos() * option.getDistance() * animate;
            double ly = y + option.getSin() * option.getDistance() * animate;
            float size = option.getSize();
            float remove = 0.7f;
            if (animate >= remove) {
                float f = (animate - remove) / (1f - remove);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - f));
            }
            Path2D p = new Path2D.Double();
            p.moveTo(lx + Math.cos(Math.toRadians(option.getAngle() + 90)) * size / 2, ly + Math.sin(Math.toRadians(option.getAngle() + 90)) * size / 2);
            double s = option.getDistance() * 0.8f * animate;
            double xx = lx + Math.cos(Math.toRadians(option.getAngle() + 180)) * s;
            double yy = ly + Math.sin(Math.toRadians(option.getAngle() + 180)) * s;
            p.lineTo(xx, yy);
            p.lineTo(lx + Math.cos(Math.toRadians(option.getAngle() - 90)) * size / 2, ly + Math.sin(Math.toRadians(option.getAngle() - 90)) * size / 2);
            Shape shape = new Ellipse2D.Double(lx - size / 2, ly - size / 2, size, size);
            g2.setPaint(new GradientPaint((int) lx, (int) ly, alphaColor(color, 0.5f), (int) xx, (int) yy, alphaColor(color, 0f)));
            g2.fill(p);
            g2.setColor(color);
            g2.fill(shape);
        }
        g2.setComposite(com);
    }

    private Color alphaColor(Color color, float alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255));
    }

    public void start(List list, Component com) {
        Animator animator = new Animator(3000, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                animate = fraction;
                com.repaint();
            }

            @Override
            public void end() {
                list.remove(Effect.this);
            }
        });
        animator.setResolution(5);
        animator.start();
    }

    private class EffectOption {

        public double getCos() {
            return cos;
        }

        public void setCos(double cos) {
            this.cos = cos;
        }

        public double getSin() {
            return sin;
        }

        public void setSin(double sin) {
            this.sin = sin;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }

        public float getSize() {
            return size;
        }

        public void setSize(float size) {
            this.size = size;
        }

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

        public EffectOption(float angle) {
            this.angle = angle;
            random();
            cos = Math.cos(Math.toRadians(angle));
            sin = Math.sin(Math.toRadians(angle));
        }

        private float angle;
        private float size;
        private float distance;
        private double cos;
        private double sin;
        private final float DISTANCES[] = {20, 10, 30, 5, 15, 50, 75, 80, 100, 150, 180, 300};

        private void random() {
            Random ran = new Random();
            size = ran.nextInt(3) + 1;
            distance = DISTANCES[ran.nextInt(DISTANCES.length)];
        }
    }
}
