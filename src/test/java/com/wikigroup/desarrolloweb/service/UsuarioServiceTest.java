package com.wikigroup.desarrolloweb.service;

import com.wikigroup.desarrolloweb.dtos.UsuarioDto;
import com.wikigroup.desarrolloweb.model.Empresa;
import com.wikigroup.desarrolloweb.model.Usuario;
import com.wikigroup.desarrolloweb.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @Mock private EmpresaService empresaService;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Usuario usuario2;
    private UsuarioDto usuarioDto;
    private UsuarioDto usuarioDto2;
    private Empresa empresa;

    @BeforeEach
    void setUp() {
        empresa = new Empresa();
        empresa.setId(1L);
        empresa.setNombre("Test Company");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPassword("password123");
        usuario.setEmpresa(empresa);

        usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Test User 2");
        usuario2.setEmail("test2@example.com");

        usuarioDto = new UsuarioDto();
        usuarioDto.setId(1L);
        usuarioDto.setNombre("Test User");
        usuarioDto.setEmail("test@example.com");
        usuarioDto.setEmpresaId(1L);

        usuarioDto2 = new UsuarioDto();
        usuarioDto2.setId(2L);
        usuarioDto2.setNombre("Test User 2");
        usuarioDto2.setEmail("test2@example.com");
        usuarioDto2.setEmpresaId(null);
    }

    @Test
    void getAll_ShouldReturnAllUsuariosAsDtos() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario, usuario2));
        when(modelMapper.map(usuario, UsuarioDto.class)).thenReturn(usuarioDto);
        when(modelMapper.map(usuario2, UsuarioDto.class)).thenReturn(usuarioDto2);

        List<UsuarioDto> result = usuarioService.getAll();

        assertEquals(2, result.size());
        assertEquals("Test User", result.get(0).getNombre());
        assertEquals("Test User 2", result.get(1).getNombre());

        verify(usuarioRepository).findAll();
        verify(modelMapper).map(usuario, UsuarioDto.class);
        verify(modelMapper).map(usuario2, UsuarioDto.class);
        verifyNoMoreInteractions(usuarioRepository, modelMapper, empresaService);
    }

    @Test
    void getById_WhenExists_ShouldReturnDto() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(usuario, UsuarioDto.class)).thenReturn(usuarioDto);

        UsuarioDto result = usuarioService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getNombre());

        verify(usuarioRepository).findById(1L);
        verify(modelMapper).map(usuario, UsuarioDto.class);
        verifyNoMoreInteractions(usuarioRepository, modelMapper);
        verifyNoInteractions(empresaService);
    }

    @Test
    void getById_WhenNotExists_ShouldThrowNotFound() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> usuarioService.getById(1L));

        assertTrue(ex.getMessage().contains("Usuario not found"));
        verify(usuarioRepository).findById(1L);
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoInteractions(modelMapper, empresaService);
    }

    @Test
    void findEntityById_WhenExists_ShouldReturnEntity() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findEntityById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(usuarioRepository).findById(1L);
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoInteractions(modelMapper, empresaService);
    }

    @Test
    void findEntityById_WhenNotExists_ShouldThrowNotFound() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> usuarioService.findEntityById(1L));

        assertTrue(ex.getMessage().contains("Usuario not found"));
        verify(usuarioRepository).findById(1L);
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoInteractions(modelMapper, empresaService);
    }

    @Test
    void create_ShouldMapSetEmpresaSaveAndReturnDto() {
        when(modelMapper.map(any(UsuarioDto.class), eq(Usuario.class))).thenReturn(usuario);
        when(empresaService.findEntityById(1L)).thenReturn(empresa);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioDto.class)).thenReturn(usuarioDto);

        UsuarioDto result = usuarioService.create(usuarioDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getNombre());
        assertEquals(1L, result.getEmpresaId());

        verify(modelMapper).map(any(UsuarioDto.class), eq(Usuario.class));
        verify(empresaService).findEntityById(1L);
        verify(usuarioRepository).save(usuario);
        verify(modelMapper).map(usuario, UsuarioDto.class);
        verifyNoMoreInteractions(usuarioRepository, modelMapper, empresaService);
    }

    @Test
    void update_WhenExists_ShouldMapSetEmpresaSaveAndReturnDto() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(modelMapper.map(any(UsuarioDto.class), eq(Usuario.class))).thenReturn(usuario);
        when(empresaService.findEntityById(1L)).thenReturn(empresa);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(modelMapper.map(usuario, UsuarioDto.class)).thenReturn(usuarioDto);

        UsuarioDto result = usuarioService.update(1L, usuarioDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getNombre());

        verify(usuarioRepository).findById(1L);
        verify(modelMapper).map(any(UsuarioDto.class), eq(Usuario.class));
        verify(empresaService).findEntityById(1L);
        verify(usuarioRepository).save(usuario);
        verify(modelMapper).map(usuario, UsuarioDto.class);
        verifyNoMoreInteractions(usuarioRepository, modelMapper, empresaService);
    }

    @Test
    void update_WhenNotExists_ShouldThrowNotFound() {
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> usuarioService.update(1L, usuarioDto));

        assertTrue(ex.getMessage().contains("Usuario not found"));
        verify(usuarioRepository).findById(1L);
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoInteractions(modelMapper, empresaService);
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.delete(1L);

        verify(usuarioRepository).existsById(1L);
        verify(usuarioRepository).deleteById(1L);
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoInteractions(modelMapper, empresaService);
    }

    @Test
    void delete_WhenNotExists_ShouldThrowNotFound() {
        when(usuarioRepository.existsById(anyLong())).thenReturn(false);

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> usuarioService.delete(1L));

        assertTrue(ex.getMessage().contains("Usuario not found"));
        verify(usuarioRepository).existsById(1L);
        verify(usuarioRepository, never()).deleteById(anyLong());
        verifyNoMoreInteractions(usuarioRepository);
        verifyNoInteractions(modelMapper, empresaService);
    }
}
