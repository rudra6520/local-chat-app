/*
 * Decompiled with CFR 0.152.
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LocalChatApp {
    public static void main(String[] stringArray) {
        SwingUtilities.invokeLater(() -> {
            AppUi.loadTheme();
            AppUi.bootstrapLookAndFeel();
            new LoginFrame().setVisible(true);
        });
    }

    static String readSocketLine(InputStream inputStream) throws IOException {
        int n;
        int n2;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (n2 = 0; n2 < 65536 && (n = inputStream.read()) >= 0 && n != 10; ++n2) {
            byteArrayOutputStream.write(n);
        }
        if (n2 >= 65536) {
            while ((n = inputStream.read()) >= 0 && n != 10) {
            }
            return null;
        }
        return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    }

    static String sanitizeIncomingFilename(String string) {
        if (string == null || string.isBlank()) {
            return "file.bin";
        }
        String string2 = new File(string.trim()).getName();
        if (string2.length() > 200) {
            string2 = string2.substring(0, 200);
        }
        return (string2 = string2.replaceAll("[^a-zA-Z0-9._-]", "_")).isBlank() ? "file.bin" : string2;
    }

    static String clipPayload(String string, int n) {
        if (string == null) {
            return "";
        }
        if (string.length() <= n) {
            return string;
        }
        return string.substring(0, n) + "\n[truncated]";
    }

    static String clipPlain(String string, int n) {
        if (string == null) {
            return "";
        }
        if (string.length() <= n) {
            return string;
        }
        return string.substring(0, n);
    }

    static boolean isValidProtocolUsername(String string) {
        return string != null && string.length() >= 1 && string.length() <= 80 && string.matches("[a-zA-Z0-9_.:-]+");
    }

    static String escape(String string) {
        return string.replace("\\", "\\\\").replace("\n", "\\n").replace(";", "\\;");
    }

    static String unescape(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean bl = false;
        for (int i = 0; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (bl) {
                if (c == 'n') {
                    stringBuilder.append('\n');
                } else {
                    stringBuilder.append(c);
                }
                bl = false;
                continue;
            }
            if (c == '\\') {
                bl = true;
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    static String htmlEscape(String string) {
        if (string == null) {
            return "";
        }
        return string.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    static String wrapChatHtmlDocument(String string) {
        return "<html><head><style type=\"text/css\">body,html{margin:0;padding:0;background:transparent;color:#f8fafc;}body{padding:20px 24px;font-family:'Segoe UI Emoji','Segoe UI',Roboto,sans-serif;font-size:14px;}</style></head><body>" + string + "</body></html>";
    }

    static String chatBubbleHtml(ChatMessage chatMessage, String string) {
        boolean bl = string.equals(chatMessage.from);
        String string2 = bl ? "right" : "left";
        // Glassmorphism colors
        String string3 = bl ? "rgba(79, 70, 229, 0.8)" : "rgba(30, 41, 59, 0.7)";
        String string4 = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(chatMessage.epochMs));
        String string5 = chatMessage.editedFromId != null ? " <span style=\"font-size:10px;opacity:0.4;\">(edited)</span>" : "";
        String string6 = LocalChatApp.htmlEscape(chatMessage.from);
        String string8 = bl ? "<span style=\"color:#e2e8f0;opacity:0.8;\">You</span>" : "<a href=\"user:" + chatMessage.from + "\" style=\"color:#818cf8;text-decoration:none;font-weight:bold;\">" + string6 + "</a>";
        String string7 = LocalChatApp.htmlEscape(chatMessage.body).replace("\n", "<br/>");
        
        return "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:8px 0;\">" +
               "<tr><td align=\"" + string2 + "\">" +
               "<div style=\"display:inline-block;max-width:85%;background:" + string3 + ";border-radius:20px;border:1px solid rgba(255,255,255,0.1);padding:12px 16px;box-shadow: 0 4px 15px rgba(0,0,0,0.2);\">" +
               "<div style=\"font-size:11px;opacity:0.6;margin-bottom:6px;font-weight:600;letter-spacing:0.5px;\">" + string8 + "</div>" +
               "<div style=\"color:#f8fafc;font-size:14.5px;line-height:1.6;word-break:break-word;overflow-wrap:anywhere;\">" + string7 + string5 + "</div>" +
               "<div style=\"font-size:10px;opacity:0.4;text-align:right;margin-top:8px;\">" + string4 + "</div>" +
               "</div>" +
               "</td></tr></table>";
    }

    static String chatSystemBannerHtml(String string) {
        return "<div align=\"center\" style=\"margin:14px 0;\"><span style=\"display:inline-block;background:#1e293b;color:#94a3b8;padding:6px 14px;border-radius:20px;font-size:12px;border:1px solid rgba(255,255,255,0.05);\">" + LocalChatApp.htmlEscape(string) + "</span></div>";
    }

    static String chatEmptyHintHtml(String string) {
        return "<div style=\"color:#8696a0;text-align:center;padding:40px 20px;font-size:14px;line-height:1.65;\">" + string + "</div>";
    }

    static int peerAddressScore(String string) {
        if (string == null || string.isBlank()) {
            return -100;
        }
        try {
            InetAddress inetAddress = InetAddress.getByName(string);
            if (inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress() || inetAddress.isMulticastAddress()) {
                return -100;
            }
            if (inetAddress instanceof Inet4Address) {
                if (inetAddress.isSiteLocalAddress()) {
                    return 40;
                }
                if (inetAddress.isLinkLocalAddress()) {
                    return 5;
                }
                return 20;
            }
            if (inetAddress instanceof Inet6Address) {
                if (inetAddress.isLinkLocalAddress()) {
                    return 8;
                }
                if (inetAddress.isSiteLocalAddress()) {
                    return 18;
                }
                return 12;
            }
            return 1;
        }
        catch (Exception exception) {
            return -100;
        }
    }

    static boolean shouldReplacePeerAddress(String string, String string2) {
        if (string2 == null || string2.isBlank()) {
            return false;
        }
        if (string == null || string.isBlank()) {
            return true;
        }
        int n = LocalChatApp.peerAddressScore(string);
        int n2 = LocalChatApp.peerAddressScore(string2);
        if (n2 > n) {
            return true;
        }
        if (n2 < n) {
            return false;
        }
        return !string2.equals(string);
    }

    static final class AppConfig {
        static final int DISCOVERY_PORT = 39001;
        static final int CHAT_PORT = 39002;
        static final int HEARTBEAT_MS = 2000;
        static final int OFFLINE_TIMEOUT_MS = 7000;
        static final String GROUP_ID = "GLOBAL_GROUP";
        static final long MESSAGE_MIN_INTERVAL_MS = 3500L;
        static final long FILE_SHARE_MAX_BYTES = 0x1400000L;
        static final long FILE_CHAT_MAX_BYTES = 0xF00000L;
        static final long CHAT_REQUEST_TTL_MS = 3600000L;
        static final int CHAT_REQUEST_NOTE_MAX_WORDS = 200;
        static final long EDIT_WINDOW_MS = 3600000L;
        static final String APP_VERSION = "1.4.0";
        static final String PRODUCT_TAGLINE = "Collaborative messaging for classrooms, labs, and team exercises on one local network.";
        static final String PRODUCT_TAGLINE_SHORT = "Class & lab use \u00b7 same Wi-Fi or LAN required";
        static final int MAX_PROTOCOL_LINE_BYTES = 65536;
        static final int MAX_MESSAGE_BODY_CHARS = 32000;
        static final int MAX_CHAT_REQUEST_NOTE_CHARS = 8000;
        static final int MAX_FILENAME_CHARS = 200;

        AppConfig() {
        }

        static String statusFooterLine() {
            return "Local network only \u00b7 UDP discovery 39001 \u00b7 TCP messaging 39002";
        }
    }

    static final class AppUi {
        static boolean isDark = true;
        static Color BG_DEEP = new Color(10, 15, 28);
        static Color BG_HEADER = new Color(15, 23, 42);
        static Color BG_PANEL = new Color(15, 23, 42);
        static Color BG_CARD = new Color(30, 41, 59);
        static Color BG_ELEVATED = new Color(51, 65, 85);
        static Color BG_FIELD = new Color(2, 6, 23);
        static Color BG_ZEBRA = new Color(15, 23, 42);
        static Color FG = new Color(248, 250, 252);
        static Color FG_MUTED = new Color(148, 163, 184);
        static Color ACCENT = new Color(99, 102, 241);
        static Color ACCENT_DIM = new Color(79, 70, 229);
        static Color ACCENT_HOVER = new Color(129, 140, 248);
        static Color SUCCESS = new Color(16, 185, 129);
        static Color BORDER = new Color(51, 65, 85);
        static Color BORDER_SOFT = new Color(30, 41, 59);
        static Color CHAT_BG = new Color(2, 6, 23);

        static void setTheme(boolean dark) {
            isDark = dark;
            saveTheme();
            if (dark) {
                BG_DEEP = new Color(10, 15, 28);
                BG_HEADER = new Color(15, 23, 42);
                BG_PANEL = new Color(15, 23, 42);
                BG_CARD = new Color(30, 41, 59);
                BG_ELEVATED = new Color(51, 65, 85);
                BG_FIELD = new Color(2, 6, 23);
                BG_ZEBRA = new Color(15, 23, 42);
                FG = new Color(248, 250, 252);
                FG_MUTED = new Color(148, 163, 184);
                ACCENT = new Color(99, 102, 241);
                ACCENT_DIM = new Color(79, 70, 229);
                ACCENT_HOVER = new Color(129, 140, 248);
                BORDER = new Color(51, 65, 85);
                BORDER_SOFT = new Color(30, 41, 59);
                CHAT_BG = new Color(2, 6, 23);
            } else {
                BG_DEEP = new Color(241, 245, 249);
                BG_HEADER = new Color(255, 255, 255);
                BG_PANEL = new Color(255, 255, 255);
                BG_CARD = new Color(255, 255, 255);
                BG_ELEVATED = new Color(226, 232, 240);
                BG_FIELD = new Color(248, 250, 252);
                BG_ZEBRA = new Color(241, 245, 249);
                FG = new Color(30, 58, 138); 
                FG_MUTED = new Color(71, 85, 105); 
                ACCENT = new Color(79, 70, 229);
                ACCENT_DIM = new Color(67, 56, 202);
                ACCENT_HOVER = new Color(99, 102, 241);
                BORDER = new Color(226, 232, 240);
                BORDER_SOFT = new Color(241, 245, 249);
                CHAT_BG = new Color(255, 255, 255);
            }
            bootstrapLookAndFeel();
        }

        private static final Path THEME_FILE = Path.of(System.getProperty("user.home"), ".local-chat-app", "theme.dat");

        static void loadTheme() {
            if (Files.exists(THEME_FILE)) {
                try {
                    String s = Files.readString(THEME_FILE).trim();
                    setTheme("dark".equals(s));
                } catch (Exception e) { setTheme(true); }
            } else {
                setTheme(true);
            }
        }

        static void saveTheme() {
            try {
                Files.createDirectories(THEME_FILE.getParent());
                Files.writeString(THEME_FILE, isDark ? "dark" : "light");
            } catch (Exception e) {}
        }

        static final int FIELD_RADIUS = 12;
        static final Font FONT_TITLE = new Font("Segoe UI", 1, 28);
        static final Font FONT_HEAD = new Font("Segoe UI", 1, 16);
        static final Font FONT_SUB = new Font("Segoe UI", 0, 14);
        static final Font FONT_SMALL = new Font("Segoe UI", 0, 13);
        static final Font FONT_CAPTION = new Font("Segoe UI", 1, 11);
        static final Font FONT_EMOJI = new Font("Segoe UI Emoji", Font.PLAIN, 18);

        AppUi() {
        }

        static void bootstrapLookAndFeel() {
            try {
                for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
                    if (!"Nimbus".equals(lookAndFeelInfo.getName())) continue;
                    UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
                    break;
                }
            }
            catch (Exception exception) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch (Exception exception2) {}
            }
            UIManager.put("control", BG_PANEL);
            UIManager.put("text", FG);
            UIManager.put("nimbusFocus", ACCENT);
            UIManager.put("nimbusBase", BG_CARD);
            UIManager.put("nimbusBlueGrey", BG_PANEL);
            UIManager.put("TabbedPane.background", BG_DEEP);
            UIManager.put("TabbedPane.foreground", FG);
            UIManager.put("TabbedPane.contentAreaColor", BG_CARD);
            UIManager.put("TextField.background", BG_FIELD);
            UIManager.put("PasswordField.background", BG_FIELD);
            UIManager.put("Button.background", ACCENT_DIM);
            UIManager.put("Panel.background", BG_DEEP);
            UIManager.put("OptionPane.background", BG_CARD);
            UIManager.put("ComboBox.background", BG_FIELD);
            UIManager.put("ScrollPane.background", BG_DEEP);
            UIManager.put("List.background", BG_FIELD);
            UIManager.put("List.foreground", FG);
            UIManager.put("TextArea.background", CHAT_BG);
            UIManager.put("TextArea.foreground", FG);
            UIManager.put("Table.background", BG_FIELD);
            UIManager.put("Table.foreground", FG);
            UIManager.put("Table.gridColor", BORDER);
            UIManager.put("Table.selectionBackground", ACCENT_DIM);
            UIManager.put("Table.selectionForeground", Color.WHITE);
            UIManager.put("TableHeader.background", BG_CARD);
            UIManager.put("TableHeader.foreground", FG);
            UIManager.put("Label.foreground", FG);
            UIManager.put("FileChooser.listViewBackground", BG_FIELD);
            UIManager.put("ScrollBar.thumb", BG_ELEVATED);
        }

        static void styleComposerIconButton(AbstractButton abstractButton, String string) {
            abstractButton.setText(string);
            abstractButton.setFont(abstractButton.getFont().deriveFont(0, 17.0f));
            abstractButton.setForeground(FG);
            abstractButton.setOpaque(true);
            abstractButton.setBackground(BG_ELEVATED);
            abstractButton.setFocusPainted(false);
            abstractButton.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(BORDER, 20), new EmptyBorder(7, 9, 7, 9)));
            abstractButton.setCursor(Cursor.getPredefinedCursor(12));
            Dimension dimension = new Dimension(42, 42);
            abstractButton.setPreferredSize(dimension);
            abstractButton.setMinimumSize(dimension);
        }

        static void styleHeaderButton(AbstractButton abstractButton, Color hoverColor) {
            abstractButton.setOpaque(false);
            abstractButton.setContentAreaFilled(false);
            abstractButton.setFocusPainted(false);
            abstractButton.setBorder(new EmptyBorder(4, 8, 4, 8));
            abstractButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            abstractButton.setForeground(FG);
            
            abstractButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    abstractButton.setForeground(hoverColor);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    abstractButton.setForeground(FG);
                }
            });
        }

        static final class BrandMessageIcon implements Icon {
            @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                g2.setColor(new Color(99, 102, 241, 30));
                g2.fillRoundRect(0, 0, 48, 44, 16, 16);
                g2.setColor(ACCENT);
                g2.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRoundRect(6, 6, 36, 26, 10, 10);
                Polygon p = new Polygon();
                p.addPoint(14, 32);
                p.addPoint(24, 32);
                p.addPoint(14, 42);
                g2.fillPolygon(p);
                g2.setColor(isDark ? new Color(255, 255, 255, 180) : ACCENT);
                g2.fillOval(14, 17, 4, 4);
                g2.fillOval(22, 17, 4, 4);
                g2.fillOval(30, 17, 4, 4);
                g2.dispose();
            }
            @Override public int getIconWidth() { return 48; }
            @Override public int getIconHeight() { return 48; }
        }

        // Hub-style network icon for login screen branding
        static final class HubIcon implements Icon {
            @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.translate(x, y);
                int cx = 20, cy = 20, r = 18;
                // Outer glow ring
                g2.setColor(new Color(99, 102, 241, 40));
                g2.fillOval(cx - r - 4, cy - r - 4, (r + 4) * 2, (r + 4) * 2);
                // Circle ring
                g2.setColor(new Color(192, 193, 255, 80));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);
                // Spokes
                g2.setColor(new Color(192, 193, 255, 120));
                g2.setStroke(new BasicStroke(1.2f));
                int[] px = {cx, cx, cx - r, cx + r, cx - 13, cx + 13};
                int[] py = {cy - r, cy + r, cy, cy, cy - 13, cy - 13};
                for (int i = 0; i < px.length; i++) g2.drawLine(cx, cy, px[i], py[i]);
                // Nodes
                g2.setColor(ACCENT);
                int nd = 5;
                g2.fillOval(cx - nd, cy - r - nd, nd * 2, nd * 2);
                g2.fillOval(cx - nd, cy + r - nd, nd * 2, nd * 2);
                g2.fillOval(cx - r - nd, cy - nd, nd * 2, nd * 2);
                g2.fillOval(cx + r - nd, cy - nd, nd * 2, nd * 2);
                g2.fillOval(cx - 13 - nd, cy - 13 - nd, nd * 2, nd * 2);
                g2.fillOval(cx + 13 - nd, cy - 13 - nd, nd * 2, nd * 2);
                // Centre dot
                g2.setColor(new Color(228, 225, 237));
                g2.fillOval(cx - 4, cy - 4, 8, 8);
                g2.dispose();
            }
            @Override public int getIconWidth()  { return 40; }
            @Override public int getIconHeight() { return 40; }
        }

        static void applyDarkShellToContainer(Container container) {
            if (container != null) {
                AppUi.styleFileChooserDeep(container);
            }
        }

        static void styleChooserList(JList<?> jList) {
            jList.setBackground(BG_FIELD);
            jList.setForeground(FG);
            jList.setSelectionBackground(ACCENT_DIM);
            jList.setSelectionForeground(Color.WHITE);
        }

        static void styleChooserTable(JTable jTable) {
            jTable.setBackground(BG_FIELD);
            jTable.setForeground(FG);
            jTable.setSelectionBackground(ACCENT_DIM);
            jTable.setSelectionForeground(Color.WHITE);
            jTable.setGridColor(BORDER);
        }

        static void styleFileChooserDeep(Container container) {
            for (Component component : container.getComponents()) {
                if (component instanceof JScrollPane) {
                    JScrollPane jScrollPane = (JScrollPane)component;
                    jScrollPane.setOpaque(true);
                    jScrollPane.setBackground(BG_PANEL);
                    jScrollPane.getViewport().setOpaque(true);
                    jScrollPane.getViewport().setBackground(BG_FIELD);
                    Component component2 = jScrollPane.getViewport().getView();
                    if (component2 instanceof JList) {
                        AppUi.styleChooserList((JList<?>)component2);
                    } else if (component2 instanceof JTable) {
                        AppUi.styleChooserTable((JTable)component2);
                    } else if (component2 instanceof Container) {
                        AppUi.styleFileChooserDeep((Container)component2);
                    }
                    continue;
                }
                if (component instanceof JList) {
                    AppUi.styleChooserList((JList<?>)component);
                    continue;
                }
                if (component instanceof JTable) {
                    AppUi.styleChooserTable((JTable)component);
                    continue;
                }
                if (component instanceof JTextField) {
                    AppUi.styleTextField((JTextField)component);
                    continue;
                }
                if (component instanceof JLabel) {
                    JLabel jLabel = (JLabel)component;
                    jLabel.setForeground(FG);
                    continue;
                }
                if (component instanceof JButton) {
                    AppUi.styleSecondaryButton((JButton)component);
                    continue;
                }
                if (component instanceof JComboBox) {
                    AppUi.styleCombo((JComboBox<?>)component);
                    continue;
                }
                if (component instanceof JPanel) {
                    JPanel jPanel = (JPanel)component;
                    jPanel.setOpaque(true);
                    jPanel.setBackground(BG_PANEL);
                    AppUi.styleFileChooserDeep(jPanel);
                    continue;
                }
                if (component instanceof Container) {
                    Container container2 = (Container)component;
                    String string = container2.getClass().getName();
                    if (string.contains("Popup") || string.contains("HeavyWeightWindow")) {
                        continue;
                    }
                    if (container2 instanceof JComponent) {
                        ((JComponent)container2).setOpaque(true);
                    }
                    container2.setBackground(BG_PANEL);
                    AppUi.styleFileChooserDeep(container2);
                }
            }
        }

        static final class ThemedFileChooser
        extends JFileChooser {
            private static final long serialVersionUID = 1L;
            ThemedFileChooser() {
            }

            ThemedFileChooser(File file) {
                super(file);
            }

            @Override
            protected JDialog createDialog(Component component) throws HeadlessException {
                JDialog jDialog = super.createDialog(component);
                jDialog.getContentPane().setBackground(BG_PANEL);
                jDialog.setBackground(BG_PANEL);
                jDialog.addWindowListener(new WindowAdapter(){

                    @Override
                    public void windowOpened(WindowEvent windowEvent) {
                        AppUi.applyDarkShellToContainer(jDialog.getContentPane());
                    }
                });
                return jDialog;
            }
        }

        static Border fieldBorder() {
            return BorderFactory.createCompoundBorder(new RoundedBorder(BORDER, 8), new EmptyBorder(11, 14, 11, 14));
        }

        static void styleTextField(JTextField jTextField) {
            jTextField.setForeground(FG);
            jTextField.setCaretColor(ACCENT);
            jTextField.setBorder(AppUi.fieldBorder());
            jTextField.setOpaque(false);
            jTextField.setBackground(new Color(BG_FIELD.getRed(), BG_FIELD.getGreen(), BG_FIELD.getBlue(), 140));
        }

        static void stylePasswordField(JPasswordField jPasswordField) {
            jPasswordField.setForeground(FG);
            jPasswordField.setCaretColor(ACCENT);
            jPasswordField.setBorder(AppUi.fieldBorder());
            jPasswordField.setOpaque(false);
            jPasswordField.setBackground(new Color(BG_FIELD.getRed(), BG_FIELD.getGreen(), BG_FIELD.getBlue(), 140));
        }

        static void stylePrimaryButton(AbstractButton abstractButton, Color color) {
            abstractButton.setBackground(color);
            abstractButton.setForeground(Color.WHITE);
            abstractButton.setOpaque(true);
            abstractButton.setFocusPainted(false);
            abstractButton.setFont(abstractButton.getFont().deriveFont(Font.BOLD, 13.0f));
            abstractButton.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(color.darker(), 14), new EmptyBorder(10, 24, 10, 24)));
            abstractButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            final Color hoverColor = ACCENT_HOVER;
            final float[] progress = {0.0f};
            final Timer timer = new Timer(15, null);
            timer.addActionListener(e -> {
                if (Boolean.TRUE.equals(abstractButton.getClientProperty("hover"))) {
                    progress[0] = Math.min(1.0f, progress[0] + 0.15f);
                } else {
                    progress[0] = Math.max(0.0f, progress[0] - 0.15f);
                }
                abstractButton.setBackground(lerpColor(color, hoverColor, progress[0]));
                if (progress[0] <= 0.0f || progress[0] >= 1.0f) timer.stop();
            });

            abstractButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    abstractButton.putClientProperty("hover", true);
                    if (!timer.isRunning()) timer.start();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    abstractButton.putClientProperty("hover", false);
                    if (!timer.isRunning()) timer.start();
                }
            });
        }

        static void styleSecondaryButton(AbstractButton abstractButton) {
            final Color baseColor = BG_CARD;
            final Color hoverColor = BG_ELEVATED;
            abstractButton.setForeground(FG);
            abstractButton.setOpaque(true);
            abstractButton.setBackground(baseColor);
            abstractButton.setFocusPainted(false);
            abstractButton.setFont(abstractButton.getFont().deriveFont(Font.PLAIN, 12.0f));
            abstractButton.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(BORDER, 12), new EmptyBorder(8, 16, 8, 16)));
            abstractButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            final float[] progress = {0.0f};
            final Timer timer = new Timer(15, null);
            timer.addActionListener(e -> {
                if (Boolean.TRUE.equals(abstractButton.getClientProperty("hover"))) {
                    progress[0] = Math.min(1.0f, progress[0] + 0.15f);
                } else {
                    progress[0] = Math.max(0.0f, progress[0] - 0.15f);
                }
                abstractButton.setBackground(lerpColor(baseColor, hoverColor, progress[0]));
                if (progress[0] <= 0.0f || progress[0] >= 1.0f) timer.stop();
            });

            abstractButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    abstractButton.putClientProperty("hover", true);
                    if (!timer.isRunning()) timer.start();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    abstractButton.putClientProperty("hover", false);
                    if (!timer.isRunning()) timer.start();
                }
            });
        }

        /** Glow-on-hover tool button: accent border + indigo glow when cursor enters */
        static void styleGlowToolButton(AbstractButton btn) {
            btn.setForeground(FG);
            btn.setOpaque(true);
            btn.setBackground(BG_CARD);
            btn.setFocusPainted(false);
            btn.setFont(btn.getFont().deriveFont(Font.PLAIN, 12.0f));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Custom border that animates glow width
            final float[] glow = {0f};
            final Timer glowTimer = new Timer(14, null);
            glowTimer.addActionListener(e -> {
                boolean hovered = Boolean.TRUE.equals(btn.getClientProperty("hover"));
                glow[0] = hovered ? Math.min(1f, glow[0] + 0.12f) : Math.max(0f, glow[0] - 0.12f);
                btn.setBackground(lerpColor(BG_CARD, new Color(60, 58, 110), glow[0]));
                btn.repaint();
                if (glow[0] <= 0f || glow[0] >= 1f) glowTimer.stop();
            });

            // Paint glow border manually
            btn.setBorder(new javax.swing.border.AbstractBorder() {
                @Override
                public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int arc = 10;
                    if (glow[0] > 0.01f) {
                        int layers = 3;
                        for (int i = layers; i >= 1; i--) {
                            float alpha = glow[0] * (0.15f / i);
                            int pad = i * 2;
                            g2.setColor(new Color(ACCENT.getRed(), ACCENT.getGreen(), ACCENT.getBlue(), (int)(alpha * 255)));
                            g2.setStroke(new BasicStroke(pad + 1));
                            g2.drawRoundRect(x - pad/2, y - pad/2, w + pad, h + pad, arc + pad, arc + pad);
                        }
                    }
                    g2.setColor(glow[0] > 0.01f ? lerpColor(BORDER, ACCENT, glow[0] * 0.7f) : BORDER);
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(x, y, w-1, h-1, arc, arc);
                    g2.dispose();
                }
                @Override
                public Insets getBorderInsets(Component c) { return new Insets(8, 14, 8, 14); }
                @Override
                public Insets getBorderInsets(Component c, Insets i) {
                    i.set(8, 14, 8, 14); return i;
                }
            });

            btn.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    btn.putClientProperty("hover", true);
                    if (!glowTimer.isRunning()) glowTimer.start();
                }
                @Override public void mouseExited(MouseEvent e) {
                    btn.putClientProperty("hover", false);
                    if (!glowTimer.isRunning()) glowTimer.start();
                }
            });
        }

        static Color lerpColor(Color c1, Color c2, float t) {
            int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * t);
            int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * t);
            int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * t);
            int a = (int) (c1.getAlpha() + (c2.getAlpha() - c1.getAlpha()) * t);
            return new Color(r, g, b, a);
        }

        static void styleGhostButton(AbstractButton abstractButton) {
            abstractButton.setForeground(ACCENT);
            abstractButton.setOpaque(false);
            abstractButton.setContentAreaFilled(false);
            abstractButton.setFont(abstractButton.getFont().deriveFont(0, 13.0f));
            abstractButton.setBorder(new EmptyBorder(12, 10, 12, 10));
            abstractButton.setCursor(Cursor.getPredefinedCursor(12));
        }

        static void styleTabPill(AbstractButton abstractButton, boolean bl) {
            abstractButton.setFocusPainted(false);
            abstractButton.setFont(abstractButton.getFont().deriveFont(1, 13.0f));
            abstractButton.setCursor(Cursor.getPredefinedCursor(12));
            if (bl) {
                abstractButton.setForeground(Color.WHITE);
                abstractButton.setOpaque(true);
                abstractButton.setBackground(ACCENT_DIM);
                abstractButton.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(ACCENT.darker(), 14), new EmptyBorder(10, 28, 10, 28)));
            } else {
                abstractButton.setForeground(FG_MUTED);
                abstractButton.setOpaque(true);
                abstractButton.setBackground(BG_CARD);
                abstractButton.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(BORDER, 14), new EmptyBorder(10, 28, 10, 28)));
            }
        }

        static TitledBorder titled(String string, Color color) {
            TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(color.getRed(), color.getGreen(), color.getBlue(), 120)), 
                "  " + string + "  ", 1, 2, FONT_CAPTION, FG_MUTED);
            return tb;
        }

        static void styleReadOnlyTextArea(JTextArea jTextArea, boolean bl) {
            jTextArea.setEditable(false);
            jTextArea.setOpaque(true);
            jTextArea.setBackground(CHAT_BG);
            jTextArea.setForeground(FG);
            jTextArea.setCaretColor(ACCENT);
            jTextArea.setFont(bl ? new Font("Monospaced", 0, 12) : new Font("SansSerif", 0, 13));
            jTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));
            jTextArea.setSelectedTextColor(Color.WHITE);
            jTextArea.setSelectionColor(ACCENT_DIM);
        }

        static JScrollPane wrapReadOnlyScroll(JTextArea jTextArea, boolean bl, int n, int n2) {
            AppUi.styleReadOnlyTextArea(jTextArea, bl);
            JScrollPane jScrollPane = new JScrollPane(jTextArea);
            jScrollPane.setOpaque(true);
            jScrollPane.getViewport().setOpaque(true);
            jScrollPane.getViewport().setBackground(CHAT_BG);
            AppUi.styleScroll(jScrollPane);
            if (n > 0 && n2 > 0) {
                jScrollPane.setPreferredSize(new Dimension(n, n2));
            }
            return jScrollPane;
        }

        static void styleEditableTextArea(JTextArea jTextArea) {
            jTextArea.setOpaque(false);
            jTextArea.setBackground(new Color(BG_FIELD.getRed(), BG_FIELD.getGreen(), BG_FIELD.getBlue(), 140));
            jTextArea.setForeground(FG);
            jTextArea.setCaretColor(ACCENT);
            jTextArea.setFont(FONT_SMALL);
            jTextArea.setBorder(AppUi.fieldBorder());
        }

        static void styleList(JList<?> jList) {
            jList.setBackground(BG_FIELD);
            jList.setForeground(FG);
            jList.setSelectionBackground(ACCENT_DIM);
            jList.setSelectionForeground(Color.WHITE);
            jList.setBorder(new RoundedBorder(BORDER_SOFT, 10));
            jList.setFixedCellHeight(30);
            jList.setFont(FONT_SMALL);
        }

        static void styleStringList(JList<String> jList) {
            AppUi.styleList(jList);
            jList.setCellRenderer(new DefaultListCellRenderer(){

                @Override
                public Component getListCellRendererComponent(JList<?> jList, Object object, int n, boolean bl, boolean bl2) {
                    JLabel jLabel = (JLabel)super.getListCellRendererComponent(jList, object, n, bl, bl2);
                    jLabel.setOpaque(true);
                    jLabel.setBorder(new EmptyBorder(4, 12, 4, 12));
                    if (bl) {
                        jLabel.setBackground(ACCENT_DIM);
                        jLabel.setForeground(Color.WHITE);
                    } else {
                        jLabel.setBackground(n % 2 == 0 ? BG_FIELD : BG_ZEBRA);
                        jLabel.setForeground(FG);
                    }
                    return jLabel;
                }
            });
        }

        static void styleEmojiList(JList<String> jList) {
            AppUi.styleList(jList);
            jList.setFont(FONT_EMOJI);
            jList.setCellRenderer(new DefaultListCellRenderer(){
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setBorder(new EmptyBorder(0, 0, 0, 0));
                    if (isSelected) {
                        label.setBackground(ACCENT_DIM);
                        label.setForeground(Color.WHITE);
                    } else {
                        label.setBackground(index % 2 == 0 ? BG_FIELD : BG_ZEBRA);
                        label.setForeground(FG);
                    }
                    return label;
                }
            });
        }

        static void styleScroll(JScrollPane jScrollPane) {
            jScrollPane.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(BORDER, 10), new EmptyBorder(0, 0, 0, 0)));
            jScrollPane.getViewport().setBackground(BG_FIELD);
        }

        static void styleCombo(JComboBox<?> jComboBox) {
            Component component;
            jComboBox.setForeground(FG);
            jComboBox.setBackground(new Color(BG_FIELD.getRed(), BG_FIELD.getGreen(), BG_FIELD.getBlue(), 120));
            jComboBox.setOpaque(false);
            jComboBox.setFont(FONT_SMALL);
            jComboBox.setBorder(BorderFactory.createCompoundBorder(new RoundedBorder(BORDER, 10), new EmptyBorder(4, 8, 4, 8)));
            jComboBox.setRenderer(new DefaultListCellRenderer(){

                @Override
                public Component getListCellRendererComponent(JList<?> jList, Object object, int n, boolean bl, boolean bl2) {
                    JLabel jLabel = (JLabel)super.getListCellRendererComponent(jList, object, n, bl, bl2);
                    jLabel.setOpaque(true);
                    if (bl) {
                        jLabel.setBackground(ACCENT_DIM);
                        jLabel.setForeground(Color.WHITE);
                    } else {
                        jLabel.setBackground(CHAT_BG);
                        jLabel.setForeground(FG);
                    }
                    return jLabel;
                }
            });
            if (jComboBox.getEditor() != null && (component = jComboBox.getEditor().getEditorComponent()) instanceof JComponent) {
                JComponent jComponent = (JComponent)component;
                jComponent.setForeground(FG);
                jComponent.setBackground(BG_FIELD);
            }
        }

        static class GlowIconLabel extends JLabel {
            private static final long serialVersionUID = 1L;
            private float glowLevel = 0.0f;
            private final Timer timer;
            private final String iconText;
            private Color iconColor = FG_MUTED;
            private Color glowColor = ACCENT;
            private boolean isMouseOver = false;

            GlowIconLabel(String icon, float size) {
                this.iconText = icon;
                this.setText(icon);
                this.setFont(this.getFont().deriveFont(size));
                this.setForeground(iconColor);
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                this.setHorizontalAlignment(SwingConstants.CENTER);
                
                this.timer = new Timer(20, e -> {
                    if (isMouseOver) {
                        glowLevel = Math.min(1.0f, glowLevel + 0.15f);
                    } else {
                        glowLevel = Math.max(0.0f, glowLevel - 0.15f);
                    }
                    if ((isMouseOver && glowLevel >= 1.0f) || (!isMouseOver && glowLevel <= 0.0f)) {
                        ((Timer)e.getSource()).stop();
                    }
                    repaint();
                });

                this.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { 
                        isMouseOver = true; 
                        if (!timer.isRunning()) timer.start(); 
                    }
                    @Override
                    public void mouseExited(MouseEvent e) { 
                        isMouseOver = false; 
                        if (!timer.isRunning()) timer.start(); 
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                if (glowLevel > 0) {
                    g2.setColor(new Color(glowColor.getRed(), glowColor.getGreen(), glowColor.getBlue(), (int)(glowLevel * 130)));
                    float spread = 2.0f * glowLevel;
                    for(float i=1; i<=spread; i+=0.5f) {
                        g2.drawString(iconText, (getWidth()-g2.getFontMetrics().stringWidth(iconText))/2.0f, getBaseline(getWidth(), getHeight()) + i/2);
                        g2.drawString(iconText, (getWidth()-g2.getFontMetrics().stringWidth(iconText))/2.0f, getBaseline(getWidth(), getHeight()) - i/2);
                    }
                }
                
                g2.setColor(isMouseOver ? Color.WHITE : iconColor);
                super.paintComponent(g2);
                g2.dispose();
            }
        }

        static void styleIconField(JPanel container, JComponent field, String icon, float iconSize) {
            container.setLayout(new BorderLayout(12, 0));
            container.setOpaque(false);
            GlowIconLabel iconLabel = new GlowIconLabel(icon, iconSize);
            iconLabel.setPreferredSize(new Dimension(30, 30));
            container.add(iconLabel, BorderLayout.WEST);
            container.add(field, BorderLayout.CENTER);
        }

        static final class RoundedBorder
        extends AbstractBorder {
            private static final long serialVersionUID = 1L;
            private final Color color;
            private final int arc;
            private final int thickness;

            RoundedBorder(Color color, int n) {
                this(color, n, 1);
            }

            RoundedBorder(Color color, int n, int thickness) {
                this.color = color;
                this.arc = n;
                this.thickness = thickness;
            }

            @Override
            public void paintBorder(Component component, Graphics graphics, int n, int n2, int n3, int n4) {
                Graphics2D graphics2D = (Graphics2D)graphics.create();
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics2D.setStroke(new BasicStroke(thickness));
                graphics2D.setColor(this.color);
                graphics2D.drawRoundRect(n + thickness, n2 + thickness, n3 - (thickness * 2 + 1), n4 - (thickness * 2 + 1), this.arc, this.arc);
                graphics2D.dispose();
            }

            @Override
            public Insets getBorderInsets(Component component) {
                return new Insets(thickness + 2, thickness + 2, thickness + 2, thickness + 2);
            }
        }

        static final class UIAnimator {
            static void fade(JComponent c, float start, float end, int duration, Runnable onDone) {
                long startTime = System.currentTimeMillis();
                Timer timer = new Timer(16, e -> {
                    long now = System.currentTimeMillis();
                    float progress = Math.min(1.0f, (float)(now - startTime) / duration);
                    float alpha = start + (end - start) * progress;
                    c.putClientProperty("alpha", alpha);
                    c.repaint();
                    if (progress >= 1.0f) {
                        ((Timer)e.getSource()).stop();
                        if (onDone != null) onDone.run();
                    }
                });
                timer.start();
            }
        }

        static final class GlassPanel extends JPanel {
            private static final long serialVersionUID = 1L;
            private float alpha = 0.15f;
            GlassPanel() { setOpaque(false); }
            GlassPanel(float alpha) { this.alpha = alpha; setOpaque(false); }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, (int)(alpha * 255)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.setStroke(new BasicStroke(1.0f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        }
    }

    static final class LoginFrame
    extends JFrame {
        private static final long serialVersionUID = 1L;
        private final JTextField suUser = new JTextField();
        private final JPasswordField suPass = new JPasswordField();
        private final JPasswordField suPass2 = new JPasswordField();
        private final JTextField liUser = new JTextField();
        private final JPasswordField liPass = new JPasswordField();
        private final transient LocalAccountStore accounts = new LocalAccountStore();
        private final transient SavedLoginStore savedLogins = new SavedLoginStore();
        private final JComboBox<String> savedAccounts = new JComboBox<String>();
        private final JCheckBox rememberSignIn = new JCheckBox("Save this username and password on this PC");
        private final CardLayout cardLayout = new CardLayout();
        private final JPanel cardHost = new JPanel(this.cardLayout);
        private JButton pillSignIn;
        private JButton pillSignUp;
        
        private static class Blob {
            double x, y, dx, dy, size;
            Color color;
            Blob(Random r, int w, int h) {
                x = r.nextInt(w); y = r.nextInt(h);
                dx = (r.nextDouble() - 0.5) * 1.5;
                dy = (r.nextDouble() - 0.5) * 1.5;
                size = 250 + r.nextInt(350);
                int type = r.nextInt(3);
                if (AppUi.isDark) {
                    if (type == 0) color = new Color(99, 102, 241, 45);  // Indigo
                    else if (type == 1) color = new Color(168, 85, 247, 40); // Purple
                    else color = new Color(6, 182, 212, 35);  // Cyan
                } else {
                    if (type == 0) color = new Color(99, 102, 241, 30);  // Soft Indigo
                    else if (type == 1) color = new Color(168, 85, 247, 25); // Soft Purple
                    else color = new Color(6, 182, 212, 20);  // Soft Cyan
                }
            }
            void move(int w, int h) {
                x += dx; y += dy;
                if (x < -size/2 || x > w + size/2) dx *= -1;
                if (y < -size/2 || y > h + size/2) dy *= -1;
            }
        }
        private final List<Blob> blobs = new ArrayList<>();
        private final Random rnd = new Random();

        LoginFrame() {
            this.setTitle("Local Chat v1.4.0");
            this.setDefaultCloseOperation(3);
            AppLog.init();

            // ── Animated background canvas ────────────────────────────
            JPanel jPanel = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics graphics) {
                    super.paintComponent(graphics);
                    Graphics2D g2 = (Graphics2D) graphics.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int w = getWidth(), h = getHeight();
                    if (blobs.isEmpty()) for (int i = 0; i < 6; i++) blobs.add(new Blob(rnd, w, h));
                    g2.setPaint(new LinearGradientPaint(0, 0, w, h,
                        new float[]{0f, 1f},
                        new Color[]{AppUi.BG_DEEP, AppUi.BG_HEADER}));
                    g2.fillRect(0, 0, w, h);
                    for (Blob b : blobs) {
                        b.move(w, h);
                        g2.setPaint(new RadialGradientPaint((float)b.x, (float)b.y, (float)b.size,
                            new float[]{0f, 1f}, new Color[]{b.color, new Color(0,0,0,0)}));
                        g2.fillOval((int)(b.x-b.size), (int)(b.y-b.size), (int)b.size*2, (int)b.size*2);
                    }
                    g2.dispose();
                }
                @Override
                public void doLayout() {
                    int w = getWidth(), h = getHeight();
                    if (getComponentCount() > 0) {
                        Component card = getComponent(0);
                        int cw = Math.min(460, w - 48);
                        int ch = card.getPreferredSize().height;
                        card.setBounds((w - cw) / 2, Math.max(24, (h - ch) / 2), cw, ch);
                    }
                    if (getComponentCount() > 1) {
                        Component ftr = getComponent(1);
                        Dimension fd = ftr.getPreferredSize();
                        ftr.setBounds((w - fd.width) / 2, h - fd.height - 20, fd.width, fd.height);
                    }
                }
            };
            new Timer(20, e -> jPanel.repaint()).start();
            // ── Glass Card ────────────────────────────────────────────
            JPanel glassCard = new JPanel(new BorderLayout(0, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (AppUi.isDark) {
                        g2.setColor(new Color(19, 19, 27, 200));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                        g2.setColor(new Color(255, 255, 255, 28));
                    } else {
                        g2.setColor(new Color(255, 255, 255, 220));
                        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                        g2.setColor(new Color(99, 102, 241, 60));
                    }
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 32, 32);
                    g2.dispose();
                }
            };
            glassCard.setOpaque(false);
            glassCard.setBorder(new EmptyBorder(32, 32, 32, 32));

            // Brand section: Hub icon + title + version
            JLabel hubLabel = new JLabel();
            hubLabel.setIcon(new AppUi.BrandMessageIcon());
            hubLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel titleLabel = new JLabel("Local Chat");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
            titleLabel.setForeground(AppUi.FG);
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel versionLabel = new JLabel("VERSION " + AppConfig.APP_VERSION);
            versionLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            versionLabel.setForeground(AppUi.FG_MUTED);
            versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            versionLabel.setBorder(new EmptyBorder(4, 0, 0, 0));
            JPanel brandPanel = new JPanel();
            brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));
            brandPanel.setOpaque(false);
            brandPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
            hubLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            brandPanel.add(hubLabel);
            brandPanel.add(Box.createVerticalStrut(10));
            brandPanel.add(titleLabel);
            brandPanel.add(versionLabel);

            // Theme toggle (top-right inside card)
            JButton btnTheme = new JButton(AppUi.isDark ? "\ud83c\udf19" : "\u2600\ufe0f");
            AppUi.styleHeaderButton(btnTheme, AppUi.ACCENT_HOVER);
            btnTheme.addActionListener(e -> {
                AppUi.setTheme(!AppUi.isDark);
                this.dispose();
                new LoginFrame().setVisible(true);
            });
            JPanel cardTopRow = new JPanel(new BorderLayout());
            cardTopRow.setOpaque(false);
            cardTopRow.add(brandPanel, BorderLayout.CENTER);
            JPanel themeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            themeWrap.setOpaque(false);
            themeWrap.add(btnTheme);
            cardTopRow.add(themeWrap, BorderLayout.EAST);

            // Tab pills
            this.pillSignIn = new JButton("Sign in");
            this.pillSignUp = new JButton("Create account");
            AppUi.styleTabPill(this.pillSignIn, true);
            AppUi.styleTabPill(this.pillSignUp, false);
            this.pillSignIn.addActionListener(e -> this.showLoginCard("in"));
            this.pillSignUp.addActionListener(e -> this.showLoginCard("up"));
            JPanel pillPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
            pillPanel.setOpaque(false);
            pillPanel.setBorder(new EmptyBorder(0, 0, 16, 0));
            pillPanel.add(this.pillSignIn);
            pillPanel.add(this.pillSignUp);

            // Forms
            this.cardHost.setOpaque(false);
            this.cardHost.setBorder(new EmptyBorder(0, 0, 0, 0));
            this.cardHost.add(this.buildSignInForm(), "in");
            this.cardHost.add(this.buildSignUpForm(), "up");
            this.cardLayout.show(this.cardHost, "in");

            // Guest link
            JButton guestBtn = new JButton("Continue as guest \u2014 skip account");
            AppUi.styleGhostButton(guestBtn);
            guestBtn.addActionListener(e -> this.enterGuest());
            JPanel guestPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
            guestPanel.setOpaque(false);
            guestPanel.add(guestBtn);

            JPanel formWrapper = new JPanel();
            formWrapper.setLayout(new BoxLayout(formWrapper, BoxLayout.Y_AXIS));
            formWrapper.setOpaque(false);
            formWrapper.add(cardTopRow);
            formWrapper.add(pillPanel);
            formWrapper.add(this.cardHost);
            formWrapper.add(guestPanel);
            glassCard.add(formWrapper, BorderLayout.CENTER);

            // Decorative badge pills at bottom
            JPanel footerPills = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
            footerPills.setOpaque(false);
            footerPills.add(makeBadgePill("\ud83d\udd12 E2E Encrypted"));
            footerPills.add(makeBadgePill("\u26a1 Real-time Sync"));

            jPanel.add(glassCard);
            jPanel.add(footerPills);
            this.setLayout(new BorderLayout());
            this.add(jPanel, BorderLayout.CENTER);
            this.setMinimumSize(new Dimension(600, 620));
            this.pack();
            this.setSize(940, 720);
            this.setLocationRelativeTo(null);
            AppUi.UIAnimator.fade(glassCard, 0f, 1f, 800, null);
        }

        private void showLoginCard(String string) {
            this.cardLayout.show(this.cardHost, "in".equals(string) ? "in" : "up");
            AppUi.styleTabPill(this.pillSignIn, "in".equals(string));
            AppUi.styleTabPill(this.pillSignUp, "up".equals(string));
        }

        private static JPanel makeBadgePill(String text) {
            JPanel pill = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4)) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(31, 31, 39, 120));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.setColor(new Color(255, 255, 255, 15));
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                    g2.dispose();
                }
            };
            pill.setOpaque(false);
            JLabel lbl = new JLabel(text);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(AppUi.FG_MUTED);
            pill.add(lbl);
            return pill;
        }

        private void addFormRow(JPanel jPanel, GridBagConstraints gridBagConstraints, int n, String label, JComponent jComponent, String icon) {
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = n;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.anchor = 22;
            gridBagConstraints.fill = 0;
            gridBagConstraints.insets = new Insets(10, 0, 0, 12);
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(AppUi.FONT_SMALL);
            jLabel.setForeground(AppUi.FG_MUTED);
            jPanel.add((Component)jLabel, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.anchor = 21;
            gridBagConstraints.fill = 2;
            gridBagConstraints.insets = new Insets(12, 0, 0, 0);
            if (icon != null) {
                JPanel jPanel2 = new JPanel();
                AppUi.styleIconField(jPanel2, jComponent, icon, 18.0f);
                jPanel.add((Component)jPanel2, gridBagConstraints);
            } else {
                jPanel.add((Component)jComponent, gridBagConstraints);
            }
        }

        private JPanel buildSignUpForm() {
            JPanel jPanel = new JPanel(new GridBagLayout());
            jPanel.setOpaque(false);
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.fill = 2;
            gridBagConstraints.insets = new Insets(0, 0, 8, 0);
            JLabel jLabel = new JLabel("Create your account");
            jLabel.setFont(AppUi.FONT_HEAD);
            jLabel.setForeground(AppUi.FG);
            jPanel.add((Component)jLabel, gridBagConstraints);
            gridBagConstraints.gridy = 1;
            JLabel jLabel2 = new JLabel("<html><body style='width:400px;color:#a8b0c4'>Username: <b>3\u201332</b> characters (letters, numbers, <b>_</b>). Password: at least <b>4</b> characters.</body></html>");
            jPanel.add((Component)jLabel2, gridBagConstraints);
            this.suUser.setColumns(24);
            this.suPass.setColumns(24);
            this.suPass2.setColumns(24);
            AppUi.styleTextField(this.suUser);
            AppUi.stylePasswordField(this.suPass);
            AppUi.stylePasswordField(this.suPass2);
            this.addFormRow(jPanel, gridBagConstraints, 2, "Username", this.suUser, "\ud83d\udc64");
            this.addFormRow(jPanel, gridBagConstraints, 3, "Password", this.suPass, "\ud83d\udd12");
            this.addFormRow(jPanel, gridBagConstraints, 4, "Confirm password", this.suPass2, "\ud83d\udee1\ufe0f");
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.fill = 2;
            gridBagConstraints.insets = new Insets(22, 0, 0, 0);
            JButton jButton = new JButton("Create account");
            AppUi.stylePrimaryButton(jButton, AppUi.ACCENT);
            jButton.addActionListener(actionEvent -> this.doSignUp());
            jPanel.add((Component)jButton, gridBagConstraints);
            return jPanel;
        }

        private JPanel buildSignInForm() {
            JPanel jPanel = new JPanel(new GridBagLayout());
            jPanel.setOpaque(false);
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.fill = 2;
            gridBagConstraints.insets = new Insets(0, 0, 8, 0);
            JLabel jLabel = new JLabel("Welcome back");
            jLabel.setFont(AppUi.FONT_HEAD);
            jLabel.setForeground(AppUi.FG);
            jPanel.add((Component)jLabel, gridBagConstraints);
            gridBagConstraints.gridy = 1;
            JLabel jLabel2 = new JLabel("Sign in with the username and password you chose at sign up.");
            jLabel2.setFont(AppUi.FONT_SUB);
            jLabel2.setForeground(AppUi.FG_MUTED);
            jPanel.add((Component)jLabel2, gridBagConstraints);
            this.liUser.setColumns(24);
            AppUi.styleTextField(this.liUser);
            AppUi.stylePasswordField(this.liPass);
            this.savedAccounts.setPrototypeDisplayValue("Select account");
            AppUi.styleCombo(this.savedAccounts);
            this.savedAccounts.addActionListener(actionEvent -> this.onSavedAccountPicked());
            this.rememberSignIn.setOpaque(false);
            this.rememberSignIn.setForeground(AppUi.FG_MUTED);
            this.addFormRow(jPanel, gridBagConstraints, 2, "Username", this.liUser, "\ud83d\udc64");
            this.addFormRow(jPanel, gridBagConstraints, 3, "Password", this.liPass, "\ud83d\udd12");

            JPanel savedPanel = new JPanel(new BorderLayout(4, 0));
            savedPanel.setOpaque(false);
            savedPanel.add(this.savedAccounts, BorderLayout.CENTER);
            AppUi.GlowIconLabel btnDeleteAccount = new AppUi.GlowIconLabel("\ud83d\uddd1", 18.0f);
            btnDeleteAccount.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
            btnDeleteAccount.setToolTipText("Delete this account from this PC");
            btnDeleteAccount.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) { deleteSavedAccount(); }
            });
            savedPanel.add(btnDeleteAccount, BorderLayout.EAST);
            
            this.addFormRow(jPanel, gridBagConstraints, 4, "Saved account", savedPanel, null);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.fill = 2;
            gridBagConstraints.insets = new Insets(10, 0, 0, 0);
            jPanel.add((Component)this.rememberSignIn, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 6;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.fill = 2;
            gridBagConstraints.insets = new Insets(22, 0, 0, 0);
            JButton jButton = new JButton("Sign in");
            AppUi.stylePrimaryButton(jButton, AppUi.ACCENT.darker());
            jButton.addActionListener(actionEvent -> this.doSignIn());
            jPanel.add((Component)jButton, gridBagConstraints);
            gridBagConstraints.gridy = 7;
            gridBagConstraints.insets = new Insets(10, 0, 0, 0);
            JButton jButton2 = new JButton("Sign in with saved account");
            AppUi.styleSecondaryButton(jButton2);
            jButton2.addActionListener(actionEvent -> this.doSavedSignIn());
            jPanel.add((Component)jButton2, gridBagConstraints);
            this.reloadSavedAccounts();
            return jPanel;
        }

        private static String validateUsernameForSignUp(String string) {
            if (string == null || string.isBlank()) {
                return "Enter a username.";
            }
            if (string.length() < 3 || string.length() > 32) {
                return "Username must be 3\u201332 characters.";
            }
            if (!string.matches("[a-zA-Z0-9_]+")) {
                return "Username may only contain letters, numbers, and underscores.";
            }
            return null;
        }

        private void doSignUp() {
            String string = this.suUser.getText().trim();
            String string2 = LoginFrame.validateUsernameForSignUp(string);
            if (string2 != null) {
                JOptionPane.showMessageDialog(this, string2, "Sign up", 2);
                return;
            }
            char[] cArray = this.suPass.getPassword();
            char[] cArray2 = this.suPass2.getPassword();
            if (cArray.length < 4) {
                JOptionPane.showMessageDialog(this, "Password must be at least 4 characters.");
                Arrays.fill(cArray, '\u0000');
                Arrays.fill(cArray2, '\u0000');
                return;
            }
            if (!Arrays.equals(cArray, cArray2)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.");
                Arrays.fill(cArray, '\u0000');
                Arrays.fill(cArray2, '\u0000');
                return;
            }
            try {
                if (!this.accounts.register(string, cArray)) {
                    JOptionPane.showMessageDialog(this, "That username is already taken, or it does not meet the rules (3\u201332 chars, letters/numbers/_).", "Sign up", 2);
                    Arrays.fill(cArray, '\u0000');
                    Arrays.fill(cArray2, '\u0000');
                    return;
                }
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Registration failed: " + exception.getMessage(), "Error", 0);
                Arrays.fill(cArray, '\u0000');
                Arrays.fill(cArray2, '\u0000');
                return;
            }
            Arrays.fill(cArray2, '\u0000');
            Arrays.fill(cArray, '\u0000');
            JOptionPane.showMessageDialog(this, "Account created. You can sign in with your username and password.", "Sign up", 1);
            this.suPass.setText("");
            this.suPass2.setText("");
            this.liUser.setText(string);
            this.liPass.setText("");
            this.liPass.requestFocusInWindow();
            this.showLoginCard("in");
        }

        private void doSignIn() {
            String string = this.liUser.getText().trim();
            char[] cArray = this.liPass.getPassword();
            if (string.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username is required.");
                Arrays.fill(cArray, '\u0000');
                return;
            }
            if (cArray.length == 0) {
                JOptionPane.showMessageDialog(this, "Password is required.");
                Arrays.fill(cArray, '\u0000');
                return;
            }
            try {
                String[] stringArray = this.accounts.login(string, cArray);
                if (stringArray == null) {
                    Arrays.fill(cArray, '\u0000');
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                    return;
                }
                if (this.rememberSignIn.isSelected()) {
                    this.savedLogins.save(string, cArray);
                }
                this.reloadSavedAccounts();
                Arrays.fill(cArray, '\u0000');
                this.dispose();
                new MainFrame(stringArray[0], "LocalAccount").setVisible(true);
            }
            catch (Exception exception) {
                Arrays.fill(cArray, '\u0000');
                JOptionPane.showMessageDialog(this, "Sign in failed: " + exception.getMessage());
            }
        }

        private void doSavedSignIn() {
            String string = (String)this.savedAccounts.getSelectedItem();
            if (string == null || string.equals("-- Select saved account --")) {
                JOptionPane.showMessageDialog(this, "Choose a saved account first.");
                return;
            }
            try {
                String string2 = this.savedLogins.loadPassword(string);
                if (string2 == null || string2.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Saved password was not found for that account.");
                    this.reloadSavedAccounts();
                    return;
                }
                this.liUser.setText(string);
                this.liPass.setText(string2);
                this.doSignIn();
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Saved login failed: " + exception.getMessage());
            }
        }

        private void onSavedAccountPicked() {
            String string = (String)this.savedAccounts.getSelectedItem();
            if (string == null || string.equals("-- Select saved account --")) {
                return;
            }
            this.liUser.setText(string);
            try {
                String string2 = this.savedLogins.loadPassword(string);
                this.liPass.setText(string2 == null ? "" : string2);
            }
            catch (Exception exception) {
                this.liPass.setText("");
            }
            this.rememberSignIn.setSelected(true);
        }

        private void reloadSavedAccounts() {
            this.savedAccounts.removeAllItems();
            this.savedAccounts.addItem("-- Select saved account --");
            for (String string : this.savedLogins.listUsernames()) {
                this.savedAccounts.addItem(string);
            }
            this.savedAccounts.setSelectedIndex(0);
        }

        private void deleteSavedAccount() {
            String string = (String)this.savedAccounts.getSelectedItem();
            if (string == null || string.equals("-- Select saved account --")) {
                JOptionPane.showMessageDialog(this, "Select a saved account to delete first.", "Delete Account", 2);
                return;
            }
            int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the account '" + string + "' from this PC?\nThis will remove saved credentials and all local data for this user.", "Delete Account", 0, 2);
            if (n != 0) {
                return;
            }
            try {
                this.savedLogins.delete(string);
                this.accounts.deleteAccount(string);
                new StorageManager(string).deleteAll();
                JOptionPane.showMessageDialog(this, "Account '" + string + "' has been deleted.", "Deleted", 1);
                this.reloadSavedAccounts();
                if (this.liUser.getText().trim().equalsIgnoreCase(string)) {
                    this.liUser.setText("");
                    this.liPass.setText("");
                }
            }
            catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Error deleting account: " + exception.getMessage(), "Error", 0);
            }
        }

        private void enterGuest() {
            String string = "Guest_" + Integer.toHexString((int)(System.nanoTime() & 0xFFFFL));
            this.dispose();
            new MainFrame(string, "Guest").setVisible(true);
        }
    }

    static final class StorageManager {
        private final Path base;
        private final byte[] aesKey;

        StorageManager(String string) {
            this.base = Path.of(System.getProperty("user.home"), ".local-chat-app", StorageManager.sanitize(string));
            try {
                Files.createDirectories(this.base);
            }
            catch (IOException iOException) {
                // empty catch block
            }
            String string2 = System.getProperty("user.name", "user") + "|" + System.getProperty("os.name", "");
            this.aesKey = StorageManager.deriveKey("local-chat-key|" + string + "|" + string2);
        }

        Map<String, List<ChatMessage>> loadPrivateChats() {
            return this.loadChatMap("private.dat");
        }

        Map<String, List<ChatMessage>> loadGroupChats() {
            return this.loadChatMap("group.dat");
        }

        Set<String> loadBlockedUsers() {
            Path path = this.base.resolve("blocked.dat");
            if (!Files.exists(path, new LinkOption[0])) {
                return new HashSet<String>();
            }
            try {
                String string2 = this.decrypt(Files.readAllBytes(path));
                return Arrays.stream(string2.split("\n")).filter(string -> !string.isBlank()).collect(Collectors.toSet());
            }
            catch (Exception exception) {
                return new HashSet<String>();
            }
        }

        Set<String> loadRequestedUsers() {
            Path path = this.base.resolve("requested.dat");
            if (!Files.exists(path, new LinkOption[0])) {
                return new HashSet<String>();
            }
            try {
                String string = this.decrypt(Files.readAllBytes(path));
                return Arrays.stream(string.split("\n")).filter(s -> !s.isBlank()).collect(Collectors.toSet());
            }
            catch (Exception exception) {
                return new HashSet<String>();
            }
        }

        Map<String, String> loadPrivateChatPasswords() {
            Path path = this.base.resolve("passwords.dat");
            if (!Files.exists(path, new LinkOption[0])) {
                return new HashMap<String, String>();
            }
            try {
                String string = this.decrypt(Files.readAllBytes(path));
                HashMap<String, String> hashMap = new HashMap<String, String>();
                for (String string2 : string.split("\n")) {
                    if (string2.isBlank() || !string2.contains("=")) continue;
                    String[] stringArray = string2.split("=", 2);
                    hashMap.put(stringArray[0], stringArray[1]);
                }
                return hashMap;
            }
            catch (Exception exception) {
                return new HashMap<String, String>();
            }
        }

        void savePrivateChats(Map<String, List<ChatMessage>> map) {
            this.saveChatMap("private.dat", map);
        }

        void saveGroupChats(Map<String, List<ChatMessage>> map) {
            this.saveChatMap("group.dat", map);
        }

        void saveBlockedUsers(Set<String> set) {
            Path path = this.base.resolve("blocked.dat");
            String string = String.join((CharSequence)"\n", set);
            this.writeEncrypted(path, string);
        }

        void saveRequestedUsers(Set<String> set) {
            Path path = this.base.resolve("requested.dat");
            String string = String.join((CharSequence)"\n", set);
            this.writeEncrypted(path, string);
        }

        void savePrivateChatPasswords(Map<String, String> map) {
            StringBuilder stringBuilder = new StringBuilder();
            map.forEach((string, string2) -> stringBuilder.append(string).append("=").append(string2).append("\n"));
            this.writeEncrypted(this.base.resolve("passwords.dat"), stringBuilder.toString());
        }

        void exportEncrypted(Path path, String string) {
            this.writeEncrypted(path, string);
        }

        void exportBase64Plain(Path path, String string) throws IOException {
            String string2 = Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
            Files.writeString(path, (CharSequence)string2, StandardCharsets.UTF_8, new OpenOption[0]);
        }

        void deleteAll() {
            try {
                if (!Files.exists(this.base, new LinkOption[0])) {
                    return;
                }
                Files.list(this.base).forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                });
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }

        private Map<String, List<ChatMessage>> loadChatMap(String string2) {
            Path path = this.base.resolve(string2);
            if (!Files.exists(path, new LinkOption[0])) {
                return new HashMap<String, List<ChatMessage>>();
            }
            try {
                String string3;
                byte[] byArray = Files.readAllBytes(path);
                try {
                    string3 = this.decrypt(byArray);
                }
                catch (Exception exception) {
                    AppLog.line("WARN", "Decrypt failed for " + string2 + ", trying .bak");
                    Path bakPath = this.base.resolve(string2 + ".bak");
                    if (Files.exists(bakPath, new LinkOption[0])) {
                        Files.copy(bakPath, path, StandardCopyOption.REPLACE_EXISTING);
                        string3 = this.decrypt(Files.readAllBytes(path));
                        AppLog.line("INFO", "Restored " + string2 + " from backup");
                    }
                    throw exception;
                }
                HashMap<String, List<ChatMessage>> hashMap = new HashMap<String, List<ChatMessage>>();
                for (String string4 : string3.split("\n")) {
                    String string5;
                    if (string4.isBlank()) continue;
                    String[] stringArray = string4.split("\\|", 8);
                    if (stringArray.length >= 8) {
                        string5 = stringArray[0];
                        long l = Long.parseLong(stringArray[4]);
                        String string6 = "null".equals(stringArray[6]) ? null : stringArray[6];
                        ChatMessage chatMessage = new ChatMessage(stringArray[5], stringArray[1], stringArray[2], LocalChatApp.unescape(stringArray[7]), stringArray[3], l, string6);
                        hashMap.computeIfAbsent(string5, string -> new CopyOnWriteArrayList<ChatMessage>()).add(chatMessage);
                        continue;
                    }
                    if (stringArray.length < 5) continue;
                    string5 = stringArray[0];
                    ChatMessage chatMessage = new ChatMessage(stringArray[1], stringArray[2], LocalChatApp.unescape(stringArray[4]), stringArray[3]);
                    hashMap.computeIfAbsent(string5, string -> new CopyOnWriteArrayList<ChatMessage>()).add(chatMessage);
                }
                return hashMap;
            }
            catch (Exception exception) {
                AppLog.line("ERROR", "loadChatMap " + string2 + ": " + exception.getMessage());
                return new HashMap<String, List<ChatMessage>>();
            }
        }

        private void saveChatMap(String string, Map<String, List<ChatMessage>> map) {
            Path path = this.base.resolve(string);
            try {
                if (Files.exists(path, new LinkOption[0])) {
                    Files.copy(path, this.base.resolve(string + ".bak"), StandardCopyOption.REPLACE_EXISTING);
                    try {
                        String logLine = String.valueOf(Instant.now()) + " backup " + string + "\n";
                        Files.writeString(AppLog.BACKUP_LOG, logLine, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    }
                    catch (IOException iOException) {}
                }
            }
            catch (IOException iOException) {
                AppLog.line("WARN", "Pre-save backup failed: " + iOException.getMessage());
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, List<ChatMessage>> entry : map.entrySet()) {
                StorageManager.appendChatLinesForRoom(stringBuilder, entry.getKey(), entry.getValue());
            }
            this.writeEncrypted(path, stringBuilder.toString());
        }

        private static void appendChatLinesForRoom(StringBuilder stringBuilder, String room, List<ChatMessage> list) {
            for (ChatMessage chatMessage : list) {
                String edited = chatMessage.editedFromId == null ? "null" : chatMessage.editedFromId;
                stringBuilder.append(room).append("|").append(chatMessage.from).append("|").append(chatMessage.to).append("|").append(chatMessage.room).append("|").append(chatMessage.epochMs).append("|").append(chatMessage.id).append("|").append(edited).append("|").append(LocalChatApp.escape(chatMessage.body)).append("\n");
            }
        }

        private void writeEncrypted(Path path, String string) {
            try {
                byte[] byArray = this.encrypt(string);
                Files.write(path, byArray, new OpenOption[0]);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }

        private byte[] encrypt(String string) throws Exception {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, new SecretKeySpec(this.aesKey, "AES"));
            return cipher.doFinal(string.getBytes(StandardCharsets.UTF_8));
        }

        private String decrypt(byte[] byArray) throws Exception {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, new SecretKeySpec(this.aesKey, "AES"));
            return new String(cipher.doFinal(byArray), StandardCharsets.UTF_8);
        }

        private static byte[] deriveKey(String string) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                byte[] byArray = messageDigest.digest(string.getBytes(StandardCharsets.UTF_8));
                return Arrays.copyOf(byArray, 16);
            }
            catch (Exception exception) {
                return "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);
            }
        }

        static String sanitize(String string) {
            return string.replaceAll("[^a-zA-Z0-9._-]", "_");
        }

    }

    static final class ChatServer {
        private final String selfUser;
        private final Set<String> blocked;
        private final CommandHandler handler;
        private volatile boolean running = true;
        private ServerSocket server;

        ChatServer(String string, Set<String> set, CommandHandler commandHandler) {
            this.selfUser = string;
            this.blocked = set;
            this.handler = commandHandler;
        }

        void start() {
            try {
                this.server = new ServerSocket(39002);
                while (this.running) {
                    Socket socket = this.server.accept();
                    new Thread(() -> this.handleClient(socket), "chat-client").start();
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }

        void stop() {
            this.running = false;
            if (this.server != null) {
                try {
                    this.server.close();
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }

        private void handleClient(Socket socket) {
            try (Socket socket2 = socket;){
                InputStream inputStream = socket2.getInputStream();
                String string = LocalChatApp.readSocketLine(inputStream);
                if (string == null || string.isEmpty()) {
                    return;
                }
                String[] stringArray = string.split(";", 3);
                if (stringArray.length < 3) {
                    return;
                }
                String string2 = stringArray[0];
                String string3 = stringArray[1];
                String string4 = LocalChatApp.unescape(stringArray[2]);
                if (!LocalChatApp.isValidProtocolUsername(string3)) {
                    return;
                }
                if (this.selfUser.equals(string3) || this.blocked.contains(string3)) {
                    return;
                }
                if ("FILE_BEGIN".equals(string2)) {
                    long l;
                    String[] stringArray2 = string4.split("\\|", 2);
                    if (stringArray2.length < 2) {
                        return;
                    }
                    String string5 = stringArray2[0];
                    try {
                        l = Long.parseLong(stringArray2[1].trim());
                    }
                    catch (NumberFormatException numberFormatException) {
                        if (socket2 != null) {
                            socket2.close();
                        }
                        return;
                    }
                    if (l <= 0L || l > 0x1400000L) {
                        return;
                    }
                    Path path = Path.of(System.getProperty("user.home"), "Downloads", "LocalChatReceived").toAbsolutePath().normalize();
                    Files.createDirectories(path);
                    String string6 = LocalChatApp.sanitizeIncomingFilename(string5);
                    Path path2 = path.resolve(System.currentTimeMillis() + "_" + string6).normalize();
                    if (!path2.startsWith(path)) {
                        AppLog.line("WARN", "Rejected file path outside receive dir from " + string3);
                        return;
                    }
                    try (OutputStream outputStream = Files.newOutputStream(path2, new OpenOption[0]);){
                        long l2;
                        int n;
                        byte[] byArray = new byte[8192];
                        for (l2 = l; l2 > 0L && (n = inputStream.read(byArray, 0, (int)Math.min((long)byArray.length, l2))) >= 0; l2 -= (long)n) {
                            outputStream.write(byArray, 0, n);
                        }
                        if (l2 > 0L) {
                            Files.deleteIfExists(path2);
                            AppLog.line("WARN", "Incomplete file receive from " + string3);
                            return;
                        }
                    }
                    this.handler.onCommand(new Command("FILE_DONE", string3, path2.toString() + "|" + string5), socket2.getRemoteSocketAddress());
                    return;
                }
                Command command = new Command(string2, string3, string4);
                this.handler.onCommand(command, socket2.getRemoteSocketAddress());
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    static final class Command {
        final String type;
        final String from;
        final String payload;

        Command(String string, String string2, String string3) {
            this.type = string;
            this.from = string2;
            this.payload = string3;
        }
    }

    static interface CommandHandler {
        public void onCommand(Command var1, SocketAddress var2);
    }

    static final class DiscoveryService {
        private final String selfUser;
        private final String selfTag;
        private final ConcurrentMap<String, UserProfile> users;
        private final AtomicReference<String> activitySupplier;
        private volatile boolean running = true;

        DiscoveryService(String string, String string2, ConcurrentMap<String, UserProfile> concurrentMap, AtomicReference<String> atomicReference) {
            this.selfUser = string;
            this.selfTag = string2;
            this.users = concurrentMap;
            this.activitySupplier = atomicReference;
        }

        void start() {
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            executorService.submit(this::sender);
            executorService.submit(this::receiver);
            while (this.running) {
                try {
                    Thread.sleep(300L);
                }
                catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            executorService.shutdownNow();
        }

        void stop() {
            this.running = false;
        }

        private void sender() {
            try (DatagramSocket datagramSocket = new DatagramSocket();){
                datagramSocket.setBroadcast(true);
                while (this.running) {
                    String string = this.activitySupplier != null ? this.activitySupplier.get() : "ONLINE";
                    String string2 = "HELLO;" + this.selfUser + ";" + this.selfTag + ";" + LocalChatApp.escape(string);
                    byte[] byArray = string2.getBytes(StandardCharsets.UTF_8);
                    Set<InetAddress> set = this.discoveryTargets();
                    for (InetAddress inetAddress : set) {
                        DatagramPacket datagramPacket = new DatagramPacket(byArray, byArray.length, inetAddress, 39001);
                        datagramSocket.send(datagramPacket);
                    }
                    Thread.sleep(2000L);
                }
            }
            catch (Exception exception) {
                AppLog.line("ERROR", "Discovery sender failed: " + exception.getMessage());
            }
        }

        private void receiver() {
            try (DatagramSocket datagramSocket = new DatagramSocket(null);){
                datagramSocket.setReuseAddress(true);
                datagramSocket.bind(new InetSocketAddress(39001));
                datagramSocket.setBroadcast(true);
                byte[] byArray = new byte[2048];
                while (this.running) {
                    String string;
                    DatagramPacket datagramPacket = new DatagramPacket(byArray, byArray.length);
                    datagramSocket.receive(datagramPacket);
                    String string2 = new String(datagramPacket.getData(), 0, datagramPacket.getLength(), StandardCharsets.UTF_8);
                    String[] stringArray = string2.split(";", 4);
                    if (stringArray.length < 3 || !"HELLO".equals(stringArray[0]) || this.selfUser.equals(string = stringArray[1])) continue;
                    String string3 = stringArray[2];
                    String string4 = stringArray.length > 3 ? LocalChatApp.unescape(stringArray[3]) : "ONLINE";
                    String string6 = datagramPacket.getAddress().getHostAddress();
                    this.users.compute(string, (string5, userProfile) -> {
                        if (userProfile == null) {
                            UserProfile userProfile2 = new UserProfile(string, string3, string6);
                            userProfile2.activity = string4;
                            return userProfile2;
                        }
                        userProfile.lastSeen = System.currentTimeMillis();
                        userProfile.rememberAddress(string6);
                        userProfile.activity = string4;
                        return userProfile;
                    });
                }
            }
            catch (Exception exception) {
                AppLog.line("ERROR", "Discovery receiver failed: " + exception.getMessage());
            }
        }

        private Set<InetAddress> discoveryTargets() {
            HashSet<InetAddress> hashSet = new HashSet<InetAddress>();
            try {
                Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
                while (enumeration.hasMoreElements()) {
                    NetworkInterface networkInterface = enumeration.nextElement();
                    if (!networkInterface.isUp() || networkInterface.isLoopback()) continue;
                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                        InetAddress inetAddress = interfaceAddress.getBroadcast();
                        if (inetAddress == null) continue;
                        hashSet.add(inetAddress);
                    }
                }
            }
            catch (Exception exception) {
                AppLog.line("ERROR", "Discovery target scan failed: " + exception.getMessage());
            }
            try {
                hashSet.add(InetAddress.getByName("255.255.255.255"));
            }
            catch (Exception exception) {
                // empty catch block
            }
            return hashSet;
        }
    }

    static final class RadarPanel
    extends JPanel {
        private final ConcurrentMap<String, UserProfile> users;
        private final String selfUser;
        private final Random rnd = new Random(42L);
        private final Map<String, Point> coords = new ConcurrentHashMap<String, Point>();
        private final BiConsumer<String, String> onUserAction;
        private final Predicate<String> canRequestPredicate;

        RadarPanel(ConcurrentMap<String, UserProfile> concurrentMap, String string, BiConsumer<String, String> biConsumer, Predicate<String> canRequestPredicate) {
            this.users = concurrentMap;
            this.selfUser = string;
            this.onUserAction = Objects.requireNonNull(biConsumer);
            this.canRequestPredicate = canRequestPredicate;
            this.setPreferredSize(new Dimension(420, 320));
            this.setBackground(AppUi.CHAT_BG);
            this.addMouseListener(new MouseAdapter(){
                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    if (!SwingUtilities.isRightMouseButton(mouseEvent)) {
                        return;
                    }
                    String string = RadarPanel.this.hitTestUser(mouseEvent.getX(), mouseEvent.getY());
                    if (string == null) {
                        return;
                    }
                    JPopupMenu jPopupMenu = new JPopupMenu();
                    if (RadarPanel.this.canRequestPredicate.test(string)) {
                        JMenuItem jMenuItem = new JMenuItem("Send chat request (with note)\u2026");
                        jMenuItem.addActionListener(actionEvent -> invokeRadarAction(string, "request"));
                        jPopupMenu.add(jMenuItem);
                    }
                    JMenuItem jMenuItem2 = new JMenuItem("Accept pending request");
                    jMenuItem2.addActionListener(actionEvent -> RadarPanel.this.invokeRadarAction(string, "accept"));
                    JMenuItem jMenuItem3 = new JMenuItem("Block");
                    jMenuItem3.addActionListener(actionEvent -> RadarPanel.this.invokeRadarAction(string, "block"));
                    jPopupMenu.add(jMenuItem2);
                    jPopupMenu.addSeparator();
                    jPopupMenu.add(jMenuItem3);
                    jPopupMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                }
            });
        }

        private void invokeRadarAction(String string, String string2) {
            this.onUserAction.accept(string, string2);
        }

        private String hitTestUser(int n, int n2) {
            long l = System.currentTimeMillis();
            for (UserProfile userProfile : this.users.values()) {
                Point point;
                if (userProfile.username.equals(this.selfUser) || l - userProfile.lastSeen > 7000L || (point = this.coords.get(userProfile.username)) == null || !(Math.hypot(n - point.x, n2 - point.y) <= 14.0)) continue;
                return userProfile.username;
            }
            return null;
        }

        private static class Particle {
            double x, y, dx, dy;
            int life;
            Particle(int x, int y, Random r) {
                this.x = x; this.y = y;
                this.dx = (r.nextDouble() - 0.5) * 2;
                this.dy = (r.nextDouble() - 0.5) * 2;
                this.life = 40 + r.nextInt(40);
            }
        }
        private final List<Particle> particles = new ArrayList<>();
        private final List<Integer> waves = new ArrayList<>();

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D graphics2D = (Graphics2D)graphics;
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int n = this.getWidth();
            int n2 = this.getHeight();
            int n3 = n / 2;
            int n4 = n2 / 2;
            float f = (float)Math.hypot(n, n2) * 0.6f;
            
            // Deep cosmic background
            graphics2D.setPaint(new RadialGradientPaint(n3, (float)n4, f, 
                new float[]{0.0f, 1.0f}, 
                new Color[]{AppUi.BG_PANEL, AppUi.BG_DEEP}));
            graphics2D.fillRect(0, 0, n, n2);
            
            int n5 = Math.min(n, n2) / 2 - 20;
            long now = System.currentTimeMillis();
            double d = (double)(now % 4000L) / 4000.0 * Math.PI * 2.0;
            
            // Data Waves
            if (now % 60 == 0) waves.add(0);
            graphics2D.setStroke(new BasicStroke(1.5f));
            for (int i = 0; i < waves.size(); i++) {
                int r = waves.get(i);
                float alpha = Math.max(0, 1.0f - (float)r / n5);
                graphics2D.setColor(new Color(99, 102, 241, (int)(alpha * 80)));
                graphics2D.drawOval(n3 - r, n4 - r, r * 2, r * 2);
                waves.set(i, r + 2);
            }
            waves.removeIf(r -> r > n5);
            
            // Glowing concentric rings
            for (int i = 50; i < n5; i += 60) {
                float opacity = 0.1f + (0.15f * (1.0f - (float)i/n5));
                graphics2D.setColor(new Color(99, 102, 241, (int)(opacity * 255)));
                graphics2D.drawOval(n3 - i, n4 - i, i * 2, i * 2);
            }
            
            // Scanner sweep with glow
            graphics2D.setPaint(new RadialGradientPaint(n3, n4, n5, 
                new float[]{0.0f, 1.0f}, 
                new Color[]{new Color(99, 102, 241, 120), new Color(99, 102, 241, 0)}));
            Polygon polygon = new Polygon();
            polygon.addPoint(n3, n4);
            for (int i = 0; i <= 40; ++i) {
                double d2 = d - (double)i / 40.0 * 1.2;
                polygon.addPoint(n3 + (int)((double)n5 * Math.cos(d2)), n4 + (int)((double)n5 * Math.sin(d2)));
            }
            graphics2D.fill(polygon);
            
            // Sweep line
            graphics2D.setColor(new Color(165, 180, 252));
            graphics2D.setStroke(new BasicStroke(2.0f));
            graphics2D.drawLine(n3, n4, n3 + (int)((double)n5 * Math.cos(d)), n4 + (int)((double)n5 * Math.sin(d)));
            
            // Particles
            if (rnd.nextInt(10) > 7) particles.add(new Particle(n3 + (int)((double)n5 * Math.cos(d)), n4 + (int)((double)n5 * Math.sin(d)), rnd));
            for (Particle p : particles) {
                p.x += p.dx; p.y += p.dy; p.life--;
                graphics2D.setColor(new Color(129, 140, 248, Math.min(255, p.life * 6)));
                graphics2D.fillRect((int)p.x, (int)p.y, 2, 2);
            }
            particles.removeIf(p -> p.life <= 0);
            
            // Central marker
            graphics2D.setPaint(new RadialGradientPaint(n3, n4, 15, 
                new float[]{0.0f, 1.0f}, 
                new Color[]{new Color(99, 102, 241, 200), new Color(99, 102, 241, 0)}));
            graphics2D.fillOval(n3 - 15, n4 - 15, 30, 30);
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillOval(n3 - 4, n4 - 4, 8, 8);
            
            // Peer markers
            List<UserProfile> list = this.users.values().stream()
                .filter(u -> !u.username.equals(this.selfUser))
                .filter(u -> System.currentTimeMillis() - u.lastSeen <= 7000L)
                .collect(Collectors.toList());
                
            for (UserProfile userProfile2 : list) {
                Point point = this.coords.computeIfAbsent(userProfile2.username, string -> {
                    double angle = this.rnd.nextDouble() * Math.PI * 2.0;
                    int radius = 60 + this.rnd.nextInt(Math.max(1, n5 - 80));
                    return new Point((int)((double)n3 + (double)radius * Math.cos(angle)), (int)((double)n4 + (double)radius * Math.sin(angle)));
                });
                
                double angleToPeer = Math.atan2(point.y - n4, point.x - n3);
                if (angleToPeer < 0) angleToPeer += Math.PI * 2;
                boolean active = Math.abs(d - angleToPeer) < 0.1 || Math.abs(d - (angleToPeer + Math.PI * 2)) < 0.1;
                
                int markerSize = active ? 10 : 6;
                Color c = active ? new Color(34, 197, 94) : new Color(34, 197, 94, 120);
                
                graphics2D.setPaint(new RadialGradientPaint(point.x, point.y, markerSize*2, 
                    new float[]{0.0f, 1.0f}, new Color[]{c, new Color(0,0,0,0)}));
                graphics2D.fillOval(point.x - markerSize*2, point.y - markerSize*2, markerSize*4, markerSize*4);
                
                graphics2D.setColor(active ? Color.WHITE : new Color(255, 255, 255, 180));
                graphics2D.fillOval(point.x - markerSize/2, point.y - markerSize/2, markerSize, markerSize);
                
                graphics2D.setColor(new Color(248, 250, 252, active ? 255 : 150));
                graphics2D.setFont(AppUi.FONT_SMALL.deriveFont(active ? Font.BOLD : Font.PLAIN));
                graphics2D.drawString(userProfile2.username, point.x + 12, point.y + 5);
            }
        }
    }

    static final class MainFrame
    extends JFrame {
        private static final long serialVersionUID = 1L;
        private final String selfUser;
        private final String selfTag;
        private final ConcurrentMap<String, UserProfile> users = new ConcurrentHashMap<String, UserProfile>();
        private final Set<String> blockedUsers = ConcurrentHashMap.newKeySet();
        private final Set<String> requestedUsers = ConcurrentHashMap.newKeySet();
        private final Map<String, List<ChatMessage>> privateChats = new ConcurrentHashMap<String, List<ChatMessage>>();
        private final Map<String, List<ChatMessage>> groupChats = new ConcurrentHashMap<String, List<ChatMessage>>();
        private final Map<String, String> privateChatPasswords = new ConcurrentHashMap<String, String>();
        private final RadarPanel radarPanel;
        private final DefaultListModel<String> userListModel = new DefaultListModel<String>();
        private final JList<String> userList = new JList<String>(this.userListModel);
        private final JEditorPane chatArea = new JEditorPane();
        private String chatHistoryInnerHtml = "";
        private final JTextField messageField = new JTextField();
        private final JComboBox<String> modeBox = new JComboBox<String>();
        private final JTextField groupField = new JTextField("general");
        private final ExecutorService ioPool = Executors.newCachedThreadPool();
        private ScheduledExecutorService scheduler;
        private DiscoveryService discoveryService;
        private ChatServer chatServer;
        private String selectedUser = null;
        private String activeGroup = "general";
        private StorageManager storageManager;
        private final AtomicReference<String> activityRef = new AtomicReference<String>("ONLINE");
        private final JLabel typingLabel = new JLabel(" ");
        private final JTextField searchField = new JTextField();
        private final JTextField directorySearchField = new JTextField();
        private final JLabel chatContextLabel = new JLabel(" ");
        private String lastCustomGroupRoom = "general";
        private boolean suppressChatSessionSelectionEvents = false;
        private final JCheckBox notificationsEnabled = new JCheckBox("Popup notifications for new messages", true);
        private final DefaultListModel<String> chatSessionsModel = new DefaultListModel<String>();
        private final JList<String> chatSessionsList = new JList<String>(this.chatSessionsModel);
        private boolean privateSessionsExpanded = true;
        private boolean publicSessionsExpanded = true;
        private boolean groupSessionsExpanded = true;
        private boolean keepActiveGroupOnNextSwitch = false;
        private final AtomicLong lastSendMs = new AtomicLong(0L);
        private final AtomicLong lastTypingNotify = new AtomicLong(0L);
        private final transient MessageStats messageStats;
        private final Map<String, Long> pendingIncomingRequestUntil = new ConcurrentHashMap<String, Long>();
        private final Map<String, String> pendingIncomingRequestNote = new ConcurrentHashMap<String, String>();
        private final Map<String, Long> pendingOutgoingRequests = new ConcurrentHashMap<String, Long>();
        private volatile transient Point lastChatClickPoint = new Point(18, 18);
        private volatile transient ScheduledFuture<?> typingStopTask;
        private static final String EMOJI_CHOICES = "\ud83d\ude00 \ud83d\ude03 \ud83d\ude04 \ud83d\ude01 \ud83d\ude05 \ud83d\ude02 \ud83e\udd23 \u2764\ufe0f \ud83d\udc4d \ud83d\udc4e \ud83d\ude4f \ud83d\udd25 \u2728 \ud83c\udf89 \ud83d\udc4b \ud83e\udd1d \ud83d\udcac \ud83d\udcce \u2705 \u274c \ud83e\udd14 \ud83d\ude2e \ud83d\ude22 \ud83c\udf82 \ud83c\udf1f \u2b50 \ud83d\udca1";

        MainFrame(String string, String string2) {
            this.selfUser = string;
            this.selfTag = string2 + ":" + string;
            this.messageStats = new MessageStats(StorageManager.sanitize(string));
            this.setTitle("Local Chat v1.4.0 \u2014 " + this.selfTag);
            this.setSize(1280, 900);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(0);
            this.radarPanel = new RadarPanel(this.users, string, this::onRadarUserAction, username -> !this.requestedUsers.contains(username) && !this.privateChats.containsKey(username));
            this.buildUi();
            this.wireEvents();
            this.setupRuntime();
        }

        private void buildUi() {
            Color color = AppUi.BG_DEEP;
            Color color2 = AppUi.FG;
            this.getContentPane().setBackground(color);
            JPanel jPanel = new JPanel(new BorderLayout(12, 12));
            jPanel.setBackground(color);
            jPanel.setBorder(new EmptyBorder(8, 12, 12, 12));
            JPanel jPanel2 = new JPanel(new BorderLayout(12, 0));
            jPanel2.setOpaque(true);
            jPanel2.setBackground(AppUi.BG_HEADER);
            jPanel2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 30)), 
                new EmptyBorder(12, 16, 12, 16)));
            JPanel jPanel3 = new JPanel();
            jPanel3.setLayout(new BoxLayout(jPanel3, 1));
            jPanel3.setOpaque(false);
            JLabel jLabel = new JLabel("Local Chat");
            jLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
            jLabel.setForeground(AppUi.ACCENT);
            jPanel3.add(jLabel);
            jPanel3.add(Box.createVerticalStrut(2));
            JLabel jLabelVersion = new JLabel("v" + AppConfig.APP_VERSION);
            jLabelVersion.setFont(new Font("Segoe UI", Font.BOLD, 10));
            jLabelVersion.setForeground(AppUi.ACCENT_DIM);
            jPanel3.add(jLabelVersion);
            jPanel3.add(Box.createVerticalStrut(4));
            JLabel jLabel2 = new JLabel("Class & lab use \u00b7 same Wi-Fi or LAN required");
            jLabel2.setFont(AppUi.FONT_SMALL);
            jLabel2.setForeground(AppUi.FG_MUTED);
            jPanel3.add(jLabel2);
            JPanel jPanel4 = new JPanel(new FlowLayout(0, 20, 0));
            jPanel4.setOpaque(false);
            jPanel4.add(jPanel3);
            JLabel jLabel3 = new JLabel(this.selfTag);
            jLabel3.setFont(AppUi.FONT_SMALL);
            jLabel3.setForeground(AppUi.FG_MUTED);
            jPanel4.add(jLabel3);
            jPanel2.add((Component)jPanel4, "West");
            
            JPanel jPanelHeaderRight = new JPanel(new FlowLayout(2, 12, 0));
            jPanelHeaderRight.setOpaque(false);
            
            JButton btnTheme = new JButton(AppUi.isDark ? "\ud83c\udf19" : "\u2600\ufe0f");
            btnTheme.setToolTipText("Toggle Light/Dark Mode");
            AppUi.styleHeaderButton(btnTheme, AppUi.ACCENT_HOVER);
            btnTheme.addActionListener(e -> {
                AppUi.setTheme(!AppUi.isDark);
                this.dispose();
                new MainFrame(this.selfUser, this.selfUser).setVisible(true);
            });
            
            JButton btnLogout = new JButton("\ud83d\udeaa Logout");
            btnLogout.setToolTipText("Logout / Switch User");
            AppUi.styleHeaderButton(btnLogout, new Color(239, 68, 68));
            btnLogout.addActionListener(e -> {
                shutdown();
                dispose();
                new LoginFrame().setVisible(true);
            });
            
            jPanelHeaderRight.add(btnTheme);
            jPanelHeaderRight.add(btnLogout);
            
            jPanel2.add((Component)jPanelHeaderRight, "East");
            JPanel jPanel5 = new JPanel(new BorderLayout(10, 10));
            jPanel5.setOpaque(false);
            jPanel5.setPreferredSize(new Dimension(330, 0));
            JPanel jPanel6 = new JPanel(new BorderLayout());
            jPanel6.setOpaque(true);
            jPanel6.setBackground(AppUi.BG_PANEL);
            jPanel6.setBorder(AppUi.titled("Network \u00b7 Discovery radar", AppUi.BORDER));
            jPanel6.setPreferredSize(new Dimension(0, 250));
            jPanel6.add((Component)this.radarPanel, "Center");
            JPanel jPanel7 = new JPanel(new BorderLayout(8, 8));
            jPanel7.setOpaque(true);
            jPanel7.setBackground(AppUi.BG_PANEL);
            jPanel7.setBorder(AppUi.titled("LAN peers", AppUi.BORDER));
            JPanel jPanelDirSearch = new JPanel(new BorderLayout(6, 0));
            jPanelDirSearch.setOpaque(false);
            JLabel jLabelDirFind = new JLabel("Search");
            jLabelDirFind.setForeground(AppUi.FG_MUTED);
            jLabelDirFind.setFont(AppUi.FONT_SMALL);
            jPanelDirSearch.add((Component)jLabelDirFind, "West");
            AppUi.styleTextField(this.directorySearchField);
            this.directorySearchField.setToolTipText("Filter peers by name or address");
            jPanelDirSearch.add((Component)this.directorySearchField, "Center");
            jPanel7.add((Component)jPanelDirSearch, "North");
            JScrollPane jScrollPane = new JScrollPane(this.userList);
            this.styleUserPeerList();
            AppUi.styleScroll(jScrollPane);
            jPanel7.add((Component)jScrollPane, "Center");
            JPanel jPanel8 = new JPanel(new FlowLayout(0, 6, 0));
            jPanel8.setOpaque(false);
            JButton jButton = new JButton("\ud83d\udce9 Request\u2026");
            jButton.setToolTipText("Send chat request with optional note");
            JButton jButton2 = new JButton("\ud83d\udeab Block");
            jButton2.setToolTipText("Block selected user");
            JButton jButton3 = new JButton("\ud83d\udd13 Unblock");
            jButton3.setToolTipText("Unblock selected user");
            for (JButton jComponent22 : new JButton[]{jButton, jButton2, jButton3}) {
                AppUi.styleSecondaryButton(jComponent22);
            }
            jPanel8.add(jButton);
            jPanel8.add(jButton2);
            jPanel8.add(jButton3);
            jPanel7.add((Component)jPanel8, "South");
            JPanel jPanel9 = new JPanel(new BorderLayout(0, 10));
            jPanel9.setOpaque(false);
            
            AppUi.GlassPanel glassRadar = new AppUi.GlassPanel(0.12f);
            glassRadar.setLayout(new BorderLayout());
            glassRadar.add((Component)jPanel6, "Center");
            jPanel9.add((Component)glassRadar, "North");
            
            AppUi.GlassPanel glassPeers = new AppUi.GlassPanel(0.12f);
            glassPeers.setLayout(new BorderLayout());
            glassPeers.add((Component)jPanel7, "Center");
            jPanel9.add((Component)glassPeers, "Center");
            
            jPanel5.add((Component)jPanel9, "Center");
            this.notificationsEnabled.setOpaque(false);
            this.notificationsEnabled.setForeground(color2);
            jPanel5.add((Component)this.notificationsEnabled, "South");
            this.modeBox.removeAllItems();
            this.modeBox.addItem("Private");
            this.modeBox.addItem("Public");
            this.modeBox.addItem("Group");
            AppUi.styleCombo(this.modeBox);
            AppUi.styleTextField(this.groupField);
            this.groupField.setColumns(12);
            JPanel jPanel10 = new JPanel(new BorderLayout(10, 10));
            jPanel10.setOpaque(false);
            JPanel jPanel11 = new JPanel(new BorderLayout(0, 4));
            jPanel11.setOpaque(true);
            jPanel11.setBackground(AppUi.BG_PANEL);
            jPanel11.setBorder(new EmptyBorder(8, 10, 8, 10));
            JPanel jPanel11Top = new JPanel(new BorderLayout(8, 0));
            jPanel11Top.setOpaque(false);
            JLabel jLabel5 = new JLabel("Chat");
            jLabel5.setForeground(AppUi.FG);
            jLabel5.setFont(AppUi.FONT_HEAD);
            jPanel11Top.add((Component)jLabel5, "West");
            this.chatContextLabel.setForeground(AppUi.ACCENT_HOVER);
            this.chatContextLabel.setFont(AppUi.FONT_SMALL.deriveFont(Font.BOLD));
            this.chatContextLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            jPanel11Top.add((Component)this.chatContextLabel, "East");
            jPanel11.add((Component)jPanel11Top, "North");
            this.typingLabel.setForeground(AppUi.ACCENT_HOVER);
            this.typingLabel.setFont(AppUi.FONT_SMALL);
            this.chatArea.setContentType("text/html");
            this.chatArea.setEditable(false);
            this.chatArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            this.chatArea.setBackground(AppUi.CHAT_BG);
            this.chatArea.setForeground(color2);
            this.chatArea.setCaretColor(AppUi.ACCENT);
            this.chatArea.setBorder(new EmptyBorder(0, 0, 0, 0));
            JScrollPane jScrollPane2 = new JScrollPane(this.chatArea);
            jScrollPane2.getViewport().setBackground(new Color(11, 20, 26));
            AppUi.styleScroll(jScrollPane2);
            JPanel jPanel12 = new JPanel(new BorderLayout(6, 6));
            jPanel12.setOpaque(false);
            jPanel12.add((Component)jPanel11, "North");
            jPanel12.add((Component)jScrollPane2, "Center");
            jPanel10.add((Component)jPanel12, "Center");
            JPanel jPanel14 = new JPanel(new BorderLayout());
            jPanel14.setOpaque(true);
            jPanel14.setBackground(AppUi.BG_PANEL);
            jPanel14.setBorder(AppUi.titled("Chats", AppUi.BORDER));
            AppUi.GlassPanel glassSessions = new AppUi.GlassPanel(0.12f);
            glassSessions.setLayout(new BorderLayout());
            glassSessions.add((Component)jPanel14, "Center");
            jPanel5.add((Component)glassSessions, "North");
            JPanel jPanel15 = new JPanel(new BorderLayout(8, 8));
            jPanel15.setOpaque(true);
            jPanel15.setBackground(AppUi.BG_PANEL);
            jPanel15.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(255, 255, 255, 20)), 
                new EmptyBorder(16, 16, 16, 16)));
            JPanel jPanel16 = new JPanel(new FlowLayout(0, 8, 4));
            jPanel16.setOpaque(false);
            JLabel jLabel6 = new JLabel("Mode");
            jLabel6.setForeground(AppUi.FG_MUTED);
            jPanel16.add(jLabel6);
            jPanel16.add(this.modeBox);
            JLabel jLabel7 = new JLabel("Room");
            jLabel7.setForeground(AppUi.FG_MUTED);
            jPanel16.add(jLabel7);
            jPanel16.add(this.groupField);
            JButton jButton5 = new JButton("\u2705 Apply");
            jButton5.setToolTipText("Apply room name and refresh view (Group mode)");
            AppUi.styleSecondaryButton(jButton5);
            jPanel16.add(jButton5);
            JButton jButtonGroup = new JButton("\ud83d\udc65 Group...");
            jButtonGroup.setToolTipText("Create or open a group and invite users");
            AppUi.styleSecondaryButton(jButtonGroup);
            jPanel16.add(jButtonGroup);
            jPanel15.add((Component)jPanel16, "North");
            JPanel jPanel17 = new JPanel(new BorderLayout(8, 0));
            jPanel17.setOpaque(false);
            JPanel jPanelComposeLeft = new JPanel(new FlowLayout(0, 6, 0));
            jPanelComposeLeft.setOpaque(false);
            JButton jButton6 = new JButton();
            JButton jButton7 = new JButton();
            jButton6.setToolTipText("Emoji");
            jButton7.setToolTipText("Attach a file");
            AppUi.styleComposerIconButton(jButton6, "\ud83d\ude0a");
            AppUi.styleComposerIconButton(jButton7, "\ud83d\udcce");
            jPanelComposeLeft.add(jButton6);
            jPanelComposeLeft.add(jButton7);
            AppUi.styleTextField(this.messageField);
            this.messageField.setFont(AppUi.FONT_EMOJI.deriveFont(14.0f));
            JButton jButton8 = new JButton("\ud83d\udce4 Send");
            AppUi.stylePrimaryButton(jButton8, AppUi.ACCENT_DIM);
            jPanel17.add((Component)jPanelComposeLeft, "West");
            jPanel17.add((Component)this.messageField, "Center");
            jPanel17.add((Component)jButton8, "East");
            jPanel15.add((Component)jPanel17, "South");
            jPanel10.add((Component)jPanel15, "South");
            JPanel jPanel18 = new JPanel(new BorderLayout(0, 0));
            jPanel18.setOpaque(true);
            jPanel18.setBackground(AppUi.BG_PANEL);
            jPanel18.setBorder(BorderFactory.createCompoundBorder(AppUi.titled("Tools", AppUi.BORDER), new EmptyBorder(12, 14, 14, 14)));
            jPanel18.setPreferredSize(new Dimension(212, 0));
            JPanel jPanelToolsInner = new JPanel();
            jPanelToolsInner.setLayout(new BoxLayout(jPanelToolsInner, 1));
            jPanelToolsInner.setOpaque(true);
            jPanelToolsInner.setBackground(AppUi.BG_PANEL);
            JLabel jLabelFind = new JLabel("Find in transcript");
            jLabelFind.setAlignmentX(0.0f);
            jLabelFind.setForeground(AppUi.FG_MUTED);
            jLabelFind.setFont(AppUi.FONT_SMALL);
            jPanelToolsInner.add(jLabelFind);
            jPanelToolsInner.add(Box.createVerticalStrut(6));
            JPanel jPanelFindRow = new JPanel(new BorderLayout(6, 0));
            jPanelFindRow.setAlignmentX(0.0f);
            jPanelFindRow.setOpaque(false);
            jPanelFindRow.setMaximumSize(new Dimension(32767, 36));
            AppUi.styleTextField(this.searchField);
            this.searchField.setToolTipText("Filter lines in the current chat (Ctrl+F)");
            JButton jButton4 = new JButton("Find");
            AppUi.styleSecondaryButton(jButton4);
            jPanelFindRow.add((Component)this.searchField, "Center");
            jPanelFindRow.add((Component)jButton4, "East");
            jPanelToolsInner.add(jPanelFindRow);
            jPanelToolsInner.add(Box.createVerticalStrut(14));
            JButton jButton9 = new JButton("Save chat (Base64)");
            JButton jButton10 = new JButton("Edit my message\u2026");
            JButton jButton11 = new JButton("Dashboard");
            JButton jButton12 = new JButton("Report\u2026");
            JButton jButton14 = new JButton("Refresh");
            JButton jButton15 = new JButton("Delete local data");
            for (JButton jButton16 : new JButton[]{jButton9, jButton10, jButton11, jButton12, jButton14, jButton15}) {
                jButton16.setAlignmentX(0.0f);
                jButton16.setMaximumSize(new Dimension(Integer.MAX_VALUE, jButton16.getPreferredSize().height + 8));
                AppUi.styleGlowToolButton(jButton16);
                jPanelToolsInner.add(jButton16);
                jPanelToolsInner.add(Box.createVerticalStrut(10));
            }
            JScrollPane jScrollTools = new JScrollPane(jPanelToolsInner);
            jScrollTools.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollTools.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            jScrollTools.getVerticalScrollBar().setUnitIncrement(20);
            jScrollTools.setBorder(null);
            jScrollTools.getViewport().setBackground(AppUi.BG_PANEL);
            AppUi.styleScroll(jScrollTools);
            jPanel18.add((Component)jScrollTools, "Center");
            JPanel jPanel19 = new JPanel(new BorderLayout(10, 0));
            jPanel19.setOpaque(false);
            jPanel19.add((Component)jPanel5, "West");
            jPanel19.add((Component)jPanel10, "Center");
            jPanel19.add((Component)jPanel18, "East");
            JPanel jPanel20 = new JPanel(new BorderLayout());
            jPanel20.setOpaque(true);
            jPanel20.setBackground(AppUi.BG_HEADER);
            jPanel20.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppUi.BORDER), new EmptyBorder(8, 12, 8, 12)));
            JLabel jLabel8 = new JLabel(AppConfig.statusFooterLine());
            jLabel8.setFont(AppUi.FONT_SMALL);
            jLabel8.setForeground(AppUi.FG);
            jPanel20.add((Component)jLabel8, "West");
            jPanel.add((Component)jPanel2, "North");
            jPanel.add((Component)jPanel19, "Center");
            jPanel.add((Component)jPanel20, "South");
            this.add(jPanel);
            jButton.addActionListener(actionEvent -> this.requestChat());
            jButton2.addActionListener(actionEvent -> this.blockSelectedUser());
            jButton3.addActionListener(actionEvent -> this.unblockSelectedUser());
            jButton5.addActionListener(actionEvent -> this.switchView());
            jButtonGroup.addActionListener(actionEvent -> this.createGroupFromPicker());
            jButton8.addActionListener(actionEvent -> this.sendCurrentMessage());
            jButton9.addActionListener(actionEvent -> this.exportActive());
            jButton15.addActionListener(actionEvent -> this.deleteData());
            jButton14.addActionListener(actionEvent -> {
                this.refreshUserList();
                this.rebuildChatSessionsList();
            });
            jButton4.addActionListener(actionEvent -> this.runSearchInChat());
            jButton6.addActionListener(actionEvent -> this.showEmojiPicker());
            jButton7.addActionListener(actionEvent -> this.sendFileAttachment());
            jButton10.addActionListener(actionEvent -> this.editOwnMessage());
            jButton11.addActionListener(actionEvent -> this.showDashboard());
            jButton12.addActionListener(actionEvent -> this.showWriteReport());
        }

        private void styleUserPeerList() {
            AppUi.styleList(this.userList);
            this.userList.setCellRenderer(new DefaultListCellRenderer(){

                @Override
                public Component getListCellRendererComponent(JList<?> jList, Object object, int n, boolean bl, boolean bl2) {
                    JLabel jLabel = (JLabel)super.getListCellRendererComponent(jList, object, n, bl, bl2);
                    jLabel.setOpaque(true);
                    jLabel.setBorder(new EmptyBorder(4, 12, 4, 12));
                    if (bl) {
                        jLabel.setBackground(AppUi.ACCENT_DIM);
                        jLabel.setForeground(Color.WHITE);
                    } else {
                        jLabel.setBackground(n % 2 == 0 ? AppUi.BG_FIELD : AppUi.BG_ZEBRA);
                        jLabel.setForeground(AppUi.FG);
                    }
                    String string = object != null ? object.toString() : "";
                    int n2 = string.indexOf(" \u00b7 ");
                    String string2 = n2 > 0 ? string.substring(0, n2).trim() : string.trim();
                    UserProfile userProfile = MainFrame.this.users.get(string2);
                    jLabel.setToolTipText(userProfile != null ? MainFrame.this.formatUserTooltip(userProfile) : null);
                    return jLabel;
                }
            });
        }

        private void wireEvents() {
            this.userList.addListSelectionListener(listSelectionEvent -> {
                if (!listSelectionEvent.getValueIsAdjusting()) {
                    String string = this.userList.getSelectedValue();
                    if (string == null) {
                        return;
                    }
                    int n = string.indexOf(" \u00b7 ");
                    if (n < 0) {
                        n = string.indexOf(" \u2014 ");
                    }
                    this.selectedUser = n > 0 ? string.substring(0, n).trim() : string.trim();
                    if ("Private".equals(this.modeBox.getSelectedItem())) {
                        this.renderPrivateChat();
                        this.updateChatContextLabel();
                        this.syncConversationListSelection();
                    }
                }
            });
            this.userList.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (!"Public".equals(MainFrame.this.modeBox.getSelectedItem())) {
                        return;
                    }
                    int n = MainFrame.this.userList.locationToIndex(mouseEvent.getPoint());
                    if (n < 0) {
                        return;
                    }
                    String string = (String)MainFrame.this.userListModel.getElementAt(n);
                    if (string == null || string.startsWith("Public chats") || string.startsWith("Private chats")) {
                        return;
                    }
                    int n2 = string.indexOf(" \u00b7 ");
                    if (n2 < 0) {
                        n2 = string.indexOf(" \u2014 ");
                    }
                    String string2 = n2 > 0 ? string.substring(0, n2).trim() : string.trim();
                    UserProfile userProfile = (UserProfile)MainFrame.this.users.get(string2);
                    if (userProfile == null || MainFrame.this.selfUser.equals(userProfile.username)) {
                        return;
                    }
                    if (SwingUtilities.isRightMouseButton(mouseEvent) || mouseEvent.getClickCount() >= 2) {
                        MainFrame.this.showUserActionMenu(userProfile.username, MainFrame.this.userList, mouseEvent.getX(), mouseEvent.getY());
                    }
                }
            });
            this.chatSessionsList.addListSelectionListener(listSelectionEvent -> {
                if (!listSelectionEvent.getValueIsAdjusting()) {
                    if (this.suppressChatSessionSelectionEvents) {
                        return;
                    }
                    String string = this.chatSessionsList.getSelectedValue();
                    if (string == null) {
                        return;
                    }
                    String string2 = string.trim();
                    if (string2.startsWith("Private chats")) {
                        this.privateSessionsExpanded = !this.privateSessionsExpanded;
                        this.rebuildChatSessionsList();
                        return;
                    }
                    if (string2.startsWith("Public chats")) {
                        this.publicSessionsExpanded = !this.publicSessionsExpanded;
                        this.rebuildChatSessionsList();
                        return;
                    }
                    if (string2.startsWith("Group chats")) {
                        this.groupSessionsExpanded = !this.groupSessionsExpanded;
                        this.rebuildChatSessionsList();
                        return;
                    }
                    if (string2.startsWith("Private: ")) {
                        this.modeBox.setSelectedItem("Private");
                        this.selectedUser = string2.substring("Private: ".length()).trim();
                        this.switchView();
                    } else if (string2.startsWith("Group: ")) {
                        this.keepActiveGroupOnNextSwitch = true;
                        this.modeBox.setSelectedItem("Group");
                        this.groupField.setText(string2.substring("Group: ".length()).trim());
                        this.activeGroup = this.groupField.getText().trim().toLowerCase(Locale.ROOT);
                        this.switchView();
                    } else if (string2.startsWith("Public")) {
                        this.modeBox.setSelectedItem("Public");
                        this.activeGroup = "public";
                        this.groupField.setText("public");
                        this.switchView();
                    }
                }
            });
            this.modeBox.addActionListener(actionEvent -> {
                this.updateActivityForDiscovery();
                this.switchView();
            });
            this.groupField.addActionListener(actionEvent -> this.updateActivityForDiscovery());
            this.messageField.addActionListener(actionEvent -> this.sendCurrentMessage());
            this.chatArea.addMouseListener(new MouseAdapter(){
                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                    MainFrame.this.lastChatClickPoint = mouseEvent.getPoint();
                }
            });
            this.chatArea.addHyperlinkListener(hyperlinkEvent -> {
                if (hyperlinkEvent.getEventType() != HyperlinkEvent.EventType.ACTIVATED) {
                    return;
                }
                String string = hyperlinkEvent.getDescription();
                if (string == null || !string.startsWith("user:")) {
                    return;
                }
                Point point = this.lastChatClickPoint == null ? new Point(18, 18) : this.lastChatClickPoint;
                this.showUserActionMenu(string.substring("user:".length()), this.chatArea, point.x + 10, point.y + 6);
            });
            this.messageField.getDocument().addDocumentListener(new DocumentListener(){
                private void ping() {
                    MainFrame.this.scheduleTypingNotify();
                }

                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    this.ping();
                }

                @Override
                public void removeUpdate(DocumentEvent documentEvent) {
                    this.ping();
                }

                @Override
                public void changedUpdate(DocumentEvent documentEvent) {
                    this.ping();
                }
            });
            InputMap inputMap = this.rootPane.getInputMap(1);
            ActionMap actionMap = this.rootPane.getActionMap();
            inputMap.put(KeyStroke.getKeyStroke(70, 128), "find");
            actionMap.put("find", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    MainFrame.this.searchField.requestFocusInWindow();
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(83, 128), "save");
            actionMap.put("save", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    MainFrame.this.exportActive();
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(10, 128), "send");
            actionMap.put("send", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    MainFrame.this.sendCurrentMessage();
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(82, 128), "report");
            actionMap.put("report", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    MainFrame.this.showWriteReport();
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(68, 128), "dash");
            actionMap.put("dash", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    MainFrame.this.showDashboard();
                }
            });
            inputMap.put(KeyStroke.getKeyStroke(69, 128), "emoji");
            actionMap.put("emoji", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    MainFrame.this.showEmojiPicker();
                }
            });
            this.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    MainFrame.this.shutdown();
                    MainFrame.this.dispose();
                    System.exit(0);
                }
            });
            this.directorySearchField.getDocument().addDocumentListener(new DocumentListener(){
                private void ping() {
                    MainFrame.this.refreshUserList();
                }

                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    this.ping();
                }

                @Override
                public void removeUpdate(DocumentEvent documentEvent) {
                    this.ping();
                }

                @Override
                public void changedUpdate(DocumentEvent documentEvent) {
                    this.ping();
                }
            });
        }

        private void setupRuntime() {
            AppLog.line("INFO", "Session start " + this.selfTag);
            this.storageManager = new StorageManager(this.selfUser);
            this.privateChats.putAll(this.storageManager.loadPrivateChats());
            this.groupChats.putAll(this.storageManager.loadGroupChats());
            this.blockedUsers.addAll(this.storageManager.loadBlockedUsers());
            this.requestedUsers.addAll(this.storageManager.loadRequestedUsers());
            this.privateChatPasswords.putAll(this.storageManager.loadPrivateChatPasswords());
            this.scheduler = Executors.newScheduledThreadPool(4);
            this.discoveryService = new DiscoveryService(this.selfUser, this.selfTag, this.users, this.activityRef);
            this.chatServer = new ChatServer(this.selfUser, this.blockedUsers, this::onCommandReceived);
            this.ioPool.submit(this.discoveryService::start);
            this.ioPool.submit(this.chatServer::start);
            this.scheduler.scheduleAtFixedRate(this::refreshUserList, 0L, 2L, TimeUnit.SECONDS);
            this.scheduler.scheduleAtFixedRate(this.radarPanel::repaint, 0L, 100L, TimeUnit.MILLISECONDS);
            this.scheduler.scheduleAtFixedRate(this::persistAll, 4L, 4L, TimeUnit.SECONDS);
            this.scheduler.scheduleAtFixedRate(this::updateActivityForDiscovery, 0L, 1L, TimeUnit.SECONDS);
            String string = this.groupField.getText().trim();
            this.lastCustomGroupRoom = string.isEmpty() ? "general" : string;

            this.rebuildChatSessionsList();
            this.updateActivityForDiscovery();
            this.switchView();
        }

        private void refreshUserList() {
            SwingUtilities.invokeLater(() -> {
                this.userListModel.clear();
                long l = System.currentTimeMillis();
                String string = this.directorySearchField.getText().trim().toLowerCase(Locale.ROOT);
                this.users.values().stream().filter(userProfile -> !userProfile.username.equals(this.selfUser)).filter(userProfile -> l - userProfile.lastSeen <= 7000L).filter(userProfile -> {
                    if (string.isEmpty()) {
                        return true;
                    }
                    return userProfile.username.toLowerCase(Locale.ROOT).contains(string) || userProfile.hostAddress.toLowerCase(Locale.ROOT).contains(string);
                }).sorted(Comparator.comparing(userProfile -> userProfile.username)).forEach(userProfile -> this.userListModel.addElement(this.formatUserLine(userProfile)));
            });
        }

        private String userProfileStatusLine(UserProfile userProfile, boolean bl) {
            String string = userProfile.activity == null ? "ONLINE" : userProfile.activity;
            if ("PRIVATE_HIDDEN".equals(string) || string.startsWith("PRIVATE")) {
                string = "Online";
            }
            if (this.nowOffline(userProfile)) {
                return "Offline";
            }
            if (string.startsWith("IN_GROUP:")) {
                String string3 = string.substring("IN_GROUP:".length());
                return bl ? string3 : "In group " + string3;
            }
            if ("IN_PUBLIC".equals(string)) {
                return bl ? "Public" : "In public chat";
            }
            if ("ONLINE".equals(string)) {
                return "Online";
            }
            return string;
        }

        private String formatUserLine(UserProfile userProfile) {
            return userProfile.username + " \u00b7 " + this.userProfileStatusLine(userProfile, true);
        }

        private String formatUserTooltip(UserProfile userProfile) {
            return userProfile.username + " \u2014 " + this.userProfileStatusLine(userProfile, false) + " @ " + userProfile.hostAddress;
        }

        private boolean nowOffline(UserProfile userProfile) {
            return System.currentTimeMillis() - userProfile.lastSeen > 7000L;
        }

        private void updateActivityForDiscovery() {
            Object object = this.modeBox.getSelectedItem();
            if ("Private".equals(object) && this.selectedUser != null) {
                this.activityRef.set("PRIVATE_HIDDEN");
            } else if ("Public".equals(object) || "Group".equals(object)) {
                if (this.activeGroup != null && !"public".equalsIgnoreCase(this.activeGroup)) {
                    this.activityRef.set("IN_GROUP:" + LocalChatApp.clipPlain(this.activeGroup, 64));
                } else {
                    this.activityRef.set("IN_PUBLIC");
                }
            } else {
                this.activityRef.set("ONLINE");
            }
        }

        private void rebuildChatSessionsList() {
            SwingUtilities.invokeLater(() -> {
                String string = this.chatSessionsList.getSelectedValue();
                this.chatSessionsModel.clear();
                this.chatSessionsModel.addElement(this.privateSessionsExpanded ? "Private chats \u25bc" : "Private chats \u25b6");
                if (this.privateSessionsExpanded) {
                    this.privateChats.keySet().stream().sorted().forEach(string2 -> this.chatSessionsModel.addElement("  Private: " + string2));
                }
                this.chatSessionsModel.addElement(this.publicSessionsExpanded ? "Public chats \u25bc" : "Public chats \u25b6");
                if (this.publicSessionsExpanded) {
                    this.chatSessionsModel.addElement("  Public");
                }
                this.chatSessionsModel.addElement(this.groupSessionsExpanded ? "Group chats \u25bc" : "Group chats \u25b6");
                if (this.groupSessionsExpanded) {
                    this.groupChats.keySet().stream().filter(string2 -> !"public".equalsIgnoreCase(string2)).sorted().forEach(string2 -> this.chatSessionsModel.addElement("  Group: " + string2));
                }
                if (string != null && this.selectChatSessionRow(string)) {
                    return;
                }
                this.syncConversationListSelection();
            });
        }

        private boolean selectChatSessionRow(String string) {
            if (string == null) {
                return false;
            }
            this.suppressChatSessionSelectionEvents = true;
            try {
                for (int i = 0; i < this.chatSessionsModel.getSize(); ++i) {
                    String string2 = this.chatSessionsModel.getElementAt(i);
                    if (string.equals(string2) || string.trim().equals(string2.trim())) {
                        this.chatSessionsList.setSelectedIndex(i);
                        return true;
                    }
                }
            } finally {
                this.suppressChatSessionSelectionEvents = false;
            }
            return false;
        }

        private void syncConversationListSelection() {
            Object object = this.modeBox.getSelectedItem();
            String string = null;
            if ("Private".equals(object) && this.selectedUser != null) {
                string = "Private: " + this.selectedUser;
            } else if ("Public".equals(object)) {
                string = "Public";
            } else if ("Group".equals(object)) {
                string = "Group: " + this.activeGroup;
            }
            if (string != null) {
                this.selectChatSessionRow(string);
            }
        }

        private void updateChatContextLabel() {
            Object object = this.modeBox.getSelectedItem();
            if ("Private".equals(object)) {
                this.chatContextLabel.setText(this.selectedUser == null ? "Private \u2014 pick a peer" : "Private \u00b7 " + this.selectedUser);
            } else if ("Public".equals(object)) {
                this.chatContextLabel.setText(this.activeGroup != null && !"public".equalsIgnoreCase(this.activeGroup) ? "Group \u00b7 " + this.activeGroup : "Public");
            } else if ("Group".equals(object)) {
                this.chatContextLabel.setText("Group \u00b7 " + this.activeGroup);
            } else {
                this.chatContextLabel.setText(" ");
            }
        }

        void onRadarUserAction(String string, String string2) {
            if (string == null) {
                return;
            }
            this.selectedUser = string;
            switch (string2) {
                case "block": {
                    this.blockedUsers.add(string);
                    this.appendSystem("Blocked " + string + " (radar)");
                    break;
                }
                case "request": {
                    this.requestChatWithNote(string);
                    break;
                }
                case "accept": {
                    this.acceptRequestFrom(string);
                    break;
                }
            }
        }

        private void requestChatWithNote(String string) {
            this.selectedUser = string;
            this.requestChat();
        }

        private void acceptRequestFrom(String string) {
            Long l = this.pendingIncomingRequestUntil.get(string);
            if (l != null && System.currentTimeMillis() > l) {
                JOptionPane.showMessageDialog(this, "That request has expired (1 hour).");
                this.pendingIncomingRequestUntil.remove(string);
                this.pendingIncomingRequestNote.remove(string);
                return;
            }
            String string2 = this.pendingIncomingRequestNote.get(string);
            this.selectedUser = string;
            if (string2 != null && string2.contains("Group invite: room \"")) {
                int n = string2.indexOf("room \"") + 6;
                int n2 = string2.indexOf("\"", n);
                if (n2 > n) {
                    String string3 = string2.substring(n, n2);
                    this.activeGroup = string3.toLowerCase(Locale.ROOT);
                    this.groupField.setText(string3);
                    this.modeBox.setSelectedItem("Group");
                    this.keepActiveGroupOnNextSwitch = true;
                } else {
                    this.modeBox.setSelectedItem("Private");
                }
            } else {
                this.modeBox.setSelectedItem("Private");
            }
            this.pendingIncomingRequestUntil.remove(string);
            this.pendingIncomingRequestNote.remove(string);
            this.switchView();
            this.appendSystem("Accepted chat with " + string);
        }

        private static int wordCount(String string) {
            if (string == null || string.isBlank()) {
                return 0;
            }
            return string.trim().split("\\s+").length;
        }

        private void requestChat() {
            if (this.selectedUser == null) {
                JOptionPane.showMessageDialog(this, "Select a user first (list or radar).");
                return;
            }
            if (this.privateChats.containsKey(this.selectedUser)) {
                JOptionPane.showMessageDialog(this, "You are already connected to " + this.selectedUser);
                return;
            }
            if (this.requestedUsers.contains(this.selectedUser)) {
                JOptionPane.showMessageDialog(this, "You have already sent a request to " + this.selectedUser + ". You can only request once.");
                return;
            }
            Long pendingUntil = this.pendingOutgoingRequests.get(this.selectedUser);
            if (pendingUntil != null && System.currentTimeMillis() < pendingUntil) {
                JOptionPane.showMessageDialog(this, "A request to " + this.selectedUser + " is already pending.");
                return;
            }

            JTextArea jTextArea = new JTextArea(5, 36);
            jTextArea.setLineWrap(true);
            jTextArea.setWrapStyleWord(true);
            AppUi.styleEditableTextArea(jTextArea);
            JScrollPane jScrollPane = new JScrollPane(jTextArea);
            jScrollPane.getViewport().setBackground(AppUi.BG_FIELD);
            AppUi.styleScroll(jScrollPane);
            int n = JOptionPane.showConfirmDialog(this, jScrollPane, "Request note (max 200 words)", 2);
            if (n != 0) {
                return;
            }
            String string = jTextArea.getText().trim();
            if (string.length() > 8000) {
                JOptionPane.showMessageDialog(this, "Note is too long (max 8000 characters).");
                return;
            }
            if (MainFrame.wordCount(string) > 200) {
                JOptionPane.showMessageDialog(this, "Note is too long (max 200 words).");
                return;
            }
            String string2 = JOptionPane.showInputDialog(this, (Object)"Optional: password to protect this private chat on your PC (leave blank for none):");
            if (string2 != null && !string2.isBlank()) {
                this.privateChatPasswords.put(this.selectedUser, string2);
            }
            long l = System.currentTimeMillis();
            this.pendingOutgoingRequests.put(this.selectedUser, l + AppConfig.CHAT_REQUEST_TTL_MS);
            this.requestedUsers.add(this.selectedUser);
            this.storageManager.saveRequestedUsers(this.requestedUsers);
            this.sendCommand(this.selectedUser, "CHAT_REQUEST", l + "|" + string);
            this.appendSystem("Chat request sent to " + this.selectedUser + " (valid 1 hour from send time)");
        }

        /*
        private void showAccountMenu(Component invoker) {
            JPopupMenu menu = new JPopupMenu();
            menu.setBackground(AppUi.BG_CARD);
            
            JMenuItem logout = new JMenuItem("Logout / Switch User");
            logout.setForeground(AppUi.FG);
            logout.addActionListener(e -> {
                shutdown();
                dispose();
                new LoginFrame().setVisible(true);
            });
            
            JMenuItem delete = new JMenuItem("Delete This Account");
            delete.setForeground(new Color(239, 68, 68)); // Red
            delete.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you SURE you want to delete your account?\nThis will wipe all local data and cannot be undone.", 
                    "Delete Account", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    LocalAccountStore las = new LocalAccountStore();
                    if (las.deleteAccount(this.selfUser)) {
                        new SavedLoginStore().delete(this.selfUser);
                        this.deleteData();
                        shutdown();
                        dispose();
                        new LoginFrame().setVisible(true);
                    }
                }
            });
            
            menu.add(logout);
            menu.addSeparator();
            menu.add(delete);
            menu.show(invoker, 0, invoker.getHeight());
        }
        */

        private void blockSelectedUser() {
            if (this.selectedUser == null) {
                return;
            }
            this.blockedUsers.add(this.selectedUser);
            this.appendSystem("Blocked " + this.selectedUser);
        }

        private void unblockSelectedUser() {
            if (this.selectedUser == null) {
                return;
            }
            this.blockedUsers.remove(this.selectedUser);
            this.appendSystem("Unblocked " + this.selectedUser);
        }

        private void showUserActionMenu(String string, Component component, int n, int n2) {
            if (string == null || string.isBlank() || this.selfUser.equals(string)) {
                return;
            }
            JPopupMenu jPopupMenu = new JPopupMenu();
            if (!this.requestedUsers.contains(string) && !this.privateChats.containsKey(string)) {
                JMenuItem jMenuItem = new JMenuItem("Send request\u2026");
                jMenuItem.addActionListener(actionEvent -> {
                    this.selectedUser = string;
                    this.requestChat();
                });
                jPopupMenu.add(jMenuItem);
            }
            JMenuItem jMenuItem2 = new JMenuItem("Block");
            jMenuItem2.addActionListener(actionEvent -> {
                this.blockedUsers.add(string);
                this.appendSystem("Blocked " + string);
                this.refreshUserList();
            });
            JMenuItem jMenuItem3 = new JMenuItem("Unblock");
            jMenuItem3.addActionListener(actionEvent -> {
                this.blockedUsers.remove(string);
                this.appendSystem("Unblocked " + string);
                this.refreshUserList();
            });
            jPopupMenu.addSeparator();
            jPopupMenu.add(jMenuItem2);
            jPopupMenu.add(jMenuItem3);
            jPopupMenu.show(component, n, n2);
        }

        private void switchView() {
            Object object = this.modeBox.getSelectedItem();
            String string = object == null ? "" : object.toString();
            if ("Public".equals(string)) {
                if (this.keepActiveGroupOnNextSwitch) {
                    this.keepActiveGroupOnNextSwitch = false;
                } else {
                    this.activeGroup = "public";
                    this.groupField.setText("public");
                }
            } else if ("Group".equals(string)) {
                String string2 = LocalChatApp.clipPlain(this.groupField.getText().trim(), 64);
                if (string2.isBlank() || "public".equalsIgnoreCase(string2)) {
                    string2 = this.lastCustomGroupRoom == null || this.lastCustomGroupRoom.isBlank() ? "study-room" : this.lastCustomGroupRoom;
                }
                this.activeGroup = string2.toLowerCase(Locale.ROOT);
                this.lastCustomGroupRoom = string2;
                this.groupField.setText(string2);
            }

            this.updateActivityForDiscovery();
            if ("Private".equals(object)) {
                this.renderPrivateChat();
            } else {
                this.renderGroupChat();
            }
            this.updateChatContextLabel();
            this.syncConversationListSelection();
        }

        private void createGroupFromPicker() {
            JTextField jTextField = new JTextField(this.activeGroup != null && !"public".equalsIgnoreCase(this.activeGroup) ? this.activeGroup : "study-room");
            AppUi.styleTextField(jTextField);
            JPasswordField jPasswordField = new JPasswordField();
            AppUi.stylePasswordField(jPasswordField);
            DefaultListModel<String> defaultListModel = new DefaultListModel<String>();
            this.users.values().stream().filter(userProfile -> !userProfile.username.equals(this.selfUser)).filter(userProfile -> !this.blockedUsers.contains(userProfile.username)).sorted(Comparator.comparing(userProfile -> userProfile.username)).forEach(userProfile -> defaultListModel.addElement(userProfile.username));
            JList<String> jList = new JList<String>(defaultListModel);
            jList.setVisibleRowCount(10);
            jList.setSelectionMode(2);
            AppUi.styleStringList(jList);
            JScrollPane jScrollPane = new JScrollPane(jList);
            AppUi.styleScroll(jScrollPane);
            JPanel jPanel = new JPanel(new BorderLayout(0, 8));
            jPanel.setOpaque(true);
            jPanel.setBackground(AppUi.BG_PANEL);
            JPanel jPanel2 = new JPanel(new BorderLayout(8, 0));
            jPanel2.setOpaque(false);
            JLabel jLabel = new JLabel("Group name:");
            jLabel.setForeground(AppUi.FG_MUTED);
            jPanel2.add((Component)jLabel, "West");
            jPanel2.add((Component)jTextField, "Center");
            JPanel jPanel3 = new JPanel(new BorderLayout(8, 0));
            jPanel3.setOpaque(false);
            JLabel jLabel3 = new JLabel("Group pass:");
            jLabel3.setForeground(AppUi.FG_MUTED);
            jPanel3.add((Component)jLabel3, "West");
            jPanel3.add((Component)jPasswordField, "Center");
            JLabel jLabel2 = new JLabel("Select people to invite:");
            jLabel2.setForeground(AppUi.FG_MUTED);
            JPanel jPanel4 = new JPanel(new BorderLayout(0, 6));
            jPanel4.setOpaque(false);
            jPanel4.add((Component)jPanel2, "North");
            jPanel4.add((Component)jPanel3, "South");
            jPanel.add((Component)jPanel4, "North");
            JPanel jPanel5 = new JPanel(new BorderLayout(0, 6));
            jPanel5.setOpaque(false);
            jPanel5.add((Component)jLabel2, "North");
            jPanel5.add((Component)jScrollPane, "Center");
            jPanel.add((Component)jPanel5, "Center");
            Object[] objectArray = new Object[]{"Create/Invite", "Delete group", "Cancel"};
            int n = JOptionPane.showOptionDialog(this, jPanel, "Group manager", -1, 3, null, objectArray, objectArray[0]);
            if (n == 2 || n == -1) {
                return;
            }
            String string = LocalChatApp.clipPlain(jTextField.getText().trim(), 64);
            if (string.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please enter a group name.");
                return;
            }
            if ("public".equalsIgnoreCase(string)) {
                JOptionPane.showMessageDialog(this, "Use a custom group name (\"public\" is reserved).");
                return;
            }
            if (n == 1) {
                if (this.groupChats.remove(string) == null) {
                    JOptionPane.showMessageDialog(this, "Group not found: " + string);
                    return;
                }
                if (string.equalsIgnoreCase(this.activeGroup)) {
                    this.activeGroup = "public";
                    this.groupField.setText("public");
                    this.modeBox.setSelectedItem("Public");
                    this.switchView();
                }
                this.appendSystem("Deleted group \"" + string + "\".");
                this.rebuildChatSessionsList();
                return;
            }
            this.activeGroup = string.toLowerCase(Locale.ROOT);
            this.lastCustomGroupRoom = string;
            this.groupField.setText(string);
            this.keepActiveGroupOnNextSwitch = true;
            this.modeBox.setSelectedItem("Group");
            this.switchView();
            String string3 = new String(jPasswordField.getPassword()).trim();
            List<String> list = jList.getSelectedValuesList();
            long l = System.currentTimeMillis();
            for (String string2 : list) {
                String string4 = "Group invite: room \"" + string + "\" (Mode: Public).";
                if (!string3.isBlank()) {
                    string4 = string4 + " Group pass: \"" + string3 + "\".";
                }
                this.sendCommand(string2, "CHAT_REQUEST", l + "|" + string4);
            }
            this.appendSystem("Group \"" + string + "\" ready. Invited " + list.size() + " online user(s)." + (string3.isBlank() ? "" : " Shared group pass in invite."));
            this.rebuildChatSessionsList();
        }

        private void applyChatHistoryToPane() {
            this.chatArea.setText(LocalChatApp.wrapChatHtmlDocument(this.chatHistoryInnerHtml));
            this.scrollChatToBottom();
        }

        private void scrollChatToBottom() {
            SwingUtilities.invokeLater(() -> {
                try {
                    this.chatArea.setCaretPosition(this.chatArea.getDocument().getLength());
                }
                catch (Exception exception) {
                    // empty catch block
                }
            });
        }

        private void renderPrivateChat() {
            if (this.selectedUser == null) {
                this.chatHistoryInnerHtml = "";
                this.applyChatHistoryToPane();
                return;
            }
            if (!this.checkPrivatePassword(this.selectedUser)) {
                return;
            }
            List<ChatMessage> list = this.privateChats.computeIfAbsent(this.selectedUser, string -> new CopyOnWriteArrayList<ChatMessage>());
            String string2 = this.buildPrivateThreadHtml(this.selectedUser, list);
            String string3 = this.findMostRecentOtherPrivateUser(this.selectedUser);
            if (string3 != null) {
                List<ChatMessage> list2 = this.privateChats.getOrDefault(string3, List.of());
                if (!list2.isEmpty()) {
                    String string4 = "<div style=\"margin:18px 0 10px;color:#94a3b8;font-size:12px;border-top:1px solid #334155;padding-top:10px;\">Previous private chat: <b>" + LocalChatApp.htmlEscape(string3) + "</b></div>";
                    string2 = string2 + string4 + this.buildPrivateThreadHtml(string3, list2);
                }
            }
            this.chatHistoryInnerHtml = string2;
            this.applyChatHistoryToPane();
        }

        private String buildPrivateThreadHtml(String string, List<ChatMessage> list) {
            if (list == null || list.isEmpty()) {
                return LocalChatApp.chatEmptyHintHtml("No messages yet with <b>" + LocalChatApp.htmlEscape(string) + "</b>.<br/><br/>Type below and press <b>Send</b> (or <b>Ctrl+Enter</b>) to start this thread.");
            }
            return list.stream().map(chatMessage -> LocalChatApp.chatBubbleHtml(chatMessage, this.selfUser)).collect(Collectors.joining());
        }

        private String findMostRecentOtherPrivateUser(String string) {
            String string2 = null;
            long l = Long.MIN_VALUE;
            for (Map.Entry<String, List<ChatMessage>> entry : this.privateChats.entrySet()) {
                String string3 = entry.getKey();
                if (string3 == null || string3.equals(string)) {
                    continue;
                }
                List<ChatMessage> list = entry.getValue();
                if (list == null || list.isEmpty()) {
                    continue;
                }
                ChatMessage chatMessage = list.get(list.size() - 1);
                if (chatMessage.epochMs <= l) {
                    continue;
                }
                l = chatMessage.epochMs;
                string2 = string3;
            }
            return string2;
        }

        private void renderGroupChat() {
            List<ChatMessage> list = this.groupChats.computeIfAbsent(this.activeGroup.toLowerCase(Locale.ROOT), string -> new CopyOnWriteArrayList<ChatMessage>());
            if (list.isEmpty()) {
                this.chatHistoryInnerHtml = LocalChatApp.chatEmptyHintHtml("Room <b>\"" + LocalChatApp.htmlEscape(this.activeGroup) + "\"</b> is empty.<br/><br/>Use the same <b>Room</b> name, Mode <b>Group</b>, then <b>Apply</b>.<br/>Messages are visible to everyone in that room on the LAN.");
            } else {
                this.chatHistoryInnerHtml = list.stream().map(chatMessage -> LocalChatApp.chatBubbleHtml(chatMessage, this.selfUser)).collect(Collectors.joining());
            }
            this.applyChatHistoryToPane();
        }

        private boolean checkPrivatePassword(String string) {
            String string2 = this.privateChatPasswords.get(string);
            if (string2 == null) {
                return true;
            }
            String string3 = JOptionPane.showInputDialog(this, (Object)("Enter password for chat with " + string + ":"));
            if (string3 == null || !string3.equals(string2)) {
                this.appendSystem("Authentication failed for private chat " + string);
                return false;
            }
            return true;
        }

        private void sendCurrentMessage() {
            long l;
            String string2 = this.messageField.getText().trim();
            if (string2.isEmpty()) {
                return;
            }
            if (string2.length() > 32000) {
                JOptionPane.showMessageDialog(this, "Message is too long (max 32000 characters).", "Local Chat", 2);
                return;
            }
            long l2 = System.currentTimeMillis();
            if (l2 - (l = this.lastSendMs.get()) < 3500L) {
                long l3 = (3500L - (l2 - l) + 999L) / 1000L;
                this.appendSystem("Rate limit: wait ~" + l3 + "s between messages (fast LAN transfer).");
                return;
            }
            this.lastSendMs.set(l2);
            this.messageField.setText("");
            this.broadcastTypingStop();
            Object object = this.modeBox.getSelectedItem();
            if ("Private".equals(object)) {
                if (this.selectedUser == null) {
                    this.appendSystem("Select a user to send private message.");
                    return;
                }
                ChatMessage chatMessage = new ChatMessage(this.selfUser, this.selectedUser, string2, "PRIVATE:" + this.selectedUser);
                this.privateChats.computeIfAbsent(this.selectedUser, string -> new CopyOnWriteArrayList<ChatMessage>()).add(chatMessage);
                this.sendCommand(this.selectedUser, "PRIVATE_MESSAGE", chatMessage.id + "|" + string2);
                this.messageStats.recordMessage(this.selectedUser);
                this.renderPrivateChat();
                this.rebuildChatSessionsList();
            } else {
                ChatMessage chatMessage = new ChatMessage(this.selfUser, "GLOBAL_GROUP", string2, "GROUP:" + this.activeGroup.toLowerCase(Locale.ROOT));
                this.groupChats.computeIfAbsent(this.activeGroup.toLowerCase(Locale.ROOT), string -> new CopyOnWriteArrayList<ChatMessage>()).add(chatMessage);
                this.broadcastGroup(this.activeGroup.toLowerCase(Locale.ROOT), chatMessage.id + "|" + string2);
                this.messageStats.recordMessage("group:" + this.activeGroup);
                this.renderGroupChat();
                this.rebuildChatSessionsList();
            }
        }

        private void onCommandReceived(Command command, SocketAddress socketAddress) {
            SwingUtilities.invokeLater(() -> {
                String string2 = command.from;
                if (socketAddress instanceof InetSocketAddress) {
                    InetAddress inetAddress = ((InetSocketAddress)socketAddress).getAddress();
                    if (inetAddress != null) {
                        String string3 = inetAddress.getHostAddress();
                        this.users.compute(string2, (string, userProfile) -> {
                            if (userProfile == null) {
                                return new UserProfile(string2, string2, string3);
                            }
                            userProfile.lastSeen = System.currentTimeMillis();
                            userProfile.rememberAddress(string3);
                            return userProfile;
                        });
                    }
                }
                if (this.blockedUsers.contains(string2)) {
                    return;
                }
                switch (command.type) {
                    case "CHAT_REQUEST": {
                        this.handleChatRequest(string2, command.payload);
                        break;
                    }
                    case "PRIVATE_MESSAGE": {
                        String[] stringArray = command.payload.split("\\|", 2);
                        String string3 = stringArray.length > 1 ? stringArray[0] : UUID.randomUUID().toString();
                        String string4 = LocalChatApp.clipPayload(stringArray.length > 1 ? stringArray[1] : stringArray[0], 32000);
                        this.privateChats.computeIfAbsent(string2, string -> new CopyOnWriteArrayList<ChatMessage>()).add(new ChatMessage(string3, string2, this.selfUser, string4, "PRIVATE:" + string2, System.currentTimeMillis(), null));
                        this.messageStats.recordMessage(string2);
                        this.notifyUser("Message from " + string2);
                        this.rebuildChatSessionsList();
                        if (!"Private".equals(this.modeBox.getSelectedItem()) || !string2.equals(this.selectedUser)) break;
                        this.renderPrivateChat();
                        break;
                    }
                    case "GROUP_MESSAGE": {
                        String[] stringArray = command.payload.split("\\|", 3);
                        String string4 = LocalChatApp.clipPlain(stringArray.length > 0 ? stringArray[0] : "general", 128).toLowerCase(Locale.ROOT);
                        String string5 = stringArray.length > 2 ? stringArray[1] : UUID.randomUUID().toString();
                        String string6 = LocalChatApp.clipPayload(stringArray.length > 2 ? stringArray[2] : (stringArray.length > 1 ? stringArray[1] : ""), 32000);
                        this.groupChats.computeIfAbsent(string4.toLowerCase(Locale.ROOT), string -> new CopyOnWriteArrayList<ChatMessage>()).add(new ChatMessage(string5, string2, "GLOBAL_GROUP", string6, "GROUP:" + string4.toLowerCase(Locale.ROOT), System.currentTimeMillis(), null));
                        this.messageStats.recordMessage("group:" + string4);
                        this.notifyUser("Group msg in " + string4);
                        this.rebuildChatSessionsList();
                        if ("Private".equals(this.modeBox.getSelectedItem()) || !string4.equalsIgnoreCase(this.activeGroup)) break;
                        this.renderGroupChat();
                        break;
                    }
                    case "EDIT_MESSAGE": {
                        this.applyIncomingEditedMessage(string2, command.payload);
                        break;
                    }
                    case "TYPING": {
                        if ("Private".equals(this.modeBox.getSelectedItem()) && string2.equals(this.selectedUser)) {
                            this.typingLabel.setText(string2 + " is typing\u2026");
                        } else if (!"Private".equals(this.modeBox.getSelectedItem())) {
                            this.typingLabel.setText(string2 + " is typing\u2026");
                        }
                        if (this.scheduler == null) break;
                        this.scheduler.schedule(() -> SwingUtilities.invokeLater(() -> this.typingLabel.setText(" ")), 3L, TimeUnit.SECONDS);
                        break;
                    }
                    case "FILE_DONE": {
                        String[] stringArray = command.payload.split("\\|", 2);
                        String string6 = stringArray[0];
                        String string7 = stringArray.length > 1 ? stringArray[1] : "file";
                        this.notifyUser("File from " + string2 + ": " + string7);
                        this.appendSystem("Received file from " + string2 + " \u2192 " + string6);
                        break;
                    }
                    default: {
                        this.appendSystem("Unknown command from " + string2 + ": " + command.type);
                    }
                }
            });
        }

        private void handleChatRequest(String string, String string2) {
            long l;
            String string3 = "";
            int n = string2.indexOf(124);
            if (n >= 0) {
                try {
                    l = Long.parseLong(string2.substring(0, n));
                }
                catch (NumberFormatException numberFormatException) {
                    l = System.currentTimeMillis();
                    string3 = string2;
                }
                if (n + 1 < string2.length()) {
                    string3 = string2.substring(n + 1);
                }
            } else {
                try {
                    l = Long.parseLong(string2);
                }
                catch (NumberFormatException numberFormatException) {
                    l = System.currentTimeMillis();
                    string3 = string2;
                }
            }
            if (System.currentTimeMillis() - l > 3600000L) {
                this.appendSystem("Expired chat request from " + string);
                return;
            }
            string3 = LocalChatApp.clipPayload(string3, 8000);
            long l2 = l + 3600000L;
            this.pendingIncomingRequestUntil.put(string, l2);
            this.pendingIncomingRequestNote.put(string, string3);
            this.notifyUser("Chat request from " + LocalChatApp.clipPayload(string, 120));
            JPanel jPanel = new JPanel(new BorderLayout(0, 8));
            jPanel.setOpaque(true);
            jPanel.setBackground(AppUi.BG_CARD);
            JLabel jLabel = new JLabel("From: " + LocalChatApp.clipPayload(string, 120));
            jLabel.setForeground(AppUi.FG);
            jLabel.setFont(jLabel.getFont().deriveFont(1, 13.0f));
            JTextArea jTextArea = new JTextArea(string3.isBlank() ? "(no note)" : string3, 10, 44);
            jTextArea.setEditable(false);
            jTextArea.setLineWrap(true);
            jTextArea.setWrapStyleWord(true);
            jTextArea.setBackground(AppUi.BG_FIELD);
            jTextArea.setForeground(AppUi.FG);
            jTextArea.setBorder(new EmptyBorder(8, 10, 8, 10));
            JScrollPane jScrollPane = new JScrollPane(jTextArea);
            AppUi.styleScroll(jScrollPane);
            jPanel.add((Component)jLabel, "North");
            jPanel.add((Component)jScrollPane, "Center");
            Object[] objectArray = new Object[]{"Accept", "Deny", "Block"};
            int n2 = JOptionPane.showOptionDialog(this, jPanel, "Chat request (1h validity)", -1, 3, null, objectArray, objectArray[0]);
            if (n2 == 2) {
                this.blockedUsers.add(string);
                this.appendSystem("Blocked " + string + " after request.");
            } else if (n2 == 1) {
                this.appendSystem("Denied chat request from " + string);
            } else if (n2 == 0) {
                if (System.currentTimeMillis() > l2) {
                    JOptionPane.showMessageDialog(this, "This request has expired.");
                    return;
                }
                this.acceptRequestFrom(string);
            }
        }

        private void sendCommand(String string, String string2, String string3) {
            UserProfile userProfile = (UserProfile)this.users.get(string);
            if (userProfile == null) {
                this.appendSystem("User " + string + " not available.");
                return;
            }
            String string4 = LocalChatApp.escape(string3);
            if (string4.length() > 65280) {
                this.appendSystem("Payload too large to send over LAN protocol.");
                return;
            }
            this.ioPool.submit(() -> {
                Exception exception2 = null;
                for (String string5 : userProfile.addressCandidates()) {
                    try (Socket socket = new Socket();){
                        socket.connect(new InetSocketAddress(string5, 39002), 1200);
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
                        outputStreamWriter.write(string2 + ";" + this.selfUser + ";" + string4 + "\n");
                        outputStreamWriter.flush();
                        userProfile.rememberAddress(string5);
                        return;
                    }
                    catch (Exception exception) {
                        exception2 = exception;
                    }
                }
                String string6 = exception2 == null ? "unknown network error" : exception2.getMessage();
                this.appendSystem("Failed to send message to " + string + ": " + string6);
                if (exception2 != null) {
                    AppLog.line("WARN", "Send failed to " + string + " using " + userProfile.addressCandidates() + " -> " + string6);
                }
            });
        }

        private void broadcastGroup(String string, String string2) {
            for (UserProfile userProfile : this.users.values()) {
                if (userProfile.username.equals(this.selfUser) || this.blockedUsers.contains(userProfile.username)) continue;
                this.sendCommand(userProfile.username, "GROUP_MESSAGE", string + "|" + string2);
            }
        }

        private void broadcastEditToGroup(String string, String string2, String string3, long l, String string4) {
            String string5 = "GROUP|" + string + "|" + string2 + "|" + string3 + "|" + l + "|" + Base64.getEncoder().encodeToString(string4.getBytes(StandardCharsets.UTF_8));
            for (UserProfile userProfile : this.users.values()) {
                if (userProfile.username.equals(this.selfUser) || this.blockedUsers.contains(userProfile.username)) continue;
                this.sendCommand(userProfile.username, "EDIT_MESSAGE", string5);
            }
        }

        private void applyIncomingEditedMessage(String string, String string2) {
            try {
                String[] stringArray = string2.split("\\|", 6);
                if (stringArray.length < 6) {
                    return;
                }
                String string3 = stringArray[0];
                String string4 = stringArray[1];
                String string5 = stringArray[2];
                String string6 = stringArray[3];
                long l = Long.parseLong(stringArray[4]);
                String string7 = new String(Base64.getDecoder().decode(stringArray[5]), StandardCharsets.UTF_8);
                if ("PRIVATE".equals(string3)) {
                    List<ChatMessage> list = this.privateChats.get(string);
                    if (list == null) {
                        return;
                    }
                    for (int i = 0; i < list.size(); ++i) {
                        ChatMessage chatMessage = (ChatMessage)list.get(i);
                        if (!chatMessage.id.equals(string5)) continue;
                        list.set(i, new ChatMessage(string6, string, this.selfUser, string7, "PRIVATE:" + string, l, string5));
                        if ("Private".equals(this.modeBox.getSelectedItem()) && string.equals(this.selectedUser)) {
                            this.renderPrivateChat();
                        }
                        this.rebuildChatSessionsList();
                        return;
                    }
                    return;
                }
                if (!"GROUP".equals(string3)) {
                    return;
                }
                List<ChatMessage> list = this.groupChats.get(string4.toLowerCase(Locale.ROOT));
                if (list == null) {
                    return;
                }
                for (int i = 0; i < list.size(); ++i) {
                    ChatMessage chatMessage = (ChatMessage)list.get(i);
                    if (!chatMessage.id.equals(string5) || !string.equals(chatMessage.from)) continue;
                    list.set(i, new ChatMessage(string6, string, "GLOBAL_GROUP", string7, "GROUP:" + string4, l, string5));
                    if (!"Private".equals(this.modeBox.getSelectedItem()) && string4.equalsIgnoreCase(this.activeGroup)) {
                        this.renderGroupChat();
                    }
                    this.rebuildChatSessionsList();
                    return;
                }
            }
            catch (Exception exception) {
                AppLog.line("WARN", "Failed incoming edit from " + string + ": " + exception.getMessage());
            }
        }

        private void exportActive() {
            AppUi.ThemedFileChooser themedFileChooser = new AppUi.ThemedFileChooser();
            themedFileChooser.setDialogTitle("Save chat as Base64 text");
            if (themedFileChooser.showSaveDialog(this) != 0) {
                return;
            }
            File file = themedFileChooser.getSelectedFile();
            try {
                String string = this.getCurrentMessageList().stream().map(ChatMessage::format).collect(Collectors.joining("\n"));
                this.storageManager.exportBase64Plain(file.toPath(), string);
                this.appendSystem("Saved Base64 chat export to " + file.getAbsolutePath());
                AppLog.line("INFO", "Exported Base64 chat " + file.getAbsolutePath());
            }
            catch (Exception exception) {
                this.appendSystem("Export failed: " + exception.getMessage());
                AppLog.line("ERROR", "Export failed: " + exception.getMessage());
            }
        }

        private void deleteData() {
            int n = JOptionPane.showConfirmDialog(this, "Delete all local chat data?", "Confirm", 0);
            if (n == 0) {
                this.privateChats.clear();
                this.groupChats.clear();
                this.blockedUsers.clear();
                this.requestedUsers.clear();
                this.privateChatPasswords.clear();
                this.storageManager.deleteAll();
                this.chatHistoryInnerHtml = "";
                this.chatArea.setText(LocalChatApp.wrapChatHtmlDocument(""));
                this.appendSystem("Local data deleted.");
            }
        }

        private void appendSystem(String string) {
            this.chatHistoryInnerHtml = this.chatHistoryInnerHtml + LocalChatApp.chatSystemBannerHtml("[SYSTEM] " + string);
            this.applyChatHistoryToPane();
        }

        private void notifyUser(String string) {
            Toolkit.getDefaultToolkit().beep();
            String string2 = LocalChatApp.clipPayload(string, 80);
            this.setTitle("Local Chat \u2014 " + this.selfTag + " \u00b7 " + string2);
            if (!this.notificationsEnabled.isSelected()) {
                return;
            }
            SwingUtilities.invokeLater(() -> {
                JDialog jDialog = new JDialog((Frame)this, false);
                jDialog.setTitle("Notification");
                jDialog.getContentPane().setBackground(AppUi.BG_PANEL);
                jDialog.setLayout(new BorderLayout(8, 8));
                JTextArea jTextArea = new JTextArea(LocalChatApp.clipPayload(string, 6000));
                jTextArea.setEditable(false);
                jTextArea.setLineWrap(true);
                jTextArea.setWrapStyleWord(true);
                jTextArea.setColumns(36);
                jTextArea.setRows(10);
                jTextArea.setBackground(AppUi.BG_FIELD);
                jTextArea.setForeground(AppUi.FG);
                jTextArea.setFont(new Font("SansSerif", 0, 13));
                JScrollPane jScrollPane = new JScrollPane(jTextArea);
                AppUi.styleScroll(jScrollPane);
                jDialog.add((Component)jScrollPane, "Center");
                JButton jButton = new JButton("OK");
                AppUi.stylePrimaryButton(jButton, AppUi.ACCENT_DIM);
                jButton.addActionListener(actionEvent -> jDialog.dispose());
                JPanel jPanel = new JPanel();
                jPanel.setOpaque(false);
                jPanel.add(jButton);
                jDialog.add((Component)jPanel, "South");
                jDialog.pack();
                jDialog.setLocationRelativeTo(this);
                jDialog.setVisible(true);
                Timer timer = new Timer(8000, actionEvent -> jDialog.dispose());
                timer.setRepeats(false);
                timer.start();
            });
        }

        private void scheduleTypingNotify() {
            long l2 = System.currentTimeMillis();
            if (l2 - this.lastTypingNotify.get() < 2000L) {
                return;
            }
            this.lastTypingNotify.set(l2);
            Object object = this.modeBox.getSelectedItem();
            if ("Private".equals(object) && this.selectedUser != null) {
                this.sendCommand(this.selectedUser, "TYPING", "1");
            } else if (!"Private".equals(object)) {
                for (UserProfile userProfile : this.users.values()) {
                    if (userProfile.username.equals(this.selfUser) || this.blockedUsers.contains(userProfile.username)) continue;
                    this.sendCommand(userProfile.username, "TYPING", "1");
                }
            }
            if (this.typingStopTask != null) {
                this.typingStopTask.cancel(false);
            }
            if (this.scheduler != null) {
                this.typingStopTask = this.scheduler.schedule(() -> SwingUtilities.invokeLater(() -> this.typingLabel.setText(" ")), 3L, TimeUnit.SECONDS);
            }
        }

        private void broadcastTypingStop() {
            this.typingLabel.setText(" ");
        }

        private List<ChatMessage> getCurrentMessageList() {
            if ("Private".equals(this.modeBox.getSelectedItem()) && this.selectedUser != null) {
                return new ArrayList<ChatMessage>(this.privateChats.getOrDefault(this.selectedUser, List.of()));
            }
            return new ArrayList<ChatMessage>(this.groupChats.getOrDefault(this.activeGroup.toLowerCase(Locale.ROOT), List.of()));
        }

        private void runSearchInChat() {
            String string = this.searchField.getText().trim();
            List<ChatMessage> list = this.getCurrentMessageList();
            if (string.isEmpty()) {
                this.applyChatHistoryToPane();
                return;
            }
            String string2 = string.toLowerCase(Locale.ROOT);
            String string3 = list.stream().filter(chatMessage -> chatMessage.body.toLowerCase(Locale.ROOT).contains(string2)).map(chatMessage -> LocalChatApp.chatBubbleHtml(chatMessage, this.selfUser)).collect(Collectors.joining());
            this.chatArea.setText(LocalChatApp.wrapChatHtmlDocument(string3.isEmpty() ? LocalChatApp.chatEmptyHintHtml("(no matches)") : string3));
            this.scrollChatToBottom();
        }

        private void showEmojiPicker() {
            String[] stringArray = EMOJI_CHOICES.split(" ");
            JList<String> jList = new JList<String>(stringArray);
            jList.setVisibleRowCount(5);
            jList.setLayoutOrientation(2);
            jList.setFixedCellWidth(48);
            jList.setFixedCellHeight(40);
            AppUi.styleEmojiList(jList);
            JScrollPane jScrollPane = new JScrollPane(jList);
            AppUi.styleScroll(jScrollPane);
            jScrollPane.setPreferredSize(new Dimension(420, 220));
            jList.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    int n = jList.locationToIndex(mouseEvent.getPoint());
                    if (n < 0) {
                        return;
                    }
                    String string = (String)jList.getModel().getElementAt(n);
                    if (string == null) {
                        return;
                    }
                    MainFrame.this.insertEmojiIntoComposer(string, jList);
                }
            });
            jList.addKeyListener(new java.awt.event.KeyAdapter(){
                @Override
                public void keyPressed(java.awt.event.KeyEvent keyEvent) {
                    if (keyEvent.getKeyCode() != 10) {
                        return;
                    }
                    String string = (String)jList.getSelectedValue();
                    if (string == null) {
                        return;
                    }
                    MainFrame.this.insertEmojiIntoComposer(string, jList);
                }
            });
            JOptionPane.showMessageDialog(this, jScrollPane, "Emoji \u2014 click to insert", -1);
        }

        private void insertEmojiIntoComposer(String string, JList<String> jList) {
            this.messageField.replaceSelection(string);
            this.messageField.requestFocusInWindow();
            SwingUtilities.invokeLater(jList::clearSelection);
        }

        private boolean allowedChatFileName(String string) {
            String string2 = string.toLowerCase(Locale.ROOT);
            return string2.endsWith(".txt") || string2.endsWith(".doc") || string2.endsWith(".docx") || string2.endsWith(".xls") || string2.endsWith(".xlsx") || string2.endsWith(".ppt") || string2.endsWith(".pptx") || string2.endsWith(".jpg");
        }

        private void sendFileAttachment() {
            AppUi.ThemedFileChooser themedFileChooser = new AppUi.ThemedFileChooser();
            themedFileChooser.setDialogTitle("Attach a file");
            themedFileChooser.setApproveButtonText("Send");
            themedFileChooser.setFileFilter(new FileNameExtensionFilter("Documents and images", "txt", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "jpg"));
            if (themedFileChooser.showOpenDialog(this) != 0) {
                return;
            }
            File file = themedFileChooser.getSelectedFile();
            if (file == null || !file.isFile()) {
                return;
            }
            if (!this.allowedChatFileName(file.getName())) {
                JOptionPane.showMessageDialog(this, "Extension not allowed for chat files.");
                return;
            }
            long l = file.length();
            if (l <= 0L || l > 52428800L) {  // 50 MB
                JOptionPane.showMessageDialog(this, "File must be 1 byte \u2013 50 MB for chat attachments.");
                return;
            }
            Object object = this.modeBox.getSelectedItem();
            if ("Private".equals(object)) {
                if (this.selectedUser == null) {
                    JOptionPane.showMessageDialog(this, "Select a private chat user first.");
                    return;
                }
                UserProfile userProfile = (UserProfile)this.users.get(this.selectedUser);
                if (userProfile == null) {
                    this.appendSystem("User not online for file send.");
                    return;
                }
                this.ioPool.submit(() -> {
                    try {
                        this.sendFileToPeer(userProfile, file);
                        SwingUtilities.invokeLater(() -> this.appendSystem("Sent file to " + this.selectedUser));
                    }
                    catch (Exception exception) {
                        SwingUtilities.invokeLater(() -> this.appendSystem("File send failed: " + exception.getMessage()));
                    }
                });
            } else {
                int n = 0;
                for (UserProfile userProfile : this.users.values()) {
                    if (userProfile.username.equals(this.selfUser) || this.blockedUsers.contains(userProfile.username)) continue;
                    UserProfile userProfile2 = userProfile;
                    this.ioPool.submit(() -> {
                        try {
                            this.sendFileToPeer(userProfile2, file);
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                    });
                    ++n;
                }
                this.appendSystem("Sending file to " + n + " peer(s) (max 20 MB transfer per file).");
            }
        }

        private void sendFileToPeer(UserProfile userProfile, File file) throws IOException {
            long l = file.length();
            if (l > 0x1400000L) {
                throw new IOException("file too large");
            }
            IOException iOException = null;
            for (String string : userProfile.addressCandidates()) {
                try (Socket socket = new Socket();){
                    socket.connect(new InetSocketAddress(string, 39002), 8000);
                    OutputStream outputStream = socket.getOutputStream();
                    String string2 = "FILE_BEGIN;" + this.selfUser + ";" + LocalChatApp.escape(file.getName() + "|" + l) + "\n";
                    outputStream.write(string2.getBytes(StandardCharsets.UTF_8));
                    try (FileInputStream fileInputStream = new FileInputStream(file);){
                        ((InputStream)fileInputStream).transferTo(outputStream);
                    }
                    outputStream.flush();
                    userProfile.rememberAddress(string);
                    return;
                }
                catch (IOException iOException2) {
                    iOException = iOException2;
                }
            }
            if (iOException != null) {
                throw iOException;
            }
            throw new IOException("peer has no reachable address");
        }

        private void editOwnMessage() {
            List<ChatMessage> list = this.getCurrentMessageList();
            long l = System.currentTimeMillis();
            List<ChatMessage> list2 = list.stream().filter(chatMessage -> this.selfUser.equals(chatMessage.from)).filter(chatMessage -> l - chatMessage.epochMs <= 3600000L).collect(Collectors.toList());
            if (list2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No messages from you in the last hour to edit.");
                return;
            }
            ChatMessage chatMessage3 = this.chooseMessageForEdit(list2);
            if (chatMessage3 == null) {
                return;
            }
            String string = this.showEditMessageDialog(chatMessage3.body);
            if (string == null) {
                return;
            }
            if ((string = string.trim()).length() > 32000) {
                JOptionPane.showMessageDialog(this, "Text too long (max 32000 characters).");
                return;
            }
            ChatMessage chatMessage4 = chatMessage3.editedCopy(string);
            if ("Private".equals(this.modeBox.getSelectedItem()) && this.selectedUser != null) {
                List<ChatMessage> list3 = this.privateChats.get(this.selectedUser);
                if (list3 != null) {
                    for (int i = 0; i < list3.size(); ++i) {
                        if (!list3.get(i).id.equals(chatMessage3.id)) continue;
                        list3.set(i, chatMessage4);
                        break;
                    }
                }
                this.sendCommand(this.selectedUser, "EDIT_MESSAGE", "PRIVATE|" + this.selectedUser + "|" + chatMessage3.id + "|" + chatMessage4.id + "|" + chatMessage3.epochMs + "|" + Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8)));
                this.renderPrivateChat();
            } else {
                List<ChatMessage> list4 = this.groupChats.get(this.activeGroup.toLowerCase(Locale.ROOT));
                if (list4 != null) {
                    for (int i = 0; i < list4.size(); ++i) {
                        if (!list4.get(i).id.equals(chatMessage3.id)) continue;
                        list4.set(i, chatMessage4);
                        break;
                    }
                }
                this.broadcastEditToGroup(this.activeGroup, chatMessage3.id, chatMessage4.id, chatMessage3.epochMs, string);
                this.renderGroupChat();
            }
        }

        private ChatMessage chooseMessageForEdit(List<ChatMessage> list) {
            DefaultListModel<ChatMessage> defaultListModel = new DefaultListModel<ChatMessage>();
            for (ChatMessage chatMessage : list) {
                defaultListModel.addElement(chatMessage);
            }
            JList<ChatMessage> jList = new JList<ChatMessage>(defaultListModel);
            AppUi.styleList(jList);
            jList.setVisibleRowCount(Math.min(8, Math.max(4, list.size())));
            if (!list.isEmpty()) {
                jList.setSelectedIndex(list.size() - 1);
            }
            JScrollPane jScrollPane = new JScrollPane(jList);
            AppUi.styleScroll(jScrollPane);
            jScrollPane.setPreferredSize(new Dimension(640, 220));
            JPanel jPanel = new JPanel(new BorderLayout(0, 8));
            jPanel.setOpaque(true);
            jPanel.setBackground(AppUi.BG_CARD);
            JLabel jLabel = new JLabel("Pick a message to edit (within 1 hour of send):");
            jLabel.setForeground(AppUi.FG_MUTED);
            jPanel.add((Component)jLabel, "North");
            jPanel.add((Component)jScrollPane, "Center");
            int n = JOptionPane.showConfirmDialog(this, jPanel, "Edit message", 2, 3);
            if (n != 0) {
                return null;
            }
            return (ChatMessage)jList.getSelectedValue();
        }

        private String showEditMessageDialog(String string) {
            JPanel jPanel = new JPanel(new BorderLayout(0, 8));
            jPanel.setOpaque(true);
            jPanel.setBackground(AppUi.BG_CARD);
            JLabel jLabel = new JLabel("New text:");
            jLabel.setForeground(AppUi.FG_MUTED);
            JTextArea jTextArea = new JTextArea(string, 8, 46);
            jTextArea.setLineWrap(true);
            jTextArea.setWrapStyleWord(true);
            AppUi.styleEditableTextArea(jTextArea);
            JScrollPane jScrollPane = new JScrollPane(jTextArea);
            AppUi.styleScroll(jScrollPane);
            jPanel.add((Component)jLabel, "North");
            jPanel.add((Component)jScrollPane, "Center");
            int n = JOptionPane.showConfirmDialog(this, jPanel, "Edit message", 2, 3);
            if (n != 0) {
                return null;
            }
            return jTextArea.getText();
        }

        private void showDashboard() {
            JTextArea jTextArea = new JTextArea(this.messageStats.dashboardText());
            JOptionPane.showMessageDialog(this, AppUi.wrapReadOnlyScroll(jTextArea, false, 540, 320), "Messaging dashboard", 1);
        }

        private static int countReportWords(String string) {
            if (string == null || string.isBlank()) {
                return 0;
            }
            return string.trim().split("\\s+").length;
        }

        private void showWriteReport() {
            JDialog jDialog = new JDialog((Frame)this, "Write report", true);
            jDialog.getContentPane().setBackground(AppUi.BG_PANEL);
            jDialog.setLayout(new BorderLayout(10, 10));
            JLabel jLabel = new JLabel("Write a report (up to 1000 words). It is saved only on this PC as a text file you choose.");
            jLabel.setForeground(AppUi.FG_MUTED);
            jLabel.setBorder(new EmptyBorder(10, 12, 0, 12));
            jDialog.add((Component)jLabel, "North");
            JTextArea jTextArea = new JTextArea(18, 52);
            jTextArea.setLineWrap(true);
            jTextArea.setWrapStyleWord(true);
            AppUi.styleEditableTextArea(jTextArea);
            JScrollPane jScrollPane = new JScrollPane(jTextArea);
            AppUi.styleScroll(jScrollPane);
            jDialog.add((Component)jScrollPane, "Center");
            JLabel jLabel2 = new JLabel("0 / 1000 words");
            jLabel2.setForeground(AppUi.FG);
            jLabel2.setBorder(new EmptyBorder(0, 12, 0, 12));
            JPanel jPanel = new JPanel(new FlowLayout(2, 10, 10));
            jPanel.setOpaque(false);
            JButton jButton = new JButton("Save to file\u2026");
            JButton jButton2 = new JButton("Close");
            AppUi.stylePrimaryButton(jButton, AppUi.ACCENT_DIM);
            AppUi.styleSecondaryButton(jButton2);
            jPanel.add(jLabel2);
            jPanel.add(jButton);
            jPanel.add(jButton2);
            jDialog.add((Component)jPanel, "South");
            jTextArea.getDocument().addDocumentListener(new DocumentListener(){
                private void update() {
                    int n = MainFrame.countReportWords(jTextArea.getText());
                    jLabel2.setText(n + " / 1000 words");
                    jLabel2.setForeground(n > 1000 ? new Color(220, 80, 80) : AppUi.FG);
                }

                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    this.update();
                }

                @Override
                public void removeUpdate(DocumentEvent documentEvent) {
                    this.update();
                }

                @Override
                public void changedUpdate(DocumentEvent documentEvent) {
                    this.update();
                }
            });
            jButton2.addActionListener(actionEvent -> jDialog.dispose());
            jButton.addActionListener(actionEvent -> {
                int n = MainFrame.countReportWords(jTextArea.getText());
                if (n == 0) {
                    JOptionPane.showMessageDialog(jDialog, "Write something in the report before saving.", "Report", 2);
                    return;
                }
                if (n > 1000) {
                    JOptionPane.showMessageDialog(jDialog, "Report is too long (max 1000 words). Current: " + n + " words.", "Report", 2);
                    return;
                }
                AppUi.ThemedFileChooser themedFileChooser = new AppUi.ThemedFileChooser();
                themedFileChooser.setDialogTitle("Save report");
                themedFileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
                String string = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(Instant.now().atZone(ZoneId.systemDefault()));
                themedFileChooser.setSelectedFile(new File("report-" + string + ".txt"));
                if (themedFileChooser.showSaveDialog(jDialog) != 0) {
                    return;
                }
                File file = themedFileChooser.getSelectedFile();
                if (!file.getName().contains(".")) {
                    file = new File(file.getParentFile(), file.getName() + ".txt");
                }
                try {
                    Files.writeString(file.toPath(), jTextArea.getText(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    JOptionPane.showMessageDialog(jDialog, "Saved to:\n" + file.getAbsolutePath(), "Report saved", 1);
                    AppLog.line("INFO", "User report saved " + file.getAbsolutePath());
                    jDialog.dispose();
                }
                catch (Exception exception) {
                    JOptionPane.showMessageDialog(jDialog, "Could not save: " + exception.getMessage(), "Report", 0);
                    AppLog.line("ERROR", "Report save failed: " + exception.getMessage());
                }
            });
            jDialog.pack();
            jDialog.setMinimumSize(jDialog.getSize());
            jDialog.setLocationRelativeTo(this);
            jDialog.setVisible(true);
        }

        /*
        private void showChangelog() {
            JOptionPane.showMessageDialog(this, AppUi.wrapReadOnlyScroll(new JTextArea(ChangeLog.FULL_TEXT), false, 560, 400), "Changelog \u2014 " + ChangeLog.CURRENT, 1);
        }
        */

        private void persistAll() {
            try {
                this.storageManager.savePrivateChats(this.privateChats);
                this.storageManager.saveGroupChats(this.groupChats);
                this.storageManager.saveBlockedUsers(this.blockedUsers);
                this.storageManager.saveRequestedUsers(this.requestedUsers);
                this.storageManager.savePrivateChatPasswords(this.privateChatPasswords);
            }
            catch (Exception exception) {
                AppLog.line("ERROR", "persistAll: " + exception.getMessage());
            }
        }

        private void shutdown() {
            if (this.discoveryService != null) {
                this.discoveryService.stop();
            }
            if (this.chatServer != null) {
                this.chatServer.stop();
            }
            this.persistAll();
            if (this.scheduler != null) {
                this.scheduler.shutdownNow();
            }
            this.ioPool.shutdownNow();
        }
    }

    static final class LocalAccountStore {
        private final Path file = Path.of(System.getProperty("user.home"), ".local-chat-app", "accounts", "users.dat");

        LocalAccountStore() {
            try {
                Files.createDirectories(this.file.getParent());
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }

        boolean register(String string, char[] cArray) throws Exception {
            if (string.isBlank() || cArray.length < 4) {
                return false;
            }
            String string2 = string.trim();
            if (!string2.matches("[a-zA-Z0-9_]{3,32}")) {
                return false;
            }
            Map<String, String[]> map = this.loadAll();
            if (map.containsKey(string2.toLowerCase(Locale.ROOT))) {
                return false;
            }
            byte[] byArray = new byte[16];
            new SecureRandom().nextBytes(byArray);
            byte[] byArray2 = this.pbkdf2(cArray, byArray);
            String string3 = string2 + "\t" + Base64.getEncoder().encodeToString(byArray) + "\t" + Base64.getEncoder().encodeToString(byArray2) + "\n";
            Files.writeString(this.file, (CharSequence)string3, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        }

        String[] login(String string, char[] cArray) throws Exception {
            Map<String, String[]> map = this.loadAll();
            String[] stringArray = map.get(string.toLowerCase(Locale.ROOT));
            if (stringArray == null) {
                return null;
            }
            byte[] byArray2 = Base64.getDecoder().decode(stringArray[0]);
            byte[] byArray3 = Base64.getDecoder().decode(stringArray[1]);
            if (!MessageDigest.isEqual(byArray3, this.pbkdf2(cArray, byArray2))) {
                return null;
            }
            return new String[]{string};
        }

        private byte[] pbkdf2(char[] cArray, byte[] byArray) throws Exception {
            PBEKeySpec pBEKeySpec = new PBEKeySpec(cArray, byArray, 65536, 256);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return secretKeyFactory.generateSecret(pBEKeySpec).getEncoded();
        }

        private Map<String, String[]> loadAll() throws IOException {
            LinkedHashMap<String, String[]> linkedHashMap = new LinkedHashMap<String, String[]>();
            if (!Files.exists(this.file, new LinkOption[0])) {
                return linkedHashMap;
            }
            for (String string : Files.readAllLines(this.file, StandardCharsets.UTF_8)) {
                String[] stringArray;
                if (string.isBlank() || (stringArray = string.split("\t", -1)).length < 3 || stringArray[0].isBlank() || stringArray[1].isBlank() || stringArray[2].isBlank()) continue;
                linkedHashMap.put(stringArray[0].toLowerCase(Locale.ROOT), new String[]{stringArray[1], stringArray[2]});
            }
            return linkedHashMap;
        }

        synchronized boolean deleteAccount(String username) {
            if (username == null || username.isBlank()) return false;
            try {
                Map<String, String[]> map = loadAll();
                if (map.remove(username.toLowerCase(Locale.ROOT)) != null) {
                    StringBuilder sb = new StringBuilder();
                    map.forEach((u, data) -> {
                        sb.append(u).append("\t").append(data[0]).append("\t").append(data[1]).append("\n");
                    });
                    Files.writeString(this.file, sb.toString(), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    return true;
                }
            } catch (IOException e) {
                AppLog.line("ERROR", "deleteAccount: " + e.getMessage());
            }
            return false;
        }
    }

    static final class SavedLoginStore {
        private final Path file = Path.of(System.getProperty("user.home"), ".local-chat-app", "accounts", "saved-logins.dat");
        private final byte[] aesKey = SavedLoginStore.deriveKey("local-chat-saved-logins|" + System.getProperty("user.name", "user") + "|" + System.getProperty("os.name", ""));

        SavedLoginStore() {
            try {
                Files.createDirectories(this.file.getParent());
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }

        synchronized void save(String string, char[] cArray) {
            if (string == null || string.isBlank() || cArray == null || cArray.length == 0) {
                return;
            }
            Map<String, String> map = this.loadMap();
            map.put(string.trim(), new String(cArray));
            this.writeMap(map);
        }

        synchronized void delete(String username) {
            if (username == null || username.isBlank()) return;
            Map<String, String> map = this.loadMap();
            if (map.remove(username.trim()) != null) {
                this.writeMap(map);
            }
        }

        synchronized List<String> listUsernames() {
            return this.loadMap().keySet().stream().sorted(String::compareToIgnoreCase).collect(Collectors.toList());
        }

        synchronized String loadPassword(String string) {
            if (string == null || string.isBlank()) {
                return null;
            }
            return this.loadMap().get(string.trim());
        }

        private Map<String, String> loadMap() {
            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
            if (!Files.exists(this.file, new LinkOption[0])) {
                return linkedHashMap;
            }
            try {
                String string = this.decrypt(Files.readAllBytes(this.file));
                for (String string2 : string.split("\n")) {
                    if (string2.isBlank()) continue;
                    String[] stringArray = string2.split("\t", 2);
                    if (stringArray.length < 2 || stringArray[0].isBlank()) continue;
                    byte[] byArray = Base64.getDecoder().decode(stringArray[1]);
                    linkedHashMap.put(stringArray[0], new String(byArray, StandardCharsets.UTF_8));
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            return linkedHashMap;
        }

        private void writeMap(Map<String, String> map) {
            StringBuilder stringBuilder = new StringBuilder();
            map.forEach((string, string2) -> {
                if (string == null || string.isBlank() || string2 == null || string2.isEmpty()) {
                    return;
                }
                stringBuilder.append(string.replace("\t", "").replace("\n", "")).append("\t").append(Base64.getEncoder().encodeToString(string2.getBytes(StandardCharsets.UTF_8))).append("\n");
            });
            try {
                Files.write(this.file, this.encrypt(stringBuilder.toString()), new OpenOption[0]);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }

        private byte[] encrypt(String string) throws Exception {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, new SecretKeySpec(this.aesKey, "AES"));
            return cipher.doFinal(string.getBytes(StandardCharsets.UTF_8));
        }

        private String decrypt(byte[] byArray) throws Exception {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, new SecretKeySpec(this.aesKey, "AES"));
            return new String(cipher.doFinal(byArray), StandardCharsets.UTF_8);
        }

        private static byte[] deriveKey(String string) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                byte[] byArray = messageDigest.digest(string.getBytes(StandardCharsets.UTF_8));
                return Arrays.copyOf(byArray, 16);
            }
            catch (Exception exception) {
                return "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);
            }
        }
    }

    static final class MessageStats {
        private final Path file;

        MessageStats(String string) {
            this.file = Path.of(System.getProperty("user.home"), ".local-chat-app", string, "stats.dat");
            try {
                Files.createDirectories(this.file.getParent());
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }

        synchronized void recordMessage(String string) {
            if (string == null || string.isBlank()) {
                return;
            }
            String string2 = DateTimeFormatter.ofPattern("yyyy-MM").withZone(ZoneId.systemDefault()).format(Instant.now());
            String string3 = DateTimeFormatter.ofPattern("yyyy").withZone(ZoneId.systemDefault()).format(Instant.now());
            Map<String, long[]> map = this.load();
            this.bump(map, "r:" + string);
            this.bump(map, "m:" + string2 + ":" + string);
            this.bump(map, "y:" + string3 + ":" + string);
            this.save(map);
        }

        private void bump(Map<String, long[]> map, String string2) {
            map.compute(string2, (string, lArray) -> {
                if (lArray == null) {
                    return new long[]{1L};
                }
                lArray[0] = lArray[0] + 1L;
                return lArray;
            });
        }

        private Map<String, long[]> load() {
            HashMap<String, long[]> hashMap = new HashMap<String, long[]>();
            if (!Files.exists(this.file, new LinkOption[0])) {
                return hashMap;
            }
            try {
                for (String string : Files.readAllLines(this.file, StandardCharsets.UTF_8)) {
                    if (string.isBlank() || !string.contains("=")) continue;
                    String[] stringArray = string.split("=", 2);
                    hashMap.put(stringArray[0], new long[]{Long.parseLong(stringArray[1])});
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            return hashMap;
        }

        private void save(Map<String, long[]> map) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                map.forEach((string, lArray) -> stringBuilder.append(string).append("=").append(lArray[0]).append("\n"));
                Files.writeString(this.file, (CharSequence)stringBuilder.toString(), StandardCharsets.UTF_8, new OpenOption[0]);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }

        String dashboardText() {
            Map<String, long[]> map = this.load();
            List<Map.Entry<String, Long>> list = map.entrySet().stream().filter(entry -> entry.getKey().startsWith("r:")).map(entry -> Map.entry(entry.getKey().substring(2), entry.getValue()[0])).sorted((entry, entry2) -> Long.compare(entry2.getValue(), entry.getValue())).limit(10L).collect(Collectors.toList());
            String string = DateTimeFormatter.ofPattern("yyyy-MM").withZone(ZoneId.systemDefault()).format(Instant.now());
            String string2 = DateTimeFormatter.ofPattern("yyyy").withZone(ZoneId.systemDefault()).format(Instant.now());
            List<Map.Entry<String, Long>> list2 = this.topForPrefix(map, "m:" + string + ":");
            List<Map.Entry<String, Long>> list3 = this.topForPrefix(map, "y:" + string2 + ":");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Dashboard \u2014 who you talk to most\n\nRecently (all time, top 10):\n");
            list.forEach(entry -> stringBuilder.append("  \u2022 ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" msgs\n"));
            stringBuilder.append("\nThis month (").append(string).append("):\n");
            list2.forEach(entry -> stringBuilder.append("  \u2022 ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n"));
            stringBuilder.append("\nThis year (").append(string2).append("):\n");
            list3.forEach(entry -> stringBuilder.append("  \u2022 ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n"));
            return stringBuilder.toString();
        }

        private List<Map.Entry<String, Long>> topForPrefix(Map<String, long[]> map, String string) {
            return map.entrySet().stream().filter(entry -> entry.getKey().startsWith(string)).map(entry -> Map.entry(entry.getKey().substring(string.length()), entry.getValue()[0])).sorted((entry, entry2) -> Long.compare(entry2.getValue(), entry.getValue())).limit(10L).collect(Collectors.toList());
        }
    }

    static final class AppLog {
        static final Path LOG_DIR = Path.of(System.getProperty("user.home"), ".local-chat-app", "logs");
        static final Path LOG_FILE = LOG_DIR.resolve("app.log");
        static final Path BACKUP_LOG = LOG_DIR.resolve("backup.log");

        AppLog() {
        }

        static void init() {
            try {
                Files.createDirectories(LOG_DIR);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }

        static void line(String string, String string2) {
            AppLog.init();
            String string3 = String.valueOf(Instant.now()) + " [" + string + "] " + string2 + System.lineSeparator();
            try {
                Files.writeString(LOG_FILE, (CharSequence)string3, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }

        static String tail(int n) {
            if (!Files.exists(LOG_FILE, new LinkOption[0])) {
                return "(no logs yet)";
            }
            try {
                List<String> list = Files.readAllLines(LOG_FILE, StandardCharsets.UTF_8);
                int n2 = Math.max(0, list.size() - n);
                return String.join((CharSequence)"\n", list.subList(n2, list.size()));
            }
            catch (IOException iOException) {
                return "Error reading log: " + iOException.getMessage();
            }
        }
    }

    static final class ChangeLog {
        static final String CURRENT = "1.4.0";
        static final String FULL_TEXT = "Local Chat \u2014 Modernized v1.4.0\n\nv1.4.0 (2026-04-24)\n\u2022 Integrated Advanced Glassmorphism UI with dynamic Light/Dark mode\n\u2022 Added Theme Persistence (saved to ~/.local-chat-app/theme.dat)\n\u2022 Implemented Delete Account functionality (removes all local data & credentials)\n\u2022 Enforced One-Time Connection Request logic (prevents redundant spam)\n\u2022 Added User Menu with Logout and Delete Account options\n\u2022 Ported Dashboard, Reporting, and Message Editing from Reference Project\n\u2022 Improved UI responsiveness and micro-animations\n\n- Ported and Enhanced by antigravity assistant\n";

        ChangeLog() {
        }
    }

    static final class ChatMessage {
        final String id;
        final String from;
        final String to;
        final String body;
        final String room;
        final long epochMs;
        volatile String editedFromId;

        ChatMessage(String string, String string2, String string3, String string4) {
            this(UUID.randomUUID().toString(), string, string2, string3, string4, System.currentTimeMillis(), null);
        }

        ChatMessage(String string, String string2, String string3, String string4, String string5, long l, String string6) {
            this.id = string;
            this.from = string2;
            this.to = string3;
            this.body = string4;
            this.room = string5;
            this.epochMs = l;
            this.editedFromId = string6;
        }

        String format() {
            ZoneId zoneId = ZoneId.systemDefault();
            Instant instant = Instant.ofEpochMilli(this.epochMs);
            String string = DateTimeFormatter.ofPattern("EEEE").withZone(zoneId).format(instant);
            String string2 = DateTimeFormatter.ofPattern("MMMM").withZone(zoneId).format(instant);
            String string3 = DateTimeFormatter.ofPattern("d").withZone(zoneId).format(instant);
            String string4 = DateTimeFormatter.ofPattern("yyyy").withZone(zoneId).format(instant);
            String string5 = DateTimeFormatter.ofPattern("HH:mm").withZone(zoneId).format(instant);
            String string6 = this.editedFromId != null ? " (edited)" : "";
            return "[" + string + ", " + string2 + " " + string3 + ", " + string4 + " " + string5 + "] " + this.from + ": " + this.body + string6;
        }

        ChatMessage editedCopy(String string) {
            return new ChatMessage(UUID.randomUUID().toString(), this.from, this.to, string, this.room, this.epochMs, this.id);
        }

        public String toString() {
            return this.format();
        }
    }

    static final class UserProfile {
        final String username;
        final String tag;
        volatile String hostAddress;
        final Set<String> knownAddresses = ConcurrentHashMap.newKeySet();
        volatile long lastSeen;
        volatile String activity = "ONLINE";

        UserProfile(String string, String string2, String string3) {
            this.username = string;
            this.tag = string2;
            this.hostAddress = string3;
            this.rememberAddress(string3);
            this.lastSeen = System.currentTimeMillis();
        }

        void rememberAddress(String string) {
            if (string == null || string.isBlank()) {
                return;
            }
            this.knownAddresses.add(string);
            if (LocalChatApp.shouldReplacePeerAddress(this.hostAddress, string)) {
                this.hostAddress = string;
            }
        }

        List<String> addressCandidates() {
            ArrayList<String> arrayList = new ArrayList<String>();
            if (this.hostAddress != null && !this.hostAddress.isBlank()) {
                arrayList.add(this.hostAddress);
            }
            for (String string : this.knownAddresses) {
                if (string == null || string.isBlank() || arrayList.contains(string)) continue;
                arrayList.add(string);
            }
            arrayList.sort((string, string2) -> Integer.compare(LocalChatApp.peerAddressScore(string2), LocalChatApp.peerAddressScore(string)));
            return arrayList;
        }
    }
}

