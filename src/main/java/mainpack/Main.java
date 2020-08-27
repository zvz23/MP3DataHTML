package mainpack;

import com.mpatric.mp3agic.*;
import htmlpack.HTMLTableGenerator;
import mp3extractpack.MP3Manager;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.*;



public class Main{

    public static void main(String[] args) throws IOException, InvalidDataException, UnsupportedTagException {

        System.out.println("Finding mp3 files...");
        MP3Manager manager = new MP3Manager(parseArgs(args).get("dir"));
        System.out.println("Done...");
        List<Mp3File> mp3Files = manager.getMp3Files();
        List<String[]> rows = new ArrayList<>();
        int count = 1;
        System.out.println("Parsing mp3 files...");
        for (Mp3File tempMP3 : mp3Files) {
            Object tempTag = MP3Manager.getTag(tempMP3);
            String title = "";
            String artist = "";
            if (tempTag instanceof ID3v1) {
                ID3v1 tag = (ID3v1) tempTag;
                title = tag.getTitle() == null ? getFileNameOnly(tempMP3.getFilename()) : tag.getTitle();
                artist = tag.getArtist() == null ? "Unknown" : tag.getArtist();
            } else if (tempTag instanceof ID3v2) {
                ID3v2 tag = (ID3v2) tempTag;
                title = tag.getTitle() == null ? getFileNameOnly(tempMP3.getFilename()) : tag.getTitle();
                artist = tag.getArtist() == null ? "Unknown" : tag.getArtist();
            } else {
                title = "Unknown";
                artist = "Unknown";
            }
            rows.add(new String[]{String.valueOf(count++), title, artist});
        }
        System.out.println("Done...");
        List<String> header = new ArrayList<>();
        header.add("ID no.");
        header.add("Title");
        header.add("Artist");
        System.out.println("Creating html file...");
        HTMLTableGenerator generator = new HTMLTableGenerator(header);
        generator.setHtmlPath("C:\\Users\\ADMIN\\Desktop\\index.html");
        generator.setTableData(rows);
        generator.generateTable();
        System.out.println("Done...");

    }

    private static String getFileNameOnly(String fileName){
        String regex = "(.+\\\\+)*(?<name>.+\\.mp3)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileName);
        if(matcher.matches()){
            return matcher.group("name");
        }
        return "";
    }

    private static HashMap<String, String> parseArgs(String[] args){
        HashMap<String, String> directoryMap = new HashMap<>();
        if(args.length == 0){
            throw new IllegalArgumentException("Error: no argument");
        }
        if(args.length > 2){
            throw new IllegalArgumentException("Error: too many arguments");
        }
        if(args[0].equals("-dir")){
            File tempDir = new File(args[1]);
            if(!(tempDir.exists() && tempDir.isDirectory())){
                throw new IllegalArgumentException("Error: invalid directory");
            }
            directoryMap.put("dir", args[1]);
        }
        return directoryMap;
    }



    
}
