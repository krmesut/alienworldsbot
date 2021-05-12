package interfaces;

public interface InputTransferrable {
	public void openGeneralInputDialog(String announcement, Messenger messenger);
	public void openGeneralInputDialog();
	public boolean isUpdated();
}
