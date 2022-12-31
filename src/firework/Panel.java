package firework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JComponent;

public class Panel extends JComponent {

    private final List<Effect> effects = new ArrayList<>();
    private final List<Firework> fireworks = new ArrayList<>();
    private final Sound sound = new Sound();

    private Color randomColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor;
    }

    public Panel() {
        setOpaque(true);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addFirework(e.getPoint());
            }

        };
        addMouseListener(mouseAdapter);
    }

    public void addFirework(Point point) {
        Firework firework = new Firework(this, point.getX(), getHeight(), randomColor());
        fireworks.add(firework);
        firework.start(fireworks);
        sound.soundFirework();
    }

    public void addEffect(Point point) {
        sound.soundEffect();
        Effect effect = new Effect(point.getX(), point.getY(), 10, randomColor());
        effects.add(effect);
        effect.start(effects, Panel.this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0));
        g2.fill(new Rectangle(0, 0, getWidth(), getHeight()));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < effects.size(); i++) {
            Effect effect = effects.get(i);
            if (effect != null) {
                effect.render(g2);
            }
        }
        for (int i = 0; i < fireworks.size(); i++) {
            Firework firework = fireworks.get(i);
            if (firework != null) {
                firework.render(g2);
            }
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
