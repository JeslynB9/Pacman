// Powered Pacman Decorator for enhanced abilities
package pacman.model.entity.dynamic.player.decorator;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.state.FrightenedMode;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.level.Level;

import java.util.Timer;
import java.util.TimerTask;

public class PoweredPacmanDecorator extends PacmanDecorator {

    public PoweredPacmanDecorator(Controllable pacman) {
        super(pacman);
    }

    // Adds functionality to collide with ghosts in frightened mode
    public void collideWithGhost(Ghost ghost, Level level) {
        if (ghost.getGhostMode() instanceof FrightenedMode) {
            int pointsAwarded = 200 * (int) Math.pow(2, level.getGhostsEatenInFrightenedMode());
            level.addPoints(pointsAwarded);
            level.incrementGhostsEatenInFrightenedMode();

            // Make ghost invisible immediately
            ghost.setLayer(Layer.INVISIBLE);

            // Delay the respawn by 1 second (1000 ms)
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ghost.reset(); // Respawn ghost after 1-second delay
                }
            }, 1000);

            System.out.println("Ghost eaten in FrightenedMode. Points awarded: " + pointsAwarded);
        } else {
            System.out.println("Ghost is not in FrightenedMode. Reverting to normal Pacman behavior.");
            pacman.collideWith(level, ghost); // Delegate normal behavior
        }
    }
}
