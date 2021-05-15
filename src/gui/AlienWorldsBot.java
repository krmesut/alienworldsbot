package gui;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JPanel;

import drivers.RunningDriver;
import entities.GeneralSettings;
import entities.PlayerProfile;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.border.TitledBorder;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import controllers.AccountsCenter;
import controllers.DriverCenter;
import controllers.MiningCenter;

public class AlienWorldsBot {

	private JFrame frame;
	private JTextField txtHostField;
	private JTextField txtUsername;
	private JTextField txtPassword;
	private JTextField txtProfilePath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AlienWorldsBot window = new AlienWorldsBot();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AlienWorldsBot() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setTitle("KC33");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
//					if (JOptionPane.showConfirmDialog(frame, "Bạn có chắc là muốn đóng ứng dụng không?", "Đóng?",
//							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, utils.getImageIconFromResourceFile(
//									GeneralSettings.defaultPopupIcon128)) == JOptionPane.YES_OPTION) {
					if (JOptionPane.showConfirmDialog(frame, "Bạn có chắc là muốn đóng ứng dụng không?", "Đóng?",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						DriverCenter.terminateAllDrivers();
						System.exit(0);
					}
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		createTopPanel();
		createBottomPanel();
		createLeftPanel();
		createRightPanel();
		createCenterPanel();
	}

	private void createTopPanel() {
		JPanel panelTop = new JPanel();
		frame.getContentPane().add(panelTop, BorderLayout.NORTH);
		panelTop.setLayout(new GridLayout(1, 3, 0, 0));
		{
			JPanel panelHost = new JPanel();
			panelTop.add(panelHost);
			panelHost.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JPanel panelHostLabel = new JPanel();
				panelHost.add(panelHostLabel);
				panelHostLabel.setLayout(new GridLayout(0, 1, 0, 0));
				{
					JLabel lblHost = new JLabel("Host address: ");
					panelHostLabel.add(lblHost);
				}
			}
			{
				JPanel panelHostField = new JPanel();
				panelHost.add(panelHostField);
				panelHostField.setLayout(new GridLayout(0, 1, 0, 0));
				{
					txtHostField = new JTextField();
					txtHostField.setText("https://play.alienworlds.io");
					panelHostField.add(txtHostField);
					txtHostField.setColumns(10);
				}
			}
		}
		JPanel panelStartButton = new JPanel();
		panelTop.add(panelStartButton);
		panelStartButton.setLayout(new GridLayout(0, 1, 0, 0));

