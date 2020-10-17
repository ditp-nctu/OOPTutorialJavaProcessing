package art.cctcc.c1635;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class Rect extends MyObject {

    public Rect(float x, float y, float size, int color) {
        super(x, y, size, color);
    }

    @Override
    public void paint(MySketch canvas) {
        canvas.fill(color);
        canvas.stroke(darkerColor());
        canvas.rect(canvas.width * x - size / 2,
                canvas.height * y - size / 2,
                size, size);
    }
}
