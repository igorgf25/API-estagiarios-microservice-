package com.igor.auth.controller;

import com.igor.auth.model.dto.oauth.AutenticacaoDTO;
import com.igor.auth.model.dto.oauth.UsuarioRS;
import com.igor.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping("/Login")
    public ResponseEntity<String> signin(@RequestBody AutenticacaoDTO autenticacaoDTO) {
        try {
            return ResponseEntity.ok(userService.signin(autenticacaoDTO));
        } catch (Exception e) {
            return new ResponseEntity("Erro ao validar credenciais: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/validateToken")
    public ResponseEntity<UsuarioRS> validateToken(@RequestParam String token) {
        try {
            return ResponseEntity.ok(userService.validateToken(token));
        } catch (Exception e) {
            return new ResponseEntity("Erro ao validar token", HttpStatus.BAD_REQUEST);
        }
    }
}
