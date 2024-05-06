import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private BinaryTree binaryTree;
    private JTextField valueField;
    private JPanel treePanel;

    public Main() {
        setTitle("Binary Tree GUI");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        binaryTree = new BinaryTree();

        valueField = new JTextField(10);
        valueField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addNode();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        JButton addButton = new JButton("Add Node");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNode();
            }
        });

        treePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (binaryTree.getRoot() != null) {
                    drawTree(g, getWidth() / 2, 50, binaryTree.getRoot(), getWidth() / 4);
                }
            }
        };

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter Node Value:"));
        inputPanel.add(valueField);
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(treePanel), BorderLayout.CENTER);
    }

    private void drawTree(Graphics g, int x, int y, TreeNode node, int xOffset) {
        g.setColor(Color.BLACK);
        g.fillOval(x - 15, y - 15, 30, 30);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(node.getValue()), x - 5, y + 5);

        if (node.getLeft() != null) {
            int xLeft = x - xOffset;
            int yLeft = y + 60;
            g.setColor(Color.BLUE);
            g.drawLine(x+14, y, xLeft, yLeft);
            drawTree(g, xLeft, yLeft, node.getLeft(), xOffset / 2);
        }

        if (node.getRight() != null) {
            int xRight = x + xOffset;
            int yRight = y + 60;
            g.setColor(Color.RED);
            g.drawLine(x+14, y, xRight, yRight);
            drawTree(g, xRight, yRight, node.getRight(), xOffset / 2);
        }
    }

    private void refreshTreePanel() {
        treePanel.revalidate();
        treePanel.repaint();
    }

    private void addNode() {
        try {
            int value = Integer.parseInt(valueField.getText());
            binaryTree.insert(value);
            refreshTreePanel();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(Main.this, "Please enter a valid integer value.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}

class TreeNode {
    private int value;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(int value) {
        this.value = value;
        left = null;
        right = null;
    }

    public int getValue() {
        return value;
    }

    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}

class BinaryTree {
    private TreeNode root;
    public BinaryTree() {
        root = null;
    }
    public TreeNode getRoot() {
        return root;
    }
    public void insert(int value) {
        root = insertRecursive(root, value);
    }
    private TreeNode insertRecursive(TreeNode root, int value) {
        if (root == null) {
            return new TreeNode(value);
        }
        if (value < root.getValue()) {
            root.setLeft(insertRecursive(root.getLeft(), value));
        } else if (value > root.getValue()) {
            root.setRight(insertRecursive(root.getRight(), value));
        }

        return root;
    }
}
