package tienda.services;

import tienda.domain.Usuario;
import tienda.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private HttpSession session;
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) 
            throws UsernameNotFoundException {
        //Se busca el username en la tabla usuario
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> 
                        new UsernameNotFoundException("Usuario no encontrado: " + username));
        
        //Si estamos acá todo ok, el username existe...
        session.removeAttribute("imagenUsuario");
        session.setAttribute("imagenUsuario", usuario.getRutaImagen());
        
         // Mapea los roles del usuario a GrantedAuthority de Spring Security
        var roles = usuario.getRoles().stream()
            .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getRol()))
            .collect(Collectors.toSet());

        System.err.println("El usuario es: "+ usuario);
        
        //Ahora se retorna un usuario con la información necesario
        return new User(usuario.getUsername(),usuario.getPassword(),roles);
    }   
}