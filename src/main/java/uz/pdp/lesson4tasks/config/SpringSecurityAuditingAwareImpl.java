package uz.pdp.lesson4tasks.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.lesson4tasks.entity.User;

import java.util.Optional;
import java.util.UUID;

public class SpringSecurityAuditingAwareImpl implements AuditorAware<UUID> {


    @Override
    public Optional<UUID> getCurrentAuditor() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")) {
            final User user = (User) authentication.getPrincipal();

            return Optional.of(user.getId());
        }
        return Optional.empty();
    }
}
