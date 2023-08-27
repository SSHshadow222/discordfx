package com.example.discordfx.repository;

import com.example.discordfx.domain.Entity;
import com.example.discordfx.exception.FileException;
import com.example.discordfx.exception.ValidException;
import com.example.discordfx.validator.Validator;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public abstract class FileRepo<ID, E extends Entity<ID>> extends MemoryRepo<ID, E> {
    protected String filePath;

    public FileRepo(Validator<ID, E> valid, String file) {
        super(valid);
        this.filePath = file;
    }

    /**
     * Loads the entities from a given file and temporarily stores them in the equivalent repository(in memory)
     */
    protected void loadDataFromFile() throws FileException {
        long invalidDataCnt = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> elems = Arrays.asList(line.split(","));
                E entity = extractEntity(elems);
                if (entity != null) {
                    try {
                        super.save(entity);
                    } catch (ValidException ignored) {   // the row had invalid data
                        invalidDataCnt++;
                    }
                }
            }
        } catch (IOException e) {
            String err = "The file from the path " + '\'' + filePath + '\'' + " could not be written!\n";
            throw new FileException(err);
        } finally {
            System.out.println("There was a total of " + invalidDataCnt + " columns with invalid data in the file " + '\'' + filePath + '\'');
        }
    }

    /**
     * Stores the temporarily assigned entities from the repository in a given file(on disk)
     */
    protected void saveDataToFile() throws FileException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            int written = 0;
            for (E entity : this.getAll()) {
                String line = entityToFileFormat(entity);
                bw.write(line);
                written++;

                if (written < this.size()) {
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            String err = "The file path " + '\'' + filePath + '\'' + " does not exist\n";
            throw new FileException(err);
        }
    }

    /**
     * Creates an entity based on the given list of string values
     *
     * @param elems The entities' values represented in a list of strings
     * @return A new entity
     */
    protected abstract E extractEntity(List<String> elems);

    /**
     * Creates a string based on all the values of an entity and separates these values by a comma
     *
     * @param entity The entity from which the values are broken down into a string
     * @return A string of comma separated values
     */
    protected abstract String entityToFileFormat(E entity);
}
