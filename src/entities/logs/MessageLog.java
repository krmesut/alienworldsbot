package entities.logs;

import controllers.MessageCenter;
import interfaces.LogMessage;

public class MessageLog implements LogMessage {
	private String message;
	private LogMessageTypes type;

	public MessageLog(String message, LogMessageTypes type) {
		super();
		this.message = message;
		this.type = type;
	}

	@Override
	public LogMessageTypes getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

	@Override
	public void printMessage() {
		// TODO Auto-generated method stub
		System.out.println(message);
	}

	@Override
	public void broadcastToMessageCenter() {
		// TODO Auto-generated method stub
		if (type.equals(LogMessageTypes.ARCHIVE)) {
			MessageCenter.appendMessageToSideLog(message);
		} else {
			MessageCenter.appendMessageToCenterLog(message);
		}
	}

}
