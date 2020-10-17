package art.cctcc.c1635;


/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class Circle extends MyObject {

    public Circle(float x, float y, float size, int color) {
        super(x, y, size, color);
    }

    @Override
    public void paint(MySketch canvas) {
        canvas.fill(color);
        canvas.stroke(darkerColor());
        canvas.circle(canvas.width * x, canvas.height * y, size);
    }
}
