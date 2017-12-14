package axoloti.dialogs;

import axoloti.IConnection;
import axoloti.TargetController;
import axoloti.TargetModel;
import axoloti.chunks.ChunkData;
import axoloti.chunks.FourCCs;
import axoloti.utils.MidiControllerNames;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.nio.ByteBuffer;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import qcmds.QCmdMemRead;

/**
 *
 * @author jtaelman
 */
public class MidiMonitor extends TJFrame implements ActionListener {

    /**
     * Creates new form Memory
     */
    public MidiMonitor(TargetController controller) {
        super(controller);
        initComponents();
        setTitle("MIDI input monitor");
        jMidiMonitorTable.setFont(Font.getFont(Font.MONOSPACED));
        //jTextAreaMemoryContent.setEditable(false);

        jMidiMonitorTable.setModel(new AbstractTableModel() {
            private String[] columnNames = {"Port", "Channel", "Data (hex)", "Event"};//, "Test Info"};

            @Override
            public int getRowCount() {
                if (msgs == null) {
                    return 0;
                }
                return msgs.length;
                //return 8;
            }

            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public String getColumnName(int column) {
                return columnNames[column];
            }

            @Override
            public Object getValueAt(int row, int column) {
                if (msgs == null) {
                    return null;
                }
                int index = (readIndex - row) & (msgs.length - 1);
                midi_message msg = msgs[index];
                if (msg.ph == 0 && msg.b0 == 0) {
                    return ""; // uninitialized
                }
                switch (column) {
                    case 0: //portnumber
                        return String.format("%2d", msg.getPortNumber() + 1);
                    case 1: // channel
                        if (msg.getCin() == 0xF) {
                            return "";
                        }
                        if ((msg.ph & 0x08) == 0x08) {
                            return String.format("%2d", msg.getChannelNumber() + 1);
                        } else {
                            return "";
                        }
                    case 2: // data
                        switch (msg.getNumDataBytes()) {
                            case 0:
                                return "";
                            case 1:
                                return String.format("%02X", msg.b0);
                            case 2:
                                return String.format("%02X %02X", msg.b0, msg.b1);
                            case 3:
                                return String.format("%02X %02X %02X", msg.b0, msg.b1, msg.b2);
                        }
                        return "???";
                    case 3:
                        return msg.getEventName();
                }
                return "?";
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }

            @Override
            public Class getColumnClass(int column) {
                return String.class;
            }
        });
        jMidiMonitorTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        doLayout();
//        jMidiMonitorTable.getColumnModel().getColumn(0).setPreferredWidth(8);
//        jMidiMonitorTable.getColumnModel().getColumn(1).setPreferredWidth(8);
//        jMidiMonitorTable.getColumnModel().getColumn(2).setPreferredWidth(16);
//        jMidiMonitorTable.getColumnModel().getColumn(3).setPreferredWidth(64);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jUpdatePanel = new javax.swing.JPanel();
        jButtonUpdate = new javax.swing.JButton();
        jScrollPane = new javax.swing.JScrollPane();
        jMidiMonitorTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jUpdatePanel.setPreferredSize(new java.awt.Dimension(20, 20));

        jButtonUpdate.setText("Update");
        jButtonUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jUpdatePanelLayout = new javax.swing.GroupLayout(jUpdatePanel);
        jUpdatePanel.setLayout(jUpdatePanelLayout);
        jUpdatePanelLayout.setHorizontalGroup(
            jUpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jUpdatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jUpdatePanelLayout.setVerticalGroup(
            jUpdatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jUpdatePanelLayout.createSequentialGroup()
                .addGap(0, 11, Short.MAX_VALUE)
                .addComponent(jButtonUpdate))
        );

        jMidiMonitorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane.setViewportView(jMidiMonitorTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jUpdatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
            .addComponent(jScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jUpdatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (TargetModel.CONNECTION.is(evt)) {
            showConnect1(evt.getNewValue() != null);
        }
    }

    class midi_message {

        byte b0;
        byte b1;
        byte b2;
        byte ph;

        void readFromByteBuffer(ByteBuffer bb) {
            ph = bb.get();
            b0 = bb.get();
            b1 = bb.get();
            b2 = bb.get();
        }

        String midiData[][] = {
            {"Note Off", "note", "velocity"},
            {"Note On", "note", "velocity"},
            {"Key Pressure", "note", "pressure"},
            {"Control Change", "number", "value"},
            {"Program Change", "number", null},
            {"Channel Pressure", "pressure", null},
            {"Pitch Bend", "lsb", "msb"}
        };

        String midiRTNames[] = {
            "System Exclusive",
            "MIDI Time Code Quarter Frame",
            "Song Position Pointer",
            "Song Select",
            "Undefined (F4)",
            "Undefined (F5)",
            "Tune Request",
            "End of Exclusive",
            "Timing Clock",
            "Undefined (F9)",
            "Start",
            "Continue",
            "Stop",
            "Undefined (FD)",
            "Active Sensing",
            "Reset"};

        final static int CIN_0_RESERVED = 0;
        final static int CIN_1_RESERVED = 1;
        final static int CIN_2_2BYTE_SYSTEM_COMMON = 2;
        final static int CIN_3_3BYTE_SYSTEM_COMMON = 3;
        final static int CIN_4_SYSEX_START_OR_CONTINUE = 4;
        final static int CIN_5_SYSEX_END_1BYTE = 5;
        final static int CIN_6_SYSEX_END_2BYTE = 6;
        final static int CIN_7_SYSEX_END_3BYTE = 7;
        final static int CIN_8_NOTEOFF = 8;
        final static int CIN_9_NOTEON = 9;
        final static int CIN_A_KEYPRESSURE = 0xA;
        final static int CIN_B_CONTROLCHANGE = 0xB;
        final static int CIN_C_PROGRAMCHANGE = 0xC;
        final static int CIN_D_CHANNELPRESSURE = 0xD;
        final static int CIN_E_PITCHBEND = 0xE;
        final static int CIN_F_SINGLE_BYTE = 0xF;

        int getNumDataBytes() {
            switch (getCin()) {
                case CIN_0_RESERVED:
                    return 3;
                case CIN_1_RESERVED:
                    return 3;
                case CIN_2_2BYTE_SYSTEM_COMMON:
                    return 2;
                case CIN_3_3BYTE_SYSTEM_COMMON:
                    return 3;
                case CIN_4_SYSEX_START_OR_CONTINUE:
                    return 3;
                case CIN_5_SYSEX_END_1BYTE:
                    return 1;
                case CIN_6_SYSEX_END_2BYTE:
                    return 2;
                case CIN_7_SYSEX_END_3BYTE:
                    return 3;
                case CIN_8_NOTEOFF:
                    return 3;
                case CIN_9_NOTEON:
                    return 3;
                case CIN_A_KEYPRESSURE:
                    return 3;
                case CIN_B_CONTROLCHANGE:
                    return 3;
                case CIN_C_PROGRAMCHANGE:
                    return 2;
                case CIN_D_CHANNELPRESSURE:
                    return 2;
                case CIN_E_PITCHBEND:
                    return 3;
                case CIN_F_SINGLE_BYTE:
                    return 1;
            }
            return 0;
        }

        int getPortNumber() {
            return (ph & 0xF0) >> 4;
        }

        int getChannelNumber() {
            return b0 & 0x0F;
        }

        int getCin() {
            return ph & 0x0F;
        }

        String getEventName() {
            switch (getCin()) {
                case CIN_0_RESERVED:
                    return "Reserved";
                case CIN_1_RESERVED:
                    return "Reserved";
                case CIN_2_2BYTE_SYSTEM_COMMON:
                    return midiRTNames[((0xFF & b0) - 0xF0)];
                case CIN_3_3BYTE_SYSTEM_COMMON:
                    return midiRTNames[((0xFF & b0) - 0xF0)];
                case CIN_4_SYSEX_START_OR_CONTINUE:
                    return "SysEx start/cont";
                case CIN_5_SYSEX_END_1BYTE:
                    return "SysEx end";
                case CIN_6_SYSEX_END_2BYTE:
                    return "SysEx end";
                case CIN_7_SYSEX_END_3BYTE:
                    return "SysEx end";
                case CIN_8_NOTEOFF:
                    return String.format("NoteOff note=%3d velo=%3d", b1 & 0xFF, b2 & 0xFF);
                case CIN_9_NOTEON:
                    return String.format("NoteOn  note=%3d velo=%3d", b1 & 0xFF, b2 & 0xFF);
                case CIN_A_KEYPRESSURE:
                    return String.format("KeyPres note=%3d pres=%3d", b1 & 0xFF, b2 & 0xFF);
                case CIN_B_CONTROLCHANGE:
                    return String.format("CtrlChng  cc=%3d val =%3d (%s)", b1 & 0xFF, b2 & 0xFF, MidiControllerNames.GetNameFromCC(b1));
                case CIN_C_PROGRAMCHANGE:
                    return "PgmChng pgm=" + (b1 & 0xFF);
                case CIN_D_CHANNELPRESSURE:
                    return "ChanPres val=" + (b1 & 0xFF);
                case CIN_E_PITCHBEND:
                    return "Bend val=" + (b1 & 0xFF); // FIXME       
                case CIN_F_SINGLE_BYTE:
                    return midiRTNames[((0xFF & b0) - 0xF0)];
            }
            return "?";
        }

        String getFirstMidiDataByteName() {
            switch (getCin()) {
                case 4:
                    return "";
                case 6:
                    return "";
                case 7:
                    return "";
            }
            if (b0 == 0) {
                return "";
            } else if ((b0 & 0xF0) < 0xF0) {
                return midiData[((b0 & 0xF0) >> 4) - 8][1];
            } else {
                return midiRTNames[((0xFF & b0) - 0xF0)];
            }
        }

        String getSecondMidiDataByteName() {
            switch (getCin()) {
                case 4:
                    return "";
                case 6:
                    return "";
                case 7:
                    return "";
            }
            if (b0 == 0) {
                return "";
            } else if ((b0 & 0xF0) < 0xF0) {
                if (midiData[((b0 & 0xF0) >> 4) - 8][2] == null) {
                    return "null";
                } else {
                    if (((b0 & 0xF0) >> 4) == 0x0B) {
                        return MidiControllerNames.GetNameFromCC(b1) + ", " + midiData[((b0 & 0xF0) >> 4) - 8][2];
                    } else {
                        return midiData[((b0 & 0xF0) >> 4) - 8][2];
                    }
                }
            } else {
                return midiRTNames[((0xFF & b0) - 0xF0)];
            }
        }

    }

    midi_message msgs[];
    int readIndex;

    void update() {
        jMidiMonitorTable.setFont(new Font("monospaced", Font.PLAIN, 12));
        int length = 256;
        IConnection conn = getController().getModel().getConnection();
        ChunkData chunk_midibuff = conn.GetFWChunks().GetOne(FourCCs.FW_MIDI_INPUT_BUFFER);
        chunk_midibuff.data.rewind();
        int addr = chunk_midibuff.data.getInt();
        conn.AppendToQueue(new QCmdMemRead(addr, length, new IConnection.MemReadHandler() {
            @Override
            public void Done(ByteBuffer mem) {
                String s = "";
                if (mem != null) {
                    readIndex = mem.getInt();
                    int writeIndex = mem.getInt();
                    midi_message msgs1[] = new midi_message[32];
                    for (int i = 0; i < msgs1.length; i++) {
                        msgs1[i] = new midi_message();
                        msgs1[i].readFromByteBuffer(mem);
                    }
                    msgs = msgs1;
                    ((AbstractTableModel) jMidiMonitorTable.getModel()).fireTableDataChanged();
                } else {
                    msgs = null;
                }
            }
        }));
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        dispose();
    }//GEN-LAST:event_formWindowClosed

    private void jButtonUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUpdateActionPerformed
        update();
    }//GEN-LAST:event_jButtonUpdateActionPerformed

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    void showConnect1(boolean connected) {
        jMidiMonitorTable.setEnabled(connected);
        jButtonUpdate.setEnabled(connected);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JTable jMidiMonitorTable;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JPanel jUpdatePanel;
    // End of variables declaration//GEN-END:variables

}
