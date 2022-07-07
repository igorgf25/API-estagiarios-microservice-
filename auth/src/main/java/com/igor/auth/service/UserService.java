package com.igor.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.igor.auth.exception.InvalidDataException;
import com.igor.auth.mapper.UsuarioMapper;
import com.igor.auth.model.Usuario;
import com.igor.auth.model.dto.oauth.AutenticacaoDTO;
import com.igor.auth.model.dto.oauth.UsuarioRS;
import com.igor.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UsuarioRepository usuarioRepository;

    public BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String signin(AutenticacaoDTO autenticacaoDTO) {
        Optional<Usuario> usuario = usuarioRepository.findByUsuario(autenticacaoDTO.getUsuario());

        if (usuario.isEmpty()){
            throw new InvalidDataException("Credenciais invalidas: Usuario não encontrado");
        }

        if (!encoder.matches(autenticacaoDTO.getSenha(), usuario.get().getSenha())) {
            throw new InvalidDataException("Credenciais invalidas: Senha invalida");
        }

        return createToken(usuario.get());
    }

    private String createToken(Usuario usuario) {
        System.out.println("Chegou na criação de token");
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong("6000000"));

        return JWT.create().withSubject(usuario.getUsuario())
                .withExpiresAt(dataExpiracao)
                .sign(Algorithm.HMAC512("bcc50eb0-b709-4fa5-bb7e-2ed5d7435922"));
    }

    public UsuarioRS validateToken(String token) {
        String subject = JWT.require(Algorithm.HMAC512("bcc50eb0-b709-4fa5-bb7e-2ed5d7435922"))
                .build()
                .verify(token)
                .getSubject();

        Optional<Usuario> usuario = usuarioRepository.findByUsuario(subject);

        if (usuario.isEmpty()) {
            throw new RuntimeException("Token invalido");
        }

        return UsuarioMapper.usuarioToUsuarioRS(usuario.get());
    }

}