		JButton btnStart = new JButton("Start");
		btnStart.setToolTipText("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startBot();
			}
		});
		panelStartButton.add(btnStart);
	}

	private void createBottomPanel() {
		JPanel panelBottom = new JPanel();
		frame.getContentPane().add(panelBottom, BorderLayout.SOUTH);
		panelBottom.setLayout(new GridLayout(1, 0, 0, 0));
	}

	private void createLeftPanel() {
		JPanel panelLeft = new JPanel();
		frame.getContentPane().add(panelLeft, BorderLayout.WEST);
		panelLeft.setLayout(new GridLayout(2, 1, 0, 0));
		{
			JPanel panelAccounts = new JPanel();
			panelAccounts
					.setBorder(new TitledBorder(null, "Accounts", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelLeft.add(panelAccounts);
			panelAccounts.setLayout(new GridLayout(1, 1, 0, 0));
			{
				JPanel panelAccountDetails = new JPanel();
				panelAccounts.add(panelAccountDetails);
				panelAccountDetails.setLayout(new GridLayout(3, 1, 0, 0));
				{
					JPanel panelUsername = new JPanel();
					panelAccountDetails.add(panelUsername);
					panelUsername.setLayout(new GridLayout(0, 2, 0, 0));
					{
						JPanel panelUsernameTitle = new JPanel();
						panelUsername.add(panelUsernameTitle);
						panelUsernameTitle.setLayout(new GridLayout(0, 1, 0, 0));
						{
							JLabel lblUsername = new JLabel("Username: ");
							panelUsernameTitle.add(lblUsername);
						}
					}
					{
						JPanel panelUsernameField = new JPanel();
						panelUsername.add(panelUsernameField);
						panelUsernameField.setLayout(new GridLayout(0, 1, 0, 0));
						{
							txtUsername = new JTextField();
//							txtUsername.setText("vevig90439@animex98.com");
							txtUsername.setText("vietlh82@gmail.com");
							panelUsernameField.add(txtUsername);
							txtUsername.setColumns(10);
						}
					}
				}
				{
					JPanel panelPassword = new JPanel();
					panelAccountDetails.add(panelPassword);
					panelPassword.setLayout(new GridLayout(0, 2, 0, 0));
					{
						JPanel panelPasswordTitle = new JPanel();
						panelPassword.add(panelPasswordTitle);
						panelPasswordTitle.setLayout(new GridLayout(0, 1, 0, 0));
						{
							JLabel lblPassword = new JLabel("Password: ");
							panelPasswordTitle.add(lblPassword);
						}
					}
					{
						JPanel panelPasswordField = new JPanel();
						panelPassword.add(panelPasswordField);
						panelPasswordField.setLayout(new GridLayout(0, 1, 0, 0));
						{
							txtPassword = new JTextField();
							txtPassword.setText("Qq!@#456");
							panelPasswordField.add(txtPassword);
							txtPassword.setColumns(10);
						}
					}
				}
				{
					JPanel panelProfilePath = new JPanel();
					panelAccountDetails.add(panelProfilePath);
					panelProfilePath.setLayout(new GridLayout(0, 2, 0, 0));
					{
						JPanel panelProfilePathTitle = new JPanel();
						panelProfilePath.add(panelProfilePathTitle);
						panelProfilePathTitle.setLayout(new GridLayout(0, 1, 0, 0));
						{
							JLabel lblProfilePathTitle = new JLabel("Profile Path:");
							panelProfilePathTitle.add(lblProfilePathTitle);
						}
					}
					{
						JPanel panelProfilePathField = new JPanel();
						panelProfilePath.add(panelProfilePathField);
						panelProfilePathField.setLayout(new GridLayout(0, 1, 0, 0));
						{
							txtProfilePath = new JTextField();
							txtProfilePath.setText("/Users/vietlh/Library/Application Support/Google/Chrome/Profile 4");
							panelProfilePathField.add(txtProfilePath);
							txtProfilePath.setColumns(10);
						}
					}
				}
			}
		}
	}

	private void createRightPanel() {
		JPanel panelRight = new JPanel();
		frame.getContentPane().add(panelRight, BorderLayout.EAST);
		panelRight.setLayout(new GridLayout(1, 0, 0, 0));
	}

	private void createCenterPanel() {
		JPanel panelCenter = new JPanel();
		frame.getContentPane().add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new GridLayout(1, 0, 0, 0));
	}

	private void startBot() {
		// update accounts before starting
		updateAccount();
		for (String email : AccountsCenter.getAllAccounts().keySet()) {
			PlayerProfile profile = AccountsCenter.getAccount(email);
			WebDriver driver = initDriver(profile);
			// open the game
			driver.navigate().to(txtHostField.getText().trim().toLowerCase());
			MiningCenter mining = new MiningCenter(driver, profile);
			Thread mineThread = new Thread(mining);
			mineThread.start();
		}
	}

	private void updateAccount() {
		PlayerProfile profile = new PlayerProfile(txtUsername.getText().trim().toLowerCase(),
				txtPassword.getText().trim(), txtProfilePath.getText().trim());
		AccountsCenter.addAccount(profile);
	}

	private WebDriver initDriver(PlayerProfile profile) {
		WebDriver driver = null;
		RunningDriver runningDriver = new RunningDriver();
		try {
			driver = runningDriver.chromeDriver(profile.getProfilePath());
			resizeBrowser(driver, 800, 600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driver;
	}

	private void resizeBrowser(WebDriver driver, int width, int height) {
		Dimension d = new Dimension(width, height);
		// Resize current window to the set dimension
		driver.manage().window().setSize(d);
	}
}
