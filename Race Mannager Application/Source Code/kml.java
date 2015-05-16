
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.TimeStamp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/*
 * This class build for BlindMatchRace application as external tool for after race information analysis
 * This class output is a KML file that can use on Google Earth 
 */

/**
 *
 * @author idan nicolet , Nehor Golan , Matan Pluda
 */
public class kml extends javax.swing.JFrame {

    String event = null;
    String choice;
    File file;
    private static final String URL_CLIENTS_TABLE = "http://matchrace.net16.net/json-clients.php?table=clientsBuoys&Event=";
    private static final String URL_HISTORY_TABLE = "http://matchrace.net16.net/json-clients.php?table=history&Event=";
    private static final String URL_Event_Check = "http://matchrace.net16.net/json-clients.php?table=eventTest&Event=";
    private final Map<Long, LatLng> sortedLatLngs = new TreeMap<Long, LatLng>();
    private final Map<Long, LatLng> sortedLatLngsOpp = new TreeMap<Long, LatLng>();

    /** Icons **/
    private final Icon FROM = new Icon().withHref("http://maps.google.com/mapfiles/kml/paddle/red-stars.png"); //red balloon with black star
    private final Icon END = new Icon().withHref("http://maps.google.com/mapfiles/kml/paddle/grn-stars.png"); //green balloon with black star
    private final Icon BOUY = new Icon().withHref("http://maps.google.com/mapfiles/kml/shapes/flag.png");//blue flag
    private final Icon COORDINATE1 = new Icon().withHref("http://maps.google.com/mapfiles/kml/paddle/wht-blank.png");//white balloon
    private final Icon COORDINATE2 = new Icon().withHref("http://maps.google.com/mapfiles/kml/paddle/blu-blank.png"); //blue balloon

    final Kml kml = new Kml();
    JFileChooser jFileChooser = new JFileChooser();

