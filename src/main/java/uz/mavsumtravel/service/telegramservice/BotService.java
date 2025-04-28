package uz.mavsumtravel.service.telegramservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import uz.mavsumtravel.dto.TelegramResultDto;
import org.telegram.telegrambots.meta.api.objects.WebhookInfo;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class BotService {

    private static final String BASE_URL = "https://api.telegram.org/bot";
    private static final String FILE_URL = "https://api.telegram.org/file/bot";
    private static final RestTemplate restTemplate = new RestTemplate();

    private final String token;
    private final String url;

    public BotService(@Value("${telegram.token}") String token, @Value("${telegram.url}") String url) {
        this.token = token;
        this.url = url;
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> void send(Method method) {
        restTemplate.postForObject(BASE_URL + token + "/" + method.getMethod(), method, TelegramResultDto.class);
    }

    public void send(SendPhoto method) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("chat_id", method.getChatId());
            map.put("photo", method.getFile().getAttachName());
            if (method.getCaption() != null) {
                map.put("caption", method.getCaption());
            }
            if (method.getReplyMarkup() != null) {
                map.put("reply_markup", new Gson().toJson(method.getReplyMarkup()));
            }
            HttpEntity<?> requestEntity = new HttpEntity<>(map);
            ResponseEntity<String> response = restTemplate.exchange(
                    BASE_URL + token + "/" + method.getMethod(),
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            System.out.println("Sending SendPhoto with map: " + map);
            System.out.println("SendPhoto response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Error sending photo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendFile(Long chatId, String filePath) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + token + "/sendDocument";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("chat_id", chatId);
        body.add("document", new FileSystemResource(filePath));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        System.out.println("File uploaded successfully!");
    }

    public String getFile(String fileId) {
        String fileUrl = "";
        String directoryPath = System.getProperty("user.home") +  "/product_photo";
        Path directory = Path.of(directoryPath);
        String fileName;

        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            BotService.FileResponse response = restTemplate.getForObject(BASE_URL + token + "/getFile?file_id=" + fileId, BotService.FileResponse.class);

            if (response != null && response.isOk()) {
                String originalFilePath = response.getResult().getFilePath();
                String fileExtension = originalFilePath.substring(originalFilePath.lastIndexOf('.'));
                fileName = UUID.randomUUID() + fileExtension;

                fileUrl = FILE_URL + token + "/" + originalFilePath;
                byte[] fileBytes = restTemplate.getForObject(fileUrl, byte[].class);

                fileUrl = "";

                if (fileBytes != null) {
                    Files.write(directory.resolve(fileName), fileBytes, StandardOpenOption.CREATE);
                    System.out.println("File downloaded successfully: " + fileName);
                    fileUrl = url.substring(0, url.indexOf("/telegram"));
                    fileUrl = fileUrl + "/image/get/" + fileName;
                } else {
                    System.out.println("Failed to download the file.");
                    return fileUrl;
                }
            } else {
                System.out.println("Failed to get file path.");
                return fileUrl;
            }
        } catch (IOException ex) {
            System.err.println("Error occurred: " + ex.getMessage());
            return fileUrl;
        }

        return fileUrl;
    }

    public void editMessageMedia(EditMessageMedia method) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("chat_id", method.getChatId());
            map.put("message_id", method.getMessageId());
            map.put("media", new Gson().toJson(method.getMedia()));
            if (method.getReplyMarkup() != null) {
                map.put("reply_markup", new Gson().toJson(method.getReplyMarkup()));
            }
            HttpEntity<?> requestEntity = new HttpEntity<>(map);
            ResponseEntity<String> response = restTemplate.exchange(
                    BASE_URL + token + "/editMessageMedia",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            System.out.println("EditMessageMedia response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Error editing message media: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getWebhookUrl() {
        BotService.WebHookResult resultDto = restTemplate.getForObject(BASE_URL + token + "/getWebhookInfo", BotService.WebHookResult.class);
        if (resultDto != null) {
            if (resultDto.getResult().getUrl() != null) {
                return resultDto.getResult().getUrl();
            }
        }
        return "";
    }

    public void deleteMessage(Long chatId, Integer messageId) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("chat_id", chatId);
            map.put("message_id", messageId);
            HttpEntity<?> requestEntity = new HttpEntity<>(map);
            ResponseEntity<String> response = restTemplate.exchange(
                    BASE_URL + token + "/deleteMessage",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            System.out.println("DeleteMessage response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Error deleting message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Setter
    @Getter
    private static class FileResponse {
        private boolean ok;
        private File result;
    }

    @Getter
    @Setter
    @ToString
    private static class File {
        @JsonProperty("file_id")
        private String fileId;
        @JsonProperty("file_path")
        private String filePath;
    }

    @AllArgsConstructor
    @Getter
    private static class WebHookResult {
        private boolean ok;
        private WebhookInfo result;
    }
}
