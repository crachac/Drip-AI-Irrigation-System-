package drip.irrigation.system.ai;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import jssc.SerialPort;
import static jssc.SerialPort.MASK_RXCHAR;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import jssc.SerialPortList;

public class MainController implements Initializable {

    /*Variables and lists in main page*/
    private static final DecimalFormat df2 = new DecimalFormat(".##");

    SerialPort arduinoPort = null;
    String st;

    ObservableList<String> portList;

    ObservableList<String> zenList = FXCollections
            .observableArrayList("Олма", "Гилос", "Узум", "Нок", "Анор", "Бодринг", "Памидор");

    @FXML
    private Pane twoPane;

    @FXML
    private Pane onePane;

    @FXML
    private Pane threePane;

    @FXML
    private JFXButton one;

    @FXML
    private JFXButton tree;

    @FXML
    private JFXButton two;
    @FXML
    private HBox navigatorHorBox;
    @FXML
    private Pane navigatorPane;
    @FXML
    private Pane controlPane;
    @FXML
    private JFXToggleButton aiToggleButton;
    @FXML
    private JFXToggleButton wateringToggleButton;
    @FXML
    private Label wateringStatusLabel;
    @FXML
    private JFXComboBox<String> zenTypeComboBox;
    @FXML
    private JFXComboBox<String> portComboBox;
    @FXML
    private JFXButton assignButton;
    @FXML
    private Label zenHumidity;
    @FXML
    private JFXSlider waterLevel;
    @FXML
    private Label totalStatus;
    @FXML
    private ProgressBar progressBarHumidity;
    @FXML
    private ProgressIndicator progressBarIndicatorHumidity;
    @FXML
    private LineChart<?, ?> humidityChart;
    @FXML
    private LineChart<?, ?> tempChart;
    @FXML
    private LineChart<?, ?> lightChart;
    @FXML
    private CategoryAxis humCA;
    @FXML
    private Text dnlab;
    @FXML
    private NumberAxis humNA;
    @FXML
    private Text dripLabel;
    @FXML
    private Text foydaLabel;

    @FXML
    void oneHandle(ActionEvent event) {
        navigatorPane.setVisible(true);
        onePane.setVisible(true);
        twoPane.setVisible(false);
        threePane.setVisible(false);

    }

    @FXML
    void twoHandle(ActionEvent event) {
        navigatorPane.setVisible(true);
        onePane.setVisible(false);
        twoPane.setVisible(true);
        threePane.setVisible(false);
    }

    @FXML
    void threeHandle(ActionEvent event) {
        navigatorPane.setVisible(true);
        onePane.setVisible(false);
        twoPane.setVisible(false);
        threePane.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        zenTypeComboBox.setValue("Олма");
        zenTypeComboBox.setItems(zenList);
        /*String[] array = {"onePane","twoPane", "threePane"};
        Random rand = new Random();
        int n = rand.nextInt(3);
        FadeTransition transition = new FadeTransition(new Duration(1500), String[n]);
        transition.setFromValue(1);
        transition.setToValue(0);
        transition.play();*/

        controlPane.setVisible(false);
        navigatorPane.setVisible(true);
        onePane.setVisible(true);
        twoPane.setVisible(false);
        threePane.setVisible(false);

        dnlab.setText("Кулай интегратсияланган дастурий таьминот\nСенсорлар ёрдамида автоматик бошкарув\nКомпютер \n\n");
        foydaLabel.setText("\n\n\n\n");
        dripLabel.setText("Сув сарфи:\n Пахтачиликда 30-65% \nБог, узум ва сабзавотчиликда 32-54%  \nХосилдорлик 25-40% ортади\nМехнат ва моддир ресурслар сарфи 2 баробаргача камаяди\n");
        // Listen for Slider value changes
    }
/// toglle button to controlPane 

