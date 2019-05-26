package com.example.partyroute.dao;

import android.content.Context;

import com.example.partyroute.model.Evento;

import java.util.List;

public interface EventoDao {
    public List<Evento> findAll();

    public Evento findByID(int id);

    public Evento findByID(Evento usr);

    public List<Evento> findByCif(Context context, String cif);

    public Evento findByEmail(Evento usr);
}
