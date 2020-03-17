package com.once.upload.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.Size;

/**
 * Imagen
 */
@Entity
public class Imagen {


    @Id
    @GeneratedValue
    private Long id;


    @Size(max = 500)
    private String comentarios;


    /**
     * La imagen es un archivo binario.
     * Se almacena como lob que quiere decir 
     * LARGE OBJECT (BINARY) - CLOB BLOB
     */
    @Lob
    private byte[] imagen;


    /**
     * Para guardarme el content type
     */
    @Size(max = 100)
    private String contentType;

    /**
     * Para guardarme el nombre del archivo
     * 
     */
    @Size(max = 300)
    private String nombreArchivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    

    
    
}