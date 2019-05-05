package com.example.partyroute.dao;


import com.example.partyroute.model.Usuario;

import java.util.List;

public interface UsuarioDao {
    public List<Usuario> findAll();

    public Usuario findByID(int id);

    public Usuario findByID(Usuario usr);

    public Usuario findByEmail(String email);

    public Usuario findByEmail(Usuario usr);
}
