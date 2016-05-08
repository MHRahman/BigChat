package bigchat;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

public class BigChat {
    static String[] emos = new String[]{"(flower)", "(H)", "\\o/", "(party)", "(cool)", "(hug)", "(heidy)", "(pi)", "(highfive)", "(clap)", "(nod)", "(dull)", ":(", ":D", "(bandit)", "(shake)", ";(", ":("};
    static String pad = emos[0];
    static String ink = emos[1];
    private static Display display;
    private static Shell shell;
    private static StyledText text;
    private static Button checkButton;
    private static KeyListener keyListener;
    private static Combo writing;
    private static Combo padding;
    private static Label label;

    static {
        keyListener = new KeyListener(){

            public void keyReleased(KeyEvent e) {
                BigChat.printAndCopy();
            }

            public void keyPressed(KeyEvent e) {
            }
        };
    }

    public static void main(String[] args) {
        BigChat.initUi();
        BigChat.listenUi();
    }

    private static void listenUi() {
        while (!shell.isDisposed()) {
            if (display.readAndDispatch()) continue;
            display.sleep();
        }
        display.dispose();
    }

    private static void initUi() {
        display = new Display();
        shell = new Shell(display);
        shell.setLayout((Layout)new GridLayout(1, false));
        Composite composite = new Composite((Composite)shell, 0);
        composite.setLayout((Layout)new GridLayout(2, false));
        BigChat.createPaddingCombo(composite);
        BigChat.createInkCombo(composite);
        checkButton = new Button((Composite)shell, 32);
        checkButton.setText("Vertical words");
        checkButton.setSelection(false);
        checkButton.addSelectionListener(new SelectionListener(){

            public void widgetSelected(SelectionEvent e) {
                BigChat.printAndCopy();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        label = new Label((Composite)shell, 1);
        text = new StyledText((Composite)shell, 2048);
        text.setText("");
        text.setLayoutData((Object)new GridData(300, 100));
        label.setLayoutData((Object)new GridData(400, 30));
        text.addKeyListener(keyListener);
        shell.setSize(400, 300);
        shell.open();
    }

    protected static void printAndCopy() {
        pad = padding.getText();
        ink = writing.getText();
        String message = BigChat.printSentence(text.getText());
        StringSelection selection = new StringSelection(message);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        label.setText("Your emo is copied! Just Paste in chat to see!");
    }

    private static void createInkCombo(Composite parent) {
        Label label = new Label(parent, 2048);
        label.setText("Writing");
        writing = new Combo(parent, 2048);
        int i = 0;
        while (i < emos.length) {
            writing.add(emos[i]);
            ++i;
        }
        writing.select(1);
        writing.addKeyListener(keyListener);
        writing.addSelectionListener(new SelectionListener(){

            public void widgetSelected(SelectionEvent e) {
                BigChat.ink = writing.getItem(writing.getSelectionIndex());
                BigChat.printAndCopy();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private static void createPaddingCombo(Composite parent) {
        Label label = new Label(parent, 2048);
        label.setText("Padding");
        padding = new Combo(parent, 2048);
        int i = 0;
        while (i < emos.length) {
            padding.add(emos[i]);
            ++i;
        }
        padding.select(0);
        padding.addKeyListener(keyListener);
        padding.addSelectionListener(new SelectionListener(){

            public void widgetSelected(SelectionEvent e) {
                BigChat.pad = padding.getText();
                BigChat.printAndCopy();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private static String printSentence(String message) {
        String[] words = message.split(" ");
        String sentence = "";
        int i = 0;
        while (i < words.length) {
            sentence = String.valueOf(sentence) + BigChat.printWord(words[i], checkButton.getSelection());
            sentence = String.valueOf(sentence) + "\n\n\n\n\n\n\n\n";
            ++i;
        }
        return sentence;
    }

    private static String printWord(String mesString) {
        return BigChat.printWord(mesString, false);
    }

    private static String printWord(String message, boolean oneCharPerLine) {
        message = message.toLowerCase();
        String word = "";
        if (ChatMaps.getMapFor(message) != null) {
            return BigChat.printEmo(message);
        }
        if (oneCharPerLine) {
            int i = 0;
            while (i < message.length()) {
                word = String.valueOf(word) + BigChat.printEmo(new StringBuilder(String.valueOf(message.charAt(i))).toString());
                word = String.valueOf(word) + "\n";
                ++i;
            }
            return word;
        }
        int j = 0;
        while (j < 8) {
            int i = 0;
            while (i < message.length()) {
                char ch = message.charAt(i);
                word = String.valueOf(word) + BigChat.printCharacterLine(new StringBuilder(String.valueOf(ch)).toString(), j);
                word = String.valueOf(word) + "   ";
                ++i;
            }
            word = String.valueOf(word) + "\n";
            ++j;
        }
        return word;
    }

    private static String printEmo(String message) {
        String emo = "";
        int i = 0;
        while (i < 8) {
            emo = String.valueOf(emo) + BigChat.printCharacterLine(message, i);
            emo = String.valueOf(emo) + "\n";
            ++i;
        }
        return emo;
    }

    public static String printCharacterLine(String characterString, int row) {
        String[] array = ChatMaps.getMapFor(characterString);
        if (array == null) {
            return "";
        }
        String line = array[row];
        String charLine = "";
        int j = 0;
        while (j < line.length()) {
            char ch = line.charAt(j);
            charLine = ch == '0' ? String.valueOf(charLine) + pad : String.valueOf(charLine) + ink;
            ++j;
        }
        return charLine;
    }

}

