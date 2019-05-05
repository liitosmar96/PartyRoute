package com.example.partyroute.service;

import com.example.partyroute.model.Usuario;

import java.util.List;

public interface UsuarioService {


    public List<Usuario> buscarTodos();

    public Usuario buscarPorNIF(int id);

    public Usuario buscarPorNIF(Usuario usr);

    public Usuario buscarPorCorreo(String email);

    public Usuario buscarPorCorreo(Usuario usr);
}
