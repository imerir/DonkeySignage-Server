package Donkey.WebSite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import Donkey.Api.JSON.Template.DeleteFileJson;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Storage.StorageProperties;
import Donkey.Tools.Exception.StorageFileNotFoundException;
import Donkey.Storage.Service.StorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final StorageProperties storageProperties;
    private Logger log = LogManager.getLogger();

    @Autowired
    public FileUploadController(StorageService storageService, StorageProperties storageProperties) {
        this.storageService = storageService;
        this.storageProperties = storageProperties;
    }

    /**
     * show all media upload in server, and you can upload a media here
     *
     * @param model
     * @return
     * @throws IOException
     */
    @GetMapping("/addMedia")
    public String listUploadedFiles(Model model, Authentication authentication) throws IOException {
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        model.addAttribute("user",userEntity);
        return "Media/uploadForm";
    }

    /**
     * Add Media in server's storage
     *
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/addMedia")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/media";
    }

    /**
     * Download the file
     *
     * @param filename
     * @return
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @RequestMapping(value = "/deleteFile", method = RequestMethod.DELETE)
    public ResponseEntity<?> eraseAFile(@RequestBody DeleteFileJson deleteFileJson) throws IOException {
        File fileToDelete = new File (storageProperties.getLocation()+"/"+storageService.load(deleteFileJson.filename).getFileName());
        if(fileToDelete.exists()){
            if(fileToDelete.delete())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}