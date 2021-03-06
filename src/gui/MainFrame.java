package gui;

import OptArea.OptArea;
import OptArea.OptAreaIndividual;
import OptArea.OptExperimentsFactory;
import experiments.Experiment;
import experiments.ExperimentEvent;
import ga.GAEvent;
import ga.GAListener;
import ga.GeneticAlgorithm;
import ga.geneticOperators.*;
import ga.selectionMethods.RouletteWheel;
import ga.selectionMethods.SelectionMethod;
import ga.selectionMethods.Tournament;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame implements GAListener {

    private static final long serialVersionUID = 1L;
    private OptArea optArea; //ex knapsack;
    private GeneticAlgorithm<OptAreaIndividual, OptArea> gat;
    private OptExperimentsFactory experimentsFactory;
    private PanelTextArea problemPanel;
    PanelGrid bestIndividualGrid;
    private PanelParameters panelParameters = new PanelParameters();
    private JButton buttonDataSet = new JButton("Data set");
    private JButton buttonRun = new JButton("Run");
    private JButton buttonStop = new JButton("Stop");
    private JButton buttonExperiments = new JButton("Experiments");
    private JButton buttonRunExperiments = new JButton("Run experiments");
    private JTextField textFieldExperimentsStatus = new JTextField("", 10);
    private XYSeries seriesBestIndividual;
    private XYSeries seriesAverage;
    private SwingWorker<Void, Void> worker;
    private JPanel centerPanel;
    private JPanel globalPanel;

    public MainFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void jbInit() throws Exception {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Solving area optimization using genetic algorithms");

        //North Left Panel
        JPanel panelNorthLeft = new JPanel(new BorderLayout());
        panelNorthLeft.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));

        panelNorthLeft.add(panelParameters, java.awt.BorderLayout.WEST);
        JPanel panelButtons = new JPanel();
        panelButtons.add(buttonDataSet);
        buttonDataSet.addActionListener(new ButtonDataSet_actionAdapter(this));
        panelButtons.add(buttonRun);
        buttonRun.setEnabled(false);
        buttonRun.addActionListener(new ButtonRun_actionAdapter(this));
        panelButtons.add(buttonStop);
        buttonStop.setEnabled(false);
        buttonStop.addActionListener(new ButtonStop_actionAdapter(this));
        panelNorthLeft.add(panelButtons, java.awt.BorderLayout.SOUTH);

        //North Right Panel - Chart creation
        seriesBestIndividual = new XYSeries("Best");
        seriesAverage = new XYSeries("Average");

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesBestIndividual);
        dataset.addSeries(seriesAverage);
        JFreeChart chart = ChartFactory.createXYLineChart("Evolution", // Title
                "generation", // x-axis Label
                "fitness", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
                );
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        //North Panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(panelNorthLeft, java.awt.BorderLayout.WEST);
        northPanel.add(chartPanel, java.awt.BorderLayout.CENTER);

        //Center panel       
        problemPanel = new PanelTextArea("Problem data: ", 20, 40);
        bestIndividualGrid = new PanelGrid("Best solution: ", new int[1][1]);
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(problemPanel, java.awt.BorderLayout.WEST);
        //Add grid
        centerPanel.add(bestIndividualGrid, java.awt.BorderLayout.CENTER);

        //South Panel
        JPanel southPanel = new JPanel();
        southPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(""),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));

        southPanel.add(buttonExperiments);
        buttonExperiments.addActionListener(new ButtonExperiments_actionAdapter(this));
        southPanel.add(buttonRunExperiments);
        buttonRunExperiments.setEnabled(false);
        buttonRunExperiments.addActionListener(new ButtonRunExperiments_actionAdapter(this));
        southPanel.add(new JLabel("Status: "));
        southPanel.add(textFieldExperimentsStatus);
        textFieldExperimentsStatus.setEditable(false);

        //Global structure
        globalPanel = new JPanel(new BorderLayout());
        globalPanel.add(northPanel, java.awt.BorderLayout.NORTH);
        globalPanel.add(centerPanel, java.awt.BorderLayout.CENTER);
        globalPanel.add(southPanel, java.awt.BorderLayout.SOUTH);
        this.getContentPane().add(globalPanel);

        pack();
    }

    public void buttonDataSet_actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser(new java.io.File("."));
        int returnVal = fc.showOpenDialog(this);

        try {
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File dataSet = fc.getSelectedFile();
                
                optArea = OptArea.buildOptArea(dataSet);
                //knapsack = Knapsack.buildKnapsack(dataSet);
                //problemPanel.textArea.setText(knapsack.toString());
                problemPanel.textArea.setText(optArea.toString());
                problemPanel.textArea.setCaretPosition(0);
                buttonRun.setEnabled(true);
            }
        } catch (IOException e1) {
            e1.printStackTrace(System.err);
        } catch (java.util.NoSuchElementException e2) {
            JOptionPane.showMessageDialog(this, "File format not valid", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void jButtonRun_actionPerformed(ActionEvent e) {
        try {
            if (optArea == null) {
                JOptionPane.showMessageDialog(this, "You must first choose a problem", "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //reset bestIndividualGrid grid and info. panel
            clearBestIndividualGrid();
            seriesBestIndividual.clear();
            seriesAverage.clear();

            optArea.setProb1s(Double.parseDouble(panelParameters.jTextFieldProb1s.getText()));
            optArea.setFitnessType(panelParameters.jComboBoxFitnessTypes.getSelectedIndex());

            gat = new GeneticAlgorithm<OptAreaIndividual, OptArea>(
                    Integer.parseInt(panelParameters.jTextFieldN.getText()),
                    Integer.parseInt(panelParameters.jTextFieldGenerations.getText()),
                    panelParameters.getSelectionMethod(),
                    panelParameters.getRecombinationMethod(),
                    panelParameters.getMutationMethod(),
                    new Random(Integer.parseInt(panelParameters.jTextFieldSeed.getText())));

            /*System.out.println("Prob of 1s: " + optArea.getProb1s());
            System.out.println("Fitness type: " + optArea.getFitnessType());
            System.out.println(gat);*/

            gat.addGAListener(this);

            manageButtons(false, false, true, false, false);

            worker = new SwingWorker<Void, Void>() {
                public Void doInBackground() {
                    try {
                        gat.run(optArea);

                    } catch (Exception e) {
                        e.printStackTrace(System.err);
                    }
                    return null;
                }

                @Override
                public void done() {
                    manageButtons(true, true, false, true, experimentsFactory != null);
                }
            };

            worker.execute();

        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(this, "Wrong parameters!", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void generationEnded(GAEvent e) {
        GeneticAlgorithm<OptAreaIndividual, OptArea> source = e.getSource();
        seriesBestIndividual.add(source.getGeneration(), source.getBestInRun().getFitness());
        seriesAverage.add(source.getGeneration(), source.getAverageFitness());
        if (worker.isCancelled()) {
            e.setStopped(true);
        }
    }

    public void clearBestIndividualGrid(){
        centerPanel.remove(bestIndividualGrid);
        bestIndividualGrid = new PanelGrid("Best solution: ", new int[1][1]);
        centerPanel.add(bestIndividualGrid, java.awt.BorderLayout.CENTER);
        bestIndividualGrid.repaint();
        bestIndividualGrid.updateUI();
    }

    public void runEnded(GAEvent e) {
        centerPanel.remove(bestIndividualGrid);
        bestIndividualGrid = new PanelGrid("Best solution: ", gat.getBestInRun().getTela());
        bestIndividualGrid.jTextArea.setText(gat.getBestInRun().toString());
        centerPanel.add(bestIndividualGrid, java.awt.BorderLayout.CENTER);
        bestIndividualGrid.repaint();
        bestIndividualGrid.updateUI();
    }

    public void jButtonStop_actionPerformed(ActionEvent e) {
        worker.cancel(true);
    }

    public void buttonExperiments_actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser(new java.io.File("."));
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
       // fc.setAcceptAllFileFilterUsed(true);
        int returnVal = fc.showOpenDialog(this);

        try {
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                experimentsFactory = new OptExperimentsFactory(fc.getSelectedFile());
                manageButtons(true, optArea != null, false, true, true);
                //System.out.println(experimentsFactory.buildExperiment().toString());
            }
        } catch (IOException e1) {
            e1.printStackTrace(System.err);
        } catch (java.util.NoSuchElementException e2) {
            JOptionPane.showMessageDialog(this, "File format not valid", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void buttonRunExperiments_actionPerformed(ActionEvent e) {

        manageButtons(false, false, false, false, false);
        textFieldExperimentsStatus.setText("Running");

        worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                try {
                    while (experimentsFactory.hasMoreExperiments()) {
                        try {

                            Experiment experiment = experimentsFactory.nextExperiment();
                            experiment.run();

                        } catch (IOException e1) {
                            e1.printStackTrace(System.err);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    //JOptionPane.showMessageDialog(null, "Enable to find the dataset especified in the experiments config file!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }

            @Override
            public void done() {
                manageButtons(true, optArea != null, false, true, false);
                textFieldExperimentsStatus.setText("Finished");
            }
        };
        worker.execute();
    }

    public void experimentEnded(ExperimentEvent e) {

    }

    private void manageButtons(
            boolean dataSet,
            boolean run,
            boolean stopRun,
            boolean experiments,
            boolean runExperiments) {

        buttonDataSet.setEnabled(dataSet);
        buttonRun.setEnabled(run);
        buttonStop.setEnabled(stopRun);
        buttonExperiments.setEnabled(experiments);
        buttonRunExperiments.setEnabled(runExperiments);
    }
}

class ButtonDataSet_actionAdapter implements ActionListener {

    private MainFrame adaptee;

    ButtonDataSet_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.buttonDataSet_actionPerformed(e);
    }
}

class ButtonRun_actionAdapter implements ActionListener {

    private MainFrame adaptee;

    ButtonRun_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButtonRun_actionPerformed(e);
    }
}

class ButtonStop_actionAdapter implements ActionListener {

    private MainFrame adaptee;

    ButtonStop_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jButtonStop_actionPerformed(e);
    }
}

class ButtonExperiments_actionAdapter implements ActionListener {

    private MainFrame adaptee;

    ButtonExperiments_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.buttonExperiments_actionPerformed(e);
    }
}

class ButtonRunExperiments_actionAdapter implements ActionListener {

    private MainFrame adaptee;

    ButtonRunExperiments_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.buttonRunExperiments_actionPerformed(e);
    }
}

class PanelTextArea extends JPanel {

    JTextArea textArea;

    public PanelTextArea(String title, int rows, int columns) {
        textArea = new JTextArea(rows, columns);
        setLayout(new BorderLayout());
        add(new JLabel(title), java.awt.BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        add(scrollPane);
    }
}

//New Stuff For Better Material Representation
class PanelGrid extends JPanel{
    public GridLayout gridLayout;
    public JTextArea jTextArea;
    public JPanel gridPanel;

    /*public PanelGrid(String title, int rows, int columns){
        setLayout(new BorderLayout());
        add(new JLabel(title), java.awt.BorderLayout.NORTH);

        gridLayout = new GridLayout(rows, columns, 2, 2);

        JPanel gridPanel = new JPanel(gridLayout);
        //gridPanel.setBackground(Color.BLACK);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JLabel label = new JLabel();
                label.setOpaque(true);
                label.setBackground(createHLSPastelColor());
                //label.setBorder(BorderFactory.createEtchedBorder());
                //label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridPanel.add(label);
            }
        }
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        add(scrollPane);
    }*/

    public PanelGrid(String title, int[][] matrix){
        setLayout(new BorderLayout());
        add(new JLabel(title), java.awt.BorderLayout.NORTH);

        gridLayout = new GridLayout(matrix.length, matrix[0].length, 2, 2);

        gridPanel = new JPanel(gridLayout);
        gridPanel.setMinimumSize(new Dimension(1,1));
        //Get pieces on matrix
        HashMap<Integer,Color> pieces = new HashMap<>();
        pieces.put(0, Color.WHITE);
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                if(!pieces.containsKey((matrix[row][col]))){
                    pieces.put(matrix[row][col], createHLSPastelColor());
                }
            }
        }

        JLabel[][] jLabels = new JLabel[matrix.length][matrix[0].length];

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                    JLabel label = new JLabel("",SwingConstants.CENTER);
                    label.setOpaque(true);
                    gridPanel.add(label);
                    jLabels[row][col]=label;
            }
        }

        for (Map.Entry<Integer, Color> entry : pieces.entrySet()) {
            for (int row = 0; row < matrix.length; row++) {
                for (int col = 0; col < matrix[0].length; col++) {
                    if(matrix[row][col]==entry.getKey()) {
                        jLabels[row][col].setText(Integer.toString(entry.getKey()));
                        jLabels[row][col].setBackground(entry.getValue());
                    }
                }
            }
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        jTextArea = new JTextArea();
        jTextArea.setRows(5);
        jTextArea.setEditable(false);
        add(jTextArea, BorderLayout.SOUTH);
        add(scrollPane);
    }

    public Color createHLSPastelColor(){
        Random random = new Random();
        float hue = random.nextFloat();
        float saturation = (random.nextInt(2000) + 1000) / 10000f;
        float luminance = 0.9f;

        return Color.getHSBColor(hue, saturation, luminance);
    }

    public Color createBrightHLSPastelColor(){
        int R = (int)(Math.random()*256);
        int G = (int)(Math.random()*256);
        int B= (int)(Math.random()*256);

        return  new Color(R, G, B); //bright or dull
    }

    public Color createRainbowBrightHLSPastelColor(){
        Random random = new Random();
        float hue = random.nextFloat();
        float saturation = 0.9f;
        float luminance = 1.0f;

        return Color.getHSBColor(hue, saturation, luminance);
    }
}

class PanelAtributesValue extends JPanel {

    protected String title;
    protected List<JLabel> labels = new ArrayList<JLabel>();
    protected List<JComponent> valueComponents = new ArrayList<JComponent>();

    public PanelAtributesValue() {
    }

    protected void configure() {

        //for(JComponent textField : textFields)
        //textField.setHorizontalAlignment(SwingConstants.RIGHT);

        GridBagLayout gridbag = new GridBagLayout();
        setLayout(gridbag);

        //addLabelTextRows

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHEAST;
        Iterator<JLabel> itLabels = labels.iterator();
        Iterator<JComponent> itTextFields = valueComponents.iterator();

        while (itLabels.hasNext()) {
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE;      //reset to default
            c.weightx = 0.0;                       //reset to default
            add(itLabels.next(), c);

            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            add(itTextFields.next(), c);
        }
    }
}

class PanelParameters extends PanelAtributesValue {

    public static final int TEXT_FIELD_LENGHT = 7;
    public static final String SEED = "1";
    public static final String POPULATION_SIZE = "100";
    public static final String GENERATIONS = "100";
    public static final String TOURNAMENT_SIZE = "2";
    public static final String PROB_RECOMBINATION = "0.8";
    public static final String PROB_MUTATION = "0.01";
    public static final String PROB_1S = "0.05";
    JTextField jTextFieldSeed = new JTextField(SEED, TEXT_FIELD_LENGHT);
    JTextField jTextFieldN = new JTextField(POPULATION_SIZE, TEXT_FIELD_LENGHT);
    JTextField jTextFieldGenerations = new JTextField(GENERATIONS, TEXT_FIELD_LENGHT);
    String[] selectionMethods = {"Tournament", "Roulette wheel"};
    JComboBox jComboBoxSelectionMethods = new JComboBox(selectionMethods);
    JTextField jTextFieldTournamentSize = new JTextField(TOURNAMENT_SIZE, TEXT_FIELD_LENGHT);
    String[] recombinationMethods = {"One cut", "Two cuts", "Uniform"};
    JComboBox jComboBoxRecombinationMethods = new JComboBox(recombinationMethods);
    JTextField jTextFieldProbRecombination = new JTextField(PROB_RECOMBINATION, TEXT_FIELD_LENGHT);
    JTextField jTextFieldProbMutation = new JTextField(PROB_MUTATION, TEXT_FIELD_LENGHT);
    JTextField jTextFieldProb1s = new JTextField(PROB_1S, TEXT_FIELD_LENGHT);
    String[] fitnessTypes = {"simple", "with penalty"};
    JComboBox jComboBoxFitnessTypes = new JComboBox(fitnessTypes);

    public PanelParameters() {
        title = "Genetic algorithm parameters";

        labels.add(new JLabel("Seed: "));
        valueComponents.add(jTextFieldSeed);
        jTextFieldSeed.addKeyListener(new IntegerTextField_KeyAdapter(null));

        labels.add(new JLabel("Population size: "));
        valueComponents.add(jTextFieldN);
        jTextFieldN.addKeyListener(new IntegerTextField_KeyAdapter(null));

        labels.add(new JLabel("# of generations: "));
        valueComponents.add(jTextFieldGenerations);
        jTextFieldGenerations.addKeyListener(new IntegerTextField_KeyAdapter(null));

        labels.add(new JLabel("Selection method: "));
        valueComponents.add(jComboBoxSelectionMethods);
        jComboBoxSelectionMethods.addActionListener(new JComboBoxSelectionMethods_ActionAdapter(this));

        labels.add(new JLabel("Tournament size: "));
        valueComponents.add(jTextFieldTournamentSize);
        jTextFieldTournamentSize.addKeyListener(new IntegerTextField_KeyAdapter(null));

        labels.add(new JLabel("Recombination method: "));
        valueComponents.add(jComboBoxRecombinationMethods);

        labels.add(new JLabel("Recombination prob.: "));
        valueComponents.add(jTextFieldProbRecombination);

        labels.add(new JLabel("Mutation prob.: "));
        valueComponents.add(jTextFieldProbMutation);

        labels.add(new JLabel("Initial proportion of 1s: "));
        valueComponents.add(jTextFieldProb1s);

        labels.add(new JLabel("Fitness type: "));
        valueComponents.add(jComboBoxFitnessTypes);
        jComboBoxFitnessTypes.addActionListener(new JComboBoxFitnessFunction_ActionAdapter(this));

        configure();
    }

    public void actionPerformedSelectionMethods(ActionEvent e) {
        if (jComboBoxFitnessTypes.getSelectedIndex() == 1
                && jComboBoxSelectionMethods.getSelectedIndex() == 1) {
            jComboBoxSelectionMethods.setSelectedIndex(0);
            JOptionPane.showMessageDialog(this, "Not allowed with penalty fitness", "Error!", JOptionPane.ERROR_MESSAGE);
        }
        jTextFieldTournamentSize.setEnabled((jComboBoxSelectionMethods.getSelectedIndex() == 0) ? true : false);
    }

    public SelectionMethod<OptAreaIndividual, OptArea> getSelectionMethod() {
        switch (jComboBoxSelectionMethods.getSelectedIndex()) {
            case 0:
                return new Tournament<OptAreaIndividual, OptArea>(
                        Integer.parseInt(jTextFieldN.getText()),Integer.parseInt(jTextFieldTournamentSize.getText()));
            case 1:
                return new RouletteWheel<OptAreaIndividual, OptArea>(Integer.parseInt(jTextFieldN.getText()));
        }
        return null;
    }

    public Recombination<OptAreaIndividual> getRecombinationMethod() {

        double recombinationProb = Double.parseDouble(jTextFieldProbRecombination.getText());

        switch (jComboBoxRecombinationMethods.getSelectedIndex()) {
            case 0:
                return new RecombinationOneCut<OptAreaIndividual>(recombinationProb);
            case 1:
                return new RecombinationTwoCuts<OptAreaIndividual>(recombinationProb);
            case 2:
                return new RecombinationUniform<OptAreaIndividual>(recombinationProb);
        }
        return null;
    }

    public PosMutation<OptAreaIndividual> getMutationMethod() {
        double mutationProb = Double.parseDouble(jTextFieldProbMutation.getText());
        return new PosMutation<OptAreaIndividual>(mutationProb);
    }

    public void actionPerformedFitnessType(ActionEvent e) {
        if (jComboBoxFitnessTypes.getSelectedIndex() == 1
                && jComboBoxSelectionMethods.getSelectedIndex() == 1) {
            jComboBoxFitnessTypes.setSelectedIndex(0);
            JOptionPane.showMessageDialog(this, "Not allowed with roulette wheel", "Error!", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class JComboBoxSelectionMethods_ActionAdapter implements ActionListener {

    private PanelParameters adaptee;

    JComboBoxSelectionMethods_ActionAdapter(PanelParameters adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.actionPerformedSelectionMethods(e);
    }
}

class JComboBoxFitnessFunction_ActionAdapter implements ActionListener {

    private PanelParameters adaptee;

    JComboBoxFitnessFunction_ActionAdapter(PanelParameters adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.actionPerformedFitnessType(e);
    }
}

class IntegerTextField_KeyAdapter implements KeyListener {

    private MainFrame adaptee;

    IntegerTextField_KeyAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
            e.consume();
        }
    }
}
