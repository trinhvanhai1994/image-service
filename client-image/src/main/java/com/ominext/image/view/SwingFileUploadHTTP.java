package com.ominext.image.view;

import com.ominext.image.entity.Image;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A Swing application that uploads files to a HTTP server.
 *
 * @author www.codejava.net
 */
public class SwingFileUploadHTTP extends JFrame implements PropertyChangeListener {
    private JLabel labelURL = new JLabel("Upload URL: ");
    private JTextField fieldURL = new JTextField(30);
    private JButton buttonUpload = new JButton("Upload");
    private JButton buttonDownload = new JButton("Download");
    private JLabel labelProgress = new JLabel("Progress:");
    private JProgressBar progressBar = new JProgressBar(0, 100);
    private JFilePicker filePicker = new JFilePicker("Choose a file: ", "Browse");

    private final static String ROOT_URL = "http://localhost:8888/images";

    public SwingFileUploadHTTP() {
        super("Swing File Upload to HTTP server");

        // set up layout
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        // set up components
        filePicker.setMode(JFilePicker.MODE_OPEN);

        buttonUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonUploadActionPerformed(event);
            }
        });

        buttonDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonUploadActionPerformed(event);
            }
        });

        progressBar.setPreferredSize(new Dimension(500, 50));
        progressBar.setStringPainted(true);

        // add components to the frame
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(labelURL, constraints);

        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        add(fieldURL, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        add(filePicker, constraints);

        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LAST_LINE_START;
        add(buttonUpload, constraints);

        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.LAST_LINE_END;
        add(buttonDownload, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;
        add(labelProgress, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(progressBar, constraints);

        pack();
        setLocationRelativeTo(null);    // center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * handle click event of the Upload button
     */
    private void buttonUploadActionPerformed(ActionEvent event) {
        String uploadURL = fieldURL.getText();
        String filePath = filePicker.getSelectedFilePath();

        if (!uploadURL.equals("")) {
            getFile(uploadURL);
            System.out.println(getFile(uploadURL));
            return;
        }
        if (filePath.equals("")) {
            JOptionPane.showMessageDialog(this, "Please choose a file to upload!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File uploadFile = new File(filePath);
            byte[] fileContent = Files.readAllBytes(uploadFile.toPath());
            postFile("imageFile", fileContent);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error executing upload task: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void postFile(String filename, byte[] someByteArray) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name(filename)
                .filename(filename)
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(someByteArray, fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(filename, fileEntity);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        try {
            restTemplate.exchange(ROOT_URL + "/uploadImage", HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    private Image getFile(String urlImage) {
        RestTemplate restTemplate = new RestTemplate();
        java.util.List<HttpMessageConverter<?>>  messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate.getForEntity(urlImage, Image.class).getBody();
    }

    /**
     * Update the progress bar's state whenever the progress of upload changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }

    /**
     * Launch the application
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SwingFileUploadHTTP().setVisible(true);
            }
        });
    }
}