import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private BinaryTree binaryTree;
    private JTextField valueField;
    private JPanel treePanel;
    private JTextField removeValueField;

    public Main() {
        setTitle("Binary Tree GUI");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        binaryTree = new BinaryTree();

        valueField = new JTextField(10);
        valueField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addNode();
                }

            }

            public void keyReleased(KeyEvent e) {
            }
        });

        JButton addButton = new JButton("Add Node");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNode();
            }
        });

        removeValueField = new JTextField(10);
        removeValueField.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    removeNode();
                }

            }

            public void keyReleased(KeyEvent e) {
            }
        });

        JButton removeButton = new JButton("Remove Node");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeNode();
            }
        });

        treePanel = new JPanel() {
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
        inputPanel.add(new JLabel("Remove Node Value:"));
        inputPanel.add(removeValueField);
        inputPanel.add(removeButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(treePanel), BorderLayout.CENTER);
    }

    private void drawTree(Graphics g, int x, int y, TreeNode node, int xOffset) {
        g.setColor(Color.BLACK);
        g.fillOval(x - 15, y - 15, 30, 30);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(node.getValue()), x - 5, y + 5);
        int xRight;
        int yRight;
        if (node.getLeft() != null) {
            xRight = x - xOffset;
            yRight = y + 60;
            g.setColor(Color.BLUE);
            g.drawLine(x + 14, y, xRight, yRight);
            drawTree(g, xRight, yRight, node.getLeft(), xOffset / 2);
        }

        if (node.getRight() != null) {
            xRight = x + xOffset;
            yRight = y + 60;
            g.setColor(Color.RED);
            g.drawLine(x + 14, y, xRight, yRight);
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
            valueField.setText(""); // Clear the text field
        } catch (NumberFormatException var2) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer value.");
        }

    }

    private void removeNode() {
        try {
            int value = Integer.parseInt(removeValueField.getText());
            if (binaryTree.search(value)) {
                binaryTree.delete(value);
                refreshTreePanel();
            }
            else {
                JOptionPane.showMessageDialog(this, "Node with value " + value + " not found.");
            }
            removeValueField.setText(""); // Clear the text field
        } catch (NumberFormatException var2) {
            JOptionPane.showMessageDialog(this, "Please enter a valid integer value.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}

class BinaryTree {
    private TreeNode root = null;

    public BinaryTree() {
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
        } else {
            if (value < root.getValue()) {
                root.setLeft(insertRecursive(root.getLeft(), value));
            } else if (value > root.getValue()) {
                root.setRight(insertRecursive(root.getRight(), value));
            }

            return root;
        }
    }

    public boolean search(int value) {
        return searchRecursive(root, value);
    }

    private boolean searchRecursive(TreeNode root, int value) {
        if (root == null) {
            return false;
        } else if (root.getValue() == value) {
            return true;
        } else if (value < root.getValue()) {
            return searchRecursive(root.getLeft(), value);
        } else {
            return searchRecursive(root.getRight(), value);
        }
    }

    public void delete(int value) {
        root = deleteRecursive(root, value);
    }

    private TreeNode deleteRecursive(TreeNode root, int value) {
        if (root == null) {
            return null;
        }
        if (value < root.getValue()) {
            root.setLeft(deleteRecursive(root.getLeft(), value));
        } else if (value > root.getValue()) {
            root.setRight(deleteRecursive(root.getRight(), value));
        } else {
            if (root.getLeft() == null) {
                return root.getRight();
            } else if (root.getRight() == null) {
                return root.getLeft();
            }
            root.setValue(minValue(root.getRight()));
            root.setRight(deleteRecursive(root.getRight(), root.getValue()));
        }
        return root;
    }

    private int minValue(TreeNode node) {
        int minv = node.getValue();
        while (node.getLeft() != null) {
            minv = node.getLeft().getValue();
            node = node.getLeft();
        }
        return minv;
    }
}
class TreeNode {
    private int value;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public TreeNode getLeft() {
        return this.left;
    }

    public TreeNode getRight() {
        return this.right;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
