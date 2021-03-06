package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;

public class IHM implements Runnable {

	private JFrame frame;
	private JTextArea textArea;
	private JLabel lblUsername;
	
	private Socket s;
	private SocketAddress me;
	
	private Thread thread;
	
	private String username;

	// CONSTRUCTOR
	public IHM(JTextArea textArea, Socket s) {
		super();
		this.textArea = textArea;
		this.s = s;
	}
	
	public IHM(String username) {
		super();
		this.username = username;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IHM window = new IHM();
					loginPage login = new loginPage(window.frame, true);
					login.setVisible(true);
					// window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IHM() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		getFrame().setBounds(100, 100, 600, 500);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(12, 49, 300, 250);
		getFrame().getContentPane().add(textArea);
		
		JButton btnJoindre = new JButton("Joindre");
		btnJoindre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// CONNECTION AU SERVER
				s = new Socket();
				me = new InetSocketAddress("127.0.0.1", 5000);
				try {
					s.connect(me);
					System.out.println("connected to the server");
					thread = new Thread(new IHM(textArea, s));
					thread.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnJoindre.setBounds(12, 12, 117, 25);
		getFrame().getContentPane().add(btnJoindre);
		
		JTextField textField = new JTextField();
		textField.setBounds(12, 311, 300, 19);
		getFrame().getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnEnvoyer = new JButton("Envoyer");
		btnEnvoyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// SEND THE MESSAGE
				String msg = textField.getText();
				OutputStream o;
				try {
					o = s.getOutputStream();
					o.write(msg.getBytes());
					textField.setText("");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnEnvoyer.setBounds(12, 342, 117, 25);
		getFrame().getContentPane().add(btnEnvoyer);
		
		JLabel lblEtat = new JLabel("Etat :");
		lblEtat.setBounds(12, 443, 70, 15);
		getFrame().getContentPane().add(lblEtat);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(12, 416, 70, 15);
		getFrame().getContentPane().add(lblNewLabel);
		
		lblUsername = new JLabel(username);
		lblUsername.setBounds(209, 17, 70, 15);
		frame.getContentPane().add(lblUsername);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("in thread");
		InputStream i;
		try {
			i = s.getInputStream();
			while (true) {
				byte[] rep = new byte[200];
				int n = i.read(rep);
				System.out.println("server : " + new String(rep));
				textArea.append(new String(rep) + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// GETTERS AND SETTERS
	public JFrame getFrame() {
		return frame;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public JLabel getlblUsername() {
		return lblUsername;
	}
	
}
