package com.amytime.convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

public class ConvertMain {
	public static void main(String [] args) {
		String header = new String("UserId,Latitude,Longitude,Junk,Altitude,NumDays,Timestamp\n");
		try{
			File file = new File("C:/Users/Amy/Documents/UserData/UserDataNov.csv");
			if(!file.exists()){
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(header);
			
			for(int i = 0; i <= 181; i++) {
				String fileNum = String.format("%03d", i);
				String myDirectoryPath = "C:/Users/Amy/Documents/Geolife/Geolife Trajectories 1.3/Data/" + fileNum + "/Trajectory/";
				File dir = new File(myDirectoryPath);
				File[] directoryListing = dir.listFiles();
				for (File child : directoryListing) {
					BufferedReader br = new BufferedReader(new FileReader(child));
					String line;
					int count = 1;
					while((line = br.readLine()) != null){
						//skip the first 6 lines
						if(count <= 6){
							count++;
							continue;
						} else {
							StringTokenizer st = new StringTokenizer(line, ",");
							String val = st.nextToken();
							for(int j =1; j<=5;j++){
								val = st.nextToken();
							}
							StringTokenizer st2 = new StringTokenizer(val, "-");
							String year = st2.nextToken();
							String month = st2.nextToken();
							int lastComma = line.lastIndexOf(',');
							String newLine = line.substring(0,lastComma) + ' ' + line.substring(lastComma+1,line.length());
							line = newLine;
							line = i + "," + line + "\n";
							if(month.equals("11") && year.equals("2008")) {
								bufferedWriter.write(line);
							}
						}
						
					}
					br.close();
				}
			}
			
			bufferedWriter.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
		System.out.println("DONE");
	}
}