    public kml() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        eventTestField = new javax.swing.JTextField();
        TimeStampB = new javax.swing.JButton();
        OnlyPathB = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        eventTestField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventTestFieldActionPerformed(evt);
            }
        });

        TimeStampB.setText("TimeStamp");
        TimeStampB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TimeStampBMousePressed(evt);
            }
        });
        TimeStampB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TimeStampBActionPerformed(evt);
            }
        });

        OnlyPathB.setText("Only Path");
        OnlyPathB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                OnlyPathBMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(OnlyPathB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                .addComponent(TimeStampB)
                .addGap(55, 55, 55))
            .addGroup(layout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(eventTestField, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(eventTestField, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TimeStampB)
                    .addComponent(OnlyPathB))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void eventTestFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventTestFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_eventTestFieldActionPerformed

    private void TimeStampBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TimeStampBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TimeStampBActionPerformed
    //TimeStammp on click event
    private void TimeStampBMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TimeStampBMousePressed
        choice = "TimeStamp";
        startAppliction();
    }//GEN-LAST:event_TimeStampBMousePressed
    //OnlyPath on click event
    private void OnlyPathBMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OnlyPathBMousePressed
        choice = "OnlyPath";
        startAppliction();
    }//GEN-LAST:event_OnlyPathBMousePressed
    //this function navigate to relevant function witin user choices
    private void startAppliction() {
        boolean available = false;
        //init the event from the textFiled
        event = eventTestField.getText();
        //open save file dialog
        jFileChooser.setSelectedFile(new File("Sailor_" + event + "_" + choice + ".kml"));
        jFileChooser.showSaveDialog(this);
        file = jFileChooser.getSelectedFile();

        try {
            available = isAvailable(event);
        } catch (Exception ex) {
        }
        if (available && choice.equalsIgnoreCase("TimeStamp")) {
            Json();
        } else if (available && choice.equalsIgnoreCase("onlyPath")) {
            JsonOnlyPath();
        } else {
            System.out.println("Data not found");
        }
    }

    //this function reads relevant data from server - only path choose
    private void JsonOnlyPath() {

        try {
            String user = "";
            JSONObject jsonHistory = JsonReader.readJsonFromUrl(URL_HISTORY_TABLE + event);
            JSONArray jsonArray = jsonHistory.getJSONArray("Positions");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);

                String lat = jsonObj.getString("lat");
                String lng = jsonObj.getString("lon");
                String name = jsonObj.getString("name");
                long toLong = Long.parseLong(jsonObj.getString("time"));
                if (i == 0) {
                    user = name;
                }
                if (Float.parseFloat(lat) == 0 || Float.parseFloat(lng) == 0) {
                    continue;
                }
                LatLng latLng = new LatLng(Float.parseFloat(lat), Float.parseFloat(lng), name);

                // Adds sailor's data to TreeMap.
                if (user.equalsIgnoreCase(name)) {
                    sortedLatLngs.put(toLong, latLng);
                } else {
                    sortedLatLngsOpp.put(toLong, latLng);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        OnlyPath();

        try {
            kml.marshal(file);
            System.out.println("Finish");
            sortedLatLngs.clear();
            sortedLatLngsOpp.clear();
        } catch (FileNotFoundException ex) {
        }
    }

    //this function reads relevant data from server - TimeStampe choose
    private void Json() {

        long highstTime = 0;
        try {
            String user = "";
            JSONObject jsonHistory = JsonReader.readJsonFromUrl(URL_HISTORY_TABLE + event);
            JSONArray jsonArray = jsonHistory.getJSONArray("Positions");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);

                String lat = jsonObj.getString("lat");
                String lng = jsonObj.getString("lon");
                String name = jsonObj.getString("name");
                String azimuth = jsonObj.getString("azimuth");
                String speed = jsonObj.getString("speed");
                long toLong = Long.parseLong(jsonObj.getString("time"));
                int type = 1;
                int whichUser = 0;
                if (i == 0) {
                    user = name;
                    type = 0;
                }

                whichUser = (user.equalsIgnoreCase(name)) ? 1 : 0;

                if (Float.parseFloat(lat) == 0 || Float.parseFloat(lng) == 0) {
                    continue;
                }
                LatLng latLng = new LatLng(Float.parseFloat(lat), Float.parseFloat(lng), whichUser, type, name, azimuth, speed, toLong);

                // Adds sailor's data to TreeMap.
                if (toLong > highstTime) {
                    highstTime = toLong;
                }
                sortedLatLngs.put(toLong, latLng);
            }

            JSONObject jsonClients = JsonReader.readJsonFromUrl(URL_CLIENTS_TABLE + event);
            jsonArray = jsonClients.getJSONArray("Buoys");
            int numOfBuoys = jsonArray.length();
            for (int i = 0; i < numOfBuoys; i++) {
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);

                String lat = jsonObj.getString("lat");
                String lng = jsonObj.getString("lon");
                // String time = i + "1";
                if (Float.parseFloat(lat) == 0 || Float.parseFloat(lng) == 0) {
                    break;
                }
                LatLng latLng = new LatLng(Float.parseFloat(lat), Float.parseFloat(lng), 2, "Buoys");
                // Adds sailor's last data to TreeMap.
                sortedLatLngs.put(--highstTime, latLng);
            }
            //  return sortedLatLngs;
        } catch (Exception e) {
            e.printStackTrace();
        }

        TimeStamp();
        try {
            kml.marshal(file);
            System.out.println("Finish");
            sortedLatLngs.clear();

        } catch (FileNotFoundException ex) {
        }
    }

    //this function draw the path using kml file - only path choose
    public void OnlyPath() {
        Document document = kml.createAndSetDocument().withName("Path");
        Placemark placemark = document.createAndAddPlacemark();

        Style style = document.createAndAddStyle();
        style.withId("FROM").createAndSetIconStyle().withScale(1.0).withIcon(FROM);
        style.createAndSetLabelStyle();

        Style style1 = document.createAndAddStyle();
        style1.withId("END").createAndSetIconStyle().withScale(1.0).withIcon(END);
        style1.createAndSetLabelStyle();
        //create path for first user
        if (sortedLatLngs != null && sortedLatLngs.size() > 1) {

            Iterator<Map.Entry<Long, LatLng>> iter = sortedLatLngs.entrySet().iterator();
            Map.Entry<Long, LatLng> entry = (Map.Entry<Long, LatLng>) iter.next();

            Style s2 = document.createAndAddStyle().withId(entry.getValue().name);
            s2.createAndSetLineStyle().withColor("50F01414").withWidth(8);

            float Lat = entry.getValue().latitude;
            float Lng = entry.getValue().longitude;

            placemark.withStyleUrl("FROM").withName("FROM").createAndSetPoint().addToCoordinates(Lng, Lat, 0);
            placemark = document.createAndAddPlacemark();
            placemark.withName(entry.getValue().name).withStyleUrl(entry.getValue().name);
            LineString l = placemark.createAndSetLineString();
            l.setTessellate(true);

            while (iter.hasNext()) {
                entry = (Map.Entry<Long, LatLng>) iter.next();

                Lat = entry.getValue().latitude;
                Lng = entry.getValue().longitude;

                if (!iter.hasNext()) {
                    placemark = document.createAndAddPlacemark();
                    placemark.withStyleUrl("END").withName("END").createAndSetPoint().addToCoordinates(Lng, Lat, 0.0);
                    placemark = document.createAndAddPlacemark();
                    break;
                } else {
                    l.addToCoordinates(Lng, Lat, 0.0).withId(entry.getValue().name);
                }
            }
        }
        //create path for second user
        if (sortedLatLngsOpp != null && sortedLatLngsOpp.size() > 1) {

            Iterator<Map.Entry<Long, LatLng>> iter = sortedLatLngsOpp.entrySet().iterator();
            Map.Entry<Long, LatLng> entry = (Map.Entry<Long, LatLng>) iter.next();

            Style s3 = document.createAndAddStyle().withId(entry.getValue().name);
            s3.createAndSetLineStyle().withColor("501400FF").withWidth(8);

            float Lat = entry.getValue().latitude;
            float Lng = entry.getValue().longitude;

            placemark.withStyleUrl("FROM").withName("FROM").createAndSetPoint().addToCoordinates(Lng, Lat, 0);
            placemark = document.createAndAddPlacemark();
            placemark.withName(entry.getValue().name).withStyleUrl(entry.getValue().name);
            LineString l2 = placemark.createAndSetLineString();
            l2.setExtrude(true);
            l2.setTessellate(true);

            while (iter.hasNext()) {
                entry = (Map.Entry<Long, LatLng>) iter.next();

                Lat = entry.getValue().latitude;
                Lng = entry.getValue().longitude;

                if (!iter.hasNext()) {
                    placemark = document.createAndAddPlacemark();
                    placemark.withStyleUrl("END").withName("END").createAndSetPoint().addToCoordinates(Lng, Lat, 0.0);
                    break;
                } else {
                    l2.addToCoordinates(Lng, Lat, 0.0).withId(entry.getValue().name);
                }
            }
        }
    }

    public void TimeStamp() {
        Document document = kml.createAndSetDocument().withName("TimeStamp");
        if (sortedLatLngs != null && sortedLatLngs.size() > 1) {

            Iterator<Map.Entry<Long, LatLng>> iter = sortedLatLngs.entrySet().iterator();
            Map.Entry<Long, LatLng> entry = (Map.Entry<Long, LatLng>) iter.next();

            Style style = document.createAndAddStyle();
            style.withId("FROM").createAndSetIconStyle().withScale(1.0).withIcon(FROM);// set size and icon
            style.createAndSetLabelStyle();
            ; // set chart icon
            Style style1 = document.createAndAddStyle();
            style1.withId("bouy").createAndSetIconStyle().withScale(1.0).withIcon(BOUY); // set size and icon
            style1.createAndSetLabelStyle();

            // set chart icon
            Style style2 = document.createAndAddStyle();
            style2.withId("cor").createAndSetIconStyle().withScale(1.0).withIcon(COORDINATE1); // set size and icon
            style2.createAndSetLabelStyle();

            Style style3 = document.createAndAddStyle();
            style3.withId("cor2").createAndSetIconStyle().withScale(1.0).withIcon(COORDINATE2); // set size and icon
            style3.createAndSetLabelStyle();

            Style style4 = document.createAndAddStyle();
            style4.withId("END").createAndSetIconStyle().withScale(1.0).withIcon(END);
            style4.createAndSetLabelStyle();

            Placemark placemark = document.createAndAddPlacemark();
            long timeForBouy = entry.getKey();
            long starttime = entry.getKey();
            float Lat = entry.getValue().latitude;
            float Lng = entry.getValue().longitude;
            String name;
            String speed;
            String azimuth;
            String time;

            Date date = new Date(starttime);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String dateFormatted = formatter.format(date);
            TimeStamp t = new TimeStamp();
            t.setWhen(dateFormatted);

            placemark.withTimePrimitive(t).withStyleUrl("FROM").withName("FROM").createAndSetPoint().addToCoordinates(Lng, Lat, 1.0);
            placemark.withDescription("latitude: " + Lat + "\n longitude: " + Lng + "\n");

            while (iter.hasNext()) {
                entry = (Map.Entry<Long, LatLng>) iter.next();
                placemark = document.createAndAddPlacemark();
                starttime = entry.getKey();
                Lat = entry.getValue().latitude;
                Lng = entry.getValue().longitude;
                name = entry.getValue().name;
                speed = entry.getValue().speed;
                azimuth = entry.getValue().azimuth;
                time = entry.getValue().time;

                date = new Date(starttime);
                formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                dateFormatted = formatter.format(date);
                t = new TimeStamp();
                t.setWhen(dateFormatted);

                if (!iter.hasNext()) {
                    placemark.withTimePrimitive(t).withStyleUrl("END").withName("END").createAndSetPoint().addToCoordinates(Lng, Lat, 1.0);
                    placemark.withDescription("latitude: " + Lat + "\n longitude: " + Lng);
                    break;
                }

                if (entry.getValue().type == 1) {
                    if (entry.getValue().user == 1) {
                        placemark.withTimePrimitive(t).withStyleUrl("cor").withName(name).createAndSetPoint().addToCoordinates(Lng, Lat, 1.0);
                    } else {
                        placemark.withTimePrimitive(t).withStyleUrl("cor2").withName(name).createAndSetPoint().addToCoordinates(Lng, Lat, 1.0);
                    }
                } else if (entry.getValue().type == 2) {
                    placemark.withTimePrimitive(t).withStyleUrl("bouy").withName(name).createAndSetPoint().addToCoordinates(Lng, Lat, 1.0);
                }
                placemark.withDescription("Time: " + time + "\n latitude: " + Lat + "\n longitude: " + Lng + "\n speed: " + speed + "\n azimuth: " + azimuth);
            }

        }
    }

    //Check if the event number is available
    public static boolean isAvailable(String check) throws JSONException, IOException {
        JSONObject jsonClients = JsonReader.readJsonFromUrl(URL_Event_Check + check);
        JSONArray jsonArray = jsonClients.getJSONArray("ans");
        JSONObject jsonObj = (JSONObject) jsonArray.get(0);
        String f = jsonObj.getString("res");
        return f.equalsIgnoreCase("True");
    }

    /**
     * @param args the command line arguments
     */
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(kml.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(kml.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(kml.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(kml.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new kml().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OnlyPathB;
    private javax.swing.JButton TimeStampB;
    private javax.swing.JTextField eventTestField;
    // End of variables declaration//GEN-END:variables
}
