package io.playce.migrator.domain.authentication.service;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dao.entity.UserAccess;
import io.playce.migrator.dao.repository.UserAccessRepository;
import io.playce.migrator.dto.authentication.SecurityUser;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccessRepository userAccessRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccess> optionalUserAccess= userAccessRepository.findById(username);
        if (optionalUserAccess.isEmpty()) {
            throw new PlayceMigratorException(ErrorCode.PM501A);
        }

        UserAccess userAccess = optionalUserAccess.get();
        SecurityUser securityUser = modelMapper.map(userAccess, SecurityUser.class);
        securityUser.setUserLoginId(userAccess.getLoginId());
        securityUser.setUsername(userAccess.getUserName());
        securityUser.setPassword(userAccess.getPassword());
        securityUser.setAuthorities(getGrantedAuthorityList(username));

        return securityUser;
    }


    private List<GrantedAuthority> getGrantedAuthorityList(String loginId) {
        List<String> userRoles = List.of("ROLE_ADMIN");
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String userRole : userRoles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(userRole));
        }
        return grantedAuthorities;
    }

    @Transactional
    public void successLogin(String userId) {
        Optional<UserAccess> optionalUserAccess= userAccessRepository.findById(userId);
        if (optionalUserAccess.isPresent()) {
            log.debug("login Success!");
            // TODO Something for login Success
//            userAccess.setLoginFailCnt(0);
//            userAccess.setBlockYn(CommonConstants.NO);
//            userAccess.setLastLoginDatetime(new Date());
//
//            userAccessRepository.save(userAccess);
        }
    }

    @Transactional
    public void failLogin(String userId) {
        Optional<UserAccess> optionalUserAccess= userAccessRepository.findById(userId);
        if (optionalUserAccess.isEmpty()) {
            log.debug("login Failed!");
            // TODO Something for login Failed
//            Integer loginFailCnt = userAccess.getLoginFailCnt() + 1;
//
//            if (loginFailCnt >= MAX_FAIL_COUNT) {
//                userAccess.setBlockYn(CommonConstants.YES);
//            }
//
//            userAccess.setLoginFailCnt(loginFailCnt);
//            userAccessRepository.save(userAccess);
        }
    }
}
