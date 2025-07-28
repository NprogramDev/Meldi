package de.a_sitko.meldi;

import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;

public class MouseOnScreen implements NativeMouseListener, NativeMouseMotionListener {

    private Main main;
    public MouseOnScreen(Main m){
        this.main = m;
    }
    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
        this.main.stopMoving();
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeEvent) {
        main.setMousePositionX(nativeEvent.getX());
        main.setMousePositionY(nativeEvent.getY());
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeEvent) {
        if(!main.isMoving()) return;
        main.getStage().setX(nativeEvent.getX() - main.getMoveXOffset());
        main.getStage().setY(nativeEvent.getY() - main.getMoveYOffset());
    }
}
