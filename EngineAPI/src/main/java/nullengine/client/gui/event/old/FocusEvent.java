package nullengine.client.gui.event.old;

import nullengine.client.gui.Node;

@Deprecated
public class FocusEvent extends ComponentEvent {
    protected FocusEvent(Node node) {
        super(node);
    }

    public static class FocusGainEvent extends FocusEvent {
        public FocusGainEvent(Node node) {
            super(node);
        }
    }

    public static class FocusLostEvent extends FocusEvent {
        public FocusLostEvent(Node node) {
            super(node);
        }
    }
}