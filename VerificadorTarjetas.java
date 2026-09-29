import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

/** Demostración educativa de validación local de formato; no verifica cuentas bancarias. */
public class VerificadorTarjetas {
    private static final Color BG = new Color(13, 23, 34);
    private static final Color PANEL = new Color(23, 37, 51);
    private static final Color INPUT = new Color(30, 47, 63);
    private static final Color INK = new Color(238, 244, 244);
    private static final Color MUTED = new Color(164, 184, 193);
    private static final Color GOOD = new Color(105, 222, 171);
    private static final Color BAD = new Color(255, 137, 132);
    private static final Color ACCENT = new Color(138, 202, 235);

    private final JFrame frame = new JFrame("Verificador de tarjetas | Aula");
    private final JTextField number = new JTextField();
    private final JTextField expiry = new JTextField();
    private final JPasswordField cvc = new JPasswordField();
    private final JCheckBox demo = new JCheckBox("Mostrar datos simulados de los ejemplos");
    private final JLabel previewBrand = label("RED POR IDENTIFICAR", 12, Font.BOLD, MUTED);
    private final JLabel previewNumber = label("••••  ••••  ••••  ••••", 24, Font.BOLD, INK);
    private final JLabel previewExpiry = label("MM/AA", 14, Font.PLAIN, INK);
    private final JLabel previewIssuer = label("Emisor: sin identificar", 14, Font.PLAIN, INK);
    private final JLabel previewType = label("Tipo: sin identificar", 14, Font.PLAIN, INK);
    private final JLabel headline = label("Empieza a escribir", 22, Font.BOLD, INK);
    private final JLabel explanation = label("Los indicadores se actualizarán en tiempo real.", 13, Font.PLAIN, MUTED);
    private final JPanel checks = new JPanel();
    private final Map<String, JLabel> values = new LinkedHashMap<>();
    private boolean formatting;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VerificadorTarjetas().show());
    }

    private void show() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(760, 680));
        JPanel root = new JPanel(new BorderLayout(26, 0));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(28, 30, 26, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(label("Verificador de tarjetas", 29, Font.BOLD, INK), BorderLayout.NORTH);
        JLabel subtitle = label("Laboratorio de validación local  ·  Java Swing", 13, Font.PLAIN, MUTED);
        subtitle.setBorder(new EmptyBorder(5, 0, 0, 0));
        header.add(subtitle, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        JPanel columns = new JPanel(new GridBagLayout());
        columns.setOpaque(false);
        columns.setBorder(new EmptyBorder(26, 0, 0, 0));
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0; gc.weighty = 1; gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 0; gc.weightx = .94; gc.insets = new Insets(0, 0, 0, 14);
        columns.add(left(), gc);
        gc.gridx = 1; gc.weightx = 1.06; gc.insets = new Insets(0, 14, 0, 0);
        columns.add(right(), gc);
        root.add(columns, BorderLayout.CENTER);

        JLabel footer = label("Formato válido ≠ tarjeta existente. No se realizan cobros ni consultas al banco.", 12, Font.PLAIN, MUTED);
        footer.setBorder(new EmptyBorder(22, 0, 0, 0));
        root.add(footer, BorderLayout.SOUTH);
        frame.setContentPane(root);
        frame.setSize(1080, 760);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        update();
    }

    private JPanel left() {
        JPanel box = panel(PANEL, 18);
        box.setLayout(new BorderLayout(0, 24));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel wordmark = label("AULA  /  CARD LAB", 13, Font.BOLD, ACCENT);
        top.add(wordmark, BorderLayout.WEST);
        top.add(previewBrand, BorderLayout.EAST);
        box.add(top, BorderLayout.NORTH);

        JPanel middle = new JPanel();
        middle.setOpaque(false);
        middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
        middle.add(Box.createVerticalGlue());
        JLabel numberLabel = label("NÚMERO DE TARJETA", 11, Font.BOLD, MUTED);
        numberLabel.setAlignmentX(0);
        previewNumber.setAlignmentX(0);
        middle.add(numberLabel);
        middle.add(Box.createVerticalStrut(13));
        middle.add(previewNumber);
        middle.add(Box.createVerticalStrut(38));
        middle.add(label("VENCIMIENTO", 11, Font.BOLD, MUTED));
        middle.add(Box.createVerticalStrut(8));
        middle.add(previewExpiry);
        middle.add(Box.createVerticalGlue());
        box.add(middle, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.add(previewIssuer);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(previewType);
        JLabel caveat = label("Los datos de emisor en los ejemplos son ficticios.", 11, Font.PLAIN, MUTED);
        caveat.setBorder(new EmptyBorder(24, 0, 0, 0));
        bottom.add(caveat);
        box.add(bottom, BorderLayout.SOUTH);
        return box;
    }

    private JPanel right() {
        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        section(box, "Escribe los datos");
        field(box, "Número", number, "Ej. 4242 4242 4242 4242");

        JPanel pair = new JPanel(new GridLayout(1, 2, 15, 0));
        pair.setOpaque(false);
        pair.add(fieldBlock("Vencimiento (MM/AA)", expiry, "MM/AA"));
        pair.add(fieldBlock("CVC (solo formato)", cvc, "•••"));
        pair.setMaximumSize(new Dimension(Integer.MAX_VALUE, 83));
        box.add(pair);
        box.add(Box.createVerticalStrut(12));

        demo.setOpaque(false);
        demo.setForeground(MUTED);
        demo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        demo.setFocusable(true);
        demo.setSelected(true);
        demo.setAlignmentX(0);
        box.add(demo);
        box.add(Box.createVerticalStrut(22));

        section(box, "Comprobaciones en vivo");
        checks.setOpaque(false);
        checks.setLayout(new BoxLayout(checks, BoxLayout.Y_AXIS));
        addCheck("Red reconocida");
        addCheck("Longitud correcta");
        addCheck("Algoritmo de Luhn");
        addCheck("Fecha vigente");
        addCheck("Formato de CVC");
        box.add(checks);
        box.add(Box.createVerticalStrut(19));
        headline.setAlignmentX(0);
        explanation.setAlignmentX(0);
        box.add(headline);
        box.add(Box.createVerticalStrut(5));
        box.add(explanation);
        box.add(Box.createVerticalGlue());

        watch(number, () -> { formatNumber(); update(); });
        watch(expiry, () -> { formatExpiry(); update(); });
        watch(cvc, () -> { formatCvc(); update(); });
        demo.addActionListener(e -> update());
        return box;
    }

    private void section(JPanel parent, String title) {
        JLabel heading = label(title, 18, Font.BOLD, INK);
        heading.setAlignmentX(0);
        heading.setBorder(new EmptyBorder(0, 0, 15, 0));
        parent.add(heading);
    }

    private void field(JPanel parent, String title, JTextField input, String hint) {
        JPanel block = fieldBlock(title, input, hint);
        block.setMaximumSize(new Dimension(Integer.MAX_VALUE, 83));
        parent.add(block);
        parent.add(Box.createVerticalStrut(13));
    }

    private JPanel fieldBlock(String title, JTextField input, String hint) {
        JPanel block = new JPanel(new BorderLayout(0, 7));
        block.setOpaque(false);
        block.add(label(title, 12, Font.BOLD, MUTED), BorderLayout.NORTH);
        input.setFont(new Font("SansSerif", Font.PLAIN, 17));
        input.setForeground(INK);
        input.setCaretColor(ACCENT);
        input.setBackground(INPUT);
        input.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(65, 88, 104)), new EmptyBorder(12, 14, 12, 14)));
        input.putClientProperty("JTextField.placeholderText", hint);
        input.setToolTipText(hint);
        block.add(input, BorderLayout.CENTER);
        return block;
    }

    private void addCheck(String name) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(6, 0, 6, 0));
        JLabel title = label(name, 13, Font.PLAIN, INK);
        JLabel result = label("Pendiente", 12, Font.BOLD, BAD);
        row.add(title, BorderLayout.WEST);
        row.add(result, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        checks.add(row);
        values.put(name, result);
    }

    private void update() {
        String digits = number.getText().replaceAll("\\D", "");
        String brand = brand(digits);
        boolean network = !brand.equals("Desconocida");
        boolean length = network && (brand.equals("Visa") ?
                (digits.length() == 13 || digits.length() == 16 || digits.length() == 19) : digits.length() == 16);
        boolean luhn = length && luhn(digits);
        boolean date = validExpiry(expiry.getText());
        int cvcLength = cvc.getPassword().length;
        boolean code = network && (brand.equals("Visa") || brand.equals("Mastercard")) && cvcLength == 3;

        mark("Red reconocida", network, network ? brand : "Pendiente");
        mark("Longitud correcta", length, length ? digits.length() + " dígitos" : "Pendiente");
        mark("Algoritmo de Luhn", luhn, luhn ? "Cumple" : "No cumple");
        mark("Fecha vigente", date, date ? "Vigente" : "Pendiente / vencida");
        mark("Formato de CVC", code, code ? "3 dígitos" : "Pendiente");

        previewBrand.setText(network ? brand.toUpperCase() : "RED POR IDENTIFICAR");
        previewBrand.setForeground(network ? ACCENT : MUTED);
        previewNumber.setText(number.getText().isEmpty() ? "••••  ••••  ••••  ••••" : number.getText());
        previewExpiry.setText(expiry.getText().isEmpty() ? "MM/AA" : expiry.getText());
        String issuer = "No disponible sin catálogo BIN";
        String type = "No disponible sin catálogo BIN";
        if (demo.isSelected() && digits.startsWith("424242")) {
            issuer = "Banco Aula Norte (simulado)";
            type = "Crédito (simulado)";
        } else if (demo.isSelected() && digits.startsWith("555555")) {
            issuer = "Banco Aula Sur (simulado)";
            type = "Débito (simulado)";
        }
        previewIssuer.setText("Emisor: " + issuer);
        previewType.setText("Tipo: " + type);

        boolean complete = luhn && date && code;
        headline.setText(complete ? "Formato válido" : "Formato por completar");
        headline.setForeground(complete ? GOOD : INK);
        explanation.setText(complete ? "Estructura correcta; existencia y fondos no comprobados."
                : "Corrige los indicadores rojos para completar el formato.");
    }

    private void mark(String key, boolean passed, String message) {
        JLabel label = values.get(key);
        label.setText((passed ? "✓  " : "×  ") + message);
        label.setForeground(passed ? GOOD : BAD);
    }

    private static String brand(String n) {
        if (n.startsWith("4")) return "Visa";
        if (n.length() >= 2) {
            int first2 = Integer.parseInt(n.substring(0, 2));
            if (first2 >= 51 && first2 <= 55) return "Mastercard";
        }
        if (n.length() >= 4) {
            int first4 = Integer.parseInt(n.substring(0, 4));
            if (first4 >= 2221 && first4 <= 2720) return "Mastercard";
        }
        return "Desconocida";
    }

    private static boolean luhn(String n) {
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = n.length() - 1; i >= 0; i--) {
            int d = n.charAt(i) - '0';
            if (doubleDigit) { d *= 2; if (d > 9) d -= 9; }
            sum += d;
            doubleDigit = !doubleDigit;
        }
        return !n.isEmpty() && sum % 10 == 0;
    }

    private static boolean validExpiry(String text) {
        if (!text.matches("\\d{2}/\\d{2}")) return false;
        int month = Integer.parseInt(text.substring(0, 2));
        int year = 2000 + Integer.parseInt(text.substring(3));
        if (month < 1 || month > 12) return false;
        YearMonth target = YearMonth.of(year, month);
        return !target.isBefore(YearMonth.now()) && !target.isAfter(YearMonth.now().plusYears(25));
    }

    private void formatNumber() {
        format(number, 19, 4, false);
    }

    private void formatExpiry() {
        format(expiry, 4, 2, true);
    }

    private void formatCvc() {
        format(cvc, 4, 0, false);
    }

    private void format(JTextField field, int max, int group, boolean slash) {
        if (formatting) return;
        String original = field.getText();
        int caret = field.getCaretPosition();
        int before = original.substring(0, Math.min(caret, original.length())).replaceAll("\\D", "").length();
        String digits = original.replaceAll("\\D", "");
        if (digits.length() > max) digits = digits.substring(0, max);
        StringBuilder out = new StringBuilder();
        int newCaret = 0;
        for (int i = 0; i < digits.length(); i++) {
            if (group > 0 && i > 0 && i % group == 0) out.append(slash ? '/' : ' ');
            if (i == before) newCaret = out.length();
            out.append(digits.charAt(i));
        }
        if (before >= digits.length()) newCaret = out.length();
        if (!original.contentEquals(out)) {
            formatting = true;
            field.setText(out.toString());
            field.setCaretPosition(Math.min(newCaret, out.length()));
            formatting = false;
        }
    }

    private static void watch(JTextField input, Runnable action) {
        input.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { SwingUtilities.invokeLater(action); }
            public void removeUpdate(DocumentEvent e) { SwingUtilities.invokeLater(action); }
            public void changedUpdate(DocumentEvent e) { SwingUtilities.invokeLater(action); }
        });
    }

    private static JLabel label(String text, int size, int weight, Color color) {
        JLabel result = new JLabel(text);
        result.setFont(new Font("SansSerif", weight, size));
        result.setForeground(color);
        return result;
    }

    private static JPanel panel(Color color, int padding) {
        JPanel result = new JPanel();
        result.setBackground(color);
        result.setBorder(new EmptyBorder(padding + 10, padding + 8, padding + 10, padding + 8));
        return result;
    }
}
