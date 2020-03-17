package com.once.upload.repository;

import java.util.List;

import com.once.upload.model.Imagen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ImagenRepository
 */
@Repository
public interface ImagenRepository  extends JpaRepository<Imagen, Long>{
    
    public List<Imagen> findByNombreArchivo(String name);
    
}