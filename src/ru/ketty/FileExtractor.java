/**
 * Распаковщик архива 7z
 * @autor Karpova E.A.
 */
package ru.ketty;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileExtractor {

    private File archive; //архив

    private File tempFolder; //директория для распаковки архива

    private FileExtractor(String archivePath) {
        archive = new File(archivePath);
        String tmpPath = archive.isAbsolute() ? archive.getParent() : archive.getAbsoluteFile().getParent();
        tempFolder = new File(tmpPath + File.separator + "_tmp");
    }


    public File getTempFolder() {
        return tempFolder;
    }

    public static FileExtractor of(String archivePath) {
        return new FileExtractor(archivePath);
    }

    /**
     * Распаковка архива
     * @throws IOException
     */
    public void extract7z()throws IOException {
        SevenZFile sevenZFile = new SevenZFile(archive);

        SevenZArchiveEntry entry;
        while ((entry = sevenZFile.getNextEntry()) != null){
            if (entry.isDirectory()){
                continue;
            }
            File curfile = new File(tempFolder+File.separator+entry.getName());
            File parent = curfile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(curfile);
            byte[] content = new byte[(int) entry.getSize()];
            sevenZFile.read(content, 0, content.length);
            //System.out.println("Распаковано: "+curfile);
            out.write(content);
            out.close();
        }
    }
}
