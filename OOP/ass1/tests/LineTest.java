import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    private final Line line = new Line(new Point(1, 2), new Point(3, 4));

    @Test
    @DisplayName("Line.length")
    void length() {
        Line line = new Line(new Point(1, 2), new Point(4, 6));
        assertEquals(5.0, line.length());
    }

    @Test
    @DisplayName("Line.middle")
    void middle() {
        assertTrue(new Point(2, 3).equals(this.line.middle()));
        Line other = new Line(new Point(1, 2), new Point(1, 2));
        assertTrue(new Point(1, 2).equals(other.middle()));
    }

    @Test
    @DisplayName("Line.start")
    void start() {
        assertTrue(new Point(1, 2).equals(this.line.start()));
    }

    @Test
    @DisplayName("Line.end")
    void end() {
        assertTrue(new Point(3, 4).equals(this.line.end()));
    }

    @Test
    @DisplayName("Line.isIntersecting with itself")
    void isIntersectingWithItself() {
        // Test intersection with itself
        Line other1 = new Line(new Point(1, 2), new Point(3, 4));
        assertTrue(this.line.isIntersecting(this.line));
        assertTrue(this.line.isIntersecting(other1));
        assertTrue(other1.isIntersecting(this.line));
    }

    @Test
    @DisplayName("Line.isIntersecting collinear, end-points")
    void isIntersectingCollinearEndPoints() {
        // Test collinear intersections at one of end-points
        Line other2 = new Line(new Point(1, 2), new Point(0, 1));
        Line other3 = new Line(new Point(5, 6), new Point(3, 4));
        assertTrue(this.line.isIntersecting(other2));
        assertTrue(other2.isIntersecting(this.line));
        assertTrue(this.line.isIntersecting(other3));
        assertTrue(other3.isIntersecting(this.line));
    }

    @Test
    @DisplayName("Line.isIntersecting collinear, no intersection")
    void isIntersectingCollinearNoIntersection() {
        // Test collinear no intersection
        Line other4 = new Line(new Point(-3, -2), new Point(-1, 0));
        Line other5 = new Line(new Point(6, 7), new Point(8, 9));
        assertFalse(this.line.isIntersecting(other4));
        assertFalse(other4.isIntersecting(this.line));
        assertFalse(this.line.isIntersecting(other5));
        assertFalse(other5.isIntersecting(this.line));
    }

    @Test
    @DisplayName("Line.isIntersecting collinear, fully included")
    void isIntersectingCollinearFullyIncluded() {
        // Test full containment
        Line other6 = new Line(new Point(-1, 0), new Point(5, 6));
        Line other7 = new Line(new Point(1.5, 2.5), new Point(2.5, 3.5));
        assertTrue(this.line.isIntersecting(other6));
        assertTrue(other6.isIntersecting(this.line));
        assertTrue(this.line.isIntersecting(other7));
        assertTrue(other7.isIntersecting(this.line));
    }

    @Test
    @DisplayName("Line.isIntersecting non-collinear, end-points")
    void isIntersectingCollinearNonCollinearEndPoints() {
        // Test non-collinear intersection at one of end-points
        Line other8 = new Line(new Point(-4, 8), new Point(1, 2));
        Line other9 = new Line(new Point(1, 2), new Point(-4, 8));
        Line other10 = new Line(new Point(-4, 8), new Point(3, 4));
        Line other11 = new Line(new Point(3, 4), new Point(-4, 8));
        Line other12 = new Line(new Point(-4, 8), new Point(6, -4));
        assertTrue(this.line.isIntersecting(other8));
        assertTrue(other8.isIntersecting(this.line));
        assertTrue(this.line.isIntersecting(other9));
        assertTrue(other9.isIntersecting(this.line));
        assertTrue(this.line.isIntersecting(other10));
        assertTrue(other10.isIntersecting(this.line));
        assertTrue(this.line.isIntersecting(other11));
        assertTrue(other11.isIntersecting(this.line));
        assertTrue(this.line.isIntersecting(other12));
        assertTrue(other12.isIntersecting(this.line));
    }

    @Test
    @DisplayName("Line.isIntersecting non-collinear, middle")
    void isIntersectingCollinearNonCollinearMiddle() {
        // Test non-collinear intersection somewhere in the middle
        Line other13 = new Line(new Point(-4, 8), new Point(6, 0));
        assertTrue(this.line.isIntersecting(other13));
        assertTrue(other13.isIntersecting(this.line));
    }

    @Test
    @DisplayName("Line.isIntersecting two lines")
    void testIsIntersecting() {
        Line other1 = new Line(new Point(1, 2), new Point(4, 6));
        Line other2 = new Line(new Point(1, 2), new Point(0, 1));
        Line other3 = new Line(new Point(5, 6), new Point(4, 6));
        assertTrue(this.line.isIntersecting(other1, other2));
        assertFalse(other2.isIntersecting(this.line, other3));
    }

    @Test
    @DisplayName("Line.intersectionWith with itself")
    void intersectionWithWithItself() {
        // Test intersection with itself
        Line other1 = new Line(new Point(1, 2), new Point(3, 4));
        assertNull(this.line.intersectionWith(this.line));
        assertNull(this.line.intersectionWith(other1));
        assertNull(other1.intersectionWith(this.line));
    }

    @Test
    @DisplayName("Line.intersectionWith collinear, one of end-points")
    void intersectionWithCollinearEndPoints() {
        // Test collinear intersections at one of end-points
        Line other2 = new Line(new Point(1, 2), new Point(0, 1));
        Line other3 = new Line(new Point(5, 6), new Point(3, 4));
        assertTrue(new Point(1, 2).equals(this.line.intersectionWith(other2)));
        assertTrue(new Point(1, 2).equals(other2.intersectionWith(this.line)));
        assertTrue(new Point(3, 4).equals(this.line.intersectionWith(other3)));
        assertTrue(new Point(3, 4).equals(other3.intersectionWith(this.line)));
    }

    @Test
    @DisplayName("Line.intersectionWith collinear, no intersection")
    void intersectionWithCollinearNoIntersection() {
        // Test collinear no intersection
        Line other4 = new Line(new Point(-3, -2), new Point(-1, 0));
        Line other5 = new Line(new Point(6, 7), new Point(8, 9));
        assertNull(this.line.intersectionWith(other4));
        assertNull(other4.intersectionWith(this.line));
        assertNull(this.line.intersectionWith(other5));
        assertNull(other5.intersectionWith(this.line));
    }

    @Test
    @DisplayName("Line.intersectionWith collinear, fully contained")
    void intersectionWithCollinearFullyContained() {
        // Test full containment
        Line other6 = new Line(new Point(-1, 0), new Point(5, 6));
        Line other7 = new Line(new Point(1.5, 2.5), new Point(2.5, 3.5));
        assertNull(this.line.intersectionWith(other6));
        assertNull(other6.intersectionWith(this.line));
        assertNull(this.line.intersectionWith(other7));
        assertNull(other7.intersectionWith(this.line));
    }

    @Test
    @DisplayName("Line.intersectionWith non-collinear, end-points")
    void intersectionWithNonCollinearEndPoints() {
        // Test non-collinear intersection at one of end-points
        Line other8 = new Line(new Point(-4, 8), new Point(1, 2));
        Line other9 = new Line(new Point(1, 2), new Point(-4, 8));
        Line other10 = new Line(new Point(-4, 8), new Point(3, 4));
        Line other11 = new Line(new Point(3, 4), new Point(-4, 8));
        Line other12 = new Line(new Point(-4, 8), new Point(6, -4));
        assertTrue(new Point(1, 2).equals(this.line.intersectionWith(other8)));
        assertTrue(new Point(1, 2).equals(other8.intersectionWith(this.line)));
        assertTrue(new Point(1, 2).equals(this.line.intersectionWith(other9)));
        assertTrue(new Point(1, 2).equals(other9.intersectionWith(this.line)));
        assertTrue(new Point(3, 4).equals(this.line.intersectionWith(other10)));
        assertTrue(new Point(3, 4).equals(other10.intersectionWith(this.line)));
        assertTrue(new Point(3, 4).equals(this.line.intersectionWith(other11)));
        assertTrue(new Point(3, 4).equals(other11.intersectionWith(this.line)));
        assertTrue(new Point(1, 2).equals(this.line.intersectionWith(other12)));
        assertTrue(new Point(1, 2).equals(other12.intersectionWith(this.line)));

        // Test non-collinear intersection somewhere in the middle
        Line other13 = new Line(new Point(-4, 8), new Point(6, 0));
        assertTrue(new Point(2.1111111, 3.1111111).equals(this.line.intersectionWith(other13)));
        assertTrue(new Point(2.1111111, 3.1111111).equals(other13.intersectionWith(this.line)));
    }

    @Test
    @DisplayName("Line.intersectionWith non-collinear, middle")
    void intersectionWithNonCollinearMiddle() {
        // Test non-collinear intersection somewhere in the middle
        Line other13 = new Line(new Point(-4, 8), new Point(6, 0));
        assertTrue(new Point(2.1111111, 3.1111111).equals(this.line.intersectionWith(other13)));
        assertTrue(new Point(2.1111111, 3.1111111).equals(other13.intersectionWith(this.line)));
    }

    @Test
    @DisplayName("Line.intersectionWith non-collinear, no intersection")
    void intersectionWithNonCollinearNoIntersection() {
        // Test non-collinear no intersection
        Line other14 = new Line(new Point(0, 3), new Point(1, 2.5));
        Line other15 = new Line(new Point(-2, 2), new Point(0, 1));
        assertNull(this.line.intersectionWith(other14));
        assertNull(other14.intersectionWith(this.line));
        assertNull(this.line.intersectionWith(other15));
        assertNull(other15.intersectionWith(this.line));
    }

    @Test
    @DisplayName("Line.equals (true)")
    void testEqualsTrue() {
        Line same1 = new Line(new Point(1, 2), new Point(3, 4));
        Line same2 = new Line(new Point(3, 4), new Point(1, 2));
        assertTrue(this.line.equals(same1));
        assertTrue(same1.equals(this.line));
        assertTrue(this.line.equals(same2));
        assertTrue(same2.equals(this.line));
    }

    @Test
    @DisplayName("Line.equals (false)")
    void testEqualsFalse() {
        Line other1 = new Line(new Point(3, 6), new Point(1, 2));
        assertFalse(this.line.equals(other1));
        assertFalse(other1.equals(this.line));
    }
}
