//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//        import controller.GameController;
//        import direction.Direction;
//        import org.junit.jupiter.api.Test;
//
//        import java.awt.*;
//        import java.awt.event.KeyEvent;
//        import java.util.ArrayList;
//
//public class GameControllerTest {
//    @Test
//    public void checkTailCollision() {
//        GameController ctrl = new GameController(10, 200, 200);
//        ArrayList<Point> points = new ArrayList<Point>();
//        Point head = new Point(1, 1);
//        boolean collided = ctrl.checkTailCollision(points, head);
//        assertEquals(false, collided, "It shouldn't collide if not points are passed");
//        points.add(head);
//        collided = ctrl.checkTailCollision(points, head);
//        assertEquals(false, collided, "It shouldn't collide if just head is passed");
//        points.add(new Point(1, 1));
//        collided = ctrl.checkTailCollision(points, head);
//        assertEquals(true, collided, "It should collide if two points have same coordinates");
//    }
//    @Test
//    public void getKeyDirection() {
//        GameController ctrl = new GameController(10, 200, 200);
//
//        assertEquals(
//                Direction.LEFT,
//                ctrl.getKeyDirection(KeyEvent.VK_A),
//                "Should allow to navigate left with A key"
//        );
//        assertEquals(
//                Direction.LEFT,
//                ctrl.getKeyDirection(KeyEvent.VK_LEFT),
//                "Should allow to navigate left with left arrow"
//        );
//        assertEquals(
//                Direction.RIGHT,
//                ctrl.getKeyDirection(KeyEvent.VK_D),
//                "Should allow to navigate left with D key"
//        );
//        assertEquals(
//                Direction.RIGHT,
//                ctrl.getKeyDirection(KeyEvent.VK_RIGHT),
//                "Should allow to navigate left with right arrow"
//        );
//        assertEquals(
//                Direction.UP,
//                ctrl.getKeyDirection(KeyEvent.VK_W),
//                "Should allow to navigate left with W key"
//        );
//        assertEquals(
//                Direction.UP,
//                ctrl.getKeyDirection(KeyEvent.VK_UP),
//                "Should allow to navigate left with up arrow"
//        );
//        assertEquals(
//                Direction.DOWN,
//                ctrl.getKeyDirection(KeyEvent.VK_S),
//                "Should allow to navigate left with S key"
//        );
//        assertEquals(
//                Direction.DOWN,
//                ctrl.getKeyDirection(KeyEvent.VK_DOWN),
//                "Should allow to navigate left with down arrow"
//        );
//    }
//    @Test
//    public void updatePosition() {
//        GameController ctrl = new GameController(10, 20, 20);
//        ctrl.setHead(new Point(0, 0));
//        ArrayList<Point> parts = new ArrayList<Point>();
//        parts.add(new Point(0, 0));
//        parts.add(new Point(0, 0));
//        ctrl.updatePosition();
//        assertEquals(ctrl.isGameOverOver(), true, "Game should end if a wall is hit");
//    }
//}