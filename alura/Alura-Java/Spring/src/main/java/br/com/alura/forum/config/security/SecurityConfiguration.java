package br.com.alura.forum.config.security;

import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@Profile("prod")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private AutenticacaoService autenticacaoService;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private TokenService tokenService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // configuracoes de autenticacao
    auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // configuracoes de autorizacao
    http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/topicos").permitAll()
            .antMatchers(HttpMethod.GET, "/topicos/*").permitAll()
            .antMatchers(HttpMethod.POST, "/auth").permitAll()
            .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
            .antMatchers(HttpMethod.DELETE, "/topicos/*").hasRole("MODERADOR")
            .anyRequest().authenticated()

            //DESABILITA CRSF E DIZ PARA O SPRING QUE VAI USAR CONEXAO STATELESS - QUE UTILIZA S TOKENS E NAO REGISTO DE LOGIN
            .and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    // configuracao de recursos estaticos(js, css, imgs)
    web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
  }

  @Override
  @Bean
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }
}
