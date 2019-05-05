package com.example.partyroute.service;

import com.example.partyroute.dao.UsuarioDao;
import com.example.partyroute.dao.UsuarioDaoImpl;
import com.example.partyroute.model.Usuario;

import java.util.List;

public class UsuarioServiceImpl implements UsuarioService {

    UsuarioDao usuarioDao = new UsuarioDaoImpl();

    @Override
    public List<Usuario> buscarTodos() {
        return null;
    }

    @Override
    public Usuario buscarPorNIF(int id) {
        return null;
    }

    @Override
    public Usuario buscarPorNIF(Usuario usr) {
        return null;
    }

    @Override
    public Usuario buscarPorCorreo(String email) {
        return usuarioDao.findByEmail(email);
    }

    @Override
    public Usuario buscarPorCorreo(Usuario usr) {
        return usuarioDao.findByEmail(usr);
    }
}
