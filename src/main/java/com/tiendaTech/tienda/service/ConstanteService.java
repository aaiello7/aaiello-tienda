package com.tiendaTech.tienda.service;

import com.tiendaTech.tienda.domain.Constante;
import com.tiendaTech.tienda.repository.ConstanteRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConstanteService {

    private final ConstanteRepository constanteRepository;

    public ConstanteService(ConstanteRepository constanteRepository) {
        this.constanteRepository = constanteRepository;
    }

    @Transactional(readOnly = true)
    public List<Constante> getConstantes() {
        var lista = constanteRepository.findAll();
        return lista;
    }

    @Transactional(readOnly = true)
    public Constante getConstante(Integer idConstante) {
        return constanteRepository.findById(idConstante).orElseThrow(
            () -> new NoSuchElementException("Constante con ID " + idConstante + " no encontrada."));
    }

    @Transactional
    public void save(Constante constante) {
        constanteRepository.save(constante);
    }

    @Transactional
    public void delete(Integer idConstante) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!constanteRepository.existsById(idConstante)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException("La Constante con ID " + idConstante + " no existe.");
        }
        try {
            constanteRepository.deleteById(idConstante);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar la constante. Tiene datos asociados.", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Constante> findByAtributo(String atributo) {
        return constanteRepository.findByAtributo(atributo);
    }
}
