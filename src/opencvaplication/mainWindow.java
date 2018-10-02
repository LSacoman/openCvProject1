package opencvaplication;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import static org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_highgui;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.namedWindow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import org.bytedeco.javacpp.opencv_videoio.VideoCapture;

public class mainWindow extends javax.swing.JFrame {

    ArrayList<Changes> changes = new ArrayList<>();
    ArrayList<Changes> changesAux = new ArrayList<>();
    ArrayList dadosTabela = new ArrayList();
    Mat frameCamera = new Mat();
    Mat novoFrame = new Mat();

    public mainWindow() {
        initComponents();

        Thread webcam = new Thread() {
            @Override
            public void run() {

                int tecla;
                VideoCapture camera = new VideoCapture(0);
                namedWindow("Captura", 1);

                do {
                    if(jToggleButton1.isSelected()){
                        camera.read(frameCamera);
                    }else{
                        frameCamera = imread("C:/bocha.jpg");
                    }
                    
                    
                    for (int i = 0; i < changesAux.size(); i++) {
                        switch (changesAux.get(i).getType()) {
                            case "color":
                                switch (changesAux.get(i).getQuantity1()){
                                    case 0:break;
                                    case 1:cvtColor(frameCamera, frameCamera, CV_BGR2RGB);break;
                                    case 2:cvtColor(frameCamera, frameCamera, CV_BGR2HSV);break;
                                    case 3:cvtColor(frameCamera, frameCamera, CV_BGR2XYZ);break;
                                    case 4:cvtColor(frameCamera, frameCamera, CV_BGR2HLS);break;
                                    case 5:cvtColor(frameCamera, frameCamera, CV_BGR2Lab);break;
                                    case 6:cvtColor(frameCamera, frameCamera, CV_BGR2Luv);break;
                                    case 7:cvtColor(frameCamera, frameCamera, CV_BGR2YCrCb);break;
                                }
                                break;
                            case "mediana":
                                medianBlur(frameCamera, frameCamera, 3 + changesAux.get(i).getQuantity1() * 2);
                                break;
                            case "gaussiano":
                                GaussianBlur(frameCamera, frameCamera, new Size(3 + changesAux.get(i).getQuantity1() * 2, 3 + changesAux.get(i).getQuantity1() * 2), 5, 5, 0);
                                break;
                            case "canny":
                                Canny(frameCamera, novoFrame, changesAux.get(i).getQuantity1(), changesAux.get(i).getQuantity2(), 3, true);
                                frameCamera = new Mat(novoFrame);
                                break;
                            case "threshold":
                                cvtColor(frameCamera, frameCamera, CV_BGR2GRAY);
                                threshold(frameCamera, frameCamera, changesAux.get(i).getQuantity2(), 255, changesAux.get(i).getQuantity1());
                                break;
                            case "inrange":
                                inRange(frameCamera,
                                        new Mat(1, 1, CV_32SC4, new Scalar(changesAux.get(i).getQuantity1(), changesAux.get(i).getQuantity3(), changesAux.get(i).getQuantity5(), 0)),
                                        new Mat(1, 1, CV_32SC4, new Scalar(changesAux.get(i).getQuantity2(), changesAux.get(i).getQuantity4(), changesAux.get(i).getQuantity6(), 0)),
                                        frameCamera);
                                break;
                            case "erode":
                                Mat element = getStructuringElement(MORPH_RECT, new Size(2 * 5 + 1, 2 * 5 + 1));
                                erode(frameCamera, frameCamera, element);
                                break;
                            case "dilate":
                                Mat element1 = getStructuringElement(MORPH_RECT, new Size(2 * 5 + 1, 2 * 5 + 1));
                                dilate(frameCamera, frameCamera, element1);
                                break;
                        }
                    }                    

                    imshow("Captura", frameCamera);
                    opencv_highgui.MouseCallback nomeFuncao = new opencv_highgui.MouseCallback() {
                        @Override
                        public void call(int event, int x, int y, int flags, Pointer param) {
                            UByteRawIndexer sI;
                            sI = frameCamera.createIndexer();

                            Posicao.setText("Pos: " + x + ", " + y);
                            Cor.setText("RGB: " + sI.get(y, x, 2) + ", " + sI.get(y, x, 1) + ", " + sI.get(y, x, 0));

                            if (event == opencv_highgui.CV_EVENT_LBUTTONDOWN) {
                                float[] hsbColor = java.awt.Color.RGBtoHSB(sI.get(y, x, 2), sI.get(y, x, 1), sI.get(y, x, 0), null);
                                Cor.setForeground(java.awt.Color.getHSBColor(hsbColor[0], hsbColor[1], hsbColor[2]));
                            }
                        }
                    };
                    opencv_highgui.setMouseCallback("Captura", nomeFuncao, null);
                    tecla = waitKey(5);
                } while (tecla != 27);

            }
        };
        webcam.start();
    }

