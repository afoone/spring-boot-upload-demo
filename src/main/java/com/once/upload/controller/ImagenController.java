package com.once.upload.controller;

import java.io.IOException;
import java.util.List;

import javax.print.attribute.standard.Media;

import com.once.upload.model.Imagen;
import com.once.upload.repository.ImagenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * ImagenController
 */
@Controller
public class ImagenController {

    @Value("${upload.max.size}")
    Long maxFileSize;

    @Autowired
    ImagenRepository ir;

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView formulario = new ModelAndView("upload-form");
        return formulario;
    }

    @PostMapping("/upload")
    public ModelAndView fileUpload(@RequestParam("comentario") String comentario,
            @RequestParam("file") MultipartFile file) {

        ModelAndView modelAndView = new ModelAndView("upload-complete");

        if (file.isEmpty()) {
            modelAndView.addObject("mensaje", "El fichero está vacío");
            return modelAndView;
        }

        if (file.getSize() > maxFileSize) {
            modelAndView.addObject("mensaje", "El fichero es demasiado grande");
            return modelAndView;
        }

        if (!file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)
                && !file.getContentType().equals(MediaType.IMAGE_GIF_VALUE)
                && !file.getContentType().equals(MediaType.IMAGE_PNG_VALUE)) {
            modelAndView.addObject("mensaje", "solo admitimos jpg, gif y png");
            return modelAndView;
        }

        Imagen imagen = new Imagen();
        imagen.setComentarios(comentario);
        imagen.setNombreArchivo(file.getOriginalFilename());
        imagen.setContentType(file.getContentType());

        try {
            imagen.setImagen(file.getBytes());
            ir.save(imagen);
        } catch (IOException e) {
            e.printStackTrace();
            modelAndView.addObject("mensaje", "Algo ha ido mal");
            return modelAndView;
        }

        modelAndView.addObject("mensaje", "Subido correctamente el archivo " + file.getOriginalFilename());

        return modelAndView;

    }

    @GetMapping("/imagen/{id}/")
    public ResponseEntity<byte[]> getImagen(@PathVariable Long id) {
        Imagen imagen = ir.findById(id).get();
        HttpHeaders headers = new HttpHeaders();

        MediaType mediaType;

        if (imagen.getContentType() != null) {
            switch (imagen.getContentType()) {
                case MediaType.IMAGE_JPEG_VALUE:
                    mediaType = MediaType.IMAGE_JPEG;
                    break;
                case MediaType.IMAGE_PNG_VALUE:
                    mediaType = MediaType.IMAGE_PNG;
                    break;
                case MediaType.IMAGE_GIF_VALUE:
                    mediaType = MediaType.IMAGE_GIF;
                    break;
                default:
                    mediaType = MediaType.IMAGE_JPEG;
                    break;
            }
        } else {
            mediaType = MediaType.IMAGE_JPEG;
        }

        headers.setContentType(mediaType);

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(imagen.getImagen(), headers, HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping("/upload/{name}")
    public ResponseEntity<byte[]> getImagenByName(@PathVariable("name") String name) {

        List<Imagen> listaImagenes = ir.findByNombreArchivo(name);
        if (listaImagenes.size() == 0) {
            ResponseEntity<byte[]> mirespuestavacia = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return mirespuestavacia;
        }
        Imagen imagen = listaImagenes.get(0);
        HttpHeaders headers = new HttpHeaders();

        MediaType mediaType;

        switch (imagen.getContentType()) {
            case MediaType.IMAGE_JPEG_VALUE:
                mediaType = MediaType.IMAGE_JPEG;
                break;
            case MediaType.IMAGE_PNG_VALUE:
                mediaType = MediaType.IMAGE_PNG;
                break;
            case MediaType.IMAGE_GIF_VALUE:
                mediaType = MediaType.IMAGE_GIF;
                break;
            default:
                mediaType = MediaType.IMAGE_JPEG;
                break;
        }

        headers.setContentType(mediaType);

        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(imagen.getImagen(), headers, HttpStatus.OK);
        return responseEntity;
    }

}