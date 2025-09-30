package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private EmpresaService empresaService;

    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("Test Company");
        empresa.setDescripcion("Test Description");
    }

    @Test
    void findAll_ShouldReturnAllEmpresas() {
        // Given
        Empresa empresa2 = new Empresa();
        empresa2.setId(2L);
        empresa2.setNombre("Test Company 2");
        empresa2.setDescripcion("Test Description 2");
        
        List<Empresa> empresas = Arrays.asList(empresa, empresa2);
        when(empresaRepository.findAll()).thenReturn(empresas);

        // When
        List<Empresa> result = empresaService.findAll();

        // Then
        assertEquals(2, result.size());
        assertEquals("Test Company", result.get(0).getNombre());
        assertEquals("Test Company 2", result.get(1).getNombre());
        verify(empresaRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenEmpresaExists_ShouldReturnEmpresa() {
        // Given
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));

        // When
        Empresa result = empresaService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Company", result.getNombre());
        assertEquals("Test Description", result.getDescripcion());
        verify(empresaRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenEmpresaDoesNotExist_ShouldThrowException() {
        // Given
        when(empresaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> empresaService.findById(1L));
        
        assertEquals("Empresa not found with id 1", exception.getMessage());
        verify(empresaRepository, times(1)).findById(1L);
    }

    @Test
    void save_ShouldReturnSavedEmpresa() {
        // Given
        when(empresaRepository.save(any(Empresa.class))).thenReturn(empresa);

        // When
        Empresa result = empresaService.save(empresa);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Company", result.getNombre());
        verify(empresaRepository, times(1)).save(empresa);
    }

    @Test
    void delete_WhenEmpresaExists_ShouldDeleteEmpresa() {
        // Given
        when(empresaRepository.existsById(1L)).thenReturn(true);

        // When
        empresaService.delete(1L);

        // Then
        verify(empresaRepository, times(1)).existsById(1L);
        verify(empresaRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_WhenEmpresaDoesNotExist_ShouldThrowException() {
        // Given
        when(empresaRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> empresaService.delete(1L));
        
        assertEquals("Empresa not found with id 1", exception.getMessage());
        verify(empresaRepository, times(1)).existsById(1L);
        verify(empresaRepository, never()).deleteById(anyLong());
    }
}