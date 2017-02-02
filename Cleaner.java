import java.util.ArrayList;
import java.io.*;

/**
 * Write a description of class Cleaner here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Cleaner implements Serializable

{
    File downloadFolder;
    File[] targetList;

    public Cleaner() {
        downloadFolder = null;
        targetList = new File[4];
    }

    public void setDownloadFolder(File f) {
        downloadFolder = f;
    }

    public File getDownloadFolder() {
        return downloadFolder;
    }

    public void addFileToTargetList(File f, int x) {
        targetList[x] = f;
    }

    public File[] getTargetList() {
        return targetList;
    }

    public boolean targetListComplete() {
        for (int i = 0; i < targetList.length; i++)
            if (targetList[i] == null)
                return false;

        return true;
    }

    public void clean() {
        ArrayList<File> files = listFiles(downloadFolder);

        for (File fileEntry : files) {
            fileEntry.renameTo(targetList[0]);
        }
    }

    public ArrayList<File> listFiles(File f) {
        FileDeterminer fd = new FileDeterminer();

        ArrayList<File> files = new ArrayList<File>();
        for (File fileEntry : f.listFiles()) {
            files.add(fileEntry);
        }

        for (File fileEntry : f.listFiles()) {
            //rename files to all sub categories

            String extension = null;
            int temp = fileEntry.getName().lastIndexOf('.');
            if (temp > 0)//check if the file has an identity
                extension = fileEntry.getName().substring(temp + 1);

            if (extension == null)
                moveFile(fileEntry, targetList[3]);
            else {
                if (fd.imageFile(extension)) {
                    moveFile(fileEntry, targetList[0]);
                } else if (fd.textFile(extension)) {
                    moveFile(fileEntry, targetList[1]);
                } else if (fd.audioFile(extension)) {
                    moveFile(fileEntry, targetList[2]);
                } else {
                    moveFile(fileEntry, targetList[3]);
                }
            }
        }
        return files;
    }

    public boolean moveFile(File f, File target) {
        if (f.renameTo(new File(target.getPath() + "//" + f.getName())))
            return true;

        return false;
    }


    private class FileDeterminer {
        private String[] pictureExtensions = {"PNG", "jpg", "jif", "jfif", "gif", "tif", "tiff", "jp2", "jpx", "j2k", "j2c", "fpx",
                "pcd", "png"};

        private String[] documentExtensions = {"pdf", "pages", "ppt", "pptx", "fbl", "doc", "txt", "mbox", "diz", "docx", "vnt", "log", "abw", "text", "pages", "rtf", "tex", "wpd", "wps"};

        private String[] musicExtensions = {"aif", "iff", "m3u", "m4a", "mid", "mp3", "mpa", "ra", "wav", "wma"};

        public boolean imageFile(String s) {
            for (String itr : pictureExtensions) {
                if (itr.equals(s)) {
                    return true;
                }
            }
            return false;
        }

        public boolean textFile(String s) {
            for (String itr : documentExtensions) {
                if (itr.equals(s)) {
                    return true;
                }
            }
            return false;
        }

        public boolean audioFile(String s) {
            for (String itr : musicExtensions) {
                if (itr.equals(s)) {
                    return true;
                }
            }
            return false;
        }

    }
}

