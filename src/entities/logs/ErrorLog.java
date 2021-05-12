package entities.logs;

import controllers.MessageCenter;
import interfaces.LogMessage;

public class ErrorLog implements LogMessage {
	private Exception e;
	private String extraInfo;

	public ErrorLog(Exception e, String extraInfo) {
		super();
		this.e = e;
		this.extraInfo = extraInfo;
	}

	@Override
	public LogMessageTypes getType() {
		// TODO Auto-generated method stub
		return LogMessageTypes.ERROR;
	}

	@Override
	public void printMessage() {
		// TODO Auto-generated method stub
		System.out.println(extraInfo);
		e.printStackTrace();
	}

	@Override
	public void broadcastToMessageCenter() {
		// TODO Auto-generated method stub
		e.printStackTrace();
		MessageCenter.appendMessageToCenterLog(e.getMessage());
	}

}
