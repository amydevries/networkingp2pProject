package FileHandling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public abstract class ConfigurationReader {

    public void parse(){
        String nameOfFileReadingFrom = getNameOfFileReadingFrom();
        ArrayList<String> lines = getLinesFromFile(nameOfFileReadingFrom);
        getValues(lines);
    }

    protected abstract String getNameOfFileReadingFrom();

    private ArrayList<String> getLinesFromFile(String nameOfFileReadingFrom){
        ArrayList<String> lines = new ArrayList<String>();
        try {
            FileReader reader = new FileReader(nameOfFileReadingFrom);

            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;

            while((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }

    protected abstract void getValues(ArrayList<String> lines);

}
