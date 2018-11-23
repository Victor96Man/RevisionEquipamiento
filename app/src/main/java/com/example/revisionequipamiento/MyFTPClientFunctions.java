package com.example.revisionequipamiento;

import android.content.Context;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MyFTPClientFunctions {

    // Now, declare a public FTP client object.

    private static final String TAG = "MyFTPClientFunctions";
    private FTPClient mFTPClient = null;

    // Method to connect to FTP server:
    public boolean ftpConnect(String host, String username, String password,
                              int port, Context context) {
        try {
            mFTPClient = new FTPClient();
            // connecting to the host
            mFTPClient.connect(host, port);

            // now check the reply code, if positive mean connection success
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                // login using username & password
                boolean status = mFTPClient.login(username, password);
                //Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT).show();
                /*
                 * Set File Transfer Mode
                 *
                 * To avoid corruption issue you must specified a correct
                 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
                 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
                 * transferring text, image, and compressed files.
                 */
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();

                return status;
            }
        } catch (Exception e) {

            Toast.makeText(context, "Error: no se pudo conectar con el servidor " + host+" ("+e+")", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    // Method to disconnect from FTP server:
/*
    public boolean ftpDisconnect() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
        }

        return false;
    }
    */
/*
    // Method to get current working directory:

    public String ftpGetCurrentWorkingDirectory() {
        try {
            String workingDir = mFTPClient.printWorkingDirectory();
            return workingDir;
        } catch (Exception e) {

        }

        return null;
    }
    */
    // Method to change working directory:
/*
    public boolean ftpChangeDirectory(String directory_path) {
        try {
            mFTPClient.changeWorkingDirectory(directory_path);
        } catch (Exception e) {

        }

        return false;
    }
*/
    // Method to list all files in a directory:
/*
    public String[] ftpPrintFilesList(String dir_path) {
        String[] fileList = null;
        try {
            FTPFile[] ftpFiles = mFTPClient.listFiles(dir_path);
            int length = ftpFiles.length;
            fileList = new String[length];
            for (int i = 0; i < length; i++) {
                String name = ftpFiles[i].getName();
                boolean isFile = ftpFiles[i].isFile();

                if (isFile) {
                    fileList[i] = "File :: " + name;

                } else {
                    fileList[i] = "Directory :: " + name;

                }
            }
            return fileList;
        } catch (Exception e) {
            return fileList;
        }
    }
*/
    // Method to create new directory:
/*
    public boolean ftpMakeDirectory(String new_dir_path) {
        try {
            boolean status = mFTPClient.makeDirectory(new_dir_path);
            return status;
        } catch (Exception e) {

        }

        return false;
    }
*/
    // Method to delete/remove a directory:
/*
    public boolean ftpRemoveDirectory(String dir_path) {
        try {
            boolean status = mFTPClient.removeDirectory(dir_path);
            return status;
        } catch (Exception e) {

        }

        return false;
    }
*/
    // Method to delete a file:
/*
    public boolean ftpRemoveFile(String filePath) {
        try {
            boolean status = mFTPClient.deleteFile(filePath);
            return status;
        } catch (Exception e) {
        }

        return false;
    }
*/
    // Method to rename a file:
/*
    public boolean ftpRenameFile(String from, String to) {
        try {
            boolean status = mFTPClient.rename(from, to);
            return status;
        } catch (Exception e) {

        }

        return false;
    }
    */

    // Method to download a file from FTP server:

    /**
     * mFTPClient: FTP client connection object (see FTP connection example)
     * srcFilePath: path to the source file in FTP server desFilePath: path to
     * the destination file to be saved in sdcard
     */

    public boolean ftpDownload(String srcFilePath, String desFilePath, Context context) {
        boolean status = false;
        try {
            FileOutputStream desFileStream = new FileOutputStream(desFilePath);
            status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
            desFileStream.close();
            Toast.makeText(context, "Descarga realizada", Toast.LENGTH_LONG).show();
            return status;
        } catch (Exception e) {
            Toast.makeText(context, "Error: no se descargar ("+e+")", Toast.LENGTH_LONG).show();
        }

        return status;
    }

    // Method to upload a file to FTP server:

    /**
     * mFTPClient: FTP client connection object (see FTP connection example)
     * srcFilePath: source file path in sdcard desFileName: file name to be
     * stored in FTP server desDirectory: directory path where the file should
     * be upload to
     */
    public boolean ftpUpload(String srcFilePath, String desFileName,
                             String desDirectory, Context context) {
        boolean status = false;
        try {
            FileInputStream srcFileStream = new FileInputStream(srcFilePath);

            // change working directory to the destination directory
            // if (ftpChangeDirectory(desDirectory)) {
            status = mFTPClient.storeFile(desFileName, srcFileStream);
            // }

            srcFileStream.close();

            return status;
        } catch (Exception e) {
        }

        return status;
    }
}
