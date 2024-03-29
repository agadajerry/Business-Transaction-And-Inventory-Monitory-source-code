package businessReceipt.jerrySoft;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;



public class CustomerInfo  extends JDialog{
	private JTable table;
	private DefaultTableModel model;
	
	public CustomerInfo() {
		setModal(true);
		setTitle("Customer Table");
		setSize( 560, 600);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		
		//
		setLayout(new BorderLayout());
		Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\ENGR_IDOKO\\eclipse-workspace\\"
				+ "DataBases\\src\\receipt\\print\\sbg image\\sale.JPEG");
		setIconImage(icon);
		initialise();

		customerDetails();
	}
	private void initialise() {
		
		//panel center that hold table
		JPanel panelTable = new JPanel();
		
		String [] tableColumn = {"CustomerId","CustomerName","CustomerAddress", "Date"};
		
		table = new JTable();
		model =  (DefaultTableModel)table.getModel();
		model.setColumnIdentifiers(tableColumn);
		
		JScrollPane scroll = new JScrollPane(table);
		panelTable.add(new JLabel("Name of Customers"));
		panelTable.add(scroll);
		
		add(panelTable, BorderLayout.CENTER);
		
		
		
	}//end of initalise method
	
	public void customerDetails()
	{
		String sql= null;
		 PreparedStatement ps =null;
		 ResultSet rSet= null;
		sql = "Select * From CustomerList";
		try {
		ps = DbConnection.getConnection().prepareStatement(sql);
		rSet = ps.executeQuery();
		table.setModel(DbUtils.resultSetToTableModel(rSet));
		
		}catch(SQLException ex) {
			ex.printStackTrace();
		}finally {
			try {
				DbConnection.getConnection().close();
			}catch(SQLException exc) {
				exc.printStackTrace();
			}
		}
		
	}

}
