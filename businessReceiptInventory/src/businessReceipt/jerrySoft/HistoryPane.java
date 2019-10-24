package businessReceipt.jerrySoft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;



public class HistoryPane extends JDialog {
	private static final long serialVersionUID = 1L;
	private JCheckBox quantity_remainCheck;
	private DefaultTableModel model;

	private JTable table;
	private JComboBox<String> dateField;
	private JButton dateButton;
	private JLabel todaySaleLabel;
	private DefaultComboBoxModel<String> dateModel = new DefaultComboBoxModel<String>();
	
	public HistoryPane() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("TRANSACTION HISTORY");
		setSize(800, 630);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setModal(true);
		dateComboBox();// date combbox box

		initUI();

		URL urlImage = HistoryPane.class.getResource("./sbg image/SBGLog.JPG");

		Image icon = Toolkit.getDefaultToolkit().getImage(urlImage);

		setIconImage(icon);
	}
	private void initUI() {

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		JLabel titleLabel = new JLabel();
		titleLabel.setText("<html><h3 style = color:rgb(255,0,0)> TRANSACTION HISTORY<hr /></h3><html>");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		northPanel.add(titleLabel, BorderLayout.NORTH);
		// check box

		// date chooser
		quantity_remainCheck = new JCheckBox();

		quantity_remainCheck.addItemListener(new ItemRemainingListener());
		quantity_remainCheck.setPreferredSize(new Dimension(100, 20));
		JPanel northEastPanel = new JPanel(new FlowLayout());
		// add date and label to north east panel
		northEastPanel.add(new JLabel("Item On Stock"));
		northEastPanel.add(quantity_remainCheck);

		northPanel.add(northEastPanel, BorderLayout.EAST);

		add(northPanel, BorderLayout.NORTH);
		// center panel that contains table
		JPanel tablePanel = new JPanel();
		table = new JTable();
		String[] columnName = { "Product Name", "QuantityDeleted", "Update Date", "Delete Date", "Sale Date" };
		model = (DefaultTableModel) table.getModel();
		//table.setAutoResizeMode(2);;
		model.setColumnIdentifiers(columnName);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(700, 300));

		//
		// selection of date that item was bought
		dateField = new JComboBox<String>(dateModel);
		dateField.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		dateField.setEditable(true);
		// dateField = new JTextField("");
		dateField.setToolTipText("Enter Transaaction date");

		dateField.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		dateField.setPreferredSize(new Dimension(140, 30));
		tablePanel.add(dateField);
		dateButton = new JButton("Ok");
		dateButton.addActionListener(new BizDateLstener());
		tablePanel.add(dateButton);
		tablePanel.add(scroll);
		// table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(300);

		add(tablePanel, BorderLayout.CENTER);
		
		// daily sale label info
		todaySaleLabel = new JLabel("hello");
		todaySaleLabel.setForeground(Color.RED);
		JPanel southPanel = new JPanel();
		southPanel.setBorder(new EmptyBorder(20,20,20,20));
		southPanel.add(todaySaleLabel, BorderLayout.CENTER);
		
		add(southPanel, BorderLayout.SOUTH);

		// *****************************************************************************************************

		purchaseHistory();

	}
	
	private void remainingQuantity() {
		  PreparedStatement ps = null; ResultSet rs
	  = null; String countQuantity = "select Sum(TotalAmount) AS totalPaid from CustomerLog where Date = ?;";
	  try {
		  ps =  DbConnection.getConnection().prepareStatement(countQuantity);
	  ps.setString(1, (String) dateField.getSelectedItem());
	  rs = ps.executeQuery();
	  while (rs.next()) {

	int  totalPaid = rs.getInt("totalPaid"); 
	  
	NumberFormat nFormat =  NumberFormat.getInstance();
	
	todaySaleLabel.setText("TOTAL AMOUNT OF ITEM SOLD THIS DAY BEEN "+dateField.getSelectedItem()
	+" IS ====== "+nFormat.format(totalPaid)+"  Naira ==== "); 
	}
	  
	  } catch (SQLException exc) { exc.printStackTrace();
	  
	  } finally { try { if (rs != null) { rs.close();
	  DbConnection.getConnection().close();
	  
	 } } catch (SQLException exce) { exce.printStackTrace(); } }
	  
	  }

	private void purchaseHistory() {
		PreparedStatement ps = null;
		ResultSet rSet = null;
		String sql = "Select * From CustomerLog";
		try {
			ps = DbConnection.getConnection().prepareStatement(sql);

			// ps.setString(1, dateField.getText());
			rSet = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rSet));

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				DbConnection.getConnection().close();
			} catch (SQLException exc) {
				exc.printStackTrace();
			}
		}
	}

	// item on stock listener
	private class ItemRemainingListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {

			int state = e.getStateChange();
			if (state == ItemEvent.SELECTED) {
				selectRemainingItems();

			} else if (state == ItemEvent.DESELECTED) {
				purchaseHistory();
			}

		}

	}

	// method that select data from product categories
	private void selectRemainingItems() {
		String sql = "Select * from ProductCategories";
		PreparedStatement ps = null;
		try {
			ps = DbConnection.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null,
					"Error Just occured On Selection \nfrom of remaining product info" + ex.getMessage());
		}
	}

	private class BizDateLstener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			remainingQuantity(); // this method sum daily sale made
			
			PreparedStatement ps = null;
			ResultSet rSet = null;
			String sql = "Select * From CustomerLog where Date =?";
			try {
				ps = DbConnection.getConnection().prepareStatement(sql);

				ps.setString(1, (String) dateField.getSelectedItem());
				rSet = ps.executeQuery();
				table.setModel(DbUtils.resultSetToTableModel(rSet));

			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				try {
					DbConnection.getConnection().close();
				} catch (SQLException exc) {
					exc.printStackTrace();
				}
			}

		}
	}

	private void dateComboBox() {
		PreparedStatement ps = null;
		ResultSet rSet = null;
		String sql = "Select DISTINCT Date From CustomerLog";
		try {
			ps = DbConnection.getConnection().prepareStatement(sql);

			rSet = ps.executeQuery();
			while (rSet.next()) {
				String transDate = rSet.getString("Date");
				dateModel.addElement(transDate);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
