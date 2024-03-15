package cat.udl.eps.softarch.demo.config;

import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.repository.SupplierRepository;
import cat.udl.eps.softarch.demo.domain.Supplier;
import cat.udl.eps.softarch.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;

@Configuration
public class AuthenticationConfig extends GlobalAuthenticationConfigurerAdapter {

  @Value("${default-password}")
  String defaultPassword;

  final BasicUserDetailsService basicUserDetailsService;
  final UserRepository userRepository;

  final SupplierRepository supplierRepository;

  public AuthenticationConfig(BasicUserDetailsService basicUserDetailsService, UserRepository userRepository,
                              SupplierRepository supplierRepository) {
    this.basicUserDetailsService = basicUserDetailsService;
    this.userRepository = userRepository;
    this.supplierRepository = supplierRepository;
  }

  @Override
  public void init(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(basicUserDetailsService)
        .passwordEncoder(User.passwordEncoder);


    // Sample User
    if (!userRepository.existsById("demo")) {
      User user = new User();
      user.setEmail("demo@sample.app");
      user.setUsername("demo");
      user.setPassword(defaultPassword);
      user.encodePassword();
      userRepository.save(user);
    }

    if (!supplierRepository.existsById("supplierDemo")) {
      Supplier supplier = new Supplier();
      supplier.setUsername("supplierDemo");
      supplier.setEmail("supplierdemo@sample.app");
      supplier.setPassword(defaultPassword);
      supplier.encodePassword();
      supplierRepository.save(supplier);
    }
  }
}
