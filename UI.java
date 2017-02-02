import javax.swing.*;
import java.io.*;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Dimension;
/**
 * Write a description of class UI here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class UI extends JFrame
{
    private JLabel[] labels; 
    private JButton[] buttons;
    private JButton run;
    private JPanel panel;
    private Cleaner cleaner;

    public UI()
    {
        Boolean dataLoaded = false;
        try{
            FileInputStream fis = new FileInputStream("data.dat");
            ObjectInputStream ois= new ObjectInputStream(fis);

            cleaner= (Cleaner)ois.readObject();
            dataLoaded = true;
        }catch (Exception o){cleaner = new Cleaner();}
        panel = new JPanel();

        labels = new JLabel[5];
        buttons = new JButton[5];
        run = new JButton("Clean");
        run.setVisible(false);
        run.setPreferredSize(new Dimension(200, 60));
        run.addActionListener(new ButtonPressed());
        for (int i=0; i< labels.length; i++)
        {
            if (!dataLoaded)
                labels[i] = new JLabel("Not selected");
            else if (i == 0)
                labels[i] = new JLabel(cleaner.getDownloadFolder().getPath());
            else
                labels[i] = new JLabel(cleaner.getTargetList()[i-1].getPath());
        }
        for (int i=0; i< buttons.length; i++)
        {
            if (i==0)
                buttons[0] = new JButton("Download folder");
            else if (i==1)
                buttons[1] = new JButton("Picture folder");
            else if (i==2)
                buttons[2] = new JButton("Document folder");
            else if (i==3)
                buttons[3] = new JButton("Music folder");
            else
                buttons[4] = new JButton("Misc folder");
                
            buttons[i].setPreferredSize(new Dimension(200, 20));

            buttons[i].addActionListener(new ButtonPressed());  
        }
        
        if (dataLoaded)
            run.setVisible(true);

    }

    private void init() //initialize beginning parts to the User Interface
    {
        panel.setLayout(new FlowLayout());
        for (int i=0; i<labels.length; i++)
        {
            if (i == 1)
            {
                JLabel targetLabel = new JLabel("-----Target Directories-----");
                targetLabel.setForeground(Color.red);
                panel.add(targetLabel);
            }
            panel.add(buttons[i]);
            panel.add(labels[i]);
        }

        panel.add(run);

        this.add(panel);
        this.setTitle("Download cleaner");
        this.setSize(270, 400);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void startProgram()
    {
        init();
    }
    
    private void endScreen()
    {
        this.remove(panel);
        JPanel end = new JPanel();
        end.add(new JLabel("Finished"));
        this.add(end);
        
        this.invalidate();
        this.validate();
    }
    
    

    private class ButtonPressed implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (e.getSource().equals(buttons[0]))
            {
                File f = null;
                int val = fc.showOpenDialog(fc);

                if (val == JFileChooser.APPROVE_OPTION)
                {
                    f = fc.getSelectedFile();
                    cleaner.setDownloadFolder(f);
                    labels[0].setText(cleaner.getDownloadFolder().toString());

                }
                else
                    System.out.println("Something got fucked");
            }
            else if (e.getSource().equals(run))
            {
                cleaner.clean();
                
                endScreen();
            }
            else
            {
                int val = fc.showOpenDialog(fc);

                if (val == JFileChooser.APPROVE_OPTION)
                {
                    File f = fc.getSelectedFile();
                    if (e.getSource().equals(buttons[1]))
                    {
                        cleaner.addFileToTargetList(f, 0);
                        labels[1].setText(cleaner.getTargetList()[0].toString());
                    }
                    else if (e.getSource().equals(buttons[2]))
                    {
                        cleaner.addFileToTargetList(f, 1);
                        labels[2].setText(cleaner.getTargetList()[1].toString());
                    }
                    else if (e.getSource().equals(buttons[3]))
                    {
                        cleaner.addFileToTargetList(f, 2);
                        labels[3].setText(cleaner.getTargetList()[2].toString());
                    }
                    else if (e.getSource().equals(buttons[4]))
                    {
                        cleaner.addFileToTargetList(f, 3);
                        labels[4].setText(cleaner.getTargetList()[3].toString());
                    }
                    else
                        System.out.println("OH NO ThATS NOT GOOD");
                }
            }

            if (cleaner.targetListComplete())
            {
                try{
                    FileOutputStream out = new FileOutputStream("data.dat");
                    ObjectOutputStream obOut = new ObjectOutputStream(out);

                    obOut.writeObject(cleaner);
                }catch(Exception a){System.out.println("This didnt work");}

                run.setVisible(true);
            }

        }
    }
}
