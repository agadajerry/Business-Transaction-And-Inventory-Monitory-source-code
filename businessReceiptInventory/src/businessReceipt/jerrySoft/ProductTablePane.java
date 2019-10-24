package businessReceipt.jerrySoft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;

public class ProductTablePane extends JDialog{
	
	private DefaultTableModel model;

	private JTable table;
	private JButton refreshB;
	private JTextField vendorIdField;
	
public ProductTablePane() {
		

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("LIST OF PRODUCT ON STOCK");
		setSize(800, 580);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setModal(true);
		 
		
		URL urlImage = ProductTablePane.class.getResource("./sbg image/SBGLog.JPG"); 

		Image icon = Toolkit.getDefaultToolkit().getImage(urlImage);
				
		setIconImage(icon);
		
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		JLabel titleLabel = new JLabel();
		titleLabel.setText("<html><h3 style = color:rgb(255,0,43)> PRODUCT INFORMATION IN DATABASE<hr /></h3><html>");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

		northPanel.add(titleLabel, BorderLayout.NORTH);
		add(northPanel, BorderLayout.NORTH);
		//
		// center panel that contains table
				JPanel tablePanel = new JPanel();
				table = new JTable();
				String[] columnName = { "Product Name", "QuantityDeleted", "Update Date", "Delete Date", "Sale Date" };
				model = (DefaultTableModel) table.getModel();
				model.setColumnIdentifiers(columnName);
				JScrollPane scroll = new JScrollPane(table);
				scroll.setPreferredSize(new Dimension(700, 300));
				tablePanel.add(scroll);
				add(tablePanel, BorderLayout.CENTER);
				//
				// panel that host search text field and panel table
				JPanel deleteVendor_panel = new JPanel(new FlowLayout());
				deleteVendor_panel.setBorder(new EmptyBorder(20, 20, 20, 20));

				JButton deleteVendorBtn = new JButton("DELETE");
				deleteVendorBtn.setPreferredSize(new Dimension(150, 30));
				deleteVendorBtn.addActionListener(new VendorDelete());
				deleteVendorBtn.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));

				vendorIdField = new JTextField();
				vendorIdField.setPreferredSize(new Dimension(100, 30));

				//
				// refresh the table after purchase has been made
				refreshB = new JButton("RERESH");
				refreshB.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
				refreshB.setPreferredSize(new Dimension(100, 30));
				refreshB.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
						// sqlite query method for selecting values from the product table
						allProduct();
					}

				});

				deleteVendor_panel.add(refreshB);

				JLabel idLabel = new JLabel("Enter Vendor Name or ID to Delete:");
				idLabel.setPreferredSize(new Dimension(230, 30));
				idLabel.setBorder(BorderFactory.createMatteBorder(1, 6, 2, 7, Color.BLACK));
				deleteVendor_panel.add(idLabel);
				deleteVendor_panel.add(vendorIdField);
				deleteVendor_panel.add(deleteVendorBtn);
				add(deleteVendor_panel, BorderLayout.SOUTH);
	}
	// populating combo box and table in update panel
		public void allProduct() {
			PreparedStatement ps = null;

			ResultSet rs = null;

			String qry = "Select * from ProductCategories" + " Order by Vendor, ProductName";

			try {
				ps = DbConnection.getConnection().prepareStatement(qry);

				rs = ps.executeQuery();
				table.setModel(DbUtils.resultSetToTableModel(rs));

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
		}
		private class VendorDelete implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {

				String vendorId = vendorIdField.getText().trim().toString().toUpperCase();

				String deleteQry = "Delete from ProductCategories where ID =?";
				int choice = JOptionPane.showConfirmDialog(null, "Do you Want to Delete Vendor With Id " + vendorId);
				if (choice == JOptionPane.YES_OPTION) {
					PreparedStatement ps = null;

					try {
						ps = DbConnection.getConnection().prepareStatement(deleteQry);
						ps.setString(1, vendorId);

						ps.execute();
						 allProduct();// refresh the table of products
						vendorIdField.setText("");

					} catch (SQLException exc) {
						exc.printStackTrace();
					} finally {
						try {
							ps.close();
							DbConnection.getConnection().close();
						} catch (SQLException exc) {
							exc.printStackTrace();
						}
					}
				} else {
					return;
				}

			}

		}
}