    @FXML
    private void aIToggleHandle(ActionEvent event) {

        if (aiToggleButton.isSelected()) {
            controlPane.setVisible(true);

            // ********In there i make progress bar in different color when value changes it automatically 
            // ******** 0.1-0.3 qizil; 0.4 -0.5 - sariq;  0.6-0.7- yashil;  0.8-1 ko'k; 
            /* double val = (float) 0.6;
            if (val <= 2) {
            } else if (val <= 0.4) {

            } else if (val <= 0.5) {

            } else if (val <= 0.8) {

            } else if (val <= 1) {

            }

           progressBarHumidity.setProgress(val);
            progressBarIndicatorHumidity.setProgress(val - 0.01);*/
            detectPort();
            portComboBox.setItems(portList);

            portComboBox.valueProperty()
                    .addListener(new ChangeListener<String>() {

                        @Override
                        public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                            System.out.println(newValue);
                            disconnectArduino();
                            connectArduino(newValue);

                            wateringToggleButton.setSelected(false);
                        }

                    });

        } else {
            controlPane.setVisible(false);
        }

    }

    private void detectPort() {

        portList = FXCollections.observableArrayList();

        String[] serialPortNames = SerialPortList.getPortNames();
        for (String name : serialPortNames) {
            System.out.println(name);
            portList.add(name);
        }
    }

    private void toggleButtonHandle() {
        try {
            if (wateringToggleButton.isSelected()) {
                if (arduinoPort != null) {
                    arduinoPort.writeByte((byte) 97);
                    System.out.println("Sensors ON");
                    //int pbv = Integer.parseInt(st);
                    //progressBar.setProgress();
                } else {
                    System.out.println("Port not connected!");
                }
            } else if (arduinoPort != null) {
                arduinoPort.writeByte((byte) 98);
                System.out.println("Sensors OFF");
            } else {
                System.out.println("Port not connected!");
            }
        } catch (SerialPortException ex) {
            Logger.getLogger(MainController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    public boolean connectArduino(String port) {

        System.out.println(" Port connected");

        boolean success = false;
        SerialPort serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if (serialPortEvent.isRXCHAR()) {
                    try {
                        String st = serialPort.readString(serialPortEvent.getEventValue());

                        String[] stlist = st.split(",");
                        for (int j = 0; j < stlist.length; j += 2) {

                            String n2 = stlist[j];
                            System.out.print(n2 + "\t");

                            // Double n2f = Double.parseDoubl    e(n2);
                            // Double n2fn = (double) 1024 / n2f;
                            //System.out.println(n2fn);
                            //   Float np2 = df02.format((float)1023 / Float.valueOf(n2));
                            //  
                        }

                        for (int i = 1; i < stlist.length; i += 2) {
                            String n1 = stlist[i];
                            System.out.print(n1);
                            System.out.println("");
                            //  Float np2 = Float.valueOf(n1);
                            //   System.out.println(np2); 
                        }

                        //    DecimalFormat df02 = new DecimalFormat("0.00");
                        //  df02.setMaximumFractionDigits(2);
                    } catch (SerialPortException ex) {
                        Logger.getLogger(MainController.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }

                }
            });

            arduinoPort = serialPort;
            success = true;
        } catch (SerialPortException ex) {
            Logger.getLogger(MainController.class.getName())
                    .log(Level.SEVERE, null, ex);
            System.out.println("SerialPortException: " + ex.toString());
        }

        return success;
    }

    public void disconnectArduino() {

        System.out.println("disconnectArduino()");
        if (arduinoPort != null) {
            try {
                arduinoPort.removeEventListener();

                if (arduinoPort.isOpened()) {
                    arduinoPort.closePort();
                }

                arduinoPort = null;
            } catch (SerialPortException ex) {
                Logger.getLogger(MainController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void wateringToggleHandle(ActionEvent event) {
        toggleButtonHandle();
        if (wateringToggleButton.isSelected()) {
            wateringStatusLabel.setText("ON");

            waterLevel.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable,
                        Number oldValue, Number newValue) {

                    totalStatus.setText("Сув хажми метр куб/соатда: " + String.valueOf(df2.format(newValue)));

                }
            });

            waterLevel.setDisable(false);

        } else if (!wateringToggleButton.isSelected()) {

            wateringStatusLabel.setText("OFF");
            waterLevel.setDisable(true);
            totalStatus.setText("Сув хажми метр куб/соатда: " + 0);
        }
    }

    // O'simlik turini tanlsh va uning turiga qarab suv quyish
    @FXML
    private void assignButtonHandle(ActionEvent event) {

        switch (zenTypeComboBox.getValue()) {
            case "Олма":
                zenHumidity.setText("Олма усимлиги илдизидаги намлик микдори");
                break;
            case "Гилос":
                zenHumidity.setText("Гилос усимлиги илдизидаги намлик микдори");
                break;
            case "Узум":
                zenHumidity.setText("Узум усимлиги илдизидаги намлик микдори");
                break;
            case "Нок":
                zenHumidity.setText("Нок усимлиги илдизидаги намлик микдори");
                break;
            case "Анор":
                zenHumidity.setText("Анор усимлиги илдизидаги намлик микдори");
                break;
            case "Бодринг":
                zenHumidity.setText("Бодринг усимлиги илдизидаги намлик микдори");
                break;
            case "Памидор":
                zenHumidity.setText("Памидор усимлиги илдизидаги намлик микдори");
                break;
            default:
                break;
        }

    }

}
