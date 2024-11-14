package com.viterbi.backend;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // ak je React app na inom origine
public class FormController {

    @PostMapping("/code")
    public String handleCode(@RequestBody Map<String, String> formData) {
        Map<String, String> responseData = new HashMap<>();
        String returnJson = "";

        System.out.println("CODE INPUT JSON: " + formData.toString()); // Input JSON string

        int[][] polynomial = ConvolutionalCodes.getImpRsp(formData.get("polynomial"));
        BinaryVect message = ConvolutionalCodes.getMsg(formData.get("message"));
        int[][] codedMessage = message.convCode(polynomial);
        int[][] channel;

        if ("".equals(formData.get("channel")) || "No Channel".equals(formData.get("channel"))) {
            channel = ConvolutionalCodes.getChannel(codedMessage);
        } else {
            channel = Conversion.stringTo2DIntArray(formData.get("channel"));
        }

        responseData.put("codedMessage", Conversion.format2DArrayToString(codedMessage));
        responseData.put("channel", Conversion.format2DArrayToString(channel));

        // Convert the Map to JSON
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            returnJson = objectMapper.writeValueAsString(responseData);
            System.out.println("CODE OUTPUT JSON: " + returnJson); // Output JSON string
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnJson;
    }

    @PostMapping("/decode")
    public String handleDecode(@RequestBody Map<String, String> formData) {
        Map<String, String> responseData = new HashMap<>();
        String returnJson = "";

        System.out.println("DECODE INPUT JSON: " + formData.toString()); // Input JSON string

        int[][] polynomial = ConvolutionalCodes.getImpRsp(formData.get("polynomial"));
        BinaryVect message = ConvolutionalCodes.getMsg(formData.get("message"));
        int[][] codedMessage = message.convCode(polynomial);
        int[][] channel;

        if ("".equals(formData.get("channel")) || "No Channel".equals(formData.get("channel"))) {
            channel = ConvolutionalCodes.getChannel(codedMessage);
        } else {
            channel = Conversion.stringTo2DIntArray(formData.get("channel"));
        }

        int[][] receivedMessage = ConvolutionalCodes.getReceivedMessage(codedMessage, channel);
        responseData.put("receivedMessage", Conversion.format2DArrayToString(receivedMessage));

        // Draw Graph and send Decoded Message
        VitDecoder decoder = new VitDecoder(channel, codedMessage);
        int[] decodedMessage;
        int[] currentBits = new int[decoder.n];
        for (int y = 0; y < decoder.nbOfStates; y++) {
            if (decoder.state - 1 > 0) {
                if (decoder.currentMetric[decoder.state - 1] == decoder.inf) {
                    decoder.nextStep(currentBits);

                }
            }
        }
        decodedMessage = decoder.decodedSeq();
        responseData.put("decodedMessage", Arrays.toString(decodedMessage));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            returnJson = objectMapper.writeValueAsString(responseData);
            System.out.println("DECODE OUTPUT JSON: " + returnJson); // Output JSON string
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnJson;
    }
}
