package com.igor.auth.mapper;

import com.igor.auth.model.Usuario;
import com.igor.auth.model.dto.oauth.UsuarioRS;

public class UsuarioMapper {

    public static UsuarioRS usuarioToUsuarioRS(Usuario usuario) {
        return new UsuarioRS(usuario.getId(), usuario.getUsuario(),
                usuario.getEmail(), usuario.getRoles());
    }
}
