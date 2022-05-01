package aligner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

// TODO: restructure!!

import static aligner.MainFileOp.getDistance;
import static aligner.MainFileOp.readFile;

public class Environment implements ActionListener {
    static JFrame frame;
    private final JMenuBar menuBar;
    private final JMenu menu;
    private final JMenuItem open;
    private final JMenuItem save;
    private final JMenuItem close;
    // Distance Panel Variables
    private final JTextArea uploadFile1;
    private final JTextArea uploadFile2;
    private final JButton browseFile1;
    private final JButton browseFile2;
    private final JButton analyseDist;
    String q = "";
    String r = "";


    public Environment(){
        frame = new JFrame("SeqAlign");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation((WindowConstants.EXIT_ON_CLOSE));

        //Set up menu
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        open = new JMenuItem("Open File");
        open.addActionListener(this);
        save = new JMenuItem("Save");
        save.addActionListener(this);
        close = new JMenuItem("Close");
        close.addActionListener(this);

        //Add to frame
        menu.add(open);
        menu.add(save);
        menu.add(close);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);


        //Add panel for distance analysis
        JPanel distance = new JPanel();
        analyseDist = new JButton("Get Sequence Distance");
        analyseDist.addActionListener(this);
        browseFile1 = new JButton("Browse File");
        browseFile1.addActionListener(this);
        browseFile2 = new JButton("Browse File");
        browseFile2.addActionListener(this);
        JLabel labelQuery = new JLabel("Query Sequence:");
        JLabel labelRef = new JLabel("Reference Sequence:");
        uploadFile1 = new JTextArea("Space for outputs", 1, 20);
        uploadFile2 = new JTextArea("Space for outputs", 1, 20);

        // TODO: Text overflow or scrollbar
        uploadFile1.setEditable(false);
        uploadFile2.setEditable(false);

        GroupLayout glDist = new GroupLayout(distance);
        distance.setLayout(glDist);
        glDist.setAutoCreateGaps(true); // automatic gap insertion
        glDist.setAutoCreateContainerGaps(true);

        glDist.setHorizontalGroup(
                glDist.createSequentialGroup()
                        .addGroup(glDist.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(labelQuery, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelRef, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(analyseDist, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(glDist.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(uploadFile1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(uploadFile2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(glDist.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(browseFile1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(browseFile2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )

        );
        glDist.setVerticalGroup(
                glDist.createSequentialGroup()
                        .addGroup(glDist.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelQuery, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(uploadFile1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(browseFile1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(glDist.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelRef, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(uploadFile2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(browseFile2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
                        .addGroup(glDist.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(analyseDist, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )

        );

        // Further Panels
        JPanel alignment = new JPanel();
        JPanel extension = new JPanel();

        //Create tabbed pane
        JTabbedPane tp = new JTabbedPane();
        tp.setBounds(0, 0, 550, 550);
        tp.setBackground(Color.lightGray);
        tp.add("Distance Metrics", distance);
        tp.add("Alignments", alignment);
        tp.add("Extensions", extension);

        frame.add(tp);



    }

    public void actionPerformed(ActionEvent e){
        //Menu [CLOSE]
        if(e.getSource() == close){
            int option = JOptionPane.showConfirmDialog(
                    frame, "Are you sure you want to exit?",
                    "Leaving already?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }

        // Upload Query

        // TODO: fix direct action after button click, only continue if both files have been selected
        if(e.getSource() == browseFile1) {
            //Create a file chooser
            q = uploadData(uploadFile1, e);
        }

        if(e.getSource() == browseFile2){
            r = uploadData(uploadFile2, e);
        }

        if(e.getSource() == analyseDist && !Objects.equals(q, "") && !Objects.equals(r, "")){
            Fasta query = new Fasta();
            Fasta ref = new Fasta();

            query.fastaList = readFile(q);
            ref.fastaList = readFile(r);


            try {
                HashMap<String, Integer> seqDistance= getDistance(query, ref);
                for ( String key : seqDistance.keySet() ) {
                    System.out.println(key);
                    System.out.println(seqDistance.get(key));
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            throw new RuntimeException("Oops, something went wrong here!");
        }

    }

    public static String uploadData(JTextArea uploadPath, ActionEvent e){
        // Upload Query

            //Create a file chooser
        final JFileChooser fc = new JFileChooser();
            //In response to a button click:
        try {
            if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(frame)) {
                File file = fc.getSelectedFile();
                uploadPath.setText(file.getAbsolutePath());
                return file.getAbsolutePath();
            }
        }
        catch (HeadlessException ex) {
            throw new RuntimeException(ex);
        }

        return "error";
    }




}
