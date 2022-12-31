package firework;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.List;
import java.util.Random;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author RAVEN
 */
public class Firework {

    private final double x;
    private final double y;
    private final float angle;
    private final float size = 3f;
    private final float distance;
    private final double cos;
    private final double sin;
    private float animate;
    private Panel panel;
    private Color color;

    public Firework(Panel panel, double x, double y, Color color) {
        this.panel = panel;
        this.x = x;
        this.y = y;
        this.color = color;
        Random ran = new Random();
        angle = ran.nextInt(30) + 255;
        cos = Math.cos(Math.toRadians(angle));
        sin = Math.sin(Math.toRadians(angle));
        distance = panel.getHeight() * 0.8f;
    }

    public void start(List list) {
        Animator animator = new Animator(3000, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                animate = fraction;
                panel.repaint();
            }

            @Override
            public void end() {
                double lx = x + distance * cos;
                double ly = y + distance * sin;
                Point point = new Point((int) lx, (int) ly);
                panel.addEffect(point);
                panel.addEffect(point);
                panel.addEffect(point);
                list.remove(Firework.this);
            }
        });
        animator.setResolution(0);
        animator.setDeceleration(.3f);
        animator.start();
    }

    public void render(Graphics2D g2) {
        double lx = x + (cos * (distance * animate));
        double ly = y + (sin * (distance * animate));
        float remove = 0.7f;
        float alpha = 1f;
        if (animate >= remove) {
            float f = (animate - remove) / (1f - remove);
            alpha = 1f - f;
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }
        Path2D p = new Path2D.Double();
        p.moveTo(lx + Math.cos(Math.toRadians(angle + 90)) * size / 2, ly + Math.sin(Math.toRadians(angle + 90)) * size / 2);
        double s = distance * 0.8f * animate;
        double xx = lx + Math.cos(Math.toRadians(angle + 180)) * s;
        double yy = ly + Math.sin(Math.toRadians(angle + 180)) * s;
        p.lineTo(xx, yy);
        p.lineTo(lx + Math.cos(Math.toRadians(angle - 90)) * size / 2, ly + Math.sin(Math.toRadians(angle - 90)) * size / 2);
        g2.setPaint(new GradientPaint((int) x, (int) y, alphaColor(color, 0f), (int) lx, (int) ly, alphaColor(color, 0.2f)));
        g2.fill(p);
        g2.setColor(color);
        g2.setComposite(AlphaComposite.SrcOver);
        g2.fill(new Ellipse2D.Double(lx - size / 2, ly - size / 2, size, size));
        g2.setComposite(AlphaComposite.SrcOver);
    }

    private Color alphaColor(Color color, float alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255));
    }

}
