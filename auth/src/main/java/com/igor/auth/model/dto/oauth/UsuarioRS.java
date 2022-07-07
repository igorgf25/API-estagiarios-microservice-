package com.igor.auth.model.dto.oauth;

import com.igor.auth.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioRS {
    private Long id;

    private String usuario;

    private String email;

    private List<Role> roles;

}