    private void attTabela() {
        String[] header = new String[]{"ID", "Tipo"};
        changesAux = new ArrayList<>(changes);
        dadosTabela.clear();
        for (int i = 0; i < changesAux.size(); i++) {
            dadosTabela.add(new String[]{
                i + "",
                changesAux.get(i).getType()
            });
        }
        TabelaModelo modelo = new TabelaModelo(dadosTabela, header);
        jTable1.setModel(modelo);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        sliderType = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        sliderValue = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        MedianBlur = new javax.swing.JSlider();
        jLabel6 = new javax.swing.JLabel();
        GaussianBlur = new javax.swing.JSlider();
        CannyLower = new javax.swing.JSlider();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        CannyUpper = new javax.swing.JSlider();
        jLabel9 = new javax.swing.JLabel();
        Posicao = new javax.swing.JLabel();
        Cor = new javax.swing.JLabel();
        min1 = new javax.swing.JSlider();
        max1 = new javax.swing.JSlider();
        min2 = new javax.swing.JSlider();
        max2 = new javax.swing.JSlider();
        min3 = new javax.swing.JSlider();
        max3 = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        addErode = new javax.swing.JButton();
        addDilate = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        addMediana = new javax.swing.JButton();
        addGaussiano = new javax.swing.JButton();
        addCanny = new javax.swing.JButton();
        addThreshold = new javax.swing.JButton();
        addInRange = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ORIGINAL", "RGB", "HSV", "XYZ", "HLS", "YCrCb", "Lab", "Luv" }));

        jLabel1.setText("Formato De Cor");

        sliderType.setMaximum(4);
        sliderType.setValue(0);

        jLabel4.setText("Threshold Type");

        sliderValue.setMaximum(255);
        sliderValue.setValue(0);

        jLabel5.setText("Threshold Value");

        MedianBlur.setValue(0);

        jLabel6.setText("Mediana");

        GaussianBlur.setMaximum(50);
        GaussianBlur.setValue(0);

        CannyLower.setMaximum(255);
        CannyLower.setValue(1);

        jLabel7.setText("Gaussiano");

        jLabel8.setText("Canny Lower");
        jLabel8.setToolTipText("");

        CannyUpper.setMaximum(255);
        CannyUpper.setValue(250);

        jLabel9.setText("Canny Upper");

        Cor.setBackground(new java.awt.Color(0, 255, 0));
        Cor.setForeground(new java.awt.Color(255, 0, 0));

        min1.setMaximum(255);
        min1.setValue(0);

        max1.setMaximum(255);
        max1.setValue(255);

        min2.setMaximum(255);
        min2.setValue(0);

        max2.setMaximum(255);
        max2.setValue(255);

        min3.setMaximum(255);
        min3.setValue(0);

        max3.setMaximum(255);
        max3.setValue(255);

        jLabel2.setText("Canal 1 Minimo");

        jLabel3.setText("Canal 1 Maximo");

        jLabel10.setText("Canal 2 Minimo");

        jLabel11.setText("Canal 2 Maximo");

        jLabel12.setText("Canal 3 Minimo");

        jLabel13.setText("Canal 3 Maximo");

        addErode.setText("Add Erode");
        addErode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addErodeActionPerformed(evt);
            }
        });

        addDilate.setText("Add Dilate");
        addDilate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDilateActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("Canny");
        jLabel14.setToolTipText("");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("Threshold");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("InRange");

        addMediana.setText("Add Mediana");
        addMediana.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMedianaActionPerformed(evt);
            }
        });

        addGaussiano.setText("Add Gaussiano");
        addGaussiano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGaussianoActionPerformed(evt);
            }
        });

        addCanny.setText("Add Canny");
        addCanny.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCannyActionPerformed(evt);
            }
        });

        addThreshold.setText("Add Threshold");
        addThreshold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addThresholdActionPerformed(evt);
            }
        });

        addInRange.setText("Add InRange");
        addInRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addInRangeActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Remover");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Add Color");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jToggleButton1.setText("Video / Image");

        jLabel17.setText("Caminho Imagem \"C:/bocha.jpg\"");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jToggleButton1)
                                .addGap(58, 58, 58)
                                .addComponent(jLabel17))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(MedianBlur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(addMediana))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel14)
                                .addComponent(jLabel7))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(GaussianBlur, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(addGaussiano))
                        .addComponent(addInRange)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(CannyLower, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(sliderType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(sliderValue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(min1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(max1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(min2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(max2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(min3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(max3, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(addErode, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(addDilate, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(addThreshold)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel15)
                                .addComponent(jLabel9))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(CannyUpper, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(addCanny)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton1)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(Cor, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(Posicao, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jToggleButton1)
                            .addComponent(jLabel17))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jButton2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MedianBlur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(addMediana))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(GaussianBlur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7))
                                .addGap(8, 8, 8)
                                .addComponent(jLabel14))
                            .addComponent(addGaussiano))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CannyLower, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CannyUpper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel15))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(addCanny)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sliderType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sliderValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel16))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addThreshold)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(min1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(max1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(min2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(max2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(min3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(max3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addInRange)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addDilate)
                            .addComponent(addErode, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(41, 41, 41))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jButton1)
                        .addGap(52, 52, 52)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Posicao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Cor, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addMedianaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMedianaActionPerformed
        this.changes.add(new Changes("mediana", MedianBlur.getValue()));
        attTabela();
    }//GEN-LAST:event_addMedianaActionPerformed

    private void addGaussianoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addGaussianoActionPerformed
        this.changes.add(new Changes("gaussiano", GaussianBlur.getValue()));
        attTabela();
    }//GEN-LAST:event_addGaussianoActionPerformed

    private void addCannyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCannyActionPerformed
        this.changes.add(new Changes("canny", CannyLower.getValue(), CannyUpper.getValue()));
        attTabela();
    }//GEN-LAST:event_addCannyActionPerformed

    private void addThresholdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addThresholdActionPerformed
        this.changes.add(new Changes("threshold", sliderType.getValue(), sliderValue.getValue()));
        attTabela();
    }//GEN-LAST:event_addThresholdActionPerformed

    private void addInRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addInRangeActionPerformed
        this.changes.add(new Changes("inrange", min1.getValue(), max1.getValue(), min2.getValue(), max2.getValue(), min3.getValue(), max3.getValue()));
        attTabela();
    }//GEN-LAST:event_addInRangeActionPerformed

    private void addErodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addErodeActionPerformed
        this.changes.add(new Changes("erode"));
        attTabela();
    }//GEN-LAST:event_addErodeActionPerformed

    private void addDilateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDilateActionPerformed
        this.changes.add(new Changes("dilate"));
        attTabela();
    }//GEN-LAST:event_addDilateActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jTable1.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Você Não Selecionou Nenhum Item Para Excluir", "Falha Ao Excluir Item", JOptionPane.ERROR_MESSAGE);
        } else {
            int showConfirmDialog = JOptionPane.showConfirmDialog(null, "Deseja Realmente Excluir Este Item?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (showConfirmDialog == JOptionPane.YES_OPTION) {
                String indexAux = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();
                int index = Integer.parseInt(indexAux);
                changes.remove(index);
                attTabela();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.changes.add(new Changes("color", jComboBox1.getSelectedIndex()));
        attTabela();
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new mainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider CannyLower;
    private javax.swing.JSlider CannyUpper;
    private javax.swing.JLabel Cor;
    private javax.swing.JSlider GaussianBlur;
    private javax.swing.JSlider MedianBlur;
    private javax.swing.JLabel Posicao;
    private javax.swing.JButton addCanny;
    private javax.swing.JButton addDilate;
    private javax.swing.JButton addErode;
    private javax.swing.JButton addGaussiano;
    private javax.swing.JButton addInRange;
    private javax.swing.JButton addMediana;
    private javax.swing.JButton addThreshold;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JSlider max1;
    private javax.swing.JSlider max2;
    private javax.swing.JSlider max3;
    private javax.swing.JSlider min1;
    private javax.swing.JSlider min2;
    private javax.swing.JSlider min3;
    private javax.swing.JSlider sliderType;
    private javax.swing.JSlider sliderValue;
    // End of variables declaration//GEN-END:variables
}
