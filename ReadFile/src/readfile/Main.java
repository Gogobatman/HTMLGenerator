package readfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {
    private File file,fileCreated;
    private FileWriter fw;
    private Scanner reader;
    private char signsInLine[];
    private char signs[][] = new char[100][100];
    private int separators,lineNumber,maxNumberInLines,wordsInLine;
    private String templateWords[]= new String[50];
    private String namesOfBooks[]= new String[50];
    private void namesOfBooksAndTemplateInit(){
        for(int i=0; i<namesOfBooks.length; i++){
            namesOfBooks[i]="";
            templateWords[i]="";
        }
    }
    private void fileOpen(){
        file = new File("C:\\Users\\dellLat01\\Desktop\\projekt\\new.html");
        try{
            reader = new Scanner(file);
        }catch (FileNotFoundException e){
            System.out.println("Couldn't find file");
        }
    }
    private boolean fileCreate(int a){
        namesOfBooks[0]="Template";
        fileCreated= new File("C:\\Users\\dellLat01\\Desktop\\projekt\\"+namesOfBooks[a-1]+".html");
        try{
            fileCreated.createNewFile();
            System.out.println("File created: " + fileCreated.getName());
            return true;
        }catch(Exception e){
            System.out.println("File already exists!");
            return false;
        }
    }
    private void fileWrite(int a){
        try{
            fw = new FileWriter("C:\\Users\\dellLat01\\Desktop\\projekt\\"+namesOfBooks[a-1]+".html");
            fw.write("<BODY>\n");
            fw.write("<ul>\n");
            if(a==1){
                for(int i=1; i<lineNumber; i++){
                    fw.write("<li><a href=\""+namesOfBooks[i]+".html\">"+namesOfBooks[i]+"</a></li>\n");
                }
                fw.write("</ul>\n");
                fw.write("</BODY>\n");
            }
        }catch (Exception e){
            System.out.println("Couldn't write to a file");
        }
    }
    private  void readOneLine(){
        String str = reader.nextLine();
        signsInLine =str.toCharArray();
        if(signsInLine.length>maxNumberInLines){
            maxNumberInLines=signsInLine.length;
        }
    }
    private void fileRead(){
        namesOfBooksAndTemplateInit();
        while(reader.hasNextLine()){
            boolean firstSeparatorDetected=false;
            readOneLine();
            for(int i=0; i<signsInLine.length; i++){
                signs[lineNumber][i]=signsInLine[i];
                if(signsInLine[i]==';'){
                    firstSeparatorDetected=true;
                }
                if(!firstSeparatorDetected && signsInLine[i]!=0){
                    namesOfBooks[lineNumber]+=signsInLine[i];
                }
            }
            if(separators==0){
                separators=countSeparators(signsInLine);
            }
            lineNumber++;
        }
        for(int j=0; j<lineNumber; j++){
            for(int i=0; i<maxNumberInLines; i++){
                System.out.print(signs[j][i]);
            }
            System.out.println(" ");
        }
        reader.close();
    }
    private int countSeparators(char c[]){
        for(int i=0; i<c.length; i++){
            if(c[i]==';'){
               separators++;
            }
        }
        wordsInLine=separators+1;
        return separators;
    }
    private void filterAndWriteOrLoad(int a, int variant){
        int separatorPosition=0;
        int separators=0;
        try{
            if(variant==2){
                fw.write("<li>"+templateWords[separators]+": ");
            }
            for(int j=a-1; j<a;j++){
                for(int i=0; i<maxNumberInLines; i++){
                    if(i==separatorPosition+1 && signs[j][separatorPosition]==';'){
                        continue;
                    }
                    else if(signs[j][i]!=';' && signs[j][i]!=0){
                        if(variant==2){
                            fw.write(signs[j][i]);
                        }
                        else if(variant==1){
                            templateWords[separators]+=signs[j][i];
                        }

                    }
                    else if(signs[j][i]==';'){
                        separatorPosition=i;
                        separators++;
                        if(variant==2){
                            fw.write("</li>\n");
                            fw.write("<li>"+templateWords[separators]+": ");
                        }

                    }
                    else if(signs[j][i]==0){
                        i+=100; //length of array is longer that it should be and this line fixes it
                    }
                }
            }
            if(variant==2){
                fw.write("</li>\n");
                fw.write("</ul>\n");
                fw.write("</BODY>\n");
            }

        }catch(Exception e){ }
    }
    public static void main(String[] args) {
	Main m = new Main();
	m.fileOpen();
	m.fileRead();
	for(int i=1; i<=m.lineNumber; i++){
        m.fileCreate(i);
        m.fileWrite(i);
        if(i==1){
            m.filterAndWriteOrLoad(i,1);
        }
        else{
            m.filterAndWriteOrLoad(i,2);
        }
        try{
            m.fw.close();
        }catch (Exception e){
            System.out.println("Couldn't close file!");
        }
    }
    }
}