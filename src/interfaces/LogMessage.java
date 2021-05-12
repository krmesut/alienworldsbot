package interfaces;

import entities.logs.LogMessageTypes;

public interface LogMessage {
	public LogMessageTypes getType();
	public void printMessage();
	public void broadcastToMessageCenter();
}
