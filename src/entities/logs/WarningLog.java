package entities.logs;

import controllers.MessageCenter;
import interfaces.LogMessage;

public class WarningLog implements LogMessage {
	private String warningMessage;

	public WarningLog(String warningMessage) {
		super();
		this.warningMessage = warningMessage;
	}

	@Override
	public LogMessageTypes getType() {
		// TODO Auto-generated method stub
		return LogMessageTypes.WARNING;
	}

	@Override
	public void printMessage() {
		// TODO Auto-generated method stub
		System.out.println(warningMessage);
	}

	@Override
	public void broadcastToMessageCenter() {
		// TODO Auto-generated method stub
		MessageCenter.appendMessageToCenterLog(warningMessage);
	}

}
