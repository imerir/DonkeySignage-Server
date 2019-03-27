package Donkey.Api;

import Donkey.Storage.Service.StorageService;
import Donkey.WebSite.FileUploadController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/media")
public class MediaApiController {

    private final
    StorageService storageService;

    @Autowired
    public MediaApiController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Get all file in storage server
     * @return all files from server in a list
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMedial(){

        List<String> files = storageService.loadAll().map(
                path -> path.getFileName().toString())
                .collect(Collectors.toList());
        return new ResponseEntity<>(files, HttpStatus.OK);

    }
}
