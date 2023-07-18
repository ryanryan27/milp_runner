import ilog.concert.IloException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ListIterator;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class milp {
    public static void main(String[] args) throws IloException, IOException {
        double timeout = 0;

        if(args.length == 0){
            System.out.println("Correct input is:");
            System.out.println("milp <dom type> <filename> <timeout (s): optional>");
            System.out.println("dom numbers:");
            System.out.println(" 1 - domination");
            System.out.println(" 2 - total");
            System.out.println(" 3 - secure");
            System.out.println(" 4 - roman");
            System.out.println(" 5 - weak roman");
            System.out.println(" 6 - connected");
            System.out.println(" 7 - upper 1");
            System.out.println(" 8 - upper 2");
            System.out.println(" 9 - two domination");
            return;
        }

        if(args.length < 2) {
            System.out.println("not enough args: " +args.length);
            return;
        }

        if(args.length > 2){
            timeout = Double.parseDouble(args[2]);
        }

        String filename = args[1];
        int dom_type = Integer.parseInt(args[0]);

        //Initialise output file
        String out_file_name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss'.csv'").format(new Date());

        File out_file = new File("./results/" + out_file_name);

        Files.createDirectories(Paths.get("./results/"));

        BufferedWriter bw = new BufferedWriter(new FileWriter(out_file));
        bw.write("filename, dom_type, result, state");
        bw.newLine();
        bw.close();


        //Read in file or files from directory
        FileParser fp = new FileParser();
        File in_file = new File(filename);
        ArrayList<File> files = new ArrayList<>(); 

        if (in_file.isDirectory()){
            files.addAll(Arrays.asList(in_file.listFiles()));
        } else {
            files.add(in_file);
        }

        ListIterator<File> iterator = files.listIterator();

        while (iterator.hasNext()){
            
            File file = iterator.next();   
            
            System.out.println(file.getPath());

            if(file.isDirectory()){
                for (File f : file.listFiles()){
                    iterator.add(f);
                    iterator.previous();
                }
                continue;
            }

            // System.out.println(file.getPath());
        
            Graph g = fp.parseEdgeList(file)[0].graph;

            MILPRunner mr = new MILPRunner(dom_type, g);

            double[] domset = mr.run(timeout);

            int sum = 0;
            for(int i = 0; i < g.getN(); i++){
                sum += Math.round(domset[i]);
            }

            if(dom_type == 4 || dom_type == 5){
                for(int i = 0; i < g.getN(); i++){
                    sum += 2*Math.round(domset[g.getN()+i]);
                }
    
            }

            
            String csv_string = file.getName() + ", " + dom_type + ", " + sum + ", " + mr.getRunState(); 
            System.out.println(csv_string);

            bw = new BufferedWriter(new FileWriter(out_file, true));
            bw.write(csv_string);
            bw.newLine();
            bw.close();
        }
    }
}
