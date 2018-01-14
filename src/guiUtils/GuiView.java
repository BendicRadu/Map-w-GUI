package guiUtils;

import exceptions.CtrlException;
import exceptions.StackException;

public interface GuiView {
	public void update() throws CtrlException, StackException; 
}
