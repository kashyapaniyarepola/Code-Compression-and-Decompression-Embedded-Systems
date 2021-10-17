import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.*;

public class SIM {

    public static int maximum (ArrayList<Integer> countArray) {
        int max = countArray.get(0);
        int maxIndex = 0;
        for (int i=1; i<countArray.size(); i++) {
            if (countArray.get(i) > max) {
                max = countArray.get(i);
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static String getBinaryValue(int number) {
        String key = Integer.toBinaryString(number);
        if (key.length() == 1) {
            key = "0000"+key;
        } else if (key.length() == 2) {
            key = "000"+key;
        } else if (key.length() == 3) {
            key = "00"+key;
        } else if (key.length() == 4) {
            key = "0"+key;
        }

        return key;
    }

    public static String checkMismatch(ArrayList<String> binaries, ArrayList<String> finalfreq, String element, int method) {
        String addElement = "";
        for (int i = 0; i < binaries.size(); i++) {
            String value = binaries.get(i);
            String xorChar = "";
            int positionOne = 100;
            int positionTwo =100;
            int positionThree=100;
            int positionFour=100;
            int position = 100;
            int numberOfOnes = 0;
            for (int j = 0; j < value.length(); j++) {
                Character x = value.charAt(j);
                Character y = element.charAt(j);
                if (x.equals(y)) {
                    xorChar = xorChar + 0;
                } else {
                    xorChar = xorChar + 1;
                    position = j;
                    if (position != 100 && positionOne == 100) {
                        positionOne = position;
                    } else if (position != 100 && positionTwo == 100) {
                        positionTwo = position;
                    } else if (position != 100 && positionThree == 100) {
                        positionThree = position;
                    } else if (position != 100 && positionFour == 100) {
                        positionFour = position;
                    }
                    numberOfOnes = numberOfOnes + 1;
                    // position = j;
                }
            }
            
            if (method ==1 && numberOfOnes == 1) {
                addElement = "011" + getBinaryValue(positionOne) + finalfreq.get(i);
                break;
            } else if (method == 2 && numberOfOnes == 2) {
                if ((positionTwo - positionOne) == 1) {
                    addElement = "100" + getBinaryValue(positionOne) + finalfreq.get(i);
                    break;
                }    
            } else if (method == 3 && numberOfOnes == 4) {
                if ((positionFour - positionOne) == 3) {
                    addElement = "101" + getBinaryValue(positionOne) + finalfreq.get(i);
                    break;
                }
            } else if (method == 4 && (numberOfOnes == 2 || numberOfOnes == 3)) {
                if (((positionTwo - positionOne) <= 3) || ((positionThree - positionOne) <= 3)) {
                    addElement = "010" + getBinaryValue(positionOne) + xorChar.charAt(positionOne) +xorChar.charAt(positionOne+1)+xorChar.charAt(positionOne +2)+xorChar.charAt(positionOne+3) + finalfreq.get(i);
                    break;
                }   
            }  else if (method == 5 && numberOfOnes == 2) {
                addElement = "110" + getBinaryValue(positionOne) + getBinaryValue(positionTwo) + finalfreq.get(i);
                break;       
            }
        }
        return addElement;
    }

    public static void compression() {

        ArrayList<String> binaries = new ArrayList<String>();
        ArrayList<String> finalfreq = new ArrayList<String>();
        ArrayList<String> freqArray = new ArrayList<String>();
        ArrayList<Integer> countArray = new ArrayList<Integer>();
        List<String> dataArray = new ArrayList<String>();
        List<String> dataArraynew = new ArrayList<String>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("original.txt"));
            String line;

            File file = new File("cout.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter pw = new PrintWriter(file);
            
            while ((line = br.readLine()) != null) { 
                dataArray.add(line); 
                dataArraynew.add(line); 
            }
            

            for (int i = 0; i<dataArraynew.size(); i++) {
                String ele = dataArraynew.get(i);
                if (ele.equals("0")) {
                    continue;
                } else {
                    int tot = 1;
                    for (int j = i+1; j < dataArraynew.size(); j++) {
                        if (ele.equals(dataArraynew.get(j))) {
                            tot = tot +1;
                            dataArraynew.set(j, "0");
                        }
                    }
                    countArray.add(tot);
                    freqArray.add(ele);
                }
            }
            // Set<String> unique = new HashSet<String>(dataArray);
            // for (String i : unique) {
            //     freqArray.add(i);
            //     countArray.add(Collections.frequency(dataArray, i));
            // }
            
            for (int i = 0; i<16; i++) {
                int maxIndex = maximum(countArray);
                String maxFreqValue = freqArray.get(maxIndex);
                countArray.set(maxIndex, 0);
                String key = Integer.toBinaryString(i);
                if (key.length() == 1) {
                    key = "000"+key;
                } else if (key.length() == 2) {
                    key = "00"+key;
                } else if (key.length() == 3) {
                    key = "0"+key;
                }
                binaries.add(maxFreqValue);
                finalfreq.add(key);
            }
            
            String compressedElement = "";
            boolean rleEnabled = false;
            boolean rleChecked = true;
            for (int i = 0; i < dataArray.size(); i++) {
                String element = dataArray.get(i);
                if (i == 0) {
                    rleEnabled = false;
                } else if (rleChecked && (element.equals(dataArray.get(i-1)))) {
                    rleEnabled = true;
                }
                
                String addElement = "";
                if (rleEnabled) {
                    int numberOfChecks = 0;
                    for (int j=i+1; j < dataArray.size(); j++) {
                        if (element.equals(dataArray.get(j))) {
                            numberOfChecks++;
                            if (numberOfChecks == 8) {
                                rleChecked = false;
                                numberOfChecks = 7;
                                i = i + numberOfChecks;
                                break;
                            }
                        } else {
                            i = i + numberOfChecks;
                            break;
                        }
                    }
                    String addKey = Integer.toBinaryString(numberOfChecks);
                    if (addKey.length() == 1) {
                        addKey = "00"+addKey;
                    } else if (addKey.length() == 2) {
                        addKey = "0"+addKey;
                    }
                    rleEnabled = false;
                    addElement = "001" + addKey;
                    
                   
                } else {
                    if (binaries.contains(element)) {
                        addElement = "111" + finalfreq.get(binaries.indexOf(element));
                    } else if (checkMismatch(binaries, finalfreq, element, 1) !=""){
                        addElement = checkMismatch(binaries, finalfreq, element, 1);
                    } else if (checkMismatch(binaries, finalfreq, element, 2) !="") {
                        addElement = checkMismatch(binaries, finalfreq, element, 2);
                    } else if (checkMismatch(binaries, finalfreq, element, 3) !="") {
                        addElement = checkMismatch(binaries, finalfreq, element, 3);
                    } else if (checkMismatch(binaries, finalfreq, element, 4) !="") {
                        addElement = checkMismatch(binaries, finalfreq, element, 4);
                    } else if (checkMismatch(binaries, finalfreq, element, 5) !="") {
                        addElement = checkMismatch(binaries, finalfreq, element, 5);
                    } else {
                        addElement = "000" + element;
                    }
                    
                    rleChecked = true;
                }

                compressedElement = compressedElement + addElement;
                addElement = "";
            }

            int countString = 0;
            String lineString = "";
            for (int s = 0; s < compressedElement.length(); s++) {
                countString++;
                lineString = lineString + Character.toString(compressedElement.charAt(s));
                if (lineString.length() == 32) {
                    pw.println(lineString);
                    lineString = "";
                }

                if (countString == compressedElement.length() && lineString != "" && lineString.length() < 32) {
                    int missingZeros = 32 - lineString.length();
                    String addString = "";
                    for (int k = 0; k<missingZeros; k++) {
                        addString += "0";
                    }
                    lineString = lineString + addString;
                    pw.println(lineString);
                    lineString = "";
                }
            }
            pw.println("xxxx");
            for (int l = 0; l<binaries.size(); l++) {
                pw.println(binaries.get(l));
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }   
        }
        
    }

    public static void decompression() {

        ArrayList<String> binaries = new ArrayList<String>();
        ArrayList<String> frequency = new ArrayList<String>();
        ArrayList<String> lines = new ArrayList<String>();
        ArrayList<String> finalString = new ArrayList<String>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("compressed.txt"));
            String line;

            File file = new File("dout.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter pw = new PrintWriter(file);
            
            while ((line = br.readLine()) != null) { 
                lines.add(line);        
            }
            int xIndex = lines.indexOf("xxxx");
            lines.remove(xIndex);
            String compressedElement ="";
            int count = 0;
            for (int i = 0; i< lines.size(); i++) {
                if (i >= xIndex) {
                    binaries.add(lines.get(i));
                    String key = Integer.toBinaryString(count);
                    if (key.length() == 1) {
                        key = "000"+key;
                    } else if (key.length() == 2) {
                        key = "00"+key;
                    } else if (key.length() == 3) {
                        key = "0"+key;
                    }
                    count++;
                    frequency.add(key);
                } else {
                    compressedElement = compressedElement + lines.get(i);
                }
            }

            String addElement = "";
            String tagElement = "";
            for (int j = 0; j < compressedElement.length(); j++) {
                // System.out.println(j);
                tagElement = tagElement + Character.toString(compressedElement.charAt(j));
                if (tagElement.length() == 3) {
                    if (tagElement.equals("111")) {
                        String checkDic = compressedElement.substring(j+1, j+5);
                        addElement = binaries.get(frequency.indexOf(checkDic));
                        finalString.add(addElement);
                        j=j+4;
                    } else if (tagElement.equals("001")) {
                        addElement = finalString.get(finalString.size() -1);
                        int decimal=Integer.parseInt(compressedElement.substring(j+1, j+4),2);
                        finalString.add(addElement);
                        for (int dec = 0; dec<decimal; dec++) {
                            finalString.add(addElement);
                        }
                        j=j+3;
                    } else if (tagElement.equals("010")) {
                        int startLoc = Integer.parseInt(compressedElement.substring(j+1, j+6), 2);
                        String bitmask = compressedElement.substring(j+6, j+10);
                        String dicValue = compressedElement.substring(j+10, j+14);
                        String dicElement = binaries.get(frequency.indexOf(dicValue));
                        String firstHalf = dicElement.substring(0,startLoc);
                        String secondHalf = dicElement.substring(startLoc, startLoc+4);
                        String thirdHalf = "";
                        if (firstHalf.length()+secondHalf.length() <32) {
                            thirdHalf = dicElement.substring(startLoc+4);
                        }
                        String xor ="";
                        for (int i = 0; i<4; i++) {
                            Character x = secondHalf.charAt(i);
                            Character y = bitmask.charAt(i);
                            if (x.equals(y)) {
                                xor += "0";
                            } else {
                                xor += "1";
                            }
                        }
                        addElement = firstHalf + xor + thirdHalf;
                        finalString.add(addElement);
                        j=j+13;
                    } else if (tagElement.equals("011")) {
                        int startLoc = Integer.parseInt(compressedElement.substring(j+1, j+6), 2);
                        String dicValue = compressedElement.substring(j+6, j+10);
                        String dicElement = binaries.get(frequency.indexOf(dicValue));
                        String firstHalf = dicElement.substring(0,startLoc);
                        String secondHalf = dicElement.substring(startLoc, startLoc+1);
                        String thirdHalf = "";
                        if (firstHalf.length()+secondHalf.length() <32) {
                            thirdHalf = dicElement.substring(startLoc+1);
                        }
                        if (secondHalf.equals("1")) {
                            secondHalf = "0";
                        } else {
                            secondHalf = "1";
                        }
                        addElement = firstHalf + secondHalf + thirdHalf;
                        finalString.add(addElement);
                        j=j+9;
                    } else if (tagElement.equals("100")) {
                        int startLoc = Integer.parseInt(compressedElement.substring(j+1, j+6), 2);
                        String dicValue = compressedElement.substring(j+6, j+10);
                        String dicElement = binaries.get(frequency.indexOf(dicValue));
                        String firstHalf = dicElement.substring(0,startLoc);
                        String secondHalf = dicElement.substring(startLoc, startLoc+2);
                        String thirdHalf = "";
                        if (firstHalf.length()+secondHalf.length() <32) {
                            thirdHalf = dicElement.substring(startLoc+2);
                        }
                        String newBits = "";
                        for (int i=0; i<secondHalf.length(); i++) {
                            Character x = secondHalf.charAt(i);
                            if (x.equals("1")) {
                                newBits = newBits + "0";
                            } else {
                                newBits = newBits + "1";
                            }
                        }
                        addElement = firstHalf + newBits + thirdHalf;
                        finalString.add(addElement);
                        j=j+9;
                    } else if (tagElement.equals("101")) {
                        int startLoc = Integer.parseInt(compressedElement.substring(j+1, j+6), 2);
                        String dicValue = compressedElement.substring(j+6, j+10);
                        String dicElement = binaries.get(frequency.indexOf(dicValue));
                        String firstHalf = dicElement.substring(0,startLoc);
                        String secondHalf = dicElement.substring(startLoc, startLoc+4);
                        String thirdHalf = "";
                        if (firstHalf.length()+secondHalf.length() <32) {
                            thirdHalf = dicElement.substring(startLoc+4);
                        }
                        String newBits = "";
                        for (int i=0; i<secondHalf.length(); i++) {
                            Character x = secondHalf.charAt(i);
                            if (x.equals("1")) {
                                newBits = newBits + "0";
                            } else {
                                newBits = newBits + "1";
                            }
                        }
                        addElement = firstHalf + newBits + thirdHalf;
                        finalString.add(addElement);
                        j=j+9;
                    } else if (tagElement.equals("110")) {
                        int firstLoc = Integer.parseInt(compressedElement.substring(j+1, j+6), 2);
                        int secondLoc = Integer.parseInt(compressedElement.substring(j+6, j+11), 2);
                        String dicValue = compressedElement.substring(j+11, j+15);
                        String dicElement = binaries.get(frequency.indexOf(dicValue));
                        String firstHalf = dicElement.substring(0,firstLoc);
                        String secondHalf = dicElement.substring(firstLoc, firstLoc+1);
                        String thirdHalf = dicElement.substring(firstLoc+1, secondLoc);
                        String forthHalf = dicElement.substring(secondLoc, secondLoc +1);
                        String fifthHalf = "";
                        if (firstHalf.length()+secondHalf.length()+thirdHalf.length()+forthHalf.length() <32) {
                            fifthHalf = dicElement.substring(secondLoc +1);
                        }
                        if (secondHalf.equals("1")) {
                            secondHalf = "0";
                        } else {
                            secondHalf = "1";
                        }

                        if (forthHalf.equals("1")) {
                            forthHalf = "0";
                        } else {
                            forthHalf = "1";
                        }
                        addElement = firstHalf + secondHalf + thirdHalf +forthHalf + fifthHalf;
                        finalString.add(addElement);
                        j=j+14;
                    } else if (tagElement.equals("000")) {
                        if (j+33 < compressedElement.length()) {
                            addElement = compressedElement.substring(j+1, j+33);
                            finalString.add(addElement);
                        }
                        j=j+32;
                    } 
                    tagElement = "";
                    
                }
                
            }
            
            for (int l = 0; l<finalString.size(); l++) {
                pw.println(finalString.get(l));
            }
            // System.out.println(finalString);
            // System.out.println(binaries);
            // System.out.println(compressedElement);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }   
        }
        
    }

    public static void main(String args[]) {

        int firstArg = 0;
        if (args.length > 0) {
            try {
                firstArg = Integer.parseInt(args[0]);
            } catch (NumberFormatException e){
                System.err.println("Argument" + args[0] + " must be an integer");
                System.exit(1);
            }
        }

        if (firstArg == 1) {
            compression();    
        } else if (firstArg == 2) {
            decompression();
        } else {
            System.out.println("Please Enter Valid Argument");
        }
        

    }
}