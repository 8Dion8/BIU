import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    private final Point point = new Point(1.0, 5.0);

    @Test
    @DisplayName("Point.distance")
    void distance() {
        Point zero = new Point(1.0, 5.0);
        Point nonZero = new Point(0.0, 5.1);
        assertEquals(0.0, this.point.distance(zero));
        assertEquals(1.004987562112, this.point.distance(nonZero), 1e-12);
    }

    @Test
    @DisplayName("Point.getX")
    void getX() {
        assertEquals(1.0, this.point.getX());
    }

    @Test
    @DisplayName("Point.getY")
    void getY() {
        assertEquals(5.0, this.point.getY());
    }

    @Test
    @DisplayName("Point.equals (true)")
    void testEqualsTrue() {
        Point same1 = new Point(1.0, 4.9999999);
        Point same2 = new Point(1.0, 5.0000001);
        assertTrue(this.point.equals(this.point));
        assertTrue(this.point.equals(same1));
        assertTrue(this.point.equals(same2));
    }

    @Test
    @DisplayName("Point.equals (false)")
    void testEqualsFalse() {
        Point other1 = new Point(1.0, 5.0000002);
        assertFalse(this.point.equals(other1));
    }

    @Test
    @DisplayName("Point.equals")
    void testEqualsPedantic() {
        Point same3 = new Point(1.0, 4.99999989999);
        Point other2 = new Point(1.0, 5.0000001001);
        Point other3 = new Point(1.0, 4.9999998999);
        assertTrue(this.point.equals(same3));
        assertFalse(this.point.equals(other2));
        assertFalse(this.point.equals(other3));
    }
}