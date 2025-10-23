package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.dtos.EmpresaDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.repository.EmpresaRepository;
import com.wikigroup.desarrolloweb.shared.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpresaServiceTest {

    @Mock private EmpresaRepository empresaRepository;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private EmpresaService empresaService;

    private Empresa empresa;
    private Empresa empresa2;
    private EmpresaDto empresaDto;
    private EmpresaDto empresaDto2;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("Test Company");
        empresa.setDescripcion("Test Description");

        empresa2 = new Empresa();
        empresa2.setId(2L);
        empresa2.setNombre("Test Company 2");
        empresa2.setDescripcion("Test Description 2");

        empresaDto = new EmpresaDto();
        empresaDto.setId(1L);
        empresaDto.setNombre("Test Company");
        empresaDto.setDescripcion("Test Description");

        empresaDto2 = new EmpresaDto();
        empresaDto2.setId(2L);
        empresaDto2.setNombre("Test Company 2");
        empresaDto2.setDescripcion("Test Description 2");
    }

    @Test
    void getAll_ShouldReturnAllEmpresasAsDtos() {
        when(empresaRepository.findAll()).thenReturn(List.of(empresa, empresa2));
        when(modelMapper.map(empresa, EmpresaDto.class)).thenReturn(empresaDto);
        when(modelMapper.map(empresa2, EmpresaDto.class)).thenReturn(empresaDto2);

        List<EmpresaDto> result = empresaService.getAll();

        assertEquals(2, result.size());
        assertEquals("Test Company", result.get(0).getNombre());
        assertEquals("Test Company 2", result.get(1).getNombre());

        verify(empresaRepository).findAll();
        verify(modelMapper).map(empresa, EmpresaDto.class);
        verify(modelMapper).map(empresa2, EmpresaDto.class);
        verifyNoMoreInteractions(empresaRepository, modelMapper);
    }

    @Test
    void getById_WhenExists_ShouldReturnDto() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(modelMapper.map(empresa, EmpresaDto.class)).thenReturn(empresaDto);

        EmpresaDto result = empresaService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Company", result.getNombre());

        verify(empresaRepository).findById(1L);
        verify(modelMapper).map(empresa, EmpresaDto.class);
        verifyNoMoreInteractions(empresaRepository, modelMapper);
    }

    @Test
    void getById_WhenNotExists_ShouldThrowNotFound() {
        when(empresaRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> empresaService.getById(1L));

        assertTrue(ex.getMessage().contains("Empresa not found"));
        verify(empresaRepository).findById(1L);
        verifyNoMoreInteractions(empresaRepository);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void findEntityById_WhenExists_ShouldReturnEntity() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));

        Empresa result = empresaService.findEntityById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(empresaRepository).findById(1L);
        verifyNoMoreInteractions(empresaRepository);
    }

    @Test
    void findEntityById_WhenNotExists_ShouldThrowNotFound() {
        when(empresaRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> empresaService.findEntityById(1L));

        assertTrue(ex.getMessage().contains("Empresa not found"));
        verify(empresaRepository).findById(1L);
        verifyNoMoreInteractions(empresaRepository);
    }

    @Test
    void create_ShouldMapSaveAndReturnDto() {
        when(modelMapper.map(any(EmpresaDto.class), eq(Empresa.class))).thenReturn(empresa);
        when(empresaRepository.save(empresa)).thenReturn(empresa);
        when(modelMapper.map(empresa, EmpresaDto.class)).thenReturn(empresaDto);

        EmpresaDto result = empresaService.create(empresaDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Company", result.getNombre());

        verify(modelMapper).map(any(EmpresaDto.class), eq(Empresa.class));
        verify(empresaRepository).save(empresa);
        verify(modelMapper).map(empresa, EmpresaDto.class);
        verifyNoMoreInteractions(empresaRepository, modelMapper);
    }

    @Test
    void update_WhenExists_ShouldMapSaveAndReturnDto() {
        when(empresaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(modelMapper.map(any(EmpresaDto.class), eq(Empresa.class))).thenReturn(empresa);
        when(empresaRepository.save(empresa)).thenReturn(empresa);
        when(modelMapper.map(empresa, EmpresaDto.class)).thenReturn(empresaDto);

        EmpresaDto result = empresaService.update(1L, empresaDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Company", result.getNombre());

        verify(empresaRepository).findById(1L);
        verify(modelMapper).map(any(EmpresaDto.class), eq(Empresa.class));
        verify(empresaRepository).save(empresa);
        verify(modelMapper).map(empresa, EmpresaDto.class);
        verifyNoMoreInteractions(empresaRepository, modelMapper);
    }

    @Test
    void update_WhenNotExists_ShouldThrowNotFound() {
        when(empresaRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> empresaService.update(1L, empresaDto));

        assertTrue(ex.getMessage().contains("Empresa not found"));
        verify(empresaRepository).findById(1L);
        verifyNoMoreInteractions(empresaRepository);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        when(empresaRepository.existsById(1L)).thenReturn(true);

        empresaService.delete(1L);

        verify(empresaRepository).existsById(1L);
        verify(empresaRepository).deleteById(1L);
        verifyNoMoreInteractions(empresaRepository);
    }

    @Test
    void delete_WhenNotExists_ShouldThrowNotFound() {
        when(empresaRepository.existsById(anyLong())).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> empresaService.delete(1L));

        assertTrue(ex.getMessage().contains("Empresa not found"));
        verify(empresaRepository).existsById(1L);
        verify(empresaRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(empresaRepository);
        verifyNoInteractions(modelMapper);
    }
}
