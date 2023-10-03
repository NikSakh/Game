package com.game.game.events.types;

import com.game.game.events.Event;

public class MouseReleasedEvent extends MouseButtonEvent {

    public MouseReleasedEvent(int button, int x, int y){
        super(button, x, y, Event.Type.MOUSE_RELEASED);
    }
}
