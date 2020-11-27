import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogInGUI implements Runnable {
    String userNameInput;
    String passwordInput;

    static JLabel usernameLabel;
    static JLabel passwordLabel;

    static JTextField userName_Text;
    static JPasswordField passwordText_Label;
    static JButton submit;
    static JButton create;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new LogInGUI());
    }

    public void run() {
        JFrame launcher = new JFrame("Login");
        JPanel panel = new JPanel(new GridLayout(3,2));
        launcher.setVisible(true);
        launcher.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        launcher.setSize(400,300);

        usernameLabel = new JLabel("Username: ");
        passwordLabel = new JLabel("Password: ");

        userName_Text = new JTextField();
        passwordText_Label = new JPasswordField();
        submit = new JButton("Sign in");
        create = new JButton("Sign up");

        submit.addActionListener(actionListener);
        create.addActionListener(actionListener);

        //Username
        panel.add(usernameLabel);
        panel.add(userName_Text);

        //Password
        panel.add(passwordLabel);
        panel.add(passwordText_Label);

        //User Action
        panel.add(submit);
        panel.add(create);

        launcher.add(panel);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == create) {
                SingUp();
            }
            if (e.getSource() == submit) {
                SignIn();
            }

        }
    };

    public void SignIn() {
        userNameInput = userName_Text.getText();
        passwordInput = passwordText_Label.getText();
        System.out.println(passwordInput);
        System.out.println(userNameInput);
        System.out.println("AAAAAA");
    }

    public void SingUp() {
        System.out.println("PPPPPPPPP");
    }

}
