package WeatherApp;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class favs{
	ArrayList<String> favs = new ArrayList<String>();
	private Path uc = Paths.get("UsersChoice.txt");
	private Charset cs = Charset.forName("US-ASCII");
	private JPanel favPanel;
	private JRadioButton bom,forecast;
	private ButtonGroup bof;
	String dataResource;
	
	public favs() throws JsonIOException, JsonSyntaxException, MalformedURLException, IOException{
		JPanel p1,p2;
		p1 = new JPanel();
		p2 = new JPanel();
		double width = Toolkit.getDefaultToolkit().getScreenSize().width; 
		double height = Toolkit.getDefaultToolkit().getScreenSize().height;
		retriveDatafromBOM rb = new retriveDatafromBOM();
		retriveDatafromForecast rdff = new retriveDatafromForecast();
	
		//Get user's choices 
		try(BufferedReader reader = Files.newBufferedReader(uc,cs)) {
			while(reader.readLine()!=null){
				favs.add(reader.readLine());
			}
		} catch (IOException e1) {
				System.out.println("read failed");
				e1.printStackTrace();
		}
					
		//add selected station in favorites
		JButton addfav = new JButton("ADD");
		addfav.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			favs.add(rb.getCity().getSelectedItem().toString());
				try(BufferedWriter writer = Files.newBufferedWriter(uc,cs,StandardOpenOption.APPEND);) {
					writer.write("\r\n" + favs.get(favs.size()-1) + "\r\n");
				} catch (IOException e1) {
					System.out.println("write failed");
					e1.printStackTrace();
				}
		}});
					
		//remove selected station in favorites
		JButton removefav = new JButton("remove");
		removefav.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			favs.remove(rb.getCity().getSelectedItem().toString());
			try(BufferedWriter writer = Files.newBufferedWriter(uc,cs,StandardOpenOption.TRUNCATE_EXISTING);) {
				for(int i = 0;i<favs.size();i++){
				writer.write(favs.get(i) + "\r\n");
				}
			} catch (IOException e1) {
				System.out.println("write failed");
				e1.printStackTrace();
			}
		}});
					
		//confirm and display all selected stations
		/*JButton confirm = new JButton("OK");
		confirm.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			mainframe mf = new mainframe();
			try {mf.favFrame(dataResource);}
			catch (JsonIOException | JsonSyntaxException | IOException e1) {e1.printStackTrace();}
		}});*/
		p1=rb.getMainPanel();
		p1.setSize((int)width/2, (int)height/4);
		p1.add(addfav);
		p1.add(removefav);
	//	p1.add(confirm);
		
		bom = new JRadioButton("the BOM weather");
		forecast = new JRadioButton("the Forecast weather");
		
		ActionListener sliceActionListener = new ActionListener() {
		      public void actionPerformed(ActionEvent actionEvent) {
		        dataResource = actionEvent.getActionCommand(); 
		      try {
		    	  mainframe mf = new mainframe();
		    	  mf.favFrame(dataResource);}
				catch (JsonIOException | JsonSyntaxException | IOException e1) {e1.printStackTrace();}
		      }
		};
		    
		bom.addActionListener(sliceActionListener);
		forecast.addActionListener(sliceActionListener);
		 
		bof = new ButtonGroup();
		bof.add(bom);
		bof.add(forecast);
		p2.setSize((int)width/2, (int)height/4);
		p2.add(bom);
		p2.add(forecast);
			
		favPanel = new JPanel(new BorderLayout());
		favPanel.add(p1,BorderLayout.NORTH);
		favPanel.add(p2,BorderLayout.SOUTH);
	}
	//return some value
	public ArrayList<String> getFavs(){
		return favs;
	}
	public JPanel getFavPa(){
		return favPanel;
	}
	public String getDataResorce(String dataresource){
		return dataResource = dataresource;
	}
	public String getResource(){
		return dataResource;
	}
}