import org.junit.platform.console.ConsoleLauncher;

public class MainTest {
    public static void main(String[] args) {
        System.out.println(
                """
                Please do not treat these tests as any kind of guarantee or source of absolute truth.
                I tried my best to make the tests correct and cover as much as possible, but I'm a human being, I can make mistakes :)
                You take full responsibility for success in assignments and for conveying this disclaimer to anybody who receives these tests from you.
                """
        );
        ConsoleLauncher.main(new String[]{
                "execute",
                "--class-path", "bin-tests",
                "--select-method=PointTest#distance",
                "--select-method=PointTest#testEqualsTrue",
                "--select-method=PointTest#testEqualsFalse",
                "--select-method=PointTest#getX",
                "--select-method=PointTest#getY",
                "--select-class=LineTest",
        });
    }
}
