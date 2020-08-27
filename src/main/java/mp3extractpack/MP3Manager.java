package mp3extractpack;

import com.mpatric.mp3agic.*;

import java.io.FileFilter;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
public class MP3Manager {

    private String rootDirectory;
    private File rootDirFile;
    private List<Mp3File> mp3Files;
    private final FileFilter filter = pathname -> pathname.getName().toLowerCase().endsWith(".mp3") || (pathname.isDirectory() && !pathname.getName().startsWith("."));


    public MP3Manager(String directory) throws InvalidDataException, IOException, UnsupportedTagException {
        mp3Files = new ArrayList<>();
        setDirectory(directory);
        findMP3Files(rootDirFile);
    }

    public String getDirectory() {
        return rootDirectory;
    }

    public void setDirectory(String directory) {
        rootDirFile = new File(directory);
        if(!(rootDirFile.exists() && rootDirFile.isDirectory())){
            throw new IllegalArgumentException("Directory not found");
        }
    }

    public List<Mp3File> getMp3Files() {
        return mp3Files;
    }

    public void setMp3Files(List<Mp3File> mp3Files) {
        this.mp3Files = mp3Files;
    }

    private void findMP3Files(File dirFile) throws InvalidDataException, IOException, UnsupportedTagException {
        File[] files = dirFile.listFiles(filter);
        if(files != null){
            for(File file : files){
                if(file.isFile()){
                    mp3Files.add(new Mp3File(file));
                }
                if(file.isDirectory()){
                    findMP3Files(file);
                }
            }
        }

    }
    public static Object getTag(Mp3File mp3File){
        if(mp3File.hasId3v1Tag()){
            return mp3File.getId3v1Tag();
        }
        if(mp3File.hasId3v2Tag()){
            return mp3File.getId3v2Tag();
        }
        return null;
    }



}
