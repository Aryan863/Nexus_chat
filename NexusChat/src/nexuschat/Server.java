
package nexuschat;

import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.net.*;



/**
 *
 * @author AryanK
 */
public class Server implements ActionListener{
    JTextField text;
    JPanel a1;     //text display area
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    static DataOutputStream dout;
    
    public Server() {
        
        f.setLayout(null);            // not using default layout of swing

        JPanel p1 = new JPanel();     //Jpanel is like a container , to group related components
        p1.setBackground(new Color(7,94,84));    //custom green background
        p1.setBounds(0,0,450,70);               //set panel location on top
        p1.setLayout(null);
        f.add(p1);                                // add panel onto frame
        
        //Back icon
        ImageIcon back_icon = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image resize_back_icon = back_icon.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon new_back_icon = new ImageIcon(resize_back_icon);
        JLabel back = new JLabel(new_back_icon);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);
        
        back.addMouseListener(new MouseAdapter()
                {
                    public void mouseClicked(MouseEvent ae){
                        System.exit(0);
                    }
                });
        
        
        //Profile Picture
        ImageIcon profile_icon = new ImageIcon(ClassLoader.getSystemResource("icons/luffy1.png"));
        Image resize_profile_icon = profile_icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
        ImageIcon new_profile_icon = new ImageIcon(resize_profile_icon);
        JLabel profile = new JLabel(new_profile_icon);
        profile.setBounds(40, 10, 50, 50);
        p1.add(profile);
        
        //Video icon
        ImageIcon video_icon = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image resize_video_icon = video_icon.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon new_video_icon = new ImageIcon(resize_video_icon);
        JLabel video = new JLabel(new_video_icon);
        video.setBounds(300,20, 30, 30);
        p1.add(video);
        
        //call icon
        ImageIcon call_icon = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image resize_call_icon = call_icon.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        ImageIcon new_call_icon = new ImageIcon(resize_call_icon);
        JLabel call = new JLabel(new_call_icon);
        call.setBounds(360, 20, 30, 30);
        p1.add(call);
        
        //3 dots icon
        ImageIcon dots_icon = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image resize_dots_icon = dots_icon.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon new_dots_icon = new ImageIcon(resize_dots_icon);
        JLabel dots = new JLabel(new_dots_icon);
        dots.setBounds(420, 20, 10, 25);
        p1.add(dots);
        
        //add name
        //usage of JLabel is used to write on frame
        JLabel name = new JLabel("Luffy");
        name.setBounds(110,15,100,18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN SERIF", Font.BOLD, 18));
        p1.add(name);
        
        //add status
        JLabel status = new JLabel("Active Now");
        status.setBounds(110,35,100,18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN SERIF", Font.BOLD, 14));
        p1.add(status);
        
        
        //Message area
        a1 = new JPanel();
        a1.setBounds(5,75,440,570);
        f.add(a1);
        
        //User text field
        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);
        
        //Send button
        JButton send = new JButton("Send");
        send.setBounds(320,655,123,40);
        send.setBackground(new Color(7, 94,84));
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        send.setForeground(Color.WHITE);
        f.add(send);
        
        send.addActionListener(this);
        
        f.setSize(450,700);           //size of frame
        f.setLocation(200,50);        // starting visible location on screen
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);        //background colour of frame
        f.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            
        
        String out = text.getText();
        JPanel p2 = formatLabel(out);
        
        
        a1.setLayout(new BorderLayout());
        
        JPanel right = new JPanel(new BorderLayout());   //aligns the messages to the right side
        right.add(p2, BorderLayout.LINE_END);            // aligns the message to the right side
        vertical.add(right);                            // aligns new messages vertically, one below other
        vertical.add(Box.createVerticalStrut(15));
        
        a1.add(vertical, BorderLayout.PAGE_START);
        
        dout.writeUTF(out);
        
        text.setText("");
        
        f.repaint();         //used to repaint the frame i.e reload the frame after adding new components
        f.invalidate();
        f.validate();
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public static JPanel formatLabel(String out)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel output = new JLabel("<html><p style=\"width: 150px\">"+ out +"</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102));
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15,15,50));
        panel.add(output);
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        
        panel.add(time);
        return panel;
    }
    
    
    public static void main(String[] args) {
        new Server();
        
        try {
            ServerSocket skt = new ServerSocket(6001);
            while(true)
            {
                Socket s= skt.accept();
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                
                while(true)
                {
                    String msg = din.readUTF();
                    JPanel panel = formatLabel(msg);
                    
                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    vertical.add(left);
                    f.validate();
                      
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
