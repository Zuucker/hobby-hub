/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sqlFileParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Zuucker
 */
public class SqlFileParser {

    private String file = "";

    public SqlFileParser(String filepath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                file += line;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> getStatements() {
        List<String> statements = new ArrayList();

        System.out.println(this.file);

        while (this.file.contains(";")) {
            int index = this.file.indexOf(";");
            String statement = this.file.substring(0, index + 1);
            this.file = this.file.replace(statement, "");
            System.out.println(statement);
            statements.add(statement);
        }

        return statements;
    }
}
