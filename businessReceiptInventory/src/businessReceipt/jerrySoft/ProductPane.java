package businessReceipt.jerrySoft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


public class ProductPane  extends JPanel{

	private static final long serialVersionUID = 1L;
	private JTextField productField, quantityField, unitField, sellingField, vendorField;
	private JPanel centerPanel;
	private JButton addB, updateB;
	private JCheckBox dbTable;
	private JTable table;
	private DefaultTableModel model;
	private JLabel southLabel;

	public ProductPane() {

		// productTable();

		setLayout(new BorderLayout());

		initUI();
		// allProduct();

	}

	private void initUI() {

		// north panel that hold title label
		JPanel northContainer = new JPanel(new GridLayout(1, 0, 20, 20));
		JLabel titleLabel = new JLabel();
		titleLabel.setText("<html><h3 style = color:rgb(255,255,255)>"
				+ "SBG INTEGRATED SYSTEMS-- STOCK KEEPING AND PRODUCTS INFORMATION MONITORING<hr /></h3><html>");

		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		northContainer.add(titleLabel);
		northContainer.setBackground(new Color(240, 98, 146));
		// center_Header is the description of panel that host center_panel

		JPanel centerHeader = new JPanel(new BorderLayout());
		centerHeader.setBackground(new Color(220, 237, 200));

		centerPanel = new JPanel(new GridLayout(10, 2, 10, 10));
		// centerPanel.setBackground(new Color(220,237,200));
		// centerPanel.setBorder(new EmptyBorder(10,10,10,10));
		JLabel vendorLabel = new JLabel("VENDOR:");
		vendorLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		JLabel productLabel = new JLabel("PRODUCT NAME:");
		productLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		JLabel quantityLabel = new JLabel("QUANTITY BOUGHT:");
		quantityLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		JLabel unitLabel = new JLabel("UNIT PRICE:");
		unitLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		JLabel sellingLabel = new JLabel("SELLING PRICE:");
		sellingLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		// fields
		vendorField = new JTextField();
		vendorField.setPreferredSize(new Dimension(250, 30));
		productField = new JTextField();
		productField.setPreferredSize(new Dimension(250, 30));
		quantityField = new JTextField();
		quantityField.setPreferredSize(new Dimension(250, 30));
		unitField = new JTextField();
		unitField.setPreferredSize(new Dimension(250, 30));
		sellingField = new JTextField();
		sellingField.setPreferredSize(new Dimension(250, 30));
		// add all label and fields to panel

		centerPanel.add(vendorLabel);
		centerPanel.add(vendorField);
		centerPanel.add(productLabel);
		centerPanel.add(productField);
		centerPanel.add(quantityLabel);
		centerPanel.add(quantityField);
		centerPanel.add(unitLabel);
		centerPanel.add(unitField);
		centerPanel.add(sellingLabel);
		centerPanel.add(sellingField);

		// adding center panel to the west of the panel(centerPanel)

		centerHeader.add(centerPanel, BorderLayout.CENTER);
		centerHeader.setBorder(new EmptyBorder(40, 40, 40, 40));

		// a panel that contains Buttons which will be added to the center_panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		addB = new JButton("ADD RECORD");
		addB.setPreferredSize(new Dimension(100, 30));
		addB.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		addB.addActionListener(new AddedItemListener());

		updateB = new JButton("UPDATE");
		updateB.setPreferredSize(new Dimension(100, 30));
		updateB.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
		updateB.addActionListener(new UpdateListener());

		buttonPanel.add(addB);
		buttonPanel.add(new JLabel(""));
		buttonPanel.add(updateB);
		buttonPanel.setBackground(new Color(240, 98, 146));
		buttonPanel.setBorder(new EmptyBorder(40, 20, 40, 20));

		// adding the panel to the panel header

		centerHeader.add(buttonPanel, BorderLayout.SOUTH);
		// allPanel.add(westPanel,BorderLayout.EAST);
		// westPanel.setBackground(new Color(240, 98, 146));
		JPanel centerImage = new JPanel();

		URL urlImage = ProductPane.class.getResource("./sbg image/MyStock.png");
		URL saleImage = ProductPane.class.getResource("./sbg image/items.jpg");

		ImageIcon image = new ImageIcon(urlImage);
		ImageIcon saleIcon = new ImageIcon(saleImage);

		centerImage.add(new JLabel(image),BorderLayout.NORTH);
		
		centerImage.add(new JLabel(saleIcon),BorderLayout.CENTER);
		centerImage.add(new JLabel(image),BorderLayout.SOUTH);
		
		add(northContainer, BorderLayout.NORTH);
		add(centerImage, BorderLayout.CENTER);
		add(centerHeader, BorderLayout.WEST);

		// *******************************************************************************************

		// table of product added to the database
		// this table display newly added vendor and items currently in the database
		String ColumnName[] = { "VendorName", "ProductName", "Quantity", "UnitPrice", "TotalPrice" };

		table = new JTable();
		table.setShowVerticalLines(false);
		table.setBackground(Color.ORANGE);
		table.addMouseListener(new TableMouseClick());
		// table.setBackground(Color.RED);
		model = (DefaultTableModel) table.getModel();
		model.setColumnIdentifiers(ColumnName);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(600, 200));

