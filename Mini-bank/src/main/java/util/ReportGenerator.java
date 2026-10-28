package util;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
public final class ReportGenerator {
    private ReportGenerator() {}
    public static String generate(Path logDir, Path reportFile) throws IOException {
        StringBuilder details=new StringBuilder();
        details.append("END-OF-DAY REPORT\n");
        details.append("=================\n");
        if(Files.exists(logDir)){
            Stream<Path> stream = Files.list(logDir);
            try{
                stream.filter(p->p.toString().endsWith(".log")).sorted().forEach(p->{
                    try{
                        details.append("\n-- ").append(p.getFileName()).append(" --\n");
                        details.append(Files.readAllLines(p,StandardCharsets.UTF_8).stream().reduce((a,b)->a+"\n"+b).orElse(""));
                        details.append("\n");
                    }catch(IOException ignored){}
                });
            }finally{ stream.close(); }
        }else{
            details.append("No transaction logs found.\n");
        }
        Path parent=reportFile.toAbsolutePath().getParent();
        if(parent!=null) Files.createDirectories(parent);
        try(BufferedWriter bw=Files.newBufferedWriter(reportFile,StandardCharsets.UTF_8)){ bw.write(details.toString()); }
        return details.toString();
    }
}