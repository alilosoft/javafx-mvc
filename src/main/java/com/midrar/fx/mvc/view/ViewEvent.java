package com.midrar.fx.mvc.view;

import javafx.event.Event;
import javafx.event.EventType;

public class ViewEvent extends Event{

    public static final EventType<ViewEvent> SHOW = new EventType<>(Event.ANY,"VIEW_SHOW");
    public static final EventType<ViewEvent> HIDE = new EventType<>(Event.ANY,"VIEW_HIDE");
    public static final EventType<ViewEvent> HIDE_REQUEST = new EventType<>(Event.ANY,"VIEW_HIDE_REQUEST");

    public ViewEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

}