		// *******************************************************************************

		
		// south panel for table of data from the database
		JPanel eastPanel = new JPanel(new BorderLayout());
		eastPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


		dbTable = new JCheckBox("SHOW LIST OF PRODUCTS IN THE DATABASE");
		
		dbTable.addItemListener(new ProductListInDBListener());
		eastPanel.add(dbTable, BorderLayout.PAGE_START);
		eastPanel.add(scroll, BorderLayout.CENTER);
		eastPanel.setBackground(new Color(220, 237, 200));

		add(eastPanel, BorderLayout.EAST);
		JPanel southPanel = new JPanel();
		
		/*
		 * string array for displaying texts on the south panel
		 */
		String [] textList = {"SBG INTEGRATED SYSTEMS"
				, "LIST OF PRODUCTS ARE SAVED IN THE DATABASE", " STOCK KEEPING AND PRODUCTS INFORMATION MONITORING",
				"Designed by JerrySoft"};
		southLabel = new JLabel();
		southLabel.setForeground(new Color(220, 237, 200));
		southLabel.setFont(new Font("David",Font.BOLD,22));
		new Thread(new Runnable() {

			@Override
			public void run() {
				for(int i =0;i<200;i++) {
					for(int j =0;j<textList.length;j++) {
						
						southLabel.setText(textList[j]);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
			
		}).start();;
		
		
		southPanel.add(southLabel, BorderLayout.CENTER);
		southPanel.setBorder(new EmptyBorder(20,20,20,20));
		southPanel.setBackground(new Color(240, 98, 146));
		add(southPanel, BorderLayout.SOUTH);

	}

	// populating combo box and table in update panel
	/*private  void allProduct() {
		PreparedStatement ps = null;

		ResultSet rs = null;

		String qry = "Select * from ProductCategories" + " Order by Vendor, ProductName";

		try {
			ps = DbConnection.getConnection().prepareStatement(qry);

			rs = ps.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			/*while(rs.next()) {
				int id = rs.getInt("ID");
				String vendor = rs.getString("VendorName");
				String productName =rs.getString("ProductName");
				int qtty =rs.getInt("Quantity");
				int unitPrice =rs.getInt("UnitPrice");
				int sellingPrice =rs.getInt("SellingPrice");
				int total =rs.getInt("TotalAmount");
				model.addRow(new Object[] {
						id, vendor, productName,qtty, unitPrice, sellingPrice,total
				});
				
				
				
			}

		} catch (SQLException exc) {
			exc.printStackTrace();
		} finally {
			try {
				rs.close();
				ps.close();
				DbConnection.getConnection().close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}*/

	// adding item to the data base --this method addNewItem
	public void addNewItem() {

		//

		String vendorName = vendorField.getText().toString().toUpperCase().trim();
		String productName = productField.getText().toString().toUpperCase().trim();
		String quantityNo = quantityField.getText().toString().toUpperCase().trim();
		String unitNo = unitField.getText().toString().toUpperCase().trim();
		String sellingNo = sellingField.getText().toString().toUpperCase().trim();
		
		int totalAmount = (Integer.parseInt(quantityNo)*Integer.parseInt(sellingNo));
		
		
		
		// condition that check if text box is empty or not
		if (vendorName.isEmpty() || productName.isEmpty() || quantityNo.isEmpty() || unitNo.isEmpty()
				|| sellingNo.isEmpty() || quantityNo.contains(".")) {
			JOptionPane.showMessageDialog(null, "Empty text Field(s)...\n Check The entered Values!");
		} else {

			model.addRow(new String[] { vendorField.getText(), productField.getText(), quantityField.getText(),
					unitField.getText(), sellingField.getText() });
			

			PreparedStatement ps = null;

			String sql = "insert into ProductCategories (Vendor, ProductName, Quantity, UnitPrice, SellingPrice,"
					+ " TotalPrice)"
					+ " Values (?,?,?,?,?,?);";

			try {

				// ************************************************************************************************
				ps = DbConnection.getConnection().prepareStatement(sql);

				ps.setString(1, vendorName);
				ps.setString(2, productName);
				ps.setString(3, quantityNo);
				ps.setString(4, unitNo);
				ps.setString(5, sellingNo);
				ps.setInt(6, totalAmount);

				ps.executeUpdate();

				JOptionPane.showMessageDialog(null, "Data Saved Successfully...");
				vendorField.setText("");
				productField.setText("");
				quantityField.setText("");
				unitField.setText("");
				sellingField.setText("");

			} catch (SQLException sqlx) {
				JOptionPane.showMessageDialog(null,
						"The Vendor" + " name entered already\n Exist in the database\n" + sqlx);
			} finally {
				try {
					DbConnection.getConnection().close();
				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, "Error just occured" + ex);
				}
			}
		}
	}
	// select item from the product category and display on jlist

	private class AddedItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			addNewItem();
			// populating combo and JList method

		}

	}

	// update method
	public void upDate()

	{
		String vendorName = vendorField.getText().toString().toUpperCase().trim();
		String productName = productField.getText().toString().toUpperCase().trim();
		String quantityNo = quantityField.getText().toString().toUpperCase().trim();
		String unitNo = unitField.getText().toString().toUpperCase().trim();
		String sellingNo = sellingField.getText().toString().toUpperCase().trim();

		// *******************************************************************************************88
		if (vendorName.isEmpty() || productName.isEmpty() || quantityNo.isEmpty() || unitNo.isEmpty()
				|| sellingNo.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Empty text Fields\n Ensure that all fields are filled..");
		} else {

			PreparedStatement ps = null;
			String sql = "Update ProductCategories SET ProductName=?, Quantity=?, UnitPrice =?, SellingPrice =? where Vendor =?";
			try {
				ps = DbConnection.getConnection().prepareStatement(sql);

				// **********************************************************************************************************
				ps.setString(1, productName);
				ps.setString(2, quantityNo);
				ps.setString(3, unitNo);
				ps.setString(4, sellingNo);
				ps.setString(5, vendorName);

				ps.executeUpdate();
				JOptionPane.showMessageDialog(null, vendorName + " has been Updated successfully");
			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				try {
					DbConnection.getConnection().close();
					ps.close();
				} catch (SQLException exc) {
					exc.printStackTrace();
				}
			}
		}
	}

	// ***********************************************************************************************
	// update inner class
	private class UpdateListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			upDate();
			// refreshing combo and table on pressing the delete table
			//allProduct();
		}
	}
	// delete vendor name and it product from the database

	
	
	/*
	 * this class populate textfields when table row is clicked
	 * 
	 */
	private class TableMouseClick implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {

			int i = table.getSelectedRow();
			//vendorIdField.setText(model.getValueAt(i, 0).toString());
			vendorField.setText(model.getValueAt(i, 0).toString());
			productField.setText(model.getValueAt(i, 1).toString());
			quantityField.setText(model.getValueAt(i, 2).toString());
			unitField.setText(model.getValueAt(i, 3).toString());
			 sellingField.setText(model.getValueAt(i, 4).toString());

		}

		@Override
		public void mousePressed(MouseEvent e) {
			

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	/*
	 * this class display product name, venor and prices from database
	 */
	private class ProductListInDBListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {

			int state = e.getStateChange();
			if (state == ItemEvent.SELECTED) {
				
				ProductTablePane pPanel = new ProductTablePane();
				pPanel.setVisible(true);
				
			} else if(state == ItemEvent.DESELECTED) {
				
				
				
				
				}
			}

		}

}
