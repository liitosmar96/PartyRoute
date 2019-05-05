package com.example.partyroute;

import com.example.partyroute.model.Usuario;
import com.example.partyroute.service.UsuarioService;
import com.example.partyroute.service.UsuarioServiceImpl;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void pruebaUsuario() {
        Usuario usr = new Usuario("1", "root", "root@root.com");

        UsuarioService usuarioService = new UsuarioServiceImpl();

        assertEquals(usuarioService.buscarPorCorreo("root@root.com"), usr);
    }

}